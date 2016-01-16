package com.cips.service;

import java.util.List;
import java.util.Map;

import com.cips.model.Poundage;
import com.cips.model.Rate;

public interface FeeService {
	public List<Rate> getRateList();
	public Rate getCurrentRate(Map<String,Object> paramMap) throws Exception;
	public Poundage getCurrPoundage(Integer status) throws Exception;
}