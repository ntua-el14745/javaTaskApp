package com.medialab.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddCategoryController {

    @FXML
    private TextField categoryNameField;

    @FXML
    private void handleAddCategory() {
        String categoryName = categoryNameField.getText();
        if (!categoryName.isEmpty()) {
            // Add the category to your TaskManager or relevant data structure
            System.out.println("Added category: " + categoryName);

            // Close the window
            Stage stage = (Stage) categoryNameField.getScene().getWindow();
            stage.close();
        }
    }
}
