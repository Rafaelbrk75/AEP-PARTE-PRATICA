package br.edu.unicesumar.aep_parte2.domain.entity;

import br.edu.unicesumar.aep_parte2.domain.enums.StatusSolicitacao;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_historico")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class HistoricoStatusModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusSolicitacao statusAnterior;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusSolicitacao statusNovo;

    @Column(nullable = false)
    private LocalDateTime dataMovimentacao;

    @ManyToOne
    @JoinColumn(name = "atendente_id", nullable = false)
    private AtendenteModel atendenteModel;

    @Column(nullable = false)
    private String observacao;

    @Column(nullable = false)
    private Boolean canceladoPorCidadao = false;

    @ManyToOne
    @JoinColumn(name = "solicitacao_id", nullable = false)
    private SolicitacaoModel solicitacao;
}
