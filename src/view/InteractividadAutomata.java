package view;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTextField;
import handler.HandlerFile;
import handler.Handler_Automata;
import handler.Handler_ConstruirTransiciones;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import model.Automata;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by Sebas on 3/04/2017.
 */
public class InteractividadAutomata implements Initializable {

    @FXML
    private TableView<String[]> tableViewNuevoAutomata;
    @FXML
    private TableView<String[]> tableView;
    @FXML
    private JFXCheckBox chkSiDeterministico;
    @FXML
    private JFXCheckBox chkNoDeterministico;
    @FXML
    private JFXTextField txtHileraEvaluar;
    @FXML
    private JFXButton btnConvertirDeterministico;
    @FXML
    private JFXButton btnUnir;
    @FXML
    private JFXButton btnIntersectar;

    @FXML
    private JFXButton btnGuardarEnDisco;

    @FXML
    private JFXButton btnGenerarPdf;

    @FXML
    private JFXButton btnSimplificar;

    @FXML
    private JFXButton btnNuevoAutomata;

    @FXML
    private JFXButton btnOtroAutomata;



    private Handler_Automata controlador;
    private int simbolosEntrada;
    private List<String[]> jdata;
    private ObservableList<String[]> datos;
    private Handler_ConstruirTransiciones controllerTransiciones;
    private Automata automata;
    private Automata automata2;
    private ArrayList<tableViewAutomata> automatas;
    private Alert alerta;
    private int focusAutomata;

    @FXML
    private void focusAutomataDos(MouseEvent evento) {
        focusAutomata=1;
        System.out.println(focusAutomata);
        tableView.setStyle("");
        tableViewNuevoAutomata.setStyle("-fx-border-color:#FFA726; -fx-border-width:4px;");
        cambiarColor("FFA726");
    }


    private void cambiarColor(String color){
        btnUnir.setStyle("-fx-background-color: #"+color+";");
        btnIntersectar.setStyle("-fx-background-color: #"+color+";");
        btnConvertirDeterministico.setStyle("-fx-background-color: #"+color+";");
        btnGuardarEnDisco.setStyle("-fx-background-color: #"+color+";");
        btnGenerarPdf.setStyle("-fx-background-color: #"+color+";");
        btnSimplificar.setStyle("-fx-background-color: #"+color+";");
        btnNuevoAutomata.setStyle("-fx-background-color: #"+color+";");
        btnOtroAutomata.setStyle("-fx-background-color: #"+color+";");
    }

    @FXML
    private void focusAutomataUno(MouseEvent evento) {
        focusAutomata=0;
        tableViewNuevoAutomata.setStyle("");
        tableView.setStyle("-fx-border-color:#03A9F4; -fx-border-width:4px;");
        cambiarColor("03A9F4");
    }




    @FXML
    private void convertirDeterministico(ActionEvent evento) {
        controlador.convertirAutomataAFN(false);
        controlador.imprimirAutomata();
    }

    @FXML
    private void guardarEnDisco(ActionEvent evento) {

        HandlerFile handlerFile = new HandlerFile((Stage) ((Node) evento.getSource()).getScene().getWindow());
        handlerFile.guardarAutomata(automata,true);
    }

    @FXML
    private void simplificar(ActionEvent evento) {
        controlador.imprimirAutomata();
        controlador.simplificarAutomata();
        //recargarTabla();
    }

    @FXML
    private void probarAutomata(ActionEvent evento) {
        alerta.setTitle("Alerta");
        alerta.getDialogPane().getChildren().stream().filter(node -> node instanceof Label).forEach(node -> ((Label) node).setMinHeight(Region.USE_PREF_SIZE));
        if (!txtHileraEvaluar.getText().equals("")) {
            if (controlador.reconocerSecuencia(txtHileraEvaluar.getText())) {
                alerta.setContentText("La secuencia es aceptada.");
                alerta.showAndWait();
            } else {
                alerta.setContentText("La secuencia no es aceptada.");
                alerta.showAndWait();
            }
        } else {
            alerta.setContentText("Debes ingresar una cadena para probar el automata");
            alerta.showAndWait();
        }
    }

    @FXML
    private void generarPdf(ActionEvent evento) {
        Node source = (Node) evento.getSource();
        Parent a = source.getParent();
        print(source);
    }

    @FXML
    private void operarAutomatas(ActionEvent evento){
        if(evento.getSource()== btnUnir){
            controlador.unirIntersectarAutomatas(automata2,false);

        }else{
            controlador.unirIntersectarAutomatas(automata2,true);
        }
        File file = new File("./src/temporal/temporal.txt");
        if(file.exists()){
            file.delete();
        }
    }

    @FXML
    private void nuevoAutomata(ActionEvent evento) throws IOException {
        File file = new File("./src/temporal/temporal.txt");
        if(file.exists()){
            file.delete();
        }
        transiciones(evento);
    }



    @FXML
    private void cargarSegundoAutomata(ActionEvent evento) throws IOException {
        HandlerFile handlerFile = new HandlerFile();
        handlerFile.guardarAutomata(automata,false);
        transiciones(evento);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources)  {
        File file = new File("./src/temporal/temporal.txt");
        file.deleteOnExit();
        automatas = new ArrayList<>();
        if(file.exists()){
            HandlerFile handlerFile = new HandlerFile();
            try {
                automata2 = handlerFile.crearAutomata("./src/temporal/temporal.txt");//automata anterior
            } catch (IOException e) {
                e.printStackTrace();
            }
            automatas.add(new tableViewAutomata(automata2,tableViewNuevoAutomata));
        }
    }

    public void initComponents(){
        alerta = new Alert(Alert.AlertType.WARNING);
        controlador = new Handler_Automata(automata);
        controlador.sortEstadoInicial();
        datos = FXCollections.observableArrayList();
        jdata = new LinkedList<>(); //Here is the data
        simbolosEntrada = 0;
        validarDeterministico();
        automatas.add(new tableViewAutomata(automata,tableView));
        tableView.setEditable(true);
    }

    private void validarDeterministico(){
        chkSiDeterministico.setSelected(false);
        chkNoDeterministico.setSelected(false);
        if (controlador.esDeterministico()) {
            chkSiDeterministico.setSelected(true);
            btnConvertirDeterministico.setDisable(true);
        } else {
            chkNoDeterministico.setSelected(true);
            btnConvertirDeterministico.setDisable(false);
        }
    }

    public void setAutomata(Automata automata){
        this.automata = automata;
    } // nuevo automata

    private void print(final Node node) {
        // Create a printer job for the default printer
        PrinterJob job = PrinterJob.createPrinterJob();
        if (job != null) {
            // Print the node
            boolean printed = job.printPage(node);
            if (printed) {
                // End the printer job
                job.endJob();
            }
        }
    }

    private void transiciones(ActionEvent event) throws IOException {
        Parent home_parent = FXMLLoader.load(getClass().getClassLoader().getResource("view/Principal.fxml"));
        Scene home_scene = new Scene(home_parent);
        Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        app_stage.hide();
        app_stage.setScene(home_scene);
        app_stage.show();
    }

}
