package br.com.coutinhocorp.wallet.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class WalletWithoutFundsException extends Exception {

    private static final long serialVersionUID = 1L;

    public WalletWithoutFundsException(String message) {
        super(message);
    }

    public WalletWithoutFundsException(String message, Throwable t) {
        super(message, t);
    }
}