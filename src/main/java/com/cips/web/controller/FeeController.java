package com.cips.web.controller;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cips.constants.GlobalPara;
import com.cips.model.Poundage;
import com.cips.model.Rate;
import com.cips.model.User;
import com.cips.service.FeeService;
import com.cips.util.PKIDUtils;

@RequestMapping("/fee")
@Controller
public class FeeController {
	
	@Resource(name="feeService")
	private FeeService feeService;
	
	@RequestMapping("/toPageRateManage")
	public ModelAndView toRateManage(HttpServletRequest request,HttpServletResponse response){
		ModelAndView mv = new ModelAndView();
		List<Rate> rateList = feeService.getRateList();
		mv.addObject("rateList", rateList);
		mv.setViewName("fee/rateManage");
		return mv;
	}
	
	@RequestMapping("/insertRate")
	@ResponseBody
	public Map<String,Object> insertRate(HttpServletRequest request,HttpServletResponse response,@RequestParam("rateHigh") String rateHigh,@RequestParam("type") String type){
		Map<String,Object> resultMap = new HashMap<String,Object>();
		User loginUser = (User)request.getSession().getAttribute(GlobalPara.USER_SESSION_TOKEN);
		Rate rate = new Rate();
		rate.setId(PKIDUtils.getUuid());
		rate.setRateHigh(new BigDecimal(rateHigh));
		rate.setStatus(0);
		rate.setType(Integer.parseInt(type));
		rate.setCreatedId(loginUser.getId());
		rate.setCreatedDate(new Date());
		
		//插入一条新的汇率时，需先将正在启用的汇率作废
		Map<String,Object> uMap = new HashMap<String,Object>();
		uMap.put("status", 1);
		uMap.put("type", type);
		uMap.put("modifiedId", loginUser.getId());
		uMap.put("modifiedDate", new Date());
		
		try {
			feeService.updatePoundageByStatus(uMap);
			feeService.insertRate(rate);
			resultMap.put("success", true);
		} catch (Exception e) {
			// TODO: handle exception
			resultMap.put("success", false);
			resultMap.put("error", e.getMessage());
		}
		
		return resultMap;
	}
	
	@RequestMapping("/toPoundageManage")
	public ModelAndView toPoundageManage(HttpServletRequest request,HttpServletResponse response){
		ModelAndView mv = new ModelAndView();
		//当前汇率
		mv.setViewName("fee/poundageManage");
		return mv;
	}
	
	@RequestMapping("/insertPoundage")
	@ResponseBody
	public Map<String,Object> insertPoundage(HttpServletRequest request,HttpServletResponse response,@RequestParam("pRatio") String pRatio){
		Map<String,Object> resultMap = new HashMap<String,Object>();
		User loginUser = (User)request.getSession().getAttribute(GlobalPara.USER_SESSION_TOKEN);
		BigDecimal poundageRatio = new BigDecimal(pRatio);
		Poundage poundage = new Poundage();
		poundage.setId(PKIDUtils.getUuid());
		poundage.setPoundageRatio(poundageRatio);
		poundage.setStatus(0);
		poundage.setCreatedId(loginUser.getId());
		poundage.setCreatedDate(new Date());
		
		Map<String,Object> uMap = new HashMap<String,Object>();
		uMap.put("status", 1);
		uMap.put("modifiedId", loginUser.getId());
		uMap.put("modifiedDate", new Date());
		
		try {
			feeService.updatePoundageByStatus(uMap);//先禁用表中正在启用的手续费比例再进行插入
			feeService.insertPoundage(poundage);
			resultMap.put("success", true);
		} catch (Exception e) {
			// TODO: handle exception
			resultMap.put("success", false);
			resultMap.put("error", e.getMessage());
		}
		return resultMap;
	}
}
