package model;

import java.util.ArrayList;

/**
 * Created by Sebas on 28/03/2017.
 */
public class Estado {

    private String nombre;
    private boolean esAceptacion;
    private boolean esError;
    private boolean esInicial;
    private ArrayList<Transicion> transiciones;

    public Estado(String nombre){
        transiciones = new ArrayList<>();
        this.nombre=nombre;
        initValues();
    }

    private void initValues(){
        esAceptacion = false;
        esError = false;
        esInicial = false;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public boolean isEsAceptacion() {
        return esAceptacion;
    }

    public void setEsAceptacion(boolean esAceptacion) {
        this.esAceptacion = esAceptacion;
    }

    public boolean isEsError() {
        return esError;
    }

    public void setEsError(boolean esError) {
        this.esError = esError;
    }

    public boolean isEsInicial() {
        return esInicial;
    }

    public void setEsInicial(boolean esInicial) {
        this.esInicial = esInicial;
    }

    public ArrayList<Transicion> getTransiciones() {
        return transiciones;
    }

    public void setTransiciones(ArrayList<Transicion> transiciones) {
        this.transiciones = transiciones;
    }

    public void addTransicion(Transicion t){
        transiciones.add(t);
    }



}
