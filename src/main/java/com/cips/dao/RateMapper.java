package com.cips.dao;

import java.util.List;
import java.util.Map;

import com.cips.model.Rate;

public interface RateMapper {
    int deleteByPrimaryKey(String id);

    int insert(Rate record);

    int insertSelective(Rate record);

    Rate selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Rate record);

    int updateByPrimaryKey(Rate record);
    
    List<Rate> getRateList();
    
    Rate getCurrentRate(Map<String,Object> paramMap);

    List<Rate> toPageGetRateList();
    
    void updateRateByStatus(Map<String,Object> map);
}