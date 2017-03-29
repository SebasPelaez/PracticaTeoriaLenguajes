package model;

/**
 * Created by Sebas on 28/03/2017.
 */
public class Transicion {

    private Estado estadosFinales[];
    private String simbolo;

    public Estado[] getEstadosFinales() {
        return estadosFinales;
    }

    public void setEstadosFinales(Estado[] estadosFinales) {
        this.estadosFinales = estadosFinales;
    }

    public String getSimbolo() {
        return simbolo;
    }

    public void setSimbolo(String simbolo) {
        this.simbolo = simbolo;
    }

}
