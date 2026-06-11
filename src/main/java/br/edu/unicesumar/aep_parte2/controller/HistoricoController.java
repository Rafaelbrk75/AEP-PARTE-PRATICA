package br.edu.unicesumar.aep_parte2.controller;

import br.edu.unicesumar.aep_parte2.domain.dto.HistoricoStatusResponse;
import br.edu.unicesumar.aep_parte2.service.HistoricoService;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api/historico", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Historico", description = "Historico de movimentacoes das solicitacoes")
public class HistoricoController {

    private final HistoricoService historicoService;

    @Operation(summary = "Lista historico de uma solicitacao")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Historico listado"),
            @ApiResponse(responseCode = "401", description = "Token ausente ou invalido"),
            @ApiResponse(responseCode = "403", description = "Usuario sem permissao"),
            @ApiResponse(responseCode = "404", description = "Solicitacao nao encontrada")
    })
    @GetMapping("/solicitacoes/{id}")
    public ResponseEntity<List<HistoricoStatusResponse>> listarHistorico(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {

        return ResponseEntity.ok(historicoService.listarPorSolicitacao(id, userDetails.getUsername()));
    }
}
