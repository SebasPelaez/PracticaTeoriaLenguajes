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

    public ArrayList<Estado> getEstados() {
        return estados;
    }

    public void setEstados(ArrayList<Estado> estados) {
        this.estados = estados;
    }

    public String[] getSimbolos() {
        return simbolos;
    }

    public void setSimbolos(String[] simbolos) {
        this.simbolos = simbolos;
    }

    public Estado obtenerEstadoInicial(){
        Estado e = null;
        for (int i = 0; i < estados.size(); i++) {
            e = estados.get(i);
            if(e.isEsInicial()){
                return estados.get(i);
            }
        }
        return e;
    }

    public boolean reconocerSecuencia(String s){
        Estado estadoActual = obtenerEstadoInicial();
        if(s.isEmpty() && estadoActual.isEsAceptacion()){
            return true;
        }
        int i = 0;
        int j = 0;
        char[] c = s.toCharArray();
        String actualSimbolo;
        Transicion t;
        while(i < s.length()){
            actualSimbolo = String.valueOf(c[i]);
            System.out.println(actualSimbolo);
            t = estadoActual.getTransiciones().get(j);
            System.out.println("Estado actual: "+estadoActual.getNombre());
            if(t.getSimbolo().equals(actualSimbolo)){
                estadoActual = t.getEstadosFinales().get(0);
                j=0;
                i++;
            }else{
                j++;
            }

        }
        return estadoActual.isEsAceptacion();

    }

    public boolean esDeterministico() {
        int cont = 1;
        for (int i = 0; i < estados.size(); i++) {
            if (estados.get(i).isEsInicial()) {
                cont++;
            }
        }
        if (cont >=2){
            return false;
        }else{
            for (int i = 0; i < estados.size(); i++) {
                Estado e = estados.get(i);
                int numTran = e.getTransiciones().size();
                for (int j = 0; j < numTran; j++) {
                    Transicion t = e.getTransiciones().get(j);
                    if(t.numeroEstadosFinales()>=2){
                        return false;
                    }
                }

            }
        }
        return true;
    }



}
