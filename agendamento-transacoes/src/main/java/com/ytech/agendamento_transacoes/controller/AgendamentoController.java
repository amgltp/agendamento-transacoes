package com.ytech.agendamento_transacoes.controller;

import com.ytech.agendamento_transacoes.model.AgendamentoTransacao;
import com.ytech.agendamento_transacoes.service.AgendamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/agendamento")
public class AgendamentoController {

    private AgendamentoService agendamentoService;

    @GetMapping()
    public ResponseEntity<Page<AgendamentoTransacao>> consultar(
            @PageableDefault(size = 10, sort = "dataAgendamento" , direction = Sort.Direction.DESC) Pageable pageable
    ){

        Page<AgendamentoTransacao> transacoes = agendamentoService.consultarTodasTransacoes(pageable);
        if(!transacoes.isEmpty()){
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(transacoes);

        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .build();
    }

    @PostMapping()
    public ResponseEntity<String> incluir(@RequestBody AgendamentoTransacao agendamentoTransacao){

        AgendamentoTransacao transacao = new AgendamentoTransacao();
        transacao.setContaOrigem(agendamentoTransacao.getContaOrigem());
        transacao.setContaDestino(agendamentoTransacao.getContaDestino());
        agendamentoTransacao.setValor(agendamentoTransacao.getValor());



        //TODO
        //transacao.setTaxa(agendamentoTransacao.getTaxa());
        //agendamentoTransacao.setValorTotal();
        //agendamentoTransacao.getDataAgendamento();

        //agendamentoTransacao.getStatusTransacao();

        return ResponseEntity.ok(" ");
    }

    @Autowired
    public void setAgendamentoService(AgendamentoService service){
        this.agendamentoService = service;
    }
}
