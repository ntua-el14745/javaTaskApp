package com.medialab.controllers;

import com.medialab.models.Reminder;
import com.medialab.models.Task;
import com.medialab.models.TaskManager;
import com.medialab.utils.EditReminderDialog;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.time.format.DateTimeFormatter;

public class ReminderController extends BaseController{
    @FXML
    private TableView<Reminder> reminderTable;

    @FXML
    private TableColumn<Reminder, String> dateColumn;

    @FXML
    private TableColumn<Reminder, String> timeColumn;

    @FXML
    private TableColumn<Reminder, Void> actionsColumn;

    private Task task;
    private TaskManager taskManager;

    public void initialize() {
        taskManager = TaskManager.getInstance();
        // Set up columns
        dateColumn.setCellValueFactory(param -> 
        new SimpleStringProperty(param.getValue().getReminderDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
        );
        setupActionsColumn();
    }

    public void setTask(Task task) {
        this.task = task;
        loadReminders();
    }

    private void loadReminders() {
        reminderTable.getItems().setAll(taskManager.getRemindersForTask(task));
    }

    private void setupActionsColumn() {
    actionsColumn.setCellFactory(param -> new TableCell<Reminder, Void>() {
        private final Button deleteButton = new Button("Delete");
        private final Button editButton = new Button("Edit");

        {
            deleteButton.setStyle("-fx-background-color: red; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 5 10; -fx-alignment: CENTER;");
            deleteButton.setOnAction(event -> handleDeleteReminder(getTableRow().getItem()));
            editButton.setOnAction(event -> handleEditReminder(getTableRow().getItem()));
        }

        @Override
        protected void updateItem(Void item, boolean empty) {
            super.updateItem(item, empty);

            if (empty || getTableRow().getItem() == null) {
                setGraphic(null);
            } else {
                HBox hbox = new HBox(10);
                hbox.getChildren().addAll(editButton, deleteButton);
                setGraphic(hbox);
                }
            }
        });
    }
    private void handleEditReminder(Reminder reminder) {
        if (reminder != null) {
            // Open an Edit Reminder dialog
            EditReminderDialog dialog = new EditReminderDialog(reminder);
            dialog.showAndWait().ifPresent(updatedReminder -> {
                taskManager.updateReminder(updatedReminder); // Update the reminder in the task manager
                loadReminders(); // Reload the reminders
                showAlert("Success", "Reminder updated successfully.", Alert.AlertType.INFORMATION);
            });
        }
    }


    private void handleDeleteReminder(Reminder reminder) {
        if (reminder != null) {
            taskManager.removeReminderFromTask(reminder);
            loadReminders();
            showAlert("Success", "Reminder deleted successfully.", Alert.AlertType.INFORMATION);
        }
    }

    private void showAlert(String title, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    private void handleClose() {
        Stage stage = (Stage) reminderTable.getScene().getWindow();
        stage.close();
    }
}
