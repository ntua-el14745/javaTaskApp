package com.medialab.models;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;


public class Priority {
    private String name;

    public static final Priority DEFAULT = new Priority("Default");

    @JsonCreator
    public Priority(@JsonProperty("name") String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Priority priority = (Priority) obj;
        return Objects.equals(name, priority.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "Priority{" +
                "name='" + name + '\'' +
                '}';
    }
}
