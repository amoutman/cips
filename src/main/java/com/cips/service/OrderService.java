package com.cips.service;

import java.util.List;
import java.util.Map;

import com.cips.model.Order;
import com.cips.model.OrderOperate;
import com.cips.model.Task;

public interface OrderService {

	public void createOrder(Order order, OrderOperate operate, Task task) throws Exception;
	public void saveOrder(Order order) throws Exception;
	public Order getOrderById(String orderId) throws Exception;
	public void updateOrderById(Order order) throws Exception;
	public List<Order> toPageOrderListByParams(Map<String,Object> paramMap);
	public void saveOrderOperate(OrderOperate operate) throws Exception;
	
}
