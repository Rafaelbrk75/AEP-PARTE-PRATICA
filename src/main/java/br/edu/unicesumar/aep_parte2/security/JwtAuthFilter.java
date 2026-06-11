package br.edu.unicesumar.aep_parte2.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        return path.startsWith("/h2-console") ||
                path.startsWith("/api/auth") ||
                path.startsWith("/v3/api-docs") ||
                path.startsWith("/swagger-ui");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // 1 - Pega o header Authorization
        String authHeader = request.getHeader("Authorization");

        // 2 - Se não tem token ou não começa com Bearer, deixa passar
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 3 - Extrai o token removendo o prefixo "Bearer "
        String token = authHeader.substring(7);

        try {
            // 4 - Extrai o email do token
            String email = jwtUtil.extrairLogin(token);

            // 5 - Se tem email e ainda não está autenticado no contexto
            if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                // 6 - Carrega o usuário do banco
                UserDetails userDetails = userDetailsService.loadUserByUsername(email);

                // 7 - Valida o token
                if (jwtUtil.isTokenValido(token, userDetails)) {

                    // 8 - Cria o objeto de autenticação e popula o SecurityContext
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );

                    authToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        } catch (Exception e) {
            // Token inválido ou expirado — loga e deixa o Spring Security
            // retornar 401 automaticamente
            log.error("Token inválido: {}", e.getMessage());
        }

        // 9 - Continua a cadeia de filtros
        filterChain.doFilter(request, response);
    }
}