package edu.virginia.cs.review;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import edu.virginia.cs.review.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;


import static javafx.application.Application.launch;

public class ReviewApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Parent logInFxmlLoader = FXMLLoader.load(ReviewApplication.class.getResource("login.fxml"));
        stage.setTitle("Log in!");
        Scene logInScene = new Scene(logInFxmlLoader, 400, 400);
        stage.setScene(logInScene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
