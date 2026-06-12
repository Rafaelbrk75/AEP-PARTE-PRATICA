package br.edu.unicesumar.aep_parte2.controller;

import br.edu.unicesumar.aep_parte2.domain.dto.AnexoRequest;
import br.edu.unicesumar.aep_parte2.domain.dto.AnexoResponse;
import br.edu.unicesumar.aep_parte2.domain.dto.ComentarioRequest;
import br.edu.unicesumar.aep_parte2.domain.dto.ComentarioResponse;
import br.edu.unicesumar.aep_parte2.domain.dto.SolicitacaoRequest;
import br.edu.unicesumar.aep_parte2.domain.dto.SolicitacaoResponse;
import br.edu.unicesumar.aep_parte2.service.CidadaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping(value = "/api/cidadao", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ROLE_CIDADAO')")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Cidadao", description = "Operacoes do cidadao autenticado")
public class CidadaoController {

    private final CidadaoService cidadaoService;

    @Operation(summary = "Abre uma nova solicitacao")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Solicitacao criada"),
            @ApiResponse(responseCode = "400", description = "Dados invalidos"),
            @ApiResponse(responseCode = "401", description = "Token ausente ou invalido"),
            @ApiResponse(responseCode = "403", description = "Usuario sem permissao")
    })
    @PostMapping(value = "/solicitacoes", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SolicitacaoResponse> abrirSolicitacao(
            @RequestBody @Valid SolicitacaoRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(cidadaoService.abrirSolicitacao(request, userDetails.getUsername()));
    }

    @Operation(summary = "Abre uma nova solicitacao com imagem anexada")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Solicitacao criada"),
            @ApiResponse(responseCode = "400", description = "Dados invalidos"),
            @ApiResponse(responseCode = "401", description = "Token ausente ou invalido"),
            @ApiResponse(responseCode = "403", description = "Usuario sem permissao")
    })
    @PostMapping(value = "/solicitacoes", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<SolicitacaoResponse> abrirSolicitacaoComAnexo(
            @RequestPart("solicitacao") @Valid SolicitacaoRequest request,
            @RequestPart(value = "arquivo", required = false) MultipartFile arquivo,
            @AuthenticationPrincipal UserDetails userDetails) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(cidadaoService.abrirSolicitacaoComAnexo(request, arquivo, userDetails.getUsername()));
    }

    @Operation(summary = "Lista minhas solicitacoes")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Solicitacoes do cidadao listadas"),
            @ApiResponse(responseCode = "401", description = "Token ausente ou invalido"),
            @ApiResponse(responseCode = "403", description = "Usuario sem permissao")
    })
    @GetMapping("/solicitacoes")
    public ResponseEntity<List<SolicitacaoResponse>> listarMinhasSolicitacoes(
            @AuthenticationPrincipal UserDetails userDetails) {

        return ResponseEntity.ok(cidadaoService.listarMinhasSolicitacoes(userDetails.getUsername()));
    }

    @Operation(summary = "Busca uma solicitacao minha por id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Solicitacao encontrada"),
            @ApiResponse(responseCode = "401", description = "Token ausente ou invalido"),
            @ApiResponse(responseCode = "403", description = "Usuario sem permissao"),
            @ApiResponse(responseCode = "404", description = "Solicitacao nao encontrada")
    })
    @GetMapping("/solicitacoes/{id}")
    public ResponseEntity<SolicitacaoResponse> buscarMinhaSolicitacao(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {

        return ResponseEntity.ok(cidadaoService.buscarMinhaSolicitacao(id, userDetails.getUsername()));
    }

    @Operation(summary = "Adiciona comentario em uma solicitacao minha")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Comentario criado"),
            @ApiResponse(responseCode = "400", description = "Dados invalidos"),
            @ApiResponse(responseCode = "401", description = "Token ausente ou invalido"),
            @ApiResponse(responseCode = "403", description = "Usuario sem permissao"),
            @ApiResponse(responseCode = "404", description = "Solicitacao nao encontrada")
    })
    @PostMapping(value = "/solicitacoes/{id}/comentarios", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ComentarioResponse> comentarMinhaSolicitacao(
            @PathVariable Long id,
            @RequestBody @Valid ComentarioRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(cidadaoService.comentarMinhaSolicitacao(id, request, userDetails.getUsername()));
    }

    @Operation(summary = "Adiciona anexo em uma solicitacao minha")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Anexo criado"),
            @ApiResponse(responseCode = "400", description = "Dados invalidos"),
            @ApiResponse(responseCode = "401", description = "Token ausente ou invalido"),
            @ApiResponse(responseCode = "403", description = "Usuario sem permissao"),
            @ApiResponse(responseCode = "404", description = "Solicitacao nao encontrada")
    })
    @PostMapping(value = "/solicitacoes/{id}/anexos", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AnexoResponse> anexarMinhaSolicitacao(
            @PathVariable Long id,
            @RequestBody @Valid AnexoRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(cidadaoService.anexarMinhaSolicitacao(id, request, userDetails.getUsername()));
    }

    @Operation(summary = "Envia imagem para uma solicitacao minha")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Anexo criado"),
            @ApiResponse(responseCode = "400", description = "Arquivo invalido"),
            @ApiResponse(responseCode = "401", description = "Token ausente ou invalido"),
            @ApiResponse(responseCode = "403", description = "Usuario sem permissao"),
            @ApiResponse(responseCode = "404", description = "Solicitacao nao encontrada")
    })
    @PostMapping(value = "/solicitacoes/{id}/anexos", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AnexoResponse> anexarArquivoMinhaSolicitacao(
            @PathVariable Long id,
            @RequestPart("arquivo") MultipartFile arquivo,
            @AuthenticationPrincipal UserDetails userDetails) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(cidadaoService.anexarArquivoMinhaSolicitacao(id, arquivo, userDetails.getUsername()));
    }
}
