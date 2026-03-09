-- Visão Geral
-- A API de Agendamento de Transações é um serviço RESTful desenvolvido com Spring Boot que permite
                        aos clientes gerenciar transações agendadas

--URL base http://localhost:8080/agendamento


-- GET /agendamento
-- 200 OK retorna lista de agendamento de transações neste formato
[
{
"id": 1,
"contaOrigem": "12345",
"contaDestino": "67890",
"valor": 1500,
"valorTotal": 1535,
"dataAgendamento": "2026-03-15",
"statusTransacao": "ACEITE"
}
]
-- 404 Not found - Nenhuma transação encontrada

--POST /agendamento
 -- 201 Created - Retorna o objeto AgendamentoTransacao criado neste formato 
[
{
"contaOrigem": "12345",
"contaDestino": "67890",
"valor": 1500,
"dataAgendamento": "2026-03-15"
}
]

-- PUT /agendamento
-- 200 OK Transação atualizada com sucesso 
404 Not Found - Transação com o id nao encontrada 

--corpo do requisicao 
[
{
"contaOrigem": "12345",
"contaDestino": "98765",
"valor": 2000,
"dataAgendamento": "2026-03-20"
}
]

--DELETE /agendamento
--204 No content - Transação eliminada com sucesso 
404 Not found - Transação com o id nao encontrada