package com.cips.service;

import java.util.List;
import java.util.Map;

import com.cips.model.Amount;
import com.cips.model.Order;
import com.cips.model.OrderDetails;
import com.cips.model.OrderOperate;
import com.cips.model.Task;

public interface OrderService {

	public void createOrder(Order order, OrderDetails orderDetails, OrderOperate operate, Task task, Amount amount) throws Exception;
	public void saveOrder(Order order) throws Exception;
	public Order getOrderById(String orderId) throws Exception;
	public void updateOrderById(Order order) throws Exception;
	public List<Order> toPageOrderListByParams(Map<String,Object> paramMap);
	public void saveOrderOperate(OrderOperate operate) throws Exception;
	
	public OrderDetails getOrderDetailsByParams(Map<String,Object> paramMap);
	public void updateOrderDetails(OrderDetails orderDetails);
	public void insertOrderDetails(OrderDetails orderDetails);
	public Order getOrderByOrderNo(String orderNo) throws Exception;
	public OrderDetails getOrderDetailsById(String orderDetailsId);
	public void updateHcApplyAmount(Order order, Amount amount)throws Exception;
	public Amount selectMatchAmountByOrderId(String orderId);
	public void updateMatchAmountByOrderId(Amount amount);
	public Order selectOrderByOrderId(String orderId);
	public List<Order> toPageMatchOrderListByParams(Map<String,Object> paramMap);
}
