package com.example;
public class UserOperation {
    private int userOperationId;
    private int userId;
    private int operationId;
    private int stateId;

    // constructor, getters, setters, etc.

    public UserOperation(int userOperationId, int userId, int operationId, int stateId) {
        this.userOperationId = userOperationId;
        this.userId = userId;
        this.operationId = operationId;
        this.stateId = stateId;
    }

    // Getters and Setters
    public int getUserOperationId() {
        return userOperationId;
    }

    public void setUserOperationId(int userOperationId) {
        this.userOperationId = userOperationId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getOperationId() {
        return operationId;
    }

    public void setOperationId(int operationId) {
        this.operationId = operationId;
    }

    public int getStateId() {
        return stateId;
    }

    public void setStateId(int stateId) {
        this.stateId = stateId;
    }
}
