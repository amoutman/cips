package com.cips.interceptor;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.cips.constants.GlobalPara;
import com.cips.model.Menu;
import com.cips.model.User;
import com.cips.page.Pager;

public class SysInterceptor extends HandlerInterceptorAdapter {
	
	private String mappingUrl;
	private String page;
	private String showCount;

	public void setPage(String page) {
		this.page = page;
	}

	public void setShowCount(String showCount) {
		this.showCount = showCount;
	}

	public void setMappingUrl(String mappingUrl) {
		this.mappingUrl = mappingUrl;
	}
	
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

		String url = request.getRequestURL().toString();
		if (!url.matches(".*login.*")) {

			User user = (User) request.getSession().getAttribute(GlobalPara.USER_SESSION_TOKEN);
			if (user == null) {
				request.getRequestDispatcher("/WEB-INF/view/admin/login.jsp").forward(request, response);
				return false;
			}
		}
		
		 if (url.matches(mappingUrl)) {
			 Pager pager = new Pager();
		
			 String currentPage = "";
		
			 if (!StringUtils.isEmpty(request.getParameter("currentPage"))) {
				 currentPage = request.getParameter("currentPage").toString();
			 } else {
				 currentPage = page;
			 }
		
			 pager.setShowCount(Integer.parseInt(showCount));
		
			 pager.setCurrentPage(Integer.parseInt(currentPage));
		
			 request.setAttribute(GlobalPara.PAGER_SESSION, pager);
		 }
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView mv) throws Exception {
		// TODO Auto-generated method stub
		//List<Menu> menuList = (List<Menu>)request.getSession().getAttribute(GlobalPara.MENU_SESSION);
		if(mv!=null){
			mv.addObject("menuList", request.getSession().getAttribute(GlobalPara.MENU_SESSION));
		}
	}


}
