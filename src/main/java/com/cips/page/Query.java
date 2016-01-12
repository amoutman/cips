package com.cips.page;

import java.util.Map;

public class Query {
	private Map<String, Object> queryParams;
	private Map<String, Object> orderParams;
	public Map<String, Object> getOrderParams() {
		return orderParams;
	}

	public void setOrderParams(Map<String, Object> orderParams) {
		this.orderParams = orderParams;
	}

	private Pager pager;  
	
	public Map<String, Object> getQueryParams() {  
	      return queryParams;  
	}  
		  
	public void setQueryParams(Map<String, Object> queryParams) {  
	      this.queryParams = queryParams;  
	}

	public Pager getPager() {
		return pager;
	}

	public void setPager(Pager pager) {
		this.pager = pager;
	} 

}
