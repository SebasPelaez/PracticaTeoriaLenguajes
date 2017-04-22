package view;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTextField;
import handler.HandlerFile;
import handler.Handler_Automata;
import handler.Handler_ConstruirTransiciones;
import handler.tableobserver.tableObserver;
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
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by Sebas on 3/04/2017.
 */
public class InteractividadAutomata implements Initializable,tableObserver {

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

    private ArrayList<Handler_Automata> controladores;
    private int simbolosEntrada;
    private List<String[]> jdata;
    private ObservableList<String[]> datos;
    private Handler_ConstruirTransiciones controllerTransiciones;
    private ArrayList<Automata> automatas;
    private ArrayList<tableViewAutomata> tableViewAutomatas;
    private Alert alerta;
    private int focusAutomata;

    @FXML
    private void focusAutomataDos(MouseEvent evento) {
        focusAutomata=1;
        validarDeterministico();
        System.out.println(focusAutomata);
    }

    @FXML
    private void focusAutomataUno(MouseEvent evento) {
        focusAutomata=0;
        validarDeterministico();
        System.out.println(focusAutomata);
    }


    @FXML
    private void convertirDeterministico(ActionEvent evento) {
        controladores.get(focusAutomata).convertirAutomataAFN(false);
        controladores.get(focusAutomata).imprimirAutomata();
        tableViewAutomatas.get(focusAutomata).setAutomata(automatas.get(focusAutomata));
        tableViewAutomatas.get(focusAutomata).recargarTabla();
        validarDeterministico();
    }

    @FXML
    private void guardarEnDisco(ActionEvent evento) {
        HandlerFile handlerFile = new HandlerFile((Stage) ((Node) evento.getSource()).getScene().getWindow());
        handlerFile.guardarAutomata(automatas.get(focusAutomata),true);
    }

    @FXML
    private void simplificar(ActionEvent evento) {
        controladores.get(focusAutomata).imprimirAutomata();
        controladores.get(focusAutomata).simplificarAutomata();
        tableViewAutomatas.get(focusAutomata).setAutomata(automatas.get(focusAutomata));
        tableViewAutomatas.get(focusAutomata).recargarTabla();
        validarDeterministico();
    }

    @FXML
    private void probarAutomata(ActionEvent evento) {
        alerta.setTitle("Alerta");
        alerta.getDialogPane().getChildren().stream().filter(node -> node instanceof Label).forEach(node -> ((Label) node).setMinHeight(Region.USE_PREF_SIZE));
        if (!txtHileraEvaluar.getText().equals("")) {
            if (controladores.get(focusAutomata).reconocerSecuencia(txtHileraEvaluar.getText())) {
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
            controladores.get(focusAutomata).unirIntersectarAutomatas(automatas.get(1),false);
        }else{
            controladores.get(focusAutomata).unirIntersectarAutomatas(automatas.get(1),true);
        }
    }

    @FXML
    private void nuevoAutomata(ActionEvent evento) throws IOException {
        transiciones(evento);
    }

    @FXML
    private void cargarSegundoAutomata(ActionEvent evento) throws IOException {
        HandlerFile handlerFile = new HandlerFile();
        handlerFile.guardarAutomata(automatas.get(focusAutomata),false);
        transiciones(evento);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources)  {
        File file = new File("./src/temporal/temporal.txt");
        tableViewAutomatas = new ArrayList<>();
        automatas = new ArrayList<>();
        controladores = new ArrayList<>();

        automatas.add(new Automata());//Creo el espacio para el automata uno
        automatas.add(new Automata());//Creo el espacio para el automata Dos
        controladores.add(new Handler_Automata(automatas.get(0)));//Creo el controlador para el primer automata
        controladores.add(new Handler_Automata(automatas.get(1)));//Creo el controlador para el segundo automata
        tableViewAutomatas.add(new tableViewAutomata());//Creo el controlador para la primer tabla de automatas
        tableViewAutomatas.add(new tableViewAutomata());//Creo el controlador para la segunda tabla de automatas

        if(file.exists()){
            HandlerFile handlerFile = new HandlerFile();
            try {
                automatas.set(1,handlerFile.crearAutomata("./src/temporal/temporal.txt")); //automata anterior
                controladores.set(1,new Handler_Automata(automatas.get(1)));
            } catch (IOException e) {
                e.printStackTrace();
            }
            tableViewAutomatas.set(1,new tableViewAutomata(automatas.get(1),tableViewNuevoAutomata));
            tableViewAutomatas.get(1).attach(this);
        }
    }

    public void initComponents(){
        alerta = new Alert(Alert.AlertType.WARNING);
        focusAutomata = 0;
        controladores.set(0,new Handler_Automata(automatas.get(0)));
        controladores.get(0).sortEstadoInicial();
        validarDeterministico();
        tableViewAutomatas.set(0,new tableViewAutomata(automatas.get(0),tableView));
        tableViewAutomatas.get(0).attach(this);
        tableView.setEditable(true);
    }

    private void validarDeterministico(){
        chkSiDeterministico.setSelected(false);
        chkNoDeterministico.setSelected(false);
        if (controladores.get(focusAutomata).esDeterministico()) {
            chkSiDeterministico.setSelected(true);
            btnConvertirDeterministico.setDisable(true);
        } else {
            chkNoDeterministico.setSelected(true);
            btnConvertirDeterministico.setDisable(false);
        }
    }

    public void setAutomata(Automata automata){
        automatas.set(0,automata);
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

    @Override
    public void update() {
        automatas.set(focusAutomata,tableViewAutomatas.get(focusAutomata).getAutomata());
        controladores.get(focusAutomata).setAutomata(automatas.get(focusAutomata));
        validarDeterministico();
    }
}
