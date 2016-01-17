package com.cips.model;

import java.math.BigDecimal;
import java.util.Date;

public class Order {
    private String id;

    private String orderNo;

    private BigDecimal applyAmount;

    private BigDecimal payAmount;

    private Integer poundageRatio;

    private BigDecimal exchangeRateRmb;

    private BigDecimal exchangeRateUsd;

    private String applyId;

    private Date applyDate;

    private Integer status;

    private String modifiedId;

    private Date modifiedDate;

    private Date completedDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo == null ? null : orderNo.trim();
    }

    public BigDecimal getApplyAmount() {
        return applyAmount;
    }

    public void setApplyAmount(BigDecimal applyAmount) {
        this.applyAmount = applyAmount;
    }

    public BigDecimal getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(BigDecimal payAmount) {
        this.payAmount = payAmount;
    }

    public Integer getPoundageRatio() {
        return poundageRatio;
    }

    public void setPoundageRatio(Integer poundageRatio) {
        this.poundageRatio = poundageRatio;
    }

    public BigDecimal getExchangeRateRmb() {
        return exchangeRateRmb;
    }

    public void setExchangeRateRmb(BigDecimal exchangeRateRmb) {
        this.exchangeRateRmb = exchangeRateRmb;
    }

    public BigDecimal getExchangeRateUsd() {
        return exchangeRateUsd;
    }

    public void setExchangeRateUsd(BigDecimal exchangeRateUsd) {
        this.exchangeRateUsd = exchangeRateUsd;
    }

    public String getApplyId() {
        return applyId;
    }

    public void setApplyId(String applyId) {
        this.applyId = applyId == null ? null : applyId.trim();
    }

    public Date getApplyDate() {
        return applyDate;
    }

    public void setApplyDate(Date applyDate) {
        this.applyDate = applyDate;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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

    public Date getCompletedDate() {
        return completedDate;
    }

    public void setCompletedDate(Date completedDate) {
        this.completedDate = completedDate;
    }
}