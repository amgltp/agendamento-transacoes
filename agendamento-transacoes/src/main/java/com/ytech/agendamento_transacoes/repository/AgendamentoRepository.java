package com.ytech.agendamento_transacoes.repository;

import com.ytech.agendamento_transacoes.model.AgendamentoTransacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AgendamentoRepository extends JpaRepository<AgendamentoTransacao,Long> {

}
