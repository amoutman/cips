package com.cips.service;

import java.util.List;
import java.util.Map;

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
	
}
