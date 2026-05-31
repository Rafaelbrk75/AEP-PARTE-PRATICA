package br.edu.unicesumar.aep_parte2.domain.dto;


import br.edu.unicesumar.aep_parte2.domain.enums.Categoria;
import br.edu.unicesumar.aep_parte2.domain.enums.Prioridade;
import br.edu.unicesumar.aep_parte2.domain.enums.StatusSolicitacao;

import java.time.LocalDateTime;
import java.util.List;

public record SolicitacaoResponse(
        String protocolo,
        String titulo,
        String descricao,
        String localizacao,
        Categoria categoria,
        StatusSolicitacao statusAtual,
        Prioridade prioridade,
        LocalDateTime dataCriacao,
        Boolean atrasado,
        String nomeSolicitante,
        List<HistoricoStatusResponse> historico
) {
}
