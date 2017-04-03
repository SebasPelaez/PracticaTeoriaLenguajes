package handler;

import model.Automata;
import model.Estado;
import model.Transicion;

import java.util.ArrayList;

/**
 * Created by Sebas on 1/04/2017.
 */
public class Handler_Automata {

    Automata automata = Automata.getInstance();

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
            System.out.println(actualSimbolo);
            t = estadoActual.getTransiciones().get(j);
            System.out.println("Estado actual: "+estadoActual.getNombre());
            if(t.getSimbolo().equals(actualSimbolo)){
                estadoActual = t.getEstadosFinales().get(0);
                j=0;
                i++;
            }else{
                j++;
            }

        }
        return estadoActual.isEsAceptacion();

    }

    public boolean esDeterministico() {
        int cont = 1;
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
        imprimirAutomata();
        System.out.println("---------");
        ArrayList<Estado> sinEstadosExtraños = quitarEstadosExtraños();
        ArrayList<ArrayList<Estado>> estadosSimplificados = quitarEstadosEquivalentes(sinEstadosExtraños);
        convertirEnNuevosEstados(estadosSimplificados);
        imprimirAutomata();
    }

    private void convertirEnNuevosEstados(ArrayList<ArrayList<Estado>> estadosSimplificados) {
        ArrayList<Estado> estados = new ArrayList<>();
        Estado nuevo;
        for (int i=0;i<estadosSimplificados.size();i++){
            nuevo = new Estado("Estado"+(i+1));
            nuevo.setEsAceptacion(validarAceptacion(estadosSimplificados,i));
            nuevo.setEsInicial(validarInicial(estadosSimplificados,i));
            estados.add(nuevo);
        }
        validarTransiciones(estadosSimplificados,estados);
        Automata.getInstance().setEstados(estados);
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

    private ArrayList<Estado> quitarEstadosExtraños() {
        ArrayList<Estado> estados = new ArrayList<>();
        Estado nuevoEstado = Automata.getInstance().getEstados().get(0);
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
            while (j<Automata.getInstance().getSimbolos().length && !bandera){
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
                if(!buscarEstado(nombresEstados.get(j),estadosFinales.get(0))) {
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
                    }
                    j++;
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
        for (String simbolo:Automata.getInstance().getSimbolos()){
            System.out.print("\t"+simbolo);
        }
        System.out.println();
        for(Estado estado: Automata.getInstance().getEstados()){
            String e = estado.getNombre();
            System.out.print(e);
            for (Transicion transicion:estado.getTransiciones()){
                System.out.print("\t"+transicion.getEstadosFinales().get(0).getNombre());
            }
            System.out.println();
        }
    }
}
