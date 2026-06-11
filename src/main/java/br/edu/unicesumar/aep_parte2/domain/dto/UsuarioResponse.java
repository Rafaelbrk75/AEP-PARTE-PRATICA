package br.edu.unicesumar.aep_parte2.domain.dto;

import br.edu.unicesumar.aep_parte2.domain.enums.RoleUser;

public record UsuarioResponse(
        Long id,
        String nome,
        String email,
        RoleUser role,
        String cpf,
        String matricula
) {
}
