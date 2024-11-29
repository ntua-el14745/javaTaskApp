package com.medialab;

import com.medialab.models.TaskManager;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class MainApp extends Application {
    private TaskManager taskManager;

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Initialize singleton
        taskManager = TaskManager.getInstance();
        // Load tasks from file
        taskManager.load(); 
        // Check for delayed tasks and update their status
        taskManager.checkDelayedTasks();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/medialab/views/MainLayout.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root, 800, 600);
        primaryStage.setTitle("MediaLab Assistant");
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setMinWidth(1200);
        primaryStage.setMinHeight(800);
        // Show a pop-up window with delayed tasks
        taskManager.showDelayedTasksPopup();
        taskManager.initializeReminderChecker();
    }

    @Override
    public void stop() {
        // Save tasks when the application is closing
        taskManager.save();
        // Shut down the reminder checker
        TaskManager.getInstance().shutdownReminderChecker();
    }
    public static void main(String[] args) {
        launch(args);
    }

    

}
