package handler;

import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Automata;
import model.Estado;
import model.Transicion;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by Familia on 28/03/2017.
 */
public class HandlerFile {

    private FileChooser fileChooser;
    private ArrayList<Estado> estadosObjecto;
    private String[] simbolos;
    private File file;
    private Stage stage;

    public HandlerFile(Stage s){
        stage = s;
    }

    public void crearAutomata() throws IOException {
        fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("text", "*.txt")
        );
        file = fileChooser.showOpenDialog(stage);
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
        int i =1;
        try {
            fr = new FileReader(file);
            br = new BufferedReader(fr);
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
        }finally {
            try {
                br.close();
            } catch (Exception e2) {
                System.out.println(e2);
            }
        }
        Automata.getInstance().setEstados(estadosObjecto);
        Automata.getInstance().setSimbolos(simbolos);
    }

    private ArrayList<Estado> CrearEstados(String[] s){
        ArrayList<Estado> a = new ArrayList<>();
        Estado e;
        for (int i = 0; i < s.length; i++) {
            String id = s[i].substring(0,1);
            switch (id){
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



    public void guardarAutomata(){
        FileChooser fileChooser = new FileChooser();
        file = fileChooser.showSaveDialog(stage);
        String linea="";
        int i;
        try{
            FileWriter w = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(w);
            for ( i = 0; i < Automata.getInstance().getEstados().size(); i++) {
                Estado e = Automata.getInstance().getEstados().get(i);
                if(e.isEsAceptacion()){
                    if(e.isEsInicial()){
                        linea += "$"+e.getNombre()+",";
                    }else{
                        linea += "*"+e.getNombre()+",";
                    }

                }else{
                    if (e.isEsError()){
                        linea += "%"+e.getNombre()+",";
                    }else{
                        if (e.isEsInicial()){
                            linea += "!"+e.getNombre()+",";
                        }else{
                            linea +=e.getNombre()+",";
                        }

                    }

                }

            }
            linea = linea.substring(0,linea.length()-1);
            bw.write(linea);
            bw.newLine();
            linea = "";
            String b;
            for (i = 0; i < simbolos.length; i++) {
                b = simbolos[i];
                linea += b+",";
            }
            linea = linea.substring(0,linea.length()-1);
            bw.write(linea);
            bw.newLine();
            linea = "";
            for (i = 0; i <  Automata.getInstance().getEstados().size(); i++) {
                Estado estadoActual = Automata.getInstance().getEstados().get(i);
                int ts = estadoActual.getTransiciones().size();
                linea += estadoActual.getNombre()+"-";
                for (int j = 0; j < ts; j++) {
                    Transicion transActual = estadoActual.getTransiciones().get(j);
                    linea += transActual.getSimbolo()+":";
                    int eft = transActual.getEstadosFinales().size();
                    for (int k = 0; k < eft ; k++) {
                        Estado estadoFinalActual = transActual.getEstadosFinales().get(k);
                        linea += estadoFinalActual.getNombre()+",";
                    }
                    linea = linea.substring(0,linea.length()-1);
                    System.out.println(linea);
                    bw.write(linea);
                    bw.newLine();
                    linea = estadoActual.getNombre()+"-";
                }
                linea="";
            }
            bw.close();
        }catch(IOException e){
            System.out.println(e);
        }

    }

}


