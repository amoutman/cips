package com.cips.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cips.dao.RateMapper;
import com.cips.model.Rate;

@Service("feeService")
public class FeeServiceImpl implements FeeService {

	private RateMapper rateMapper;
	
	@Autowired
	public void setRateMapper(RateMapper rateMapper) {
		this.rateMapper = rateMapper;
	}

	@Override
	public List<Rate> getRateList() {
		// TODO Auto-generated method stub
		return rateMapper.getRateList();
	}

}
