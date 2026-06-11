package br.edu.unicesumar.aep_parte2.repository;

import br.edu.unicesumar.aep_parte2.domain.entity.SolicitacaoModel;
import br.edu.unicesumar.aep_parte2.domain.enums.Categoria;
import br.edu.unicesumar.aep_parte2.domain.enums.StatusSolicitacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface SolicitacaoRepository extends JpaRepository<SolicitacaoModel, Long>, JpaSpecificationExecutor<SolicitacaoModel> {
    Optional<SolicitacaoModel> findByProtocolo(String protocolo);

    List<SolicitacaoModel> findByCategoria(Categoria categoria);

    // filtra por bairro/localização
    List<SolicitacaoModel> findByLocalizacaoContainingIgnoreCase(String localizacao);

    // lista ordenada por peso da prioridade (atendente vê do mais urgente ao menos)
    List<SolicitacaoModel> findAllByOrderByPrioridadeAsc();

    // busca solicitações em aberto para o SLAService verificar atraso
    List<SolicitacaoModel> findByStatusAtualNotIn(List<StatusSolicitacao> statusExcluidos);

    // busca todas as solicitações de um cidadão específico
    List<SolicitacaoModel> findBySolicitanteId(Long cidadaoId);
}
