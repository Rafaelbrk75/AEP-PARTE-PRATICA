package br.edu.unicesumar.aep_parte2.domain.dto;

import java.time.LocalDateTime;

public record AnexoResponse(
        Long id,
        Long solicitacaoId,
        String nomeArquivo,
        String url,
        String tipoConteudo,
        String nomeAutor,
        LocalDateTime dataEnvio
) {
}
