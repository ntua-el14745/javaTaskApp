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
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.stage.Stage;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class TaskController extends BaseController {
    @FXML
    private TableView taskTable;

    @FXML
    private TextField searchField;

       @FXML
    private TreeTableView<Task> taskTreeTable;

    @FXML
    private TreeTableColumn<Task, String> titleColumn;

    @FXML
    private TreeTableColumn<Task, String> statusColumn;

               
    private TaskManager taskManager;

     public void initialize() {
        // Link columns to task properties
        titleColumn.setCellValueFactory(param -> param.getValue().getValue().getTitleProp());
        statusColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getStatus().name()));

        // Initialize data
        taskManager = TaskManager.getInstance();
        loadTasksByCategory();
    }

    private void loadTasksByCategory() {
        TreeItem<Task> root = new TreeItem<>(null); // Root node
        root.setExpanded(true);

        // Get categories
        List<String> categories = taskManager.getCategories();
        for (String category : categories) {
            TreeItem<Task> categoryItem = new TreeItem<Task>(); // Category node
            categoryItem.setExpanded(true);

            // Get tasks for this category
            List<Task> tasks = taskManager.getTasksByCategory(category);
            for (Task task : tasks) {
                TreeItem<Task> taskItem = new TreeItem<>(task); // Task node
                categoryItem.getChildren().add(taskItem);
            }

            root.getChildren().add(categoryItem);
        }

        taskTreeTable.setRoot(root);
        taskTreeTable.setShowRoot(false); // Hide the root node
    }

    public void goBack() {
        switchScene("/com/medialab/views/MainLayout.fxml");
    }

    public void navigateToCreateTask() {
        switchScene("/com/medialab/views/CreateTaskView.fxml");
    }

    private void switchScene(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load(); // Load the FXML file
            
            // Get the current stage
            Stage stage = (Stage) searchField.getScene().getWindow();
    
            // Set the new scene
            stage.setScene(new Scene(root));
            stage.show(); // Ensure the stage is shown
        } catch (Exception e) {
            e.printStackTrace();
    }
}

    public void setTaskManager(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

 
    
}