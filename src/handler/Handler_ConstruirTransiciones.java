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

    public void guardarAutomata(ObservableList data){
        Transicion transicion;
        for (int i=0;i<data.size();i++){
            if (data.get(i) instanceof String[]) {
                String[] transiciones = (String[]) data.get(i);
                for (int j=1;j<transiciones.length;j++){
                    int dir=direccionEstado(transiciones[j]);
                    transicion = new Transicion(Automata.getInstance().getSimbolos()[j-1]);
                    transicion.agregarEstadoFinal(Automata.getInstance().getEstados().get(dir));
                    Automata.getInstance().getEstados().get(i).addTransicion(transicion);
                }
            }
        }
    }

    public int direccionEstado(String valorEstado){
        ArrayList<Estado> estados = Automata.getInstance().getEstados();
        for (int i=0;i<estados.size();i++){
            if(estados.get(i).getNombre().equals(valorEstado)){
                return i;
            }
        }
        return -1;
    }

}
