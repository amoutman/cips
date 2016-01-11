package com.cips.model;

import java.util.Date;

public class OrderOperate {
    private String id;

    private String orderId;

    private Integer status;

    private String operatedId;

    private Date opBeginTime;

    private Date opEndTime;

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

    public Date getOpBeginTime() {
        return opBeginTime;
    }

    public void setOpBeginTime(Date opBeginTime) {
        this.opBeginTime = opBeginTime;
    }

    public Date getOpEndTime() {
        return opEndTime;
    }

    public void setOpEndTime(Date opEndTime) {
        this.opEndTime = opEndTime;
    }
}