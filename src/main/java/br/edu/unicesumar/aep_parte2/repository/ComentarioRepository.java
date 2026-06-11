package br.edu.unicesumar.aep_parte2.repository;

import br.edu.unicesumar.aep_parte2.domain.entity.ComentarioModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ComentarioRepository extends JpaRepository<ComentarioModel, Long> {
}
