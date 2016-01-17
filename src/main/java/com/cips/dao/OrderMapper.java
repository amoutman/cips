package com.cips.dao;

import java.util.List;
import java.util.Map;

import com.cips.model.Order;

public interface OrderMapper {
    int deleteByPrimaryKey(String id);

    int insert(Order record);

    int insertSelective(Order record);

    Order selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Order record);

    int updateByPrimaryKey(Order record);
    
    List<Order> toPageOrderListByParams(Map<String,Object> paramMap);
}