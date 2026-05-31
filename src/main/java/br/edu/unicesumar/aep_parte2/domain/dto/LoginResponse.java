package br.edu.unicesumar.aep_parte2.domain.dto;

public record LoginResponse(
        String token,
        String tipo,
        String nomeUsuario,
        String role
) {
}
