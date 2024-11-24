package com.medialab.models;

import java.time.LocalDate;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Reminder {
    private String message;
    private LocalDate reminderDate;
    private String relatedTaskID;
    private ReminderType reminderType;
    private boolean notified = false; 
    private String id;
    @JsonCreator
    public Reminder(@JsonProperty("reminderDate") LocalDate reminderDate,
                    @JsonProperty("relatedTask") String relatedTaskID,
                    @JsonProperty("reminderType") ReminderType reminderType,
                    @JsonProperty("message") String message) {
        this.reminderDate = reminderDate;
        this.relatedTaskID = relatedTaskID;
        this.reminderType = reminderType;
        this.message = message;
    }
    // Getter and Setter for notified
    public boolean isNotified() {
        return notified;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public void setNotified(boolean notified) {
        this.notified = notified;
    }
    public LocalDate getReminderDate() {
        return reminderDate;
    }

    public void setReminderDate(LocalDate reminderDate) {
        this.reminderDate = reminderDate;
    }

    public String getRelatedTask() {
        return relatedTaskID;
    }

    public void setRelatedTask(String relatedTask) {
        this.relatedTaskID = relatedTask;
    }

    public ReminderType getReminderType() {
        return reminderType;
    }

    public void setReminderType(ReminderType reminderType) {
        this.reminderType = reminderType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Reminder reminder = (Reminder) obj;
        return Objects.equals(reminderDate, reminder.reminderDate) &&
                Objects.equals(relatedTaskID, reminder.relatedTaskID) &&
                reminderType == reminder.reminderType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(reminderDate, relatedTaskID, reminderType);
    }

    @Override
    public String toString() {
        return "Reminder{" +
                "reminderDate=" + reminderDate +
                ", relatedTask=" + relatedTaskID +
                ", reminderType=" + reminderType +
                ", message='" + message + '\'' +
                '}';
    }

    // Reminder Types
    public enum ReminderType {
        ONE_DAY_BEFORE,
        ONE_WEEK_BEFORE,
        ONE_MONTH_BEFORE,
        SPECIFIC_DATE
    }

    
}
