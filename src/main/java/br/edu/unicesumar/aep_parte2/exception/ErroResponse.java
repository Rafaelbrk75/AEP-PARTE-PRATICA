package br.edu.unicesumar.aep_parte2.exception;

public class ErroResponse extends RuntimeException {
    public ErroResponse(String message) {
        super(message);
    }
}
