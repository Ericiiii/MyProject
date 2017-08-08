package com.mayday.entity;

public class TimingTask {

    private Integer taskId;

    private Long expression;

    public TimingTask() {

    }

    public TimingTask(Integer taskId, Long expression) {
        this.taskId = taskId;
        this.expression = expression;
    }

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public Long getExpression() {
        return expression;
    }

    public void setExpression(Long expression) {
        this.expression = expression;
    }
}
