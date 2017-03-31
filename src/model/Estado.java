package model;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import java.util.ArrayList;

/**
 * Created by Sebas on 28/03/2017.
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

    public Estado(String nombre){
        transiciones = new ArrayList<>();
        this.nombre=nombre;
        initValues();
    }

    public Estado(String nombre,Boolean _aceptacion,Boolean _error,Boolean _inicial){
        transiciones = new ArrayList<>();
        this.nombre=nombre;
        this._aceptacion = new SimpleBooleanProperty(_aceptacion);
        this._error = new SimpleBooleanProperty(_error);
        this._inicial = new SimpleBooleanProperty(_inicial);
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
        this.esAceptacion=esAceptacion;
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

    /**
     * Esto hace parte del check de los componentes, no mover
     */
    public boolean is_Aceptacion() {
        return _aceptacion.get();
    }

    public void set_aceptacion(boolean _aceptacion) {
        this._aceptacion.set(_aceptacion);
    }
    public final BooleanProperty aceptacionProperty(){
        return this._aceptacion;
    }

    public boolean is_error() {
        return _error.get();
    }

    public void set_error(boolean _error) {
        this._error.set(_error);
    }
    public final BooleanProperty errorProperty(){
        return this._error;
    }

    public boolean is_inicial() {
        return _inicial.get();
    }
    public void set_inicial(boolean _inicial) {
        this._inicial.set(_inicial);
    }
    public final BooleanProperty inicialProperty(){
        return this._inicial;
    }
    /**
     * Hasta acá, no mover
     */



}
