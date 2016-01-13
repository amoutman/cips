package com.cips.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cips.dao.TaskMapper;
import com.cips.model.Task;

@Service("taskService")
public class TaskServiceImpl implements TaskService {
	
	private TaskMapper taskMapper;
	
	public TaskMapper getTaskMapper() {
		return taskMapper;
	}

	@Autowired
	public void setTaskMapper(TaskMapper taskMapper) {
		this.taskMapper = taskMapper;
	}

	@Override
	public synchronized String processingTaskById(String taskId) {
		//根据ID查询任务状态 如果为未处理则修改状态 否则直接返回
		
		//如果状态为未处理 则为任务分配当前用户为处理人 并修改状态为处理中
		
		return null;
	}

	@Override
	public void processedTaskById(String taskId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Task> getTaskListByParams(List<String> roleIds, String userId, Integer status) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>(2);
        params.put("status", status);
        params.put("userId", userId);
        params.put("roleIds", roleIds);
		return taskMapper.toPageTaskListByParams(params);
	}

}
