package com.cips.dao;

import java.util.Map;

import com.cips.model.OrderDetails;

public interface OrderDetailsMapper {
    int deleteByPrimaryKey(String id);

    int insert(OrderDetails record);

    int insertSelective(OrderDetails record);

    OrderDetails selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(OrderDetails record);

    int updateByPrimaryKey(OrderDetails record);
    
    OrderDetails getOrderDetailsByParams(Map<String, Object> paramMap);
}