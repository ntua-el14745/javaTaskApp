package com.medialab;

import com.medialab.models.TaskManager;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class MainApp extends Application {
    private TaskManager taskManager;
    // private MainController mainController;
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/medialab/views/MainLayout.fxml"));
        Parent root = loader.load();

        // Initialize singleton
        taskManager = TaskManager.getInstance();
        taskManager.load(); // Load tasks from file
        // Check for delayed tasks and update their status
        taskManager.checkDelayedTasks();

        // Show a pop-up window with delayed tasks
        Scene scene = new Scene(root, 800, 600);
        primaryStage.setTitle("MediaLab Assistant");
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setMinWidth(1200);
        primaryStage.setMinHeight(800);

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
