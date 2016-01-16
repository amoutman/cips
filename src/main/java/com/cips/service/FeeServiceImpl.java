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
	public List<Rate> getRateList() {
		return rateMapper.getRateList();
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
