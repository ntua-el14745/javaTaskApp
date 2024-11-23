package com.medialab.controllers;

import com.medialab.models.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.time.LocalDate;

public class AddTaskController {

    @FXML
    private TextField taskTitleField;
    @FXML
    private TextArea taskDescriptionArea;
    @FXML
    private TextField taskCategoryField;
    @FXML
    private TextField taskPriorityField;
    @FXML
    private DatePicker taskDeadlinePicker;

    private TaskManager taskManager; // Injected from MainController

    public void setTaskManager(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @FXML
    public void handleAddTask(ActionEvent event) {
        String title = taskTitleField.getText();
        String description = taskDescriptionArea.getText();
        String categoryName = taskCategoryField.getText();
        String priorityName = taskPriorityField.getText();
        LocalDate deadline = taskDeadlinePicker.getValue();

        // Validate fields (optional)
        if (title.isEmpty() || description.isEmpty() || categoryName.isEmpty() || priorityName.isEmpty() || deadline == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "All fields are required!", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        // Create and add task
        Task task = new Task(
            title,
            description,
            new Category(categoryName),
            new Priority(priorityName.toUpperCase()), // Ensure priority matches enum
            deadline
        );
        taskManager.addTask(task);

        // Close the form
        Stage stage = (Stage) taskTitleField.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void handleCancel(ActionEvent event) {
        // Close the form
        Stage stage = (Stage) taskTitleField.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void handleViewCategories() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/medialab/views/CategoriesView.fxml"));
            Parent root = loader.load();
            CategoriesController controller = loader.getController();
            controller.setTaskManager(taskManager);

            Stage stage = new Stage();
            stage.setTitle("Categories");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleViewPriorities() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/medialab/views/PrioritiesView.fxml"));
            Parent root = loader.load();
            PrioritiesController controller = loader.getController();
            controller.setTaskManager(taskManager);

            Stage stage = new Stage();
            stage.setTitle("Priorities");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

