package model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import java.util.ArrayList;

/**
 * Clase que representa los estados con los cuales está construido el autómata
 * cada estado a su vez está constituido por una seríe de valores booleanos dependiendo
 * si es de aceptación,inicial o error, y una colección de transiciones, 1 por cada  símbolo de
 * entrada del autómata.
 */
public class Estado{

    private String nombre;
    private boolean esAceptacion;
    private boolean esError;
    private boolean esInicial;
    private ArrayList<Transicion> transiciones;

    private BooleanProperty _aceptacion;
    private BooleanProperty _error;
    private BooleanProperty _inicial;

    /**
     * Constructor de la clase.
     * @param nombre Nombre que tendrá el estado.
     */
    public Estado(String nombre){
        transiciones = new ArrayList<>();
        this.nombre=nombre;
        initValues();
    }

    /**
     *Constructor de la clase
     * @param nombre Nombre que tendrá el estado.
     * @param _aceptacion Si el estado es de aceptación.
     * @param _error Si el estado es de error
     * @param _inicial Si el estado es de error.
     */
    public Estado(String nombre,Boolean _aceptacion,Boolean _error,Boolean _inicial){
        transiciones = new ArrayList<>();
        this.nombre=nombre;
        this._aceptacion = new SimpleBooleanProperty(_aceptacion);
        this._error = new SimpleBooleanProperty(_error);
        this._inicial = new SimpleBooleanProperty(_inicial);
    }

    /**
     * Inicialize los valores booleanos del estado.
     */
    private void initValues(){
        esAceptacion = false;
        esError = false;
        esInicial = false;
        this._aceptacion = new SimpleBooleanProperty(false);
        this._error = new SimpleBooleanProperty(false);
        this._inicial = new SimpleBooleanProperty(false);
    }

    /**
     * Retorna el nombre del estado.
     * @return Nombre del estado.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Setea el nombre del estado.
     * @param nombre Nombre que será asignado al estado.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Retorna verdadero o false dependiendo si el estado es de aceptación o no.
     * @return Verdadero si el estado es de aceptación, falso en caso contrario.
     */
    public boolean isEsAceptacion() {
        return esAceptacion;
    }

    /**
     * Setea el valor de la propiedad 'aceptación' del estado
     * @param esAceptacion Valor que será asignado a la propiedad 'aceptación del autómata.
     */
    public void setEsAceptacion(boolean esAceptacion) {
        this.esAceptacion=esAceptacion;
        this.set_aceptacion(esAceptacion);
    }

    /**
     * Retorna si el estado es un estado de error.
     * @return Verdadero si el estado es de error, falso en caso contrario
     */
    public boolean isEsError() {
        return esError;
    }

    /**
     * Setea el valor de la propiedad 'error' del estado.
     * @param esError Valor que será asignado a la propiedad.
     */
    public void setEsError(boolean esError) {
        this.esError = esError;
        this.set_error(esError);
    }

    /**
     * Retorna si el estado es inicial.
     * @return El estado es inicial.
     */
    public boolean isEsInicial() {
        return esInicial;
    }

    /**
     * Setea el valor de la propiedad 'inicial' del estado.
     * @param esInicial Valor que será asignado a la propiedad.
     */
    public void setEsInicial(boolean esInicial) {
        this.esInicial = esInicial;
        this.set_inicial(esInicial);
    }

    /**
     * Retorna la colección de transiciones del estado.
     * @return Colección de transiciones del estado.
     */
    public ArrayList<Transicion> getTransiciones() {
        return transiciones;
    }

    /**
     * Setea la colección de transiciones del estado.
     * @param transiciones Colección que será asignada.
     */
    public void setTransiciones(ArrayList<Transicion> transiciones) {
        this.transiciones = transiciones;
    }

    /**
     * Añade una nueva transición a la colección de transiciones del estado.
     * @param t
     */
    public void addTransicion(Transicion t){
        transiciones.add(t);
    }

    /**
     * Esto hace parte del check de los componentes, no mover
     */
    public boolean is_Aceptacion() {
        return _aceptacion.get();
    }

    public void set_aceptacion(boolean _aceptacion) {
        this._aceptacion.set(_aceptacion);
        this.esAceptacion=_aceptacion;
    }
    public final BooleanProperty aceptacionProperty(){
        return this._aceptacion;
    }

    public boolean is_error() {
        return _error.get();
    }

    public void set_error(boolean _error) {
        this._error.set(_error);
        this.esError=_error;
    }
    public final BooleanProperty errorProperty(){
        return this._error;
    }

    public boolean is_inicial() {
        return _inicial.get();
    }
    public void set_inicial(boolean _inicial) {
        this._inicial.set(_inicial);
        this.esInicial=_inicial;
    }
    public final BooleanProperty inicialProperty(){
        return this._inicial;
    }
    /**
     * Hasta acá, no mover
     */



}
