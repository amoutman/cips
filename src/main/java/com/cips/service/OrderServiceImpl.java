package com.cips.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cips.dao.OrderMapper;
import com.cips.dao.OrderOperateMapper;
import com.cips.dao.TaskMapper;
import com.cips.model.Order;
import com.cips.model.OrderOperate;
import com.cips.model.Task;

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
	
	private TaskMapper taskMapper;

	@Autowired
	public void setTaskMapper(TaskMapper taskMapper) {
		this.taskMapper = taskMapper;
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

	@Override
	public void createOrder(Order order, OrderOperate operate, Task task) throws Exception {
		//订单保存
		orderMapper.insert(order);
		//操作日志保存
		orderOperateMapper.insert(operate);
		//生成新的待办任务
		taskMapper.insert(task);
		
	}

}
