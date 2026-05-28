package br.edu.unicesumar.aep_parte2.exception;

public class TransicaoStatusInvalidaException extends RuntimeException {
    public TransicaoStatusInvalidaException(String message) {
        super(message);
    }
}
