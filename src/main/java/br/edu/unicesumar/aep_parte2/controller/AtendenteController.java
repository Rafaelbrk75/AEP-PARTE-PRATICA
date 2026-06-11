package br.edu.unicesumar.aep_parte2.controller;

import br.edu.unicesumar.aep_parte2.domain.dto.AtualizarStatusRequest;
import br.edu.unicesumar.aep_parte2.domain.dto.SolicitacaoResponse;
import br.edu.unicesumar.aep_parte2.domain.enums.Categoria;
import br.edu.unicesumar.aep_parte2.domain.enums.Prioridade;
import br.edu.unicesumar.aep_parte2.domain.enums.StatusSolicitacao;
import br.edu.unicesumar.aep_parte2.service.SolicitacaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/atendente", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ROLE_ATENDENTE')")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Atendimento", description = "Fila e operacoes do atendente")
public class AtendenteController {

    private final SolicitacaoService solicitacaoService;

    @Operation(summary = "Lista fila de atendimento com filtros e paginacao")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Fila listada"),
            @ApiResponse(responseCode = "400", description = "Filtro invalido"),
            @ApiResponse(responseCode = "401", description = "Token ausente ou invalido"),
            @ApiResponse(responseCode = "403", description = "Usuario sem permissao")
    })
    @GetMapping("/solicitacoes")
    public ResponseEntity<Page<SolicitacaoResponse>> listarFila(
            @RequestParam(required = false) StatusSolicitacao status,
            @RequestParam(required = false) Prioridade prioridade,
            @RequestParam(required = false) Categoria categoria,
            @RequestParam(required = false) String bairro,
            @RequestParam(required = false) Boolean atrasado,
            @PageableDefault(size = 20) Pageable pageable) {

        return ResponseEntity.ok(solicitacaoService.listarFilaAtendente(
                status,
                prioridade,
                categoria,
                bairro,
                atrasado,
                pageable
        ));
    }

    @Operation(summary = "Atribui solicitacao ao atendente autenticado")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Solicitacao atribuida"),
            @ApiResponse(responseCode = "401", description = "Token ausente ou invalido"),
            @ApiResponse(responseCode = "403", description = "Usuario sem permissao"),
            @ApiResponse(responseCode = "404", description = "Solicitacao nao encontrada")
    })
    @PatchMapping("/solicitacoes/{id}/atribuir")
    public ResponseEntity<SolicitacaoResponse> atribuir(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {

        return ResponseEntity.ok(solicitacaoService.atribuir(id, userDetails.getUsername()));
    }

    @Operation(summary = "Atualiza status de uma solicitacao")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Status atualizado"),
            @ApiResponse(responseCode = "400", description = "Dados invalidos"),
            @ApiResponse(responseCode = "401", description = "Token ausente ou invalido"),
            @ApiResponse(responseCode = "403", description = "Usuario sem permissao"),
            @ApiResponse(responseCode = "404", description = "Solicitacao nao encontrada"),
            @ApiResponse(responseCode = "409", description = "Transicao de status invalida")
    })
    @PatchMapping(value = "/solicitacoes/{id}/status", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SolicitacaoResponse> atualizarStatus(
            @PathVariable Long id,
            @RequestBody @Valid AtualizarStatusRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {

        return ResponseEntity.ok(
                solicitacaoService.atualizarStatus(id, request, userDetails.getUsername())
        );
    }
}
