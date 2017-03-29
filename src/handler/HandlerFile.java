package handler;

import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Automata;
import model.Estado;
import model.Transicion;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

/**
 * Created by Familia on 28/03/2017.
 */
public class HandlerFile {

    private FileChooser fileChooser;
    private ArrayList<Estado> estadosObjecto;
    private String[] simbolos;
    public HandlerFile(Stage s){
        fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("text", "*.txt")
        );
        File file = fileChooser.showOpenDialog(s);
        procesarAutomata(file);
        returnAutomata(estadosObjecto,simbolos);
    }

    private void procesarAutomata(File f){
        FileReader fr = null;
        String linea;
        String[] estados;
        String[] splitInicial;
        String parte1;
        String parte2;
        String[] splitFinal;
        Transicion t;
        Estado e;
        int i =1;
        try {
            fr = new FileReader(f);
            BufferedReader br = new BufferedReader(fr);
            linea = br.readLine();
            while(linea!= null) {
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
                        t = crearTransiciones(splitFinal,splitInicial[1]);
                        if(e!= null){
                            e.addTransicion(t);
                        }else{
                            System.out.println("ese estado no existe");
                        }

                }
                linea = br.readLine();
            }
        } catch (Exception ex) {
            System.out.println("No existe el archivo");;
        }
    }

    private ArrayList<Estado> CrearEstados(String[] s){
        ArrayList<Estado> a = new ArrayList<>();
        Estado e;
        for (int i = 0; i < s.length; i++) {
            String id = s[i].substring(0,1);
            switch (id){
                case "*":
                    e = new Estado(s[i].substring(1));
                    e.setEsAceptacion(true);
                    break;
                case "!":
                    e = new Estado(s[i].substring(1));
                    e.setEsInicial(true);
                    break;

                case "$":
                    e = new Estado(s[i].substring(1));
                    e.setEsAceptacion(true);
                    e.setEsInicial(true);
                    break;
                case "%":
                    e = new Estado(s[i].substring(1));
                    e.setEsError(true);
                    break;

                default:
                    e = new Estado(id);
                break;
            }
            a.add(e);
        }
        return a;
    }

    private Transicion crearTransiciones(String[] estadosFinales,String simbolo){
        Transicion t = new Transicion(simbolo);
        for (int i = 0; i < estadosFinales.length; i++) {
            Estado e = obtenerEstado(estadosFinales[i]);
            if(e!=null){
                t.agregarEstadoFinal(e);
            }
        }

        return t;
    }

    private Estado obtenerEstado(String a){
        Estado e = null;
        for (int i = 0; i < estadosObjecto.size(); i++) {
            e = estadosObjecto.get(i);
            if(e.getNombre().equals(a)){
                return e;
            }
        }
        return e;
    }

    public Automata returnAutomata(ArrayList<Estado> estados, String[] simbolosIN){
        return new Automata(estados,simbolosIN);
    }


}
