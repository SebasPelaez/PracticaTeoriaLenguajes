package view;

import com.jfoenix.controls.JFXButton;
import handler.Handler_ConstruirTransiciones;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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
import java.util.ResourceBundle;

/**
 * Created by Sebas on 31/03/2017.
 */
public class ConstruccionTransiciones implements Initializable {

    @FXML private TableView tableView;
    @FXML private JFXButton btnGuardar;

    private int simbolosEntrada;
    private ObservableList<ObservableList> datos;
    private Handler_ConstruirTransiciones controller;

    @FXML private void guardarAutomata(ActionEvent evento){
        controller.guardarAutomata(tableView.getItems());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        controller = new Handler_ConstruirTransiciones();
        datos = FXCollections.observableArrayList();
        simbolosEntrada = 0;
        inicializarColumnas();
        agregarFilas();
        tableView.setItems(datos);
        tableView.setEditable(true);
    }

    private void agregarFilas() {
        for (int j=0;j<Automata.getInstance().getEstados().size();j++) {
            ObservableList<String> estados = FXCollections.observableArrayList();
            for (int i = 0; i < simbolosEntrada; i++) {
                if(i==0){
                    estados.add(Automata.getInstance().getEstados().get(j).getNombre());
                }else{
                    estados.add("Transición");
                }
            }
            datos.add(estados);
        }
    }

    public void inicializarColumnas(){
        TableColumn columna;
        for (int i=0;i<=Automata.getInstance().getSimbolos().length;i++){
            final int j = i;
            if(i==0){
                columna = new TableColumn("Estado/Símbolo");
                columna.setPrefWidth(120);
            }else{
                columna = new TableColumn(Automata.getInstance().getSimbolos()[i-1]);
                columna.setCellFactory(TextFieldTableCell.forTableColumn());
                columna.setPrefWidth(70);
            }
            columna.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList,String>,ObservableValue<String>>(){
                public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                    return new SimpleStringProperty(param.getValue().get(j).toString());
                }
            });

            tableView.getColumns().addAll(columna);
            simbolosEntrada++;
        }
    }
}
