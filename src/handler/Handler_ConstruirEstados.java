package handler;

import javafx.collections.ObservableList;
import model.Automata;
import model.Estado;

import java.util.ArrayList;

/**
 * Created by Sebas y Juan on 1/04/2017.
 * Todas las acciones que se puedan generar en la pantalla de construcción de estados, estan ejecutadas aqui.
 */
public class Handler_ConstruirEstados {
    private Automata automata;

    /**
     * Método constructor.
     * @param automata Setea el atributo de la clase.
     */
    public Handler_ConstruirEstados(Automata automata){
        this.automata = automata;
    }

    /**
     * Guarda todos los símbolos de entrada en el autómata.
     * @param cadena Los símbolos de entrada.
     */
    public void agregarSimbolos(String cadena){
        String[] simbolos = cadena.split(",");
        automata.setSimbolos(simbolos);
    }

    /**
     * Verifica que la cadena ingresada si sea valida.
     * @param cadena La cadena que se analizara
     * @return Verdadero si cumple con los requisitos de contrucción, falso, de lo contrario.
     */
    public boolean cadenaValida(String cadena){
        if (cadena.charAt(0)==',' || cadena.charAt(cadena.length()-1)==','){
            return false;
        }
        return true;
    }

    /**
     * Verifica que los símbolos de entrada sean unicos
     * @param cadena Los símbolos de entrada.
     * @return Verdadero si no existen símbolos repetidos.
     */
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

    /**
     * Verifica que la cadena de Símbolos no este vacia.
     * @param cadena La cadena de símbolos.
     * @return Verdadero en caso de que este vacia, falsa si no.
     */
    public boolean estaVaciaCadena(String cadena){
        return cadena.equals("");
    }

    /**
     * Guarda en el autómata los estados ingresados por pantalla.
     * @param observableEstados Todos aquellos estados que se ingresaron por pantalla.
     */
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
        Handler_Automata handler_automata = new Handler_Automata(automata);
        handler_automata.sortEstadoInicial();
    }

    /**
     * Verifica que el usuario si haya ingresado estados.
     * @param estados EL arreglo con los estados.
     * @return Verdadero si si hay estados.
     */
    public boolean existenEstados(ObservableList<Estado> estados){
        return estados.size()!=0;
    }

    /**
     * Imprime en consola los símbolos creados.
     */
    public void imprimirSimbolos(){
        for (int i=0;i<automata.getSimbolos().length;i++){
            System.out.println("Simbolo: " + automata.getSimbolos()[i]);
        }
    }

    /**
     * Verifica que si le dieron en agregar un estado, si hayan escrito un estado ahí
     * @param estados El array con todos los estados
     * @return Verdadero en cao de que si este bien creado.
     */
    public boolean nombresDeEstadosCorrectos(ObservableList<Estado> estados){
        for(Estado e: estados){
            if(e.getNombre().equals("Ingrese el estado aquí")){
                return false;
            }
        }
        return true;
    }

    /**
     * Un estado no puede ser de aceptación y de error al tiempo.
     * @param estados El array con los estados.
     * @return Verdadero si ay un estado con aceptación y error.
     */
    public boolean esErrorYAceptacion(ObservableList<Estado> estados){
        for(Estado e: estados){
            if(e.is_Aceptacion() && e.is_error() || e.is_inicial() && e.is_error()){
                return true;
            }
        }
        return false;
    }

    /**
     * Verifica que exista por lo menos un estado de aceptación.
     * @param estados El array con los estados.
     * @return Verdadero si por lo menos existe uno de aceptación.
     */
    public boolean existeAceptacion(ObservableList<Estado> estados){
        int cont=0;
        for(Estado e: estados){
            if(e.isEsAceptacion() || e.is_Aceptacion()){
                cont++;
            }
        }
        return cont!=0;
    }

    /**
     *Verifica que exista por lo menos un estado inicial.
     * @param estados El array con los estados.
     * @return Verdadero si por lo menos existe uno inicial.
     */
    public boolean existeInicial(ObservableList<Estado> estados){
        int cont=0;
        for(Estado e: estados){
            if(e.isEsInicial() || e.is_inicial()){
                cont++;
            }
        }
        return cont!=0;
    }

    /**
     * Si un simbolo de entrada es igual al nombre de un estado no deja avanzar.
     * @param estados El array con todos los estados.
     * @param cadenaSimbolos Los símbolos que se van a analizar.
     * @return Verdaddero si no existen, falso de lo contrario.
     */
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

    /**
     * Verifica que no existan dos estados con el mismo nombre.
     * @param estados El array con los estados.
     * @return Verdadero en caso de hayan 2 estados iguales.
     */
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

    /**
     * Busca la ocurrencia de unestado en el autómata.
     * @param estados El array de estados
     * @param nombre El nombre dle estado que se quiere buscar.
     * @return el valor de la posición.
     */
    public int retornatFilar(ObservableList<Estado> estados,String nombre){
        int index=-1;
        for (Estado e: estados){
            if (e.getNombre().equals(nombre)){
                return estados.indexOf(e);
            }
        }
        return index;
    }

    /**
     * Realiza una serie de validaciones que permiten determinar si los estados y los símbolos iniciales si se construyeron de la
     * forma adecuada.
     * @param estados EL array de estados-
     * @param cadenaSimbolos Los símbolos de entrada.
     * @return Un array deStrings con los posibles mensajes de error.
     */
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
            if(esErrorYAceptacion(estados)){
                valido.add("El estado no puede ser de error y aceptación al mismo tiempo o inicial y de error.");
            }

        }
        if(!simbolosDiferentesDeEstados(estados,cadenaSimbolos)){
            valido.add("Los nombres de los estados deben ser diferentes a los símbolos de entrada.");
        }
        return valido;
    }

}
