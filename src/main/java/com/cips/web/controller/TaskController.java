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
import com.cips.constants.EnumConstants.OrderStsEnum;
import com.cips.constants.GlobalPara;
import com.cips.model.Order;
import com.cips.model.OrderDetails;
import com.cips.model.Role;
import com.cips.model.Task;
import com.cips.model.User;
import com.cips.page.Pager;
import com.cips.service.OrderService;
import com.cips.service.RoleService;
import com.cips.service.TaskService;
import com.cips.service.UserService;

@Controller
@RequestMapping("/task")
public class TaskController {

	final static Logger logger = LoggerFactory.getLogger(TaskController.class); 
	
	@Resource(name="taskService")
	private TaskService taskService;
	
	@Resource(name="roleService")
	private RoleService roleService;
	
	@Resource(name="userService")
	private UserService userService;
	
	@Resource(name="orderService")
	private OrderService orderService;
	
	/**
	 * 待办管理
	 */
	@RequestMapping(value = "/toPageTaskMage")
	public ModelAndView toPageTaskManagement(HttpServletRequest request){
		try {
			//分页条件
			Pager pager = (Pager)request.getAttribute(GlobalPara.PAGER_SESSION);
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
			Map<String,Object> params = new HashMap<String,Object>();
			params.put(GlobalPara.PAGER_SESSION, pager);
	        params.put("status", BusConstants.TASK_STATUS_NOT_PROCESS);
	        params.put("userId", user.getId());
	        params.put("roleIds", roleIds);
	        
			List<Task> tasks = taskService.getTaskListByParams(params);
			
			mv.addObject("pager", pager);
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
	@RequestMapping(value = "/processingTask")
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
	
	@RequestMapping(value = "/preProTask")
	public ModelAndView prepareProTask(HttpServletRequest request, @RequestParam("taskId")String taskId){
		try {
			ModelAndView mv = new ModelAndView();
			//查询当前要处理的待办
			Task task = taskService.getTaskById(taskId);
			//根据类型选择视图及参数
			switch (task.getTaskType()) {
			case 1:
				//查询订单信息
				Order order = orderService.getOrderById(task.getOrderId());
				order.setStatusDesc(OrderStsEnum.getNameByCode(order.getStatus().toString()));
				User user = userService.getUserByUserId(order.getApplyId());
				//获取海外账户信息
				Map<String,Object> paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", task.getOrderId());
				paramMap.put("taskType", task.getTaskType());
				OrderDetails hwAcc = orderService.getOrderDetailsByParams(paramMap);
				mv.addObject("order", order);
				mv.addObject("user", user);
				mv.addObject("hwAcc", hwAcc);
				mv.setViewName("task/plpProTaskT2");
				break;
			case 2:

				break;
			case 3:

				break;
			case 4:

				break;
			case 5:
				
				break;
			case 6:

				break;
			case 7:

				break;
			case 8:

				break;
			case 9:

				break;
			case 10:

				break;
			case 11:

				break;
			case 12:

				break;
			case 13:

				break;
			case 14:

				break;
			case 15:

				break;
			case 16:

				break;
			case 17:

				break;
			case 18:

				break;
			case 19:

				break;
			case 20:
	
				break;
			case 21:

				break;
			case 22:

				break;
			case 23:

				break;
			case 24:

				break;
			case 25:

				break;
			case 26:

				break;
			case 27:

				break;
			case 28:

				break;
			case 29:

				break;
			case 30:

				break;
			case 31:

				break;
			case 32:

				break;
			case 33:

				break;
			case 34:

				break;
			case 35:

				break;
			case 36:

				break;
			case 37:

				break;
			case 38:

				break;
			case 39:

				break;
			case 40:

				break;
			case 41:

				break;
			case 42:

				break;
			case 43:

				break;
			case 44:

				break;
			case 45:

				break;
			case 46:

				break;
			case 47:

				break;
			case 48:

				break;
			case 49:

				break;
			case 50:

				break;
			case 51:

				break;
			}
			
			return mv;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("待办处理页面异常!");
		}
	}
	
	@ResponseBody
	@RequestMapping(value = "/proTaskUpload")
	public Object processTaskUpload(HttpServletRequest request, @RequestParam("taskId")String taskId){
		Map<String,Object> map = new HashMap<String,Object>();
		try {
			//查询当前要处理的待办
			Task task = taskService.getTaskById(taskId);
			
			switch (task.getTaskType()) {
			case 4:

				break;
			case 5:
				
				break;
			case 6:

				break;
			case 7:

				break;
			case 8:

				break;
			case 9:

				break;
			case 10:

				break;
			case 11:

				break;
			case 12:

				break;
			case 13:

				break;
			case 14:

				break;
			case 15:

				break;
			case 16:

				break;
			case 17:

				break;
			case 18:

				break;
			case 19:

				break;
			case 20:
	
				break;
			case 21:

				break;
			case 22:

				break;
			case 23:

				break;
			case 24:

				break;
			case 25:

				break;
			case 26:

				break;
			case 27:

				break;
			case 28:

				break;
			case 29:

				break;
			case 30:

				break;
			case 31:

				break;
			case 32:

				break;
			case 33:

				break;
			case 34:

				break;
			case 35:

				break;
			case 36:

				break;
			case 37:

				break;
			case 38:

				break;
			case 39:

				break;
			case 40:

				break;
			case 41:

				break;
			case 42:

				break;
			case 43:

				break;
			case 44:

				break;
			case 45:

				break;
			case 46:

				break;
			case 47:

				break;
			case 48:

				break;
			case 49:

				break;
			case 50:

				break;
			case 51:

				break;
			}
			return map;
		} catch (Exception e) {
			e.printStackTrace();
			map = new HashMap<String,Object>();
			map.put(GlobalPara.AJAX_KEY, "待办处理异常，请重试！");
			return map;
		}
	}
	
	@ResponseBody
	@RequestMapping(value = "/proTaskStatus")
	public Object processTaskStatus(HttpServletRequest request, @RequestParam("taskId")String taskId){
		Map<String,Object> map = new HashMap<String,Object>();
		try {
			//查询当前要处理的待办
			Task task = taskService.getTaskById(taskId);
			
			switch (task.getTaskType()) {
			case 2:

				break;
			case 3:

				break;
			case 4:

				break;
			case 5:
				
				break;
			case 6:

				break;
			case 7:

				break;
			case 8:

				break;
			case 9:

				break;
			case 10:

				break;
			case 11:

				break;
			case 12:

				break;
			case 13:

				break;
			case 14:

				break;
			case 15:

				break;
			case 16:

				break;
			case 17:

				break;
			case 18:

				break;
			case 19:

				break;
			case 20:
	
				break;
			case 21:

				break;
			case 22:

				break;
			case 23:

				break;
			case 24:

				break;
			case 25:

				break;
			case 26:

				break;
			case 27:

				break;
			case 28:

				break;
			case 29:

				break;
			case 30:

				break;
			case 31:

				break;
			case 32:

				break;
			case 33:

				break;
			case 34:

				break;
			case 35:

				break;
			case 36:

				break;
			case 37:

				break;
			case 38:

				break;
			case 39:

				break;
			case 40:

				break;
			case 41:

				break;
			case 42:

				break;
			case 43:

				break;
			case 44:

				break;
			case 45:

				break;
			case 46:

				break;
			case 47:

				break;
			case 48:

				break;
			case 49:

				break;
			case 50:

				break;
			case 51:

				break;
			}
			return map;
		} catch (Exception e) {
			e.printStackTrace();
			map = new HashMap<String,Object>();
			map.put(GlobalPara.AJAX_KEY, "待办处理异常，请重试！");
			return map;
		}
	}
	
	@ResponseBody
	@RequestMapping(value = "/proTaskAcc")
	public Object processTaskAccount(HttpServletRequest request, @RequestParam("taskId")String taskId){
		Map<String,Object> map = new HashMap<String,Object>();
		try {
			//查询当前要处理的待办
			Task task = taskService.getTaskById(taskId);
			
			switch (task.getTaskType()) {
			case 1:
				
				break;
			case 2:

				break;
			case 3:

				break;
			case 4:

				break;
			case 5:
				
				break;
			case 6:

				break;
			case 7:

				break;
			case 8:

				break;
			case 9:

				break;
			case 10:

				break;
			case 11:

				break;
			case 12:

				break;
			case 13:

				break;
			case 14:

				break;
			case 15:

				break;
			case 16:

				break;
			case 17:

				break;
			case 18:

				break;
			case 19:

				break;
			case 20:
	
				break;
			case 21:

				break;
			case 22:

				break;
			case 23:

				break;
			case 24:

				break;
			case 25:

				break;
			case 26:

				break;
			case 27:

				break;
			case 28:

				break;
			case 29:

				break;
			case 30:

				break;
			case 31:

				break;
			case 32:

				break;
			case 33:

				break;
			case 34:

				break;
			case 35:

				break;
			case 36:

				break;
			case 37:

				break;
			case 38:

				break;
			case 39:

				break;
			case 40:

				break;
			case 41:

				break;
			case 42:

				break;
			case 43:

				break;
			case 44:

				break;
			case 45:

				break;
			case 46:

				break;
			case 47:

				break;
			case 48:

				break;
			case 49:

				break;
			case 50:

				break;
			case 51:

				break;
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
			//分页条件
			Pager pager = (Pager)request.getAttribute(GlobalPara.PAGER_SESSION);
			//获取客户用户名userId
			User user = (User) request.getSession().getAttribute(GlobalPara.USER_SESSION_TOKEN);
			//根据userId查询该用户所属角色
			List<Role> roles = roleService.getRoleListByUserId(user.getId());
			List<String> roleIds = new ArrayList<String>();
			for (Role role : roles) {
				roleIds.add(role.getId());
			}
			//根据用户角色查询该角色所有未处理任务
			Map<String,Object> params = new HashMap<String,Object>();
			params.put(GlobalPara.PAGER_SESSION, pager);
	        params.put("status", BusConstants.TASK_STATUS_PROCESSED);
	        params.put("userId", user.getId());
	        params.put("roleIds", roleIds);
	        
			List<Task> tasks = taskService.getTaskListByParams(params);
			
			mv.addObject("pager", pager);
			mv.addObject("tasks", tasks);
			mv.setViewName("task/proTaskMage");
			return mv;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("已办管理模块异常!");
		}
	}
}
