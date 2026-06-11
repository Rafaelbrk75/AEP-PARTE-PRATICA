package br.edu.unicesumar.aep_parte2.service;

import br.edu.unicesumar.aep_parte2.domain.dto.AnexoRequest;
import br.edu.unicesumar.aep_parte2.domain.dto.AnexoResponse;
import br.edu.unicesumar.aep_parte2.domain.dto.ComentarioRequest;
import br.edu.unicesumar.aep_parte2.domain.dto.ComentarioResponse;
import br.edu.unicesumar.aep_parte2.domain.dto.SolicitacaoRequest;
import br.edu.unicesumar.aep_parte2.domain.dto.SolicitacaoResponse;
import br.edu.unicesumar.aep_parte2.domain.entity.CidadaoModel;
import br.edu.unicesumar.aep_parte2.domain.entity.SolicitacaoModel;
import br.edu.unicesumar.aep_parte2.exception.SolicitacaoNaoEncontradaException;
import br.edu.unicesumar.aep_parte2.mapper.SolicitacaoMapper;
import br.edu.unicesumar.aep_parte2.repository.CidadaoRepository;
import br.edu.unicesumar.aep_parte2.repository.SolicitacaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CidadaoService {

    private final SolicitacaoService solicitacaoService;
    private final CidadaoRepository cidadaoRepository;
    private final SolicitacaoRepository solicitacaoRepository;
    private final SolicitacaoMapper solicitacaoMapper;

    public SolicitacaoResponse abrirSolicitacao(SolicitacaoRequest request, String emailCidadao) {
        return solicitacaoService.abrir(request, emailCidadao);
    }

    public List<SolicitacaoResponse> listarMinhasSolicitacoes(String emailCidadao) {
        return solicitacaoService.listarMinhas(emailCidadao);
    }

    public SolicitacaoResponse buscarMinhaSolicitacao(Long id, String emailCidadao) {
        return solicitacaoMapper.toResponse(buscarSolicitacaoDoCidadao(id, emailCidadao));
    }

    public ComentarioResponse comentarMinhaSolicitacao(
            Long id,
            ComentarioRequest request,
            String emailCidadao) {

        buscarSolicitacaoDoCidadao(id, emailCidadao);
        return solicitacaoService.comentar(id, request, emailCidadao);
    }

    public AnexoResponse anexarMinhaSolicitacao(
            Long id,
            AnexoRequest request,
            String emailCidadao) {

        buscarSolicitacaoDoCidadao(id, emailCidadao);
        return solicitacaoService.anexar(id, request, emailCidadao);
    }

    private SolicitacaoModel buscarSolicitacaoDoCidadao(Long id, String emailCidadao) {
        CidadaoModel cidadao = cidadaoRepository.findByEmail(emailCidadao)
                .orElseThrow(() -> new RuntimeException("Cidadao nao encontrado"));

        SolicitacaoModel solicitacao = solicitacaoRepository.findById(id)
                .orElseThrow(() -> new SolicitacaoNaoEncontradaException(
                        "Solicitacao nao encontrada: " + id));

        if (solicitacao.getSolicitante() == null
                || !Objects.equals(solicitacao.getSolicitante().getId(), cidadao.getId())) {
            throw new SolicitacaoNaoEncontradaException("Solicitacao nao encontrada: " + id);
        }

        return solicitacao;
    }
}
