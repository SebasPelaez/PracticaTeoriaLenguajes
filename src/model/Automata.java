package model;

import java.util.ArrayList;

/**
 * Created by Sebas on 28/03/2017.
 */
public class Automata {

    private ArrayList<Estado> estados;
    private String[] simbolos;


    public Automata(ArrayList<Estado> estados, String[] simbolos){
        this.estados = estados;
        this.simbolos = simbolos;
    }

    public Automata(){
        estados= new ArrayList<>();
        simbolos = new String[0];
    }

    public ArrayList<Estado> getEstados() {
        return estados;
    }

    public void setEstados(ArrayList<Estado> estados) {
        this.estados = estados;
    }

    public String[] getSimbolos() {
        return simbolos;
    }

    public void agregarEstado(Estado e){
        estados.add(e);
    }

    public void setSimbolos(String[] simbolos) {
        this.simbolos = simbolos;
    }

}
