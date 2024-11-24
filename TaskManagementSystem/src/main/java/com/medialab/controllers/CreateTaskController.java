package com.medialab.controllers;

import java.time.LocalDate;

import com.medialab.models.Category;
import com.medialab.models.Priority;
import com.medialab.models.Task;
import com.medialab.models.TaskManager;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class CreateTaskController {
    @FXML
    private TextField titleField;
    @FXML
    private TextArea descriptionField;
    // @FXML
    // private TextField categoryField;
    // @FXML
    // private TextField priorityField;
    // @FXML
    // private TextField statusField;
    @FXML
    private DatePicker deadlinePicker;

     @FXML
    private ComboBox<Category> categoryBox;

    @FXML
    private ComboBox<Priority> priorityBox;


    @FXML
    private void createTask() {
        String title = titleField.getText();
        String description = descriptionField.getText();
        Category category = categoryBox.getValue();
        Priority priority = priorityBox.getValue();
        LocalDate deadline = deadlinePicker.getValue();

        if (title == null || title.isEmpty() || category == null || priority == null || deadline == null) {
            showAlert("Error", "Please fill all fields!", Alert.AlertType.ERROR);
            return;
        }

        Task newTask = new Task(title, description, category, priority, deadline);
        TaskManager taskManager = TaskManager.getInstance();
        taskManager.addTask(newTask);
        taskManager.saveTasks();

        showAlert("Success", "Task created successfully!", Alert.AlertType.INFORMATION);

        // Clear the form fields
        titleField.clear();
        descriptionField.clear();
        categoryBox.setValue(null);
        priorityBox.setValue(null);
        deadlinePicker.setValue(null);
    }

    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void goBack() {
        switchScene("/com/medialab/views/TaskView.fxml");
    }

      private void switchScene(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load(); // Load the FXML file
            
            // Get the current stage
            Stage stage = (Stage) titleField.getScene().getWindow();
    
            // Set the new scene
            stage.setScene(new Scene(root));
            stage.show(); // Ensure the stage is shown
        } catch (Exception e) {
            e.printStackTrace();
    }
    }
}
