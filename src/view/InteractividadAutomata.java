package view;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTextField;
import handler.HandlerFile;
import handler.Handler_Automata;
import javafx.beans.property.ReadOnlyStringWrapper;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import model.Automata;
import model.Estado;

import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
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
    @FXML private JFXButton btnGenerarPdf;
    private Handler_Automata controlador;
    private int simbolosEntrada;
    private List<String[]> jdata;
    private ObservableList<String[]> datos;

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
        Parent a = source.getParent();
        print(a);
    }

    @FXML private void nuevoAutomata(ActionEvent evento) throws IOException {
        Automata.getInstance().reinicializarAutomata();
        transiciones(evento);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        controlador = new Handler_Automata();
        datos = FXCollections.observableArrayList();
        jdata = new LinkedList<>(); //Here is the data
        simbolosEntrada = 0;
        validarDeterministico();
        inicializarColumnas();
        agregarFilas();
        tableView.getItems().addAll(datos);
        tableView.setEditable(true);
    }

    private void validarDeterministico() {
        if(controlador.esDeterministico()){
            chkSiDeterministico.setSelected(true);
        }else{
            chkNoDeterministico.setSelected(true);
            btnConvertirDeterministico.setDisable(false);
        }

    }

    private void print(final Node node){
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

    private void agregarFilas() {
        for (int j=0;j<Automata.getInstance().getEstados().size();j++) {
            String[] estados = new String[simbolosEntrada];
            for (int i = 0; i < simbolosEntrada; i++) {
                Estado e = Automata.getInstance().getEstados().get(j);
                estados[i]="";
                if(i==0){
                    estados[i]=e.getNombre();
                }else{
                    for (int k=0;k<e.getTransiciones().get(i-1).getEstadosFinales().size();k++){
                        estados[i]+=e.getTransiciones().get(i-1).getEstadosFinales().get(k).getNombre()+",";
                    }
                    estados[i] = estados[i].substring(0,estados[i].length()-1);
                }
            }
            jdata.add(estados);
        }
        datos = FXCollections.observableList(jdata);
    }
}
