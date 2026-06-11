package br.edu.unicesumar.aep_parte2.controller;

import br.edu.unicesumar.aep_parte2.domain.dto.UsuarioResponse;
import br.edu.unicesumar.aep_parte2.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/usuarios", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Usuarios", description = "Dados do usuario autenticado")
public class UsuarioController {

    private final UsuarioService usuarioService;

    @Operation(summary = "Busca dados do usuario autenticado")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuario encontrado"),
            @ApiResponse(responseCode = "401", description = "Token ausente ou invalido"),
            @ApiResponse(responseCode = "403", description = "Usuario sem permissao")
    })
    @GetMapping("/me")
    public ResponseEntity<UsuarioResponse> buscarUsuarioLogado(
            @AuthenticationPrincipal UserDetails userDetails) {

        return ResponseEntity.ok(usuarioService.buscarUsuarioLogado(userDetails.getUsername()));
    }
}
