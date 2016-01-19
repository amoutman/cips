package com.cips.web.controller;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.druid.util.StringUtils;
import com.cips.constants.GlobalPara;
import com.cips.model.AccountFr;
import com.cips.model.User;
import com.cips.page.Pager;
import com.cips.service.AccountFrService;
import com.cips.service.UserService;
import com.cips.util.PKIDUtils;

@RequestMapping("/accountFr")
@Controller
public class AccountFrController {
	
	@Resource 
	private AccountFrService accountFrService;
	
	@Resource
	private UserService userService;

	@RequestMapping("/toPageAccountFrManage")
	public ModelAndView toAccountFrManage(HttpServletRequest request,HttpServletResponse response,@ModelAttribute("accountFr") AccountFr accountFr) throws Exception{
		ModelAndView mv = new ModelAndView();
		Map<String,Object> paraMap = new HashMap<String,Object>();
		//分页条件
		Pager pager = (Pager)request.getAttribute(GlobalPara.PAGER_SESSION);
		paraMap.put(GlobalPara.PAGER_SESSION, pager);
		Field[] fields = AccountFr.class.getDeclaredFields();
		for(Field field:fields){
			field.setAccessible(true); // 设置些属性是可以访问的
			Object val = field.get(accountFr);
			if(val == null){
				continue;
			}
			paraMap.put(field.getName(), val);
		}
		List<AccountFr> afList = accountFrService.getAccountFrList(paraMap);
		mv.addObject(GlobalPara.PAGER_SESSION, pager);
		mv.addObject("afList", afList);
		mv.setViewName("accountFr/accountFrManage");
		return mv;
	}
	
	@RequestMapping("/toPageAccountFrMap")
	@ResponseBody
	public Map<String,Object> toAccountFrMap(HttpServletRequest request,HttpServletResponse response,@ModelAttribute(value="accountFr") AccountFr accountFr) throws Exception{
		Map<String,Object> resultMap = new HashMap<String,Object>();
		Map<String,Object> paraMap = new HashMap<String,Object>();
		//分页条件
		Pager pager = (Pager)request.getAttribute(GlobalPara.PAGER_SESSION);
		paraMap.put(GlobalPara.PAGER_SESSION, pager);
		Field[] fields = AccountFr.class.getDeclaredFields();
		for(Field field:fields){
			field.setAccessible(true); // 设置些属性是可以访问的
			Object val = field.get(accountFr);
			if(val == null || val == ""){
				continue;
			}
			paraMap.put(field.getName(), val);
		}
		List<AccountFr> afList = accountFrService.getAccountFrList(paraMap);
		//String afString = JSONArray.fromObject(afList).toString();   
		resultMap.put(GlobalPara.PAGER_SESSION, pager);
		resultMap.put("afList", afList);
		return resultMap;
	}
	
	@RequestMapping("/toPageInsertAcccountFr")
	public ModelAndView toInsertAcccountFr(HttpServletRequest request,HttpServletResponse response,@RequestParam(value="userInfo", required=false) String userInfo){
		ModelAndView mv = new ModelAndView();
		Map<String,Object> map = new HashMap<String,Object>();
		//分页条件
		Pager pager = (Pager)request.getAttribute(GlobalPara.PAGER_SESSION);

		if(!StringUtils.isEmpty(userInfo)){
			map.put("userInfo", userInfo);
		}
		map.put(GlobalPara.PAGER_SESSION, pager);
		map.put("roleName", "海外用户");
		List<User> frUserList = userService.toPageGetFrUserList(map);
		mv.addObject("frUserList", frUserList);
		mv.addObject(GlobalPara.PAGER_SESSION, pager);
		mv.setViewName("accountFr/addAccountFr");
		return mv;
	}
	
	@RequestMapping("/insertAccountFr")
	public ModelAndView insertAccountFr(HttpServletRequest request,HttpServletResponse response,@ModelAttribute("accountFr") AccountFr accountFr){
		ModelAndView mv = new ModelAndView();
		User loginUser = (User)request.getSession().getAttribute(GlobalPara.USER_SESSION_TOKEN);
		accountFr.setId(PKIDUtils.getUuid());
		accountFr.setCreatedId(loginUser.getCreditId());
		accountFr.setCreatedDate(new Date());
		try {
			accountFrService.insertAccountFr(accountFr);
		} catch (Exception e) {
			// TODO: handle exception
		}
		mv.setViewName("redirect:toPageAccountFrManage");
		return mv;
	}
	
	@RequestMapping("/updateAccountFr")
	public ModelAndView updateAccountFr(HttpServletRequest request,HttpServletResponse response,@ModelAttribute("accountFr") AccountFr accountFr){
		ModelAndView mv = new ModelAndView();
		User loginUser = (User)request.getSession().getAttribute(GlobalPara.USER_SESSION_TOKEN);
		accountFr.setModifiedId(loginUser.getId());
		accountFr.setModifiedDate(new Date());
		try {
			accountFrService.updateAccountFr(accountFr);
		} catch (Exception e) {
			// TODO: handle exception
		}
		mv.setViewName("redirect:toPageAccountFrManage");
		return mv;
	}
	
	@RequestMapping("/validateAccountCode")
	public void validateAccountCode(HttpServletRequest request,HttpServletResponse response,@RequestParam("accountCode") String accountCode) throws IOException{
		AccountFr accountFr = accountFrService.getAccountFrByCode(accountCode);
		if(accountFr != null){
			response.getWriter().print(false);
		}else{
			response.getWriter().println(true);
		}
	}
}
