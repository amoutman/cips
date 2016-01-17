package com.cips.service;

import java.util.List;
import java.util.Map;

import com.cips.model.Poundage;
import com.cips.model.Rate;

public interface FeeService {
	public List<Rate> getRateList();
	
	public void insertRate(Rate rate);

	public void updateRateByStatus(Map<String,Object> map);
	
	public void insertPoundage(Poundage record);
	
    public Poundage selectPoundageByStatus();
    
    public void updatePoundageByStatus(Map<String,Object> map);
}
