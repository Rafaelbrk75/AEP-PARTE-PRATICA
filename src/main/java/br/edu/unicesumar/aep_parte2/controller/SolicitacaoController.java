package br.edu.unicesumar.aep_parte2.controller;

import br.edu.unicesumar.aep_parte2.domain.dto.SolicitacaoResponse;
import br.edu.unicesumar.aep_parte2.service.SolicitacaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api/solicitacoes", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Tag(name = "Solicitacoes", description = "Consultas publicas de solicitacoes")
public class SolicitacaoController {

    private final SolicitacaoService solicitacaoService;

    @Operation(summary = "Busca solicitacao por protocolo")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Solicitacao encontrada"),
            @ApiResponse(responseCode = "404", description = "Protocolo nao encontrado")
    })
    @GetMapping("/protocolo/{protocolo}")
    public ResponseEntity<SolicitacaoResponse> buscarPorProtocolo(@PathVariable String protocolo) {
        return ResponseEntity.ok(solicitacaoService.buscarPorProtocolo(protocolo));
    }

    @Operation(summary = "Lista solicitacoes por categoria")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Solicitacoes listadas"),
            @ApiResponse(responseCode = "400", description = "Categoria invalida")
    })
    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<List<SolicitacaoResponse>> listarPorCategoria(@PathVariable String categoria) {
        return ResponseEntity.ok(solicitacaoService.listarPorCategoria(categoria));
    }

    @Operation(summary = "Filtra solicitacoes por bairro ou localizacao")
    @ApiResponse(responseCode = "200", description = "Solicitacoes listadas")
    @GetMapping("/bairro")
    public ResponseEntity<List<SolicitacaoResponse>> filtrarPorBairro(@RequestParam String q) {
        return ResponseEntity.ok(solicitacaoService.filtrarPorBairro(q));
    }
}
