package com.medialab.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.medialab.models.Reminder.ReminderType;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

public class TaskManagerTest {

    private TaskManager taskManager;
    private Task task1;
    private Task task2;
    private Task task3;
    private Task task4;
    private Reminder reminder;

    @BeforeEach
    public void setUp() {
        taskManager = TaskManager.getInstance();

        // Create Category and Priority objects
        Category workCategory = new Category("Work");
        Category personalCategory = new Category("Personal");
        Priority highPriority = new Priority("High");
        Priority lowPriority = new Priority("Low");

        // Create some sample tasks
        task1 = new Task("Task 1", "Description of task 1", workCategory, highPriority, LocalDate.of(2024, 12, 15));
        task2 = new Task("Task 2", "Description of task 2", personalCategory, lowPriority, LocalDate.of(2024, 12, 20));
        task3 = new Task("Task 3", "Description of task 3", workCategory, lowPriority, LocalDate.of(2024, 12, 25));
        task4 = new Task("Task 4", "Description of task 4", workCategory, lowPriority, LocalDate.of(2024, 12, 30));

        // Add the tasks to the manager
        taskManager.addTask(task1);
        taskManager.addTask(task2);
        taskManager.addTask(task3);
        taskManager.addTask(task4);

        // Create a reminder for task 1
        ReminderType reminderType = ReminderType.ONE_DAY_BEFORE;
        reminder = new Reminder(LocalDate.of(2024, 12, 14), task1.getId(), reminderType, "Reminder for Task 1");
        taskManager.addReminderToTask(task1, reminder);

        // Save data to JSON files
        taskManager.save();
    }

    @Test
    public void testAddTask() {
        // Ensure the tasks were added correctly
        assertTrue(taskManager.getAllTasks().contains(task1));
        assertTrue(taskManager.getAllTasks().contains(task2));
    }

//     @Test
//     public void testRemoveTask() {
//         // Remove task 1 and check if it's removed
//         taskManager.deleteTask(task1);
//         assertFalse(taskManager.getAllTasks().contains(task1));
//         // Check if reminders for the task are also removed
//         assertTrue(taskManager.getRemindersForTask(task1).isEmpty());
//     }

//     @Test
//     public void testMarkTaskAsCompleted() {
//         // Mark task 1 as completed
//         taskManager.markTaskAsCompleted(task1);
//         assertEquals(TaskStatus.COMPLETED, task1.getStatus());
//         // Check if reminders for the completed task are removed
//         assertTrue(taskManager.getRemindersForTask(task1).isEmpty());
//     }

//     @Test
//     public void testAddReminderToTask() {
//         // Add the reminder to task 1
//         taskManager.addReminderToTask(task1, reminder);

//         // Check if the reminder was added
//         assertTrue(taskManager.getRemindersForTask(task1).contains(reminder));
//     }

//     @Test
//     public void testSaveAndLoadTasks() {
//         // Save the current state
//         taskManager.save();

//         // Load tasks into a new manager and verify the data
//         TaskManager newTaskManager = new TaskManager();
//         assertEquals(4, newTaskManager.getAllTasks().size());
//         assertTrue(newTaskManager.getAllTasks().stream()
//                 .anyMatch(task -> task.getTitle().equals("Task 1")));
//     }

//     @Test
//     public void testRemoveReminderFromTask() {
//         // Add and then remove a reminder from task 1
//         taskManager.addReminderToTask(task1, reminder);
//         taskManager.removeReminderFromTask(reminder);

//         // Check if the reminder was removed
//         assertFalse(taskManager.getRemindersForTask(task1).contains(reminder));
//     }

//     @Test
//     public void testGetRemindersForTask() {
//         // Ensure the reminder is correctly retrieved
//         assertEquals(1, taskManager.getRemindersForTask(task1).size());
//     }
}
