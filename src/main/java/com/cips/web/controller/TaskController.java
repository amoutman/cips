package com.cips.web.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cips.constants.GlobalPara;
import com.cips.model.User;
import com.cips.service.TaskService;

@Controller
@RequestMapping("/task")
public class TaskController {

	final static Logger logger = LoggerFactory.getLogger(TaskController.class); 
	
	@Resource(name="taskService")
	private TaskService taskService;
	
	/**
	 * 待办管理
	 */
	@RequestMapping(value = "/toPageTaskMage",  method = RequestMethod.POST)
	public ModelAndView toPageTaskManagement(HttpServletRequest request){
		//获取客户用户名userId
		User user = (User) request.getSession().getAttribute(GlobalPara.USER_SESSION_TOKEN);
		//根据userId查询该用户所属角色
		
		//根据用户角色查询该角色所有未处理任务
		
		return null;
	}
	
	/**
	 * 待办处理
	 */
	@ResponseBody
	@RequestMapping(value = "/proTask",  method = RequestMethod.POST)
	public Object processingTask(@RequestParam("taskId")String taskId){
		
		//根据ID修改任务所属人为当前用户及修改该任务状态为处理中
		
		return null;
	}
}
