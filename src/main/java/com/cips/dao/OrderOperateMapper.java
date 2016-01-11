package com.cips.dao;

import com.cips.model.OrderOperate;

public interface OrderOperateMapper {
    int deleteByPrimaryKey(String id);

    int insert(OrderOperate record);

    int insertSelective(OrderOperate record);

    OrderOperate selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(OrderOperate record);

    int updateByPrimaryKey(OrderOperate record);
}