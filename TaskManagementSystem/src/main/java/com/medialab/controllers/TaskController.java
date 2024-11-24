package com.medialab.controllers;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
// import java.time.LocalDate;
import java.util.List;

import com.medialab.models.Category;
import com.medialab.models.Priority;
// import com.medialab.models.Category;
// import com.medialab.models.Priority;
import com.medialab.models.Task;
import com.medialab.models.TaskManager;
import com.medialab.models.TaskStatus;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Label;
// import javafx.scene.control.Alert;
// import javafx.scene.control.ComboBox;
// import javafx.scene.control.DatePicker;
// import javafx.scene.control.TableView;
// import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.beans.property.SimpleStringProperty;
// import javafx.beans.property.StringProperty;

public class TaskController extends BaseController {
    // @FXML
    // private TableView taskTable;

    @FXML
    private TextField searchField;

    @FXML
    private CheckBox searchTitleCheckBox;
    
    @FXML
    private CheckBox searchCategoryCheckBox;
    
    @FXML
    private CheckBox searchPriorityCheckBox;
    
    @FXML
    private TreeTableView<Task> taskTreeTable;

    @FXML
    private TreeTableColumn<Task, String> titleColumn;

    @FXML
    private TreeTableColumn<Task, String> statusColumn;

    @FXML
    private TreeTableColumn<Task, Void> actionsColumn;
    
    @FXML
    private Label noResultsLabel;

    private TaskManager taskManager;

     public void initialize() {
        // Link columns to task properties
        titleColumn.setCellValueFactory(param -> param.getValue().getValue().getTitleProp());
        statusColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getStatus().name()));

        // Initialize data
        taskManager = TaskManager.getInstance();
        loadTasksByCategory();
        // Initialize actions column
        setupActionsColumn();

         // Add listener to search field to filter tasks as the user types
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
        updateSearch();  // Filter tasks based on the new search text
    });
    }

    private void loadTasksByCategory() {
        // Create a dummy root node
        TreeItem<Task> root = new TreeItem<>(new Task("Root", "", new Category("RootCategory"), new Priority("Low"), LocalDate.now()));
        root.setExpanded(true);

        // Get categories from TaskManager
        List<String> categories = taskManager.getCategories();

        for (String category : categories) {
            // Create a category node
            Task categoryPlaceholder = new Task(category, "CATEGORY", new Category(category), new Priority("Low"), LocalDate.now());
            TreeItem<Task> categoryItem = new TreeItem<>(categoryPlaceholder);
            categoryItem.setExpanded(true);

            // Get tasks for the category
            List<Task> tasks = taskManager.getTasksByCategory(category);
            for (Task task : tasks) {
                // Create a task node
                TreeItem<Task> taskItem = new TreeItem<>(task);
                categoryItem.getChildren().add(taskItem);
            }

            // Add category node to root
            root.getChildren().add(categoryItem);
            }

        // Set the root for the TreeTableView
        taskTreeTable.setRoot(root);
        taskTreeTable.setShowRoot(false); // Hide the dummy root
    }

     private void setupActionsColumn() {
        actionsColumn.setCellFactory(param -> new TreeTableCell<>() {
            private final Button editButton = new Button("Edit Details");
            private final Button viewButton = new Button("View Details");
            private final Button updateStatusButton = new Button("Update Status");
            private final Button deleteButton = new Button("Delete");
            private final Button addReminder = new Button("Add Reminder");
            private final Button viewRemindersButton = new Button("View Reminders");
            {
                editButton.setOnAction(event -> handleEditTask(getTableRow().getItem()));
                viewButton.setOnAction(event -> handleViewTask(getTableRow().getItem()));
                updateStatusButton.setOnAction(event -> handleUpdateStatus(getTableRow().getItem()));
                deleteButton.setOnAction(event -> handleDeleteTask(getTableRow().getItem()));
                addReminder.setOnAction(event -> handleAddReminder(getTableRow().getItem()));
                viewRemindersButton.setOnAction(event -> handleViewReminders(getTableRow().getItem()));
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || getTableRow().getItem() == null || getTableRow().getItem().getDescription() == "CATEGORY") {
                    if (!empty && getTableRow().getItem() != null && getTableRow().getItem().getDescription() == "CATEGORY"){
                       getTableRow().getItem().setStatus(TaskStatus.CATEGORY);
                       setText(null); // Hide status for categories
                    }
                    setGraphic(null);

                } else {
                    HBox actionButtons = new HBox(5, editButton, viewButton, updateStatusButton, deleteButton, addReminder, viewRemindersButton);
                    setGraphic(actionButtons);
                }
            }
        });
    }   

    private void handleEditTask(Task task) {
    if (task != null) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/medialab/views/EditTaskView.fxml"));
            Parent root = loader.load();

            EditTaskController controller = loader.getController();
            controller.setTask(task);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Edit Task");
            stage.showAndWait();

            loadTasksByCategory(); // Refresh tasks
        } catch (IOException e) {
            e.printStackTrace();
        }
       }
    }

    private void handleViewTask(Task task) {
        if (task != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/medialab/views/TaskDetails.fxml"));
                Parent root = loader.load();

                ViewTaskController controller = loader.getController();
                controller.setTask(task);

                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("Task Details");
                stage.showAndWait();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

        private void handleAddReminder(Task task) {
        if (task != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/medialab/views/AddReminderView.fxml"));
                Parent root = loader.load();
            
                AddReminderController controller = loader.getController();
                controller.setTask(task);
            
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("Add Reminder");
                stage.initModality(Modality.APPLICATION_MODAL); // Make it modal
                stage.showAndWait();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private void handleViewReminders(Task task) {
        if (task != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/medialab/views/ReminderView.fxml"));
                Parent root = loader.load();

                ReminderController controller = loader.getController();
                controller.setTask(task);

                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("Reminders for Task: " + task.getTitle());
                stage.showAndWait();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleUpdateStatus(Task task) {
    if (task != null) {
        // Filter TaskStatus values to exclude CATEGORY
        TaskStatus[] availableStatuses = 
            Arrays.stream(TaskStatus.values())
                  .filter(status -> status != TaskStatus.CATEGORY)
                  .toArray(TaskStatus[]::new);

        // Create a choice dialog with the filtered statuses
        ChoiceDialog<TaskStatus> statusDialog = new ChoiceDialog<>(task.getStatus(), availableStatuses);
        statusDialog.setTitle("Update Task Status");
        statusDialog.setHeaderText("Change Task Status");
        statusDialog.setContentText("Select the new status for the task:");

        // Show the dialog and get the user's choice
        statusDialog.showAndWait().ifPresent(selectedStatus -> {
            if (selectedStatus != task.getStatus()) {
                // Update the task's status
                task.setStatus(selectedStatus);
                taskManager.saveTasks(); // Save changes to the TaskManager
                loadTasksByCategory(); // Refresh the task list
                showAlert(Alert.AlertType.INFORMATION, "Status Updated", "Task status successfully updated to: " + selectedStatus);
            } else {
                showAlert(Alert.AlertType.INFORMATION, "No Changes", "Task status remains the same.");
            }
        });
    }
}

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    private void handleDeleteTask(Task task) {
        if (task != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this task?");
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    taskManager.deleteTask(task); // Implement this in TaskManager
                    taskManager.saveTasks();
                    loadTasksByCategory(); // Refresh view
                }
            });
        }
    }


    public void goBack() {
        switchScene("/com/medialab/views/MainLayout.fxml");
    }

    public void navigateToCreateTask() {
        switchScene("/com/medialab/views/CreateTaskView.fxml");
    }

    private void switchScene(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load(); // Load the FXML file
            
            // Get the current stage
            Stage stage = (Stage) searchField.getScene().getWindow();
    
            // Set the new scene
            stage.setScene(new Scene(root));
            stage.show(); // Ensure the stage is shown
        } catch (Exception e) {
            e.printStackTrace();
    }
}
private void updateSearch() {
    String searchText = searchField.getText().toLowerCase();

    boolean searchByTitle = searchTitleCheckBox.isSelected();
    boolean searchByCategory = searchCategoryCheckBox.isSelected();
    boolean searchByPriority = searchPriorityCheckBox.isSelected();

    List<String> categories = taskManager.getCategories();
    TreeItem<Task> root = new TreeItem<>(null);
    root.setExpanded(true);

    boolean hasResults = false;

    for (String category : categories) {
        // Create a category item (but do not add it to the root yet)
        TreeItem<Task> categoryItem = new TreeItem<>(new Task(category, "CATEGORY", new Category(category), new Priority("Low"), LocalDate.now()));
        categoryItem.setExpanded(true);

        // Get tasks for the category
        List<Task> tasks = taskManager.getTasksByCategory(category);
        boolean hasMatchingTasks = false;

        for (Task task : tasks) {
            // Skip the category placeholder tasks
            if ("CATEGORY".equals(task.getDescription())) {
                continue;
            }

            // Check if the task matches any selected search criteria
            boolean matchesSearch = false;
            if (searchByTitle && task.getTitle().toLowerCase().contains(searchText)) {
                matchesSearch = true;
            }
            if (searchByCategory && task.getCategory().getName().toLowerCase().contains(searchText)) {
                matchesSearch = true;
            }
            if (searchByPriority && task.getPriority().getName().toLowerCase().contains(searchText)) {
                matchesSearch = true;
            }

            // Add task to the category if it matches the search
            if (matchesSearch) {
                TreeItem<Task> taskItem = new TreeItem<>(task);
                categoryItem.getChildren().add(taskItem);
                hasMatchingTasks = true;
                hasResults = true;
            }
        }

        // Add the category node to the root only if it has matching tasks
        if (hasMatchingTasks) {
            root.getChildren().add(categoryItem);
        }
    }

    if (hasResults) {
        noResultsLabel.setVisible(false); // Hide the "No Results" label
        taskTreeTable.setRoot(root);
        taskTreeTable.setShowRoot(false); // Hide the dummy root node
    } else {
        noResultsLabel.setVisible(true); // Show the "No Results" label
        taskTreeTable.setRoot(null); // Clear the TreeTableView
    }
}


@FXML
private void handleClearSearch() {
    searchField.clear(); // Clear the search text
    noResultsLabel.setVisible(false); // Hide the "No Results" label
    loadTasksByCategory(); // Reset the tasks to show all tasks
}

 
    
}