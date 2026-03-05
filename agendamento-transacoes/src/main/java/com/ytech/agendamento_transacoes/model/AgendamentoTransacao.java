package com.ytech.agendamento_transacoes.model;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;


@Entity
@Table(name = "agendamentos_transacoes")
@Getter
@Setter
@EqualsAndHashCode

public class AgendamentoTransacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String contaOrigem;

    @Column(nullable = false)
    private String contaDestino;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal taxa;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal valor;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal valorTotal;

    @Column(nullable = false , updatable = false)
    private LocalDate dataAgendamento;

    @Enumerated(EnumType.STRING)
    private StatusTransacao statusTransacao;


    public enum StatusTransacao {
        CRIADA,
        PENDENTE,
        ACEITE,
        CANCELADA
    }
}
