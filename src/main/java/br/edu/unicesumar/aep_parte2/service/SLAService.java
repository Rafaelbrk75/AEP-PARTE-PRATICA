package br.edu.unicesumar.aep_parte2.service;

import br.edu.unicesumar.aep_parte2.domain.entity.SolicitacaoModel;
import br.edu.unicesumar.aep_parte2.domain.enums.StatusSolicitacao;
import br.edu.unicesumar.aep_parte2.repository.SolicitacaoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SLAService {

    private final SolicitacaoRepository solicitacaoRepository;

    // Roda automaticamente todo hora
    @Scheduled(cron = "0 0 * * * *")
    @Transactional
    public void verificarAtrasos() {
        // Busca somente solicitações que ainda estão em aberto
        List<SolicitacaoModel> abertas = solicitacaoRepository
                .findByStatusAtualNotIn(List.of(
                        StatusSolicitacao.ENCERRADA,
                        StatusSolicitacao.CANCELADA,
                        StatusSolicitacao.RESOLVIDA
                ));

        for (SolicitacaoModel solicitacao : abertas) {
            // Calcula o prazo limite baseado na prioridade
            LocalDateTime prazoLimite = solicitacao.getDataCriacao()
                    .plusHours(solicitacao.getPrioridade().getPrazoHoras());

            // Se passou do prazo e ainda não estava marcada como atrasada
            if (LocalDateTime.now().isAfter(prazoLimite) && !solicitacao.getAtrasado()) {
                solicitacao.setAtrasado(true);
                solicitacaoRepository.save(solicitacao);
                log.warn("Solicitação {} marcada como atrasada.", solicitacao.getProtocolo());
            }
        }
    }
}