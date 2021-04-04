package com.app.arguments;

public class Argument {

    private String name;
    private String description;
    private String value;

    public Argument(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public Argument(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
