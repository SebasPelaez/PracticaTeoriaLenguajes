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
        ArrayList<Transicion> transiciones;
        for (int i=0;i<data.size();i++){
            transiciones = new ArrayList<>();
            if (data.get(i) instanceof String[]) {
                String[] t = (String[]) data.get(i);
                for (int j=1;j<t.length;j++){
                    String vectorEstados[] = t[j].split(",");
                    transicion = new Transicion(Automata.getInstance().getSimbolos()[j-1]);
                    for(int k=0;k<vectorEstados.length;k++){
                        int dir=direccionEstado(vectorEstados[k]);
                        transicion.agregarEstadoFinal(Automata.getInstance().getEstados().get(dir));
                    }
                    transiciones.add(transicion);
                }
                Automata.getInstance().getEstados().get(i).setTransiciones(transiciones);
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

    public boolean validarTransicionesCorrectas(ObservableList data){
        Handler_Automata handler_automata = new Handler_Automata();
        for (int i=0;i<data.size();i++){
            if (data.get(i) instanceof String[]) {
                String[] t = (String[]) data.get(i);
                for (int j=1;j<t.length;j++) {
                    String vectorEstados[] = t[j].split(",");
                    for (String cadena:vectorEstados){
                        if(!handler_automata.buscarEstado(cadena,Automata.getInstance().getEstados())){
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

}
