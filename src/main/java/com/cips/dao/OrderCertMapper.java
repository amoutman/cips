package com.cips.dao;

import java.util.List;
import java.util.Map;

import com.cips.model.OrderCert;
import com.cips.model.TaskCert;

public interface OrderCertMapper {
    int deleteByPrimaryKey(String id);

    int insert(OrderCert record);

    int insertSelective(OrderCert record);

    OrderCert selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(OrderCert record);

    int updateByPrimaryKey(OrderCert record);
    
    int insertOrderCertList(List<OrderCert> ocList);
    
    int deleteOrderCertByParam(Map<String,Object> param);
    
    List<OrderCert> getOrderCertList(Map<String,Object> param);
    
    int updateOrderCertByParam(Map<String,Object> param);
    
    List<OrderCert> getOrderCertListByTaskCert(TaskCert taskCert);
    
    OrderCert selectOrderCertByParam(Map<String,Object> param);
    
}