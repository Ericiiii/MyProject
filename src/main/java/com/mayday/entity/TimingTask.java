package com.mayday.entity;

public class TimingTask {

    private Integer taskId;

    private String expression;

    public TimingTask() {

    }

    public TimingTask(Integer taskId, String expression) {
        this.taskId = taskId;
        this.expression = expression;
    }

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }
}
