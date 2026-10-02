package com.exemplo.saga.orquestrador.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Respostas de erro no formato RFC 9457 (application/problem+json). A classe base já trata
 * as exceções do Spring MVC (JSON malformado, método não suportado...) nesse formato.
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(final MethodArgumentNotValidException ex,
                                                                  final HttpHeaders headers,
                                                                  final HttpStatusCode status,
                                                                  final WebRequest request) {
        final Map<String, String> erros = new LinkedHashMap<>();
        for (final FieldError erro : ex.getBindingResult().getFieldErrors()) {
            erros.putIfAbsent(erro.getField(), Objects.requireNonNullElse(erro.getDefaultMessage(), "inválido"));
        }

        final ProblemDetail problem = ex.getBody();
        problem.setTitle("Requisição inválida");
        problem.setDetail("Um ou mais campos são inválidos");
        problem.setProperty("erros", erros);

        log.warn("invalid request fields={}", erros.keySet());

        return handleExceptionInternal(ex, problem, headers, status, request);
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleUnexpected(final Exception ex) {
        log.error("unexpected error", ex);

        final ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.INTERNAL_SERVER_ERROR, "Erro inesperado ao processar a requisição");
        problem.setTitle("Erro interno");
        return problem;
    }
}
