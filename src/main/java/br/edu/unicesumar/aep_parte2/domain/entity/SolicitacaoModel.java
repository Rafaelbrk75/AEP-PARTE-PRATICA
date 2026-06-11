package br.edu.unicesumar.aep_parte2.domain.entity;

import br.edu.unicesumar.aep_parte2.domain.enums.Categoria;
import br.edu.unicesumar.aep_parte2.domain.enums.Prioridade;
import br.edu.unicesumar.aep_parte2.domain.enums.StatusSolicitacao;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "tb_solicitacao")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SolicitacaoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String protocolo;

    @Column(nullable = false)
    private String titulo;

    @Column(nullable = false)
    private String descricao;

    @Column(nullable = false)
    private String localizacao;

    @Enumerated(EnumType.STRING)
    private Categoria categoria;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusSolicitacao statusAtual;

    @Enumerated(EnumType.STRING)
    private Prioridade prioridade;

    @Column(nullable = false)
    private Boolean atrasado = false;

    @Column(nullable = false)
    private LocalDateTime dataCriacao;

    @ManyToOne
    @JoinColumn(name = "cidadao_id")
    private CidadaoModel solicitante;

    @ManyToOne
    @JoinColumn(name = "atendente_responsavel_id")
    private AtendenteModel atendenteResponsavel;

    @OneToMany(mappedBy = "solicitacao", cascade = CascadeType.ALL)
    private List<HistoricoStatusModel> historico;
}
