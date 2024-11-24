package com.medialab.controllers;

import com.medialab.models.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class ViewTaskController {
    @FXML
    private Label titleLabel;

    @FXML
    private TextArea descriptionArea;

    @FXML
    private Label categoryLabel;

    @FXML
    private Label priorityLabel;

    @FXML
    private Label deadlineLabel;

    public void setTask(Task task) {
        titleLabel.setText(task.getTitle());
        descriptionArea.setText(task.getDescription());
        categoryLabel.setText(task.getCategory().getName());
        priorityLabel.setText(task.getPriority().getName());
        deadlineLabel.setText(task.getDeadline().toString());
    }

    @FXML
    private void handleClose() {
        Stage stage = (Stage) titleLabel.getScene().getWindow();
        stage.close();
    }
}
