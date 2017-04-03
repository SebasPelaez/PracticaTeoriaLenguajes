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
        int tamañoVector = (cadena.length()/2)+(cadena.length()%2);
        int i=0;
        String simbolos[]= new String[tamañoVector];
        StringTokenizer token = new StringTokenizer(cadena,",");
        while (token.hasMoreTokens()){
            simbolos[i++]=token.nextToken();
        }
        automata.setSimbolos(simbolos);
    }

    public String validarCadena(String cadena){
        if (cadena.charAt(0)==',')return "La cadena de símbolos no debe empezar con comas(,)";
        if(cadena.charAt(cadena.length()-1)==',')return "La cadena de símbolos no debe empezar con comas(,)";
        return "";
    }

    public boolean estaVaciaCadena(String cadena){
        return cadena.equals("");
    }

    public void agregarEstados(ObservableList<Estado> observableEstados){
        ArrayList<Estado> estados = new ArrayList<>();
        for (int i=0;i<observableEstados.size();i++){
            estados.add(new Estado(observableEstados.get(i).getNombre(),
                    observableEstados.get(i).is_Aceptacion(),
                    observableEstados.get(i).is_error(),
                    observableEstados.get(i).is_inicial()));
        }
        automata.setEstados(estados);
        for (int i=0;i<automata.getEstados().size();i++){
            System.out.println("Nombre ("+i+"): " + automata.getEstados().get(i).getNombre());
            System.out.println("Inicial("+i+"): " + automata.getEstados().get(i).is_inicial());
            System.out.println("Aceptación?("+i+"): " + automata.getEstados().get(i).is_Aceptacion());
            System.out.println("Error?("+i+"): " + automata.getEstados().get(i).is_error());
        }

    }

    public boolean existenEstados(ObservableList<Estado> estados){
        return estados.size()!=0;
    }

    public void imprimirSimbolos(){
        for (int i=0;i<automata.getSimbolos().length;i++){
            System.out.println("Simbolo: " + automata.getSimbolos()[i]);
        }
    }

}
