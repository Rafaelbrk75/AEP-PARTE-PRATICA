package br.edu.unicesumar.aep_parte2.controller;

import br.edu.unicesumar.aep_parte2.domain.dto.SolicitacaoRequest;
import br.edu.unicesumar.aep_parte2.domain.dto.SolicitacaoResponse;
import br.edu.unicesumar.aep_parte2.service.SolicitacaoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/solicitacoes")
@RequiredArgsConstructor
public class SolicitacaoController {

    private final SolicitacaoService solicitacaoService;

    // Cidadão abre uma nova solicitação
    @PostMapping
    public ResponseEntity<SolicitacaoResponse> abrir(
            @RequestBody @Valid SolicitacaoRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(solicitacaoService.abrir(request));
    }

    // Cidadão consulta pelo protocolo
    @GetMapping("/{protocolo}")
    public ResponseEntity<SolicitacaoResponse> buscarPorProtocolo(
            @PathVariable String protocolo) {

        return ResponseEntity.ok(solicitacaoService.buscarPorProtocolo(protocolo));
    }

    // Filtra por categoria
    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<List<SolicitacaoResponse>> listarPorCategoria(
            @PathVariable String categoria) {

        return ResponseEntity.ok(solicitacaoService.listarPorCategoria(categoria));
    }

    // Filtra por bairro/localização
    @GetMapping("/bairro")
    public ResponseEntity<List<SolicitacaoResponse>> filtrarPorBairro(
            @RequestParam String q) {

        return ResponseEntity.ok(solicitacaoService.filtrarPorBairro(q));
    }
}