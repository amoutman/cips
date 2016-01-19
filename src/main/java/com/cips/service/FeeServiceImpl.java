package com.cips.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cips.dao.PoundageMapper;
import com.cips.dao.RateMapper;
import com.cips.model.Poundage;
import com.cips.model.Rate;

@Service("feeService")
public class FeeServiceImpl implements FeeService {

	private RateMapper rateMapper;
	
	@Autowired
	public void setRateMapper(RateMapper rateMapper) {
		this.rateMapper = rateMapper;
	}
	
	private PoundageMapper poundageMapper;

	@Autowired
	public void setPoundageMapper(PoundageMapper poundageMapper) {
		this.poundageMapper = poundageMapper;
	}

	@Override
	public List<Rate> getRateList(Map<String,Object> paramMap) {
		return rateMapper.toPageGetRateList(paramMap);
	}
	
	public void insertRate(Rate rate){
		rateMapper.insertSelective(rate);
	}

	public void updateRateByStatus(Map<String,Object> map){
		rateMapper.updateRateByStatus(map);
	}
	
	public void insertPoundage(Poundage record){
		poundageMapper.insertSelective(record);
	}
	
	public Poundage selectPoundageByStatus(){
		return poundageMapper.selectPoundageByStatus();
	}
    
    public void updatePoundageByStatus(Map<String,Object> map){
    	poundageMapper.updatePoundageByStatus(map);
    }

	@Override
	public Rate getCurrentRate(Map<String,Object> paramMap) throws Exception {
		return rateMapper.getCurrentRate(paramMap);
	}

	@Override
	public Poundage getCurrPoundage(Integer status) throws Exception {
		return poundageMapper.getCurrPoundage(status);
	}

}
