package br.edu.unicesumar.aep_parte2.domain.enums;

public enum Prioridade {
    BAIXA(4),
    MEDIA(3),
    ALTA(2),
    URGENTE(1);

    private final int peso;

    Prioridade(int peso) {
        this.peso = peso;
    }

    public int getPeso() {
        return peso;
    }
}
