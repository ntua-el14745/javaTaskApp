package com.medialab.controllers;

import com.medialab.models.TaskManager;
import com.medialab.utils.DialogUtils;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class PriorityController extends BaseController{
    @FXML
    private ListView<String> prioritiesListView;

    @FXML
    private TextField newPriorityField;

    @FXML
    private TextField editPriorityField;

    public void initialize() {
        taskManager = TaskManager.getInstance();
        refreshPriorityList();
    }

    
    @FXML
    private void handleAddPriority() {
        String priority = newPriorityField.getText();
        if (!priority.isEmpty()) {
            taskManager = TaskManager.getInstance();
            taskManager.addPriority(priority);
            prioritiesListView.getItems().add(priority);
            newPriorityField.clear();
        }
    }

    public void goBack() {
    switchScene("/com/medialab/views/MainLayout.fxml");
}
    private void switchScene(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load(); // Load the FXML file
            
            // Get the current stage
            Stage stage = (Stage) newPriorityField.getScene().getWindow();
    
            // Set the new scene
            stage.setScene(new Scene(root));
            stage.show(); // Ensure the stage is shown
        } catch (Exception e) {
            e.printStackTrace();
     }
    }
    @FXML
    private void handleEditPriority() {
        String selectedPriority = prioritiesListView.getSelectionModel().getSelectedItem();
        String newPriorityName = editPriorityField.getText();
        if (selectedPriority != null && !newPriorityName.isEmpty()) {
            boolean confirmed = DialogUtils.showConfirmationDialog(
                "Edit Priority",
                "Are you sure you want to rename the priority '" + selectedPriority + "' to '" + newPriorityName + "'?"
            );
            if (confirmed) {
                taskManager.editPriority(selectedPriority, newPriorityName);
                refreshPriorityList();
                taskManager.savePriorities();
                editPriorityField.clear();
            }
        } else {
            DialogUtils.showWarningDialog("Edit Error", "Please select a priority and provide a new name.");
        }
    }

    @FXML
    private void handleDeletePriority() {
        String selectedPriority = prioritiesListView.getSelectionModel().getSelectedItem();
        if (selectedPriority != null) {
            boolean confirmed = DialogUtils.showConfirmationDialog(
                "Delete Priority",
                "Are you sure you want to delete the priority '" + selectedPriority + "'? This will change all associated tasks priority to Default."
            );
            if (confirmed) {
                taskManager.deletePriority(selectedPriority);
                refreshPriorityList();
                taskManager.save();
            }
        } else {
            DialogUtils.showWarningDialog("Delete Error", "Please select a priority to delete.");
        }
    }

    private void refreshPriorityList() {
        ObservableList<String> priorities = FXCollections.observableArrayList(taskManager.getPriorities());
        prioritiesListView.setItems(priorities);
    }

    @FXML
    private void handlePrioritySelection(MouseEvent event) {
        String selectedPriority = prioritiesListView.getSelectionModel().getSelectedItem();
        if (selectedPriority != null) {
            editPriorityField.setText(selectedPriority);
        }
    }



}
