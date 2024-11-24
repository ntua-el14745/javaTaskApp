package com.medialab.utils;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.time.LocalDate;

import com.medialab.models.Reminder;

public class EditReminderDialog extends Dialog<Reminder> {

   @FXML
    private DatePicker datePicker;
    private TextField relatedTaskField;

    public EditReminderDialog(Reminder reminder) {
        setTitle("Edit Reminder");
        setHeaderText("Edit the details of the reminder");

        // Set the dialog's content
        datePicker = new DatePicker(reminder.getReminderDate());
        relatedTaskField = new TextField(reminder.getRelatedTask());

        VBox vbox = new VBox(10);
        vbox.getChildren().addAll(
            new Label("Reminder Date:"), datePicker,
            new Label("Related Task ID:"), relatedTaskField
        );
        getDialogPane().setContent(vbox);

        // Add "Save" and "Cancel" buttons
        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButtonType = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        getDialogPane().getButtonTypes().addAll(saveButtonType, cancelButtonType);

        // Handle "Save" action
        setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                LocalDate selectedDate = datePicker.getValue();
                String newRelatedTaskId = relatedTaskField.getText();

                if (selectedDate != null) {
                    reminder.setReminderDate(selectedDate); // Use midnight for the time
                    reminder.setRelatedTask(newRelatedTaskId);
                    return reminder;
                } else {
                    showError("Invalid Input", "Please select a valid date.");
                    return null;
                }
            }
            return null;
        });
    }
    private void showError(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
