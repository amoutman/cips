package com.cips.interceptor;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.cips.constants.GlobalPara;
import com.cips.model.Menu;
import com.cips.model.User;

public class SysInterceptor extends HandlerInterceptorAdapter {
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

		String url = request.getRequestURL().toString();
		if (!url.matches(".*login.*")) {

			User user = (User) request.getSession().getAttribute(GlobalPara.USER_SESSION_TOKEN);
			if (user == null) {
				request.getRequestDispatcher("/WEB-INF/view/admin/login.jsp").forward(request, response);
				return false;
			}
		}
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView mv) throws Exception {
		// TODO Auto-generated method stub
		List<Menu> menuList = (List<Menu>)request.getSession().getAttribute(GlobalPara.MENU_SESSION);
		mv.addObject("menuList", menuList);
	}


}
