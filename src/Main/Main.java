package Main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("view/ConstruccionEstados_View.fxml"));
        primaryStage.setTitle("Peppin World");
        primaryStage.setScene(new Scene(root, 500, 500));
        primaryStage.show();



        //ConstruccionAutomata c = new ConstruccionAutomata();
        //c.desplegarVentana();


    }


    public static void main(String[] args) {
        launch(args);
    }
}
