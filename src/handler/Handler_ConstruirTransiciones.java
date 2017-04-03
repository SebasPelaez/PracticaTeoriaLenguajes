package handler;

import javafx.collections.ObservableList;

/**
 * Created by Sebas on 2/04/2017.
 */
public class Handler_ConstruirTransiciones {

    public void guardarAutomata(ObservableList data){
        System.out.println("----------------");
        for (int i=0;i<data.size();i++){
            System.out.println(data.get(i));
            System.out.println(data.get(i).toString().charAt(2));
        }
    }

}
