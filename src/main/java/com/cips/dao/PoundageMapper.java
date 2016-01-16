package com.cips.dao;

import com.cips.model.Poundage;

public interface PoundageMapper {
    int deleteByPrimaryKey(String id);

    int insert(Poundage record);

    int insertSelective(Poundage record);

    Poundage selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Poundage record);

    int updateByPrimaryKey(Poundage record);
    
    Poundage getCurrPoundage(Integer status);
}