package br.com.coutinhocorp.wallet.dto;

import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

public record TransactionTransferBalanceRecordDto(@NotBlank BigDecimal amount, @NotBlank String numberDocumentUserSender, @NotBlank String numberDocumentUserReceiver) {}
