package com.cips.model;

import java.math.BigDecimal;
import java.util.Date;

public class Rate {
    private String id;

    private Integer type;

    private Integer status;

    private BigDecimal rateHigh;

    private BigDecimal rateLow;

    private BigDecimal rateDiff;

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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public BigDecimal getRateHigh() {
        return rateHigh;
    }

    public void setRateHigh(BigDecimal rateHigh) {
        this.rateHigh = rateHigh;
    }

    public BigDecimal getRateLow() {
        return rateLow;
    }

    public void setRateLow(BigDecimal rateLow) {
        this.rateLow = rateLow;
    }

    public BigDecimal getRateDiff() {
        return rateDiff;
    }

    public void setRateDiff(BigDecimal rateDiff) {
        this.rateDiff = rateDiff;
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