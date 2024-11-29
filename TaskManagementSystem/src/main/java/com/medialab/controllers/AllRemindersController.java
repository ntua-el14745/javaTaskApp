package com.medialab.controllers;

import com.medialab.models.Reminder;
import com.medialab.models.Task;
import com.medialab.models.TaskManager;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class AllRemindersController extends BaseController {

    @FXML
    private TableView<Reminder> remindersTable;

    @FXML
    private TableColumn<Reminder, String> taskColumn;

    @FXML
    private TableColumn<Reminder, String> dateColumn;

    @FXML
    private TableColumn<Reminder, String> timeColumn;

    @FXML
    private TableColumn<Reminder, Void> actionsColumn;

    private TaskManager taskManager;

    public void initialize() {
        taskManager = TaskManager.getInstance();
        setupColumns();
        loadReminders();
    }

    private void setupColumns() {
        // Task name column: Get the task title based on relatedTaskId
        taskColumn.setCellValueFactory(param -> {
            Reminder reminder = param.getValue();
            Task relatedTask = taskManager.getTaskById(reminder.getRelatedTask()); // Fetch the task
            return new SimpleStringProperty(relatedTask != null ? relatedTask.getTitle() : "Unknown Task");
        });

        // Reminder date column
        dateColumn.setCellValueFactory(param -> 
            new SimpleStringProperty(param.getValue().getReminderDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
        );

    
        // Actions column (Delete button)
        actionsColumn.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = new Button("Delete");

            {
                // Style the delete button to appear red
                deleteButton.setStyle("-fx-background-color: red; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 5 10; -fx-alignment: CENTER;");
                deleteButton.setOnAction(event -> handleDeleteReminder(getTableRow().getItem()));
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableRow().getItem() == null) {
                    setGraphic(null);
                } else {
                    HBox actions = new HBox(deleteButton);
                    setGraphic(actions);
                }
            }
        });
    }

    private void loadReminders() {
        List<Reminder> reminders = taskManager.getAllReminders();
        remindersTable.getItems().setAll(reminders);
    }

    private void handleDeleteReminder(Reminder reminder) {
        if (reminder != null) {
            taskManager.removeReminderFromTask(reminder);
            loadReminders(); // Refresh the table
        }
    }

    @FXML
    private void handleBack() {
        switchScene("/com/medialab/views/MainLayout.fxml");
    }

    public void switchScene(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load(); // Load the FXML file
            // Get the current stage
            Stage stage = (Stage) remindersTable.getScene().getWindow();
    
            // Set the new scene
            Scene newScene = new Scene(root);
            stage.setScene(newScene);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
