package br.com.coutinhocorp.wallet.dto;

import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

public record TransactionRecordDto(@NotBlank BigDecimal amount, @NotBlank String numberDocumentUser) {
}
