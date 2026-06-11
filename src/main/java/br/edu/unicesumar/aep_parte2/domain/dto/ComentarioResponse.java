package br.edu.unicesumar.aep_parte2.domain.dto;

import java.time.LocalDateTime;

public record ComentarioResponse(
        Long id,
        Long solicitacaoId,
        String mensagem,
        String nomeAutor,
        LocalDateTime dataCriacao
) {
}
