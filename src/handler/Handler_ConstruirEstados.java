package handler;

import javafx.collections.ObservableList;
import model.Automata;
import model.Estado;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * Created by Sebas on 1/04/2017.
 */
public class Handler_ConstruirEstados {
    Automata automata;

    public Handler_ConstruirEstados(){
        automata = Automata.getInstance();
    }

    public void agregarSimbolos(String cadena){
        String simbolos[]= cadena.split(",");
        automata.setSimbolos(simbolos);
    }

    public boolean estaVaciaCadena(String cadena){
        return cadena.equals("");
    }

    public void agregarEstados(ObservableList<Estado> observableEstados){
        ArrayList<Estado> estados = new ArrayList<>();
        for (int i=0;i<observableEstados.size();i++){
            Estado e = new Estado(observableEstados.get(i).getNombre(),
                    observableEstados.get(i).is_Aceptacion(),
                    observableEstados.get(i).is_error(),
                    observableEstados.get(i).is_inicial());
            e.setEsAceptacion(observableEstados.get(i).is_Aceptacion());
            e.setEsInicial(observableEstados.get(i).is_inicial());
            e.setEsError(observableEstados.get(i).is_error());
            estados.add(e);
        }
        automata.setEstados(estados);
    }

    public boolean existenEstados(ObservableList<Estado> estados){
        return estados.size()!=0;
    }

    public void imprimirSimbolos(){
        for (int i=0;i<automata.getSimbolos().length;i++){
            System.out.println("Simbolo: " + automata.getSimbolos()[i]);
        }
    }

    public boolean nombresDeEstadosCorrectos(ObservableList<Estado> estados){
        for(Estado e: estados){
            if(e.getNombre().equals("Ingrese el estado aquí")){
                return false;
            }
        }
        return true;
    }

    public boolean existeAceptacion(ObservableList<Estado> estados){
        int cont=0;
        for(Estado e: estados){
            if(e.isEsAceptacion() || e.is_Aceptacion()){
                cont++;
            }
        }
        return cont!=0;
    }

    public boolean existeInicial(ObservableList<Estado> estados){
        int cont=0;
        for(Estado e: estados){
            if(e.isEsInicial() || e.is_inicial()){
                cont++;
            }
        }
        return cont!=0;
    }

    public boolean simbolosDiferentesDeEstados(ObservableList<Estado> estados,String cadenaSimbolos){
        String simbolos[] = cadenaSimbolos.split(",");
        for (String s: simbolos){
            for (Estado e: estados){
                if (e.getNombre().equals(s)){
                    return false;
                }
            }
        }
        return true;
    }

}
