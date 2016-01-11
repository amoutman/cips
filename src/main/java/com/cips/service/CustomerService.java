package com.cips.service;

import java.util.List;

import com.cips.model.Task;

public interface CustomerService {

	/**
	 * 分页查询所有待办事项
	 */
	public List<Task> getTaskListByCustomerId(String customerId);
	
	/**
	 * 待办处理
	 */
	public void viewTask(String taskId);
	
	/**
	 * 待办实际处理
	 */
	//public void processingTask(String taskId);
}
