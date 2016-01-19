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

import com.alibaba.druid.util.StringUtils;
import com.cips.constants.GlobalPara;
import com.cips.model.Menu;
import com.cips.model.Role;
import com.cips.model.User;
import com.cips.model.UserRole;
import com.cips.page.Pager;
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
		if(loginUser!=null){
			mv.setViewName("redirect:user/toPageUserManage");
		}else{
			mv.setViewName("redirect:user/toLogin");
		}
		return mv;
	}
	
	@RequestMapping("/toLogin")
	public ModelAndView toLogin(HttpServletRequest request,HttpServletResponse response){
		ModelAndView mv = new ModelAndView();
		mv.setViewName("admin/login");
		return mv;
	}
	
	@RequestMapping(value="/login",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> login(HttpServletRequest request,HttpServletResponse response,@RequestParam("userName") String userName,@RequestParam("password") String password){
		Map<String,Object> resultMap = new HashMap<String,Object>();
		User user = new User();
		user.setUserName(userName);
		user.setPassword(MD5.md5(password));
		try {
			User lUser = userService.loginUser(user);
			if(lUser==null){
				resultMap.put("success", false);
				resultMap.put("error", "用户名或者密码不正确");
			}else{
				if(lUser.getIsFirstLogin().equals(0)){
					resultMap.put("isFirstLogin", true);
				}else{
					resultMap.put("isFirstLogin", false);
				}
				request.getSession().setAttribute(GlobalPara.USER_SESSION_TOKEN, lUser);
				//查询用户的角色
				List<Role> urList = roleService.getRoleListByUserId(lUser.getId());
				String[] roleIdArray;
				String roleIds = "";
				for(Role role:urList){
					if(roleIds.equals("")){
						roleIds = role.getId();
					}else{
						roleIds = roleIds + "," + role.getId();
					}
				}
				if(roleIds.length() > 32){
					roleIdArray = roleIds.split(",");
				}else{
					roleIdArray = new String[]{roleIds};
				}
				List<Menu> menuList = getMenuListByRoleId(roleIdArray);
				request.getSession().setAttribute(GlobalPara.MENU_SESSION, menuList);
				resultMap.put("success", true);
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			resultMap.put("success", false);
			resultMap.put("error", e.getMessage());
		}
		return resultMap;
	}

	@RequestMapping("/toPageUserManage")
	public ModelAndView toUserManage(HttpServletRequest request,HttpServletResponse response,@RequestParam(value="userInfo", required=false) String userInfo){
		ModelAndView mv = new ModelAndView();
		Map<String,Object> map = new HashMap<String,Object>();
		//分页条件
		Pager pager = (Pager)request.getAttribute(GlobalPara.PAGER_SESSION);

		if(!StringUtils.isEmpty(userInfo)){
			map.put("userInfo", userInfo);
		}
		map.put(GlobalPara.PAGER_SESSION, pager);
		List<User> userList = userService.getUserList(map);
		//查询系统中所有角色
		List<Role> roleList = roleService.getRoleList();
		
		List<User> nUserList = new ArrayList<User>();
		if(!userList.isEmpty()){
			for(User user:userList){
				String userId = user.getId();
				//查询用户的角色
				List<Role> urList = roleService.getRoleListByUserId(userId);
				String roleName = "";
				String roleIds = "";
				for(Role r:urList){
					if(roleName.equals("")){
						roleName = r.getRoleName();
					}else{
						roleName = roleName + "," + r.getRoleName();
					}
					if(roleIds.equals("")){
						roleIds = r.getId();
					}else{
						roleIds = roleIds + "," + r.getId();
					}
				}
				
				if(roleName!=""){
					user.setRoleNames(roleName);
				}
				if(roleIds!=""){
					user.setRoleId(roleIds);
				}
				List<Role> checkRoleList = new ArrayList<Role>();
				for(Role role:roleList){
					String rid = role.getId();
					boolean isIndex = roleIds.matches(rid);
					if(isIndex){
						role.setIsCheck(1);
					}else if(roleIds.equals(rid)){
						role.setIsCheck(1);
					}else{
						role.setIsCheck(0);
					}
					checkRoleList.add(role);
				}
				
				user.setRoleList(checkRoleList);
				nUserList.add(user);
			}
			mv.addObject("userList", nUserList);
		}
		
		
		if(!roleList.isEmpty()){
			mv.addObject("roleList", roleList);
		}	
		mv.addObject("pager", pager);
		mv.setViewName("admin/userManage");
		return mv;
	}
	
	@RequestMapping("/insertUser")
	public ModelAndView insertUser(HttpServletRequest request,HttpServletResponse response,@ModelAttribute("user") User user){
		ModelAndView mv = new ModelAndView();
		User loginUser = (User)request.getSession().getAttribute(GlobalPara.USER_SESSION_TOKEN);
		user.setId(PKIDUtils.getUuid());
		user.setPassword(MD5.md5("12345678"));
		user.setStatus(0);
		user.setIsFirstLogin(0);
		user.setCreatedId(loginUser.getId());
		user.setCreatedDate(new Date());
		
		//添加用户角色
		String roleIds = user.getRoleId();
		List<UserRole> urList = new ArrayList<UserRole>();
		UserRole ur = null;
		if(roleIds.length()>32){
			String[] roleIdArray = roleIds.split(",");
			for(int i=0;i<roleIdArray.length-1;i++){
				ur = new UserRole();
				ur.setId(PKIDUtils.getUuid());
				ur.setUserId(user.getId());
				ur.setRoleId(roleIdArray[i]);
				ur.setCreatedId(loginUser.getId());
				ur.setCreatedDate(new Date());
				urList.add(ur);
			}
		}else{
			ur = new UserRole();
			ur.setId(PKIDUtils.getUuid());
			ur.setUserId(user.getId());
			ur.setRoleId(roleIds);
			ur.setCreatedId(loginUser.getId());
			ur.setCreatedDate(new Date());
			urList.add(ur);
		}
		try {
			userService.insertUser(user);
			if(!urList.isEmpty()){
				roleService.insertUserRoleList(urList);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		mv.setViewName("redirect:toPageUserManage");
		return mv;
	}
	
	@RequestMapping("/updateUser")
	public ModelAndView updateUser(HttpServletRequest request,HttpServletResponse response,@ModelAttribute("user") User user){
		ModelAndView mv = new ModelAndView();
		User loginUser = (User)request.getSession().getAttribute(GlobalPara.USER_SESSION_TOKEN);
		user.setModifiedId(loginUser.getId());
		user.setModifiedDate(new Date());
		
		//修改用户角色（先删除再添加tb_user_role表中的记录）
		String roleIds = user.getRoleId();
		List<UserRole> urList = new ArrayList<UserRole>();
		UserRole ur = null;
		if(roleIds.length()>32){
			String[] roleIdArray = roleIds.split(",");
			for(int i=0;i<roleIdArray.length-1;i++){
				ur = new UserRole();
				ur.setId(PKIDUtils.getUuid());
				ur.setUserId(user.getId());
				ur.setRoleId(roleIdArray[i]);
				ur.setCreatedId(loginUser.getId());
				ur.setCreatedDate(new Date());
				urList.add(ur);
			}
		}else{
			ur = new UserRole();
			ur.setId(PKIDUtils.getUuid());
			ur.setUserId(user.getId());
			ur.setRoleId(roleIds);
			ur.setCreatedId(loginUser.getId());
			ur.setCreatedDate(new Date());
			urList.add(ur);
		}
		
		try {
			userService.updateUser(user);
			roleService.deleteUserRoleByUserId(user.getId());
			if(!urList.isEmpty()){
				roleService.insertUserRoleList(urList);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		mv.setViewName("redirect:toPageUserManage");
		return mv;
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
			roleService.deleteUserRoleByUserId(userId);
		} catch (Exception e) {
			// TODO: handle exception
			resultMap.put("success", false);
			resultMap.put("error", e.getMessage());
		}
		return resultMap;
	}
	
	@RequestMapping("/toRoleManage")
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
//		List<Menu> menuList = (List<Menu>)request.getSession().getAttribute(GlobalPara.MENU_SESSION);
//		System.out.println("---------------------"+menuList.size());
//		mv.addObject("menuList", menuList);
		mv.setViewName("admin/changePassword");
		return mv;
	}
	
	@RequestMapping("/updatePassword")
	@ResponseBody
	public Map<String,Object> updatePassword(HttpServletRequest request,HttpServletResponse response,@RequestParam("password") String password){
		Map<String,Object> resultMap = new HashMap<String,Object>();
		User loginUser = (User)request.getSession().getAttribute(GlobalPara.USER_SESSION_TOKEN);
		User nUser = new User();
		nUser.setId(loginUser.getId());
		nUser.setPassword(MD5.md5(password));
		nUser.setModifiedId(loginUser.getId());
		nUser.setModifiedDate(new Date());
		if(loginUser.getIsFirstLogin().equals(0)){
			nUser.setIsFirstLogin(1);
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
			loginUser.setPassword(MD5.md5(password));
			request.getSession().setAttribute(GlobalPara.USER_SESSION_TOKEN, loginUser);
		}
		return resultMap;
	}
	
	@RequestMapping("/validatePassword")
	public Map<String,Object> validatePassword(HttpServletRequest request,HttpServletResponse response,@RequestParam("password") String password){
		Map<String,Object> resultMap = new HashMap<String,Object>();
		return resultMap;
	}
	
	private List<Menu> getMenuListByRoleId(String[] roleId){
		Map<String,Object> paraMap = new HashMap<String,Object>();
		paraMap.put("roleId", roleId);
		paraMap.put("pid", "0");
		List<Menu> menuList = menuService.getMenuListByRoleId(paraMap);
//		List<Menu> showMenuList = new ArrayList<Menu>();
//		if(!menuList.isEmpty()){
//			for(Menu menu:menuList){
//				Map<String,Object> cMap = new HashMap<String,Object>();
//				cMap.put("roleId", roleId);
//				cMap.put("pid", menu.getId());
//				List<Menu> cList = menuService.getMenuListByRoleId(cMap);
//				if(!cList.isEmpty()){
//					menu.setChildren(cList);
//					showMenuList.add(menu);
//				}
//			}
//		}
		
		return menuList;
	}
}