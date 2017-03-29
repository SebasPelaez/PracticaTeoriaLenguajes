package model;

/**
 * Created by Sebas on 28/03/2017.
 */
public class Automata {

    private Estado estados[];
    private String simbolos[];

    public Estado[] getEstados() {
        return estados;
    }

    public void setEstados(Estado[] estados) {
        this.estados = estados;
    }

    public String[] getSimbolos() {
        return simbolos;
    }

    public void setSimbolos(String[] simbolos) {
        this.simbolos = simbolos;
    }
}
