package view;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTextField;
import handler.HandlerFile;
import handler.Handler_Automata;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import model.Automata;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Sebas on 3/04/2017.
 */
public class InteractividadAutomata implements Initializable {

    @FXML private TableView<String[]> tableView;
    @FXML private JFXCheckBox chkSiDeterministico;
    @FXML private JFXCheckBox chkNoDeterministico;
    @FXML private JFXTextField txtHileraEvaluar;
    @FXML private JFXButton btnConvertirDeterministico;

    private Handler_Automata controlador;

    @FXML private void convertirDeterministico(ActionEvent evento){
        controlador.convertirAutomataAFN();
        controlador.simplificarAutomata();
    }

    @FXML private void guardarEnDisco(ActionEvent evento){
        HandlerFile handlerFile = new HandlerFile((Stage) ((Node) evento.getSource()).getScene().getWindow());
        handlerFile.guardarAutomata();
    }

    @FXML private void simplificar(ActionEvent evento){
        controlador.imprimirAutomata();
        controlador.simplificarAutomata();
    }

    @FXML private void probarAutomata(ActionEvent evento){
        if (!txtHileraEvaluar.getText().equals("")){
            controlador.reconocerSecuencia(txtHileraEvaluar.getText());
        }else{
            System.out.println("Debes ingresar una cadena para probar el automata");
        }
    }

    @FXML private void generarPdf(ActionEvent evento){
        Node source = (Node) evento.getSource();
        print(source.getParent());
    }

    @FXML private void nuevoAutomata(ActionEvent evento) throws IOException {
        Automata.getInstance().reinicializarAutomata();
        transiciones(evento);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        controlador = new Handler_Automata();
        validarDeterministico();
    }

    private void validarDeterministico() {
        //controlador.esDeterministico() VA EN EL IF
        if(true){
            chkSiDeterministico.setSelected(true);
        }else{
            chkNoDeterministico.setSelected(false);
            btnConvertirDeterministico.setDisable(true);
        }

    }

    private void print(Node node){
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
