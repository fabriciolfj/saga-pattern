package com.exemplo.saga.orquestrador.controller;

import com.exemplo.saga.orquestrador.domain.Transacao;
import com.exemplo.saga.orquestrador.service.inicializacao.TransactionInicializacaoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static com.exemplo.saga.orquestrador.controller.TransacaoRequestMapper.toTransaction;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/transactions")
public class TransactionController {

    private final TransactionInicializacaoService transactionInicializacaoService;

    @PostMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public TransacaoResponse createTransaction(@Valid @RequestBody final TransacaoRequest request) {
        log.info("receive request transactionId={} value={}", request.transactionId(), request.value());

        final Transacao transacao = toTransaction(request);
        final var saga = transactionInicializacaoService.execute(transacao);

        return new TransacaoResponse(saga.getId(), saga.getStatus());
    }
}
