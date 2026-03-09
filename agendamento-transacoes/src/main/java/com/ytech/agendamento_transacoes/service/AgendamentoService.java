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
import java.util.List;
import java.util.Optional;


@Service("agendamento-service")
@Primary
@Validated
@Slf4j
public class AgendamentoService {

    @Transactional(
            readOnly = true,
            rollbackFor = Exception.class)
    public List<AgendamentoTransacao> consultarTodasTransacoes(){
        return agendamentoRepository.findAll(
                Sort.by("dataAgendamento").descending()
        );
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
        t.setValorTotal(taxa.add(transacao.getValor()));

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
        System.out.println("Data Atual: " + dataAtual + "\n" + "DataAgendamento: " +dataAgendamento);

        long dias = ChronoUnit.DAYS.between(dataAtual, dataAgendamento);
        log.info("Dias: " + dias);

        if (valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Valor da transação inválido");
        }

        if (valor.compareTo(AgendamentoTransacao.LIMITE_1000) <= 0) {
            log.info("Taxa A - 0 a 1000");
            if (dataAtual.isEqual(dataAgendamento)) {
                BigDecimal taxa = valor.multiply(new BigDecimal("0.03"));
                System.out.println("Valor taxa A:" +  taxa);
                return taxa.add(new BigDecimal("3"));
            }
            return BigDecimal.ZERO;
        }

        if (valor.compareTo(AgendamentoTransacao.LIMITE_2000) <= 0) {
            log.info("Taxa B - 1001 a 2000");
            if (dias >= 1 && dias <= 10) {
                BigDecimal taxa = valor.multiply(new BigDecimal("0.09"));
                System.out.println("Valor taxa B:" +  taxa);
                return taxa;
            }
            return BigDecimal.ZERO;
        }

        log.info("Taxa C - 2000+");

        if (dias >= 11 && dias <= 20) {
            BigDecimal taxa = valor.multiply(new BigDecimal("0.082"));
            System.out.println("Valor taxa C:" +  taxa);
            return taxa;
        }

        if (dias >= 21 && dias <= 30) {
            BigDecimal taxa = valor.multiply(new BigDecimal("0.069"));
            System.out.println("Valor taxa C:" +  taxa);
            return taxa;
        }

        if (dias >= 31 && dias <= 40) {
            BigDecimal taxa = valor.multiply(new BigDecimal("0.047"));
            System.out.println("Valor taxa C:" +  taxa);
            return taxa;
        }

        if (dias > 40) {
            BigDecimal taxa = valor.multiply(new BigDecimal("0.017"));
            System.out.println("Valor taxa C:" +  taxa);
            return taxa;
        }

        return BigDecimal.ZERO;
    }


    public Optional<AgendamentoTransacao> pesquisarPorId(long id) {
        return agendamentoRepository.findById(id);
    }

    @Transactional(
            readOnly = false,
            rollbackFor = Exception.class)
    public void alterarTransacao(long id, @Valid AgendamentoTransacao transacao) {

        AgendamentoTransacao agendamentoTransacaoExistente = agendamentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Agendamento não encontrado"));

        agendamentoTransacaoExistente.setContaOrigem(transacao.getContaOrigem());
        agendamentoTransacaoExistente.setContaDestino(transacao.getContaDestino());
        agendamentoTransacaoExistente.setValor(transacao.getValor());

        BigDecimal taxa = taxarTransacao(transacao.getValor(), transacao.getDataAgendamento());
        agendamentoTransacaoExistente.setValorTotal(transacao.getValor().add(taxa));

        agendamentoTransacaoExistente.setDataAgendamento(transacao.getDataAgendamento());
        agendamentoTransacaoExistente.setStatusTransacao(AgendamentoTransacao.StatusTransacao.ACEITE);

        agendamentoRepository.save(agendamentoTransacaoExistente);
    }

    @Transactional(
            readOnly = false,
            rollbackFor = RuntimeException.class)
    public void eliminar(long id) {

        if (!agendamentoRepository.existsById(id)) {
            throw new RuntimeException("ID não encontrado: " + id);
        }

        agendamentoRepository.deleteById(id);
    }
}
