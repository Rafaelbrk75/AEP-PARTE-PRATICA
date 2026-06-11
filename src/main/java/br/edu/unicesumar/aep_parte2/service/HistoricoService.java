package br.edu.unicesumar.aep_parte2.service;

import br.edu.unicesumar.aep_parte2.domain.dto.HistoricoStatusResponse;
import br.edu.unicesumar.aep_parte2.domain.entity.SolicitacaoModel;
import br.edu.unicesumar.aep_parte2.domain.entity.Usuario;
import br.edu.unicesumar.aep_parte2.domain.enums.RoleUser;
import br.edu.unicesumar.aep_parte2.exception.SolicitacaoNaoEncontradaException;
import br.edu.unicesumar.aep_parte2.mapper.HistoricoStatusMapper;
import br.edu.unicesumar.aep_parte2.repository.HistoricoStatusRepository;
import br.edu.unicesumar.aep_parte2.repository.SolicitacaoRepository;
import br.edu.unicesumar.aep_parte2.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class HistoricoService {

    private final HistoricoStatusRepository historicoStatusRepository;
    private final SolicitacaoRepository solicitacaoRepository;
    private final UsuarioRepository usuarioRepository;
    private final HistoricoStatusMapper historicoStatusMapper;

    public List<HistoricoStatusResponse> listarPorSolicitacao(Long solicitacaoId, String emailUsuario) {
        Usuario usuario = usuarioRepository.findByEmail(emailUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario nao encontrado"));

        SolicitacaoModel solicitacao = solicitacaoRepository.findById(solicitacaoId)
                .orElseThrow(() -> new SolicitacaoNaoEncontradaException(
                        "Solicitacao nao encontrada: " + solicitacaoId));

        validarAcesso(solicitacao, usuario);

        return historicoStatusMapper.toResponseList(
                historicoStatusRepository.findBySolicitacaoIdOrderByDataMovimentacaoDesc(solicitacaoId)
        );
    }

    private void validarAcesso(SolicitacaoModel solicitacao, Usuario usuario) {
        if (usuario.getRole() == RoleUser.ROLE_ATENDENTE) {
            return;
        }

        if (usuario.getRole() == RoleUser.ROLE_CIDADAO
                && solicitacao.getSolicitante() != null
                && Objects.equals(solicitacao.getSolicitante().getId(), usuario.getId())) {
            return;
        }

        throw new SolicitacaoNaoEncontradaException("Solicitacao nao encontrada: " + solicitacao.getId());
    }
}
