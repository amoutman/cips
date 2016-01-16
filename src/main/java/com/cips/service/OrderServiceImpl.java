package com.cips.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cips.dao.OrderMapper;
import com.cips.dao.OrderOperateMapper;
import com.cips.model.Order;
import com.cips.model.OrderOperate;

@Service("orderService")
public class OrderServiceImpl implements OrderService {
	
	private OrderMapper orderMapper;

	@Autowired
	public void setOrderMapper(OrderMapper orderMapper) {
		this.orderMapper = orderMapper;
	}
	
	private OrderOperateMapper orderOperateMapper;

	@Autowired
	public void setOrderOperateMapper(OrderOperateMapper orderOperateMapper) {
		this.orderOperateMapper = orderOperateMapper;
	}

	@Override
	public void saveOrder(Order order) throws Exception {
		orderMapper.insert(order);
	}

	@Override
	public Order getOrderById(String orderId) throws Exception {
		return orderMapper.selectByPrimaryKey(orderId);
	}

	@Override
	public void updateOrderById(Order order) throws Exception {
		orderMapper.updateByPrimaryKeySelective(order);
	}

	@Override
	public List<Order> toPageOrderListByParams(Map<String, Object> paramMap) {
		return orderMapper.toPageOrderListByParams(paramMap);
	}

	@Override
	public void saveOrderOperate(OrderOperate operate) throws Exception {
		orderOperateMapper.insertSelective(operate);
	}

}
