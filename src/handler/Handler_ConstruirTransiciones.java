package handler;

import javafx.collections.ObservableList;
import model.Automata;
import model.Estado;
import model.Transicion;

import java.util.ArrayList;

/**
 * Created by Sebas on 2/04/2017.
 */
public class Handler_ConstruirTransiciones {

    private Handler_Automata controllerAutomata;
    private Automata automata;

    public Handler_ConstruirTransiciones(Automata automata){
        this.automata=automata;
        controllerAutomata = new Handler_Automata(automata);
    }
    public void guardarAutomata(ObservableList data,int bandera){
        Transicion transicion;
        ArrayList<Transicion> transiciones;
        for (int i=0;i<data.size();i++){
            transiciones = new ArrayList<>();
            if (data.get(i) instanceof String[]) {
                String[] t = (String[]) data.get(i);
                for (int j=1;j<t.length-bandera;j++){
                    String vectorEstados[] = t[j].split(",");
                    transicion = new Transicion(automata.getSimbolos()[j-1]);
                    for(int k=0;k<vectorEstados.length;k++){
                        int dir=direccionEstado(vectorEstados[k]);
                        transicion.agregarEstadoFinal(automata.getEstados().get(dir));
                    }
                    transiciones.add(transicion);
                }
                automata.getEstados().get(i).setTransiciones(transiciones);
            }
        }
    }

    public int direccionEstado(String valorEstado){
        ArrayList<Estado> estados = automata.getEstados();
        for (int i=0;i<estados.size();i++){
            if(estados.get(i).getNombre().equals(valorEstado)){
                return i;
            }
        }
        return -1;
    }

    public boolean validarTransicionesCorrectas(ObservableList data){
        Handler_Automata handler_automata = new Handler_Automata(automata);
        for (int i=0;i<data.size();i++){
            if (data.get(i) instanceof String[]) {
                String[] t = (String[]) data.get(i);
                for (int j=1;j<t.length;j++) {
                    String vectorEstados[] = t[j].split(",");
                    for (String cadena:vectorEstados){
                        if(!handler_automata.buscarEstado(cadena,automata.getEstados())){
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    public void vaciarTransiciones(){
        for (Estado e: automata.getEstados()){
            e.getTransiciones().clear();
        }
    }

}
