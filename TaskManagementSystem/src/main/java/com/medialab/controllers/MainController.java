



package com.medialab.controllers;

import com.medialab.models.TaskManager;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class MainController {

    @FXML
    private Button taskButton;
    @FXML
    private Button reminderButton;
    @FXML
    private Button categoryButton;
    @FXML
    private Button priorityButton;

    private TaskManager taskManager;

    public void navigateToTaskScene() {
        switchScene("/com/medialab/views/TaskView.fxml");
    }

    public void navigateToReminderScene() {
        switchScene("/com/medialab/views/ReminderView.fxml");
    }

    public void navigateToCategoryScene() {
        switchScene("/com/medialab/views/CategoryView.fxml");
        // switchScene("/com/medialab/views/Test.fxml");

    }

    public void navigateToPriorityScene() {
        switchScene("/com/medialab/views/PriorityView.fxml");
    }

    public void navigateToHelpScene() {
        switchScene("/com/medialab/views/HelpView.fxml");
    }


    public void setTaskManager(TaskManager taskManager) {
        this.taskManager = taskManager;
    }
    public void switchScene(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load(); // Load the FXML file
            // Pass the MainController to the new controller
            BaseController newController = loader.getController();
            newController.setMainController(this);
            // Pass TaskManager to the new controller
            // Object controller = loader.getController();
            // Get the current stage
            Stage stage = (Stage) taskButton.getScene().getWindow();
    
             // Save current stage size
            double currentWidth = stage.getWidth();
            double currentHeight = stage.getHeight();
            // Set the new scene
            Scene newScene = new Scene(root);
            stage.setScene(newScene);
            // Restore the saved size
            stage.setWidth(currentWidth);
            stage.setHeight(currentHeight);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public TaskManager getTaskManager() {
        return taskManager;
    }
    
}








// package com.medialab.controllers;

// import com.medialab.models.*;

// import java.io.IOException;
// import java.util.List;

// import javafx.event.ActionEvent;
// import javafx.fxml.FXML;
// import javafx.fxml.FXMLLoader;
// import javafx.scene.Node;
// import javafx.scene.Parent;
// import javafx.scene.Scene;
// import javafx.scene.control.Alert;
// import javafx.scene.control.Button;
// // import javafx.scene.control.Button;
// import javafx.scene.control.ButtonType;
// import javafx.scene.control.ContextMenu;
// import javafx.scene.control.Label;
// import javafx.scene.control.ListView;
// import javafx.scene.control.MenuButton;
// import javafx.scene.control.MenuItem;
// import javafx.scene.layout.VBox;
// import javafx.stage.Modality;
// import javafx.stage.Stage;

// public class MainController {

//     

//     @FXML
//     private ListView<String> taskListView;

//     @FXML
//     private Label titleLabel;

//     @FXML
//     private Label descriptionLabel;

//     @FXML
//     private Label categoryLabel;

//     @FXML
//     private Label priorityLabel;

//     @FXML
//     private Label deadlineLabel;

//     @FXML
//     private Label statusLabel;

//     @FXML
//     private Button categoryMenuButton;

//     @FXML
//     private MenuButton priorityMenuButton;
//     // @FXML
//     // private Button loadTasksButton;

//     @FXML
//     private Task currentTask;

//     @FXML
//     private ContextMenu categoryMenu;

//     @FXML
//     public void initialize() {
//         categoryMenu = new ContextMenu(
//         new MenuItem("View All Categories"),
//         new MenuItem("Add Category")
//         );
//          // Show the ContextMenu when the button is clicked
//          categoryMenuButton.setOnMouseClicked(event -> 
//         categoryMenu.show(categoryMenuButton, event.getScreenX(), event.getScreenY())
//         );
    
//     }

//     public MainController() {

//     }


//     public void handleLoadTasks(ActionEvent event) {
//         // System.out.println("Load tasks button clicked!");
//         List<String> taskTitles = taskManager.getAllTasks().stream()
//                                          .map(Task::getTitle)
//                                          .toList();
//         taskListView.getItems().clear();
//         taskListView.getItems().addAll(taskTitles);
//     }

//     @FXML
//     public void handleAddTask(ActionEvent event) {
//         try {
//             FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/medialab/views/AddTaskForm.fxml"));
//             Parent root = loader.load();
//             // Pass the task manager to the form's controller
//             AddTaskController controller = loader.getController();
//             controller.setTaskManager(taskManager);
//             // Create a new stage for the popup
//             Stage stage = new Stage();
//             stage.setTitle("Add New Task");
//             stage.setScene(new Scene(root));
//             stage.showAndWait(); // Wait for the form to close
//             // Refresh task table or list view after the task is added
//             refreshTaskListView();
//         } catch (Exception e) {
//             e.printStackTrace();
//         }
//     }
//         // Refresh the task list view
//     private void refreshTaskListView() {
//         taskListView.getItems().clear();
//         for (Task task : taskManager.getAllTasks()) {
//             taskListView.getItems().add(task.getTitle());
//         }
//     }
    
//     @FXML
//     private void handleDeleteTask(ActionEvent event) {
//         // Get the selected task from the list view
//         String selectedTaskTitle = taskListView.getSelectionModel().getSelectedItem();
//         if (selectedTaskTitle == null) {
//             // Show a warning if no task is selected
//             Alert alert = new Alert(Alert.AlertType.WARNING, "Please select a task to delete.", ButtonType.OK);
//             alert.showAndWait();
//             return;
//         }

//         // Confirm deletion
//         Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this task?", ButtonType.YES, ButtonType.NO);
//         confirmation.showAndWait();

//         if (confirmation.getResult() == ButtonType.YES) {
//             // Find and remove the task from the task manager
//             Task taskToDelete = taskManager.getAllTasks().stream()
//                     .filter(task -> task.getTitle().equals(selectedTaskTitle))
//                     .findFirst()
//                     .orElse(null);

//             if (taskToDelete != null) {
//                 taskManager.removeTask(taskToDelete); // Implement this method in your TaskManager if not done already
//             }

//             // Refresh the task list view
//             refreshTaskListView();
//         }
//     }   

//     public void showTaskDetails(ActionEvent event) throws IOException {
//         FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/medialab/views/TaskDetails.fxml"));
//         Parent root = loader.load();

//         Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
//         Scene scene = new Scene(root);
//         stage.setScene(scene);
//         stage.show();
//     }   

//     @FXML
//     public void handleTaskClick() {
//         // Get the selected task title
//         String selectedTaskTitle = taskListView.getSelectionModel().getSelectedItem();

//         if (selectedTaskTitle != null) {
//             // Find the corresponding task from the TaskManager
//             Task selectedTask = taskManager.getTaskByTitle(selectedTaskTitle);

//         if (selectedTask != null) {
//             // Update the details view
//             titleLabel.setText("Title: " + selectedTask.getTitle());
//             descriptionLabel.setText("Description: " + selectedTask.getDescription());
//             categoryLabel.setText("Category: " + selectedTask.getCategory().getName().toString());
//             priorityLabel.setText("Priority: " + selectedTask.getPriority().getName().toString());
//             deadlineLabel.setText("Deadline: " + selectedTask.getDeadline().toString());
//             statusLabel.setText("Status: " + selectedTask.getStatus().toString());

//         } else {
//             clearDetails();
//         }
//         } else {
//         clearDetails();
//         }
//     }

//     // Helper method to clear details view
//     private void clearDetails() {
//         titleLabel.setText("Title: ");
//         descriptionLabel.setText("Description: ");
//         categoryLabel.setText("Category: ");
//         priorityLabel.setText("Priority: ");
//         deadlineLabel.setText("Deadline: ");
//         statusLabel.setText("Status: ");

//     }
//     @FXML
//     private void handleAddCategory() {
//         try {
//             FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/medialab/views/AddCategory.fxml"));
//             Parent root = loader.load();
//             Stage stage = new Stage();
//             stage.setTitle("Add Category");
//             stage.initModality(Modality.APPLICATION_MODAL);
//             stage.setScene(new Scene(root));
//             stage.showAndWait();
//         } catch (Exception e) {
//             e.printStackTrace();
//         }
//     }

//     @FXML
//     private void handleAddPriority() {
//         try {
//             FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/medialab/views/AddPriority.fxml"));
//             Parent root = loader.load();
//             Stage stage = new Stage();
//             stage.setTitle("Add Priority");
//             stage.initModality(Modality.APPLICATION_MODAL);
//             stage.setScene(new Scene(root));
//             stage.showAndWait();
//         } catch (Exception e) {
//             e.printStackTrace();
//         }

//     }
//     @FXML
//     private void handleViewCategories() {
//         try {
//             FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/medialab/views/CategoriesView.fxml"));
//             Parent root = loader.load();
//             CategoriesController controller = loader.getController();
//             controller.setTaskManager(taskManager);

//             Stage stage = new Stage();
//             stage.setTitle("Categories");
//             stage.initModality(Modality.APPLICATION_MODAL);
//             stage.setScene(new Scene(root));
//             stage.show();
//         } catch (Exception e) {
//             e.printStackTrace();
//         }
//     }

//     @FXML
//     private void handleViewPriorities() {
//         try {
//             FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/medialab/views/PrioritiesView.fxml"));
//             Parent root = loader.load();
//             PrioritiesController controller = loader.getController();
//             controller.setTaskManager(taskManager);

//             Stage stage = new Stage();
//             stage.setTitle("Priorities");
//             stage.initModality(Modality.APPLICATION_MODAL);
//             stage.setScene(new Scene(root));
//             stage.show();
//         } catch (Exception e) {
//             e.printStackTrace();
//         }
//     }

// }
