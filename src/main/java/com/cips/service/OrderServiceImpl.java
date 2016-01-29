package com.cips.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cips.dao.AmountMapper;
import com.cips.dao.OrderDetailsMapper;
import com.cips.dao.OrderMapper;
import com.cips.dao.OrderOperateMapper;
import com.cips.dao.TaskMapper;
import com.cips.model.Amount;
import com.cips.model.Order;
import com.cips.model.OrderDetails;
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
	
	private OrderDetailsMapper orderDetailsMapper;

	@Autowired
	public void setOrderDetailsMapper(OrderDetailsMapper orderDetailsMapper) {
		this.orderDetailsMapper = orderDetailsMapper;
	}
	
	private AmountMapper amountMapper;

	@Autowired
	public void setAmountMapper(AmountMapper amountMapper) {
		this.amountMapper = amountMapper;
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
	public void createOrder(Order order, OrderDetails orderDetails, OrderOperate operate, Task task) throws Exception {
		//订单保存
		orderMapper.insert(order);
		//订单账户
		orderDetailsMapper.insertSelective(orderDetails);
		//操作日志保存
		orderOperateMapper.insert(operate);
		//生成新的待办任务
		taskMapper.insert(task);
		
	}

	@Override
	public OrderDetails getOrderDetailsByParams(Map<String, Object> paramMap) {
		return orderDetailsMapper.getOrderDetailsByParams(paramMap);
	}

	@Override
	public Order getOrderByOrderNo(String orderNo) throws Exception {
		return orderMapper.getOrderByOrderNo(orderNo);
	}

	@Override
	public OrderDetails getOrderDetailsById(String orderDetailsId) {
		return orderDetailsMapper.selectByPrimaryKey(orderDetailsId);
	}

	@Override
	public void updateOrderDetails(OrderDetails orderDetails) {
		orderDetailsMapper.updateByPrimaryKeySelective(orderDetails);		
	}

	@Override
	public void insertOrderDetails(OrderDetails orderDetails) {
		orderDetailsMapper.insertSelective(orderDetails);		
	}

	public Amount selectMatchAmountByOrderId(String orderId){
		return amountMapper.selectMatchAmountByOrderId(orderId);
	}
	
	public void updateMatchAmountByOrderId(Amount amount){
		amountMapper.updateByPrimaryKeySelective(amount);
	}
}
