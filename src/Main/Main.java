package Main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;

public class Main extends Application {

    private static Stage primaryStage;

    private void setPrimaryStage(Stage stage) {
        Main.primaryStage = stage;
    }

    static public Stage getPrimaryStage() {
        return Main.primaryStage;
    }

    @Override
    public void start(Stage primaryStage) throws Exception{

        setPrimaryStage(primaryStage); // **Set the Stage**
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("view/Principal.fxml"));
        primaryStage.setTitle("Autómatas");
        primaryStage.setScene(new Scene(root, 400, 380));
        primaryStage.setOnCloseRequest(event -> {
            File file = new File("./src/temporal/temporal.txt");
            if(file.exists()){
                file.delete();
            }
        });
        primaryStage.show();


/*
        Handler_Automata automata = new Handler_Automata();
        HandlerFile hf = new HandlerFile(primaryStage);
        hf.crearAutomata();
        automata.imprimirAutomata();
        automata.convertirAutomataAFN();
        automata.imprimirAutomata();
        automata.simplificarAutomata();
        automata.imprimirAutomata();*/


    }





    public static void main(String[] args) {
        launch(args);
    }
}
