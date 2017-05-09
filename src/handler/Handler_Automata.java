package handler;

import model.Automata;
import model.Estado;
import model.Transicion;

import java.util.ArrayList;

/**
 * Created by Sebas on 1/04/2017.
 */
public class Handler_Automata {

    private Automata automata;

    public Handler_Automata(Automata automata){
        this.automata = automata;
    }

    public void setAutomata(Automata automata){
        this.automata=automata;
    }

    public Estado obtenerEstadoInicial(){
        Estado e = null;
        for (int i = 0; i < automata.getEstados().size(); i++) {
            e = automata.getEstados().get(i);
            if(e.isEsInicial()){
                return automata.getEstados().get(i);
            }
        }
        return e;
    }

    public boolean reconocerSecuencia(String s){
        if(validarSecuencia(s)){
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
                t = estadoActual.getTransiciones().get(j);
                if(t.getSimbolo().equals(actualSimbolo)){
                    estadoActual = t.getEstadosFinales().get(0);
                    if(estadoActual.isEsError()){
                        return false;
                    }
                    j=0;
                    i++;
                }else{
                    j++;
                }

            }
            return estadoActual.isEsAceptacion();
        }else{
            return false;
        }
    }

    private boolean validarSecuencia(String cadena) {
        String concatSimbolos="";
        for (String simbolos : automata.getSimbolos()) {
            concatSimbolos+=simbolos;
        }
        for(int i=0;i<cadena.length();i++) {
            if(!concatSimbolos.contains(""+cadena.charAt(i)))
                return false;
        }
        return true;
    }

    public boolean esDeterministico() {
        int cont = 0;
        for (int i = 0; i < automata.getEstados().size(); i++) {
            if (automata.getEstados().get(i).isEsInicial()) {
                cont++;
            }
        }
        if (cont >=2){
            return false;
        }else{
            for (int i = 0; i < automata.getEstados().size(); i++) {
                Estado e = automata.getEstados().get(i);
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

    public void simplificarAutomata(){
        ArrayList<Estado> sinEstadosExtranios = quitarEstadosExtranios();
        ArrayList<ArrayList<Estado>> estadosSimplificados = quitarEstadosEquivalentes(sinEstadosExtranios);
        convertirEnNuevosEstados(estadosSimplificados);
        sortEstadoInicial();
        imprimirAutomata();
    }

    private void convertirEnNuevosEstados(ArrayList<ArrayList<Estado>> estadosSimplificados) {
        ArrayList<Estado> estados = new ArrayList<>();
        Estado nuevo;
        for (int i=0;i<estadosSimplificados.size();i++){
            String nombre = concatenarNombres(estadosSimplificados.get(i));
            nuevo = new Estado(nombre);
            nuevo.setEsAceptacion(validarAceptacion(estadosSimplificados,i));
            nuevo.setEsInicial(validarInicial(estadosSimplificados,i));
            estados.add(nuevo);
        }
        validarTransiciones(estadosSimplificados,estados);
        automata.setEstados(estados);
    }

    private String concatenarNombres(ArrayList<Estado> estados){
        String apariciones="";
        for (int i = 0; i < estados.size(); i++) {
            String vector[] = estados.get(i).getNombre().split("-");
            for (int j = 0; j < vector.length; j++) {
                if(!apariciones.contains(vector[j])){
                    apariciones += vector[j];
                }
            }
        }
        return apariciones;
    }


    public void validarTransiciones(ArrayList<ArrayList<Estado>> estadosSimplificados,ArrayList<Estado> estados){
        ArrayList<Transicion> transiciones;
        Transicion nuevaTransicion;
        for (int i=0;i<estadosSimplificados.size();i++){
            transiciones = new ArrayList<>();
            ArrayList<Transicion> transicionesSimplificadas=estadosSimplificados.get(i).get(0).getTransiciones();
            for(int j=0;j<transicionesSimplificadas.size();j++){
                String valor = transicionesSimplificadas.get(j).getEstadosFinales().get(0).getNombre();
                int indiceTransicion=dondeEstaLaTransicion(estadosSimplificados,valor);
                nuevaTransicion = new Transicion(transicionesSimplificadas.get(j).getSimbolo());
                nuevaTransicion.agregarEstadoFinal(estados.get(indiceTransicion));
                transiciones.add(nuevaTransicion);
            }
            estados.get(i).setTransiciones(transiciones);
        }
    }

    public boolean validarAceptacion(ArrayList<ArrayList<Estado>> estadosSimplificados,int i){
        int j=0;
        while (j<estadosSimplificados.get(i).size() && !estadosSimplificados.get(i).get(j).isEsAceptacion()){
            j++;
        }
        return j<estadosSimplificados.get(i).size();
    }

    public boolean validarInicial(ArrayList<ArrayList<Estado>> estadosSimplificados,int i){
        int j=0;
        while (j<estadosSimplificados.get(i).size() && !estadosSimplificados.get(i).get(j).isEsInicial()){
            j++;
        }
        return j<estadosSimplificados.get(i).size();
    }

    private ArrayList<Estado> quitarEstadosExtranios() {
        ArrayList<Estado> estados = new ArrayList<>();
        Estado nuevoEstado = automata.getEstados().get(0);
        estados.add(nuevoEstado);
        boolean bandera=true;
        int seguimientoEstado=0;
        while (seguimientoEstado<estados.size()){
            for (int i=0;i<estados.get(seguimientoEstado).getTransiciones().size();i++){
                String transicion= estados.get(seguimientoEstado).getTransiciones().get(i).getEstadosFinales().get(0).getNombre();
                if(!buscarEstado(transicion,estados)){
                    nuevoEstado = estados.get(seguimientoEstado).getTransiciones().get(i).getEstadosFinales().get(0);
                    estados.add(nuevoEstado);
                }
            }
            seguimientoEstado++;
        }
        return estados;
    }

    public boolean buscarEstado(String simboloBusqueda,ArrayList<Estado> arrayBusqueda){
        for (int i=0;i<arrayBusqueda.size();i++){
            if(arrayBusqueda.get(i).getNombre().equals(simboloBusqueda)){
                return true;
            }
        }
        return false;
    }

    public ArrayList<ArrayList<Estado>> quitarEstadosEquivalentes(ArrayList<Estado> estados){
        ArrayList<ArrayList<Estado>> estadosFinales = new ArrayList<>();
        ArrayList<Estado> pCero= new ArrayList<>();;
        ArrayList<Estado> pUno= new ArrayList<>();
        for(int i=0;i<estados.size();i++){
            if(estados.get(i).isEsAceptacion()){
                pUno.add(estados.get(i));
            }else{
                pCero.add(estados.get(i));
            }
        }
        estadosFinales.add(pCero);
        estadosFinales.add(pUno);
        int seguimientoEstadosFinales=0;
        boolean bandera=false;
        while (seguimientoEstadosFinales<estadosFinales.size() &&!bandera){
            int j=0;
            while (j<automata.getSimbolos().length && !bandera){
                ArrayList<String> nombresEstados = new ArrayList<>();
                int i=0;
                while (i<estadosFinales.get(seguimientoEstadosFinales).size()) {
                    ArrayList<Transicion> transicions = estadosFinales.get(seguimientoEstadosFinales).get(i).getTransiciones();
                    nombresEstados.add(transicions.get(j).getEstadosFinales().get(0).getNombre());
                    i++;
                }
                ArrayList<Estado> temporal = validarConjuntosDeEstados(seguimientoEstadosFinales,estadosFinales,nombresEstados);
                if(temporal.size()!=0){
                    estadosFinales.add(temporal);
                    seguimientoEstadosFinales=0;
                    bandera=true;
                }else{
                    j++;
                }
            }
            if(!bandera)seguimientoEstadosFinales++;
            bandera=false;
            quitarEstadosNulos(estadosFinales);
        }
        return estadosFinales;
    }

    private void quitarEstadosNulos(ArrayList<ArrayList<Estado>> estadosFinales) {
        for (int i=0;i<estadosFinales.size();i++){
            if(estadosFinales.get(i).size()==0){
                estadosFinales.remove(i);
            }
        }
    }

    public ArrayList<Estado> validarConjuntosDeEstados(int d,ArrayList<ArrayList<Estado>> estadosFinales,ArrayList<String> nombresEstados){
        ArrayList<Estado> nuevoArray= new ArrayList<>();
        boolean bandera=false;
        int x=-1;
        int j=0;
        if(!validarQueTodosEsten(estadosFinales,nombresEstados)){
            while (!bandera && j<nombresEstados.size()){
                if(!buscarEstado(nombresEstados.get(j),estadosFinales.get(d))) {
                    nuevoArray.add(estadosFinales.get(d).get(j));
                    estadosFinales.get(d).remove(estadosFinales.get(d).get(j));
                    x = buscarEnLista(estadosFinales, nombresEstados.get(j));
                    nombresEstados.remove(j);
                    bandera = true;
                }else{
                    j++;
                }
            }
            if(bandera){
                while (j<nombresEstados.size()){
                    if(buscarEstado(nombresEstados.get(j),estadosFinales.get(x))){
                        nuevoArray.add(estadosFinales.get(d).get(j));
                        estadosFinales.get(d).remove(estadosFinales.get(d).get(j));
                        nombresEstados.remove(j);
                    }else{
                        j++;
                    }
                }
            }
        }
        return nuevoArray;
    }

    public int buscarEnLista(ArrayList<ArrayList<Estado>> estadosFinales,String nombreEstado){
        for (int i=0;i<estadosFinales.size();i++){
            if(buscarEstado(nombreEstado,estadosFinales.get(i))){
                return i;
            }
        }
        return -1;
    }

    public boolean validarQueTodosEsten(ArrayList<ArrayList<Estado>> estadosFinales,ArrayList<String> nombresEstados){
        int j=0;
        boolean bandera=true;
        int i=dondeEstaLaTransicion(estadosFinales,nombresEstados.get(0));
        bandera=true;
        while (bandera && j<nombresEstados.size()){
            if(!buscarEstado(nombresEstados.get(j),estadosFinales.get(i))) {
                bandera=false;
            }else{
                j++;
            }
        }
        return bandera;
    }

    public int dondeEstaLaTransicion(ArrayList<ArrayList<Estado>> estados,String valor){
        int i=0;
        boolean bandera=true;
        while (i<estados.size() && bandera){
            if(buscarEstado(valor,estados.get(i))){
                bandera=false;
            }else{
                i++;
            }
        }
        return i;
    }

    public void imprimirAutomata(){
        for (String simbolo:automata.getSimbolos()){
            System.out.print("\t"+simbolo);
        }
        System.out.println();
        for(Estado estado: automata.getEstados()){
            String e = estado.getNombre();
            System.out.print(e);
            for (Transicion transicion:estado.getTransiciones()){
                for(Estado eTrans:transicion.getEstadosFinales()){
                    System.out.print("\t"+eTrans.getNombre());
                }
            }
            System.out.println("\t"+estado.isEsAceptacion());

        }
    }

    public Estado obtenerEstadosDeString(String s){
        for (int i = 0; i < automata.getEstados().size(); i++) {
            if(automata.getEstados().get(i).getNombre().equals(s)){
                return automata.getEstados().get(i);
            }
        }
        return null;
    }

    private int masDeUnEstadoInicial(){
        int cont =0;
        for (int i = 0; i < automata.getEstados().size(); i++) {
            Estado e = automata.getEstados().get(i);
            if(e.isEsInicial()){
                cont ++;
            }
        }
        return cont;
    }

    public void convertirAutomataAFN(boolean operacion){
        if(!esDeterministico()){
            if(masDeUnEstadoInicial()>=2){
                setEstadosIniciales();
            }
            String nuevoEstado="";
            int i =0;
            while(i < automata.getEstados().size()) {
                Estado e = automata.getEstados().get(i);
                ArrayList<Transicion> t = e.getTransiciones();
                if (t.isEmpty()) {
                    setTransicionEstadoNuevo(e, operacion);
                    t = e.getTransiciones();
                }
                int j = 0;
                while (j < t.size()) {
                    if (t.get(j).getEstadosFinales().size() >= 2) {
                        int k = 0;
                        Estado e2;
                        while (k < t.get(j).getEstadosFinales().size()) {
                            nuevoEstado += t.get(j).getEstadosFinales().get(k).getNombre() + "-";
                            k++;
                        }
                        nuevoEstado = validarQueNoEsteString(nuevoEstado);
                        nuevoEstado = nuevoEstado.substring(0, nuevoEstado.length() - 1);
                        if (!buscarEstado(nuevoEstado, automata.getEstados())) {
                            e2 = new Estado(nuevoEstado);
                            t.get(j).getEstadosFinales().clear();
                            t.get(j).agregarEstadoFinal(e2);
                            automata.agregarEstado(e2);
                        }else{
                            e2=obtenerEstadosDeString(nuevoEstado);
                            t.get(j).getEstadosFinales().clear();
                            t.get(j).agregarEstadoFinal(e2);
                        }
                        nuevoEstado = "";
                    }

                    j++;
                }

                i++;
            }
        }
    }

    private void setEstadosIniciales(){
        String estadosIniciales="";
        for (int i = 0; i < automata.getEstados().size(); i++) {
            Estado e = automata.getEstados().get(i);
            if(e.isEsInicial()){
                estadosIniciales += e.getNombre()+"-";
                e.setEsInicial(false);
                e.set_inicial(false);
            }
        }
        estadosIniciales = estadosIniciales.substring(0,estadosIniciales.length()-1);
        Estado estadoNuevo = new Estado(estadosIniciales);
        estadoNuevo.set_inicial(true);
        automata.agregarEstado(estadoNuevo);
        sortEstadoInicial();
    }

    private void setTransicionEstadoNuevo(Estado e,boolean op){
        boolean aceptacion = op;
        String[] nombre = e.getNombre().split("-");
        ArrayList<Estado> estadosAgregar;
        ArrayList<Transicion> transicion=new ArrayList<>();
        for (int j = 0; j < automata.getSimbolos().length; j++) {
            Transicion t =new Transicion(automata.getSimbolos()[j]);
            String simboloTransicion = automata.getSimbolos()[j];
            estadosAgregar = new ArrayList<>();
            for (int i = 0; i < nombre.length; i++) {
                Estado e1 = obtenerEstadosDeString(nombre[i]);
                if(!op){
                    aceptacion = aceptacion|e1.isEsAceptacion();
                }else{
                    aceptacion = aceptacion&e1.isEsAceptacion();
                }
                Estado e2 = obtenerTransicionSimbolo(e1,simboloTransicion).getEstadosFinales().get(0);
                if(!estadosAgregar.contains(e2)){
                    estadosAgregar.add(e2);
                }
            }
            t.setEstadosFinales(estadosAgregar);
            transicion.add(t);
        }
        e.setEsAceptacion(aceptacion);
        e.set_aceptacion(aceptacion);
        e.setTransiciones(transicion);
    }

    public Transicion obtenerTransicionSimbolo(Estado e, String s){
        Transicion t;
        for (int i = 0; i < e.getTransiciones().size(); i++){
            t = e.getTransiciones().get(i);
            if(t.getSimbolo().equals(s)){
                return t;
            }
        }

        return null;
    }



    public String validarQueNoEsteString(String a){
        String b[] = a.split("-");
        String retornoVerdadero="";
        for (String i: b){
            if(!retornoVerdadero.contains(i)){
                retornoVerdadero +=i+"-";
            }
        }
        return retornoVerdadero;
    }

    public void sortEstadoInicial(){
        for (int i=0;i<automata.getEstados().size();i++){
            for(int j=i+1;j<automata.getEstados().size();j++){
                Estado aux;
                if(!automata.getEstados().get(i).is_inicial() && automata.getEstados().get(j).is_inicial()){
                    aux=automata.getEstados().get(i);
                    automata.getEstados().set(i,automata.getEstados().get(j));
                    automata.getEstados().set(j,aux);
                }
            }
        }
    }

    public void unirIntersectarAutomatas(Automata automata2, boolean op){
        for (int i = 0; i < automata2.getEstados().size(); i++) {
            automata.agregarEstado(automata2.getEstados().get(i));
        }

        convertirAutomataAFN(op);
        simplificarAutomata();
    }

    public boolean validarSimbolos(Automata a){
      String simbolos = "";
        for (int i = 0; i < a.getSimbolos().length; i++) {
            simbolos += a.getSimbolos()[i];
        }
        for (int i = 0; i < automata.getSimbolos().length; i++) {
            if(!simbolos.contains(automata.getSimbolos()[i])){
                return false;
            }
        }
        return true;
    }

}
