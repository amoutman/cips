package com.cips.constants;

/**
 * 系统级静态变量管理类 
 * */
public class GlobalPara {

	/**AJAX返回值KEY*/
	public static final String AJAX_KEY = "msg";
	
	/**ajax返回信息提示符 1 - 正确*/
	public static final String AJAX_SUCCESS = "1";
	
	/**ajax返回信息提示符 0 - 错误*/
	public static final String AJAX_FALSE = "0";
	
	public static final String USER_SESSION_TOKEN = "UserSessionToken";
	
	public static final String SPLIT = ",";
	
	public static final String DICTIONARY_ORDER_CODE = "ORDER_CODE";
	
	public static final String MENU_SESSION = "MenuSession";
	
	/**角色名称*/
	public static final String RNAME_HW_ADMIN = "海外管理员";
	public static final String RNAME_HW_AUDITOR = "海外审核员";
	public static final String RNAME_CUSTOMER = "客户";
	
	/**待办信息常量*/
	public static final String TASK_ORDER_COMMIT = "此新订单已完成提交请开始撮合";
}
