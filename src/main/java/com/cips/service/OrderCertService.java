package com.cips.service;

import java.util.List;
import java.util.Map;

import com.cips.model.OrderCert;
import com.cips.model.TaskCert;

public interface OrderCertService {
	
	void insertOrderCertList(List<OrderCert> ocList);
    
	void deleteOrderCertByParam(Map<String,Object> param);
    
    List<OrderCert> getOrderCertList(Map<String,Object> param);
    
    void updateOrderCertByParam(Map<String,Object> param);
    
    void insertTaskCert(TaskCert taskCert);
    
    OrderCert selectOrderCertByParam(Map<String,Object> param);
    
    void deleteTaskCertByCertId(Map<String,Object> param);
    
}
