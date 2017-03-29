package model;

import java.util.ArrayList;

/**
 * Created by Sebas on 28/03/2017.
 */
public class Transicion {

    private ArrayList<Estado> estadosFinales;
    private String simbolo;

    public Transicion(String s){
        estadosFinales = new ArrayList<>();
        simbolo = s;
    }

    public ArrayList<Estado> getEstadosFinales() {
        return estadosFinales;
    }

    public void setEstadosFinales(ArrayList<Estado> estadosFinales) {
        this.estadosFinales = estadosFinales;
    }

    public String getSimbolo() {
        return simbolo;
    }

    public void setSimbolo(String simbolo) {
        this.simbolo = simbolo;
    }

    public void agregarEstadoFinal(Estado e){
        estadosFinales.add(e);
    }

}
