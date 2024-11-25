package com.medialab.models;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.io.File;
import java.io.IOException;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Singleton class for managing tasks, reminders, categories, and priorities.
 * Provides methods for adding, updating, deleting, and retrieving tasks and reminders,
 * as well as saving and loading data to/from JSON files.
 */
public class TaskManager {
    // Singleton instance of TaskManager
    private static TaskManager instance;
    private List<Task> tasks;
    private List<Reminder> reminders; 
    private List<String> categories = new ArrayList<>();
    private List<String> priorities = new ArrayList<>();
    // Scheduler instance
    private ScheduledExecutorService scheduler;
    // File paths for JSON persistence
    private static final String CATEGORIES_FILE = "medialab/categories.json";
    private static final String TASKS_FILE = "medialab/tasks.json";
    private static final String REMINDERS_FILE = "medialab/reminders.json";
    private static final String PRIORITIES_FILE = "medialab/priorities.json";

    // Default priority value
    private static final String DEFAULT_PRIORITY = "Default";

    private ObjectMapper objectMapper;
     /**
     * Private constructor to initialize TaskManager as a singleton.
     * Loads existing data from JSON files or initializes empty collections if files do not exist.
     */
    public TaskManager() {
        this.tasks = new ArrayList<>();
        this.reminders = new ArrayList<>();
        this.objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule()); 
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        load();
    }

     /**
     * Retrieves the singleton instance of TaskManager.
     * If the instance does not exist, it initializes a new one.
     *
     * @return The singleton instance of TaskManager.
     */
      public static TaskManager getInstance() {
        if (instance == null) {
            instance = new TaskManager();
        }
        return instance;
    }
        /**
         * Calls All the Save methods, for Tasks, Reminders, Categories and Priorities.
         * Used in order to save the state of the App on exit
         */
        public void save(){
            saveTasks();
            saveReminders();
            saveCategories(); 
            savePriorities(); 

        }
        /**
         * Calls All the Load methods, for Tasks, Reminders, Categories and Priorities.
         * Used in order to Load the last state of the App on start
         */
        public void load(){
            loadTasks();
            loadReminders();
            loadCategories(); 
            loadPriorities(); 
        }   
        
        /**
         * Saves all Tasks to a JSON file for persistence.
         */
        public void saveTasks() {
            try {
                objectMapper.writeValue(new File(TASKS_FILE), tasks);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        /**
         * Loads all Tasks from a JSON file. If the file does not exist, initializes an empty list.
         */
        public void loadTasks() {
            try {
                File file = new File(TASKS_FILE);
                if (file.exists() && file.length() > 0) { 
                    tasks = objectMapper.readValue(file, new TypeReference<List<Task>>() {});
                } else {
                    tasks = new ArrayList<>();
                }
            } catch (IOException e) {
                e.printStackTrace();
                tasks = new ArrayList<>();
            }
        }

        /**
         * Saves all Priorities to a JSON file for persistence.
         */
          public void savePriorities() {
            try {
                objectMapper.writeValue(new File(PRIORITIES_FILE), priorities);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    
        /**
         * Loads all Priorities from a JSON file. If the file does not exist, initializes an empty list.
         */
        public void loadPriorities() {
            try {
                File file = new File(PRIORITIES_FILE);
                if (file.exists() && file.length() > 0) { 
                    priorities = objectMapper.readValue(file, new TypeReference<List<String>>() {});
                } else {
                    priorities = new ArrayList<>();
                }
                addDefaultPriorityIfMissing();
            } catch (IOException e) {
                e.printStackTrace();
                priorities = new ArrayList<>();
            }
        }

         /**
           * Saves all Categories to a JSON file for persistence.
           */
         public void saveCategories() {
            try {
                objectMapper.writeValue(new File(CATEGORIES_FILE), categories);
            } catch (IOException e) {
             e.printStackTrace();
            }
        }

         /**
         * Loads all Categories from a JSON file. If the file does not exist, initializes an empty list.
         */
        public void loadCategories() {
            try {
                File file = new File(CATEGORIES_FILE);
                if (file.exists() && file.length() > 0) {
                    categories = objectMapper.readValue(file, new TypeReference<List<String>>() {});
                } else {
                    categories = new ArrayList<>();
                }
            } catch (IOException e) {
                e.printStackTrace();
                categories = new ArrayList<>();
            }
        }

        /**
           * Saves all Reminders to a JSON file for persistence.
           */
        public void saveReminders() {
            try {
                objectMapper.writeValue(new File(REMINDERS_FILE), reminders);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        /**
         * Loads all Reminders from a JSON file. If the file does not exist, initializes an empty list.
         */
        public void loadReminders() {
            try {
                File file = new File(REMINDERS_FILE);
                if (file.exists() && file.length() > 0) { 
                    reminders = objectMapper.readValue(file, new TypeReference<List<Reminder>>() {});
                    for (Reminder reminder : reminders) {
                        Task relatedTask = getTaskById(reminder.getRelatedTask());
                        if (relatedTask != null) {
                            reminder.setRelatedTask(relatedTask.getId());  // Set the related task
                        }
                    }
                } else {
                    // System.out.println("Reminders file does not exist or is empty, initializing an empty list.");
                    reminders = new ArrayList<>();
                }
            } catch (IOException e) {
                e.printStackTrace();
                reminders = new ArrayList<>();
                System.out.println("IOException occurred, initializing an empty list.");
            }
        }
    
        /**
         * Adds a new task to the task list. Generates a unique ID for the task if it is not already set.
         *
         * @param task The task to be added. Must not be null.
         */
        public void addTask(Task task) {
            if (task.getId() == null || task.getId().isEmpty()) {
            task.setId(UUID.randomUUID().toString());
            }
            tasks.add(task);
        }

        /**
         * Removes a task from the task list and deletes all reminders associated with it.
         *
         * @param task The task to be removed. Must not be null.
         */
        public void deleteTask(Task task) {
            tasks.remove(task);
            // Remove all reminders related to this task
            reminders.removeIf(reminder -> reminder.getRelatedTask().equals(task.getId()));
        }

        /**
         * Updates the properties of an existing task.
         *
         * @param task        The task to be updated. Must not be null.
         * @param title       The updated title of the task. Must not be null or empty.
         * @param description The updated description of the task. Can be null.
         * @param priority    The updated priority of the task. Must not be null.
         * @param dueDate     The updated due date of the task. Must not be null.
         */
        public void updateTask(Task task, String title, String description, Priority priority, LocalDate dueDate) {
            task.setTitle(title);
            task.setDescription(description);
            task.setPriority(priority);
            task.setDeadline(dueDate);
        }


          /**
         * Retrieves a list of all tasks managed by TaskManager.
         *
         * @return A list of all tasks.
         */
        public List<Task> getAllTasks() {
            return tasks;
        }

         /**
         * Retrieves tasks with a specific status.
         *
         * @param status The status to filter tasks by. Must not be null.
         * @return A list of tasks with the specified status.
         */
        public List<Task> getTasksByStatus(TaskStatus status) {
            return tasks.stream()
                    .filter(task -> task.getStatus() == status)
                    .collect(Collectors.toList());
        }

         /**
         * Retrieves a list of all tasks related to a specific category.
         *
         * @param categoryName The category in which the tasks must belong to.
         * @return A list of Task Objects.
         */
        public List<Task> getTasksByCategory(String categoryName) {
            List<Task> result = new ArrayList<>();
            for (Task task : tasks) {
                if (task.getCategory().getName().equalsIgnoreCase(categoryName)) {
                    result.add(task);
                }
            }
            return result;
        }

         /**
         * Retrieves a task with a specific id if found.
         *
         * @param id The id of the task to be retrieved.
         * @return a Task object.
         */
        public Task getTaskById(String id) {
            return tasks.stream()
                        .filter(task -> task.getId().equals(id))
                        .findFirst()
                        .orElse(null);
        }

        /**
         * Retrieves a list of all tasks related to a specific priority.
         *
         * @param priority The priority ithat the tasks must have.
         * @return A list of Task objects.
         */
        public List<Task> getTasksByPriority(Priority priority) {
            return tasks.stream()
                    .filter(task -> task.getPriority() == priority)
                    .collect(Collectors.toList());
        }

       
         /**
         * Retrieves a task with a specific title if found.
         *
         * @param title The title of the task to be retrieved.
         * @return a Task object.
         */
        public Task getTaskByTitle(String title) {
            return tasks.stream()
                    .filter(task -> task.getTitle().equalsIgnoreCase(title))
                    .findFirst()
                    .orElse(null);
        }

        
        /**
         * Marks a task as completed and removes its associated reminders.
         *
         * @param task The task to be marked as completed. Must not be null.
         */
        public void markTaskAsCompleted(Task task) {
            task.setStatus(TaskStatus.COMPLETED);
        
            // Remove associated reminders
            reminders.removeIf(reminder -> reminder.getRelatedTask().equals(task.getId()));
            saveReminders(); // Save changes to reminders
        }
        
         /**
         * Adds a reminder to a task and associates it with the task ID.
         *
         * @param task     The task to which the reminder is added. Must not be null.
         * @param reminder The reminder to be added. Must not be null.
         */
        public void addReminderToTask(Task task, Reminder reminder) {
            task.addReminder(reminder);
            reminder.setRelatedTask(task.getId());
            reminder.setId(UUID.randomUUID().toString());
            reminders.add(reminder);
        }

         /**
         * Removes a reminder from a task.
         *
         * @param reminder The reminder to be removed. Must not be null.
         */
        public void removeReminderFromTask(Reminder reminder) {
            reminders.remove(reminder);
        }

          /**
          * Retrieves all reminders associated with a task.
          *
          * @param task The task for which reminders are retrieved. Must not be null.
          * @return A list of reminders associated with the task.
          */
          public List<Reminder> getRemindersForTask(Task task) {
            return reminders.stream()
                    .filter(reminder -> reminder.getRelatedTask().equals(task.getId()))
                    .collect(Collectors.toList());
        }

         /**
          * Retrieves all reminders.
          *
          * @param task The task for which reminders are retrieved. Must not be null.
          * @return A list of reminders.
          */
        public List<Reminder> getAllReminders() {
            return reminders;
        }

        /**
          * Add a new category.
          *
          * @param category The category to add.
          */
        public void addCategory(String category) {
            if (!categories.contains(category)) {
                categories.add(category);
            }
        }

        /**
          * Add a new priority.
          *
          * @param priority The priority to add.
          */
        public void addPriority(String priority) {
            if (!priorities.contains(priority)) {
                priorities.add(priority);
            }
        }

        /**
          * Retrieves all categories.
          *
          * @return A list of categories.
          */
        public List<String> getCategories() {
            return categories;
        }

        /**
          * Retrieves all priorities.
          *
          * @return A list of priorities.
          */
        public List<String> getPriorities() {
            return priorities;
        }

         /**
          * Edits a category name and updates tasks that belonged to this category, with the new name.
          *
          * @param oldCategory The old category name.
          * @param newCategory The new category name.
          */
         public void editCategory(String oldCategory, String newCategory) {
            if (categories.contains(oldCategory) && !categories.contains(newCategory)) {
                categories.remove(oldCategory);
                categories.add(newCategory);

                // Update tasks with the old category
                for (Task task : tasks) {
                    if (task.getCategory().getName().equals(oldCategory)) {
                        task.setCategory(new Category(newCategory));
                    }
                }
            }
        }

        /**
          * Deletes a category and its associated tasks.
          *
          * @param category The category to be deleted.
          */
        public void deleteCategory(String category) {
            if (categories.remove(category)) {
                // Remove tasks associated with this category
                tasks = tasks.stream()
                             .filter(task -> !task.getCategory().getName().equals(category))
                             .collect(Collectors.toList());
            }
        }
         
         /**
          * Edits a priority name and updates tasks that had this priority, with the new name.
          *
          * @param oldPriority The old priority name.
          * @param newPriority The new priority name.
          */
         public void editPriority(String oldPriority, String newPriority) {
            if (priorities.contains(oldPriority) && !priorities.contains(newPriority)) {
                priorities.remove(oldPriority);
                priorities.add(newPriority);

                // Update tasks with the old priority
                for (Task task : tasks) {
                    if (task.getPriority().getName().equals(oldPriority)) {
                        task.setPriority(new Priority(newPriority));
                    }
                }
            }
        }

          /**
          * Deletes a priority and changes its associated tasks priority to default.
          *
          * @param priority The priority to be deleted.
          */
        public void deletePriority(String priority) {
            if (priorities.remove(priority)) {
                // Update tasks with the deleted priority to the default priority
                for (Task task : tasks) {
                    if (task.getPriority().getName().equals(priority)) {
                        task.setPriority(new Priority(DEFAULT_PRIORITY));
                    }
                }
            }
        }

        /**
          * Checks if the default priority is defined, if not it adds it in the priorities.
          *
          */
        public void addDefaultPriorityIfMissing() {
            if (!priorities.contains(DEFAULT_PRIORITY)) {
                priorities.add(DEFAULT_PRIORITY);
            }
        }

        /**
          * Checks for tasks that are delayed and if it finds any, updates their status.
          *
          */
        public void checkDelayedTasks() {
            LocalDate currentDate = LocalDate.now();
        
            for (Task task : tasks) {
                if (task.getStatus() != TaskStatus.COMPLETED && task.getDeadline().isBefore(currentDate)) {
                    task.setStatus(TaskStatus.DELAYED); // Set task status to DELAYED
                }
            }
        }

        /**
          * Checks for reminders that have to be displayed.
          *
          */
        public void initializeReminderChecker() {
            scheduler = Executors.newScheduledThreadPool(1);

            scheduler.scheduleAtFixedRate(() -> {
                LocalDate currentDate = LocalDate.now();
                List<Reminder> dueReminders = reminders.stream()
                    .filter(reminder -> (reminder.getReminderDate().isBefore(currentDate) || reminder.getReminderDate().isEqual(currentDate))&&
                                        !reminder.isNotified()) // Avoid notifying more than once
                    .collect(Collectors.toList());

                for (Reminder reminder : dueReminders) {
                    Task relatedTask = getTaskById(reminder.getRelatedTask());
                    String taskTitle = relatedTask != null ? relatedTask.getTitle() : "No related task";

                     Platform.runLater(() -> {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Reminder Notification");
                        alert.setHeaderText("Reminder Due!");
                        alert.setContentText("Task: " + taskTitle + "\nDue Date: " + reminder.getReminderDate());
                        alert.showAndWait();
                    });
                    // Mark as notified to prevent duplicate notifications
                    reminder.setNotified(true);
                }
             
             }, 0, 1, TimeUnit.MINUTES); // Check every minute
         }
         
          /**
         * Shuts down the Scheduler that checks for Reminders.
         * Used in order to shut down the thread on app exit.
         *
         * @param reminder The reminder to be updated.
         */
        public void shutdownReminderChecker() {
            if (scheduler != null && !scheduler.isShutdown()) {
                scheduler.shutdown();
            }
        }
         /**
         * Updates a reminder.
         *
         * @param reminder The reminder to be updated.
         */
        public void updateReminder(Reminder reminder) {
            // Update the reminder in the list of reminders
            reminders = reminders.stream()
                .map(existingReminder -> existingReminder.getId().equals(reminder.getId()) ? reminder : existingReminder)
                .collect(Collectors.toList());
            saveReminders(); // Save the updated list of reminders to the file
        }
        

         /**
         * Searches for tasks that are delayed
         * and displays them in a pop up
         *
         */
        public void showDelayedTasksPopup() {
            // Create a ListView to display the delayed tasks
            ListView<String> listView = new ListView<>();

            // Get the delayed tasks
            List<Task> delayedTasks = getAllTasks().stream()
                                           .filter(task -> task.getStatus() == TaskStatus.DELAYED)
                                           .collect(Collectors.toList());

            // If there are delayed tasks, populate the ListView
            if (!delayedTasks.isEmpty()) {
                for (Task task : delayedTasks) {
                    listView.getItems().add(task.getTitle() + " - " + task.getDeadline().toString());
                }
            } else {
                return;
                // listView.getItems().add("No delayed tasks.");
            }
        
            // Create a new window (Stage)
            Stage delayedTasksStage = new Stage();
            delayedTasksStage.setTitle("Delayed Tasks");
        
            // Create a layout (VBox) for better organization and styling
            VBox vbox = new VBox(15, 
                new Label("Delayed Tasks"),  // Title label with styling
                listView
            );
            vbox.setPadding(new Insets(20));  // Add padding around the VBox
            vbox.setStyle("-fx-background-color: #f4f4f4; -fx-border-color: #0078D4; -fx-border-width: 2px; -fx-border-radius: 10px;");  // Background color and border style
            vbox.setSpacing(15);  // Space between the label and list
            // Create a HBox to center the Close button
            HBox buttonBox = new HBox();
            buttonBox.setAlignment(javafx.geometry.Pos.CENTER);  // Center the button
            Button closeButton = new Button("Close");
            closeButton.setStyle("-fx-background-color: #FF5722; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10px 20px;");
            closeButton.setOnAction(event -> delayedTasksStage.close());
            buttonBox.getChildren().add(closeButton);  // Add the close button to the HBox
        
            // Add the HBox (with the centered close button) to the VBox
            vbox.getChildren().add(buttonBox);
        
            // Create a scene for the new window with the updated layout
            Scene scene = new Scene(vbox, 400, 300);  // Adjust size as needed
            delayedTasksStage.setScene(scene);
        
            // Show the stage (new window)
            delayedTasksStage.show();
        }       
}
