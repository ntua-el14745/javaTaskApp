package com.medialab;

import java.util.List;
import java.util.stream.Collectors;

import com.medialab.models.Task;
// import com.medialab.controllers.MainController;
import com.medialab.models.TaskManager;
import com.medialab.models.TaskStatus;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
// import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
// import javafx.scene.control.Alert;
// import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class MainApp extends Application {
    private TaskManager taskManager;
    // private MainController mainController;
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/medialab/views/MainLayout.fxml"));
        Parent root = loader.load();

        // Pass the TaskManager to the controller
        // Initialize singletons
        // mainController = new MainController();
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
        showDelayedTasksPopup();

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


    public void showDelayedTasksPopup() {
    // Create a ListView to display the delayed tasks
    ListView<String> listView = new ListView<>();
    
    // Get the delayed tasks
    List<Task> delayedTasks = taskManager.getAllTasks().stream()
                                   .filter(task -> task.getStatus() == TaskStatus.DELAYED)
                                   .collect(Collectors.toList());

    // If there are delayed tasks, populate the ListView
    if (!delayedTasks.isEmpty()) {
        for (Task task : delayedTasks) {
            listView.getItems().add(task.getTitle() + " - " + task.getDeadline().toString());
        }
    } else {
        listView.getItems().add("No delayed tasks.");
    }

    // Create a new window (Stage)
    Stage delayedTasksStage = new Stage();
    delayedTasksStage.setTitle("Delayed Tasks");

    // Create a layout (VBox) for better organization and styling
    VBox vbox = new VBox(15, 
        new Label("Delayed Tasks"),  // Title label with styling
        listView
    );
    vbox.setPadding(new Insets(20));  // Add padding around the VBox
    vbox.setStyle("-fx-background-color: #f4f4f4; -fx-border-color: #0078D4; -fx-border-width: 2px; -fx-border-radius: 10px;");  // Background color and border style
    vbox.setSpacing(15);  // Space between the label and list
    // Create a HBox to center the Close button
    HBox buttonBox = new HBox();
    buttonBox.setAlignment(javafx.geometry.Pos.CENTER);  // Center the button
    Button closeButton = new Button("Close");
    closeButton.setStyle("-fx-background-color: #FF5722; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10px 20px;");
    closeButton.setOnAction(event -> delayedTasksStage.close());
    buttonBox.getChildren().add(closeButton);  // Add the close button to the HBox

    // Add the HBox (with the centered close button) to the VBox
    vbox.getChildren().add(buttonBox);

    // Create a scene for the new window with the updated layout
    Scene scene = new Scene(vbox, 400, 300);  // Adjust size as needed
    delayedTasksStage.setScene(scene);

    // Show the stage (new window)
    delayedTasksStage.show();
}

}
