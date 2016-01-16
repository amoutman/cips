package com.cips.dao;

import java.util.Map;

import com.cips.model.Poundage;

public interface PoundageMapper {
    int deleteByPrimaryKey(String id);

    int insert(Poundage record);

    int insertSelective(Poundage record);

    Poundage selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Poundage record);

    int updateByPrimaryKey(Poundage record);
    
    Poundage getCurrPoundage(Integer status);
    
    Poundage selectPoundageByStatus();
    
    int updatePoundageByStatus(Map<String,Object> map);
}