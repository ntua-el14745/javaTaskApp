package com.medialab.models;

public enum TaskStatus {
    OPEN("Open"),
    IN_PROGRESS("In Progress"),
    POSTPONED("Postponed"),
    COMPLETED("Completed"),
    DELAYED("Delayed"),
    CATEGORY("");

    private final String displayName;

    // Constructor to associate a display name with each enum value
    TaskStatus(String displayName) {
        this.displayName = displayName;
    }

    // Getter for the display name
    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName; // When the enum is converted to a string, return the display name
    }
}
