package br.edu.unicesumar.aep_parte2.domain.dto;

import br.edu.unicesumar.aep_parte2.domain.enums.StatusSolicitacao;

import java.time.LocalDateTime;

public record HistoricoStatusResponse(
        StatusSolicitacao statusAnterior,
        StatusSolicitacao statusNovo,
        String observacao,
        String tipoAcao,
        LocalDateTime dataMovimentacao,
        String nomeAtendente
) {
}
