package br.edu.unicesumar.aep_parte2.repository;

import br.edu.unicesumar.aep_parte2.domain.entity.HistoricoStatusModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface HistoricoStatusRepository extends JpaRepository<HistoricoStatusModel, Long> {

    List<HistoricoStatusModel> findBySolicitacaoIdOrderByDataMovimentacaoDesc(Long solicitacaoId);
}
