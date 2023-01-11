package sample;

import Model.Message;
import javafx.application.Application;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.ImageCursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;


import java.util.LinkedList;

public class Main extends Application {
    private static final SimpleIntegerProperty timeProperty = new SimpleIntegerProperty();

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Dashboard's PFD Simulator");
        primaryStage.setMaximized(true);
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.getIcons().add(new Image("./resources/globe.png"));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
        setUpContexts();
    }
    private static void setUpContexts() {

        LinkedList<Message> messages = new LinkedList<Message>();

    }
}
