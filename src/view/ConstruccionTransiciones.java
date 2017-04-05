package view;

import com.jfoenix.controls.JFXButton;
import handler.HandlerFile;
import handler.Handler_ConstruirTransiciones;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.Automata;
import model.Estado;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by Sebas on 31/03/2017.
 */
public class ConstruccionTransiciones implements Initializable {

    @FXML private TableView<String[]> tableView;
    @FXML private JFXButton btnGuardar;

    private int simbolosEntrada;
    private ObservableList<String[]> datos;
    private Handler_ConstruirTransiciones controller;
    private List<String[]> jdata;

    @FXML private void guardarAutomata(ActionEvent evento) throws IOException {
        if(controller.validarTransicionesCorrectas(tableView.getItems())){
            controller.guardarAutomata(tableView.getItems());
            transiciones(evento);
        }else{
            System.out.println("LAS TRANSICIONES DEBEN SER A ESTADOS");
        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        controller = new Handler_ConstruirTransiciones();
        datos = FXCollections.observableArrayList();
        jdata = new LinkedList<>(); //Here is the data
        simbolosEntrada = 0;
        inicializarColumnas();
        agregarFilas();
        tableView.getItems().addAll(datos);
        tableView.setEditable(true);
    }

    private void agregarFilas() {
        for (int j=0;j<Automata.getInstance().getEstados().size();j++) {
            String[] estados = new String[simbolosEntrada];
            for (int i = 0; i < simbolosEntrada; i++) {
                if(i==0){
                    estados[i]=Automata.getInstance().getEstados().get(j).getNombre();
                }else{
                    estados[i]="Transición";
                }
            }
            jdata.add(estados);
        }
        datos = FXCollections.observableList(jdata);
    }

    public void inicializarColumnas(){
        for (int i=0;i<=Automata.getInstance().getSimbolos().length;i++){
            TableColumn<String[], String> columna;
            int j = i;
            if(i==0){
                columna  = new TableColumn<>("Estado/Símbolo");
                columna.setPrefWidth(120);
            }else{
                columna = new TableColumn<>(Automata.getInstance().getSimbolos()[i-1]);
                columna.setCellFactory(TextFieldTableCell.forTableColumn());
                columna.setPrefWidth(70);
            }
            columna.setCellValueFactory(cellData -> {
                String[] rowData = cellData.getValue();
                if (j >= rowData.length) {
                    return new ReadOnlyStringWrapper("");
                } else {
                    String cellValue = rowData[j];
                    return new ReadOnlyStringWrapper(cellValue);
                }
            });
            columna.setOnEditCommit(event -> {
                String[] row = event.getRowValue();
                row[j] = event.getNewValue();
            });
            tableView.getColumns().addAll(columna);
            simbolosEntrada++;
        }
    }

    private void transiciones(ActionEvent event) throws IOException {
        Parent home_parent = FXMLLoader.load(getClass().getClassLoader().getResource("view/Interactividad.fxml"));
        Scene home_scene = new Scene(home_parent);
        Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        app_stage.hide();
        app_stage.setScene(home_scene);
        app_stage.show();
    }

}
