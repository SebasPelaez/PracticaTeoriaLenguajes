package view;

import handler.HandlerFile;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.Estado;

/**
 * Created by Sebas on 28/03/2017.
 */
public class ConstruccionAutomata {

    private TextField simbolosAutomata;
    TableView<Estado> table = new TableView();

    private ObservableValue<Boolean> _activo;

    public void desplegarVentana(){
        initComponents();
    }

    public void initComponents(){
        Stage ventana =  new Stage();
        new HandlerFile(ventana);
        ventana.initModality(Modality.APPLICATION_MODAL);
        ventana.setTitle("Creación Autómata");
        ventana.setWidth(400);
        ventana.setHeight(500);

        VBox layout  = new VBox(10);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(new Group());
        scene.getStylesheets().add("view/style.css");

        simbolosAutomata = new TextField();
        simbolosAutomata.setPromptText("Ingrese los símbolos del autómata");

        final Button addButton = new Button("Add");
        addButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                Estado p1 = new Estado("Juan");
                table.getItems().addAll(p1);
            }
        });



        TableColumn<Estado, String> colNombre = new TableColumn<>("Nombre");
        colNombre.setCellFactory(TextFieldTableCell.forTableColumn());
        TableColumn<Estado, Boolean> colInicial = new TableColumn<>("Inicial");
        colInicial.setCellValueFactory((Callback<TableColumn.CellDataFeatures<Estado, Boolean>, ObservableValue<Boolean>>) _activo);
        colInicial.setCellFactory(CheckBoxTableCell.forTableColumn(colInicial));
        TableColumn<Estado, Boolean> colAceptacion = new TableColumn<>("Aceptacion");
        TableColumn<Estado, Boolean> colError = new TableColumn<>("Error");
        table.getColumns().addAll(colNombre, colInicial, colAceptacion,colError);

        colNombre.setCellValueFactory(new PropertyValueFactory<>("Nombre"));
        colInicial.setCellValueFactory(new PropertyValueFactory<>("Inicial"));
        colAceptacion.setCellValueFactory(new PropertyValueFactory<>("Aceptación"));
        colError.setCellValueFactory(new PropertyValueFactory<>("Error"));

        Estado p1 = new Estado("Juan");
        Estado p2 = new Estado("Juan");
        Estado p3 = new Estado("Juan");

        table.setEditable(true);
        table.getItems().addAll(p1, p2, p3);

        colNombre.setOnEditCommit(data -> {
            Estado p = data.getRowValue();
            p.setNombre(data.getNewValue());
        });

        colInicial.setCellValueFactory(cell -> {
            Estado p = cell.getValue();
            p.setEsInicial(p.isEsInicial());
            return new ReadOnlyBooleanWrapper(p.isEsInicial());
        });

        final VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 0, 0, 10));
        vbox.getChildren().addAll(simbolosAutomata,table,addButton);

        ((Group) scene.getRoot()).getChildren().addAll(vbox);
        ventana.setScene(scene);
        ventana.showAndWait();
    }

}
