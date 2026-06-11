package br.edu.unicesumar.aep_parte2.service;

import br.edu.unicesumar.aep_parte2.domain.dto.AtualizarStatusRequest;
import br.edu.unicesumar.aep_parte2.domain.dto.AnexoRequest;
import br.edu.unicesumar.aep_parte2.domain.dto.AnexoResponse;
import br.edu.unicesumar.aep_parte2.domain.dto.ComentarioRequest;
import br.edu.unicesumar.aep_parte2.domain.dto.ComentarioResponse;
import br.edu.unicesumar.aep_parte2.domain.dto.SolicitacaoRequest;
import br.edu.unicesumar.aep_parte2.domain.dto.SolicitacaoResponse;
import br.edu.unicesumar.aep_parte2.domain.entity.AnexoModel;
import br.edu.unicesumar.aep_parte2.domain.entity.AtendenteModel;
import br.edu.unicesumar.aep_parte2.domain.entity.CidadaoModel;
import br.edu.unicesumar.aep_parte2.domain.entity.ComentarioModel;
import br.edu.unicesumar.aep_parte2.domain.entity.HistoricoStatusModel;
import br.edu.unicesumar.aep_parte2.domain.entity.SolicitacaoModel;
import br.edu.unicesumar.aep_parte2.domain.entity.Usuario;
import br.edu.unicesumar.aep_parte2.domain.enums.Categoria;
import br.edu.unicesumar.aep_parte2.domain.enums.Prioridade;
import br.edu.unicesumar.aep_parte2.domain.enums.StatusSolicitacao;
import br.edu.unicesumar.aep_parte2.exception.SolicitacaoNaoEncontradaException;
import br.edu.unicesumar.aep_parte2.exception.TransicaoStatusInvalidaException;
import br.edu.unicesumar.aep_parte2.mapper.SolicitacaoMapper;
import br.edu.unicesumar.aep_parte2.repository.AtendenteRepository;
import br.edu.unicesumar.aep_parte2.repository.CidadaoRepository;
import br.edu.unicesumar.aep_parte2.repository.AnexoRepository;
import br.edu.unicesumar.aep_parte2.repository.ComentarioRepository;
import br.edu.unicesumar.aep_parte2.repository.HistoricoStatusRepository;
import br.edu.unicesumar.aep_parte2.repository.SolicitacaoRepository;
import br.edu.unicesumar.aep_parte2.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SolicitacaoService {

    private static final List<StatusSolicitacao> STATUS_FORA_DA_FILA = List.of(
            StatusSolicitacao.ENCERRADA,
            StatusSolicitacao.CANCELADA,
            StatusSolicitacao.RESOLVIDA
    );

    private final SolicitacaoRepository solicitacaoRepository;
    private final CidadaoRepository cidadaoRepository;
    private final AtendenteRepository atendenteRepository;
    private final UsuarioRepository usuarioRepository;
    private final ComentarioRepository comentarioRepository;
    private final AnexoRepository anexoRepository;
    private final HistoricoStatusRepository historicoStatusRepository;
    private final SolicitacaoMapper solicitacaoMapper;
    private final ProtocoloService protocoloService;

    @Transactional
    public SolicitacaoResponse abrir(SolicitacaoRequest request, String emailSolicitante) {
        CidadaoModel cidadao = buscarCidadaoSolicitante(request, emailSolicitante);

        SolicitacaoModel solicitacao = solicitacaoMapper.toEntity(request);
        solicitacao.setProtocolo(protocoloService.gerar());
        solicitacao.setStatusAtual(StatusSolicitacao.ABERTA);
        solicitacao.setDataCriacao(LocalDateTime.now());
        solicitacao.setAtrasado(false);
        solicitacao.setSolicitante(cidadao);

        return solicitacaoMapper.toResponse(solicitacaoRepository.save(solicitacao));
    }

    public SolicitacaoResponse buscarPorProtocolo(String protocolo) {
        return solicitacaoMapper.toResponse(
                solicitacaoRepository.findByProtocolo(protocolo)
                        .orElseThrow(() -> new SolicitacaoNaoEncontradaException(
                                "Protocolo nao encontrado: " + protocolo))
        );
    }

    public SolicitacaoResponse buscarPorId(Long id) {
        return solicitacaoMapper.toResponse(buscarSolicitacao(id));
    }

    public Page<SolicitacaoResponse> listar(Pageable pageable) {
        return solicitacaoRepository.findAll(pageable).map(solicitacaoMapper::toResponse);
    }

    public List<SolicitacaoResponse> listarMinhas(String emailSolicitante) {
        CidadaoModel cidadao = cidadaoRepository.findByEmail(emailSolicitante)
                .orElseThrow(() -> new RuntimeException("Cidadao nao encontrado"));

        return solicitacaoMapper.toResponseList(solicitacaoRepository.findBySolicitanteId(cidadao.getId()));
    }

    public List<SolicitacaoResponse> listarPorCategoria(String categoria) {
        return solicitacaoMapper.toResponseList(
                solicitacaoRepository.findByCategoria(Categoria.valueOf(categoria))
        );
    }

    public List<SolicitacaoResponse> filtrarPorBairro(String bairro) {
        return solicitacaoMapper.toResponseList(
                solicitacaoRepository.findByLocalizacaoContainingIgnoreCase(bairro)
        );
    }

    public Page<SolicitacaoResponse> listarFilaAtendente(
            StatusSolicitacao status,
            Prioridade prioridade,
            Categoria categoria,
            String bairro,
            Boolean atrasado,
            Pageable pageable) {

        Specification<SolicitacaoModel> spec = null;

        if (status != null) {
            spec = adicionarFiltro(spec, (root, query, cb) -> cb.equal(root.get("statusAtual"), status));
        } else {
            spec = adicionarFiltro(spec, (root, query, cb) -> cb.not(root.get("statusAtual").in(STATUS_FORA_DA_FILA)));
        }

        if (prioridade != null) {
            spec = adicionarFiltro(spec, (root, query, cb) -> cb.equal(root.get("prioridade"), prioridade));
        }

        if (categoria != null) {
            spec = adicionarFiltro(spec, (root, query, cb) -> cb.equal(root.get("categoria"), categoria));
        }

        if (bairro != null && !bairro.isBlank()) {
            String termo = "%" + bairro.toLowerCase() + "%";
            spec = adicionarFiltro(spec, (root, query, cb) -> cb.like(cb.lower(root.get("localizacao")), termo));
        }

        if (atrasado != null) {
            spec = adicionarFiltro(spec, (root, query, cb) -> cb.equal(root.get("atrasado"), atrasado));
        }

        return solicitacaoRepository.findAll(spec, pageable).map(solicitacaoMapper::toResponse);
    }

    @Transactional
    public SolicitacaoResponse atribuir(Long id, String emailAtendente) {
        SolicitacaoModel solicitacao = buscarSolicitacao(id);
        AtendenteModel atendente = buscarAtendente(emailAtendente);

        solicitacao.setAtendenteResponsavel(atendente);
        registrarHistorico(
                solicitacao,
                atendente,
                solicitacao.getStatusAtual(),
                solicitacao.getStatusAtual(),
                "Solicitacao atribuida ao atendente " + atendente.getNome(),
                "ATRIBUICAO"
        );

        return solicitacaoMapper.toResponse(solicitacaoRepository.save(solicitacao));
    }

    @Transactional
    public SolicitacaoResponse atualizarStatus(Long id, AtualizarStatusRequest request, String emailAtendente) {
        SolicitacaoModel solicitacao = buscarSolicitacao(id);

        StatusSolicitacao statusAtual = solicitacao.getStatusAtual();
        if (!statusAtual.podeTransicionarPara(request.getNovoStatus())) {
            throw new TransicaoStatusInvalidaException(
                    String.format("Nao e possivel transicionar de %s para %s",
                            statusAtual, request.getNovoStatus()));
        }

        AtendenteModel atendente = buscarAtendente(emailAtendente);
        solicitacao.setStatusAtual(request.getNovoStatus());

        if (solicitacao.getAtendenteResponsavel() == null) {
            solicitacao.setAtendenteResponsavel(atendente);
        }

        registrarHistorico(
                solicitacao,
                atendente,
                statusAtual,
                request.getNovoStatus(),
                request.getObservacao(),
                "ALTERACAO_STATUS"
        );

        return solicitacaoMapper.toResponse(solicitacaoRepository.save(solicitacao));
    }

    @Transactional
    public ComentarioResponse comentar(Long id, ComentarioRequest request, String emailAutor) {
        SolicitacaoModel solicitacao = buscarSolicitacao(id);
        Usuario autor = buscarUsuario(emailAutor);

        ComentarioModel comentario = new ComentarioModel();
        comentario.setSolicitacao(solicitacao);
        comentario.setAutor(autor);
        comentario.setMensagem(request.mensagem());
        comentario.setDataCriacao(LocalDateTime.now());

        ComentarioModel salvo = comentarioRepository.save(comentario);
        return new ComentarioResponse(
                salvo.getId(),
                solicitacao.getId(),
                salvo.getMensagem(),
                autor.getNome(),
                salvo.getDataCriacao()
        );
    }

    @Transactional
    public AnexoResponse anexar(Long id, AnexoRequest request, String emailAutor) {
        SolicitacaoModel solicitacao = buscarSolicitacao(id);
        Usuario autor = buscarUsuario(emailAutor);

        AnexoModel anexo = new AnexoModel();
        anexo.setSolicitacao(solicitacao);
        anexo.setAutor(autor);
        anexo.setNomeArquivo(request.nomeArquivo());
        anexo.setUrl(request.url());
        anexo.setTipoConteudo(request.tipoConteudo());
        anexo.setDataEnvio(LocalDateTime.now());

        AnexoModel salvo = anexoRepository.save(anexo);
        return new AnexoResponse(
                salvo.getId(),
                solicitacao.getId(),
                salvo.getNomeArquivo(),
                salvo.getUrl(),
                salvo.getTipoConteudo(),
                autor.getNome(),
                salvo.getDataEnvio()
        );
    }

    private CidadaoModel buscarCidadaoSolicitante(SolicitacaoRequest request, String emailSolicitante) {
        if (emailSolicitante != null && !emailSolicitante.isBlank()) {
            return cidadaoRepository.findByEmail(emailSolicitante)
                    .orElseThrow(() -> new RuntimeException("Cidadao nao encontrado"));
        }

        if (request.getCpfSolicitante() != null && !request.getCpfSolicitante().isBlank()) {
            return cidadaoRepository.findByCpf(request.getCpfSolicitante())
                    .orElseThrow(() -> new RuntimeException("Cidadao nao encontrado"));
        }

        throw new RuntimeException("Solicitante nao informado");
    }

    private SolicitacaoModel buscarSolicitacao(Long id) {
        return solicitacaoRepository.findById(id)
                .orElseThrow(() -> new SolicitacaoNaoEncontradaException(
                        "Solicitacao nao encontrada: " + id));
    }

    private AtendenteModel buscarAtendente(String emailAtendente) {
        return atendenteRepository.findByEmail(emailAtendente)
                .orElseThrow(() -> new RuntimeException("Atendente nao encontrado"));
    }

    private Usuario buscarUsuario(String email) {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario nao encontrado"));
    }

    private void registrarHistorico(
            SolicitacaoModel solicitacao,
            AtendenteModel atendente,
            StatusSolicitacao statusAnterior,
            StatusSolicitacao statusNovo,
            String observacao,
            String tipoAcao) {

        HistoricoStatusModel historico = new HistoricoStatusModel();
        historico.setStatusAnterior(statusAnterior);
        historico.setStatusNovo(statusNovo);
        historico.setObservacao(observacao);
        historico.setTipoAcao(tipoAcao);
        historico.setDataMovimentacao(LocalDateTime.now());
        historico.setAtendenteModel(atendente);
        historico.setSolicitacao(solicitacao);
        historicoStatusRepository.save(historico);
    }

    private Specification<SolicitacaoModel> adicionarFiltro(
            Specification<SolicitacaoModel> atual,
            Specification<SolicitacaoModel> novo) {

        return atual == null ? novo : atual.and(novo);
    }
}
