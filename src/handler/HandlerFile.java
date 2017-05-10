package handler;

import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Automata;
import model.Estado;
import model.Transicion;

import java.io.*;
import java.util.ArrayList;

/**
 * Clase que se encarga de cargar un autómata desde un archivo de texto, y de guardar un autómata
 * en un archivo de texto.
 */
public class HandlerFile {

    private FileChooser fileChooser;
    private ArrayList<Estado> estadosObjecto;
    private String[] simbolos;
    private File file;
    private Stage stage;

    /**
     * Constructor de la clase.
     *
     * @param s Ventana para selecionar el archivo
     */
    public HandlerFile(Stage s) {
        stage = s;
    }

    /**
     * Constructor vacio.
     */
    public HandlerFile() {
    }

    /**
     * Crea el autómata de acuerdo al archivo leido de disco.
     *
     * @param path Ruta del archivo que contiene el autómata.
     * @return Autómata.
     * @throws IOException
     */
    public Automata crearAutomata(String path) throws IOException {
        if (path.equals("")) {
            fileChooser = new FileChooser();
            fileChooser.setTitle("Cargar Archivo del Autómata");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("text", "*.txt")
            );
            file = fileChooser.showOpenDialog(stage);
        } else {
            file = new File(path);
        }
        FileReader fr = null;
        BufferedReader br = null;
        String linea;
        String[] estados;
        String[] splitInicial;
        String parte1;
        String parte2;
        String[] splitFinal;
        Transicion t;
        Estado e;
        int i = 1;
        try {
            fr = new FileReader(file);
            br = new BufferedReader(fr);
            linea = br.readLine();
            linea = quitarEspacios(linea);
            while (linea != null) {
                switch (i) {
                    case 1:
                        estados = linea.split(",");
                        estadosObjecto = CrearEstados(estados);
                        i++;
                        break;
                    case 2:
                        simbolos = linea.split(",");
                        i++;
                        break;
                    default:
                        splitInicial = linea.split(":");
                        parte1 = splitInicial[0];
                        parte2 = splitInicial[1];
                        splitInicial = parte1.split("-");
                        splitFinal = parte2.split(",");
                        e = obtenerEstado(splitInicial[0]);
                        if (e != null && existeSimboloEntrada(splitInicial[1])) {
                            t = crearTransiciones(splitFinal, splitInicial[1]);
                            e.addTransicion(t);
                        } else {
                            System.out.println("Estado o simbolo en la linea " + linea + " no existe");
                        }
                }
                linea = br.readLine();
                if (linea != null) {
                    linea = quitarEspacios(linea);
                }

            }
        } catch (Exception ex) {
            System.out.println(ex);
            ;
        } finally {
            try {
                br.close();
            } catch (Exception e2) {
                System.out.println(e2);
            }
        }
        return new Automata(estadosObjecto, simbolos);
    }

    /**
     * Retira los caracteres en blanco que pueda tener cada linea que se va leyendo del archivo
     *
     * @param s String de la linea leida.
     * @return String sin espacios.
     */
    private String quitarEspacios(String s) {
        String nuevoString = "";
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) != ' ') {
                nuevoString += s.charAt(i);
            }
        }
        return nuevoString;
    }

    /**
     * Comprueba que los símbolos de entrada asociados a una transición realmente existan
     * en el archivo de construcción del autómata.
     *
     * @param a Símbolo a evaluar.
     * @return Existe símbolo de entrada.
     */
    private boolean existeSimboloEntrada(String a) {
        for (int i = 0; i < simbolos.length; i++) {
            if (simbolos[i].equals(a)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Crea los estados del autómata a partir del archivo de configuración del
     * autómata teniendo en cuenta la nomenclatura propia para designar si se trata
     * de un estado de aceptación, error inicial.
     *
     * @param s String (leido del archivo de configuración) con los estados a crear.
     */
    private ArrayList<Estado> CrearEstados(String[] s) {
        ArrayList<Estado> a = new ArrayList<>();
        Estado e;
        for (int i = 0; i < s.length; i++) {
            String id = s[i].substring(0, 1);
            switch (id) {
                case "*":  //estado de aceptacion
                    e = new Estado(s[i].substring(1));
                    e.setEsAceptacion(true);
                    break;
                case "!":  //estado inicial
                    e = new Estado(s[i].substring(1));
                    e.setEsInicial(true);
                    break;

                case "$": // estado de aceptacion e inicial
                    e = new Estado(s[i].substring(1));
                    e.setEsAceptacion(true);
                    e.setEsInicial(true);
                    break;
                case "%": //estado de error
                    e = new Estado(s[i].substring(1));
                    e.setEsError(true);
                    break;

                default:
                    e = new Estado(s[i]);
                    break;
            }
            a.add(e);
        }
        return a;
    }

    /**
     * Construye la respectiva transición que hace determinado estado dependiendo del símbolo de entrada.
     *
     * @param estadosFinales String(leido del archivo de configuración del autómata) con los estados
     *                       hacia los cuales se debe hacer transición dependiendo del símbolo de entrada que se tenga.
     * @param simbolo        Símbolo de entrada para la respectiva transición.
     * @return Transición.
     */
    private Transicion crearTransiciones(String[] estadosFinales, String simbolo) {
        Transicion t = new Transicion(simbolo);
        for (int i = 0; i < estadosFinales.length; i++) {
            Estado e = obtenerEstado(estadosFinales[i]);
            if (e != null) {
                t.agregarEstadoFinal(e);
            }
        }

        return t;
    }

    /**
     * Retorna el estado correspondiente al parámetro de entrada.
     *
     * @param a Nombre del estado  que se desea retornar su respectiva referencia.
     * @return Estado.
     */
    private Estado obtenerEstado(String a) {
        Estado e = null;
        for (int i = 0; i < estadosObjecto.size(); i++) {
            e = estadosObjecto.get(i);
            if (e.getNombre().equals(a)) {
                return e;
            }
        }
        return e;
    }

    /**
     * Guarda un autómata temporal cuando se carga un segundo automáta cuando se desea hacer la unión
     * o la intersección de ambos o bien cuando se desea simplemente guardar el autómata en dicho formato.
     *
     * @param automata Autómata que será guardado de manera temporal en formato de texto.
     * @param bandera  Bandera que determina si se está guardando una copia temporal para posterior unión o intersección, o si se
     *                 guarda el autómata en formato texto para darle persistencia.
     */
    public void guardarAutomata(Automata automata, boolean bandera) {
        simbolos = automata.getSimbolos();
        if (bandera) {
            FileChooser fileChooser = new FileChooser();
            file = fileChooser.showSaveDialog(stage);
        } else {
            file = new File("./src/temporal/temporal.txt");
        }
        String linea = "";
        int i;
        try {
            FileWriter w = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(w);
            for (i = 0; i < automata.getEstados().size(); i++) {
                Estado e = automata.getEstados().get(i);
                if (e.isEsAceptacion()) {
                    if (e.isEsInicial()) {
                        linea += "$" + e.getNombre() + ",";
                    } else {
                        linea += "*" + e.getNombre() + ",";
                    }

                } else {
                    if (e.isEsError()) {
                        linea += "%" + e.getNombre() + ",";
                    } else {
                        if (e.isEsInicial()) {
                            linea += "!" + e.getNombre() + ",";
                        } else {
                            linea += e.getNombre() + ",";
                        }

                    }

                }

            }
            linea = linea.substring(0, linea.length() - 1);
            bw.write(linea);
            bw.newLine();
            linea = "";
            String b;
            for (i = 0; i < simbolos.length; i++) {
                b = simbolos[i];
                linea += b + ",";
            }
            linea = linea.substring(0, linea.length() - 1);
            bw.write(linea);
            bw.newLine();
            linea = "";
            for (i = 0; i < automata.getEstados().size(); i++) {
                Estado estadoActual = automata.getEstados().get(i);
                int ts = estadoActual.getTransiciones().size();
                linea += estadoActual.getNombre() + "-";
                for (int j = 0; j < ts; j++) {
                    Transicion transActual = estadoActual.getTransiciones().get(j);
                    linea += transActual.getSimbolo() + ":";
                    int eft = transActual.getEstadosFinales().size();
                    for (int k = 0; k < eft; k++) {
                        Estado estadoFinalActual = transActual.getEstadosFinales().get(k);
                        linea += estadoFinalActual.getNombre() + ",";
                    }
                    linea = linea.substring(0, linea.length() - 1);
                    System.out.println(linea);
                    bw.write(linea);
                    bw.newLine();
                    linea = estadoActual.getNombre() + "-";
                }
                linea = "";
            }
            bw.close();
        } catch (IOException e) {
            System.out.println(e);
        }

    }

}


