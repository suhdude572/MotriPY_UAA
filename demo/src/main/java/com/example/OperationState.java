package com.example;

public class OperationState {
    private int operationStateId;
    private int operationId;
    private String stateName;
    private boolean isFinalState;

    // constructor, getters, setters, etc.

    public OperationState(int operationStateId, int operationId, String stateName, boolean isFinalState) {
        this.operationStateId = operationStateId;
        this.operationId = operationId;
        this.stateName = stateName;
        this.isFinalState = isFinalState;
    }

    // Getters and Setters
    public int getOperationStateId() {
        return operationStateId;
    }

    public void setOperationStateId(int operationStateId) {
        this.operationStateId = operationStateId;
    }

    public int getOperationId() {
        return operationId;
    }

    public void setOperationId(int operationId) {
        this.operationId = operationId;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public boolean isFinalState() {
        return isFinalState;
    }

    public void setFinalState(boolean isFinalState) {
        this.isFinalState = isFinalState;
    }
}