package com.medialab.controllers;

import java.time.LocalDate;
import java.util.List;

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
    private ComboBox<String> categoryBox;

    @FXML
    private ComboBox<String> priorityBox;

       private TaskManager taskManager;

    // Initialize method to populate ComboBoxes
    @FXML
    public void initialize() {
        taskManager = TaskManager.getInstance();
        
        // Retrieve and populate categories in the ComboBox
        List<String> categories = taskManager.getCategories();  // Assuming getCategories() returns a list of categories
        categoryBox.getItems().setAll(categories);

        // Retrieve and populate priorities in the ComboBox
        List<String> priorities = taskManager.getPriorities();  // Assuming getPriorities() returns a list of priorities
        priorityBox.getItems().setAll(priorities);
    }

    @FXML
    private void createTask() {
        String title = titleField.getText();
        String description = descriptionField.getText();
        Category category = new Category(categoryBox.getValue());
        Priority priority = new Priority(priorityBox.getValue());
        LocalDate deadline = deadlinePicker.getValue();

        if (title == null || title.isEmpty() || category == null || category.getName().isEmpty() || priority == null || priority.getName().isEmpty() || deadline == null) {
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
