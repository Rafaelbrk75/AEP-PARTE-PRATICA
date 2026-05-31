package br.edu.unicesumar.aep_parte2.repository;

import br.edu.unicesumar.aep_parte2.domain.entity.AtendenteModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AtendenteRepository extends JpaRepository<AtendenteModel, Long> {
    Optional<AtendenteModel> findByMatricula(String matricula);
}
