package view;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import handler.Handler_ConstruirEstados;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import model.Automata;
import model.Estado;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Created by Sebas on 29/03/2017.
 */
public class ConstruccionEstados implements Initializable {

    @FXML
    private Pane layout;
    @FXML
    private TableView<Estado> tableView;
    @FXML
    private JFXTextField txtSimbolos;
    @FXML
    private JFXButton btnAnadir;
    @FXML
    private JFXButton btnTransiciones;

    private String simboloAnt = "";
    private Automata automata;

    private Handler_ConstruirEstados controller;

    private Alert alerta = new Alert(Alert.AlertType.WARNING);

    @FXML
    private void agregarEstado(ActionEvent evento) {
        Estado p1 = new Estado("Ingrese el estado aquí", false, false, false);
        tableView.getItems().addAll(p1);
    }

    @FXML
    private void borrarEstado(ActionEvent evento) {
        alerta.setTitle("Alerta");
        alerta.getDialogPane().getChildren().stream().filter(node -> node instanceof Label).forEach(node -> ((Label)node).setMinHeight(Region.USE_PREF_SIZE));
        if(tableView.getItems().size()!=0){
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Mensaje de borrado");
            dialog.setHeaderText("Estas a punto de borrar un estado");
            dialog.setContentText("¿Cuál estado quieres eliminar?");
            Optional<String> result = dialog.showAndWait();
            String a="";
            if (result.isPresent()){
                a=result.get();
            }
            if(!a.equals("")){
                if(controller.retornatFilar(tableView.getItems(),a)!=-1){
                    tableView.getItems().remove(controller.retornatFilar(tableView.getItems(),a));
                }else{
                    alerta.setContentText("Ese estado no existe");
                    alerta.showAndWait();
                }
            }else{
                alerta.setContentText("No copiaste ningún estado");
                alerta.showAndWait();
            }
        }else{
            alerta.setContentText("No hay estados que borrar");
            alerta.showAndWait();
        }

    }


    @FXML public void validarCaracter(KeyEvent e){
        if(txtSimbolos.getText().length()>0){
            String a = txtSimbolos.getText().substring(txtSimbolos.getText().length()-1);
            if(e.getCharacter().equals(",") && a.equals(",") || e.getCharacter().equals(" ") || e.getCharacter().matches("[¬°!#$\\[%+\\]&/()=?¡¨*;:.¿'|}{^~`´_-]")){
                e.consume();
            }
        }else{
            if(e.getCharacter().equals(",") || e.getCharacter().matches("[¬°!#$%+&/(\\[)=\\]?¡¨*;:.¿'|}{^~`´_-]")){

                e.consume();
            }
        }

    }

    @FXML
    private void construirTransiciones(ActionEvent evento) throws IOException {

        ArrayList<String> s = controller.validarAutomata(tableView.getItems(),txtSimbolos.getText());
        if (s.isEmpty()){
            controller.agregarEstados(tableView.getItems());
            controller.agregarSimbolos(txtSimbolos.getText());
            controller.imprimirSimbolos();
            transiciones(evento);
        }else{
            alerta.setTitle("Alerta");
            alerta.getDialogPane().getChildren().stream().filter(node -> node instanceof Label).forEach(node -> ((Label)node).setMinHeight(Region.USE_PREF_SIZE));
            String cadenaAlerta="";
            for (int i = 0; i < s.size(); i++) {
                cadenaAlerta += s.get(i)+"\n";
            }
            alerta.setContentText(cadenaAlerta);
            alerta.showAndWait();
        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        automata= new Automata();
        controller = new Handler_ConstruirEstados(automata);
        TableColumn<Estado, String> colNombre = new TableColumn<>("Nombre");
        TableColumn<Estado, Boolean> colInicial = new TableColumn<>("Inicial");
        TableColumn<Estado, Boolean> colAceptacion = new TableColumn<>("Aceptacion");
        TableColumn<Estado, Boolean> colError = new TableColumn<>("Error");

        colNombre.setCellFactory(TextFieldTableCell.forTableColumn());
        colNombre.setCellValueFactory(new PropertyValueFactory<>("Nombre"));
        colNombre.setPrefWidth(210);

        colInicial.setCellValueFactory(cell -> cell.getValue().inicialProperty());
        colInicial.setCellFactory(CheckBoxTableCell.forTableColumn(colInicial));
        colInicial.setPrefWidth(90);

        colAceptacion.setCellValueFactory(cell -> cell.getValue().aceptacionProperty());
        colAceptacion.setCellFactory(CheckBoxTableCell.forTableColumn(colAceptacion));
        colAceptacion.setPrefWidth(90);

        colError.setCellValueFactory(cell -> cell.getValue().errorProperty());
        colError.setCellFactory(CheckBoxTableCell.forTableColumn(colError));
        colError.setPrefWidth(90);

        tableView.getColumns().addAll(colNombre, colAceptacion, colInicial, colError);
        tableView.setEditable(true);

        colNombre.setOnEditCommit(data -> {
            Estado p = data.getRowValue();
            p.setNombre(data.getNewValue());
        });
    }

    private void transiciones(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("view/ConstruccionTransiciones_View.fxml"));
        Parent home_parent = (Parent)fxmlLoader.load();
        ConstruccionTransiciones controller =  fxmlLoader.getController();
        controller.setAutomata(automata);
        controller.initComponents();
        Scene home_scene = new Scene(home_parent);
        Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        app_stage.hide();
        app_stage.setScene(home_scene);
        app_stage.show();
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

}
