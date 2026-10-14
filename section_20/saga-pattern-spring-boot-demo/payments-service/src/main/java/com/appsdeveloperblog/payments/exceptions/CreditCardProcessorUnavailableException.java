package com.appsdeveloperblog.payments.exceptions;

public class CreditCardProcessorUnavailableException extends RuntimeException {

    public CreditCardProcessorUnavailableException(Throwable cause) {
        super(cause);
    }
}
