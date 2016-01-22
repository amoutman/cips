package com.cips.model;

import java.util.Date;

public class Task {
    private String id;

    private String orderId;

    private String roleId;

    private Integer orderStatus;

    private Integer taskType;

    private Date beginTime;

    private Date endTime;

    private Integer status;

    private String operatedId;

    private String remark;
    
	private String msg;
	
	private String orderNo;
	
	private String proTaskBTime;
	
	private String proTaskETime;

    public String getProTaskBTime() {
		return proTaskBTime;
	}

	public void setProTaskBTime(String proTaskBTime) {
		this.proTaskBTime = proTaskBTime;
	}

	public String getProTaskETime() {
		return proTaskETime;
	}

	public void setProTaskETime(String proTaskETime) {
		this.proTaskETime = proTaskETime;
	}

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

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId == null ? null : roleId.trim();
    }

    public Integer getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Integer getTaskType() {
        return taskType;
    }

    public void setTaskType(Integer taskType) {
        this.taskType = taskType;
    }

    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }
    
    public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
}