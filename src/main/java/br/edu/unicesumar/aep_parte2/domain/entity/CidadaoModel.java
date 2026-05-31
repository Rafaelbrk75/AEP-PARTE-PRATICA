package br.edu.unicesumar.aep_parte2.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tb_cidadao")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CidadaoModel extends Usuario{
    @Column(nullable = false)
    private String cpf;
}
