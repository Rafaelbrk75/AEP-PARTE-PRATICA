package br.edu.unicesumar.aep_parte2.service;

import br.edu.unicesumar.aep_parte2.domain.dto.UsuarioResponse;
import br.edu.unicesumar.aep_parte2.domain.entity.AtendenteModel;
import br.edu.unicesumar.aep_parte2.domain.entity.CidadaoModel;
import br.edu.unicesumar.aep_parte2.domain.entity.Usuario;
import br.edu.unicesumar.aep_parte2.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioResponse buscarUsuarioLogado(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario nao encontrado"));

        return toResponse(usuario);
    }

    private UsuarioResponse toResponse(Usuario usuario) {
        String cpf = usuario instanceof CidadaoModel cidadao ? cidadao.getCpf() : null;
        String matricula = usuario instanceof AtendenteModel atendente ? atendente.getMatricula() : null;

        return new UsuarioResponse(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getRole(),
                cpf,
                matricula
        );
    }
}
