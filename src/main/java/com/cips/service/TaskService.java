package com.cips.service;

import java.util.List;
import java.util.Map;

import com.cips.model.Order;
import com.cips.model.OrderDetails;
import com.cips.model.OrderOperate;
import com.cips.model.Task;

public interface TaskService {

	/**
	 * 根据角色ID及用户ID分页查询所有待办事项
	 */
	public List<Task> getTaskListByParams(Map<String, Object> params) throws Exception;
	
	/**
	 * 待办处理
	 */
	public String processingTaskById(String taskId, String userId) throws Exception;
	
	/**
	 * 初始化出一个新的待办任务
	 */
	public Task initNewTask(String orderId, Integer taskType) throws Exception;
	
	/**
	 * 根据ID查询待办
	 */
	public Task getTaskById(String taskId) throws Exception;
	
	/**
	 * 保存海外用户账户信息并更新待办状态及生成新的待办任务
	 */
	public void processTask(Order order, OrderDetails orderDetails,OrderOperate orderOperate, Task curTask, Task newTask) throws Exception;
	
	/**
	 * 根据参数查询任务
	 */
	public Task getTaskByParams(Map<String, Object> params) throws Exception;
	
	/**
	 * 保存待办
	 */
	public void saveNewTask(Task newTask) throws Exception;
}
