package com.cips.dao;

import com.cips.model.OrderCert;

public interface OrderCertMapper {
    int deleteByPrimaryKey(String id);

    int insert(OrderCert record);

    int insertSelective(OrderCert record);

    OrderCert selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(OrderCert record);

    int updateByPrimaryKey(OrderCert record);
}