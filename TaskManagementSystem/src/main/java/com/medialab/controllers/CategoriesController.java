package com.medialab.controllers;

import com.medialab.models.TaskManager;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class CategoriesController {
    @FXML
    private ListView<String> categoriesListView;

    @FXML
    private TextField newCategoryField;

    private TaskManager taskManager;

    public void setTaskManager(TaskManager taskManager) {
        this.taskManager = taskManager;
        categoriesListView.getItems().addAll(taskManager.getCategories());
    }

    @FXML
    private void handleAddCategory() {
        String category = newCategoryField.getText();
        if (!category.isEmpty()) {
            taskManager.addCategory(category);
            categoriesListView.getItems().add(category);
            newCategoryField.clear();
        }
    }
}
