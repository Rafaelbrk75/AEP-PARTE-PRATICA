package br.edu.unicesumar.aep_parte2.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    public static final String BEARER_AUTH = "bearerAuth";

    @Bean
    public OpenAPI observaAcaoOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("ObservaAcao API")
                        .version("v1")
                        .description("API para abertura, acompanhamento e atendimento de solicitacoes urbanas"))
                .components(new Components()
                        .addSecuritySchemes(BEARER_AUTH, new SecurityScheme()
                                .name(BEARER_AUTH)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")))
                .addTagsItem(new Tag().name("Autenticacao").description("Login e emissao de token JWT"))
                .addTagsItem(new Tag().name("Cidadao").description("Operacoes do cidadao autenticado"))
                .addTagsItem(new Tag().name("Solicitacoes").description("Abertura e consulta de solicitacoes"))
                .addTagsItem(new Tag().name("Atendimento").description("Fila e operacoes do atendente"))
                .addTagsItem(new Tag().name("Historico").description("Historico de movimentacoes"))
                .addTagsItem(new Tag().name("Usuarios").description("Usuarios do sistema"));
    }
}
