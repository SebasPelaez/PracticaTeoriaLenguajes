package view;

import com.jfoenix.controls.JFXButton;
import handler.Handler_ConstruirTransiciones;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;
import model.Automata;
import model.Estado;

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

    @FXML private void guardarAutomata(ActionEvent evento){
        controller.guardarAutomata(tableView.getItems());
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
}
