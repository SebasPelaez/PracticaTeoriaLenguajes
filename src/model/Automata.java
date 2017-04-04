package model;

import java.util.ArrayList;

/**
 * Created by Sebas on 28/03/2017.
 */
public class Automata {

    private ArrayList<Estado> estados;
    private String[] simbolos;
    private static Automata instance;

    public static Automata getInstance(){
        if(instance==null){
            instance = new Automata();
        }
        return instance;
    }

    public void reinicializarAutomata(){
        instance=null;
    }
/*
    public Automata(ArrayList<Estado> estados, String[] simbolos){
        this.estados = estados;
        this.simbolos = simbolos;
    }
*/
    private Automata(){}

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
