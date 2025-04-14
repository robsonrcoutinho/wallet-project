package br.com.coutinhocorp.wallet.dto;

import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

public record WalletRecordDto(@NotBlank BigDecimal balance, @NotBlank Boolean isActive, @NotBlank  String walletType, @NotBlank String numberDocumentUser) {

}
