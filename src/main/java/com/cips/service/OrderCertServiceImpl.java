package com.cips.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cips.dao.OrderCertMapper;
import com.cips.dao.TaskCertMapper;
import com.cips.model.OrderCert;
import com.cips.model.TaskCert;

@Service("orderCertService")
public class OrderCertServiceImpl implements OrderCertService {

	private OrderCertMapper orderCertMapper;
	
	@Autowired
	public void setOrderCertMapper(OrderCertMapper orderCertMapper) {
		this.orderCertMapper = orderCertMapper;
	}
	
	private TaskCertMapper taskCertMapper;

	@Autowired
	public void setTaskCertMapper(TaskCertMapper taskCertMapper) {
		this.taskCertMapper = taskCertMapper;
	}

	@Override
	public void insertOrderCertList(List<OrderCert> ocList) {
		// TODO Auto-generated method stub
		orderCertMapper.insertOrderCertList(ocList);
	}

	@Override
	public void deleteOrderCertByParam(Map<String, Object> param) {
		// TODO Auto-generated method stub
		orderCertMapper.deleteOrderCertByParam(param);
	}

	@Override
	public List<OrderCert> getOrderCertList(Map<String, Object> param) {
		// TODO Auto-generated method stub
		return orderCertMapper.getOrderCertList(param);
	}
	
	public void updateOrderCertByParam(Map<String,Object> param){
		orderCertMapper.updateOrderCertByParam(param);
	}

	@Override
	public void insertTaskCert(TaskCert taskCert) {
		taskCertMapper.insertSelective(taskCert);
	}

}
