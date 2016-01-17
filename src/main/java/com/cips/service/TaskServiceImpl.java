package com.cips.service;

import java.util.Date;
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
	public synchronized String processingTaskById(String taskId, String userId) {
		//用来返回处理信息
		String msg = null;
		//根据ID查询任务状态 如果为未处理则修改状态及为任务分配当前用户为处理人 否则直接返回
		Task task = taskMapper.selectByPrimaryKey(taskId);
		if(task.getOperatedId() != null){
			task.setOperatedId(userId);
			task.setBeginTime(new Date());
			taskMapper.updateByPrimaryKey(task);
		}else{
			msg = "该待办任务已由其他人进行处理！";
		}
		return msg;
	}

	@Override
	public void processedTaskById(String taskId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Task> getTaskListByParams(List<String> roleIds, String userId, Integer status) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
        params.put("status", status);
        params.put("userId", userId);
        params.put("roleIds", roleIds);
		return taskMapper.toPageTaskListByParams(params);
	}

	@Override
	public void insertTask(Task task) throws Exception {
		taskMapper.insertSelective(task);
	}

}
