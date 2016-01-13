package com.cips.web.controller;

import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cips.constants.GlobalPara;
import com.cips.model.Menu;
import com.cips.model.Role;
import com.cips.model.User;
import com.cips.service.MenuService;
import com.cips.service.RoleService;
import com.cips.service.UserService;
import com.cips.util.MD5;
import com.cips.util.PKIDUtils;

@RequestMapping("/user")
@Controller
public class UserController {
	
	@Resource(name="userService")
	private UserService userService;
	@Resource(name="roleService")
	private RoleService roleService;
	@Resource(name="menuService")
	private MenuService menuService;
	
	@RequestMapping("/toIndexPage")
	public ModelAndView toIndexPage(HttpServletRequest request,HttpServletResponse response){
		ModelAndView mv = new ModelAndView();
		User loginUser = (User)request.getSession().getAttribute(GlobalPara.USER_SESSION_TOKEN);
		if(loginUser.getIsFirstLogin().equals(0)){
			mv.setViewName("redirect:user/toChangePassword");
		}else{
			mv.setViewName("redirect:");
		}
		return mv;
	}
	
	@RequestMapping("/toLogin")
	public ModelAndView toLogin(HttpServletRequest request,HttpServletResponse response){
		ModelAndView mv = new ModelAndView();
		return mv;
	}
	
	@RequestMapping(value="/login",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> login(HttpServletRequest request,HttpServletResponse response,@RequestParam("userName") String userName,@RequestParam("password") String password){
		Map<String,Object> resultMap = new HashMap<String,Object>();
		User user = new User();
		user.setUserName(userName);
		user.setPassword(password);
		try {
			User lUser = userService.loginUser(user);
			request.getSession().setAttribute(GlobalPara.USER_SESSION_TOKEN, lUser);
			List<Menu> menuList = getMenuListByRoleId(lUser.getRoleId());
			request.getSession().setAttribute(GlobalPara.MENU_SESSION, menuList);
			resultMap.put("success", true);
		} catch (Exception e) {
			// TODO: handle exception
			resultMap.put("success", false);
			resultMap.put("error", e.getMessage());
		}
		return resultMap;
	}

	@RequestMapping("/toPageUserManage")
	public ModelAndView toUserManage(HttpServletRequest request,HttpServletResponse response){
		ModelAndView mv = new ModelAndView();
		Map<String,Object> map = new HashMap<String,Object>();
		List<User> userList = userService.getUserList(map);
		List<User> nUserList = new ArrayList<User>();
		if(!userList.isEmpty()){
			for(User user:userList){
				String userId = user.getId();
				//查询用户的角色
				List<Role> urList = roleService.getRoleListByUserId(userId);
				String roleName = "";
				for(Role r:urList){
					if(roleName == ""){
						roleName = r.getRoleName();
					}else{
						roleName = ","+r.getRoleName();
					}
				}
				if(roleName!=""){
					user.setRoleNames(roleName);
				}
				user.setRoleList(urList);
				nUserList.add(user);
			}
			mv.addObject("userList", nUserList);
		}
		
		//查询系统中所有角色
		List<Role> roleList = roleService.getRoleList();
		if(!roleList.isEmpty()){
			mv.addObject("roleList", roleList);
		}	
		
		mv.setViewName("admin/userManage");
		return mv;
	}
	
	@RequestMapping("/toPageSelectUserInfo")
	public ModelAndView selectUserInfo(HttpServletRequest request,HttpServletResponse response,@RequestParam("userInfo") String userInfo){
		ModelAndView mv = new ModelAndView();
		List<User> userList = userService.selectUserInfo(userInfo);
		mv.addObject("userList", userList);
		mv.setViewName("admin/userManage");
		return mv;
	}
	
	@RequestMapping("/insertUser")
	@ResponseBody
	public Map<String,Object> insertUser(HttpServletRequest request,HttpServletResponse response,@ModelAttribute("user") User user){
		Map<String,Object> resultMap = new HashMap<String,Object>();
		User loginUser = (User)request.getSession().getAttribute(GlobalPara.USER_SESSION_TOKEN);
		user.setId(PKIDUtils.getUuid());
		user.setPassword(MD5.md5("12345678"));
		user.setStatus(0);
		user.setIsFirstLogin(0);
		user.setCreatedId(loginUser.getId());
		user.setCreatedDate(new Date());
		
		//添加用户角色
		String roleIds = user.getRoleId();
		if(roleIds.length()>32){
			String[] roleIdArray = roleIds.split(",");
			
		}else{
			
		}
		
		resultMap.put("success", true);
		try {
			userService.insertUser(user);
		} catch (Exception e) {
			// TODO: handle exception
			resultMap.put("success", false);
			resultMap.put("error", e.getMessage());
		}
		
		return resultMap;
	}
	
	@RequestMapping("/updateUser")
	@ResponseBody
	public Map<String,Object> updateUser(HttpServletRequest request,HttpServletResponse response,@ModelAttribute("user") User user){
		Map<String,Object> resultMap = new HashMap<String,Object>();
		User loginUser = (User)request.getSession().getAttribute(GlobalPara.USER_SESSION_TOKEN);
		user.setModifiedId(loginUser.getId());
		user.setModifiedDate(new Date());
		
		//修改用户角色（先删除再添加tb_user_role表中的记录）
		String roleIds = user.getRoleId();
		if(roleIds.length()>32){
			String[] roleIdArray = roleIds.split(",");
			
		}else{
					
		}
		resultMap.put("success", true);
		try {
			userService.updateUser(user);
		} catch (Exception e) {
			// TODO: handle exception
			resultMap.put("success", false);
			resultMap.put("error", e.getMessage());
		}
		
		return resultMap;
	}
	
	@RequestMapping("/deleteUser")
	@ResponseBody
	public Map<String,Object> deleteUser(HttpServletRequest request,HttpServletResponse response,@RequestParam("userId") String userId){
		Map<String,Object> resultMap = new HashMap<String,Object>();
		User loginUser = (User)request.getSession().getAttribute(GlobalPara.USER_SESSION_TOKEN);
		
		User user = new User();
		user.setId(userId);
		user.setStatus(1);
		user.setModifiedId(loginUser.getId());
		user.setModifiedDate(new Date());
		resultMap.put("success", true);
		try {
			userService.updateUser(user);
		} catch (Exception e) {
			// TODO: handle exception
			resultMap.put("success", false);
			resultMap.put("error", e.getMessage());
		}
		return resultMap;
	}
	
	@RequestMapping("/toPageRoleManage")
	public ModelAndView toRoleManage(HttpServletRequest request,HttpServletResponse response){
		ModelAndView mv = new ModelAndView();
		List<Role> roleList = roleService.getRoleList();
		mv.addObject("roleList", roleList);
		mv.setViewName("admin/roleManage");
		return mv;
	}
	
	@RequestMapping("/insertRole")
	@ResponseBody
	public Map<String,Object> insertRole(HttpServletRequest request,HttpServletResponse response,@RequestParam("roleName") String roleName){
		Map<String,Object> resultMap = new HashMap<String,Object>();
		
		User loginUser = (User)request.getSession().getAttribute(GlobalPara.USER_SESSION_TOKEN);
		Role role = new Role();
		role.setId(PKIDUtils.getUuid());
		role.setRoleName(roleName);
		role.setStatus(0);
		role.setCreatedId(loginUser.getId());
		role.setCreatedDate(new Date());
		resultMap.put("success", true);
		try {
			roleService.insertRole(role);
		} catch (Exception e) {
			// TODO: handle exception
			resultMap.put("success", false);
			resultMap.put("error", e.getMessage());
		}
		
		return resultMap;
	}
	
	@RequestMapping("/updateRole")
	@ResponseBody
	public Map<String,Object> updateRole(HttpServletRequest request,HttpServletResponse response,@RequestParam("roleName") String roleName,@RequestParam("roleId") String roleId){
		Map<String,Object> resultMap = new HashMap<String,Object>();
		User loginUser = (User)request.getSession().getAttribute(GlobalPara.USER_SESSION_TOKEN);
		Role role = new Role();
		role.setId(roleId);
		role.setRoleName(roleName);
		role.setModifiedId(loginUser.getId());
		role.setModifiedDate(new Date());
		resultMap.put("success", true);
		try {
			roleService.updateRole(role);
		} catch (Exception e) {
			// TODO: handle exception
			resultMap.put("success", false);
			resultMap.put("error", e.getMessage());
		}
		return resultMap;
	}
	
	@RequestMapping("/deleteRole")
	@ResponseBody
	public Map<String,Object> deleteRole(HttpServletRequest request,HttpServletResponse response,@RequestParam("roleId") String roleId){
		Map<String,Object> resultMap = new HashMap<String,Object>();
		User loginUser = (User)request.getSession().getAttribute(GlobalPara.USER_SESSION_TOKEN);
		Role role = new Role();
		role.setId(roleId);
		role.setStatus(1);
		role.setModifiedId(loginUser.getId());
		role.setModifiedDate(new Date());
		resultMap.put("success", true);
		try {
			roleService.updateRole(role);
		} catch (Exception e) {
			// TODO: handle exception
			resultMap.put("success", false);
			resultMap.put("error", e.getMessage());
		}
		return resultMap;
	}
	
	@RequestMapping("/toChangePassword")
	public ModelAndView toChangePassword(HttpServletRequest request,HttpServletResponse response){
		ModelAndView mv = new ModelAndView();
		User loginUser = (User)request.getSession().getAttribute(GlobalPara.USER_SESSION_TOKEN);
		mv.addObject("user", loginUser);
		mv.setViewName("admin/changePassword");
		return mv;
	}
	
	@RequestMapping("/updatePassword")
	@ResponseBody
	public Map<String,Object> updatePassword(HttpServletRequest request,HttpServletResponse response,@RequestParam("password") String password){
		Map<String,Object> resultMap = new HashMap<String,Object>();
		User loginUser = (User)request.getSession().getAttribute(GlobalPara.USER_SESSION_TOKEN);
		loginUser.setPassword(password);
		loginUser.setModifiedId(loginUser.getId());
		loginUser.setModifiedDate(new Date());
		if(loginUser.getIsFirstLogin().equals(0)){
			loginUser.setIsFirstLogin(1);
		}
		resultMap.put("success", true);
		try {
			userService.updateUser(loginUser);
			
		} catch (Exception e) {
			// TODO: handle exception
			resultMap.put("success", false);
			resultMap.put("error", e.getMessage());
		}
		if(resultMap.get("success").equals(true)){
			request.getSession().setAttribute(GlobalPara.USER_SESSION_TOKEN, loginUser);
		}
		return resultMap;
	}
	
	@RequestMapping("/validatePassword")
	public Map<String,Object> validatePassword(HttpServletRequest request,HttpServletResponse response,@RequestParam("password") String password){
		Map<String,Object> resultMap = new HashMap<String,Object>();
		return resultMap;
	}
	
	private List<Menu> getMenuListByRoleId(String roleId){
		Map<String,Object> paraMap = new HashMap<String,Object>();
		paraMap.put("roleId", roleId);
		paraMap.put("pid", "0");
		List<Menu> menuList = menuService.getMenuListByRoleId(paraMap);
		List<Menu> showMenuList = new ArrayList<Menu>();
		if(!menuList.isEmpty()){
			for(Menu menu:menuList){
				Map<String,Object> cMap = new HashMap<String,Object>();
				cMap.put("roleId", roleId);
				cMap.put("pid", menu.getId());
				List<Menu> cList = menuService.getMenuListByRoleId(cMap);
				if(!cList.isEmpty()){
					menu.setChildren(cList);
					showMenuList.add(menu);
				}
			}
		}
		
		return showMenuList;
	}
}
