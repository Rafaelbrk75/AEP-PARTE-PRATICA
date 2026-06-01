package br.edu.unicesumar.aep_parte2.domain.enums;

public enum Prioridade {
    BAIXA(4, 360),
    MEDIA(3, 168),
    ALTA(2, 72),
    URGENTE(1, 24);

    private final int peso;
    private final int prazoHoras;

    Prioridade(int peso, int prazoHoras) {
        this.peso = peso;
        this.prazoHoras = prazoHoras;
    }

    public int getPeso() { return peso; }
    public int getPrazoHoras() { return prazoHoras; }
}
