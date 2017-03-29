package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import view.ConstruccionAutomata;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        /*
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Peppin World");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
        */
        ConstruccionAutomata c = new ConstruccionAutomata();
        c.desplegarVentana();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
