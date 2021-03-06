package com.cips.model;

import java.math.BigDecimal;
import java.util.Date;

public class Order {
    private String id;

    private String orderNo;

    private BigDecimal applyAmount;

    private BigDecimal payAmount;

    private BigDecimal poundageRatio;

    private BigDecimal exchangeRateRmb;

    private BigDecimal exchangeRateUsd;

    private String applyId;

    private Date applyDate;
    
    private String applyBDate;
    
    private String applyEDate;

    private Integer status;
    
    private String statusDesc;

	private String modifiedId;

    private Date modifiedDate;

    private Date completedDate;

    private String hwUserId;
    
    private BigDecimal matchAmount;
    
    private String matchPercent;
    
    private BigDecimal hcApplyAmount;
    
    private BigDecimal hcPayAmount;
    
    private Integer isMatch; //是否100%匹配

    public Integer getIsMatch() {
		return isMatch;
	}

	public void setIsMatch(Integer isMatch) {
		this.isMatch = isMatch;
	}

	public BigDecimal getHcApplyAmount() {
		return hcApplyAmount;
	}

	public void setHcApplyAmount(BigDecimal hcApplyAmount) {
		this.hcApplyAmount = hcApplyAmount;
	}

	public BigDecimal getMatchAmount() {
		return matchAmount;
	}

	public void setMatchAmount(BigDecimal matchAmount) {
		this.matchAmount = matchAmount;
	}

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

    public BigDecimal getPoundageRatio() {
        return poundageRatio;
    }

    public void setPoundageRatio(BigDecimal poundageRatio) {
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

    public String getHwUserId() {
        return hwUserId;
    }

    public void setHwUserId(String hwUserId) {
        this.hwUserId = hwUserId == null ? null : hwUserId.trim();
    }
    
    public String getStatusDesc() {
		return statusDesc;
	}

	public void setStatusDesc(String statusDesc) {
		this.statusDesc = statusDesc;
	}

	public String getApplyBDate() {
		return applyBDate;
	}

	public void setApplyBDate(String applyBDate) {
		this.applyBDate = applyBDate;
	}

	public String getApplyEDate() {
		return applyEDate;
	}

	public void setApplyEDate(String applyEDate) {
		this.applyEDate = applyEDate;
	}

	public String getMatchPercent() {
		return matchPercent;
	}

	public void setMatchPercent(String matchPercent) {
		this.matchPercent = matchPercent;
	}

	public BigDecimal getHcPayAmount() {
		return hcPayAmount;
	}

	public void setHcPayAmount(BigDecimal hcPayAmount) {
		this.hcPayAmount = hcPayAmount;
	}
}