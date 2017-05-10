package model;

import java.util.ArrayList;

/**
 * Clase que representa las transiciones de un estado dependiendo del símbolo que le pertenezca
 * a la transición. La clase cuenta con un arreglo hacia los cuales se hace transición y un símbolo de entrada.
 */
public class Transicion {

    private ArrayList<Estado> estadosFinales;
    private String simbolo;

    /**
     * Constructor de la clase.
     * @param s Símbolo de entrada de la transición.
     */
    public Transicion(String s){
        estadosFinales = new ArrayList<>();
        simbolo = s;
    }

    /**
     * Retorna la colección estados hacia los cuales se hace transición de acuerdo al símbolo de entrada.
     * @return Colección de estados.
     */
    public ArrayList<Estado> getEstadosFinales() {
        return estadosFinales;
    }

    /**
     * Setea la colección de estado finales.
     * @param estadosFinales Colección que será asignada.
     */
    public void setEstadosFinales(ArrayList<Estado> estadosFinales) {
        this.estadosFinales = estadosFinales;
    }

    /**
     * Retorna el símbolo de entrada de la transición.
     * @return Símbolo de entrada de la transición
     */
    public String getSimbolo() {
        return simbolo;
    }

    /**
     * Setea el símbolo de entrada de la transición.
     * @param simbolo Símbolo que será asignado.
     */
    public void setSimbolo(String simbolo) {
        this.simbolo = simbolo;
    }

    /**
     * Añade un estado a la colección de estados finales de la transición.
     * @param e Estado que será agregado a la colección.
     */
    public void agregarEstadoFinal(Estado e){
        estadosFinales.add(e);
    }

    /**
     * Retorna el número de estados hacia los cuales se hace transición dependiendo del símbolo de entrada.
     * @return Número de estados finales.
     */
    public int numeroEstadosFinales(){
        return estadosFinales.size();
    }


}
