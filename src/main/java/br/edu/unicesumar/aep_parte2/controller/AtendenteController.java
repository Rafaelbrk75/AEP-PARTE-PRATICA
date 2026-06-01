package br.edu.unicesumar.aep_parte2.controller;

import br.edu.unicesumar.aep_parte2.domain.dto.AtualizarStatusRequest;
import br.edu.unicesumar.aep_parte2.domain.dto.SolicitacaoResponse;
import br.edu.unicesumar.aep_parte2.service.FilaAtendimentoService;
import br.edu.unicesumar.aep_parte2.service.SolicitacaoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/atendente")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ROLE_ATENDENTE')") // protege todos os endpoints da classe
public class AtendenteController {

    private final SolicitacaoService solicitacaoService;
    private final FilaAtendimentoService filaAtendimentoService;

    // Lista fila ordenada por prioridade
    @GetMapping("/solicitacoes")
    public ResponseEntity<List<SolicitacaoResponse>> listarFila() {
        return ResponseEntity.ok(filaAtendimentoService.listarFila());
    }

    // Atualiza o status de uma solicitação
    @PatchMapping("/solicitacoes/{id}/status")
    public ResponseEntity<SolicitacaoResponse> atualizarStatus(
            @PathVariable Long id,
            @RequestBody @Valid AtualizarStatusRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        // @AuthenticationPrincipal pega o usuário logado automaticamente
        // do SecurityContext — sem precisar passar no body

        return ResponseEntity.ok(
                solicitacaoService.atualizarStatus(id, request, userDetails.getUsername())
        );
    }
}