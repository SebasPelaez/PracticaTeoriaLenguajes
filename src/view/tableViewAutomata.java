package view;

import handler.Handler_ConstruirTransiciones;
import handler.tableobserver.tableObservable;
import handler.tableobserver.tableObserver;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import model.Automata;
import model.Estado;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Sebas y Juan on 20/04/2017.
 * Esta clase es la que se encarga de crear la tabla de visualización de autómatas.
 */
public class tableViewAutomata implements tableObservable {

    /**
     * Atributos de la clase
     */
    private TableView<String[]> tableView;
    private int simbolosEntrada;
    private List<String[]> jdata;
    private ObservableList<String[]> datos;
    private Automata automata;
    private Handler_ConstruirTransiciones controllerTransiciones;
    private tableObserver observador;

    /**
     * Constructor.
     * @param automata el automata que va a representar.
     * @param tableView La tabla inyectable, construida en el fxml.
     */
    public tableViewAutomata(Automata automata,TableView<String[]> tableView){
        this.automata = automata;
        this.tableView = tableView;
        initComponents();
    }

    /**
     * Constructor vacio.
     */
    public tableViewAutomata(){}

    /**
     * Método que se encarga de inicializar todos los componentes del automa y pintarlos en la tabla para posteriormente
     * quedar en pantalla.
     */
    public void initComponents(){
        controllerTransiciones = new Handler_ConstruirTransiciones(automata);
        datos = FXCollections.observableArrayList();
        jdata = new LinkedList<>(); //Here is the data
        simbolosEntrada = 0;
        inicializarColumnas();
        recargarTabla();
        tableView.setEditable(true);
    }

    /**
     * Este método lo que se encarga es de determinar según el autómata que recibe, cuantas columnas reserva en la
     * tabla para los simbolos de entrada, adicional 2 columnas que muestran si se de aceptación e inicial.
     */
    public void inicializarColumnas() {
        TableColumn<String[], String> columna = null;
        if(automata.getEstados().size()!=0) {//Valida que el automata tenga estados a los cuales dirigirse.
            for (int i = 0; i <= automata.getSimbolos().length + 2; i++) {//Recorre todos los simbolos de entrada y 2 columnas más para la aceptación y el estado inicial
                int j = i;
                if (i == 0) {
                    columna = new TableColumn<>("Estado/Símbolo");
                    columna.setPrefWidth(120);
                } else {
                    if (i == automata.getSimbolos().length + 1) {
                        columna = new TableColumn<>("Aceptación");
                        columna.setPrefWidth(80);
                    } else {
                        if (i == automata.getSimbolos().length + 2) {
                            columna = new TableColumn<>("Inicial");
                            columna.setPrefWidth(90);
                        } else {
                            columna = new TableColumn<>(automata.getSimbolos()[i - 1]);
                            columna.setCellFactory(TextFieldTableCell.forTableColumn());
                            columna.setPrefWidth(70);
                        }
                    }

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
                    String temporalEstado = row[j];
                    row[j] = event.getNewValue();
                    if(validarSiEstadosExisten()){
                        actualizarAutomata();
                        advise();
                    }else{
                       row[j]= temporalEstado;
                       actualizarAutomata();
                       advise();
                    }
                });
                tableView.getColumns().addAll(columna);
                simbolosEntrada++;
            }
        }
    }

    /**
     * Cuando alguien modifica la tabla este método valida que si exista ese valor que ingreso.
     * @return True si existe, False si no.
     */
    private boolean validarSiEstadosExisten() {
        return controllerTransiciones.validarTransicionesCorrectas(tableView.getItems(),4);
    }

    /**
     * Lo que se hace en este método es llenar lla información de la tabla con las transiciones del autómata.
     */
    private void agregarFilas() {
        for (int j = 0; j < automata.getEstados().size(); j++) {//recorre todos los estados.
            String[] estados = new String[simbolosEntrada + 2];
            Estado e = automata.getEstados().get(j);
            for (int i = 0; i < simbolosEntrada; i++) {
                estados[i] = "";
                if (i == 0) {
                    estados[i] = e.getNombre();
                } else {
                    if (i == simbolosEntrada - 2) {
                        String a = "" + e.isEsAceptacion();
                        estados[i] = a.toUpperCase();
                    } else {
                        if (i == simbolosEntrada - 1) {
                            String a = "" + e.isEsInicial();
                            estados[i] = a.toUpperCase();
                        } else {
                            for (int k = 0; k < e.getTransiciones().get(i - 1).getEstadosFinales().size(); k++) {
                                estados[i] += e.getTransiciones().get(i - 1).getEstadosFinales().get(k).getNombre() + ",";
                            }
                            estados[i] = estados[i].substring(0, estados[i].length() - 1);
                        }
                    }
                }
            }
            jdata.add(estados);
        }
        datos = FXCollections.observableList(jdata);
    }

    /**
     * Repinta la tabla de visualización del autómata.
     */
    public void recargarTabla() {
        datos.clear();
        tableView.getItems().clear();
        agregarFilas();
        tableView.getItems().addAll(datos);
    }

    /**
     * actualiza la tabla de visualización del autómata.
     */
    public void actualizarAutomata() {
        controllerTransiciones.vaciarTransiciones();
        controllerTransiciones.guardarAutomata(tableView.getItems(),4);
        recargarTabla();
    }

    public void setAutomata(Automata automata){
        this.automata=automata;
    }

    public Automata getAutomata(){
        return automata;
    }

    /**
     * conecta el observador con el observable.
     * @param observador La vista que se encarga de observar.
     */
    public void attach(tableObserver observador){
        this.observador=observador;
    }

    /**
     * Avisa que algo en la tabla se modifico.
     */
    @Override
    public void advise() {
        observador.update();
    }

    /**
     * Cuando se unen o intersectan 2 automatas, las tablas se deben reiniciar.
     * @param tableView
     */
    public void resetTableView(TableView<String[]> tableView){
        this.tableView = tableView;
        tableView.getColumns().remove(0,automata.getSimbolos().length+3);
    }
}
