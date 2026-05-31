package br.edu.unicesumar.aep_parte2.domain.enums;

import java.util.List;

public enum StatusSolicitacao {
    ABERTA,
    EM_ANALISE,
    EM_ANDAMENTO,
    RESOLVIDA,
    ENCERRADA,
    CANCELADA;

    public List<StatusSolicitacao> proximosValidos() {
        return switch (this) {
            case ABERTA       -> List.of(EM_ANALISE, CANCELADA);
            case EM_ANALISE   -> List.of(EM_ANDAMENTO, CANCELADA);
            case EM_ANDAMENTO -> List.of(RESOLVIDA, CANCELADA);
            case RESOLVIDA    -> List.of(ENCERRADA);
            case ENCERRADA    -> List.of();
            case CANCELADA    -> List.of();
        };
    }

    public boolean podeTransicionarPara(StatusSolicitacao novoStatus) {
        return proximosValidos().contains(novoStatus);
    }
}
