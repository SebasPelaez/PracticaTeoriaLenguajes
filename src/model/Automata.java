package model;

import java.util.ArrayList;

/**
 * Clase utilizada para representar un autómata;
 */
public class Automata {

    private ArrayList<Estado> estados;
    private String[] simbolos;


    /**
     *Constructor con parámetros de la clase.
     * @param estados Colección de con los estados del automata.
     * @param simbolos Colección con los símbolos que componen el autómata.
     */
    public Automata(ArrayList<Estado> estados, String[] simbolos){
        this.estados = estados;
        this.simbolos = simbolos;
    }

    /**
     *Constructor sin parámetros de la clase.
     */
    public Automata(){
        estados= new ArrayList<>();
        simbolos = new String[0];
    }

    /**
     *Retorna la colección de estado que compone el autómata.
     * @return Colección de estados.
     */
    public ArrayList<Estado> getEstados() {
        return estados;
    }

    /**
     * Setea la colección de estados del autómata.
     * @param estados colección de estados que será asignado al automáta.
     */
    public void setEstados(ArrayList<Estado> estados) {
        this.estados = estados;
    }

    /**
     *Retorna la colección de símbolos de entrada del autómata.
     * @return Colección con los símbolos de entrada del autómata.
     */
    public String[] getSimbolos() {
        return simbolos;
    }

    /**
     *Añade un nuevo estado a la colección de estados del autómata.
     * @param e Estado que será agregado a la colección de estados del autómata.
     */
    public void agregarEstado(Estado e){
        estados.add(e);
    }

    /**
     * Setea la colección de símbolos de entrada del autómata.
     * @param simbolos Colección de símbolos de entrada que será asignado al autómata.
     */
    public void setSimbolos(String[] simbolos) {
        this.simbolos = simbolos;
    }

}
