package com.cips.web.controller;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.cfg.PkDrivenByDefaultMapsIdSecondPass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.cips.constants.BusConstants;
import com.cips.constants.EnumConstants.OrderStsEnum;
import com.cips.constants.GlobalPara;
import com.cips.model.AccountFr;
import com.cips.model.Order;
import com.cips.model.OrderCert;
import com.cips.model.OrderDetails;
import com.cips.model.OrderOperate;
import com.cips.model.Rate;
import com.cips.model.Role;
import com.cips.model.Task;
import com.cips.model.User;
import com.cips.page.Pager;
import com.cips.service.AccountFrService;
import com.cips.service.FeeService;
import com.cips.service.OrderCertService;
import com.cips.service.OrderService;
import com.cips.service.RoleService;
import com.cips.service.TaskService;
import com.cips.service.UserService;
import com.cips.util.CipsFileUtils;
import com.cips.util.DownloadUtil;
import com.cips.util.PKIDUtils;

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
	
	@Resource(name="accountFrService")
	private AccountFrService accountFrService;
	
	@Resource(name="orderCertService")
	private OrderCertService orderCertService;
	
	@Resource(name="feeService")
	private FeeService feeService;
	
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
			mv.addObject("size", tasks.size());
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
			String msg = taskService.processingTaskById(taskId, user.getId(), BusConstants.TASK_PRO_TYPE_PROCESSING);
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
			//查询订单信息
			Order order = orderService.getOrderById(task.getOrderId());
			order.setStatusDesc(OrderStsEnum.getNameByCode(order.getStatus().toString()));
			User user = userService.getUserByUserId(order.getApplyId());
			
			OrderDetails hwAcc = null;
			OrderDetails hwUserAcc = null;
			OrderDetails hcAccT3 = null;
			OrderDetails hcAccT4 = null;
			Rate curRToURate = null;
			Map<String,Object> paramMap =  null;
			//凭证信息
			List<OrderCert> ocList = null;
			switch (task.getTaskType()) {
			case 1:
				//获取海外账户信息
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", task.getOrderId());
				paramMap.put("type", BusConstants.ORDERDETAILS_TYPE_CUSTOMER_HWACC);
				hwAcc = orderService.getOrderDetailsByParams(paramMap);
				mv.addObject("order", order);
				mv.addObject("user", user);
				mv.addObject("hwAcc", hwAcc);
				mv.addObject("task", task);
				mv.setViewName("task/plpProTaskT1");
				break;
			case 2:
				//获取海外账户信息
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", task.getOrderId());
				paramMap.put("type", BusConstants.ORDERDETAILS_TYPE_CUSTOMER_HWACC);
				hwAcc = orderService.getOrderDetailsByParams(paramMap);
				
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", task.getOrderId());
				paramMap.put("type", BusConstants.ORDERDETAILS_TYPE_HWUSER_LOCACC);
				hwUserAcc = orderService.getOrderDetailsByParams(paramMap);
				
				mv.addObject("order", order);
				mv.addObject("user", user);
				mv.addObject("hwAcc", hwAcc);
				mv.addObject("hwUserAcc", hwUserAcc);
				mv.addObject("task", task);
				mv.setViewName("task/plcProTaskT2");
				break;
			case 3:
				//获取海外账户信息
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", task.getOrderId());
				paramMap.put("type", BusConstants.ORDERDETAILS_TYPE_CUSTOMER_HWACC);
				hwAcc = orderService.getOrderDetailsByParams(paramMap);
				//操作员选择的海外用户
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", task.getOrderId());
				paramMap.put("type", BusConstants.ORDERDETAILS_TYPE_HWUSER_LOCACC);
				hwUserAcc = orderService.getOrderDetailsByParams(paramMap);
				
				mv.addObject("order", order);
				mv.addObject("user", user);
				mv.addObject("hwAcc", hwAcc);
				mv.addObject("hwUserAcc", hwUserAcc);
				mv.addObject("task", task);
				mv.setViewName("task/plpProTaskT1");
				break;
			case 4:
				//获取海外用户账户信息 华创上传打款凭证
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", task.getOrderId());
				paramMap.put("type", BusConstants.ORDERDETAILS_TYPE_HWUSER_LOCACC);
				hwUserAcc = orderService.getOrderDetailsByParams(paramMap);
				
				mv.addObject("accInfo", hwUserAcc);
				mv.addObject("payMoney", order.getPayAmount().add(new BigDecimal(50000))+"￥");
				mv.addObject("task", task);
				mv.setViewName("task/hcFirstPayTask");
				break;
			case 5:
				//海外用户账户信息
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", task.getOrderId());
				paramMap.put("type", BusConstants.ORDERDETAILS_TYPE_HWUSER_LOCACC);
				hwUserAcc = orderService.getOrderDetailsByParams(paramMap);
				//查询上传图片信息
				paramMap.put("taskType", 4);
				ocList = orderCertService.getOrderCertList(paramMap);
	
				//查询出华创维护的国内国外账户信息
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", task.getOrderId());
				paramMap.put("type", BusConstants.ORDERDETAILS_TYPE_HC_LOCACC);
				hcAccT3 = orderService.getOrderDetailsByParams(paramMap);
				
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", task.getOrderId());
				paramMap.put("type", BusConstants.ORDERDETAILS_TYPE_HC_HWACC);
				hcAccT4 = orderService.getOrderDetailsByParams(paramMap);
				
				mv.addObject("ocList", ocList);
				mv.addObject("accInfo", hwUserAcc);
				mv.addObject("hcT3", hcAccT3);
				mv.addObject("hcT4", hcAccT4);
				mv.addObject("payMoney", order.getPayAmount().add(new BigDecimal(50000)));
				mv.addObject("task", task);
				mv.setViewName("task/plProTaskAudit");
				break;
			case 6:
				//海外用户账户信息 
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", task.getOrderId());
				paramMap.put("type", BusConstants.ORDERDETAILS_TYPE_HWUSER_LOCACC);
				hwUserAcc = orderService.getOrderDetailsByParams(paramMap);
				//查询上传图片信息
				paramMap.put("taskType", 4);
				ocList = orderCertService.getOrderCertList(paramMap);
				//查询出华创维护的国内国外账户信息
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", task.getOrderId());
				paramMap.put("type", BusConstants.ORDERDETAILS_TYPE_HC_LOCACC);
				hcAccT3 = orderService.getOrderDetailsByParams(paramMap);
				
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", task.getOrderId());
				paramMap.put("type", BusConstants.ORDERDETAILS_TYPE_HC_HWACC);
				hcAccT4 = orderService.getOrderDetailsByParams(paramMap);
				
				mv.addObject("ocList", ocList);
				mv.addObject("accInfo", hwUserAcc);
				mv.addObject("hcT3", hcAccT3);
				mv.addObject("hcT4", hcAccT4);
				mv.addObject("payMoney", order.getPayAmount().add(new BigDecimal(50000)));
				mv.addObject("task", task);
				mv.setViewName("task/hcReFirstPayTask");
				break;
			case 7:
				//海外用户账户信息
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", task.getOrderId());
				paramMap.put("type", BusConstants.ORDERDETAILS_TYPE_HWUSER_LOCACC);
				hwUserAcc = orderService.getOrderDetailsByParams(paramMap);
				//查询上传图片信息
				paramMap.put("taskType",4);
				ocList = orderCertService.getOrderCertList(paramMap);
				mv.addObject("ocList", ocList);
				//查询出华创维护的国内国外账户信息
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", task.getOrderId());
				paramMap.put("type", BusConstants.ORDERDETAILS_TYPE_HC_LOCACC);
				hcAccT3 = orderService.getOrderDetailsByParams(paramMap);
				
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", task.getOrderId());
				paramMap.put("type", BusConstants.ORDERDETAILS_TYPE_HC_HWACC);
				hcAccT4 = orderService.getOrderDetailsByParams(paramMap);
				
				mv.addObject("accInfo", hwUserAcc);
				mv.addObject("hcT3", hcAccT3);
				mv.addObject("hcT4", hcAccT4);
				mv.addObject("payMoney", order.getPayAmount().add(new BigDecimal(50000)));
				mv.addObject("task", task);
				mv.setViewName("task/plProTaskAudit");
				break;
			case 8:
				//海外用户账户信息 上传收款凭证
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", task.getOrderId());
				paramMap.put("type", BusConstants.ORDERDETAILS_TYPE_HWUSER_LOCACC);
				hwUserAcc = orderService.getOrderDetailsByParams(paramMap);
				
				mv.addObject("accInfo", hwUserAcc);
				mv.addObject("payMoney", order.getPayAmount().add(new BigDecimal(50000)));
				mv.addObject("task", task);
				mv.setViewName("task/proTaskUpload");
				break;
			case 9:
				//海外用户账户信息
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", task.getOrderId());
				paramMap.put("type", BusConstants.ORDERDETAILS_TYPE_HWUSER_LOCACC);
				hwUserAcc = orderService.getOrderDetailsByParams(paramMap);
				//查询上传图片信息
				paramMap.put("taskType", 8);
				ocList = orderCertService.getOrderCertList(paramMap);
				mv.addObject("ocList", ocList);
				
				mv.addObject("accInfo", hwUserAcc);
				mv.addObject("payMoney", order.getPayAmount().add(new BigDecimal(50000)));
				mv.addObject("task", task);
				mv.setViewName("task/plProTaskAudit");
				break;
			case 10:
				//海外用户账户信息
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", task.getOrderId());
				paramMap.put("type", BusConstants.ORDERDETAILS_TYPE_HWUSER_LOCACC);
				hwUserAcc = orderService.getOrderDetailsByParams(paramMap);
				//查询上传图片信息 并重新上传收款凭证
				paramMap.put("taskType", 8);
				ocList = orderCertService.getOrderCertList(paramMap);
				mv.addObject("ocList", ocList);
				
				mv.addObject("accInfo", hwUserAcc);
				mv.addObject("payMoney", order.getPayAmount().add(new BigDecimal(50000)));
				mv.addObject("task", task);
				mv.setViewName("task/proTaskReUpload");
				break;
			case 11:
				//海外用户账户信息
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", task.getOrderId());
				paramMap.put("type", BusConstants.ORDERDETAILS_TYPE_HWUSER_LOCACC);
				hwUserAcc = orderService.getOrderDetailsByParams(paramMap);
				//查询上传图片信息
				paramMap.put("taskType", 8);
				ocList = orderCertService.getOrderCertList(paramMap);
				mv.addObject("ocList", ocList);
				
				mv.addObject("accInfo", hwUserAcc);
				mv.addObject("payMoney", order.getPayAmount().add(new BigDecimal(50000)));
				mv.addObject("task", task);
				mv.setViewName("task/plProTaskAudit");
				break;
			case 12:
				//查询华创国内账户信息 上传打款凭证
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", task.getOrderId());
				paramMap.put("type", BusConstants.ORDERDETAILS_TYPE_HC_LOCACC);
				hcAccT3 = orderService.getOrderDetailsByParams(paramMap);
				
				mv.addObject("accInfo", hcAccT3);
				mv.addObject("payMoney", order.getPayAmount());
				mv.addObject("task", task);
				mv.setViewName("task/proTaskUpload");
				break;
			case 13:
				//查询华创国内账户信息
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", task.getOrderId());
				paramMap.put("type", BusConstants.ORDERDETAILS_TYPE_HC_LOCACC);
				hcAccT3 = orderService.getOrderDetailsByParams(paramMap);
				
				//查询上传图片信息
				paramMap.put("taskType",12);
				ocList = orderCertService.getOrderCertList(paramMap);
				mv.addObject("ocList", ocList);
				
				mv.addObject("accInfo", hcAccT3);
				mv.addObject("payMoney", order.getPayAmount());
				mv.addObject("task", task);
				mv.setViewName("task/plProTaskAudit");
				break;
			case 14:
				//查询华创国内账户信息
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", task.getOrderId());
				paramMap.put("type", BusConstants.ORDERDETAILS_TYPE_HC_LOCACC);
				hcAccT3 = orderService.getOrderDetailsByParams(paramMap);
				
				//查询上传图片信息
				paramMap.put("taskType", 12);
				ocList = orderCertService.getOrderCertList(paramMap);
				mv.addObject("ocList", ocList);
				
				mv.addObject("accInfo", hcAccT3);
				mv.addObject("payMoney", order.getPayAmount());
				mv.addObject("task", task);
				mv.setViewName("task/proTaskReUpload");
				break;
			case 15:
				//查询华创国内账户信息
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", task.getOrderId());
				paramMap.put("type", BusConstants.ORDERDETAILS_TYPE_HC_LOCACC);
				hcAccT3 = orderService.getOrderDetailsByParams(paramMap);
				
				//查询上传图片信息
				paramMap.put("taskType", 12);
				ocList = orderCertService.getOrderCertList(paramMap);
				mv.addObject("ocList", ocList);
				
				mv.addObject("accInfo", hcAccT3);
				mv.addObject("payMoney", order.getPayAmount());
				mv.addObject("task", task);
				mv.setViewName("task/plProTaskAudit");
				break;
			case 16:
				//查询华创国内账户信息 上传收款凭证
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", task.getOrderId());
				paramMap.put("type", BusConstants.ORDERDETAILS_TYPE_HC_LOCACC);
				hcAccT3 = orderService.getOrderDetailsByParams(paramMap);
				
				
				mv.addObject("accInfo", hcAccT3);
				mv.addObject("payMoney", order.getPayAmount());
				mv.addObject("task", task);
				mv.setViewName("task/proTaskUpload");
				break;
			case 17:
				//查询华创国内账户信息
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", task.getOrderId());
				paramMap.put("type", BusConstants.ORDERDETAILS_TYPE_HC_LOCACC);
				hcAccT3 = orderService.getOrderDetailsByParams(paramMap);
				
				//查询上传图片信息
				paramMap.put("taskType", 16);
				ocList = orderCertService.getOrderCertList(paramMap);
				mv.addObject("ocList", ocList);
				
				mv.addObject("accInfo", hcAccT3);
				mv.addObject("payMoney", order.getPayAmount());
				mv.addObject("task", task);
				mv.setViewName("task/plProTaskAudit");
				break;
			case 18:
				//查询华创国内账户信息 可重新上传收款凭证
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", task.getOrderId());
				paramMap.put("type", BusConstants.ORDERDETAILS_TYPE_HC_LOCACC);
				hcAccT3 = orderService.getOrderDetailsByParams(paramMap);
				
				//查询上传图片信息
				paramMap.put("taskType", 16);
				ocList = orderCertService.getOrderCertList(paramMap);
				mv.addObject("ocList", ocList);
				
				mv.addObject("accInfo", hcAccT3);
				mv.addObject("payMoney", order.getPayAmount());
				mv.addObject("task", task);
				mv.setViewName("task/proTaskReUpload");
				break;
			case 19:
				//查询华创国内账户信息
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", task.getOrderId());
				paramMap.put("type", BusConstants.ORDERDETAILS_TYPE_HC_LOCACC);
				hcAccT3 = orderService.getOrderDetailsByParams(paramMap);
				
				//查询上传图片信息
				paramMap.put("taskType", 16);
				ocList = orderCertService.getOrderCertList(paramMap);
				mv.addObject("ocList", ocList);
				
				mv.addObject("accInfo", hcAccT3);
				mv.addObject("payMoney", order.getPayAmount());
				mv.addObject("task", task);
				mv.setViewName("task/plProTaskAudit");
				break;
			case 20:
				//海外用户账户信息 上传打款凭证
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", task.getOrderId());
				paramMap.put("type", BusConstants.ORDERDETAILS_TYPE_HWUSER_LOCACC);
				hwUserAcc = orderService.getOrderDetailsByParams(paramMap);
				
				mv.addObject("accInfo", hwUserAcc);
				mv.addObject("payMoney", order.getPayAmount());
				mv.addObject("task", task);
				mv.setViewName("task/proTaskUpload");
				break;
			case 21:
				//海外用户账户信息 
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", task.getOrderId());
				paramMap.put("type", BusConstants.ORDERDETAILS_TYPE_HWUSER_LOCACC);
				hwUserAcc = orderService.getOrderDetailsByParams(paramMap);
				
				//查询上传图片信息
				paramMap.put("taskType", 20);
				ocList = orderCertService.getOrderCertList(paramMap);
				mv.addObject("ocList", ocList);
				
				mv.addObject("accInfo", hwUserAcc);
				mv.addObject("payMoney", order.getPayAmount());
				mv.addObject("task", task);
				mv.setViewName("task/plProTaskAudit");
				break;
			case 22:
				//海外用户账户信息 可重新上传打款凭证 
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", task.getOrderId());
				paramMap.put("type", BusConstants.ORDERDETAILS_TYPE_HWUSER_LOCACC);
				hwUserAcc = orderService.getOrderDetailsByParams(paramMap);
				
				//查询上传图片信息
				paramMap.put("taskType", 20);
				ocList = orderCertService.getOrderCertList(paramMap);
				mv.addObject("ocList", ocList);
				
				mv.addObject("accInfo", hwUserAcc);
				mv.addObject("payMoney", order.getPayAmount());
				mv.addObject("task", task);
				mv.setViewName("task/proTaskReUpload");
				break;
			case 23:
				//海外用户账户信息 
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", task.getOrderId());
				paramMap.put("type", BusConstants.ORDERDETAILS_TYPE_HWUSER_LOCACC);
				hwUserAcc = orderService.getOrderDetailsByParams(paramMap);
				
				//查询上传图片信息
				paramMap.put("taskType", 20);
				ocList = orderCertService.getOrderCertList(paramMap);
				mv.addObject("ocList", ocList);
				
				mv.addObject("accInfo", hwUserAcc);
				mv.addObject("payMoney", order.getPayAmount());
				mv.addObject("task", task);
				mv.setViewName("task/plProTaskAudit");
				break;
			case 24:
				//海外用户账户信息 上传收款凭证
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", task.getOrderId());
				paramMap.put("type", BusConstants.ORDERDETAILS_TYPE_HWUSER_LOCACC);
				hwUserAcc = orderService.getOrderDetailsByParams(paramMap);
				
				mv.addObject("accInfo", hwUserAcc);
				mv.addObject("payMoney", order.getPayAmount());
				mv.addObject("task", task);
				mv.setViewName("task/proTaskUpload");
				break;
			case 25:
				//海外用户账户信息
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", task.getOrderId());
				paramMap.put("type", BusConstants.ORDERDETAILS_TYPE_HWUSER_LOCACC);
				hwUserAcc = orderService.getOrderDetailsByParams(paramMap);
				
				//查询上传图片信息
				paramMap.put("taskType", 24);
				ocList = orderCertService.getOrderCertList(paramMap);
				mv.addObject("ocList", ocList);
				
				mv.addObject("accInfo", hwUserAcc);
				mv.addObject("payMoney", order.getPayAmount());
				mv.addObject("task", task);
				mv.setViewName("task/plProTaskAudit");
				break;
			case 26:
				//海外用户账户信息 可重新上传收款凭证
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", task.getOrderId());
				paramMap.put("type", BusConstants.ORDERDETAILS_TYPE_HWUSER_LOCACC);
				hwUserAcc = orderService.getOrderDetailsByParams(paramMap);
				
				//查询上传图片信息
				paramMap.put("taskType", 24);
				ocList = orderCertService.getOrderCertList(paramMap);
				mv.addObject("ocList", ocList);
				
				mv.addObject("accInfo", hwUserAcc);
				mv.addObject("payMoney", order.getPayAmount());
				mv.addObject("task", task);
				mv.setViewName("task/proTaskReUpload");
				break;
			case 27:
				//海外用户账户信息
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", task.getOrderId());
				paramMap.put("type", BusConstants.ORDERDETAILS_TYPE_HWUSER_LOCACC);
				hwUserAcc = orderService.getOrderDetailsByParams(paramMap);
				
				//查询上传图片信息
				paramMap.put("taskType",24);
				ocList = orderCertService.getOrderCertList(paramMap);
				mv.addObject("ocList", ocList);
				
				mv.addObject("accInfo", hwUserAcc);
				mv.addObject("payMoney", order.getPayAmount());
				mv.addObject("task", task);
				mv.setViewName("task/plProTaskAudit");
				break;
			case 28:
				//查询华创海外账户 上传海外用户第一次打款凭证
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", task.getOrderId());
				paramMap.put("type", BusConstants.ORDERDETAILS_TYPE_HC_HWACC);
				hcAccT4 = orderService.getOrderDetailsByParams(paramMap);
				
				mv.addObject("accInfo", hcAccT4);
				mv.addObject("payMoney", order.getApplyAmount());
				mv.addObject("task", task);
				mv.setViewName("task/proTaskUpload");
				break;
			case 29:
				//查询华创海外账户 
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", task.getOrderId());
				paramMap.put("type", BusConstants.ORDERDETAILS_TYPE_HC_HWACC);
				hcAccT4 = orderService.getOrderDetailsByParams(paramMap);
				
				//查询上传图片信息
				paramMap.put("taskType", 28);
				ocList = orderCertService.getOrderCertList(paramMap);
				mv.addObject("ocList", ocList);
				
				mv.addObject("accInfo", hcAccT4);
				mv.addObject("payMoney", order.getApplyAmount());
				mv.addObject("task", task);
				mv.setViewName("task/plProTaskAudit");
				break;
			case 30:
				//查询华创海外账户 可重新上传海外用户第一次打款凭证
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", task.getOrderId());
				paramMap.put("type", BusConstants.ORDERDETAILS_TYPE_HC_HWACC);
				hcAccT4 = orderService.getOrderDetailsByParams(paramMap);
				
				//查询上传图片信息
				paramMap.put("taskType", 28);
				ocList = orderCertService.getOrderCertList(paramMap);
				mv.addObject("ocList", ocList);
				
				mv.addObject("accInfo", hcAccT4);
				mv.addObject("payMoney", order.getApplyAmount());
				mv.addObject("task", task);
				mv.setViewName("task/proTaskReUpload");
				break;
			case 31:
				//查询华创海外账户 
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", task.getOrderId());
				paramMap.put("type", BusConstants.ORDERDETAILS_TYPE_HC_HWACC);
				hcAccT4 = orderService.getOrderDetailsByParams(paramMap);
				
				//查询上传图片信息
				paramMap.put("taskType", 28);
				ocList = orderCertService.getOrderCertList(paramMap);
				mv.addObject("ocList", ocList);
				
				mv.addObject("accInfo", hcAccT4);
				mv.addObject("payMoney", order.getApplyAmount());
				mv.addObject("task", task);
				mv.setViewName("task/plProTaskAudit");
				break;
			case 32:
				//查询华创海外账户 上传收款凭证
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", task.getOrderId());
				paramMap.put("type", BusConstants.ORDERDETAILS_TYPE_HC_HWACC);
				hcAccT4 = orderService.getOrderDetailsByParams(paramMap);
				
				mv.addObject("accInfo", hcAccT4);
				mv.addObject("payMoney", order.getApplyAmount());
				mv.addObject("task", task);
				mv.setViewName("task/proTaskUpload");
				break;
			case 33:
				//查询华创海外账户 
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", task.getOrderId());
				paramMap.put("type", BusConstants.ORDERDETAILS_TYPE_HC_HWACC);
				hcAccT4 = orderService.getOrderDetailsByParams(paramMap);
				
				//查询上传图片信息
				paramMap.put("taskType", 32);
				ocList = orderCertService.getOrderCertList(paramMap);
				mv.addObject("ocList", ocList);
				
				mv.addObject("accInfo", hcAccT4);
				mv.addObject("payMoney", order.getApplyAmount());
				mv.addObject("task", task);
				mv.setViewName("task/plProTaskAudit");
				break;
			case 34:
				//查询华创海外账户 可重新上传收款凭证
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", task.getOrderId());
				paramMap.put("type", BusConstants.ORDERDETAILS_TYPE_HC_HWACC);
				hcAccT4 = orderService.getOrderDetailsByParams(paramMap);
				
				//查询上传图片信息
				paramMap.put("taskType", 32);
				ocList = orderCertService.getOrderCertList(paramMap);
				mv.addObject("ocList", ocList);
				
				mv.addObject("accInfo", hcAccT4);
				mv.addObject("payMoney", order.getApplyAmount());
				mv.addObject("task", task);
				mv.setViewName("task/proTaskReUpload");
				break;
			case 35:
				//查询华创海外账户
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", task.getOrderId());
				paramMap.put("type", BusConstants.ORDERDETAILS_TYPE_HC_HWACC);
				hcAccT4 = orderService.getOrderDetailsByParams(paramMap);
				
				//查询上传图片信息
				paramMap.put("taskType", 32);
				ocList = orderCertService.getOrderCertList(paramMap);
				mv.addObject("ocList", ocList);
				
				mv.addObject("accInfo", hcAccT4);
				mv.addObject("payMoney", order.getApplyAmount());
				mv.addObject("task", task);
				mv.setViewName("task/plProTaskAudit");
				break;
			case 36:
				//查询hwj海外账户信息 上传打款凭证
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", task.getOrderId());
				paramMap.put("type", BusConstants.ORDERDETAILS_TYPE_CUSTOMER_HWACC);
				hwAcc = orderService.getOrderDetailsByParams(paramMap);
				
				mv.addObject("accInfo", hwAcc);
				mv.addObject("payMoney", order.getApplyAmount());
				mv.addObject("task", task);
				mv.setViewName("task/proTaskUpload");
				break;
			case 37:
				//查询hwj海外账户信息
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", task.getOrderId());
				paramMap.put("type", BusConstants.ORDERDETAILS_TYPE_CUSTOMER_HWACC);
				hwAcc = orderService.getOrderDetailsByParams(paramMap);
				
				//查询上传图片信息
				paramMap.put("taskType", 36);
				ocList = orderCertService.getOrderCertList(paramMap);
				mv.addObject("ocList", ocList);
				
				mv.addObject("accInfo", hwAcc);
				mv.addObject("payMoney", order.getApplyAmount());
				mv.addObject("task", task);
				mv.setViewName("task/plProTaskAudit");
				break;
			case 38:
				//查询hwj海外账户信息 可重新上传凭证
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", task.getOrderId());
				paramMap.put("type", BusConstants.ORDERDETAILS_TYPE_CUSTOMER_HWACC);
				hwAcc = orderService.getOrderDetailsByParams(paramMap);
				
				//查询上传图片信息
				paramMap.put("taskType", 36);
				ocList = orderCertService.getOrderCertList(paramMap);
				mv.addObject("ocList", ocList);
				
				mv.addObject("accInfo", hwAcc);
				mv.addObject("payMoney", order.getApplyAmount());
				mv.addObject("task", task);
				mv.setViewName("task/proTaskReUpload");
				break;
			case 39:
				//查询hwj海外账户信息
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", task.getOrderId());
				paramMap.put("type", BusConstants.ORDERDETAILS_TYPE_CUSTOMER_HWACC);
				hwAcc = orderService.getOrderDetailsByParams(paramMap);
				
				//查询上传图片信息
				paramMap.put("taskType", 36);
				ocList = orderCertService.getOrderCertList(paramMap);
				mv.addObject("ocList", ocList);
				
				mv.addObject("accInfo", hwAcc);
				mv.addObject("payMoney", order.getApplyAmount());
				mv.addObject("task", task);
				mv.setViewName("task/plProTaskAudit");
				break;
			case 40:
				//查询hwj海外账户信息 上传收款凭证
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", task.getOrderId());
				paramMap.put("type", BusConstants.ORDERDETAILS_TYPE_CUSTOMER_HWACC);
				hwAcc = orderService.getOrderDetailsByParams(paramMap);
				
				mv.addObject("accInfo", hwAcc);
				mv.addObject("payMoney", order.getApplyAmount());
				mv.addObject("task", task);
				mv.setViewName("task/proTaskUpload");
				break;
			case 41:
				//查询hwj海外账户信息
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", task.getOrderId());
				paramMap.put("type", BusConstants.ORDERDETAILS_TYPE_CUSTOMER_HWACC);
				hwAcc = orderService.getOrderDetailsByParams(paramMap);
				
				//查询上传图片信息
				paramMap.put("taskType", 40);
				ocList = orderCertService.getOrderCertList(paramMap);
				mv.addObject("ocList", ocList);
				
				mv.addObject("accInfo", hwAcc);
				mv.addObject("payMoney", order.getApplyAmount());
				mv.addObject("task", task);
				mv.setViewName("task/plProTaskAudit");
				break;
			case 42:
				//查询hwj海外账户信息 可重新上传收款凭证
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", task.getOrderId());
				paramMap.put("type", BusConstants.ORDERDETAILS_TYPE_CUSTOMER_HWACC);
				hwAcc = orderService.getOrderDetailsByParams(paramMap);
				
				//查询上传图片信息
				paramMap.put("taskType", 40);
				ocList = orderCertService.getOrderCertList(paramMap);
				mv.addObject("ocList", ocList);
				
				mv.addObject("accInfo", hwAcc);
				mv.addObject("payMoney", order.getApplyAmount());
				mv.addObject("task", task);
				mv.setViewName("task/proTaskReUpload");
				break;
			case 43:
				//查询hwj海外账户信息
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", task.getOrderId());
				paramMap.put("type", BusConstants.ORDERDETAILS_TYPE_CUSTOMER_HWACC);
				hwAcc = orderService.getOrderDetailsByParams(paramMap);
				
				//查询上传图片信息
				paramMap.put("taskType", 40);
				ocList = orderCertService.getOrderCertList(paramMap);
				mv.addObject("ocList", ocList);
				
				mv.addObject("accInfo", hwAcc);
				mv.addObject("payMoney", order.getApplyAmount());
				mv.addObject("task", task);
				mv.setViewName("task/plProTaskAudit");
				break;
			case 44:
				//查询华创海外账户 上传打款凭证
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", task.getOrderId());
				paramMap.put("type", BusConstants.ORDERDETAILS_TYPE_HC_HWACC);
				hcAccT4 = orderService.getOrderDetailsByParams(paramMap);
				//查询rmb兑美元汇率
				paramMap =  new HashMap<String,Object>();
				paramMap.put("status", BusConstants.RATE_STATUS_YES);
				paramMap.put("type", BusConstants.RATE_TYPE_RMB_TO_US);
				curRToURate = feeService.getCurrentRate(paramMap);
				
				mv.addObject("accInfo", hcAccT4);
				mv.addObject("payMoney", order.getApplyAmount().add(new BigDecimal(50000).multiply(curRToURate.getRateHigh().divide(new BigDecimal(100)))));
				mv.addObject("task", task);
				mv.setViewName("task/proTaskUpload");
				break;
			case 45:
				//查询华创海外账户 
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", task.getOrderId());
				paramMap.put("type", BusConstants.ORDERDETAILS_TYPE_HC_HWACC);
				hcAccT4 = orderService.getOrderDetailsByParams(paramMap);
				
				//查询上传图片信息
				paramMap.put("taskType", 44);
				ocList = orderCertService.getOrderCertList(paramMap);
				mv.addObject("ocList", ocList);
				
				//查询rmb兑美元汇率
				paramMap =  new HashMap<String,Object>();
				paramMap.put("status", BusConstants.RATE_STATUS_YES);
				paramMap.put("type", BusConstants.RATE_TYPE_RMB_TO_US);
				curRToURate = feeService.getCurrentRate(paramMap);
				
				mv.addObject("accInfo", hcAccT4);
				mv.addObject("payMoney", order.getApplyAmount().add(new BigDecimal(50000).multiply(curRToURate.getRateHigh()).divide(new BigDecimal(100))));
				mv.addObject("task", task);
				mv.setViewName("task/plProTaskAudit");
				break;
			case 46:
				//查询华创海外账户 可重新上传打款凭证
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", task.getOrderId());
				paramMap.put("type", BusConstants.ORDERDETAILS_TYPE_HC_HWACC);
				hcAccT4 = orderService.getOrderDetailsByParams(paramMap);
				
				//查询上传图片信息
				paramMap.put("taskType", 44);
				ocList = orderCertService.getOrderCertList(paramMap);
				mv.addObject("ocList", ocList);
				
				//查询rmb兑美元汇率
				paramMap =  new HashMap<String,Object>();
				paramMap.put("status", BusConstants.RATE_STATUS_YES);
				paramMap.put("type", BusConstants.RATE_TYPE_RMB_TO_US);
				curRToURate = feeService.getCurrentRate(paramMap);
				
				mv.addObject("accInfo", hcAccT4);
				mv.addObject("payMoney", order.getApplyAmount().add(new BigDecimal(50000).multiply(curRToURate.getRateHigh()).divide(new BigDecimal(100))));
				mv.addObject("task", task);
				mv.setViewName("task/proTaskReUpload");
				break;
			case 47:
				//查询华创海外账户 
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", task.getOrderId());
				paramMap.put("type", BusConstants.ORDERDETAILS_TYPE_HC_HWACC);
				hcAccT4 = orderService.getOrderDetailsByParams(paramMap);
				
				//查询上传图片信息
				paramMap.put("taskType", 44);
				ocList = orderCertService.getOrderCertList(paramMap);
				mv.addObject("ocList", ocList);
				
				//查询rmb兑美元汇率
				paramMap =  new HashMap<String,Object>();
				paramMap.put("status", BusConstants.RATE_STATUS_YES);
				paramMap.put("type", BusConstants.RATE_TYPE_RMB_TO_US);
				curRToURate = feeService.getCurrentRate(paramMap);
				
				mv.addObject("accInfo", hcAccT4);
				mv.addObject("payMoney", order.getApplyAmount().add(new BigDecimal(50000).multiply(curRToURate.getRateHigh()).divide(new BigDecimal(100))));
				mv.addObject("task", task);
				mv.setViewName("task/plProTaskAudit");
				break;
			case 48:
				//查询华创海外账户  上传收款凭证
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", task.getOrderId());
				paramMap.put("type", BusConstants.ORDERDETAILS_TYPE_HC_HWACC);
				hcAccT4 = orderService.getOrderDetailsByParams(paramMap);
				
				//查询rmb兑美元汇率
				paramMap =  new HashMap<String,Object>();
				paramMap.put("status", BusConstants.RATE_STATUS_YES);
				paramMap.put("type", BusConstants.RATE_TYPE_RMB_TO_US);
				curRToURate = feeService.getCurrentRate(paramMap);
				
				mv.addObject("accInfo", hcAccT4);
				mv.addObject("payMoney", order.getApplyAmount().add(new BigDecimal(50000).multiply(curRToURate.getRateHigh()).divide(new BigDecimal(100))));
				mv.addObject("task", task);
				mv.setViewName("task/proTaskUpload");
				break;
			case 49:
				//查询华创海外账户
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", task.getOrderId());
				paramMap.put("type", BusConstants.ORDERDETAILS_TYPE_HC_HWACC);
				hcAccT4 = orderService.getOrderDetailsByParams(paramMap);
				
				//查询上传图片信息
				paramMap.put("taskType", 48);
				ocList = orderCertService.getOrderCertList(paramMap);
				mv.addObject("ocList", ocList);
				
				//查询rmb兑美元汇率
				paramMap =  new HashMap<String,Object>();
				paramMap.put("status", BusConstants.RATE_STATUS_YES);
				paramMap.put("type", BusConstants.RATE_TYPE_RMB_TO_US);
				curRToURate = feeService.getCurrentRate(paramMap);
				
				mv.addObject("accInfo", hcAccT4);
				mv.addObject("payMoney", order.getApplyAmount().add(new BigDecimal(50000).multiply(curRToURate.getRateHigh()).divide(new BigDecimal(100))));
				mv.addObject("task", task);
				mv.setViewName("task/plProTaskAudit");
				break;
			case 50:
				//查询华创海外账户 可重新上传收款凭证
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", task.getOrderId());
				paramMap.put("type", BusConstants.ORDERDETAILS_TYPE_HC_HWACC);
				hcAccT4 = orderService.getOrderDetailsByParams(paramMap);
				
				//查询上传图片信息
				paramMap.put("taskType", 48);
				ocList = orderCertService.getOrderCertList(paramMap);
				mv.addObject("ocList", ocList);
				
				//查询rmb兑美元汇率
				paramMap =  new HashMap<String,Object>();
				paramMap.put("status", BusConstants.RATE_STATUS_YES);
				paramMap.put("type", BusConstants.RATE_TYPE_RMB_TO_US);
				curRToURate = feeService.getCurrentRate(paramMap);
				
				mv.addObject("accInfo", hcAccT4);
				mv.addObject("payMoney", order.getApplyAmount().add(new BigDecimal(50000).multiply(curRToURate.getRateHigh()).divide(new BigDecimal(100))));
				mv.addObject("task", task);
				mv.setViewName("task/proTaskReUpload");
				break;
			case 51:
				//查询华创海外账户
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", task.getOrderId());
				paramMap.put("type", BusConstants.ORDERDETAILS_TYPE_HC_HWACC);
				hcAccT4 = orderService.getOrderDetailsByParams(paramMap);
				
				//查询上传图片信息
				paramMap.put("taskType", 48);
				ocList = orderCertService.getOrderCertList(paramMap);
				mv.addObject("ocList", ocList);
				
				//查询rmb兑美元汇率
				paramMap =  new HashMap<String,Object>();
				paramMap.put("status", BusConstants.RATE_STATUS_YES);
				paramMap.put("type", BusConstants.RATE_TYPE_RMB_TO_US);
				curRToURate = feeService.getCurrentRate(paramMap);
				
				mv.addObject("accInfo", hcAccT4);
				mv.addObject("payMoney", order.getApplyAmount().add(new BigDecimal(50000).multiply(curRToURate.getRateHigh()).divide(new BigDecimal(100))));
				mv.addObject("task", task);
				mv.setViewName("task/plProTaskAudit");
				break;
			case 52:
				//查询海外用户账户信息 上传打款凭证
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", task.getOrderId());
				paramMap.put("type", BusConstants.ORDERDETAILS_TYPE_HWUSER_LOCACC);
				hwUserAcc = orderService.getOrderDetailsByParams(paramMap);

				mv.addObject("accInfo", hwUserAcc);
				mv.addObject("payMoney", order.getPayAmount()+"￥");
				mv.addObject("task", task);
				mv.setViewName("task/proTaskUpload");
				break;
			case 53:
				//查询海外用户账户信息 上传打款凭证
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", task.getOrderId());
				paramMap.put("type", BusConstants.ORDERDETAILS_TYPE_HWUSER_LOCACC);
				hwUserAcc = orderService.getOrderDetailsByParams(paramMap);

				//查询上传图片信息
				paramMap.put("taskType",52);
				ocList = orderCertService.getOrderCertList(paramMap);
				mv.addObject("ocList", ocList);
				
				mv.addObject("accInfo", hwUserAcc);
				mv.addObject("payMoney", order.getPayAmount()+"￥");
				mv.addObject("task", task);
				mv.setViewName("task/plProTaskAudit");
				break;
			case 54:
				//查询海外用户账户信息 可重新上传打款凭证
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", task.getOrderId());
				paramMap.put("type", BusConstants.ORDERDETAILS_TYPE_HWUSER_LOCACC);
				hwUserAcc = orderService.getOrderDetailsByParams(paramMap);

				//查询上传图片信息
				paramMap.put("taskType", 52);
				ocList = orderCertService.getOrderCertList(paramMap);
				mv.addObject("ocList", ocList);
				
				mv.addObject("accInfo", hwUserAcc);
				mv.addObject("payMoney", order.getPayAmount()+"￥");
				mv.addObject("task", task);
				mv.setViewName("task/proTaskReUpload");
				break;
			case 55:
				//查询海外用户账户信息 
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", task.getOrderId());
				paramMap.put("type", BusConstants.ORDERDETAILS_TYPE_HWUSER_LOCACC);
				hwUserAcc = orderService.getOrderDetailsByParams(paramMap);

				//查询上传图片信息
				paramMap.put("taskType", 52);
				ocList = orderCertService.getOrderCertList(paramMap);
				mv.addObject("ocList", ocList);
				
				mv.addObject("accInfo", hwUserAcc);
				mv.addObject("payMoney", order.getPayAmount()+"￥");
				mv.addObject("task", task);
				mv.setViewName("task/plProTaskAudit");
				break;
			case 56:
				//查询海外用户账户信息 上传收款凭证
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", task.getOrderId());
				paramMap.put("type", BusConstants.ORDERDETAILS_TYPE_HWUSER_LOCACC);
				hwUserAcc = orderService.getOrderDetailsByParams(paramMap);

				mv.addObject("accInfo", hwUserAcc);
				mv.addObject("payMoney", order.getPayAmount()+"￥");
				mv.addObject("task", task);
				mv.setViewName("task/proTaskUpload");
				break;
			case 57:
				//查询海外用户账户信息
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", task.getOrderId());
				paramMap.put("type", BusConstants.ORDERDETAILS_TYPE_HWUSER_LOCACC);
				hwUserAcc = orderService.getOrderDetailsByParams(paramMap);

				//查询上传图片信息
				paramMap.put("taskType", 56);
				ocList = orderCertService.getOrderCertList(paramMap);
				mv.addObject("ocList", ocList);
				
				mv.addObject("accInfo", hwUserAcc);
				mv.addObject("payMoney", order.getPayAmount()+"￥");
				mv.addObject("task", task);
				mv.setViewName("task/plProTaskAudit");
				break;
			case 58:
				//查询海外用户账户信息 可重新上传凭证
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", task.getOrderId());
				paramMap.put("type", BusConstants.ORDERDETAILS_TYPE_HWUSER_LOCACC);
				hwUserAcc = orderService.getOrderDetailsByParams(paramMap);

				//查询上传图片信息
				paramMap.put("taskType", 56);
				ocList = orderCertService.getOrderCertList(paramMap);
				mv.addObject("ocList", ocList);
				
				mv.addObject("accInfo", hwUserAcc);
				mv.addObject("payMoney", order.getPayAmount()+"￥");
				mv.addObject("task", task);
				mv.setViewName("task/proTaskReUpload");
				break;
			case 59:
				//查询海外用户账户信息
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", task.getOrderId());
				paramMap.put("type", BusConstants.ORDERDETAILS_TYPE_HWUSER_LOCACC);
				hwUserAcc = orderService.getOrderDetailsByParams(paramMap);

				//查询上传图片信息
				paramMap.put("taskType", 56);
				ocList = orderCertService.getOrderCertList(paramMap);
				mv.addObject("ocList", ocList);
				
				mv.addObject("accInfo", hwUserAcc);
				mv.addObject("payMoney", order.getPayAmount()+"￥");
				mv.addObject("task", task);
				mv.setViewName("task/plProTaskAudit");
				break;
			case 60:
				//查询海外账户信息 上传打款凭证
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", task.getOrderId());
				paramMap.put("type", BusConstants.ORDERDETAILS_TYPE_CUSTOMER_HWACC);
				hwAcc = orderService.getOrderDetailsByParams(paramMap);
				
				mv.addObject("accInfo", hwAcc);
				mv.addObject("payMoney", order.getApplyAmount()+"$");
				mv.addObject("task", task);
				mv.setViewName("task/proTaskUpload");
				break;
			case 61:
				//查询海外账户信息
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", task.getOrderId());
				paramMap.put("type", BusConstants.ORDERDETAILS_TYPE_CUSTOMER_HWACC);
				hwAcc = orderService.getOrderDetailsByParams(paramMap);
				
				//查询上传图片信息
				paramMap.put("taskType", 60);
				ocList = orderCertService.getOrderCertList(paramMap);
				mv.addObject("ocList", ocList);
				
				mv.addObject("accInfo", hwAcc);
				mv.addObject("payMoney", order.getApplyAmount()+"$");
				mv.addObject("task", task);
				mv.setViewName("task/plProTaskAudit");
				break;
			case 62:
				//查询海外账户信息 可重新上传打款凭证
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", task.getOrderId());
				paramMap.put("type", BusConstants.ORDERDETAILS_TYPE_CUSTOMER_HWACC);
				hwAcc = orderService.getOrderDetailsByParams(paramMap);
				
				//查询上传图片信息
				paramMap.put("taskType", 60);
				ocList = orderCertService.getOrderCertList(paramMap);
				mv.addObject("ocList", ocList);
				
				mv.addObject("accInfo", hwAcc);
				mv.addObject("payMoney", order.getApplyAmount()+"$");
				mv.addObject("task", task);
				mv.setViewName("task/proTaskReUpload");
				break;
			case 63:
				//查询海外账户信息
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", task.getOrderId());
				paramMap.put("type", BusConstants.ORDERDETAILS_TYPE_CUSTOMER_HWACC);
				hwAcc = orderService.getOrderDetailsByParams(paramMap);
				
				//查询上传图片信息
				paramMap.put("taskType", 60);
				ocList = orderCertService.getOrderCertList(paramMap);
				mv.addObject("ocList", ocList);
				
				mv.addObject("accInfo", hwAcc);
				mv.addObject("payMoney", order.getApplyAmount()+"$");
				mv.addObject("task", task);
				mv.setViewName("task/plProTaskAudit");
				break;
			case 64:
				//查询海外账户信息 上传收款凭证
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", task.getOrderId());
				paramMap.put("type", BusConstants.ORDERDETAILS_TYPE_CUSTOMER_HWACC);
				hwAcc = orderService.getOrderDetailsByParams(paramMap);
				
				mv.addObject("accInfo", hwAcc);
				mv.addObject("payMoney", order.getApplyAmount()+"$");
				mv.addObject("task", task);
				mv.setViewName("task/proTaskUpload");
				break;
			case 65:
				//查询海外账户信息
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", task.getOrderId());
				paramMap.put("type", BusConstants.ORDERDETAILS_TYPE_CUSTOMER_HWACC);
				hwAcc = orderService.getOrderDetailsByParams(paramMap);
				
				//查询上传图片信息
				paramMap.put("taskType", 64);
				ocList = orderCertService.getOrderCertList(paramMap);
				mv.addObject("ocList", ocList);
				
				mv.addObject("accInfo", hwAcc);
				mv.addObject("payMoney", order.getApplyAmount()+"$");
				mv.addObject("task", task);
				mv.setViewName("task/plProTaskAudit");
				break;
			case 66:
				//查询海外账户信息 可重新上传收款凭证
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", task.getOrderId());
				paramMap.put("type", BusConstants.ORDERDETAILS_TYPE_CUSTOMER_HWACC);
				hwAcc = orderService.getOrderDetailsByParams(paramMap);
				
				paramMap.put("taskType", 64);
				ocList = orderCertService.getOrderCertList(paramMap);
				mv.addObject("ocList", ocList);
				
				mv.addObject("accInfo", hwAcc);
				mv.addObject("payMoney", order.getApplyAmount()+"$");
				mv.addObject("task", task);
				mv.setViewName("task/proTaskReUpload");
				break;
			case 67:
				//查询海外账户信息
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", task.getOrderId());
				paramMap.put("type", BusConstants.ORDERDETAILS_TYPE_CUSTOMER_HWACC);
				hwAcc = orderService.getOrderDetailsByParams(paramMap);
				
				//查询上传图片信息
				paramMap.put("taskType", 64);
				ocList = orderCertService.getOrderCertList(paramMap);
				mv.addObject("ocList", ocList);
				
				mv.addObject("accInfo", hwAcc);
				mv.addObject("payMoney", order.getApplyAmount()+"$");
				mv.addObject("task", task);
				mv.setViewName("task/plProTaskAudit");
				break;
			}
			
			return mv;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("待办处理页面异常!");
		}
	}
	
	@ResponseBody
	@RequestMapping(value = "/plpProTaskT1")
	public Map<String, Object> plpProTaskT1(HttpServletRequest request, String taskId, String accountId){
		Map<String,Object> map = new HashMap<String,Object>();
		try {
			//验证海外用户真实性
			AccountFr accountFr = accountFrService.getAccountFrById(accountId);
			if(accountFr == null){
				map.put(GlobalPara.AJAX_KEY, "你选择的海外用户系统不存在");
				return map;
			}
			
			//获取客户用户名userId
			User user = (User) request.getSession().getAttribute(GlobalPara.USER_SESSION_TOKEN);
			
			//获取当前待办
			Task curTask = taskService.getTaskById(taskId);
			curTask.setStatus(BusConstants.TASK_STATUS_PROCESSED);
			curTask.setEndTime(new Date());
			
			if(BusConstants.TASK_TYPE_HWUSERINFO_REJECT.equals(curTask.getTaskType())){
				Map<String,Object> paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", curTask.getOrderId());
				paramMap.put("type", BusConstants.ORDERDETAILS_TYPE_HWUSER_LOCACC);
				OrderDetails orderDetails = orderService.getOrderDetailsByParams(paramMap);
				orderDetails.setAccountBank(accountFr.getAccountBank());
				orderDetails.setAccountName(accountFr.getAccountName());
				orderDetails.setAccountCode(accountFr.getAccountCode());
				orderDetails.setOrderId(curTask.getOrderId());
				orderDetails.setTaskType(curTask.getTaskType());
				orderDetails.setType(BusConstants.ORDERDETAILS_TYPE_HWUSER_LOCACC);
				
				//生成新的待办任务
				Task newTask = taskService.initNewTask(curTask.getOrderId(), BusConstants.TASK_TYPE_HWUSERINFO);
				newTask.setOrderStatus(curTask.getOrderStatus());
				
				//生成操作步骤
				OrderOperate oOperate = new OrderOperate();
				oOperate.setId(PKIDUtils.getUuid());
				oOperate.setOrderId(curTask.getOrderId());
				oOperate.setStatus(curTask.getOrderStatus());
				oOperate.setOperatedId(user.getId());
				oOperate.setOpEndTime(new Date());
				oOperate.setOpSequence(curTask.getTaskType());
				oOperate.setTaskId(curTask.getId());
				oOperate.setOrderAccountId(orderDetails.getId());
				
				taskService.processTask(null, orderDetails, oOperate, curTask, newTask);
			}else{
				//生成新的订单账户
				OrderDetails orderDetails = new OrderDetails();
				orderDetails.setId(PKIDUtils.getUuid());
				orderDetails.setAccountBank(accountFr.getAccountBank());
				orderDetails.setAccountName(accountFr.getAccountName());
				orderDetails.setAccountCode(accountFr.getAccountCode());
				orderDetails.setOrderId(curTask.getOrderId());
				orderDetails.setTaskType(curTask.getTaskType());
				orderDetails.setType(BusConstants.ORDERDETAILS_TYPE_HWUSER_LOCACC);
				
				//查询当前订单信息 修改其状态为处理中
				Order order = orderService.getOrderById(curTask.getOrderId());
				order.setStatus(BusConstants.ORDER_STATUS_PROCESSING);
				order.setHwUserId(accountFr.getUserId());
				
				//生成新的待办任务
				Task newTask = taskService.initNewTask(curTask.getOrderId(), BusConstants.TASK_TYPE_HWUSERINFO);
				newTask.setOrderStatus(order.getStatus());
				
				//生成操作步骤
				OrderOperate oOperate = new OrderOperate();
				oOperate.setId(PKIDUtils.getUuid());
				oOperate.setOrderId(curTask.getOrderId());
				oOperate.setStatus(curTask.getOrderStatus());
				oOperate.setOperatedId(user.getId());
				oOperate.setOpEndTime(new Date());
				oOperate.setOpSequence(curTask.getTaskType());
				oOperate.setTaskId(curTask.getId());
				oOperate.setOrderAccountId(orderDetails.getId());
				
				taskService.processTask(order, orderDetails, oOperate, curTask, newTask);
			}
			
			map.put(GlobalPara.AJAX_KEY, GlobalPara.AJAX_SUCCESS);
			return map;
		} catch (Exception e) {
			e.printStackTrace();
			map = new HashMap<String,Object>();
			map.put(GlobalPara.AJAX_KEY, "待办处理异常，请重试！");
			return map;
		}
	}
	
	//华创第一次付款需要维护其此订单的国内国外账户
	@ResponseBody
	@RequestMapping(value = "/hcAddAccInfo")
	public Map<String, Object> hcAddAccountInfo(HttpServletRequest request, String taskId, OrderDetails orderDetails){
		Map<String,Object> map = new HashMap<String,Object>();
		try {
			//获取当前待办
			Task curTask = taskService.getTaskById(taskId);
			//根据类型获取OrderDetails
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("orderId", curTask.getOrderId());
			params.put("type", orderDetails.getType());
			OrderDetails curDetailsByType = orderService.getOrderDetailsByParams(params);
			
			if(curDetailsByType != null){
				curDetailsByType.setAccountName(orderDetails.getAccountName());
				curDetailsByType.setAccountCode(orderDetails.getAccountCode());
				curDetailsByType.setAccountBank(orderDetails.getAccountBank());
				
				orderService.updateOrderDetails(curDetailsByType);
			}else{
				orderDetails.setId(PKIDUtils.getUuid());
				orderDetails.setOrderId(curTask.getOrderId());
				
				orderService.insertOrderDetails(orderDetails);
			}
			
			//查询账户信息
			params = new HashMap<String,Object>();
			params.put("orderId", curTask.getOrderId());
			params.put("type", BusConstants.ORDERDETAILS_TYPE_HC_LOCACC);
			OrderDetails hcT3 = orderService.getOrderDetailsByParams(params);
			
			params = new HashMap<String,Object>();
			params.put("orderId", curTask.getOrderId());
			params.put("type", BusConstants.ORDERDETAILS_TYPE_HC_HWACC);
			OrderDetails hcT4 = orderService.getOrderDetailsByParams(params);
			
			map.put("hcT3", hcT3);
			map.put("hcT4", hcT4);
			map.put(GlobalPara.AJAX_KEY, GlobalPara.AJAX_SUCCESS);
			return map;
		} catch (Exception e) {
			e.printStackTrace();
			map = new HashMap<String,Object>();
			map.put(GlobalPara.AJAX_KEY, "保存账户信息异常，请重试！");
			return map;
		}
	}
	
	//上传点击确定
	@ResponseBody
	@RequestMapping(value = "/uploadConfirm")
	public Map<String, Object> uploadConfirm(HttpServletRequest request, String taskId) {
		Map<String,Object> map = new HashMap<String,Object>();
	
		boolean isUpLoad = true;
		try {
			isUpLoad = uploadImg(request,taskId);
			if(!isUpLoad){
				map.put(GlobalPara.AJAX_KEY, "上传凭证失败，请重试！");
			}
		} catch (Exception e) {
			// TODO: handle exception
			isUpLoad = false;
			map.put(GlobalPara.AJAX_KEY, "上传凭证失败，请重试！");
		}
		//判断凭证是否已经上传成功
		if(isUpLoad){
			try {
				//获取客户用户名userId
				User user = (User) request.getSession().getAttribute(GlobalPara.USER_SESSION_TOKEN);
				
				//获取当前待办
				Task curTask = taskService.getTaskById(taskId);
				curTask.setStatus(BusConstants.TASK_STATUS_PROCESSED);
				curTask.setEndTime(new Date());
				
				Task newTask = null;
				switch (curTask.getTaskType()) {
				case 4:
					//生成新的待办任务至平台操作员
					newTask = taskService.initNewTask(curTask.getOrderId(), BusConstants.TASK_TYPE_CONFIRM_FIRST_HCPAY);
					newTask.setOrderStatus(curTask.getOrderStatus());
					break;
				case 6:
					//生成新的待办任务至平台操作员
					newTask = taskService.initNewTask(curTask.getOrderId(), BusConstants.TASK_TYPE_CONFIRM_FIRST_HCPAY);
					newTask.setOrderStatus(curTask.getOrderStatus());
					break;
				case 8:
					//生成新的待办任务至平台操作员
					newTask = taskService.initNewTask(curTask.getOrderId(), BusConstants.TASK_TYPE_FIRST_MONEYRECEIPT_VOUCHER);
					newTask.setOrderStatus(curTask.getOrderStatus());
					break;
				case 10:
					//生成新的待办任务至平台操作员
					newTask = taskService.initNewTask(curTask.getOrderId(), BusConstants.TASK_TYPE_FIRST_MONEYRECEIPT_VOUCHER);
					newTask.setOrderStatus(curTask.getOrderStatus());
					break;
				case 12:
					//生成新的待办任务至平台操作员
					newTask = taskService.initNewTask(curTask.getOrderId(), BusConstants.TASK_TYPE_HWJPAY_CONFIRM);
					newTask.setOrderStatus(curTask.getOrderStatus());
					break;
				case 16:
					//生成新的待办任务至平台操作员
					newTask = taskService.initNewTask(curTask.getOrderId(), BusConstants.TASK_TYPE_HWJPAY_HCRECEIPT_VOUCHER);
					newTask.setOrderStatus(curTask.getOrderStatus());
					break;
				case 20:
					//生成新的待办任务至平台操作员
					newTask = taskService.initNewTask(curTask.getOrderId(), BusConstants.TASK_TYPE_SECOND_HCPAY_CONFIRM);
					newTask.setOrderStatus(curTask.getOrderStatus());
					break;
				case 24:
					//生成新的待办任务至平台操作员
					newTask = taskService.initNewTask(curTask.getOrderId(), BusConstants.TASK_TYPE_SECOND_HCPAY_RECEIPT_VOUCHER);
					newTask.setOrderStatus(curTask.getOrderStatus());
					break;
				case 28:
					//生成新的待办任务至平台操作员
					newTask = taskService.initNewTask(curTask.getOrderId(), BusConstants.TASK_TYPE_FIRST_HWUSERPAY_CONFIRM);
					newTask.setOrderStatus(curTask.getOrderStatus());
					break;
				case 32:
					//生成新的待办任务至平台操作员
					newTask = taskService.initNewTask(curTask.getOrderId(), BusConstants.TASK_TYPE_FIRST_HCRECEIPT_CONFIRM);
					newTask.setOrderStatus(curTask.getOrderStatus());
					break;
				case 36:
					//生成新的待办任务至平台操作员
					newTask = taskService.initNewTask(curTask.getOrderId(), BusConstants.TASK_TYPE_HC_HWPAY_CONFIRM);
					newTask.setOrderStatus(curTask.getOrderStatus());
					break;
				case 40:
					//生成新的待办任务至平台操作员
					newTask = taskService.initNewTask(curTask.getOrderId(), BusConstants.TASK_TYPE_HWJ_HWRECEIPT_CONFIRM);
					newTask.setOrderStatus(curTask.getOrderStatus());
					break;
				case 44:
					//生成新的待办任务至平台操作员
					newTask = taskService.initNewTask(curTask.getOrderId(), BusConstants.TASK_TYPE_SECOND_HWUSERPAY_CONFIRM);
					newTask.setOrderStatus(curTask.getOrderStatus());
					break;
				case 48:
					//生成新的待办任务至平台操作员
					newTask = taskService.initNewTask(curTask.getOrderId(), BusConstants.TASK_TYPE_SECOND_HCRECEIPT_CONFIRM);
					newTask.setOrderStatus(curTask.getOrderStatus());
					break;
				case 52:
					//生成新的待办任务至平台操作员
					newTask = taskService.initNewTask(curTask.getOrderId(), BusConstants.TASK_TYPE_CUSTOMER_PAY_CONFIRM);
					newTask.setOrderStatus(curTask.getOrderStatus());
					break;
				case 56:
					//生成新的待办任务至平台操作员
					newTask = taskService.initNewTask(curTask.getOrderId(), BusConstants.TASK_TYPE_HWUSER_RECEIPT_CONFIRM);
					newTask.setOrderStatus(curTask.getOrderStatus());
					break;
				case 60:
					//生成新的待办任务至平台操作员
					newTask = taskService.initNewTask(curTask.getOrderId(), BusConstants.TASK_TYPE_HWUSER_PAY_CONFIRM);
					newTask.setOrderStatus(curTask.getOrderStatus());
					break;
				case 64:
					//生成新的待办任务至平台操作员
					newTask = taskService.initNewTask(curTask.getOrderId(), BusConstants.TASK_TYPE_CUSTOMER_RECEIPT_CONFIRM);
					newTask.setOrderStatus(curTask.getOrderStatus());
					break;
				}

				//生成操作步骤
				OrderOperate oOperate = new OrderOperate();
				oOperate.setId(PKIDUtils.getUuid());
				oOperate.setOrderId(curTask.getOrderId());
				oOperate.setStatus(curTask.getOrderStatus());
				oOperate.setOperatedId(user.getId());
				oOperate.setOpEndTime(new Date());
				oOperate.setOpSequence(curTask.getTaskType());
				oOperate.setTaskId(curTask.getId());
				
				taskService.processTask(null, null, oOperate, curTask, newTask);
			} catch (Exception e) {
				e.printStackTrace();
				map = new HashMap<String,Object>();
				map.put(GlobalPara.AJAX_KEY, "待办处理异常，请重试！");
				
			}
		}
		map.put(GlobalPara.AJAX_KEY, GlobalPara.AJAX_SUCCESS);
		return map;
		
	}
	
	//平台操作员点击确定
	@ResponseBody
	@RequestMapping(value = "/plpProTaskConfirm")
	public Map<String, Object> plpProTaskConfirm(HttpServletRequest request, String taskId){
		Map<String,Object> map = new HashMap<String,Object>();
		try {
			//获取客户用户名userId
			User user = (User) request.getSession().getAttribute(GlobalPara.USER_SESSION_TOKEN);
			
			//获取当前待办
			Task curTask = taskService.getTaskById(taskId);
			curTask.setStatus(BusConstants.TASK_STATUS_PROCESSED);
			curTask.setEndTime(new Date());
			
			Task newTask = null;
			switch (curTask.getTaskType()) {
			case 5:
				//生成新的待办任务
				newTask = taskService.initNewTask(curTask.getOrderId(), BusConstants.TASK_TYPE_FIRST_HCPAY_VOUCHER_CHECK);
				newTask.setOrderStatus(curTask.getOrderStatus());
				break;
			case 9:
				//生成新的待办任务
				newTask = taskService.initNewTask(curTask.getOrderId(), BusConstants.TASK_TYPE_FIRST_MONEYRECEIPT_VOUCHER_REJECT_CHECK);
				newTask.setOrderStatus(curTask.getOrderStatus());
				break;
			case 13:
				//生成新的待办任务
				newTask = taskService.initNewTask(curTask.getOrderId(), BusConstants.TASK_TYPE_HWJPAY_CONFIRM_CHECK);
				newTask.setOrderStatus(curTask.getOrderStatus());
				break;
			case 17:
				//生成新的待办任务
				newTask = taskService.initNewTask(curTask.getOrderId(), BusConstants.TASK_TYPE_HWJPAY_HCRECEIPT_VOUCHER_CHECK);
				newTask.setOrderStatus(curTask.getOrderStatus());
				break;
			case 21:
				//生成新的待办任务
				newTask = taskService.initNewTask(curTask.getOrderId(), BusConstants.TASK_TYPE_SECOND_HCPAY_RECHECK);
				newTask.setOrderStatus(curTask.getOrderStatus());
				break;
			case 25:
				//生成新的待办任务
				newTask = taskService.initNewTask(curTask.getOrderId(), BusConstants.TASK_TYPE_SECOND_HCPAY_RECEIPT_VOUCHER_RECHECK);
				newTask.setOrderStatus(curTask.getOrderStatus());
				break;
			case 29:
				//生成新的待办任务
				newTask = taskService.initNewTask(curTask.getOrderId(), BusConstants.TASK_TYPE_FIRST_HWUSERPAY_RECHECK);
				newTask.setOrderStatus(curTask.getOrderStatus());
				break;
			case 33:
				//生成新的待办任务
				newTask = taskService.initNewTask(curTask.getOrderId(), BusConstants.TASK_TYPE_FIRST_HCRECEIPT_RECHECK);
				newTask.setOrderStatus(curTask.getOrderStatus());
				break;
			case 37:
				//生成新的待办任务
				newTask = taskService.initNewTask(curTask.getOrderId(), BusConstants.TASK_TYPE_HC_HWPAY_RECHECK);
				newTask.setOrderStatus(curTask.getOrderStatus());
				break;
			case 41:
				//生成新的待办任务
				newTask = taskService.initNewTask(curTask.getOrderId(), BusConstants.TASK_TYPE_HWJ_HWRECEIPT_RECHECK);
				newTask.setOrderStatus(curTask.getOrderStatus());
				break;
			case 45:
				//生成新的待办任务
				newTask = taskService.initNewTask(curTask.getOrderId(), BusConstants.TASK_TYPE_SECOND_HWUSERPAY_RECHECK);
				newTask.setOrderStatus(curTask.getOrderStatus());
				break;
			case 49:
				//生成新的待办任务
				newTask = taskService.initNewTask(curTask.getOrderId(), BusConstants.TASK_TYPE_SECOND_HCRECEIPT_RECHECK);
				newTask.setOrderStatus(curTask.getOrderStatus());
				break;
			case 53:
				//生成新的待办任务
				newTask = taskService.initNewTask(curTask.getOrderId(), BusConstants.TASK_TYPE_CUSTOMER_PAY_RECHECK);
				newTask.setOrderStatus(curTask.getOrderStatus());
				break;
			case 57:
				//生成新的待办任务
				newTask = taskService.initNewTask(curTask.getOrderId(), BusConstants.TASK_TYPE_HWUSER_RECEIPT_RECHECK);
				newTask.setOrderStatus(curTask.getOrderStatus());
				break;
			case 61:
				//生成新的待办任务
				newTask = taskService.initNewTask(curTask.getOrderId(), BusConstants.TASK_TYPE_HWUSER_PAY_RECHECK);
				newTask.setOrderStatus(curTask.getOrderStatus());
				break;
			case 65:
				//生成新的待办任务
				newTask = taskService.initNewTask(curTask.getOrderId(), BusConstants.TASK_TYPE_CUSTOMER_RECEIPT_RECHECK);
				newTask.setOrderStatus(curTask.getOrderStatus());
				break;
			}
			
			//生成下一环节待办
			Order order = null;
			String applyId = null;
			List<Role> roles = null;
			switch (curTask.getTaskType()) {
			case 2:
				applyId = orderService.getOrderById(curTask.getOrderId()).getApplyId();
				roles = roleService.getRoleListByUserId(applyId);
				for (Role role : roles) {
					if(GlobalPara.RNAME_HWJ_OPERATOR.equals(role.getRoleName())){
						newTask = taskService.initNewTask(curTask.getOrderId(), BusConstants.TASK_TYPE_FIRST_HCPAY);
						newTask.setOrderStatus(curTask.getOrderStatus());
					}
					if(GlobalPara.RNAME_CN_OTHER_CUSTOMER.equals(role.getRoleName())){
						newTask = taskService.initNewTask(curTask.getOrderId(), BusConstants.TASK_TYPE_CUSTOMER_PAY);
						newTask.setOrderStatus(curTask.getOrderStatus());
					}
				}
				break;
			case 7:
				//生成新的待办任务
				newTask = taskService.initNewTask(curTask.getOrderId(), BusConstants.TASK_TYPE_FIRST_MONEYRECEIPT);
				newTask.setOrderStatus(curTask.getOrderStatus());
				//角色为海外用户 需指定到人
				order = orderService.getOrderById(curTask.getOrderId());
				newTask.setOperatedId(order.getHwUserId());
				break;
			case 11:
				//生成新的待办任务
				newTask = taskService.initNewTask(curTask.getOrderId(), BusConstants.TASK_TYPE_HWJPAY);
				newTask.setOrderStatus(curTask.getOrderStatus());
				break;
			case 15:
				//生成新的待办任务
				newTask = taskService.initNewTask(curTask.getOrderId(), BusConstants.TASK_TYPE_HWJPAY_HCRECEIPT_CONFIRM);
				newTask.setOrderStatus(curTask.getOrderStatus());
				break;
			case 19:
				//生成新的待办任务
				newTask = taskService.initNewTask(curTask.getOrderId(), BusConstants.TASK_TYPE_SECOND_HCPAY);
				newTask.setOrderStatus(curTask.getOrderStatus());
				break;
			case 23:
				//生成新的待办任务
				newTask = taskService.initNewTask(curTask.getOrderId(), BusConstants.TASK_TYPE_SECOND_HCPAY_RECEIPT_CONFIRM);
				newTask.setOrderStatus(curTask.getOrderStatus());
				//角色为海外用户 需指定到人
				order = orderService.getOrderById(curTask.getOrderId());
				newTask.setOperatedId(order.getHwUserId());
				break;
			case 27:
				//生成新的待办任务
				newTask = taskService.initNewTask(curTask.getOrderId(), BusConstants.TASK_TYPE_FIRST_HWUSERPAY);
				newTask.setOrderStatus(curTask.getOrderStatus());
				//角色为海外用户 需指定到人
				order = orderService.getOrderById(curTask.getOrderId());
				newTask.setOperatedId(order.getHwUserId());
				break;
			case 31:
				//生成新的待办任务
				newTask = taskService.initNewTask(curTask.getOrderId(), BusConstants.TASK_TYPE_FIRST_HCRECEIPT);
				newTask.setOrderStatus(curTask.getOrderStatus());
				break;
			case 35:
				//生成新的待办任务
				newTask = taskService.initNewTask(curTask.getOrderId(), BusConstants.TASK_TYPE_HC_HWPAY);
				newTask.setOrderStatus(curTask.getOrderStatus());
				break;
			case 39:
				//生成新的待办任务
				newTask = taskService.initNewTask(curTask.getOrderId(), BusConstants.TASK_TYPE_HWJ_HWRECEIPT);
				newTask.setOrderStatus(curTask.getOrderStatus());
				break;
			case 43:
				//生成新的待办任务
				newTask = taskService.initNewTask(curTask.getOrderId(), BusConstants.TASK_TYPE_SECOND_HWUSERPAY);
				newTask.setOrderStatus(curTask.getOrderStatus());
				break;
			case 47:
				//生成新的待办任务
				newTask = taskService.initNewTask(curTask.getOrderId(), BusConstants.TASK_TYPE_SECOND_HCRECEIPT);
				newTask.setOrderStatus(curTask.getOrderStatus());
				break;
			case 51:
				//更新订单状态
				order = orderService.getOrderById(curTask.getOrderId());
				order.setModifiedDate(new Date());
				order.setModifiedId(user.getId());
				order.setStatus(BusConstants.ORDER_STATUS_COMPLETED);
				break;
			case 55:
				//生成新的待办任务
				newTask = taskService.initNewTask(curTask.getOrderId(), BusConstants.TASK_TYPE_HWUSER_RECEIPT);
				newTask.setOrderStatus(curTask.getOrderStatus());
				break;
			case 59:
				//生成新的待办任务
				newTask = taskService.initNewTask(curTask.getOrderId(), BusConstants.TASK_TYPE_HWUSER_PAY);
				newTask.setOrderStatus(curTask.getOrderStatus());
				//角色为海外用户 需指定到人
				order = orderService.getOrderById(curTask.getOrderId());
				newTask.setOperatedId(order.getHwUserId());
				break;
			case 63:
				//生成新的待办任务
				newTask = taskService.initNewTask(curTask.getOrderId(), BusConstants.TASK_TYPE_CUSTOMER_RECEIPT);
				newTask.setOrderStatus(curTask.getOrderStatus());
				break;
			case 67:
				//更新订单状态
				order = orderService.getOrderById(curTask.getOrderId());
				order.setModifiedDate(new Date());
				order.setModifiedId(user.getId());
				order.setStatus(BusConstants.ORDER_STATUS_COMPLETED);
				break;
			}
			 
			//生成操作步骤
			OrderOperate oOperate = new OrderOperate();
			oOperate.setId(PKIDUtils.getUuid());
			oOperate.setOrderId(curTask.getOrderId());
			oOperate.setStatus(curTask.getOrderStatus());
			oOperate.setOperatedId(user.getId());
			oOperate.setOpEndTime(new Date());
			oOperate.setOpSequence(curTask.getTaskType());
			oOperate.setTaskId(curTask.getId());
			
			if(BusConstants.TASK_TYPE_SECOND_HCRECEIPT_RECHECK.equals(curTask.getTaskType()) || BusConstants.TASK_TYPE_CUSTOMER_RECEIPT_RECHECK.equals(curTask.getTaskType())){
				taskService.processTask(order, null, oOperate, curTask, null);
			}else{
				taskService.processTask(null, null, oOperate, curTask, newTask);
			}
			
			map.put(GlobalPara.AJAX_KEY, GlobalPara.AJAX_SUCCESS);
			return map;
		} catch (Exception e) {
			e.printStackTrace();
			map = new HashMap<String,Object>();
			map.put(GlobalPara.AJAX_KEY, "平台操作员待办处理异常，请重试！");
			return map;
		}
	}
	
	//平台操作员点击驳回
	@ResponseBody
	@RequestMapping(value = "/plpProTaskRejected")
	public Map<String, Object> plpProTaskRejected(HttpServletRequest request, String taskId, String remark){
		Map<String,Object> map = new HashMap<String,Object>();
		try {
			//获取客户用户名userId
			User user = (User) request.getSession().getAttribute(GlobalPara.USER_SESSION_TOKEN);
			
			//获取当前待办
			Task curTask = taskService.getTaskById(taskId);
			curTask.setStatus(BusConstants.TASK_STATUS_PROCESSED);
			curTask.setEndTime(new Date());
			curTask.setRemark(remark);
			
			Task newTask = null;
			Map<String,Object> paramMap = null;
			Task upTask = null;
			
			switch (curTask.getTaskType()) {
			case 5:
				//生成新的待办任务
				newTask = taskService.initNewTask(curTask.getOrderId(), BusConstants.TASK_TYPE_FIRST_HCPAY_VOUCHER_REJECT);
				newTask.setOrderStatus(curTask.getOrderStatus());
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", curTask.getOrderId());
				paramMap.put("taskType", BusConstants.TASK_TYPE_FIRST_HCPAY);
				upTask = taskService.getTaskByParams(paramMap);
				newTask.setOperatedId(upTask.getOperatedId());
				break;
			case 9:
				//生成新的待办任务
				newTask = taskService.initNewTask(curTask.getOrderId(), BusConstants.TASK_TYPE_FIRST_MONEYRECEIPT_VOUCHER_REJECT);
				newTask.setOrderStatus(curTask.getOrderStatus());
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", curTask.getOrderId());
				paramMap.put("taskType", BusConstants.TASK_TYPE_FIRST_MONEYRECEIPT);
				upTask = taskService.getTaskByParams(paramMap);
				newTask.setOperatedId(upTask.getOperatedId());
				break;
			case 13:
				//生成新的待办任务
				newTask = taskService.initNewTask(curTask.getOrderId(), BusConstants.TASK_TYPE_HWJPAY_CONFIRM_REJECT);
				newTask.setOrderStatus(curTask.getOrderStatus());
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", curTask.getOrderId());
				paramMap.put("taskType", BusConstants.TASK_TYPE_HWJPAY);
				upTask = taskService.getTaskByParams(paramMap);
				newTask.setOperatedId(upTask.getOperatedId());
				break;
			case 17:
				//生成新的待办任务
				newTask = taskService.initNewTask(curTask.getOrderId(), BusConstants.TASK_TYPE_HWJPAY_HCRECEIPT_VOUCHER_REJECT);
				newTask.setOrderStatus(curTask.getOrderStatus());
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", curTask.getOrderId());
				paramMap.put("taskType", BusConstants.TASK_TYPE_HWJPAY_HCRECEIPT_CONFIRM);
				upTask = taskService.getTaskByParams(paramMap);
				newTask.setOperatedId(upTask.getOperatedId());
				break;
			case 21:
				//生成新的待办任务
				newTask = taskService.initNewTask(curTask.getOrderId(), BusConstants.TASK_TYPE_SECOND_HCPAY_CONFIRM_REJECT);
				newTask.setOrderStatus(curTask.getOrderStatus());
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", curTask.getOrderId());
				paramMap.put("taskType", BusConstants.TASK_TYPE_SECOND_HCPAY);
				upTask = taskService.getTaskByParams(paramMap);
				newTask.setOperatedId(upTask.getOperatedId());
				break;
			case 25:
				//生成新的待办任务
				newTask = taskService.initNewTask(curTask.getOrderId(), BusConstants.TASK_TYPE_SECOND_HCPAY_RECEIPT_VOUCHER_REJECT);
				newTask.setOrderStatus(curTask.getOrderStatus());
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", curTask.getOrderId());
				paramMap.put("taskType", BusConstants.TASK_TYPE_SECOND_HCPAY_RECEIPT_CONFIRM);
				upTask = taskService.getTaskByParams(paramMap);
				newTask.setOperatedId(upTask.getOperatedId());
				break;
			case 29:
				//生成新的待办任务
				newTask = taskService.initNewTask(curTask.getOrderId(), BusConstants.TASK_TYPE_FIRST_HWUSERPAY_REJECT);
				newTask.setOrderStatus(curTask.getOrderStatus());
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", curTask.getOrderId());
				paramMap.put("taskType", BusConstants.TASK_TYPE_FIRST_HWUSERPAY);
				upTask = taskService.getTaskByParams(paramMap);
				newTask.setOperatedId(upTask.getOperatedId());
				break;
			case 33:
				//生成新的待办任务
				newTask = taskService.initNewTask(curTask.getOrderId(), BusConstants.TASK_TYPE_FIRST_HCRECEIPT_REJECT);
				newTask.setOrderStatus(curTask.getOrderStatus());
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", curTask.getOrderId());
				paramMap.put("taskType", BusConstants.TASK_TYPE_FIRST_HCRECEIPT);
				upTask = taskService.getTaskByParams(paramMap);
				newTask.setOperatedId(upTask.getOperatedId());
				break;
			case 37:
				//生成新的待办任务
				newTask = taskService.initNewTask(curTask.getOrderId(), BusConstants.TASK_TYPE_HC_HWPAY_REJECT);
				newTask.setOrderStatus(curTask.getOrderStatus());
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", curTask.getOrderId());
				paramMap.put("taskType", BusConstants.TASK_TYPE_HC_HWPAY);
				upTask = taskService.getTaskByParams(paramMap);
				newTask.setOperatedId(upTask.getOperatedId());
				break;
			case 41:
				//生成新的待办任务
				newTask = taskService.initNewTask(curTask.getOrderId(), BusConstants.TASK_TYPE_HWJ_HWRECEIPT_REJECT);
				newTask.setOrderStatus(curTask.getOrderStatus());
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", curTask.getOrderId());
				paramMap.put("taskType", BusConstants.TASK_TYPE_HWJ_HWRECEIPT);
				upTask = taskService.getTaskByParams(paramMap);
				newTask.setOperatedId(upTask.getOperatedId());
				break;
			case 45:
				//生成新的待办任务
				newTask = taskService.initNewTask(curTask.getOrderId(), BusConstants.TASK_TYPE_SECOND_HWUSERPAY_REJECT);
				newTask.setOrderStatus(curTask.getOrderStatus());
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", curTask.getOrderId());
				paramMap.put("taskType", BusConstants.TASK_TYPE_SECOND_HWUSERPAY);
				upTask = taskService.getTaskByParams(paramMap);
				newTask.setOperatedId(upTask.getOperatedId());
				break;
			case 49:
				//生成新的待办任务
				newTask = taskService.initNewTask(curTask.getOrderId(), BusConstants.TASK_TYPE_SECOND_HCRECEIPT_REJECT);
				newTask.setOrderStatus(curTask.getOrderStatus());
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", curTask.getOrderId());
				paramMap.put("taskType", BusConstants.TASK_TYPE_SECOND_HCRECEIPT);
				upTask = taskService.getTaskByParams(paramMap);
				newTask.setOperatedId(upTask.getOperatedId());
				break;
			case 53:
				//生成新的待办任务
				newTask = taskService.initNewTask(curTask.getOrderId(), BusConstants.TASK_TYPE_CUSTOMER_PAY_REJECT);
				newTask.setOrderStatus(curTask.getOrderStatus());
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", curTask.getOrderId());
				paramMap.put("taskType", BusConstants.TASK_TYPE_CUSTOMER_PAY);
				upTask = taskService.getTaskByParams(paramMap);
				newTask.setOperatedId(upTask.getOperatedId());
				break;
			case 57:
				//生成新的待办任务
				newTask = taskService.initNewTask(curTask.getOrderId(), BusConstants.TASK_TYPE_HWUSER_RECEIPT_REJECT);
				newTask.setOrderStatus(curTask.getOrderStatus());
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", curTask.getOrderId());
				paramMap.put("taskType", BusConstants.TASK_TYPE_HWUSER_RECEIPT);
				upTask = taskService.getTaskByParams(paramMap);
				newTask.setOperatedId(upTask.getOperatedId());
				break;
			case 61:
				//生成新的待办任务
				newTask = taskService.initNewTask(curTask.getOrderId(), BusConstants.TASK_TYPE_HWUSER_PAY_REJECT);
				newTask.setOrderStatus(curTask.getOrderStatus());
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", curTask.getOrderId());
				paramMap.put("taskType", BusConstants.TASK_TYPE_HWUSER_PAY);
				upTask = taskService.getTaskByParams(paramMap);
				newTask.setOperatedId(upTask.getOperatedId());
				break;
			case 65:
				//生成新的待办任务
				newTask = taskService.initNewTask(curTask.getOrderId(), BusConstants.TASK_TYPE_CUSTOMER_RECEIPT_REJECT);
				newTask.setOrderStatus(curTask.getOrderStatus());
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", curTask.getOrderId());
				paramMap.put("taskType", BusConstants.TASK_TYPE_CUSTOMER_RECEIPT);
				upTask = taskService.getTaskByParams(paramMap);
				newTask.setOperatedId(upTask.getOperatedId());
				break;
			}
			
			//生成上一环节待办
			switch (curTask.getTaskType()) {
			case 2:
				newTask = taskService.initNewTask(curTask.getOrderId(), BusConstants.TASK_TYPE_HWUSERINFO_REJECT);
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", curTask.getOrderId());
				paramMap.put("taskType", BusConstants.TASK_TYPE_COMMIT);
				upTask = taskService.getTaskByParams(paramMap);
				newTask.setOperatedId(upTask.getOperatedId());
				break;
			case 7:
				//生成新的待办任务
				newTask = taskService.initNewTask(curTask.getOrderId(), BusConstants.TASK_TYPE_FIRST_HCPAY_VOUCHER_REJECT);
				newTask.setOrderStatus(curTask.getOrderStatus());
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", curTask.getOrderId());
				paramMap.put("taskType", BusConstants.TASK_TYPE_FIRST_HCPAY);
				upTask = taskService.getTaskByParams(paramMap);
				newTask.setOperatedId(upTask.getOperatedId());
				break;
			case 11:
				//生成新的待办任务
				newTask = taskService.initNewTask(curTask.getOrderId(), BusConstants.TASK_TYPE_FIRST_MONEYRECEIPT_VOUCHER_REJECT);
				newTask.setOrderStatus(curTask.getOrderStatus());
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", curTask.getOrderId());
				paramMap.put("taskType", BusConstants.TASK_TYPE_FIRST_MONEYRECEIPT);
				upTask = taskService.getTaskByParams(paramMap);
				newTask.setOperatedId(upTask.getOperatedId());
				break;
			case 15:
				//生成新的待办任务
				newTask = taskService.initNewTask(curTask.getOrderId(), BusConstants.TASK_TYPE_HWJPAY_CONFIRM_REJECT);
				newTask.setOrderStatus(curTask.getOrderStatus());
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", curTask.getOrderId());
				paramMap.put("taskType", BusConstants.TASK_TYPE_HWJPAY);
				upTask = taskService.getTaskByParams(paramMap);
				newTask.setOperatedId(upTask.getOperatedId());
				break;
			case 19:
				//生成新的待办任务
				newTask = taskService.initNewTask(curTask.getOrderId(), BusConstants.TASK_TYPE_HWJPAY_HCRECEIPT_VOUCHER_REJECT);
				newTask.setOrderStatus(curTask.getOrderStatus());
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", curTask.getOrderId());
				paramMap.put("taskType", BusConstants.TASK_TYPE_HWJPAY_HCRECEIPT_CONFIRM);
				upTask = taskService.getTaskByParams(paramMap);
				newTask.setOperatedId(upTask.getOperatedId());
				break;
			case 23:
				//生成新的待办任务
				newTask = taskService.initNewTask(curTask.getOrderId(), BusConstants.TASK_TYPE_SECOND_HCPAY_CONFIRM_REJECT);
				newTask.setOrderStatus(curTask.getOrderStatus());
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", curTask.getOrderId());
				paramMap.put("taskType", BusConstants.TASK_TYPE_SECOND_HCPAY);
				upTask = taskService.getTaskByParams(paramMap);
				newTask.setOperatedId(upTask.getOperatedId());
				break;
			case 27:
				//生成新的待办任务
				newTask = taskService.initNewTask(curTask.getOrderId(), BusConstants.TASK_TYPE_SECOND_HCPAY_RECEIPT_VOUCHER_REJECT);
				newTask.setOrderStatus(curTask.getOrderStatus());
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", curTask.getOrderId());
				paramMap.put("taskType", BusConstants.TASK_TYPE_SECOND_HCPAY_RECEIPT_CONFIRM);
				upTask = taskService.getTaskByParams(paramMap);
				newTask.setOperatedId(upTask.getOperatedId());
				break;
			case 31:
				//生成新的待办任务
				newTask = taskService.initNewTask(curTask.getOrderId(), BusConstants.TASK_TYPE_FIRST_HWUSERPAY_REJECT);
				newTask.setOrderStatus(curTask.getOrderStatus());
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", curTask.getOrderId());
				paramMap.put("taskType", BusConstants.TASK_TYPE_FIRST_HWUSERPAY);
				upTask = taskService.getTaskByParams(paramMap);
				newTask.setOperatedId(upTask.getOperatedId());
				break;
			case 35:
				//生成新的待办任务
				newTask = taskService.initNewTask(curTask.getOrderId(), BusConstants.TASK_TYPE_FIRST_HCRECEIPT_REJECT);
				newTask.setOrderStatus(curTask.getOrderStatus());
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", curTask.getOrderId());
				paramMap.put("taskType", BusConstants.TASK_TYPE_FIRST_HCRECEIPT);
				upTask = taskService.getTaskByParams(paramMap);
				newTask.setOperatedId(upTask.getOperatedId());
				break;
			case 39:
				//生成新的待办任务
				newTask = taskService.initNewTask(curTask.getOrderId(), BusConstants.TASK_TYPE_HC_HWPAY_REJECT);
				newTask.setOrderStatus(curTask.getOrderStatus());
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", curTask.getOrderId());
				paramMap.put("taskType", BusConstants.TASK_TYPE_HC_HWPAY);
				upTask = taskService.getTaskByParams(paramMap);
				newTask.setOperatedId(upTask.getOperatedId());
				break;
			case 43:
				//生成新的待办任务
				newTask = taskService.initNewTask(curTask.getOrderId(), BusConstants.TASK_TYPE_HWJ_HWRECEIPT_REJECT);
				newTask.setOrderStatus(curTask.getOrderStatus());
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", curTask.getOrderId());
				paramMap.put("taskType", BusConstants.TASK_TYPE_HWJ_HWRECEIPT);
				upTask = taskService.getTaskByParams(paramMap);
				newTask.setOperatedId(upTask.getOperatedId());
				break;
			case 47:
				//生成新的待办任务
				newTask = taskService.initNewTask(curTask.getOrderId(), BusConstants.TASK_TYPE_SECOND_HWUSERPAY_REJECT);
				newTask.setOrderStatus(curTask.getOrderStatus());
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", curTask.getOrderId());
				paramMap.put("taskType", BusConstants.TASK_TYPE_SECOND_HWUSERPAY);
				upTask = taskService.getTaskByParams(paramMap);
				newTask.setOperatedId(upTask.getOperatedId());
				break;
			case 51:
				//生成新的待办任务
				newTask = taskService.initNewTask(curTask.getOrderId(), BusConstants.TASK_TYPE_SECOND_HCRECEIPT_REJECT);
				newTask.setOrderStatus(curTask.getOrderStatus());
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", curTask.getOrderId());
				paramMap.put("taskType", BusConstants.TASK_TYPE_SECOND_HCRECEIPT);
				upTask = taskService.getTaskByParams(paramMap);
				newTask.setOperatedId(upTask.getOperatedId());
				break;
			case 55:
				//生成新的待办任务
				newTask = taskService.initNewTask(curTask.getOrderId(), BusConstants.TASK_TYPE_CUSTOMER_PAY_REJECT);
				newTask.setOrderStatus(curTask.getOrderStatus());
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", curTask.getOrderId());
				paramMap.put("taskType", BusConstants.TASK_TYPE_CUSTOMER_PAY);
				upTask = taskService.getTaskByParams(paramMap);
				newTask.setOperatedId(upTask.getOperatedId());
				break;
			case 59:
				//生成新的待办任务
				newTask = taskService.initNewTask(curTask.getOrderId(), BusConstants.TASK_TYPE_HWUSER_RECEIPT_REJECT);
				newTask.setOrderStatus(curTask.getOrderStatus());
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", curTask.getOrderId());
				paramMap.put("taskType", BusConstants.TASK_TYPE_HWUSER_RECEIPT);
				upTask = taskService.getTaskByParams(paramMap);
				newTask.setOperatedId(upTask.getOperatedId());
				break;
			case 63:
				//生成新的待办任务
				newTask = taskService.initNewTask(curTask.getOrderId(), BusConstants.TASK_TYPE_HWUSER_PAY_REJECT);
				newTask.setOrderStatus(curTask.getOrderStatus());
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", curTask.getOrderId());
				paramMap.put("taskType", BusConstants.TASK_TYPE_HWUSER_PAY);
				upTask = taskService.getTaskByParams(paramMap);
				newTask.setOperatedId(upTask.getOperatedId());
				break;
			case 67:
				//生成新的待办任务
				newTask = taskService.initNewTask(curTask.getOrderId(), BusConstants.TASK_TYPE_CUSTOMER_RECEIPT_REJECT);
				newTask.setOrderStatus(curTask.getOrderStatus());
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", curTask.getOrderId());
				paramMap.put("taskType", BusConstants.TASK_TYPE_CUSTOMER_RECEIPT);
				upTask = taskService.getTaskByParams(paramMap);
				newTask.setOperatedId(upTask.getOperatedId());
				break;
			}
			
			//生成操作步骤
			OrderOperate oOperate = new OrderOperate();
			oOperate.setId(PKIDUtils.getUuid());
			oOperate.setOrderId(curTask.getOrderId());
			oOperate.setStatus(curTask.getOrderStatus());
			oOperate.setOperatedId(user.getId());
			oOperate.setOpEndTime(new Date());
			oOperate.setOpSequence(curTask.getTaskType());
			oOperate.setTaskId(curTask.getId());
			
			//添加驳回原因
			newTask.setRemark(remark);
			
			taskService.processTask(null, null, oOperate, curTask, newTask);
			map.put(GlobalPara.AJAX_KEY, GlobalPara.AJAX_SUCCESS);
			return map;
		} catch (Exception e) {
			e.printStackTrace();
			map = new HashMap<String,Object>();
			map.put(GlobalPara.AJAX_KEY, "待办处理异常，请重试！");
			return map;
		}
	}
	
	/**
	 *  平台审核员审核通过
	 */
	@ResponseBody
	@RequestMapping(value = "/plcProTaskConfirm")
	public Object plcProTaskConfirm(HttpServletRequest request, @RequestParam("taskId")String taskId){
		Map<String,Object> map = new HashMap<String,Object>();
		try {
			//获取客户用户名userId
			User user = (User) request.getSession().getAttribute(GlobalPara.USER_SESSION_TOKEN);
			//获取当前待办
			Task curTask = taskService.getTaskById(taskId);
			curTask.setStatus(BusConstants.TASK_STATUS_PROCESSED);
			curTask.setEndTime(new Date());
			
			//生成下一环节待办
			Task newTask = null;
			Order order = null;
			String applyId = null;
			List<Role> roles = null;
			switch (curTask.getTaskType()) {
			case 2:
				applyId = orderService.getOrderById(curTask.getOrderId()).getApplyId();
				roles = roleService.getRoleListByUserId(applyId);
				for (Role role : roles) {
					if(GlobalPara.RNAME_HWJ_OPERATOR.equals(role.getRoleName())){
						newTask = taskService.initNewTask(curTask.getOrderId(), BusConstants.TASK_TYPE_FIRST_HCPAY);
						newTask.setOrderStatus(curTask.getOrderStatus());
					}
					if(GlobalPara.RNAME_CN_OTHER_CUSTOMER.equals(role.getRoleName())){
						newTask = taskService.initNewTask(curTask.getOrderId(), BusConstants.TASK_TYPE_CUSTOMER_PAY);
						newTask.setOrderStatus(curTask.getOrderStatus());
					}
				}
				break;
			case 7:
				//生成新的待办任务
				newTask = taskService.initNewTask(curTask.getOrderId(), BusConstants.TASK_TYPE_FIRST_MONEYRECEIPT);
				newTask.setOrderStatus(curTask.getOrderStatus());
				//角色为海外用户 需指定到人
				order = orderService.getOrderById(curTask.getOrderId());
				newTask.setOperatedId(order.getHwUserId());
				break;
			case 11:
				//生成新的待办任务
				newTask = taskService.initNewTask(curTask.getOrderId(), BusConstants.TASK_TYPE_HWJPAY);
				newTask.setOrderStatus(curTask.getOrderStatus());
				break;
			case 15:
				//生成新的待办任务
				newTask = taskService.initNewTask(curTask.getOrderId(), BusConstants.TASK_TYPE_HWJPAY_HCRECEIPT_CONFIRM);
				newTask.setOrderStatus(curTask.getOrderStatus());
				break;
			case 19:
				//生成新的待办任务
				newTask = taskService.initNewTask(curTask.getOrderId(), BusConstants.TASK_TYPE_SECOND_HCPAY);
				newTask.setOrderStatus(curTask.getOrderStatus());
				break;
			case 23:
				//生成新的待办任务
				newTask = taskService.initNewTask(curTask.getOrderId(), BusConstants.TASK_TYPE_SECOND_HCPAY_RECEIPT_CONFIRM);
				newTask.setOrderStatus(curTask.getOrderStatus());
				//角色为海外用户 需指定到人
				order = orderService.getOrderById(curTask.getOrderId());
				newTask.setOperatedId(order.getHwUserId());
				break;
			case 27:
				//生成新的待办任务
				newTask = taskService.initNewTask(curTask.getOrderId(), BusConstants.TASK_TYPE_FIRST_HWUSERPAY);
				newTask.setOrderStatus(curTask.getOrderStatus());
				//角色为海外用户 需指定到人
				order = orderService.getOrderById(curTask.getOrderId());
				newTask.setOperatedId(order.getHwUserId());
				break;
			case 31:
				//生成新的待办任务
				newTask = taskService.initNewTask(curTask.getOrderId(), BusConstants.TASK_TYPE_FIRST_HCRECEIPT);
				newTask.setOrderStatus(curTask.getOrderStatus());
				break;
			case 35:
				//生成新的待办任务
				newTask = taskService.initNewTask(curTask.getOrderId(), BusConstants.TASK_TYPE_HC_HWPAY);
				newTask.setOrderStatus(curTask.getOrderStatus());
				break;
			case 39:
				//生成新的待办任务
				newTask = taskService.initNewTask(curTask.getOrderId(), BusConstants.TASK_TYPE_HWJ_HWRECEIPT);
				newTask.setOrderStatus(curTask.getOrderStatus());
				break;
			case 43:
				//生成新的待办任务
				newTask = taskService.initNewTask(curTask.getOrderId(), BusConstants.TASK_TYPE_SECOND_HWUSERPAY);
				newTask.setOrderStatus(curTask.getOrderStatus());
				break;
			case 47:
				//生成新的待办任务
				newTask = taskService.initNewTask(curTask.getOrderId(), BusConstants.TASK_TYPE_SECOND_HCRECEIPT);
				newTask.setOrderStatus(curTask.getOrderStatus());
				break;
			case 51:
				//更新订单状态
				order = orderService.getOrderById(curTask.getOrderId());
				order.setModifiedDate(new Date());
				order.setModifiedId(user.getId());
				order.setStatus(BusConstants.ORDER_STATUS_COMPLETED);
				break;
			case 55:
				//生成新的待办任务
				newTask = taskService.initNewTask(curTask.getOrderId(), BusConstants.TASK_TYPE_HWUSER_RECEIPT);
				newTask.setOrderStatus(curTask.getOrderStatus());
				break;
			case 59:
				//生成新的待办任务
				newTask = taskService.initNewTask(curTask.getOrderId(), BusConstants.TASK_TYPE_HWUSER_PAY);
				newTask.setOrderStatus(curTask.getOrderStatus());
				//角色为海外用户 需指定到人
				order = orderService.getOrderById(curTask.getOrderId());
				newTask.setOperatedId(order.getHwUserId());
				break;
			case 63:
				//生成新的待办任务
				newTask = taskService.initNewTask(curTask.getOrderId(), BusConstants.TASK_TYPE_CUSTOMER_RECEIPT);
				newTask.setOrderStatus(curTask.getOrderStatus());
				break;
			case 67:
				//更新订单状态
				order = orderService.getOrderById(curTask.getOrderId());
				order.setModifiedDate(new Date());
				order.setModifiedId(user.getId());
				order.setStatus(BusConstants.ORDER_STATUS_COMPLETED);
				break;
			}
			 
			//生成操作步骤
			OrderOperate oOperate = new OrderOperate();
			oOperate.setId(PKIDUtils.getUuid());
			oOperate.setOrderId(curTask.getOrderId());
			oOperate.setStatus(curTask.getOrderStatus());
			oOperate.setOperatedId(user.getId());
			oOperate.setOpEndTime(new Date());
			oOperate.setOpSequence(curTask.getTaskType());
			oOperate.setTaskId(curTask.getId());
			
			if(BusConstants.TASK_TYPE_SECOND_HCRECEIPT_RECHECK.equals(curTask.getTaskType()) || BusConstants.TASK_TYPE_CUSTOMER_RECEIPT_RECHECK.equals(curTask.getTaskType())){
				taskService.processTask(order, null, oOperate, curTask, null);
			}else{
				taskService.processTask(null, null, oOperate, curTask, newTask);
			}
			
			map.put(GlobalPara.AJAX_KEY, GlobalPara.AJAX_SUCCESS);
			return map;
		} catch (Exception e) {
			e.printStackTrace();
			map = new HashMap<String,Object>();
			map.put(GlobalPara.AJAX_KEY, "待办处理异常，请重试！");
			return map;
		}
	}
	
	/**
	 *  平台审核员审核驳回
	 */
	@ResponseBody
	@RequestMapping(value = "/plcProTaskRejected")
	public Object plcProTaskRejected(HttpServletRequest request, @RequestParam("taskId")String taskId, String remark){
		Map<String,Object> map = new HashMap<String,Object>();
		try {
			//获取客户用户名userId
			User user = (User) request.getSession().getAttribute(GlobalPara.USER_SESSION_TOKEN);
			//获取当前待办
			Task curTask = taskService.getTaskById(taskId);
			curTask.setStatus(BusConstants.TASK_STATUS_PROCESSED);
			curTask.setEndTime(new Date());
			curTask.setRemark(remark);
			
			//生成上一环节待办
			Task newTask = null;
			Map<String,Object> paramMap = null;
			Task upTask = null;
			switch (curTask.getTaskType()) {
			case 2:
				newTask = taskService.initNewTask(curTask.getOrderId(), BusConstants.TASK_TYPE_HWUSERINFO_REJECT);
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", curTask.getOrderId());
				paramMap.put("taskType", BusConstants.TASK_TYPE_COMMIT);
				upTask = taskService.getTaskByParams(paramMap);
				newTask.setOperatedId(upTask.getOperatedId());
				break;
			case 7:
				//生成新的待办任务
				newTask = taskService.initNewTask(curTask.getOrderId(), BusConstants.TASK_TYPE_FIRST_HCPAY_VOUCHER_REJECT);
				newTask.setOrderStatus(curTask.getOrderStatus());
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", curTask.getOrderId());
				paramMap.put("taskType", BusConstants.TASK_TYPE_FIRST_HCPAY);
				upTask = taskService.getTaskByParams(paramMap);
				newTask.setOperatedId(upTask.getOperatedId());
				break;
			case 11:
				//生成新的待办任务
				newTask = taskService.initNewTask(curTask.getOrderId(), BusConstants.TASK_TYPE_FIRST_MONEYRECEIPT_VOUCHER_REJECT);
				newTask.setOrderStatus(curTask.getOrderStatus());
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", curTask.getOrderId());
				paramMap.put("taskType", BusConstants.TASK_TYPE_FIRST_MONEYRECEIPT);
				upTask = taskService.getTaskByParams(paramMap);
				newTask.setOperatedId(upTask.getOperatedId());
				break;
			case 15:
				//生成新的待办任务
				newTask = taskService.initNewTask(curTask.getOrderId(), BusConstants.TASK_TYPE_HWJPAY_CONFIRM_REJECT);
				newTask.setOrderStatus(curTask.getOrderStatus());
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", curTask.getOrderId());
				paramMap.put("taskType", BusConstants.TASK_TYPE_HWJPAY);
				upTask = taskService.getTaskByParams(paramMap);
				newTask.setOperatedId(upTask.getOperatedId());
				break;
			case 19:
				//生成新的待办任务
				newTask = taskService.initNewTask(curTask.getOrderId(), BusConstants.TASK_TYPE_HWJPAY_HCRECEIPT_VOUCHER_REJECT);
				newTask.setOrderStatus(curTask.getOrderStatus());
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", curTask.getOrderId());
				paramMap.put("taskType", BusConstants.TASK_TYPE_HWJPAY_HCRECEIPT_CONFIRM);
				upTask = taskService.getTaskByParams(paramMap);
				newTask.setOperatedId(upTask.getOperatedId());
				break;
			case 23:
				//生成新的待办任务
				newTask = taskService.initNewTask(curTask.getOrderId(), BusConstants.TASK_TYPE_SECOND_HCPAY_CONFIRM_REJECT);
				newTask.setOrderStatus(curTask.getOrderStatus());
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", curTask.getOrderId());
				paramMap.put("taskType", BusConstants.TASK_TYPE_SECOND_HCPAY);
				upTask = taskService.getTaskByParams(paramMap);
				newTask.setOperatedId(upTask.getOperatedId());
				break;
			case 27:
				//生成新的待办任务
				newTask = taskService.initNewTask(curTask.getOrderId(), BusConstants.TASK_TYPE_SECOND_HCPAY_RECEIPT_VOUCHER_REJECT);
				newTask.setOrderStatus(curTask.getOrderStatus());
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", curTask.getOrderId());
				paramMap.put("taskType", BusConstants.TASK_TYPE_SECOND_HCPAY_RECEIPT_CONFIRM);
				upTask = taskService.getTaskByParams(paramMap);
				newTask.setOperatedId(upTask.getOperatedId());
				break;
			case 31:
				//生成新的待办任务
				newTask = taskService.initNewTask(curTask.getOrderId(), BusConstants.TASK_TYPE_FIRST_HWUSERPAY_REJECT);
				newTask.setOrderStatus(curTask.getOrderStatus());
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", curTask.getOrderId());
				paramMap.put("taskType", BusConstants.TASK_TYPE_FIRST_HWUSERPAY);
				upTask = taskService.getTaskByParams(paramMap);
				newTask.setOperatedId(upTask.getOperatedId());
				break;
			case 35:
				//生成新的待办任务
				newTask = taskService.initNewTask(curTask.getOrderId(), BusConstants.TASK_TYPE_FIRST_HCRECEIPT_REJECT);
				newTask.setOrderStatus(curTask.getOrderStatus());
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", curTask.getOrderId());
				paramMap.put("taskType", BusConstants.TASK_TYPE_FIRST_HCRECEIPT);
				upTask = taskService.getTaskByParams(paramMap);
				newTask.setOperatedId(upTask.getOperatedId());
				break;
			case 39:
				//生成新的待办任务
				newTask = taskService.initNewTask(curTask.getOrderId(), BusConstants.TASK_TYPE_HC_HWPAY_REJECT);
				newTask.setOrderStatus(curTask.getOrderStatus());
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", curTask.getOrderId());
				paramMap.put("taskType", BusConstants.TASK_TYPE_HC_HWPAY);
				upTask = taskService.getTaskByParams(paramMap);
				newTask.setOperatedId(upTask.getOperatedId());
				break;
			case 43:
				//生成新的待办任务
				newTask = taskService.initNewTask(curTask.getOrderId(), BusConstants.TASK_TYPE_HWJ_HWRECEIPT_REJECT);
				newTask.setOrderStatus(curTask.getOrderStatus());
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", curTask.getOrderId());
				paramMap.put("taskType", BusConstants.TASK_TYPE_HWJ_HWRECEIPT);
				upTask = taskService.getTaskByParams(paramMap);
				newTask.setOperatedId(upTask.getOperatedId());
				break;
			case 47:
				//生成新的待办任务
				newTask = taskService.initNewTask(curTask.getOrderId(), BusConstants.TASK_TYPE_SECOND_HWUSERPAY_REJECT);
				newTask.setOrderStatus(curTask.getOrderStatus());
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", curTask.getOrderId());
				paramMap.put("taskType", BusConstants.TASK_TYPE_SECOND_HWUSERPAY);
				upTask = taskService.getTaskByParams(paramMap);
				newTask.setOperatedId(upTask.getOperatedId());
				break;
			case 51:
				//生成新的待办任务
				newTask = taskService.initNewTask(curTask.getOrderId(), BusConstants.TASK_TYPE_SECOND_HCRECEIPT_REJECT);
				newTask.setOrderStatus(curTask.getOrderStatus());
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", curTask.getOrderId());
				paramMap.put("taskType", BusConstants.TASK_TYPE_SECOND_HCRECEIPT);
				upTask = taskService.getTaskByParams(paramMap);
				newTask.setOperatedId(upTask.getOperatedId());
				break;
			case 55:
				//生成新的待办任务
				newTask = taskService.initNewTask(curTask.getOrderId(), BusConstants.TASK_TYPE_CUSTOMER_PAY_REJECT);
				newTask.setOrderStatus(curTask.getOrderStatus());
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", curTask.getOrderId());
				paramMap.put("taskType", BusConstants.TASK_TYPE_CUSTOMER_PAY);
				upTask = taskService.getTaskByParams(paramMap);
				newTask.setOperatedId(upTask.getOperatedId());
				break;
			case 59:
				//生成新的待办任务
				newTask = taskService.initNewTask(curTask.getOrderId(), BusConstants.TASK_TYPE_HWUSER_RECEIPT_REJECT);
				newTask.setOrderStatus(curTask.getOrderStatus());
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", curTask.getOrderId());
				paramMap.put("taskType", BusConstants.TASK_TYPE_HWUSER_RECEIPT);
				upTask = taskService.getTaskByParams(paramMap);
				newTask.setOperatedId(upTask.getOperatedId());
				break;
			case 63:
				//生成新的待办任务
				newTask = taskService.initNewTask(curTask.getOrderId(), BusConstants.TASK_TYPE_HWUSER_PAY_REJECT);
				newTask.setOrderStatus(curTask.getOrderStatus());
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", curTask.getOrderId());
				paramMap.put("taskType", BusConstants.TASK_TYPE_HWUSER_PAY);
				upTask = taskService.getTaskByParams(paramMap);
				newTask.setOperatedId(upTask.getOperatedId());
				break;
			case 67:
				//生成新的待办任务
				newTask = taskService.initNewTask(curTask.getOrderId(), BusConstants.TASK_TYPE_CUSTOMER_RECEIPT_REJECT);
				newTask.setOrderStatus(curTask.getOrderStatus());
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", curTask.getOrderId());
				paramMap.put("taskType", BusConstants.TASK_TYPE_CUSTOMER_RECEIPT);
				upTask = taskService.getTaskByParams(paramMap);
				newTask.setOperatedId(upTask.getOperatedId());
				break;
			}
			
			//生成操作步骤
			OrderOperate oOperate = new OrderOperate();
			oOperate.setId(PKIDUtils.getUuid());
			oOperate.setOrderId(curTask.getOrderId());
			oOperate.setStatus(curTask.getOrderStatus());
			oOperate.setOperatedId(user.getId());
			oOperate.setOpEndTime(new Date());
			oOperate.setOpSequence(curTask.getTaskType());
			oOperate.setTaskId(curTask.getId());
			
			//添加驳回原因
			newTask.setRemark(remark);
			
			taskService.processTask(null, null, oOperate, curTask, newTask);
			map.put(GlobalPara.AJAX_KEY, GlobalPara.AJAX_SUCCESS);
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
			
			
			Map<String,Object> params = new HashMap<String,Object>();
			params.put(GlobalPara.PAGER_SESSION, pager);
	        params.put("status", BusConstants.TASK_STATUS_PROCESSED);
	        params.put("operatedId", user.getId());
	        
	        if(StringUtils.isNotBlank(task.getOrderNo())){
	        	params.put("orderNo", task.getOrderNo());
	        }
	        if(StringUtils.isNotBlank(task.getProTaskBTime())){
	        	params.put("beginTime", task.getProTaskBTime());
	        }
	        if(StringUtils.isNotBlank(task.getProTaskETime())){
	        	params.put("endTime", task.getProTaskETime());
	        }
	        
	        
			List<Task> tasks = taskService.getTaskListByParams(params);
			
			mv.addObject("task", task);
			mv.addObject("pager", pager);
			mv.addObject("tasks", tasks);
			mv.setViewName("task/toPageTaskProed");
			return mv;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("已办管理模块异常!");
		}
	}
	
	@RequestMapping(value="/toDownload",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> toDownLoad(HttpServletRequest request,HttpServletResponse response,String fileName) throws Exception{
		Map<String,Object> resultMap = new HashMap<String,Object>();
		request.setCharacterEncoding("utf-8");
		String path = request.getSession().getServletContext().getRealPath("/")+GlobalPara.CERT_FILE_URL;
		System.out.println("------------"+path);
		//String newFileName = fileName.replaceAll('\')
		String filePath = path + File.separator + fileName;
		System.out.println("------------"+filePath);
		DownloadUtil util = new DownloadUtil();
		
		util.download(filePath, fileName, response);
		resultMap.put(GlobalPara.AJAX_KEY, GlobalPara.AJAX_SUCCESS);
		
		return resultMap;
		
	}
	
	private boolean uploadImg(HttpServletRequest request,String taskId) throws Exception{
		boolean isUpload = true;
		//获取客户用户名userId
		User user = (User) request.getSession().getAttribute(GlobalPara.USER_SESSION_TOKEN);
		//获取当前待办
		Task curTask = taskService.getTaskById(taskId);
		
		MultipartHttpServletRequest mhRequest = (MultipartHttpServletRequest)request;
		Map<String,MultipartFile> mfMap = mhRequest.getFileMap();
		String ctxPath = request.getSession().getServletContext().getRealPath("/")+GlobalPara.CERT_FILE_URL;
		System.err.println(ctxPath);
		//查询订单信息
		Order order = orderService.getOrderById(curTask.getOrderId());
		String orderNo = order.getOrderNo();
		Integer taskType = curTask.getTaskType();
		String taskPath = "task_type"+ taskType.toString();
		
		String finalPath = ctxPath  + File.separator + orderNo + File.separator + taskPath + File.separator;
        
		//凭证存储的相对路径
		//String certPath = orderNo + File.separator + taskPath + File.separator;
		
		String certPath = orderNo + "/" + taskPath + "/";
				
		File file = new File(finalPath);
		
		//如果该文件夹存在，标示驳回重新上传，先删除之前的凭证
		if(!file.exists()){
			file.mkdirs();
		}
		
		
		
		List<OrderCert> orderCertList = new ArrayList<OrderCert>();
		String fileName = null;
		//String certName = null;
		OrderCert oCert = null;
		int fileIndex = 1;
		boolean isSuccess = true;
		for(Map.Entry<String, MultipartFile> entity:mfMap.entrySet()){
			MultipartFile mf = entity.getValue();
			fileName = mf.getOriginalFilename();
			//certName = "cert" + fileIndex;
			fileIndex = fileIndex + 1;
			//生成凭证存储List
			oCert = new OrderCert();
			oCert.setId(PKIDUtils.getUuid());
			oCert.setTaskType(taskType);
			oCert.setStatus(0);
			oCert.setOrderId(order.getId());
			oCert.setCertPic(certPath + fileName);
			oCert.setCreatedId(user.getId());
			oCert.setCreatedDate(new Date());
			
			String targetPath = finalPath + fileName;
			File uploadFile = new File(finalPath + fileName);
			try {
				FileCopyUtils.copy(mf.getBytes(), uploadFile);
				//CipsFileUtils.copyFile(uploadFile, targetPath);
				orderCertList.add(oCert);
				isSuccess = true;
			} catch (Exception e) {
				// TODO: handle exception
				isSuccess = false;
			}
		}
		
		if(isSuccess){
			if(orderCertList.size()>0){
				try {
					//删除相同的凭证
					Map<String,Object> param = new HashMap<String,Object>();
					param.put("orderId", curTask.getId());
					param.put("taskType", curTask.getTaskType());
					
					orderCertService.deleteOrderCertByParam(param);
					
					orderCertService.insertOrderCertList(orderCertList);
					isUpload = true;
				} catch (Exception e) {
					// TODO: handle exception
					isUpload = false;
				}
			}else{
				isUpload = false;
			}
		}else{
			isUpload = false;
		}
		
		return isUpload;
	}
	
	
	@RequestMapping(value = "/viewProTask")
	public ModelAndView viewProTask(HttpServletRequest request, @RequestParam("taskId")String taskId){
		try {
			ModelAndView mv = new ModelAndView();
			//查询当前要处理的待办
			Task task = taskService.getTaskById(taskId);
			//根据类型选择视图及参数
			//查询订单信息
			Order order = orderService.getOrderById(task.getOrderId());
			order.setStatusDesc(OrderStsEnum.getNameByCode(order.getStatus().toString()));
			User user = userService.getUserByUserId(order.getApplyId());
			
			OrderDetails hwAcc = null;
			OrderDetails hwUserAcc = null;
			OrderDetails hcAccT3 = null;
			OrderDetails hcAccT4 = null;
			Rate curRToURate = null;
			Map<String,Object> paramMap =  null;
			//凭证信息
			List<OrderCert> ocList = null;
			switch (task.getTaskType()) {
			case 1:
				//获取海外账户信息
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", task.getOrderId());
				paramMap.put("type", BusConstants.ORDERDETAILS_TYPE_CUSTOMER_HWACC);
				hwAcc = orderService.getOrderDetailsByParams(paramMap);
				
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", task.getOrderId());
				paramMap.put("type", BusConstants.ORDERDETAILS_TYPE_HWUSER_LOCACC);
				hwUserAcc = orderService.getOrderDetailsByParams(paramMap);
				
				mv.addObject("order", order);
				mv.addObject("user", user);
				mv.addObject("hwAcc", hwAcc);
				mv.addObject("hwUserAcc", hwUserAcc);
				mv.addObject("task", task);
				mv.setViewName("task/viewProTaskHwUser");
				break;
			case 2:
				//获取海外账户信息
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", task.getOrderId());
				paramMap.put("type", BusConstants.ORDERDETAILS_TYPE_CUSTOMER_HWACC);
				hwAcc = orderService.getOrderDetailsByParams(paramMap);
				
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", task.getOrderId());
				paramMap.put("type", BusConstants.ORDERDETAILS_TYPE_HWUSER_LOCACC);
				hwUserAcc = orderService.getOrderDetailsByParams(paramMap);
				
				mv.addObject("order", order);
				mv.addObject("user", user);
				mv.addObject("hwAcc", hwAcc);
				mv.addObject("hwUserAcc", hwUserAcc);
				mv.addObject("task", task);
				mv.setViewName("task/viewProTaskHwUser");
				break;
			case 3:
				//获取海外账户信息
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", task.getOrderId());
				paramMap.put("type", BusConstants.ORDERDETAILS_TYPE_CUSTOMER_HWACC);
				hwAcc = orderService.getOrderDetailsByParams(paramMap);
				//操作员选择的海外用户
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", task.getOrderId());
				paramMap.put("type", BusConstants.ORDERDETAILS_TYPE_HWUSER_LOCACC);
				hwUserAcc = orderService.getOrderDetailsByParams(paramMap);
				
				mv.addObject("order", order);
				mv.addObject("user", user);
				mv.addObject("hwAcc", hwAcc);
				mv.addObject("hwUserAcc", hwUserAcc);
				mv.addObject("task", task);
				mv.setViewName("task/viewProTaskHwUser");
				break;
			case 4:
				//海外用户账户信息
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", task.getOrderId());
				paramMap.put("type", BusConstants.ORDERDETAILS_TYPE_HWUSER_LOCACC);
				hwUserAcc = orderService.getOrderDetailsByParams(paramMap);
				
				//查询上传图片信息
				paramMap.put("taskType", 4);
				ocList = orderCertService.getOrderCertList(paramMap);
	
				//查询出华创维护的国内国外账户信息
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", task.getOrderId());
				paramMap.put("type", BusConstants.ORDERDETAILS_TYPE_HC_LOCACC);
				hcAccT3 = orderService.getOrderDetailsByParams(paramMap);
				
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", task.getOrderId());
				paramMap.put("type", BusConstants.ORDERDETAILS_TYPE_HC_HWACC);
				hcAccT4 = orderService.getOrderDetailsByParams(paramMap);
				
				mv.addObject("ocList", ocList);
				mv.addObject("accInfo", hwUserAcc);
				mv.addObject("hcT3", hcAccT3);
				mv.addObject("hcT4", hcAccT4);
				mv.addObject("payMoney", order.getPayAmount().add(new BigDecimal(50000)));
				mv.addObject("task", task);
				mv.setViewName("task/viewProTask");
				break;
			case 5:
				//海外用户账户信息
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", task.getOrderId());
				paramMap.put("type", BusConstants.ORDERDETAILS_TYPE_HWUSER_LOCACC);
				hwUserAcc = orderService.getOrderDetailsByParams(paramMap);
				
				//查询上传图片信息
				paramMap.put("taskType", 4);
				ocList = orderCertService.getOrderCertList(paramMap);
	
				//查询出华创维护的国内国外账户信息
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", task.getOrderId());
				paramMap.put("type", BusConstants.ORDERDETAILS_TYPE_HC_LOCACC);
				hcAccT3 = orderService.getOrderDetailsByParams(paramMap);
				
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", task.getOrderId());
				paramMap.put("type", BusConstants.ORDERDETAILS_TYPE_HC_HWACC);
				hcAccT4 = orderService.getOrderDetailsByParams(paramMap);
				
				mv.addObject("ocList", ocList);
				mv.addObject("accInfo", hwUserAcc);
				mv.addObject("hcT3", hcAccT3);
				mv.addObject("hcT4", hcAccT4);
				mv.addObject("payMoney", order.getPayAmount().add(new BigDecimal(50000)));
				mv.addObject("task", task);
				mv.setViewName("task/viewProTask");
				break;
			case 6:
				//海外用户账户信息
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", task.getOrderId());
				paramMap.put("type", BusConstants.ORDERDETAILS_TYPE_HWUSER_LOCACC);
				hwUserAcc = orderService.getOrderDetailsByParams(paramMap);
				
				//查询上传图片信息
				paramMap.put("taskType", 4);
				ocList = orderCertService.getOrderCertList(paramMap);
	
				//查询出华创维护的国内国外账户信息
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", task.getOrderId());
				paramMap.put("type", BusConstants.ORDERDETAILS_TYPE_HC_LOCACC);
				hcAccT3 = orderService.getOrderDetailsByParams(paramMap);
				
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", task.getOrderId());
				paramMap.put("type", BusConstants.ORDERDETAILS_TYPE_HC_HWACC);
				hcAccT4 = orderService.getOrderDetailsByParams(paramMap);
				
				mv.addObject("ocList", ocList);
				mv.addObject("accInfo", hwUserAcc);
				mv.addObject("hcT3", hcAccT3);
				mv.addObject("hcT4", hcAccT4);
				mv.addObject("payMoney", order.getPayAmount().add(new BigDecimal(50000)));
				mv.addObject("task", task);
				mv.setViewName("task/viewProTask");
				break;
			case 7:
				//海外用户账户信息
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", task.getOrderId());
				paramMap.put("type", BusConstants.ORDERDETAILS_TYPE_HWUSER_LOCACC);
				hwUserAcc = orderService.getOrderDetailsByParams(paramMap);
				
				//查询上传图片信息
				paramMap.put("taskType", 4);
				ocList = orderCertService.getOrderCertList(paramMap);
	
				//查询出华创维护的国内国外账户信息
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", task.getOrderId());
				paramMap.put("type", BusConstants.ORDERDETAILS_TYPE_HC_LOCACC);
				hcAccT3 = orderService.getOrderDetailsByParams(paramMap);
				
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", task.getOrderId());
				paramMap.put("type", BusConstants.ORDERDETAILS_TYPE_HC_HWACC);
				hcAccT4 = orderService.getOrderDetailsByParams(paramMap);
				
				mv.addObject("ocList", ocList);
				mv.addObject("accInfo", hwUserAcc);
				mv.addObject("hcT3", hcAccT3);
				mv.addObject("hcT4", hcAccT4);
				mv.addObject("payMoney", order.getPayAmount().add(new BigDecimal(50000)));
				mv.addObject("task", task);
				mv.setViewName("task/viewProTask");
				break;
			case 8:
				//海外用户账户信息
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", task.getOrderId());
				paramMap.put("type", BusConstants.ORDERDETAILS_TYPE_HWUSER_LOCACC);
				hwUserAcc = orderService.getOrderDetailsByParams(paramMap);
				//查询上传图片信息
				paramMap.put("taskType", 8);
				ocList = orderCertService.getOrderCertList(paramMap);
				mv.addObject("ocList", ocList);
				
				mv.addObject("accInfo", hwUserAcc);
				mv.addObject("payMoney", order.getPayAmount().add(new BigDecimal(50000)));
				mv.addObject("task", task);
				mv.setViewName("task/viewProTask");
				break;
			case 9:
				//海外用户账户信息
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", task.getOrderId());
				paramMap.put("type", BusConstants.ORDERDETAILS_TYPE_HWUSER_LOCACC);
				hwUserAcc = orderService.getOrderDetailsByParams(paramMap);
				//查询上传图片信息
				paramMap.put("taskType", 8);
				ocList = orderCertService.getOrderCertList(paramMap);
				mv.addObject("ocList", ocList);
				
				mv.addObject("accInfo", hwUserAcc);
				mv.addObject("payMoney", order.getPayAmount().add(new BigDecimal(50000)));
				mv.addObject("task", task);
				mv.setViewName("task/viewProTask");
				break;
			case 10:
				//海外用户账户信息
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", task.getOrderId());
				paramMap.put("type", BusConstants.ORDERDETAILS_TYPE_HWUSER_LOCACC);
				hwUserAcc = orderService.getOrderDetailsByParams(paramMap);
				//查询上传图片信息
				paramMap.put("taskType", 8);
				ocList = orderCertService.getOrderCertList(paramMap);
				mv.addObject("ocList", ocList);
				
				mv.addObject("accInfo", hwUserAcc);
				mv.addObject("payMoney", order.getPayAmount().add(new BigDecimal(50000)));
				mv.addObject("task", task);
				mv.setViewName("task/viewProTask");
				break;
			case 11:
				//海外用户账户信息
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", task.getOrderId());
				paramMap.put("type", BusConstants.ORDERDETAILS_TYPE_HWUSER_LOCACC);
				hwUserAcc = orderService.getOrderDetailsByParams(paramMap);
				//查询上传图片信息
				paramMap.put("taskType", 8);
				ocList = orderCertService.getOrderCertList(paramMap);
				mv.addObject("ocList", ocList);
				
				mv.addObject("accInfo", hwUserAcc);
				mv.addObject("payMoney", order.getPayAmount().add(new BigDecimal(50000)));
				mv.addObject("task", task);
				mv.setViewName("task/viewProTask");
				break;
			case 12:
				//查询华创国内账户信息 上传打款凭证
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", task.getOrderId());
				paramMap.put("type", BusConstants.ORDERDETAILS_TYPE_HC_LOCACC);
				hcAccT3 = orderService.getOrderDetailsByParams(paramMap);
				
				//查询上传图片信息
				paramMap.put("taskType",12);
				ocList = orderCertService.getOrderCertList(paramMap);
				mv.addObject("ocList", ocList);
				
				mv.addObject("accInfo", hcAccT3);
				mv.addObject("payMoney", order.getPayAmount());
				mv.addObject("task", task);
				mv.setViewName("task/viewProTask");
				break;
			case 13:
				//查询华创国内账户信息
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", task.getOrderId());
				paramMap.put("type", BusConstants.ORDERDETAILS_TYPE_HC_LOCACC);
				hcAccT3 = orderService.getOrderDetailsByParams(paramMap);
				
				//查询上传图片信息
				paramMap.put("taskType",12);
				ocList = orderCertService.getOrderCertList(paramMap);
				mv.addObject("ocList", ocList);
				
				mv.addObject("accInfo", hcAccT3);
				mv.addObject("payMoney", order.getPayAmount());
				mv.addObject("task", task);
				mv.setViewName("task/viewProTask");
				break;
			case 14:
				//查询华创国内账户信息
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", task.getOrderId());
				paramMap.put("type", BusConstants.ORDERDETAILS_TYPE_HC_LOCACC);
				hcAccT3 = orderService.getOrderDetailsByParams(paramMap);
				
				//查询上传图片信息
				paramMap.put("taskType", 12);
				ocList = orderCertService.getOrderCertList(paramMap);
				mv.addObject("ocList", ocList);
				
				mv.addObject("accInfo", hcAccT3);
				mv.addObject("payMoney", order.getPayAmount());
				mv.addObject("task", task);
				mv.setViewName("task/viewProTask");
				break;
			case 15:
				//查询华创国内账户信息
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", task.getOrderId());
				paramMap.put("type", BusConstants.ORDERDETAILS_TYPE_HC_LOCACC);
				hcAccT3 = orderService.getOrderDetailsByParams(paramMap);
				
				//查询上传图片信息
				paramMap.put("taskType", 12);
				ocList = orderCertService.getOrderCertList(paramMap);
				mv.addObject("ocList", ocList);
				
				mv.addObject("accInfo", hcAccT3);
				mv.addObject("payMoney", order.getPayAmount());
				mv.addObject("task", task);
				mv.setViewName("task/viewProTask");
				break;
			case 16:
				//查询华创国内账户信息 上传收款凭证
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", task.getOrderId());
				paramMap.put("type", BusConstants.ORDERDETAILS_TYPE_HC_LOCACC);
				hcAccT3 = orderService.getOrderDetailsByParams(paramMap);
				
				//查询上传图片信息
				paramMap.put("taskType", 16);
				ocList = orderCertService.getOrderCertList(paramMap);
				mv.addObject("ocList", ocList);
				
				mv.addObject("accInfo", hcAccT3);
				mv.addObject("payMoney", order.getPayAmount());
				mv.addObject("task", task);
				mv.setViewName("task/viewProTask");
				break;
			case 17:
				//查询华创国内账户信息
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", task.getOrderId());
				paramMap.put("type", BusConstants.ORDERDETAILS_TYPE_HC_LOCACC);
				hcAccT3 = orderService.getOrderDetailsByParams(paramMap);
				
				//查询上传图片信息
				paramMap.put("taskType", 16);
				ocList = orderCertService.getOrderCertList(paramMap);
				mv.addObject("ocList", ocList);
				
				mv.addObject("accInfo", hcAccT3);
				mv.addObject("payMoney", order.getPayAmount());
				mv.addObject("task", task);
				mv.setViewName("task/viewProTask");
				break;
			case 18:
				//查询华创国内账户信息 可重新上传收款凭证
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", task.getOrderId());
				paramMap.put("type", BusConstants.ORDERDETAILS_TYPE_HC_LOCACC);
				hcAccT3 = orderService.getOrderDetailsByParams(paramMap);
				
				//查询上传图片信息
				paramMap.put("taskType", 16);
				ocList = orderCertService.getOrderCertList(paramMap);
				mv.addObject("ocList", ocList);
				
				mv.addObject("accInfo", hcAccT3);
				mv.addObject("payMoney", order.getPayAmount());
				mv.addObject("task", task);
				mv.setViewName("task/viewProTask");
				break;
			case 19:
				//查询华创国内账户信息
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", task.getOrderId());
				paramMap.put("type", BusConstants.ORDERDETAILS_TYPE_HC_LOCACC);
				hcAccT3 = orderService.getOrderDetailsByParams(paramMap);
				
				//查询上传图片信息
				paramMap.put("taskType", 16);
				ocList = orderCertService.getOrderCertList(paramMap);
				mv.addObject("ocList", ocList);
				
				mv.addObject("accInfo", hcAccT3);
				mv.addObject("payMoney", order.getPayAmount());
				mv.addObject("task", task);
				mv.setViewName("task/viewProTask");
				break;
			case 20:
				//海外用户账户信息 上传打款凭证
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", task.getOrderId());
				paramMap.put("type", BusConstants.ORDERDETAILS_TYPE_HWUSER_LOCACC);
				hwUserAcc = orderService.getOrderDetailsByParams(paramMap);
				
				//查询上传图片信息
				paramMap.put("taskType", 20);
				ocList = orderCertService.getOrderCertList(paramMap);
				mv.addObject("ocList", ocList);
				
				mv.addObject("accInfo", hwUserAcc);
				mv.addObject("payMoney", order.getPayAmount());
				mv.addObject("task", task);
				mv.setViewName("task/viewProTask");
				break;
			case 21:
				//海外用户账户信息 
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", task.getOrderId());
				paramMap.put("type", BusConstants.ORDERDETAILS_TYPE_HWUSER_LOCACC);
				hwUserAcc = orderService.getOrderDetailsByParams(paramMap);
				
				//查询上传图片信息
				paramMap.put("taskType", 20);
				ocList = orderCertService.getOrderCertList(paramMap);
				mv.addObject("ocList", ocList);
				
				mv.addObject("accInfo", hwUserAcc);
				mv.addObject("payMoney", order.getPayAmount());
				mv.addObject("task", task);
				mv.setViewName("task/viewProTask");
				break;
			case 22:
				//海外用户账户信息 可重新上传打款凭证 
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", task.getOrderId());
				paramMap.put("type", BusConstants.ORDERDETAILS_TYPE_HWUSER_LOCACC);
				hwUserAcc = orderService.getOrderDetailsByParams(paramMap);
				
				//查询上传图片信息
				paramMap.put("taskType", 20);
				ocList = orderCertService.getOrderCertList(paramMap);
				mv.addObject("ocList", ocList);
				
				mv.addObject("accInfo", hwUserAcc);
				mv.addObject("payMoney", order.getPayAmount());
				mv.addObject("task", task);
				mv.setViewName("task/viewProTask");
				break;
			case 23:
				//海外用户账户信息 
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", task.getOrderId());
				paramMap.put("type", BusConstants.ORDERDETAILS_TYPE_HWUSER_LOCACC);
				hwUserAcc = orderService.getOrderDetailsByParams(paramMap);
				
				//查询上传图片信息
				paramMap.put("taskType", 20);
				ocList = orderCertService.getOrderCertList(paramMap);
				mv.addObject("ocList", ocList);
				
				mv.addObject("accInfo", hwUserAcc);
				mv.addObject("payMoney", order.getPayAmount());
				mv.addObject("task", task);
				mv.setViewName("task/viewProTask");
				break;
			case 24:
				//海外用户账户信息 上传收款凭证
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", task.getOrderId());
				paramMap.put("type", BusConstants.ORDERDETAILS_TYPE_HWUSER_LOCACC);
				hwUserAcc = orderService.getOrderDetailsByParams(paramMap);
				
				//查询上传图片信息
				paramMap.put("taskType", 24);
				ocList = orderCertService.getOrderCertList(paramMap);
				mv.addObject("ocList", ocList);
				
				mv.addObject("accInfo", hwUserAcc);
				mv.addObject("payMoney", order.getPayAmount());
				mv.addObject("task", task);
				mv.setViewName("task/viewProTask");
				break;
			case 25:
				//海外用户账户信息
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", task.getOrderId());
				paramMap.put("type", BusConstants.ORDERDETAILS_TYPE_HWUSER_LOCACC);
				hwUserAcc = orderService.getOrderDetailsByParams(paramMap);
				
				//查询上传图片信息
				paramMap.put("taskType", 24);
				ocList = orderCertService.getOrderCertList(paramMap);
				mv.addObject("ocList", ocList);
				
				mv.addObject("accInfo", hwUserAcc);
				mv.addObject("payMoney", order.getPayAmount());
				mv.addObject("task", task);
				mv.setViewName("task/viewProTask");
				break;
			case 26:
				//海外用户账户信息 可重新上传收款凭证
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", task.getOrderId());
				paramMap.put("type", BusConstants.ORDERDETAILS_TYPE_HWUSER_LOCACC);
				hwUserAcc = orderService.getOrderDetailsByParams(paramMap);
				
				//查询上传图片信息
				paramMap.put("taskType", 24);
				ocList = orderCertService.getOrderCertList(paramMap);
				mv.addObject("ocList", ocList);
				
				mv.addObject("accInfo", hwUserAcc);
				mv.addObject("payMoney", order.getPayAmount());
				mv.addObject("task", task);
				mv.setViewName("task/viewProTask");
				break;
			case 27:
				//海外用户账户信息
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", task.getOrderId());
				paramMap.put("type", BusConstants.ORDERDETAILS_TYPE_HWUSER_LOCACC);
				hwUserAcc = orderService.getOrderDetailsByParams(paramMap);
				
				//查询上传图片信息
				paramMap.put("taskType",24);
				ocList = orderCertService.getOrderCertList(paramMap);
				mv.addObject("ocList", ocList);
				
				mv.addObject("accInfo", hwUserAcc);
				mv.addObject("payMoney", order.getPayAmount());
				mv.addObject("task", task);
				mv.setViewName("task/viewProTask");
				break;
			case 28:
				//查询华创海外账户 上传海外用户第一次打款凭证
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", task.getOrderId());
				paramMap.put("type", BusConstants.ORDERDETAILS_TYPE_HC_HWACC);
				hcAccT4 = orderService.getOrderDetailsByParams(paramMap);
				
				//查询上传图片信息
				paramMap.put("taskType", 28);
				ocList = orderCertService.getOrderCertList(paramMap);
				mv.addObject("ocList", ocList);
				
				mv.addObject("accInfo", hcAccT4);
				mv.addObject("payMoney", order.getApplyAmount());
				mv.addObject("task", task);
				mv.setViewName("task/viewProTask");
				break;
			case 29:
				//查询华创海外账户 
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", task.getOrderId());
				paramMap.put("type", BusConstants.ORDERDETAILS_TYPE_HC_HWACC);
				hcAccT4 = orderService.getOrderDetailsByParams(paramMap);
				
				//查询上传图片信息
				paramMap.put("taskType", 28);
				ocList = orderCertService.getOrderCertList(paramMap);
				mv.addObject("ocList", ocList);
				
				mv.addObject("accInfo", hcAccT4);
				mv.addObject("payMoney", order.getApplyAmount());
				mv.addObject("task", task);
				mv.setViewName("task/viewProTask");
				break;
			case 30:
				//查询华创海外账户 可重新上传海外用户第一次打款凭证
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", task.getOrderId());
				paramMap.put("type", BusConstants.ORDERDETAILS_TYPE_HC_HWACC);
				hcAccT4 = orderService.getOrderDetailsByParams(paramMap);
				
				//查询上传图片信息
				paramMap.put("taskType", 28);
				ocList = orderCertService.getOrderCertList(paramMap);
				mv.addObject("ocList", ocList);
				
				mv.addObject("accInfo", hcAccT4);
				mv.addObject("payMoney", order.getApplyAmount());
				mv.addObject("task", task);
				mv.setViewName("task/viewProTask");
				break;
			case 31:
				//查询华创海外账户 
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", task.getOrderId());
				paramMap.put("type", BusConstants.ORDERDETAILS_TYPE_HC_HWACC);
				hcAccT4 = orderService.getOrderDetailsByParams(paramMap);
				
				//查询上传图片信息
				paramMap.put("taskType", 28);
				ocList = orderCertService.getOrderCertList(paramMap);
				mv.addObject("ocList", ocList);
				
				mv.addObject("accInfo", hcAccT4);
				mv.addObject("payMoney", order.getApplyAmount());
				mv.addObject("task", task);
				mv.setViewName("task/viewProTask");
				break;
			case 32:
				//查询华创海外账户 上传收款凭证
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", task.getOrderId());
				paramMap.put("type", BusConstants.ORDERDETAILS_TYPE_HC_HWACC);
				hcAccT4 = orderService.getOrderDetailsByParams(paramMap);
				
				//查询上传图片信息
				paramMap.put("taskType", 32);
				ocList = orderCertService.getOrderCertList(paramMap);
				mv.addObject("ocList", ocList);
				
				mv.addObject("accInfo", hcAccT4);
				mv.addObject("payMoney", order.getApplyAmount());
				mv.addObject("task", task);
				mv.setViewName("task/viewProTask");
				break;
			case 33:
				//查询华创海外账户 
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", task.getOrderId());
				paramMap.put("type", BusConstants.ORDERDETAILS_TYPE_HC_HWACC);
				hcAccT4 = orderService.getOrderDetailsByParams(paramMap);
				
				//查询上传图片信息
				paramMap.put("taskType", 32);
				ocList = orderCertService.getOrderCertList(paramMap);
				mv.addObject("ocList", ocList);
				
				mv.addObject("accInfo", hcAccT4);
				mv.addObject("payMoney", order.getApplyAmount());
				mv.addObject("task", task);
				mv.setViewName("task/viewProTask");
				break;
			case 34:
				//查询华创海外账户 可重新上传收款凭证
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", task.getOrderId());
				paramMap.put("type", BusConstants.ORDERDETAILS_TYPE_HC_HWACC);
				hcAccT4 = orderService.getOrderDetailsByParams(paramMap);
				
				//查询上传图片信息
				paramMap.put("taskType", 32);
				ocList = orderCertService.getOrderCertList(paramMap);
				mv.addObject("ocList", ocList);
				
				mv.addObject("accInfo", hcAccT4);
				mv.addObject("payMoney", order.getApplyAmount());
				mv.addObject("task", task);
				mv.setViewName("task/viewProTask");
				break;
			case 35:
				//查询华创海外账户
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", task.getOrderId());
				paramMap.put("type", BusConstants.ORDERDETAILS_TYPE_HC_HWACC);
				hcAccT4 = orderService.getOrderDetailsByParams(paramMap);
				
				//查询上传图片信息
				paramMap.put("taskType", 32);
				ocList = orderCertService.getOrderCertList(paramMap);
				mv.addObject("ocList", ocList);
				
				mv.addObject("accInfo", hcAccT4);
				mv.addObject("payMoney", order.getApplyAmount());
				mv.addObject("task", task);
				mv.setViewName("task/viewProTask");
				break;
			case 36:
				//查询hwj海外账户信息 上传打款凭证
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", task.getOrderId());
				paramMap.put("type", BusConstants.ORDERDETAILS_TYPE_CUSTOMER_HWACC);
				hwAcc = orderService.getOrderDetailsByParams(paramMap);
				
				//查询上传图片信息
				paramMap.put("taskType", 36);
				ocList = orderCertService.getOrderCertList(paramMap);
				mv.addObject("ocList", ocList);
				
				mv.addObject("accInfo", hwAcc);
				mv.addObject("payMoney", order.getApplyAmount());
				mv.addObject("task", task);
				mv.setViewName("task/viewProTask");
				break;
			case 37:
				//查询hwj海外账户信息
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", task.getOrderId());
				paramMap.put("type", BusConstants.ORDERDETAILS_TYPE_CUSTOMER_HWACC);
				hwAcc = orderService.getOrderDetailsByParams(paramMap);
				
				//查询上传图片信息
				paramMap.put("taskType", 36);
				ocList = orderCertService.getOrderCertList(paramMap);
				mv.addObject("ocList", ocList);
				
				mv.addObject("accInfo", hwAcc);
				mv.addObject("payMoney", order.getApplyAmount());
				mv.addObject("task", task);
				mv.setViewName("task/viewProTask");
				break;
			case 38:
				//查询hwj海外账户信息 可重新上传凭证
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", task.getOrderId());
				paramMap.put("type", BusConstants.ORDERDETAILS_TYPE_CUSTOMER_HWACC);
				hwAcc = orderService.getOrderDetailsByParams(paramMap);
				
				//查询上传图片信息
				paramMap.put("taskType", 36);
				ocList = orderCertService.getOrderCertList(paramMap);
				mv.addObject("ocList", ocList);
				
				mv.addObject("accInfo", hwAcc);
				mv.addObject("payMoney", order.getApplyAmount());
				mv.addObject("task", task);
				mv.setViewName("task/viewProTask");
				break;
			case 39:
				//查询hwj海外账户信息
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", task.getOrderId());
				paramMap.put("type", BusConstants.ORDERDETAILS_TYPE_CUSTOMER_HWACC);
				hwAcc = orderService.getOrderDetailsByParams(paramMap);
				
				//查询上传图片信息
				paramMap.put("taskType", 36);
				ocList = orderCertService.getOrderCertList(paramMap);
				mv.addObject("ocList", ocList);
				
				mv.addObject("accInfo", hwAcc);
				mv.addObject("payMoney", order.getApplyAmount());
				mv.addObject("task", task);
				mv.setViewName("task/viewProTask");
				break;
			case 40:
				//查询hwj海外账户信息 上传收款凭证
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", task.getOrderId());
				paramMap.put("type", BusConstants.ORDERDETAILS_TYPE_CUSTOMER_HWACC);
				hwAcc = orderService.getOrderDetailsByParams(paramMap);
				
				//查询上传图片信息
				paramMap.put("taskType", 40);
				ocList = orderCertService.getOrderCertList(paramMap);
				mv.addObject("ocList", ocList);
				
				mv.addObject("accInfo", hwAcc);
				mv.addObject("payMoney", order.getApplyAmount());
				mv.addObject("task", task);
				mv.setViewName("task/viewProTask");
				break;
			case 41:
				//查询hwj海外账户信息
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", task.getOrderId());
				paramMap.put("type", BusConstants.ORDERDETAILS_TYPE_CUSTOMER_HWACC);
				hwAcc = orderService.getOrderDetailsByParams(paramMap);
				
				//查询上传图片信息
				paramMap.put("taskType", 40);
				ocList = orderCertService.getOrderCertList(paramMap);
				mv.addObject("ocList", ocList);
				
				mv.addObject("accInfo", hwAcc);
				mv.addObject("payMoney", order.getApplyAmount());
				mv.addObject("task", task);
				mv.setViewName("task/viewProTask");
				break;
			case 42:
				//查询hwj海外账户信息 可重新上传收款凭证
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", task.getOrderId());
				paramMap.put("type", BusConstants.ORDERDETAILS_TYPE_CUSTOMER_HWACC);
				hwAcc = orderService.getOrderDetailsByParams(paramMap);
				
				//查询上传图片信息
				paramMap.put("taskType", 40);
				ocList = orderCertService.getOrderCertList(paramMap);
				mv.addObject("ocList", ocList);
				
				mv.addObject("accInfo", hwAcc);
				mv.addObject("payMoney", order.getApplyAmount());
				mv.addObject("task", task);
				mv.setViewName("task/viewProTask");
				break;
			case 43:
				//查询hwj海外账户信息
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", task.getOrderId());
				paramMap.put("type", BusConstants.ORDERDETAILS_TYPE_CUSTOMER_HWACC);
				hwAcc = orderService.getOrderDetailsByParams(paramMap);
				
				//查询上传图片信息
				paramMap.put("taskType", 40);
				ocList = orderCertService.getOrderCertList(paramMap);
				mv.addObject("ocList", ocList);
				
				mv.addObject("accInfo", hwAcc);
				mv.addObject("payMoney", order.getApplyAmount());
				mv.addObject("task", task);
				mv.setViewName("task/viewProTask");
				break;
			case 44:
				//查询华创海外账户 上传打款凭证
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", task.getOrderId());
				paramMap.put("type", BusConstants.ORDERDETAILS_TYPE_HC_HWACC);
				hcAccT4 = orderService.getOrderDetailsByParams(paramMap);
				
				//查询上传图片信息
				paramMap.put("taskType", 44);
				ocList = orderCertService.getOrderCertList(paramMap);
				mv.addObject("ocList", ocList);
				
				//查询rmb兑美元汇率
				paramMap =  new HashMap<String,Object>();
				paramMap.put("status", BusConstants.RATE_STATUS_YES);
				paramMap.put("type", BusConstants.RATE_TYPE_RMB_TO_US);
				curRToURate = feeService.getCurrentRate(paramMap);
				
				mv.addObject("accInfo", hcAccT4);
				mv.addObject("payMoney", order.getApplyAmount().add(new BigDecimal(50000).multiply(curRToURate.getRateHigh().divide(new BigDecimal(100)))));
				mv.addObject("task", task);
				mv.setViewName("task/viewProTask");
				break;
			case 45:
				//查询华创海外账户 
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", task.getOrderId());
				paramMap.put("type", BusConstants.ORDERDETAILS_TYPE_HC_HWACC);
				hcAccT4 = orderService.getOrderDetailsByParams(paramMap);
				
				//查询上传图片信息
				paramMap.put("taskType", 44);
				ocList = orderCertService.getOrderCertList(paramMap);
				mv.addObject("ocList", ocList);
				
				//查询rmb兑美元汇率
				paramMap =  new HashMap<String,Object>();
				paramMap.put("status", BusConstants.RATE_STATUS_YES);
				paramMap.put("type", BusConstants.RATE_TYPE_RMB_TO_US);
				curRToURate = feeService.getCurrentRate(paramMap);
				
				mv.addObject("accInfo", hcAccT4);
				mv.addObject("payMoney", order.getApplyAmount().add(new BigDecimal(50000).multiply(curRToURate.getRateHigh()).divide(new BigDecimal(100))));
				mv.addObject("task", task);
				mv.setViewName("task/viewProTask");
				break;
			case 46:
				//查询华创海外账户 可重新上传打款凭证
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", task.getOrderId());
				paramMap.put("type", BusConstants.ORDERDETAILS_TYPE_HC_HWACC);
				hcAccT4 = orderService.getOrderDetailsByParams(paramMap);
				
				//查询上传图片信息
				paramMap.put("taskType", 44);
				ocList = orderCertService.getOrderCertList(paramMap);
				mv.addObject("ocList", ocList);
				
				//查询rmb兑美元汇率
				paramMap =  new HashMap<String,Object>();
				paramMap.put("status", BusConstants.RATE_STATUS_YES);
				paramMap.put("type", BusConstants.RATE_TYPE_RMB_TO_US);
				curRToURate = feeService.getCurrentRate(paramMap);
				
				mv.addObject("accInfo", hcAccT4);
				mv.addObject("payMoney", order.getApplyAmount().add(new BigDecimal(50000).multiply(curRToURate.getRateHigh()).divide(new BigDecimal(100))));
				mv.addObject("task", task);
				mv.setViewName("task/viewProTask");
				break;
			case 47:
				//查询华创海外账户 
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", task.getOrderId());
				paramMap.put("type", BusConstants.ORDERDETAILS_TYPE_HC_HWACC);
				hcAccT4 = orderService.getOrderDetailsByParams(paramMap);
				
				//查询上传图片信息
				paramMap.put("taskType", 44);
				ocList = orderCertService.getOrderCertList(paramMap);
				mv.addObject("ocList", ocList);
				
				//查询rmb兑美元汇率
				paramMap =  new HashMap<String,Object>();
				paramMap.put("status", BusConstants.RATE_STATUS_YES);
				paramMap.put("type", BusConstants.RATE_TYPE_RMB_TO_US);
				curRToURate = feeService.getCurrentRate(paramMap);
				
				mv.addObject("accInfo", hcAccT4);
				mv.addObject("payMoney", order.getApplyAmount().add(new BigDecimal(50000).multiply(curRToURate.getRateHigh()).divide(new BigDecimal(100))));
				mv.addObject("task", task);
				mv.setViewName("task/viewProTask");
				break;
			case 48:
				//查询华创海外账户  上传收款凭证
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", task.getOrderId());
				paramMap.put("type", BusConstants.ORDERDETAILS_TYPE_HC_HWACC);
				hcAccT4 = orderService.getOrderDetailsByParams(paramMap);
				
				//查询上传图片信息
				paramMap.put("taskType", 48);
				ocList = orderCertService.getOrderCertList(paramMap);
				mv.addObject("ocList", ocList);
				
				//查询rmb兑美元汇率
				paramMap =  new HashMap<String,Object>();
				paramMap.put("status", BusConstants.RATE_STATUS_YES);
				paramMap.put("type", BusConstants.RATE_TYPE_RMB_TO_US);
				curRToURate = feeService.getCurrentRate(paramMap);
				
				mv.addObject("accInfo", hcAccT4);
				mv.addObject("payMoney", order.getApplyAmount().add(new BigDecimal(50000).multiply(curRToURate.getRateHigh()).divide(new BigDecimal(100))));
				mv.addObject("task", task);
				mv.setViewName("task/viewProTask");
				break;
			case 49:
				//查询华创海外账户
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", task.getOrderId());
				paramMap.put("type", BusConstants.ORDERDETAILS_TYPE_HC_HWACC);
				hcAccT4 = orderService.getOrderDetailsByParams(paramMap);
				
				//查询上传图片信息
				paramMap.put("taskType", 48);
				ocList = orderCertService.getOrderCertList(paramMap);
				mv.addObject("ocList", ocList);
				
				//查询rmb兑美元汇率
				paramMap =  new HashMap<String,Object>();
				paramMap.put("status", BusConstants.RATE_STATUS_YES);
				paramMap.put("type", BusConstants.RATE_TYPE_RMB_TO_US);
				curRToURate = feeService.getCurrentRate(paramMap);
				
				mv.addObject("accInfo", hcAccT4);
				mv.addObject("payMoney", order.getApplyAmount().add(new BigDecimal(50000).multiply(curRToURate.getRateHigh()).divide(new BigDecimal(100))));
				mv.addObject("task", task);
				mv.setViewName("task/viewProTask");
				break;
			case 50:
				//查询华创海外账户 可重新上传收款凭证
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", task.getOrderId());
				paramMap.put("type", BusConstants.ORDERDETAILS_TYPE_HC_HWACC);
				hcAccT4 = orderService.getOrderDetailsByParams(paramMap);
				
				//查询上传图片信息
				paramMap.put("taskType", 48);
				ocList = orderCertService.getOrderCertList(paramMap);
				mv.addObject("ocList", ocList);
				
				//查询rmb兑美元汇率
				paramMap =  new HashMap<String,Object>();
				paramMap.put("status", BusConstants.RATE_STATUS_YES);
				paramMap.put("type", BusConstants.RATE_TYPE_RMB_TO_US);
				curRToURate = feeService.getCurrentRate(paramMap);
				
				mv.addObject("accInfo", hcAccT4);
				mv.addObject("payMoney", order.getApplyAmount().add(new BigDecimal(50000).multiply(curRToURate.getRateHigh()).divide(new BigDecimal(100))));
				mv.addObject("task", task);
				mv.setViewName("task/viewProTask");
				break;
			case 51:
				//查询华创海外账户
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", task.getOrderId());
				paramMap.put("type", BusConstants.ORDERDETAILS_TYPE_HC_HWACC);
				hcAccT4 = orderService.getOrderDetailsByParams(paramMap);
				
				//查询上传图片信息
				paramMap.put("taskType", 48);
				ocList = orderCertService.getOrderCertList(paramMap);
				mv.addObject("ocList", ocList);
				
				//查询rmb兑美元汇率
				paramMap =  new HashMap<String,Object>();
				paramMap.put("status", BusConstants.RATE_STATUS_YES);
				paramMap.put("type", BusConstants.RATE_TYPE_RMB_TO_US);
				curRToURate = feeService.getCurrentRate(paramMap);
				
				mv.addObject("accInfo", hcAccT4);
				mv.addObject("payMoney", order.getApplyAmount().add(new BigDecimal(50000).multiply(curRToURate.getRateHigh()).divide(new BigDecimal(100))));
				mv.addObject("task", task);
				mv.setViewName("task/viewProTask");
				break;
			case 52:
				//查询海外用户账户信息 上传打款凭证
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", task.getOrderId());
				paramMap.put("type", BusConstants.ORDERDETAILS_TYPE_HWUSER_LOCACC);
				hwUserAcc = orderService.getOrderDetailsByParams(paramMap);

				//查询上传图片信息
				paramMap.put("taskType",52);
				ocList = orderCertService.getOrderCertList(paramMap);
				mv.addObject("ocList", ocList);
				
				mv.addObject("accInfo", hwUserAcc);
				mv.addObject("payMoney", order.getPayAmount()+"￥");
				mv.addObject("task", task);
				mv.setViewName("task/viewProTask");
				break;
			case 53:
				//查询海外用户账户信息 上传打款凭证
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", task.getOrderId());
				paramMap.put("type", BusConstants.ORDERDETAILS_TYPE_HWUSER_LOCACC);
				hwUserAcc = orderService.getOrderDetailsByParams(paramMap);

				//查询上传图片信息
				paramMap.put("taskType",52);
				ocList = orderCertService.getOrderCertList(paramMap);
				mv.addObject("ocList", ocList);
				
				mv.addObject("accInfo", hwUserAcc);
				mv.addObject("payMoney", order.getPayAmount()+"￥");
				mv.addObject("task", task);
				mv.setViewName("task/viewProTask");
				break;
			case 54:
				//查询海外用户账户信息 可重新上传打款凭证
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", task.getOrderId());
				paramMap.put("type", BusConstants.ORDERDETAILS_TYPE_HWUSER_LOCACC);
				hwUserAcc = orderService.getOrderDetailsByParams(paramMap);

				//查询上传图片信息
				paramMap.put("taskType", 52);
				ocList = orderCertService.getOrderCertList(paramMap);
				mv.addObject("ocList", ocList);
				
				mv.addObject("accInfo", hwUserAcc);
				mv.addObject("payMoney", order.getPayAmount()+"￥");
				mv.addObject("task", task);
				mv.setViewName("task/viewProTask");
				break;
			case 55:
				//查询海外用户账户信息 
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", task.getOrderId());
				paramMap.put("type", BusConstants.ORDERDETAILS_TYPE_HWUSER_LOCACC);
				hwUserAcc = orderService.getOrderDetailsByParams(paramMap);

				//查询上传图片信息
				paramMap.put("taskType", 52);
				ocList = orderCertService.getOrderCertList(paramMap);
				mv.addObject("ocList", ocList);
				
				mv.addObject("accInfo", hwUserAcc);
				mv.addObject("payMoney", order.getPayAmount()+"￥");
				mv.addObject("task", task);
				mv.setViewName("task/viewProTask");
				break;
			case 56:
				//查询海外用户账户信息 上传收款凭证
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", task.getOrderId());
				paramMap.put("type", BusConstants.ORDERDETAILS_TYPE_HWUSER_LOCACC);
				hwUserAcc = orderService.getOrderDetailsByParams(paramMap);

				//查询上传图片信息
				paramMap.put("taskType", 56);
				ocList = orderCertService.getOrderCertList(paramMap);
				mv.addObject("ocList", ocList);
				
				mv.addObject("accInfo", hwUserAcc);
				mv.addObject("payMoney", order.getPayAmount()+"￥");
				mv.addObject("task", task);
				mv.setViewName("task/viewProTask");
				break;
			case 57:
				//查询海外用户账户信息
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", task.getOrderId());
				paramMap.put("type", BusConstants.ORDERDETAILS_TYPE_HWUSER_LOCACC);
				hwUserAcc = orderService.getOrderDetailsByParams(paramMap);

				//查询上传图片信息
				paramMap.put("taskType", 56);
				ocList = orderCertService.getOrderCertList(paramMap);
				mv.addObject("ocList", ocList);
				
				mv.addObject("accInfo", hwUserAcc);
				mv.addObject("payMoney", order.getPayAmount()+"￥");
				mv.addObject("task", task);
				mv.setViewName("task/viewProTask");
				break;
			case 58:
				//查询海外用户账户信息 可重新上传凭证
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", task.getOrderId());
				paramMap.put("type", BusConstants.ORDERDETAILS_TYPE_HWUSER_LOCACC);
				hwUserAcc = orderService.getOrderDetailsByParams(paramMap);

				//查询上传图片信息
				paramMap.put("taskType", 56);
				ocList = orderCertService.getOrderCertList(paramMap);
				mv.addObject("ocList", ocList);
				
				mv.addObject("accInfo", hwUserAcc);
				mv.addObject("payMoney", order.getPayAmount()+"￥");
				mv.addObject("task", task);
				mv.setViewName("task/viewProTask");
				break;
			case 59:
				//查询海外用户账户信息
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", task.getOrderId());
				paramMap.put("type", BusConstants.ORDERDETAILS_TYPE_HWUSER_LOCACC);
				hwUserAcc = orderService.getOrderDetailsByParams(paramMap);

				//查询上传图片信息
				paramMap.put("taskType", 56);
				ocList = orderCertService.getOrderCertList(paramMap);
				mv.addObject("ocList", ocList);
				
				mv.addObject("accInfo", hwUserAcc);
				mv.addObject("payMoney", order.getPayAmount()+"￥");
				mv.addObject("task", task);
				mv.setViewName("task/viewProTask");
				break;
			case 60:
				//查询海外账户信息 上传打款凭证
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", task.getOrderId());
				paramMap.put("type", BusConstants.ORDERDETAILS_TYPE_CUSTOMER_HWACC);
				hwAcc = orderService.getOrderDetailsByParams(paramMap);
				
				//查询上传图片信息
				paramMap.put("taskType", 60);
				ocList = orderCertService.getOrderCertList(paramMap);
				mv.addObject("ocList", ocList);
				
				mv.addObject("accInfo", hwAcc);
				mv.addObject("payMoney", order.getApplyAmount()+"$");
				mv.addObject("task", task);
				mv.setViewName("task/viewProTask");
				break;
			case 61:
				//查询海外账户信息
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", task.getOrderId());
				paramMap.put("type", BusConstants.ORDERDETAILS_TYPE_CUSTOMER_HWACC);
				hwAcc = orderService.getOrderDetailsByParams(paramMap);
				
				//查询上传图片信息
				paramMap.put("taskType", 60);
				ocList = orderCertService.getOrderCertList(paramMap);
				mv.addObject("ocList", ocList);
				
				mv.addObject("accInfo", hwAcc);
				mv.addObject("payMoney", order.getApplyAmount()+"$");
				mv.addObject("task", task);
				mv.setViewName("task/viewProTask");
				break;
			case 62:
				//查询海外账户信息 可重新上传打款凭证
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", task.getOrderId());
				paramMap.put("type", BusConstants.ORDERDETAILS_TYPE_CUSTOMER_HWACC);
				hwAcc = orderService.getOrderDetailsByParams(paramMap);
				
				//查询上传图片信息
				paramMap.put("taskType", 60);
				ocList = orderCertService.getOrderCertList(paramMap);
				mv.addObject("ocList", ocList);
				
				mv.addObject("accInfo", hwAcc);
				mv.addObject("payMoney", order.getApplyAmount()+"$");
				mv.addObject("task", task);
				mv.setViewName("task/viewProTask");
				break;
			case 63:
				//查询海外账户信息
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", task.getOrderId());
				paramMap.put("type", BusConstants.ORDERDETAILS_TYPE_CUSTOMER_HWACC);
				hwAcc = orderService.getOrderDetailsByParams(paramMap);
				
				//查询上传图片信息
				paramMap.put("taskType", 60);
				ocList = orderCertService.getOrderCertList(paramMap);
				mv.addObject("ocList", ocList);
				
				mv.addObject("accInfo", hwAcc);
				mv.addObject("payMoney", order.getApplyAmount()+"$");
				mv.addObject("task", task);
				mv.setViewName("task/viewProTask");
				break;
			case 64:
				//查询海外账户信息 上传收款凭证
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", task.getOrderId());
				paramMap.put("type", BusConstants.ORDERDETAILS_TYPE_CUSTOMER_HWACC);
				hwAcc = orderService.getOrderDetailsByParams(paramMap);
				
				//查询上传图片信息
				paramMap.put("taskType", 64);
				ocList = orderCertService.getOrderCertList(paramMap);
				mv.addObject("ocList", ocList);
				
				mv.addObject("accInfo", hwAcc);
				mv.addObject("payMoney", order.getApplyAmount()+"$");
				mv.addObject("task", task);
				mv.setViewName("task/viewProTask");
				break;
			case 65:
				//查询海外账户信息
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", task.getOrderId());
				paramMap.put("type", BusConstants.ORDERDETAILS_TYPE_CUSTOMER_HWACC);
				hwAcc = orderService.getOrderDetailsByParams(paramMap);
				
				//查询上传图片信息
				paramMap.put("taskType", 64);
				ocList = orderCertService.getOrderCertList(paramMap);
				mv.addObject("ocList", ocList);
				
				mv.addObject("accInfo", hwAcc);
				mv.addObject("payMoney", order.getApplyAmount()+"$");
				mv.addObject("task", task);
				mv.setViewName("task/viewProTask");
				break;
			case 66:
				//查询海外账户信息 可重新上传收款凭证
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", task.getOrderId());
				paramMap.put("type", BusConstants.ORDERDETAILS_TYPE_CUSTOMER_HWACC);
				hwAcc = orderService.getOrderDetailsByParams(paramMap);
				
				paramMap.put("taskType", 64);
				ocList = orderCertService.getOrderCertList(paramMap);
				mv.addObject("ocList", ocList);
				
				mv.addObject("accInfo", hwAcc);
				mv.addObject("payMoney", order.getApplyAmount()+"$");
				mv.addObject("task", task);
				mv.setViewName("task/viewProTask");
				break;
			case 67:
				//查询海外账户信息
				paramMap =  new HashMap<String,Object>();
				paramMap.put("orderId", task.getOrderId());
				paramMap.put("type", BusConstants.ORDERDETAILS_TYPE_CUSTOMER_HWACC);
				hwAcc = orderService.getOrderDetailsByParams(paramMap);
				
				//查询上传图片信息
				paramMap.put("taskType", 64);
				ocList = orderCertService.getOrderCertList(paramMap);
				mv.addObject("ocList", ocList);
				
				mv.addObject("accInfo", hwAcc);
				mv.addObject("payMoney", order.getApplyAmount()+"$");
				mv.addObject("task", task);
				mv.setViewName("task/viewProTask");
				break;
			}
			
			return mv;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("待办处理页面异常!");
		}
	}
}
