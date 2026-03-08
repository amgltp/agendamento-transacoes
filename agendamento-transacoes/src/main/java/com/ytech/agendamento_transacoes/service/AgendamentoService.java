package com.ytech.agendamento_transacoes.service;

import com.ytech.agendamento_transacoes.model.AgendamentoTransacao;
import com.ytech.agendamento_transacoes.repository.AgendamentoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Optional;


@Service("agendamento-service")
@Primary
@Validated
@Slf4j
public class AgendamentoService {

    @Transactional(
            readOnly = true,
            rollbackFor = Exception.class)
    public Page<AgendamentoTransacao> consultarTodasTransacoes(Pageable pageable){
       Pageable sortedPageable = PageRequest.of(
               pageable.getPageNumber(),
               pageable.getPageSize(),
               Sort.by("dataAgendamento").descending()
       );
       return agendamentoRepository.findAll(sortedPageable);
    }

    @Transactional(
            readOnly = false,
            rollbackFor = Exception.class)
    public void incluir(@Valid AgendamentoTransacao transacao) {
        AgendamentoTransacao t = new AgendamentoTransacao();
        t.setContaOrigem(transacao.getContaOrigem());
        t.setContaDestino(transacao.getContaDestino());
        t.setValor(transacao.getValor());

        BigDecimal taxa = taxarTransacao(transacao.getValor(), transacao.getDataAgendamento());
        t.setValorTotal(taxa);

        t.setDataAgendamento(transacao.getDataAgendamento());
        t.setStatusTransacao(AgendamentoTransacao.StatusTransacao.ACEITE);
        agendamentoRepository.save(t);
    }

    private AgendamentoRepository agendamentoRepository;
    @Autowired
    public void setAgendamentoRepository(AgendamentoRepository repository){
        this.agendamentoRepository = repository;
    }

    public BigDecimal taxarTransacao(BigDecimal valor, LocalDate dataAgendamento) {

        LocalDate dataAtual = LocalDate.now();
        long dias = ChronoUnit.DAYS.between(dataAtual, dataAgendamento);

        if (valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Valor da transação inválido");
        }

        if (valor.compareTo(AgendamentoTransacao.LIMITE_1000) <= 0) {
            log.info("Taxa A - 0 a 1000");
            if (dias == 0) {
                BigDecimal taxa = valor.multiply(new BigDecimal("0.03"));
                return taxa.add(new BigDecimal("3"));
            }
            return BigDecimal.ZERO;
        }

        if (valor.compareTo(AgendamentoTransacao.LIMITE_2000) <= 0) {
            log.info("Taxa B - 1001 a 2000");
            if (dias >= 1 && dias <= 10) {
                return valor.multiply(new BigDecimal("0.09"));
            }
            return BigDecimal.ZERO;
        }

        log.info("Taxa C - 2000+");

        if (dias >= 11 && dias <= 20) {
            return valor.multiply(new BigDecimal("0.082"));
        }

        if (dias >= 21 && dias <= 30) {
            return valor.multiply(new BigDecimal("0.069"));
        }

        if (dias >= 31 && dias <= 40) {
            return valor.multiply(new BigDecimal("0.047"));
        }

        if (dias > 40) {
            return valor.multiply(new BigDecimal("0.017"));
        }

        return BigDecimal.ZERO;
    }


    public Optional<AgendamentoTransacao> pesquisarPorId(long id) {
        return agendamentoRepository.findById(id);
    }
}
