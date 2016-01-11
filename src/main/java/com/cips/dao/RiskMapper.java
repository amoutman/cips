package com.cips.dao;

import com.cips.model.Risk;

public interface RiskMapper {
    int deleteByPrimaryKey(String id);

    int insert(Risk record);

    int insertSelective(Risk record);

    Risk selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Risk record);

    int updateByPrimaryKey(Risk record);
}