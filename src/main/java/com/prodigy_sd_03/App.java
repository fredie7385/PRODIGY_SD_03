package com.prodigy_sd_03;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;
    private UserController uController = new UserController();

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("user"));
        stage.setTitle("User Registration");
        uController.setStage(stage);
        stage.setScene(scene);
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
//        DbConnection db = new DbConnection();
//        db.getDBConn();
//        System.out.println("COnnected!"+ db.getCon());
    }
}
