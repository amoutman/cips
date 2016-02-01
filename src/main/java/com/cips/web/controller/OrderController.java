package com.cips.web.controller;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cips.constants.BusConstants;
import com.cips.constants.EnumConstants.OrderStsEnum;
import com.cips.constants.GlobalPara;
import com.cips.model.Amount;
import com.cips.model.Dictionary;
import com.cips.model.Order;
import com.cips.model.OrderDetails;
import com.cips.model.OrderOperate;
import com.cips.model.Rate;
import com.cips.model.Role;
import com.cips.model.Task;
import com.cips.model.User;
import com.cips.page.Pager;
import com.cips.service.DictionaryService;
import com.cips.service.FeeService;
import com.cips.service.OrderService;
import com.cips.service.RoleService;
import com.cips.service.TaskService;
import com.cips.service.UserService;
import com.cips.util.PKIDUtils;
import com.cips.util.PropertiesUtil;

@Controller
@RequestMapping("/order")
public class OrderController {
	
	@Resource(name="taskService")
	private TaskService taskService;
	
	@Resource(name="dictionaryService")
	private DictionaryService dictionaryService;
	
	@Resource(name="orderService")
	private OrderService orderService;
	
	@Resource(name="feeService")
	private FeeService feeService;
	
	@Resource(name="roleService")
	private RoleService roleService;
	
	@Resource(name="userService")
	private UserService userService;


	/**
	 * 订单申请
	 */
	@RequestMapping(value = "/preCreateOrder")
	public ModelAndView preCreateOrder(){
		try {
			ModelAndView mv = new ModelAndView();
			//获取汇率 $兑RMB
			Map<String,Object> paramMap = new HashMap<String,Object>();
			paramMap.put("status", BusConstants.RATE_STATUS_YES);
			paramMap.put("type", BusConstants.RATE_TYPE_US_TO_RMB);
			Rate rate = feeService.getCurrentRate(paramMap);
			//获取手续费收费标准
			//Poundage poundage = feeService.getCurrPoundage(BusConstants.POUNDAGE_STATUS_YES);
			mv.addObject("rate", rate);
			//mv.addObject("poundage", poundage);
			mv.setViewName("order/preCreateOrder");
			return mv;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("前往订单申请页面异常!");
		}
	}
	
	@ResponseBody
	@RequestMapping(value = "/createOrder")
	public Object createOrder(Order order, OrderDetails orderDetails, HttpServletRequest request){
		Map<String,Object> map = new HashMap<String,Object>();
		try {
			//获取客户用户名userId
			User user = (User) request.getSession().getAttribute(GlobalPara.USER_SESSION_TOKEN);
			/** 订单基本信息 */
			//订单ID
			order.setId(PKIDUtils.getUuid());
			//订单编号
			order.setOrderNo(getOrderNo());
			//订单使用汇率 分别设置$对RMB RMB对$
			Map<String,Object> paramMap =  new HashMap<String,Object>();
			paramMap.put("status", BusConstants.RATE_STATUS_YES);
			paramMap.put("type", BusConstants.RATE_TYPE_RMB_TO_US);
			Rate curRToURate = feeService.getCurrentRate(paramMap);
			paramMap.put("type", BusConstants.RATE_TYPE_US_TO_RMB);
			Rate curUToRRate = feeService.getCurrentRate(paramMap);
			order.setExchangeRateRmb(curRToURate.getRateHigh());
			order.setExchangeRateUsd(curUToRRate.getRateHigh());
			//订单应付金额 先获取汇率再进行计算
			order.setPayAmount(order.getApplyAmount().multiply(curUToRRate.getRateHigh()).divide(new BigDecimal(100), 2));
			//手续费 先获取收费标准再进行计算
			//Poundage curPoundage = feeService.getCurrPoundage(BusConstants.POUNDAGE_STATUS_YES);
			//order.setPoundageRatio(order.getApplyAmount().multiply(curPoundage.getPoundageRatio()));
			//订单提交人
			order.setApplyId(user.getId());
			//订单提交时间
			order.setApplyDate(new Date());
			//订单状态
			order.setStatus(BusConstants.ORDER_STATUS_COMMIT);
			//订单修改人
			order.setModifiedId(user.getId());
			//订单修改时间
			order.setModifiedDate(new Date());
			
			//OrderDetail表
			orderDetails.setId(PKIDUtils.getUuid());
			orderDetails.setOrderId(order.getId());
			orderDetails.setType(BusConstants.ORDERDETAILS_TYPE_CUSTOMER_HWACC);
			orderDetails.setTaskType(BusConstants.TASK_TYPE_COMMIT);
			

			/**订单日志记录*/
			OrderOperate oOperate = new OrderOperate();
			oOperate.setId(PKIDUtils.getUuid());
			oOperate.setOrderId(order.getId());
			oOperate.setStatus(order.getStatus());
			oOperate.setOperatedId(user.getId());
			oOperate.setOpEndTime(order.getApplyDate());
			oOperate.setOpSequence(0);
			
			/**向平台操作员发送待办*/
			Task task = taskService.initNewTask(order.getId(), BusConstants.TASK_TYPE_COMMIT);
			task.setOrderStatus(order.getStatus());
			
			/**tb_account_amount 插入记录用来维护撮合进度 */
			Amount amount = new Amount();
			amount.setId(PKIDUtils.getUuid());
			amount.setOrderId(order.getId());
			amount.setAmountTotal(new BigDecimal(0));
			amount.setCreatedId(user.getId());
			amount.setCreatedDate(new Date());
			amount.setModifiedId(user.getId());
			amount.setModifiedDate(new Date());
			
			//订单生成
			orderService.createOrder(order, orderDetails, oOperate, task, amount);
			
			map.put(GlobalPara.AJAX_KEY, GlobalPara.AJAX_SUCCESS);
			return map; 
		} catch (Exception e) {
			e.printStackTrace();
			map = new HashMap<String,Object>();
			map.put(GlobalPara.AJAX_KEY, "订单申请模块异常，请重试！");
			return map;
		}
	}
	
	/**
	 * 订单查询
	 */
	@RequestMapping(value = "/toPageOrders")
	public ModelAndView toPageOrderListByParams(HttpServletRequest request, Order order, @RequestParam(value="roleType",required=false)String roleType){
		try {
			ModelAndView mv = new ModelAndView();
			//分页条件
			Pager pager = (Pager)request.getAttribute(GlobalPara.PAGER_SESSION);
			//获取客户用户名userId
			User user = (User) request.getSession().getAttribute(GlobalPara.USER_SESSION_TOKEN);
			//获取用户角色
			List<Role> roles = roleService.getRoleListByUserId(user.getId());
			//查询参数
			Map<String, Object> params = new HashMap<String, Object>();
			params.put(GlobalPara.PAGER_SESSION, pager);
			params.put("order", order);
			for (Role role : roles) {
				if(GlobalPara.RNAME_SUPER_ADMIN.equals(role.getRoleName()) || GlobalPara.RNAME_PL_CHECKER.equals(role.getRoleName()) 
						|| GlobalPara.RNAME_PL_OPERATOR.equals(role.getRoleName())){
					mv.setViewName("order/toPageOrders");
				}else if(GlobalPara.RNAME_HC_OPERATOR.equals(role.getRoleName())){
					mv.setViewName("order/toPageOrdersForHc");
					if(StringUtils.isBlank(roleType) || roleType.equals("1")){
						order.setApplyId(user.getId());
					}else if(roleType.equals("2")){
						Map<String, Object> param = new HashMap<String, Object>();
						param.put("taskType", BusConstants.TASK_TYPE_FIRST_HCPAY);
						param.put("operatedId", user.getId());
						List<Task> tasks = taskService.getTasksByParams(param);
						List<String> orderIds = new ArrayList<String>();
						for (Task task : tasks) {
					        if(!orderIds.contains(task.getOrderId())){  
					        	orderIds.add(task.getOrderId()); 
					        } 
						}
						params.put("orderIds", orderIds);
					}
				}else{
					order.setApplyId(user.getId());
					mv.setViewName("order/toPageOrders");
				}
			}
			//分页查询
			List<Order> orders = orderService.toPageOrderListByParams(params);
			for (Order o : orders) {
				o.setStatusDesc(OrderStsEnum.getNameByCode(o.getStatus().toString()));
				BigDecimal matchAmount = null;
				if(o.getHcApplyAmount() == null){
					matchAmount = o.getMatchAmount().multiply(new BigDecimal(100)).divide(o.getApplyAmount(), 0, BigDecimal.ROUND_HALF_UP);
				}else{
					matchAmount = o.getMatchAmount().multiply(new BigDecimal(100)).divide(o.getApplyAmount().add(o.getHcApplyAmount()), 0, BigDecimal.ROUND_HALF_UP);
				}
				
				o.setMatchPercent(matchAmount + "%");
			}
			
			mv.addObject("order", order);
			mv.addObject("orders", orders);
			mv.addObject("orderNum", orders.size());
			mv.addObject("roleType", roleType);
			mv.addObject("pager", pager);
			return mv;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("订单查询页面异常，请重试!");
		}
	}
	
	/**
	 * 订单删除（逻辑删除）
	 */
	@ResponseBody
	@RequestMapping(value = "/delOrder")
	public Object deleteOrder(HttpServletRequest request, @RequestParam("orderId")String orderId){
		Map<String,Object> map = new HashMap<String,Object>();
		try {
			//获取客户用户名userId
			User user = (User) request.getSession().getAttribute(GlobalPara.USER_SESSION_TOKEN);
			//获取创建订单后生成的待办
			Map<String,Object> paramMap =  new HashMap<String,Object>();
			paramMap.put("orderId", orderId);
			paramMap.put("taskType", BusConstants.TASK_TYPE_COMMIT);
			Task task = taskService.getTaskByParams(paramMap);
			String msg = taskService.processingTaskById(task.getId(), user.getId(), BusConstants.TASK_PRO_TYPE_DELETE);
			if(msg == null){
				map.put(GlobalPara.AJAX_KEY, GlobalPara.AJAX_SUCCESS);
			}else{
				map.put(GlobalPara.AJAX_KEY, msg);
			}
			return map;
		} catch (Exception e) {
			e.printStackTrace();
			map = new HashMap<String,Object>();
			map.put(GlobalPara.AJAX_KEY, "订单删除失败，请重试！");
			return map;
		}
	}
	
	/**
	 * 查看订单
	 * @param orderId
	 * @return
	 */
	@RequestMapping(value = "/viewOrder")
	public ModelAndView viewOrder(@RequestParam("orderId")String orderId){
		try {
			ModelAndView mv = new ModelAndView();
			Order order = orderService.getOrderById(orderId);
			order.setStatusDesc(OrderStsEnum.getNameByCode(order.getStatus().toString()));
			BigDecimal matchAmount = null;
			if(order.getHcApplyAmount() == null){
				matchAmount = order.getMatchAmount().multiply(new BigDecimal(100)).divide(order.getApplyAmount(), 0, BigDecimal.ROUND_HALF_UP);
			}else{
				matchAmount = order.getMatchAmount().multiply(new BigDecimal(100)).divide(order.getApplyAmount().add(order.getHcApplyAmount()), 0, BigDecimal.ROUND_HALF_UP);
			}
			order.setMatchPercent(matchAmount + "%");
			User user = userService.getUserByUserId(order.getApplyId());
			//获取海外账户信息
			Map<String,Object> paramMap =  new HashMap<String,Object>();
			paramMap.put("orderId", order.getId());
			paramMap.put("type", BusConstants.ORDERDETAILS_TYPE_CUSTOMER_HWACC);
			OrderDetails hwAcc = orderService.getOrderDetailsByParams(paramMap);
			
			mv.addObject("hwAcc", hwAcc);
			mv.addObject("user", user);
			mv.addObject("order", order);
			mv.setViewName("order/viewOrder");
			return mv;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("订单查看页面异常，请重试!");
		}
	}
	
	/**
	 * 撮合金额维护
	 */
	@RequestMapping(value = "/toPageMatchOrderAmount")
	public ModelAndView toPageMatchOrderAmount(HttpServletRequest request, Order order){
		try {
			ModelAndView mv = new ModelAndView();
			//分页条件
			Pager pager = (Pager)request.getAttribute(GlobalPara.PAGER_SESSION);
			//获取客户用户名userId
			User user = (User) request.getSession().getAttribute(GlobalPara.USER_SESSION_TOKEN);
			//获取用户角色
			List<Role> roles = roleService.getRoleListByUserId(user.getId());
			//查询参数
			Map<String, Object> params = new HashMap<String, Object>();
			params.put(GlobalPara.PAGER_SESSION, pager);
//			for (Role role : roles) {
//				if(GlobalPara.RNAME_SUPER_ADMIN.equals(role.getRoleName()) || GlobalPara.RNAME_PL_CHECKER.equals(role.getRoleName()) 
//						|| GlobalPara.RNAME_PL_OPERATOR.equals(role.getRoleName())){
//				}else{
//					order.setApplyId(user.getId());
//				}
//			}
			order.setStatus(0);
			params.put("order", order);
			//分页查询
			List<Order> orders = orderService.toPageMatchOrderListByParams(params);
			for (Order o : orders) {
				o.setStatusDesc(OrderStsEnum.getNameByCode(o.getStatus().toString()));
				//查询申请金额是不是100%匹配
				BigDecimal totalAmount = o.getApplyAmount().add(o.getHcApplyAmount());
				if(totalAmount.equals(o.getMatchAmount())){
					o.setIsMatch(1);
				}else{
					o.setIsMatch(0);
				}
			}
			
			mv.addObject("order", order);
			mv.addObject("orders", orders);
			mv.addObject("orderNum", orders.size());
			mv.addObject("pager", pager);
			mv.setViewName("order/toPageChangeOrderAmount");
			return mv;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("订单查询页面异常，请重试!");
		}
	}
	
	/**
	 * 查看订单
	 * @param orderId
	 * @return
	 */
	@RequestMapping(value = "/viewMatchOrderAmount")
	public ModelAndView viewMatchOrderAmount(@RequestParam("orderId")String orderId){
		try {
			ModelAndView mv = new ModelAndView();
			Order order = orderService.selectOrderByOrderId(orderId);
			order.setStatusDesc(OrderStsEnum.getNameByCode(order.getStatus().toString()));
			User user = userService.getUserByUserId(order.getApplyId());
			//获取海外账户信息
			Map<String,Object> paramMap =  new HashMap<String,Object>();
			paramMap.put("orderId", order.getId());
			paramMap.put("type", BusConstants.ORDERDETAILS_TYPE_CUSTOMER_HWACC);
			OrderDetails hwAcc = orderService.getOrderDetailsByParams(paramMap);
			
			BigDecimal mMatchAmount = order.getApplyAmount().add(order.getHcApplyAmount());
			
			mv.addObject("mMatchAmount", mMatchAmount);
			mv.addObject("hwAcc", hwAcc);
			mv.addObject("user", user);
			mv.addObject("order", order);
			mv.setViewName("order/matchOrderAmount");
			return mv;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("订单查看页面异常，请重试!");
		}
	}
	
	@RequestMapping(value="/updateMatchAmount",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> updateMatchAmount(HttpServletRequest request,@RequestParam("orderId")String orderId,@RequestParam("matchAmount")String matchAmount){
		Map<String,Object> resultMap = new HashMap<String,Object>();
		//获取客户用户名userId
		User user = (User) request.getSession().getAttribute(GlobalPara.USER_SESSION_TOKEN);
		
		Amount uAmount = new Amount();
		uAmount.setAmountTotal(new BigDecimal(matchAmount));
		uAmount.setOrderId(orderId);
		uAmount.setModifiedId(user.getId());
		uAmount.setModifiedDate(new Date());
		
		try {
			orderService.updateMatchAmountByOrderId(uAmount);
			resultMap.put(GlobalPara.AJAX_KEY, GlobalPara.AJAX_SUCCESS);
		} catch (Exception e) {
			// TODO: handle exception
			resultMap.put(GlobalPara.AJAX_KEY, "更新失败");
		}
		return resultMap;
	}
	
	/**
	 * 生成订单号
	 */
	private synchronized String getOrderNo() throws Exception{
		
		StringBuffer sb = new StringBuffer();
		sb.append(new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()));
		if(PropertiesUtil.getPropertiesValue("machineId")==null){
			PropertiesUtil.loadFile("config.properties");
		}
		String machineId = PropertiesUtil.getPropertiesValue("machineId");
		sb.append(machineId);
		//8为序列号.
		Dictionary dictionary = dictionaryService.getDictionaryByCode(GlobalPara.DICTIONARY_ORDER_CODE);
		if(dictionary == null){
			dictionary = new Dictionary();
			dictionary.setId(PKIDUtils.getUuid());
			dictionary.setCode(GlobalPara.DICTIONARY_ORDER_CODE);
			dictionary.setName("订单编号序列号");
			dictionary.setVal("00000001");
			dictionaryService.saveDictionary(dictionary);
		}else{
			Integer val = Integer.valueOf(dictionary.getVal());
			val++;
			String newVal = val.toString();
			for(int i = 0 ; i < 8 - newVal.length(); i++){
				newVal = "0" + newVal;
			}
			dictionary.setVal(newVal);
			dictionaryService.updateDictionary(dictionary);
		}
		sb.append(dictionary.getVal());
		return sb.toString();
	}
}
