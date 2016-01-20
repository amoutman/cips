package com.cips.web.controller;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cips.constants.BusConstants;
import com.cips.constants.GlobalPara;
import com.cips.model.Dictionary;
import com.cips.model.Order;
import com.cips.model.OrderDetails;
import com.cips.model.OrderOperate;
import com.cips.model.Poundage;
import com.cips.model.Rate;
import com.cips.model.Task;
import com.cips.model.User;
import com.cips.service.DictionaryService;
import com.cips.service.FeeService;
import com.cips.service.OrderService;
import com.cips.service.RoleService;
import com.cips.service.TaskService;
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
			Poundage poundage = feeService.getCurrPoundage(BusConstants.POUNDAGE_STATUS_YES);
			mv.addObject("rate", rate);
			mv.addObject("poundage", poundage);
			mv.setViewName("order/preCreateOrder");
			return mv;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("前往订单申请页面异常!");
		}
	}
	
	@RequestMapping(value = "/createOrder")
	public ModelAndView createOrder(Order order, OrderDetails orderDetails, HttpServletRequest request){
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
			order.setPayAmount(order.getApplyAmount().multiply(curUToRRate.getRateHigh()));
			//手续费 先获取收费标准再进行计算
			Poundage curPoundage = feeService.getCurrPoundage(BusConstants.POUNDAGE_STATUS_YES);
			order.setPoundageRatio(order.getApplyAmount().multiply(curPoundage.getPoundageRatio()));
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
			
			//订单生成
			orderService.createOrder(order, orderDetails, oOperate, task);
			
			return new ModelAndView("redirect:/order/toPageOrders"); 
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("订单申请提交异常，请重试!");
		}
	}
	
	/**
	 * 订单查询
	 */
	@RequestMapping(value = "/toPageOrders")
	public ModelAndView toPageOrderListByParams(HttpServletRequest request, Order order){
		try {
			ModelAndView mv = new ModelAndView();
			//获取客户用户名userId
			User user = (User) request.getSession().getAttribute(GlobalPara.USER_SESSION_TOKEN);
			//查询参数
			Map<String, Object> params = new HashMap<String, Object>();
			order.setApplyId(user.getId());
			params.put("order", order);
			//分页查询
			List<Order> orders = orderService.toPageOrderListByParams(params);
			mv.addObject("orders", orders);
			mv.setViewName("order/toPageOrders");
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
	public Object deleteOrder(@RequestParam("orderId")String orderId){
		Map<String,Object> map = new HashMap<String,Object>();
		try {
			Order order = orderService.getOrderById(orderId);
			if(BusConstants.ORDER_STATUS_COMMIT.equals(order.getStatus())){
				order.setStatus(BusConstants.ORDER_STATUS_DELETE);
				orderService.updateOrderById(order);
				map.put(GlobalPara.AJAX_KEY, GlobalPara.AJAX_SUCCESS);
			}else{
				map.put(GlobalPara.AJAX_KEY, "订单已受理无法删除！");
			}
			
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			map = new HashMap<String,Object>();
			map.put(GlobalPara.AJAX_KEY, "订单删除失败，请重试！");
			return map;
		}
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
