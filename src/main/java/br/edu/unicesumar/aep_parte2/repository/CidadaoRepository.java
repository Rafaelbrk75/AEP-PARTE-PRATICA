package br.edu.unicesumar.aep_parte2.repository;

import br.edu.unicesumar.aep_parte2.domain.entity.CidadaoModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CidadaoRepository extends JpaRepository<CidadaoModel, Long> {
    Optional<CidadaoModel> findByCpf(String cpf);
}
