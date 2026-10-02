package com.exemplo.saga.orquestrador.controller;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record TransacaoRequest(

        @NotBlank(message = "é obrigatório")
        String transactionId,

        @NotNull(message = "é obrigatório")
        @Positive(message = "deve ser maior que zero")
        @Digits(integer = 15, fraction = 2, message = "deve ter no máximo 2 casas decimais")
        BigDecimal value,

        @NotBlank(message = "é obrigatório")
        @Pattern(regexp = "\\d{11}|\\d{14}", message = "deve ser CPF (11 dígitos) ou CNPJ (14 dígitos)")
        String documentCustomer) {
}
