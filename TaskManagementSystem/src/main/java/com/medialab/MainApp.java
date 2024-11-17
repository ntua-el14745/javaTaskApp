package com.medialab;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    // public void start(Stage primaryStage) {
    //     try {
    //         BorderPane root = new BorderPane();
    //         Scene scene = new Scene(root, 800, 600);
    //         primaryStage.setTitle("Task Management System");
    //         primaryStage.setScene(scene);
    //         primaryStage.show();
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //     }
    public void start(Stage primaryStage) throws Exception {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/medialab/views/MainLayout.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, 800, 600);
    
            primaryStage.setTitle("Task Management System");
            primaryStage.setScene(scene);
            primaryStage.show();
        }
    

    public static void main(String[] args) {
        launch(args);
    }
}
