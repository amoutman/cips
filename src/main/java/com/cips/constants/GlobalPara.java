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
	
	/**
	 * 向HttpSession中放置用户信息时的key,不许别的操作再使用这个
	 * 值向HttpSession中保存数据,以免冲掉HttpSession中的用户信息数据。
	 * */
	public static final String USER_SESSION_TOKEN = "UserSessionToken";
	
	public static final String SPLIT = ",";
	
	public static final String DICTIONARY_ORDER_CODE = "ORDER_CODE";
}
