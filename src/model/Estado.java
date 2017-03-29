package model;

/**
 * Created by Sebas on 28/03/2017.
 */
public class Estado {

    private String nombre;
    private boolean esAceptacion;
    private boolean esError;
    private boolean esInicial;
    private Transicion transiciones[];

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

    public Transicion[] getTransiciones() {
        return transiciones;
    }

    public void setTransiciones(Transicion[] transiciones) {
        this.transiciones = transiciones;
    }
}
