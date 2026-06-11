package br.edu.unicesumar.aep_parte2.repository;

import br.edu.unicesumar.aep_parte2.domain.entity.AnexoModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnexoRepository extends JpaRepository<AnexoModel, Long> {
}
