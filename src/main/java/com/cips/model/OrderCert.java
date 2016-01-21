package com.cips.model;

import java.util.Date;

public class OrderCert {
    private String id;

    private String orderId;

    private String orderOperateId;

    private String orderAccountId;

    private Integer taskType;

    private Integer status;

    private String certPic;

    private String createdId;

    private Date createdDate;

    private String modifiedId;

    private Date modifiedDate;

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

    public String getOrderOperateId() {
        return orderOperateId;
    }

    public void setOrderOperateId(String orderOperateId) {
        this.orderOperateId = orderOperateId == null ? null : orderOperateId.trim();
    }

    public String getOrderAccountId() {
        return orderAccountId;
    }

    public void setOrderAccountId(String orderAccountId) {
        this.orderAccountId = orderAccountId == null ? null : orderAccountId.trim();
    }

    public Integer getTaskType() {
        return taskType;
    }

    public void setTaskType(Integer taskType) {
        this.taskType = taskType;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getCertPic() {
        return certPic;
    }

    public void setCertPic(String certPic) {
        this.certPic = certPic == null ? null : certPic.trim();
    }

    public String getCreatedId() {
        return createdId;
    }

    public void setCreatedId(String createdId) {
        this.createdId = createdId == null ? null : createdId.trim();
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getModifiedId() {
        return modifiedId;
    }

    public void setModifiedId(String modifiedId) {
        this.modifiedId = modifiedId == null ? null : modifiedId.trim();
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }
}