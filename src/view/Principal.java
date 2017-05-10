package view;

import com.jfoenix.controls.JFXButton;
import handler.HandlerFile;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Automata;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Ventana inicial de la aplicación con las opciones de cargar o crear un nuevo autómata.
 */
public class Principal implements Initializable {
    /**
     * Definción de los componentes visuales de la ventana.
     */
    @FXML private JFXButton btnCrearAutomata;
    @FXML private JFXButton btnCargarAutomata;

    @FXML private void crearAutomata(ActionEvent evento) throws IOException {
        transiciones(evento,0);
    }

    @FXML private void cargarAutomata(ActionEvent evento) throws IOException {
        transiciones(evento,1);
    }

    /**
     * Inicialización de la ventana
     * @param location
     * @param resources
     */

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    /**
     * Carga la siguiente ventana dependiendo del botón clickedo
     * @param event
     * @param ventana Entero que indica que ventana se deberá desplegar;
     * @throws IOException
     */
    private void transiciones(ActionEvent event,int ventana) throws IOException {
        Parent home_parent=null;
        Stage escena = (Stage) ((Node) event.getSource()).getScene().getWindow();
        switch (ventana){
            case 0:
                home_parent = FXMLLoader.load(getClass().getClassLoader().getResource("view/ConstruccionEstados_View.fxml"));
                break;
            case 1:
                HandlerFile hf = new HandlerFile(escena);
                Automata automata =  hf.crearAutomata("");
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("view/Interactividad.fxml"));
                home_parent = (Parent)fxmlLoader.load();
                InteractividadAutomata controller =  fxmlLoader.getController();
                controller.setAutomata(automata);
                controller.initComponents();
                break;
        }
        Scene home_scene = new Scene(home_parent);
        Stage app_stage = escena;
        app_stage.hide();
        app_stage.setScene(home_scene);
        app_stage.show();
    }
}
