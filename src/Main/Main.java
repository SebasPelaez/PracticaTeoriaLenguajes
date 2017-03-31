package Main;

import handler.HandlerFile;
import javafx.application.Application;
import javafx.stage.Stage;
import model.Automata;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        /*Parent root = FXMLLoader.load(getClass().getResource("Ejemplo.fxml"));
        primaryStage.setTitle("Peppin World");
        primaryStage.setScene(new Scene(root, 500, 500));
        primaryStage.show();*/



        //ConstruccionAutomata c = new ConstruccionAutomata();
        //c.desplegarVentana();

        HandlerFile hf = new HandlerFile(primaryStage);
        Automata a = hf.crearAutomata();
        a.reconocerSecuencia("");

    }


    public static void main(String[] args) {
        launch(args);
    }
}
