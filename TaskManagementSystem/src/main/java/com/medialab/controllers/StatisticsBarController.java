package com.medialab.controllers;

import java.time.LocalDate;
import java.util.List;

import com.medialab.models.Task;
import com.medialab.models.TaskManager;
import com.medialab.models.TaskStatus;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class StatisticsBarController extends BaseController {

    @FXML
    private Label totalTasksLabel;

    @FXML
    private Label completedTasksLabel;

    @FXML
    private Label delayedTasksLabel;

    @FXML
    private Label upcomingTasksLabel;

    private TaskManager taskManager;

    public void initialize() {
        taskManager = TaskManager.getInstance();
        updateStatistics();
        taskManager.addChangeListener(this::updateStatistics);
    }
    

    private void updateStatistics() {
        List<Task> allTasks = taskManager.getAllTasks();
        int totalTasks = allTasks.size();
        int completedTasks = (int) allTasks.stream().filter(task -> task.getStatus() == TaskStatus.COMPLETED).count();
        int delayedTasks = (int) allTasks.stream().filter(task -> task.getStatus() == TaskStatus.DELAYED).count();
        int upcomingTasks = (int) allTasks.stream().filter(task -> !task.getDeadline().isAfter(LocalDate.now().plusDays(7))).count();

         // Set the values for the labels
         totalTasksLabel.setText("Total Tasks: " + totalTasks);
         completedTasksLabel.setText("Completed Tasks: " + completedTasks);
         delayedTasksLabel.setText("Delayed Tasks: " + delayedTasks);
         upcomingTasksLabel.setText("Upcoming Tasks (within 7 days): " + upcomingTasks);
     }
    
}
