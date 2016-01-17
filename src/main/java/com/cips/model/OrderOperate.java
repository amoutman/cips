package com.cips.model;

import java.util.Date;

public class OrderOperate {
    private String id;

    private String orderId;

    private String taskId;

    private String orderAccountId;

    private Integer status;

    private String operatedId;

    private Date opEndTime;

    private Integer opSequence;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId == null ? null : orderId.trim();
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId == null ? null : taskId.trim();
    }

    public String getOrderAccountId() {
        return orderAccountId;
    }

    public void setOrderAccountId(String orderAccountId) {
        this.orderAccountId = orderAccountId == null ? null : orderAccountId.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getOperatedId() {
        return operatedId;
    }

    public void setOperatedId(String operatedId) {
        this.operatedId = operatedId == null ? null : operatedId.trim();
    }

    public Date getOpEndTime() {
        return opEndTime;
    }

    public void setOpEndTime(Date opEndTime) {
        this.opEndTime = opEndTime;
    }

    public Integer getOpSequence() {
        return opSequence;
    }

    public void setOpSequence(Integer opSequence) {
        this.opSequence = opSequence;
    }
}