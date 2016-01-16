package com.cips.service;

import java.util.List;

import com.cips.model.Task;

public interface TaskService {

	/**
	 * 根据角色ID及用户ID分页查询所有待办事项
	 */
	public List<Task> getTaskListByParams(List<String> roleIds, String userId, Integer status) throws Exception;
	
	/**
	 * 待办处理
	 */
	public String processingTaskById(String taskId, String userId) throws Exception;
	
	/**
	 * 待办实际处理
	 */
	public void processedTaskById(String taskId) throws Exception;
	
	/**
	 * 新增待办
	 */
	public void insertTask(Task task) throws Exception;
}
