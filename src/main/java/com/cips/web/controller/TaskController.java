package com.cips.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cips.constants.BusConstants;
import com.cips.constants.GlobalPara;
import com.cips.model.Role;
import com.cips.model.Task;
import com.cips.model.User;
import com.cips.service.RoleService;
import com.cips.service.TaskService;

@Controller
@RequestMapping("/task")
public class TaskController {

	final static Logger logger = LoggerFactory.getLogger(TaskController.class); 
	
	@Resource(name="taskService")
	private TaskService taskService;
	
	@Resource(name="roleService")
	private RoleService roleService;
	
	/**
	 * 待办管理
	 */
	@RequestMapping(value = "/toPageTaskMage")
	public ModelAndView toPageTaskManagement(HttpServletRequest request){
		try {
			ModelAndView mv = new ModelAndView();
			//获取客户用户名userId
			User user = (User) request.getSession().getAttribute(GlobalPara.USER_SESSION_TOKEN);
			//根据userId查询该用户所属角色
			List<Role> roles = roleService.getRoleListByUserId(user.getId());
			List<String> roleIds = new ArrayList<String>();
			for (Role role : roles) {
				roleIds.add(role.getId());
			}
			//根据用户角色查询该角色所有未处理任务
			List<Task> tasks = taskService.getTaskListByParams(roleIds, user.getId(), BusConstants.TASK_STATUS_NOT_PROCESS);
			mv.addObject("tasks", tasks);
			mv.setViewName("task/proTaskMage");
			return mv;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("待办管理模块异常!");
		}
	}
	
	/**
	 * 待办处理
	 */
	@ResponseBody
	@RequestMapping(value = "/proTask")
	public Object processingTask(HttpServletRequest request, @RequestParam("taskId")String taskId){
		Map<String,Object> map = new HashMap<String,Object>();
		try {
			//获取客户用户名userId
			User user = (User) request.getSession().getAttribute(GlobalPara.USER_SESSION_TOKEN);
			//根据ID修改任务所属人为当前用户及修改该任务状态为处理中
			String msg = taskService.processingTaskById(taskId, user.getId());
			if(msg != null){
				map.put(GlobalPara.AJAX_KEY, msg);
			}else{
				map.put(GlobalPara.AJAX_KEY, GlobalPara.AJAX_SUCCESS);
			}
			return map;
		} catch (Exception e) {
			e.printStackTrace();
			map = new HashMap<String,Object>();
			map.put(GlobalPara.AJAX_KEY, "待办处理异常，请重试！");
			return map;
		}
	}
	
	/**
	 * 已办管理
	 */
	@RequestMapping(value = "/toPageProedTasksMage")
	public ModelAndView toPageProcessedTasksMage(HttpServletRequest request, Task task){
		try {
			ModelAndView mv = new ModelAndView();
			//获取客户用户名userId
			User user = (User) request.getSession().getAttribute(GlobalPara.USER_SESSION_TOKEN);
			//根据userId查询该用户所属角色
			List<Role> roles = roleService.getRoleListByUserId(user.getId());
			List<String> roleIds = new ArrayList<String>();
			for (Role role : roles) {
				roleIds.add(role.getId());
			}
			//根据用户角色查询该角色所有未处理任务
			List<Task> tasks = taskService.getTaskListByParams(roleIds, user.getId(), BusConstants.TASK_STATUS_PROCESSED);
			mv.addObject("tasks", tasks);
			mv.setViewName("task/proTaskMage");
			return mv;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("已办管理模块异常!");
		}
	}
}
