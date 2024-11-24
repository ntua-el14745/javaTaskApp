package com.medialab.controllers;

import com.medialab.models.Category;
import com.medialab.models.Priority;
import com.medialab.models.Task;
import com.medialab.models.TaskManager;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class EditTaskController {
    @FXML
    private TextField titleField;

    @FXML
    private TextArea descriptionField;

    @FXML
    private ComboBox<String> categoryComboBox;

    @FXML
    private ComboBox<String> priorityComboBox;

    @FXML
    private DatePicker deadlinePicker;

    private TaskManager taskManager;
    private Task task;

    public void initialize() {
        taskManager = TaskManager.getInstance();

        // Populate categories and priorities
        categoryComboBox.getItems().addAll(taskManager.getCategories());
        priorityComboBox.getItems().addAll(taskManager.getPriorities());
    }

    public void setTask(Task task) {
        this.task = task;

        // Pre-fill fields with task data
        titleField.setText(task.getTitle());
        descriptionField.setText(task.getDescription());
        categoryComboBox.setValue(task.getCategory().getName());
        priorityComboBox.setValue(task.getPriority().getName());
        deadlinePicker.setValue(task.getDeadline());
    }

    @FXML
    private void handleSave() {
        if (task != null) {
            task.setTitle(titleField.getText());
            task.setDescription(descriptionField.getText());
            task.setCategory(new Category(categoryComboBox.getValue()));
            task.setPriority(new Priority(priorityComboBox.getValue()));
            task.setDeadline(deadlinePicker.getValue());

            taskManager.saveTasks();
        }

        closeWindow();
    }

    @FXML
    private void handleCancel() {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) titleField.getScene().getWindow();
        stage.close();
    }
}
