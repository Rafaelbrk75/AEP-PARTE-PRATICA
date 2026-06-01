package br.edu.unicesumar.aep_parte2.service;

import br.edu.unicesumar.aep_parte2.domain.dto.SolicitacaoResponse;
import br.edu.unicesumar.aep_parte2.domain.enums.StatusSolicitacao;
import br.edu.unicesumar.aep_parte2.mapper.SolicitacaoMapper;
import br.edu.unicesumar.aep_parte2.repository.SolicitacaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FilaAtendimentoService {

    private final SolicitacaoRepository solicitacaoRepository;
    private final SolicitacaoMapper solicitacaoMapper;

    public List<SolicitacaoResponse> listarFila() {
        // Busca só as abertas/em andamento ordenadas por prioridade
        return solicitacaoMapper.toResponseList(
                solicitacaoRepository.findByStatusAtualNotIn(List.of(
                                StatusSolicitacao.ENCERRADA,
                                StatusSolicitacao.CANCELADA,
                                StatusSolicitacao.RESOLVIDA
                        ))
                        .stream()
                        .sorted((a, b) -> Integer.compare(
                                a.getPrioridade().getPeso(),
                                b.getPrioridade().getPeso()
                        ))
                        .toList()
        );
    }
}