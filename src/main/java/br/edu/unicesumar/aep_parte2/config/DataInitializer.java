package br.edu.unicesumar.aep_parte2.config;

import br.edu.unicesumar.aep_parte2.domain.entity.AtendenteModel;
import br.edu.unicesumar.aep_parte2.domain.entity.CidadaoModel;
import br.edu.unicesumar.aep_parte2.domain.enums.RoleUser;
import br.edu.unicesumar.aep_parte2.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner initData() {
        return args -> {
            if (usuarioRepository.count() > 0) {
                log.info("Banco já populado — pulando inicialização.");
                return;
            }

            // Cidadão
            CidadaoModel cidadao = new CidadaoModel();
            cidadao.setNome("João Silva");
            cidadao.setEmail("cidadao@teste.com");
            cidadao.setPassword(passwordEncoder.encode("123456"));
            cidadao.setRole(RoleUser.ROLE_CIDADAO);
            cidadao.setCpf("12345678901");
            usuarioRepository.save(cidadao);

            // Atendente
            AtendenteModel atendente = new AtendenteModel();
            atendente.setNome("Maria Atendente");
            atendente.setEmail("atendente@teste.com");
            atendente.setPassword(passwordEncoder.encode("123456"));
            atendente.setRole(RoleUser.ROLE_ATENDENTE);
            atendente.setMatricula("MAT-001");
            usuarioRepository.save(atendente);

            log.info("✅ Usuários de teste criados:");
            log.info("   Cidadão  → cidadao@teste.com  / 123456");
            log.info("   Atendente → atendente@teste.com / 123456");

            log.info(">>> Usuarios criados. Total: {}", usuarioRepository.count());
        };


    }
}