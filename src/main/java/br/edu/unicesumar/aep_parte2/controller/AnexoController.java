package br.edu.unicesumar.aep_parte2.controller;

import br.edu.unicesumar.aep_parte2.service.SolicitacaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/api/anexos")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Anexos", description = "Arquivos anexados as solicitacoes")
public class AnexoController {

    private final SolicitacaoService solicitacaoService;

    @Operation(summary = "Baixa o arquivo de um anexo")
    @GetMapping("/{id}/arquivo")
    public ResponseEntity<Resource> baixarArquivo(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {

        SolicitacaoService.AnexoArquivo anexo = solicitacaoService.carregarArquivoAnexo(
                id,
                userDetails.getUsername()
        );
        MediaType mediaType = anexo.tipoConteudo() == null
                ? MediaType.APPLICATION_OCTET_STREAM
                : MediaType.parseMediaType(anexo.tipoConteudo());

        return ResponseEntity.ok()
                .contentType(mediaType)
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.inline()
                                .filename(anexo.nomeArquivo(), StandardCharsets.UTF_8)
                                .build()
                                .toString()
                )
                .body(anexo.resource());
    }
}
