package com.example;

public class Operation {
    private int operationId;
    private String name;

    // constructor, getters, setters, etc.

    public Operation(int operationId, String name) {
        this.operationId = operationId;
        this.name = name;
    }

    // Getters and Setters
    public int getOperationId() {
        return operationId;
    }

    public void setOperationId(int operationId) {
        this.operationId = operationId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}