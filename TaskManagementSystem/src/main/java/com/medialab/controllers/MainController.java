package com.medialab.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class MainController {

    @FXML
    private Button taskButton;
    @FXML
    private Button reminderButton;
    @FXML
    private Button categoryButton;
    @FXML
    private Button priorityButton;
    
    public void navigateToTaskScene() {
        switchScene("/com/medialab/views/TaskView.fxml");
    }

    public void navigateToReminderScene() {
        switchScene("/com/medialab/views/AllRemindersView.fxml");
    }

    public void navigateToCategoryScene() {
        switchScene("/com/medialab/views/CategoryView.fxml");
    }

    public void navigateToPriorityScene() {
        switchScene("/com/medialab/views/PriorityView.fxml");
    }

    public void navigateToHelpScene() {
        switchScene("/com/medialab/views/HelpView.fxml");
    }

    public void switchScene(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load(); // Load the FXML file
            // Pass the MainController to the new controller
            BaseController newController = loader.getController();
            newController.setMainController(this);
            // Pass TaskManager to the new controller
            // Object controller = loader.getController();
            // Get the current stage
            Stage stage = (Stage) taskButton.getScene().getWindow();
    
             // Save current stage size
            double currentWidth = stage.getWidth();
            double currentHeight = stage.getHeight();
            // Set the new scene
            Scene newScene = new Scene(root);
            stage.setScene(newScene);
            // Restore the saved size
            stage.setWidth(currentWidth);
            stage.setHeight(currentHeight);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}