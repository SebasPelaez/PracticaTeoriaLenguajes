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
                    String vectorEstados[] = transiciones[j].split(",");
                    for(int k=0;k<vectorEstados.length;k++){
                        int dir=direccionEstado(vectorEstados[k]);
                        transicion = new Transicion(Automata.getInstance().getSimbolos()[j-1]);
                        transicion.agregarEstadoFinal(Automata.getInstance().getEstados().get(dir));
                        Automata.getInstance().getEstados().get(i).addTransicion(transicion);
                    }
                }
            }
        }
        Handler_Automata a = new Handler_Automata();
        a.imprimirAutomata();
        a.convertirAutomataAFN();
        a.imprimirAutomata();
        a.simplificarAutomata();
        a.imprimirAutomata();
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
