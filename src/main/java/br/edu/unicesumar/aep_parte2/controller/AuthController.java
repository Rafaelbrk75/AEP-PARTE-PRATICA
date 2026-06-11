package br.edu.unicesumar.aep_parte2.controller;

import br.edu.unicesumar.aep_parte2.domain.dto.LoginRequest;
import br.edu.unicesumar.aep_parte2.domain.dto.LoginResponse;
import br.edu.unicesumar.aep_parte2.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/auth", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Tag(name = "Autenticacao", description = "Login e emissao de token JWT")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "Autentica usuario")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Login realizado"),
            @ApiResponse(responseCode = "400", description = "Dados invalidos"),
            @ApiResponse(responseCode = "401", description = "Credenciais invalidas")
    })
    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest request) {
        return ResponseEntity.ok(authService.autenticar(request));
    }
}
