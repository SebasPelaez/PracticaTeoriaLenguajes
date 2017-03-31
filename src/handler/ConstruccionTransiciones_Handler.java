package handler;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import model.Estado;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Sebas on 31/03/2017.
 */
public class ConstruccionTransiciones_Handler implements Initializable {

    @FXML private TableView<Estado> tableView;
    @FXML private JFXButton btnGuardar;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
