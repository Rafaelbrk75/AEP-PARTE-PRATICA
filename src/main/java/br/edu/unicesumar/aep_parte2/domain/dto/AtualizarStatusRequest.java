package br.edu.unicesumar.aep_parte2.domain.dto;

import br.edu.unicesumar.aep_parte2.domain.enums.StatusSolicitacao;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AtualizarStatusRequest {
    @NotNull(message = "Novo status é obrigatório")
    private StatusSolicitacao novoStatus;

    @NotBlank(message = "Observação é obrigatória")
    private String observacao;
}
