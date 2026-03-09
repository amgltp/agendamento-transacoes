package com.ytech.agendamento_transacoes.controller;

import com.ytech.agendamento_transacoes.model.AgendamentoTransacao;
import com.ytech.agendamento_transacoes.service.AgendamentoService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping(value = "/agendamento")
@Slf4j
public class AgendamentoController {

    @GetMapping
    public ResponseEntity<List<AgendamentoTransacao>> consultar(){

        List<AgendamentoTransacao> transacoes =
                agendamentoService.consultarTodasTransacoes();

        if(!transacoes.isEmpty()){
            log.info("Transações agendadas encontradas");

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(transacoes);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .build();
    }

    @PostMapping
    public ResponseEntity<AgendamentoTransacao> incluir(@Valid @RequestBody AgendamentoTransacao agendamentoTransacao){
        agendamentoService.incluir(agendamentoTransacao);
        return ResponseEntity.status(HttpStatus.CREATED).body(agendamentoTransacao);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AgendamentoTransacao> alterar( @PathVariable("id") long id,
                                                         @Valid @RequestBody AgendamentoTransacao agendamentoTransacao){

        Optional<AgendamentoTransacao> agendamentoTransacaoEncontrado = agendamentoService.pesquisarPorId(id);

        if(agendamentoTransacaoEncontrado.isPresent()){
            agendamentoService.alterarTransacao(id, agendamentoTransacao);
            return ResponseEntity.ok(agendamentoTransacao);
        }
        return ResponseEntity.notFound().build();

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable("id") long id){
        Optional<AgendamentoTransacao> agendamentoTransacaoEncontrado = agendamentoService.pesquisarPorId(id);
        if(agendamentoTransacaoEncontrado.isPresent()){
            agendamentoService.eliminar(id);
            return ResponseEntity.noContent().build();

        }
        return ResponseEntity.notFound().build();
    }

    private AgendamentoService agendamentoService;
    @Autowired
    public void setAgendamentoService(AgendamentoService service){
        this.agendamentoService = service;
    }
}
