package br.edu.unicesumar.aep_parte2.service;

import br.edu.unicesumar.aep_parte2.domain.dto.AtualizarStatusRequest;
import br.edu.unicesumar.aep_parte2.domain.dto.SolicitacaoRequest;
import br.edu.unicesumar.aep_parte2.domain.dto.SolicitacaoResponse;
import br.edu.unicesumar.aep_parte2.domain.entity.AtendenteModel;
import br.edu.unicesumar.aep_parte2.domain.entity.HistoricoStatusModel;
import br.edu.unicesumar.aep_parte2.domain.entity.SolicitacaoModel;
import br.edu.unicesumar.aep_parte2.domain.enums.StatusSolicitacao;
import br.edu.unicesumar.aep_parte2.exception.SolicitacaoNaoEncontradaException;
import br.edu.unicesumar.aep_parte2.exception.TransicaoStatusInvalidaException;
import br.edu.unicesumar.aep_parte2.mapper.SolicitacaoMapper;
import br.edu.unicesumar.aep_parte2.repository.AtendenteRepository;
import br.edu.unicesumar.aep_parte2.repository.CidadaoRepository;
import br.edu.unicesumar.aep_parte2.repository.HistoricoStatusRepository;
import br.edu.unicesumar.aep_parte2.repository.SolicitacaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SolicitacaoService {

    private final SolicitacaoRepository solicitacaoRepository;
    private final CidadaoRepository cidadaoRepository;
    private final AtendenteRepository atendenteRepository;
    private final HistoricoStatusRepository historicoStatusRepository;
    private final SolicitacaoMapper solicitacaoMapper;
    private final ProtocoloService protocoloService;

    @Transactional
    public SolicitacaoResponse abrir(SolicitacaoRequest request) {

        // 1 - Busca o cidadão pelo CPF
        var cidadao = cidadaoRepository.findByCpf(request.getCpfSolicitante())
                .orElseThrow(() -> new RuntimeException("Cidadão não encontrado"));

        // 2 - Converte request para entidade
        SolicitacaoModel solicitacao = solicitacaoMapper.toEntity(request);

        // 3 - Preenche campos que o mapper ignorou
        solicitacao.setProtocolo(protocoloService.gerar());
        solicitacao.setStatusAtual(StatusSolicitacao.ABERTA);
        solicitacao.setDataCriacao(LocalDateTime.now());
        solicitacao.setAtrasado(false);
        solicitacao.setSolicitante(cidadao);

        // 4 - Salva e retorna
        return solicitacaoMapper.toResponse(solicitacaoRepository.save(solicitacao));
    }

    public SolicitacaoResponse buscarPorProtocolo(String protocolo) {
        return solicitacaoMapper.toResponse(
                solicitacaoRepository.findByProtocolo(protocolo)
                        .orElseThrow(() -> new SolicitacaoNaoEncontradaException(
                                "Protocolo não encontrado: " + protocolo))
        );
    }

    public List<SolicitacaoResponse> listarPorCategoria(String categoria) {
        return solicitacaoMapper.toResponseList(
                solicitacaoRepository.findByCategoria(
                        br.edu.unicesumar.aep_parte2.domain.enums.Categoria.valueOf(categoria))
        );
    }

    public List<SolicitacaoResponse> filtrarPorBairro(String bairro) {
        return solicitacaoMapper.toResponseList(
                solicitacaoRepository.findByLocalizacaoContainingIgnoreCase(bairro)
        );
    }

    @Transactional
    public SolicitacaoResponse atualizarStatus(Long id, AtualizarStatusRequest request, String emailAtendente) {

        // 1 - Busca a solicitação
        SolicitacaoModel solicitacao = solicitacaoRepository.findById(id)
                .orElseThrow(() -> new SolicitacaoNaoEncontradaException(
                        "Solicitação não encontrada: " + id));

        // 2 - Valida se a transição é permitida pelo enum
        StatusSolicitacao statusAtual = solicitacao.getStatusAtual();
        if (!statusAtual.podeTransicionarPara(request.getNovoStatus())) {
            throw new TransicaoStatusInvalidaException(
                    String.format("Não é possível transicionar de %s para %s",
                            statusAtual, request.getNovoStatus()));
        }

        // 3 - Busca o atendente pelo email
        AtendenteModel atendente = (AtendenteModel) atendenteRepository
                .findByMatricula(emailAtendente)
                .orElseThrow(() -> new RuntimeException("Atendente não encontrado"));

        // 4 - Registra o histórico
        HistoricoStatusModel historico = new HistoricoStatusModel();
        historico.setStatusAnterior(statusAtual);
        historico.setStatusNovo(request.getNovoStatus());
        historico.setObservacao(request.getObservacao());
        historico.setDataMovimentacao(LocalDateTime.now());
        historico.setAtendenteModel(atendente);
        historico.setSolicitacao(solicitacao);
        historicoStatusRepository.save(historico);

        // 5 - Atualiza o status e salva
        solicitacao.setStatusAtual(request.getNovoStatus());
        return solicitacaoMapper.toResponse(solicitacaoRepository.save(solicitacao));
    }
}