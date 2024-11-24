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

public class CategoryController extends BaseController{
    @FXML
    private ListView<String> categoriesListView;

    @FXML
    private TextField newCategoryField;

    @FXML
    private TextField editCategoryField;

    public void initialize() {
        taskManager = TaskManager.getInstance();
        refreshCategoryList();
    }

       @FXML
    private void handleAddCategory() {
        String category = newCategoryField.getText();
        if (!category.isEmpty()) {
            taskManager.addCategory(category);
            refreshCategoryList();
            taskManager.saveCategories();
            newCategoryField.clear();
        }
    }

    @FXML
    private void handleEditCategory() {
        String selectedCategory = categoriesListView.getSelectionModel().getSelectedItem();
        String newCategoryName = editCategoryField.getText();
        if (selectedCategory != null && !newCategoryName.isEmpty()) {
            boolean confirmed = DialogUtils.showConfirmationDialog(
                "Edit Category",
                "Are you sure you want to rename the category '" + selectedCategory + "' to '" + newCategoryName + "'?"
            );
            if (confirmed) {
                taskManager.editCategory(selectedCategory, newCategoryName);
                refreshCategoryList();
                taskManager.saveCategories();
                editCategoryField.clear();
            }
        } else {
            DialogUtils.showWarningDialog("Edit Error", "Please select a category and provide a new name.");
        }
    }

    @FXML
    private void handleDeleteCategory() {
        String selectedCategory = categoriesListView.getSelectionModel().getSelectedItem();
        if (selectedCategory != null) {
            boolean confirmed = DialogUtils.showConfirmationDialog(
                "Delete Category",
                "Are you sure you want to delete the category '" + selectedCategory + "'? This will remove all associated tasks."
            );
            if (confirmed) {
                taskManager.deleteCategory(selectedCategory);
                refreshCategoryList();
                taskManager.save();
            }
        } else {
            DialogUtils.showWarningDialog("Delete Error", "Please select a category to delete.");
        }
    }

    private void refreshCategoryList() {
        ObservableList<String> categories = FXCollections.observableArrayList(taskManager.getCategories());
        categoriesListView.setItems(categories);
    }

    @FXML
    private void handleCategorySelection(MouseEvent event) {
        String selectedCategory = categoriesListView.getSelectionModel().getSelectedItem();
        if (selectedCategory != null) {
            editCategoryField.setText(selectedCategory);
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
            Stage stage = (Stage) newCategoryField.getScene().getWindow();
    
            // Set the new scene
            stage.setScene(new Scene(root));
            stage.show(); // Ensure the stage is shown
        } catch (Exception e) {
            e.printStackTrace();
    }
}

}
