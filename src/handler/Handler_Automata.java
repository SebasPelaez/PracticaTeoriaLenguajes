package handler;

import model.Automata;
import model.Estado;
import model.Transicion;

import java.util.ArrayList;

/**
 * Created by Sebas y Juan on 1/04/2017.
 */
public class Handler_Automata {

    private Automata automata;

    /**
     * Constructor.
     * @param automata El autómata que setearemos.
     */
    public Handler_Automata(Automata automata){
        this.automata = automata;
    }

    public void setAutomata(Automata automata){
        this.automata=automata;
    }

    /**
     * Retorna el primer estado incicial.
     * @return Estado inicial.
     */
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

    /**
     * Reconoce una hilera de caracteres ingresada al autómata.
     * @param s hilera a ser reconocida por el autómata.
     * @return Hilera aceptada o rechaza.
     */
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

    /**
     * Valida que los símbolos de la hilera a reconocer esten en la colección de
     * símbolos de entrada del autómata.
     * @param cadena Hilera  que será procesada y a la cual se le verificarán sus caracteres.
     * @return Hilera valida/rechazada.
     */
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

    /**
     * Verifica si el autómata es determinístico.
     * @return Verdadero en caso de que si lo sea, falso de lo contrario.
     */
    public boolean esDeterministico() {
        int cont = 0;
        for (int i = 0; i < automata.getEstados().size(); i++) {
            if (automata.getEstados().get(i).isEsInicial()) {
                cont++;
            }
        }
        if (cont >=2){//Si tiene mas de dos estados iniciales no.
            return false;
        }else{
            for (int i = 0; i < automata.getEstados().size(); i++) {//verifica que no tenga transiciones a mas de un estado.
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

    /**
     * Simplifica el autómata.
     */
    public void simplificarAutomata(){
        ArrayList<Estado> sinEstadosExtranios = quitarEstadosExtranios();//Guarda un arraylist sin los estados extraños
        ArrayList<ArrayList<Estado>> estadosSimplificados = quitarEstadosEquivalentes(sinEstadosExtranios);//Guarda los conjuntos de estados.
        convertirEnNuevosEstados(estadosSimplificados);
        sortEstadoInicial();//pone el estado inicial de primero
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

    /**
     * Concatena los nombres del array de estados que ingresa.
     * @param estados array de estados.
     * @return Nombre de los estados concatenado.
     */
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


    /**
     * Indica hacia que punto deben ir las transiciones creadas en los diferentes conjuntos de estaods.
     * @param estadosSimplificados Todos los conjuntos de estados.
     * @param estados EL estado al cual se le setearan las transiciones.
     */
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

    /**
     * Verifica si el conjunto de estados es de aceptación.
     * @param estadosSimplificados El conjunto de estados.
     * @param i Indica cual subconjunto de estados se va a analizar.
     * @return Verdadero en caso de que el sbconjunto sea de aceptación.
     */
    public boolean validarAceptacion(ArrayList<ArrayList<Estado>> estadosSimplificados,int i){
        int j=0;
        while (j<estadosSimplificados.get(i).size() && !estadosSimplificados.get(i).get(j).isEsAceptacion()){
            j++;
        }
        return j<estadosSimplificados.get(i).size();
    }

    /**
     * Verifica si el conjunto de estados es inicial.
     * @param estadosSimplificados El conjunto de estados.
     * @param i Indica cual subconjunto de estados se va a analizar.
     * @return Verdadero en caso de que el sbconjunto sea inicial.
     */
    public boolean validarInicial(ArrayList<ArrayList<Estado>> estadosSimplificados,int i){
        int j=0;
        while (j<estadosSimplificados.get(i).size() && !estadosSimplificados.get(i).get(j).isEsInicial()){
            j++;
        }
        return j<estadosSimplificados.get(i).size();
    }

    /**
     * Verifica a cuales estados nunca se accede y los quita.
     * @return ArrayList con todos los estados que no son extraños, osea a los que si se puede llegar.
     */
    private ArrayList<Estado> quitarEstadosExtranios() {
        ArrayList<Estado> estados = new ArrayList<>();
        Estado nuevoEstado = automata.getEstados().get(0);
        estados.add(nuevoEstado);
        boolean bandera=true;
        int seguimientoEstado=0;
        while (seguimientoEstado<estados.size()){//Hace esto mientras que haya estados por analizar.
            for (int i=0;i<estados.get(seguimientoEstado).getTransiciones().size();i++){//Valida en todas las posibles transiciones.
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

    /**
     * Busca un estado dentro de un conjunto de ellos.
     * @param simboloBusqueda El estado que quiero buscar.
     * @param arrayBusqueda El conjunto donde lo quiero buscar
     * @return Verdader si lo encuentra.
     */
    public boolean buscarEstado(String simboloBusqueda,ArrayList<Estado> arrayBusqueda){
        for (int i=0;i<arrayBusqueda.size();i++){
            if(arrayBusqueda.get(i).getNombre().equals(simboloBusqueda)){
                return true;
            }
        }
        return false;
    }

    /**
     * Crea grupos de estados que son equivalentes y que se pueden reemplazar por uno.
     * @param estados Todos los estados que conforman el automara.
     * @return Un conjunto, con conjuntos de estados equivalentes.
     */
    public ArrayList<ArrayList<Estado>> quitarEstadosEquivalentes(ArrayList<Estado> estados){
        ArrayList<ArrayList<Estado>> estadosFinales = new ArrayList<>();
        ArrayList<Estado> pCero= new ArrayList<>();;
        ArrayList<Estado> pUno= new ArrayList<>();
        for(int i=0;i<estados.size();i++){//Pone todos lo estados de aceptación en un conjunto y los de rechazo en otro.
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
        while (seguimientoEstadosFinales<estadosFinales.size() &&!bandera){//mientras haya conjuntos que analizar.
            int j=0;
            while (j<automata.getSimbolos().length && !bandera){ //Dentro de todas las transiciones.
                ArrayList<String> nombresEstados = new ArrayList<>();
                int i=0;
                while (i<estadosFinales.get(seguimientoEstadosFinales).size()) { //Obtiene los nombres de los estados a los cuales va un cojunto determinado.
                    ArrayList<Transicion> transicions = estadosFinales.get(seguimientoEstadosFinales).get(i).getTransiciones();
                    nombresEstados.add(transicions.get(j).getEstadosFinales().get(0).getNombre());
                    i++;
                }
                ArrayList<Estado> temporal = validarConjuntosDeEstados(seguimientoEstadosFinales,estadosFinales,nombresEstados);
                if(temporal.size()!=0){//Si el subconjunto de estados no se separo entonces avanza con el siguiente subconjunto.
                    estadosFinales.add(temporal);
                    seguimientoEstadosFinales=0;
                    bandera=true;
                }else{
                    j++;
                }
            }
            if(!bandera)seguimientoEstadosFinales++;
            bandera=false;
            quitarEstadosNulos(estadosFinales);//Puede llegar a pasar que haya subconjuntos si estados, en ese caso estarian nulos, por ende se eliminan.
        }
        return estadosFinales;
    }

    /**
     * Los subconjuntos que esten nulos, se quitan.
     * @param estadosFinales El paquete con todos los conjuntos.
     */
    private void quitarEstadosNulos(ArrayList<ArrayList<Estado>> estadosFinales) {
        for (int i=0;i<estadosFinales.size();i++){
            if(estadosFinales.get(i).size()==0){
                estadosFinales.remove(i);
            }
        }
    }

    /**
     * Verifica que los subconjuntos de estados tengan transiciones a estados que esten dentro del mismo subconjunto.
     * @param d En cual Subconjunto estoy parado.
     * @param estadosFinales El paquete completo de todos los conjuntos de estados.
     * @param nombresEstados Los nombres de los estados a los cuales se dirige.
     * @return Un subconjunto de estados al cual todos su estados tienen transiciones dentro de si mismos.
     */
    public ArrayList<Estado> validarConjuntosDeEstados(int d,ArrayList<ArrayList<Estado>> estadosFinales,ArrayList<String> nombresEstados){
        ArrayList<Estado> nuevoArray= new ArrayList<>();
        boolean bandera=false;
        int x=-1;
        int j=0;
        if(!validarQueTodosEsten(estadosFinales,nombresEstados)){//Verifica que todos los estados tengan transiciones al mismo subconjunto.
            while (!bandera && j<nombresEstados.size()){// Si hay datos por analizar y no he encontrado uno que no pertenezca.
                if(!buscarEstado(nombresEstados.get(j),estadosFinales.get(d))) {//Si el estado tiene transición a un estado que esta en el mismo conjunto.
                    nuevoArray.add(estadosFinales.get(d).get(j));
                    estadosFinales.get(d).remove(estadosFinales.get(d).get(j));
                    x = buscarEnLista(estadosFinales, nombresEstados.get(j));
                    nombresEstados.remove(j);
                    bandera = true;
                }else{
                    j++;
                }
            }
            if(bandera){//si no encontro cambios.
                while (j<nombresEstados.size()){//Realice esto mientras haya datos por analizar.
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

    /**
     * Indica en cual subcinjunto de estados esta un conjunto.
     * @param estadosFinales Todos el paquete de conjuntos de estados.
     * @param nombreEstado EL estado que estoy buscando.
     * @return EL indice en del subconjunto en el cual esta el estado.
     */
    public int buscarEnLista(ArrayList<ArrayList<Estado>> estadosFinales,String nombreEstado){
        for (int i=0;i<estadosFinales.size();i++){
            if(buscarEstado(nombreEstado,estadosFinales.get(i))){
                return i;
            }
        }
        return -1;
    }

    /**
     * Controla que las transiciones de un subconjunto de estados vayan al mismo lado.
     * @param estadosFinales El paquete con todas los subconjuntos de estados.
     * @param nombresEstados El nombre de todos los estados a los cuales se crea transicion
     * @return verdadero si todos los estados estan en el mismo subconjunto.
     */
    public boolean validarQueTodosEsten(ArrayList<ArrayList<Estado>> estadosFinales,ArrayList<String> nombresEstados){
        int j=0;
        boolean bandera=true;
        int i=dondeEstaLaTransicion(estadosFinales,nombresEstados.get(0));//identifica a donde deben buscar.
        bandera=true;
        while (bandera && j<nombresEstados.size()){//Mientras los estados esten juntos y haya datos por analizar.
            if(!buscarEstado(nombresEstados.get(j),estadosFinales.get(i))) {
                bandera=false;
            }else{
                j++;
            }
        }
        return bandera;
    }

    /**
     * Mètodo que identifica en que subconjunto esta una estado.
     * @param estados El paquete con todos los subconjuntos de estados.
     * @param valor La transicin que estoy buscando.
     * @return EL subconjunto en el cual esta ese valor.
     */
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

    /**
     * Imprime el autómata en consola.
     */
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

    /**
     * Retorna la dirección de memoria del estado.
     * @param s El no,bre del estado.
     * @return La dirección de memoria.
     */
    public Estado obtenerEstadosDeString(String s){
        for (int i = 0; i < automata.getEstados().size(); i++) {
            if(automata.getEstados().get(i).getNombre().equals(s)){
                return automata.getEstados().get(i);
            }
        }
        return null;
    }

    /**
     * Verfica si el autómata tiene mas de un estado inicial.
     * @return Número de estado iniciales del autómata.
     */
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

    /**
     * Convierte el autómata a determinístico dado el caso que no lo sea.
     * @param operacion Variable auxiliar para el manejo de la unión o intersección del
     *                  autómata.
     */
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

    /**
     * Une los estados iniciales del autómata en un solo estado.
     */
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

    /**
     * Asigna a los estado nuevos que se van creando en el proceso de conversión una
     * nueva transición teniendo en cuenta los nombres de los estado con los que está compuesto.
     * @param e Estado al cual se le asignarán sus respectivas transiciones.
     * @param op Variable que controla la operación a realizar (unión o intersección).
     */
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

    /**
     * Obtiene la transición  de dicho estado correspondiente a ese símbolo de entrada.
     * @param e Estado del cual se desea extraer su respectiva transición de acuerdo al símbolo
     *          de entrada.
     * @param s Símbolo de entrada del cual se desea extraer la respectiva transición.
     * @return Transición del respectivo símbolo de entrada para ese estado.
     */
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


    /**
     * Caoncatena un estado con el nuevo nombre
     * @param a Un String mal formado.
     * @return El Estado bien nombrado.
     */
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

    /**
     * Ordena por estado inicial el automata, pone arriba los estados iniciales.
     */
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

    /**
     * Prepara los autómatas para su unión o intersección.
     * @param automata2 autómata que será unido o intersectado con el This.
     * @param op Operación que será realizada ( unión o intersección).
     */
    public void unirIntersectarAutomatas(Automata automata2, boolean op){
        for (int i = 0; i < automata2.getEstados().size(); i++) {
            automata.agregarEstado(automata2.getEstados().get(i));
        }
        convertirAutomataAFN(op);
        simplificarAutomata();
    }

    /**
     * Valida que cuando se haga una operación de unión o intersección entre 2
     * autómatas, estos tengan los mismos símbolos de entrada.
     * @param a Uno de los 2 autómatas a comparar (el otro es el this).
     * @return Simbolos coinsiden/no coinsiden.
     */
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
