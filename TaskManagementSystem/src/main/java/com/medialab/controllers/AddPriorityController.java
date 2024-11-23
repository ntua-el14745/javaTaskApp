package com.medialab.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddPriorityController {

    @FXML
    private TextField priorityNameField;

    @FXML
    private void handleAddPriority() {
        String priorityName = priorityNameField.getText();
        if (!priorityName.isEmpty()) {
            // Add the priority to your TaskManager or relevant data structure
            System.out.println("Added priority: " + priorityName);

            // Close the window
            Stage stage = (Stage) priorityNameField.getScene().getWindow();
            stage.close();
        }
    }
}
