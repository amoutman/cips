package com.cips.web.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.cips.model.Rate;
import com.cips.service.FeeService;

@RequestMapping("/fee")
@Controller
public class FeeController {
	
	@Resource(name="feeService")
	private FeeService feeService;
	
	@RequestMapping("/toRateManage")
	public ModelAndView toRateManage(HttpServletRequest request,HttpServletResponse response){
		ModelAndView mv = new ModelAndView();
		List<Rate> rateList = feeService.getRateList();
		mv.addObject("rateList", rateList);
		mv.setViewName("fee/rateManage");
		return mv;
	}
}
