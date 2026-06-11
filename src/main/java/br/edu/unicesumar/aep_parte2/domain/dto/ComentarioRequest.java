package br.edu.unicesumar.aep_parte2.domain.dto;

import jakarta.validation.constraints.NotBlank;

public record ComentarioRequest(
        @NotBlank(message = "Comentario e obrigatorio")
        String mensagem
) {
}
