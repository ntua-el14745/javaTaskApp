package com.medialab.controllers;

import com.medialab.models.Reminder;
import com.medialab.models.Task;
import com.medialab.models.TaskManager;
import com.medialab.models.TaskStatus;
import com.medialab.models.Reminder.ReminderType;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
// import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.time.LocalDate;
// import java.time.LocalTime;
// import java.time.LocalTime;
// import java.time.format.DateTimeFormatter;
// import java.time.format.DateTimeParseException;

public class AddReminderController extends BaseController{

    @FXML
    private DatePicker reminderDatePicker;
    @FXML
    private ComboBox<ReminderType> reminderTypeComboBox;
    // @FXML
    // private TextField reminderTimeField;

    private Task task;

    public void setTask(Task task) {
        this.task = task;
    }
    @FXML
    private void initialize() {
        // Populate the combo box with reminder types
        reminderTypeComboBox.getItems().setAll(ReminderType.values());

        // Listen for changes in the combo box
        reminderTypeComboBox.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue == ReminderType.SPECIFIC_DATE) {
                reminderDatePicker.setVisible(true);
            } else {
                reminderDatePicker.setVisible(false);
            }
        });
    }
    @FXML
    private void handleSaveReminder() {
    
    // Get the selected reminder type
    ReminderType selectedType = reminderTypeComboBox.getValue();

    if (selectedType == null) {
        // Show error if no reminder type is selected
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Reminder Error");
        alert.setHeaderText("No Reminder Type Selected");
        alert.setContentText("Please select a reminder type before proceeding.");
        alert.showAndWait();
        return;
    }

     // Check if the task is completed
    if (task.getStatus() == TaskStatus.COMPLETED) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Reminder Error");
        alert.setHeaderText("Cannot Add Reminder");
        alert.setContentText("This task is already completed. Reminders can only be set for active tasks.");
        alert.showAndWait();
        return;
    }

    // Retrieve the task's deadline
    LocalDate deadline = task.getDeadline();
    LocalDate reminderDate = null;
    // Calculate the reminder date based on the type
    switch (selectedType) {
        case ONE_DAY_BEFORE:
            reminderDate = deadline.minusDays(1);
            break;
        case ONE_WEEK_BEFORE:
            reminderDate = deadline.minusWeeks(1);
            break;
        case ONE_MONTH_BEFORE:
            reminderDate = deadline.minusMonths(1);
            break;
        case SPECIFIC_DATE:
            reminderDate = reminderDatePicker.getValue();
            break;
    }
    // Ensure reminder date is valid (e.g., not in the past)
    if (reminderDate == null || reminderDate.isBefore(LocalDate.now())) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Invalid Reminder Date");
        alert.setHeaderText("Reminder Date Error");
        alert.setContentText("The calculated reminder date is invalid. Please choose a valid date.");
        alert.showAndWait();
        return;
    }
        // Add the reminder (pass the selected type and date to your addReminder method)
        Reminder reminder = new Reminder(reminderDate, task.getId(), selectedType,"Default Reminder Message");
        // Add the reminder to the task using TaskManager
        TaskManager.getInstance().addReminderToTask(task, reminder);
         // Optionally show success message
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Reminder Added");
        alert.setHeaderText("Reminder Added Successfully");
        alert.setContentText("Your reminder has been set for: " + reminderDate);
        alert.showAndWait();
        // Close the dialog
        Stage stage = (Stage) reminderDatePicker.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void handleCancelReminder() {
        Stage stage = (Stage) reminderDatePicker.getScene().getWindow();
        stage.close();
    }

   
}
