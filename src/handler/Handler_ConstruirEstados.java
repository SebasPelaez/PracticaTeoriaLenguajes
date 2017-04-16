package handler;

import javafx.collections.ObservableList;
import model.Automata;
import model.Estado;

import java.util.ArrayList;

/**
 * Created by Sebas on 1/04/2017.
 */
public class Handler_ConstruirEstados {
    Automata automata;

    public Handler_ConstruirEstados(){
        automata = Automata.getInstance();
    }

    public void agregarSimbolos(String cadena){
        String[] simbolos = cadena.split(",");
        automata.setSimbolos(simbolos);
    }

    public boolean cadenaValida(String cadena){
        if (cadena.charAt(0)==',' || cadena.charAt(cadena.length()-1)==','){
            return false;
        }
        return true;
    }

    public boolean simbolosRepetidos(String cadena){
        String apariciones="";
        String[] vector = cadena.split(",");
        for (int i = 0; i < vector.length; i++) {
            if(apariciones.contains(vector[i])){
                return true;
            }else{
                apariciones += vector[i];
            }
        }
        return false;
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
        Handler_Automata handler_automata = new Handler_Automata();
        handler_automata.sortEstadoInicial();
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

    public boolean estadosRepetidos(ObservableList<Estado> estados){
        String apariciones="";
        for (Estado e: estados){
            if (apariciones.contains(e.getNombre())){
                return true;
            }else{
                apariciones += e.getNombre();
            }
        }
        return false;
    }

    public int retornatFilar(ObservableList<Estado> estados,String nombre){
        int index=-1;
        for (Estado e: estados){
            if (e.getNombre().equals(nombre)){
                return estados.indexOf(e);
            }
        }
        return index;
    }

    public ArrayList<String> validarAutomata(ObservableList<Estado> estados,String cadenaSimbolos){
        ArrayList<String> valido = new ArrayList<>();
        if(estaVaciaCadena(cadenaSimbolos)){
            valido.add("Debe haber por lo menos 1 símbolo de entrada.");
            return valido;
        }else{
            if(!cadenaValida(cadenaSimbolos)){
                valido.add("La cadena de símbolos no puede empezar o terminar con comas(,).");
            }

            if(simbolosRepetidos(cadenaSimbolos)){
                valido.add("Hay símbolos de entrada repetidos.");
            }
        }

        if(!existenEstados(estados)){
            valido.add("Debe haber por lo menos 1 estado.");
            return valido;
        }else{
            if(!existeInicial(estados)){
                valido.add("Debe haber por lo menos 1 estado inicial.");
            }
            if(!existeAceptacion(estados)){
                valido.add("Debe haber por lo menos 1 estado de aceptación.");
            }

            if(!nombresDeEstadosCorrectos(estados)){
                valido.add("El nombre de algunos estados está sin asignar.");
            }

            if(estadosRepetidos(estados)){
                valido.add("Hay estados con el mismo nombre.");
            }

        }
        if(!simbolosDiferentesDeEstados(estados,cadenaSimbolos)){
            valido.add("Los nombres de los estados deben ser diferentes a los símbolos de entrada.");
        }



        return valido;
    }

}
