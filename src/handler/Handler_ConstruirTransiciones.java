package handler;

import javafx.collections.ObservableList;
import model.Automata;
import model.Estado;
import model.Transicion;

import java.util.ArrayList;

/**
 * Clase encargada de construir las transiciones del autómata a partir de la
 * ventana de construcción de transiciones.
 */
public class Handler_ConstruirTransiciones {

    private Handler_Automata controllerAutomata;
    private Automata automata;

    /**
     * Constructor de la clase
     * @param automata autómata sobre el cual se construyen las transiciones.
     */
    public Handler_ConstruirTransiciones(Automata automata){
        this.automata=automata;
        controllerAutomata = new Handler_Automata(automata);
    }

    /**
     * Guarda las transiciones hechas en la ventana de construcción de transiciones en el autómata.
     * @param data Datos con las transiciones obtenidas de la ventana de construcción de transiciones.
     * @param bandera Controlodar.
     */

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

    /**
     * Retorna la posición relativa de un estado dentro de la colección de estados del autómata.
     * @param valorEstado Nombre del estado del cual se desea conocer su posición relativa dentro de
     *                    la colección de estados del autómata.
     * @return Posición del estado.
     */
    public int direccionEstado(String valorEstado){
        ArrayList<Estado> estados = automata.getEstados();
        for (int i=0;i<estados.size();i++){
            if(estados.get(i).getNombre().equals(valorEstado)){
                return i;
            }
        }
        return -1;
    }

    /**
     * Valida que las transiciones especificadas en la tabla de construcción de transiciones
     * sean correctas.
     * @param data Datos de las transiciones.
     * @param bandera Controlador.
     * @return Transiciones validas.
     */
    public boolean validarTransicionesCorrectas(ObservableList data,int bandera){
        Handler_Automata handler_automata = new Handler_Automata(automata);
        for (int i=0;i<data.size();i++){
            if (data.get(i) instanceof String[]) {
                String[] t = (String[]) data.get(i);
                for (int j=1;j<t.length-bandera;j++) {
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

    /**
     * Valida que las transiciones de un estado de error sean hacia ese mismo estado.
     * @param data Datos con las transiciones obtenidas en la ventana de construcción de
     *             tansiciones.
     * @return Transiciones del estado de error validas.
     */
    public boolean validarTransicionesError(ObservableList data){
        Handler_Automata handler_automata = new Handler_Automata(automata);
        for (int i=0;i<data.size();i++){
            if (data.get(i) instanceof String[]) {
                String[] t = (String[]) data.get(i);
                Estado e = handler_automata.obtenerEstadosDeString(t[0]);
                if(e.is_error()){
                    for (int j=1;j<t.length;j++) {
                        String vectorEstados[] = t[j].split(",");
                        for (String cadena:vectorEstados){
                            if(!cadena.equals(e.getNombre())){
                                return false;
                            }
                        }
                    }
                }
            }
        }
        return true;
    }

    /**
     * Restaura las transiciones de los estados del autómata.
     */
    public void vaciarTransiciones(){
        for (Estado e: automata.getEstados()){
            e.getTransiciones().clear();
        }
    }

}
