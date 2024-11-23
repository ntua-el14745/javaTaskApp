package com.medialab.controllers;

import com.medialab.models.TaskManager;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class PrioritiesController {
    @FXML
    private ListView<String> prioritiesListView;

    @FXML
    private TextField newPriorityField;

    private TaskManager taskManager;

    public void setTaskManager(TaskManager taskManager) {
        this.taskManager = taskManager;
        prioritiesListView.getItems().addAll(taskManager.getPriorities());
    }

    @FXML
    private void handleAddPriority() {
        String priority = newPriorityField.getText();
        if (!priority.isEmpty()) {
            taskManager.addPriority(priority);
            prioritiesListView.getItems().add(priority);
            newPriorityField.clear();
        }
    }
}
