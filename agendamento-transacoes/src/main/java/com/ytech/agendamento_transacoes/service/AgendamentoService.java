package com.ytech.agendamento_transacoes.service;

import com.ytech.agendamento_transacoes.model.AgendamentoTransacao;
import com.ytech.agendamento_transacoes.repository.AgendamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service("agendamento-service")
@Primary
public class AgendamentoService {

    private AgendamentoRepository agendamentoRepository;

    public Page<AgendamentoTransacao> consultarTodasTransacoes(Pageable pageable){
       Pageable sortedPageable = PageRequest.of(
               pageable.getPageNumber(),
               pageable.getPageSize(),
               Sort.by("dataAgendamento").descending()
       );
       return agendamentoRepository.findAll(sortedPageable);
    }

    @Autowired
    public void setAgendamentoRepository(AgendamentoRepository repository){
        this.agendamentoRepository = repository;
    }
}
