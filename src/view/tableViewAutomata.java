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
 * Created by Sebas on 20/04/2017.
 */
public class tableViewAutomata implements tableObservable {

    private TableView<String[]> tableView;
    private int simbolosEntrada;
    private List<String[]> jdata;
    private ObservableList<String[]> datos;
    private Automata automata;
    private Handler_ConstruirTransiciones controllerTransiciones;
    private tableObserver observador;

    public tableViewAutomata(Automata automata,TableView<String[]> tableView){
        this.automata = automata;
        this.tableView = tableView;
        initComponents();
    }

    public tableViewAutomata(){}

    public void initComponents(){
        controllerTransiciones = new Handler_ConstruirTransiciones(automata);
        datos = FXCollections.observableArrayList();
        jdata = new LinkedList<>(); //Here is the data
        simbolosEntrada = 0;
        inicializarColumnas();
        recargarTabla();
        tableView.setEditable(true);
    }

    public void inicializarColumnas() {
        TableColumn<String[], String> columna = null;
        if(automata.getEstados().size()!=0) {
            for (int i = 0; i <= automata.getSimbolos().length + 2; i++) {
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
                    actualizarAutomata();
                    advise();
                });
                tableView.getColumns().addAll(columna);
                simbolosEntrada++;
            }
        }
    }

    private void agregarFilas() {
        for (int j = 0; j < automata.getEstados().size(); j++) {
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

    public void recargarTabla() {
        datos.clear();
        tableView.getItems().clear();
        agregarFilas();
        tableView.getItems().addAll(datos);
    }

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

    public void attach(tableObserver observador){
        this.observador=observador;
    }

    @Override
    public void advise() {
        observador.update();
    }
}
