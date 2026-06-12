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

    @Scheduled(cron = "0 0 * * * *")
    @Transactional
    public void verificarAtrasos() {
        List<SolicitacaoModel> abertas = solicitacaoRepository
                .findByStatusAtualNotIn(List.of(
                        StatusSolicitacao.ENCERRADA,
                        StatusSolicitacao.CANCELADA,
                        StatusSolicitacao.RESOLVIDA
                ));

        for (SolicitacaoModel solicitacao : abertas) {
            LocalDateTime prazoLimite = prazoLimite(solicitacao);

            if (LocalDateTime.now().isAfter(prazoLimite) && !solicitacao.getAtrasado()) {
                solicitacao.setAtrasado(true);
                solicitacao.setJustificativaAtraso(
                        "Prazo alvo excedido para prioridade " + solicitacao.getPrioridade()
                                + ". Limite previsto: " + prazoLimite
                );
                solicitacaoRepository.save(solicitacao);
                log.warn("Solicitacao {} marcada como atrasada.", solicitacao.getProtocolo());
            }
        }
    }

    private LocalDateTime prazoLimite(SolicitacaoModel solicitacao) {
        if (solicitacao.getPrazoLimite() != null) {
            return solicitacao.getPrazoLimite();
        }

        LocalDateTime prazoLimite = solicitacao.getDataCriacao()
                .plusHours(solicitacao.getPrioridade().getPrazoHoras());
        solicitacao.setPrazoLimite(prazoLimite);
        return prazoLimite;
    }
}
