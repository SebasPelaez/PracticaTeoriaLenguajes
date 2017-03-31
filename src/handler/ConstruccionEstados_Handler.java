package handler;

import Main.Main;
import com.jfoenix.controls.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import model.Estado;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Sebas on 29/03/2017.
 */
public class ConstruccionEstados_Handler implements Initializable {

    @FXML private Pane layout;
    @FXML private TableView<Estado> tableView;
    @FXML private JFXTextField txtSimbolos;
    @FXML private JFXButton btnAnadir;
    @FXML private JFXButton btnTransiciones;

    @FXML private void agregarEstado(ActionEvent evento){
        Estado p1 = new Estado("Ingrese el estado aquí",false,false,false);
        tableView.getItems().addAll(p1);
    }

    @FXML private void construirTransiciones(ActionEvent evento) throws IOException {
       //paso a la siguiente ventana
        transiciones(evento);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        TableColumn<Estado, String> colNombre = new TableColumn<>("Nombre");
        TableColumn<Estado, Boolean> colInicial = new TableColumn<>("Inicial");
        TableColumn<Estado, Boolean> colAceptacion = new TableColumn<>("Aceptacion");
        TableColumn<Estado, Boolean> colError = new TableColumn<>("Error");

        colNombre.setCellFactory(TextFieldTableCell.forTableColumn());
        colNombre.setCellValueFactory(new PropertyValueFactory<>("Nombre"));
        colNombre.setPrefWidth(210);

        colInicial.setCellFactory(CheckBoxTableCell.forTableColumn(colInicial));
        colInicial.setCellValueFactory(new PropertyValueFactory<>("Inicial"));
        colInicial.setCellValueFactory(cell -> cell.getValue().inicialProperty());
        colInicial.setPrefWidth(90);

        colAceptacion.setCellFactory(CheckBoxTableCell.forTableColumn(colAceptacion));
        colAceptacion.setCellValueFactory(new PropertyValueFactory<>("Aceptación"));
        colAceptacion.setCellValueFactory(cell -> cell.getValue().aceptacionProperty());
        colAceptacion.setPrefWidth(90);

        colError.setCellFactory(CheckBoxTableCell.forTableColumn(colError));
        colError.setCellValueFactory(new PropertyValueFactory<>("Error"));
        colError.setCellValueFactory(cell -> cell.getValue().errorProperty());
        colError.setPrefWidth(90);

        tableView.getColumns().addAll(colNombre, colInicial, colAceptacion,colError);
        tableView.setEditable(true);

        colNombre.setOnEditCommit(data -> {
            Estado p = data.getRowValue();
            p.setNombre(data.getNewValue());
        });
    }

    private void transiciones(ActionEvent event) throws IOException {
        Parent home_parent =  FXMLLoader.load(getClass().getClassLoader().getResource("view/ConstruccionTransiciones_View.fxml"));
        Scene home_scene = new Scene(home_parent);
        Stage app_stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        app_stage.hide();
        app_stage.setScene(home_scene);
        app_stage.show();
    }
}
