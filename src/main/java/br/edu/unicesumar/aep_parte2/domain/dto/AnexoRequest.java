package br.edu.unicesumar.aep_parte2.domain.dto;

import jakarta.validation.constraints.NotBlank;

public record AnexoRequest(
        @NotBlank(message = "Nome do arquivo e obrigatorio")
        String nomeArquivo,

        @NotBlank(message = "URL do anexo e obrigatoria")
        String url,

        String tipoConteudo
) {
}
