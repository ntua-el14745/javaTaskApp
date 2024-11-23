package com.medialab;

import com.medialab.controllers.MainController;
import com.medialab.models.TaskManager;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
// import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class MainApp extends Application {
    private TaskManager taskManager = new TaskManager(); //
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        taskManager.load(); // Load tasks from file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/medialab/views/MainLayout.fxml"));
        Parent root = loader.load();

        // Pass the TaskManager to the controller
        MainController controller = loader.getController();
        controller.setTaskManager(taskManager);

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setTitle("Task Management System");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void stop() {
        // Save tasks when the application is closing
        taskManager.save();
        // System.out.println("Application closed. Data saved.");
    }
    public static void main(String[] args) {
        launch(args);
    }
}
