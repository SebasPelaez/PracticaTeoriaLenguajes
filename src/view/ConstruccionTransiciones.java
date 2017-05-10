package view;

import com.jfoenix.controls.JFXButton;
import handler.Handler_ConstruirTransiciones;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import model.Automata;

import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by Sebas on 31/03/2017.
 */
public class ConstruccionTransiciones implements Initializable {

    /**
     * Inyección de componentes.
     */
    @FXML private TableView<String[]> tableView;
    @FXML private JFXButton btnGuardar;

    /**
     * Atributos de clase.
     */
    private int simbolosEntrada;
    private ObservableList<String[]> datos;
    private Handler_ConstruirTransiciones controller;
    private List<String[]> jdata;
    private Alert alerta = new Alert(Alert.AlertType.WARNING);
    private Automata automata;

    /**
     * Cuando se termina de ingresar todas las transiciones, este método guarda todos esto en el autómata.
     * @param evento La acción del botón.
     * @throws IOException
     */
    @FXML private void guardarAutomata(ActionEvent evento) throws IOException {
        alerta.setTitle("Alerta");
        alerta.getDialogPane().getChildren().stream().filter(node -> node instanceof Label).forEach(node -> ((Label)node).setMinHeight(Region.USE_PREF_SIZE));
        if(controller.validarTransicionesCorrectas(tableView.getItems(),0)){//verifica que las transiciones si sean correctas.
            if(controller.validarTransicionesError(tableView.getItems())){
                controller.guardarAutomata(tableView.getItems(),0);
                transiciones(evento,"Transiciones");
            }else{
                alerta.setContentText("Los estados de error solo tienen transiciones hacia si mismos");
                alerta.showAndWait();
            }
        }else{
            alerta.setContentText("LAS TRANSICIONES DEBEN SER A ESTADOS");
            alerta.showAndWait();
        }

    }

    /**
     * Elimina todas las instancias de autómatas previos y vuelve a empezar.
     * @param evento
     * @throws IOException
     */
    @FXML
    private void regresar(ActionEvent evento) throws IOException {
        automata = new Automata();
        transiciones(evento,"Principal");
    }

    /**
     * Como esta clase es un controlador, se debe sobreescribir el método de inicializar. que es el encargado de cargar toda la vista.
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    /**
     * Inicializa todos los componentes de la vista, permite que que se visualicen correctamente.
     */
    public void initComponents(){
        controller = new Handler_ConstruirTransiciones(automata);
        datos = FXCollections.observableArrayList();
        jdata = new LinkedList<>(); //Here is the data
        simbolosEntrada = 0;
        inicializarColumnas();
        agregarFilas();
        tableView.getItems().addAll(datos);
        tableView.setEditable(true);
    }

    public void setAutomata(Automata automata){
        this.automata = automata;
    }

    /**
     * Lo que se hace en este método es llenar lla información de la tabla con las transiciones del autómata.
     */
    private void agregarFilas() {
        for (int j=0;j<automata.getEstados().size();j++) {
            String[] estados = new String[simbolosEntrada];
            for (int i = 0; i < simbolosEntrada; i++) {
                if(i==0){
                    estados[i]=automata.getEstados().get(j).getNombre();
                }else{
                    if(automata.getEstados().get(j).is_error()){
                        estados[i]=automata.getEstados().get(j).getNombre();
                    }else{
                        estados[i]="Transición";
                    }
                }
            }
            jdata.add(estados);
        }
        datos = FXCollections.observableList(jdata);
    }

    /**
     * Este método lo que se encarga es de determinar según el autómata que recibe, cuantas columnas reserva en la
     * tabla para los simbolos de entrada, adicional 2 columnas que muestran si se de aceptación e inicial.
     */
    public void inicializarColumnas(){
        for (int i=0;i<=automata.getSimbolos().length;i++){
            TableColumn<String[], String> columna;
            int j = i;
            if(i==0){
                columna  = new TableColumn<>("Estado/Símbolo");
                columna.setPrefWidth(120);
            }else{
                columna = new TableColumn<>(automata.getSimbolos()[i-1]);
                columna.setCellFactory(TextFieldTableCell.forTableColumn());
                columna.setPrefWidth(70);
            }
            /**
             * Esto se encarga de mantener la tabla actualizada.
             */
            columna.setCellValueFactory(cellData -> {
                String[] rowData = cellData.getValue();
                if (j >= rowData.length) {
                    return new ReadOnlyStringWrapper("");
                } else {
                    String cellValue = rowData[j];
                    return new ReadOnlyStringWrapper(cellValue);
                }
            });
            /**
             * Esto se encarga de persistir los cambios que se hacen en la tabla al autómata, además es muy importante
             * decir que avisa a la vista principal que algo se modifico.
             */
            columna.setOnEditCommit(event -> {
                String[] row = event.getRowValue();
                row[j] = event.getNewValue();
            });
            tableView.getColumns().addAll(columna);
            simbolosEntrada++;
        }
    }

    /**
     * Este método nos es util cuando vamos acambiar de pantallas, por ejemplo a la inicial.
     * @param event El evento del botón.
     * @throws IOException Por si sale un error con el archivo.
     */
    private void transiciones(ActionEvent event,String ventana) throws IOException {
        Parent home_parent = null;
        switch (ventana){
            case "Principal":
                home_parent = FXMLLoader.load(getClass().getClassLoader().getResource("view/Principal.fxml"));
                break;
            case "Transiciones":
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("view/Interactividad.fxml"));
                home_parent = (Parent)fxmlLoader.load();
                InteractividadAutomata controller =  fxmlLoader.getController();
                controller.setAutomata(automata);
                controller.initComponents();
                break;
        }
        Scene home_scene = new Scene(home_parent);
        Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        app_stage.hide();
        app_stage.setScene(home_scene);
        app_stage.show();
    }


}
