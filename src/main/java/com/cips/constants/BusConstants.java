package com.cips.constants;

/**
 * 业务常量类
 */
public class BusConstants {

	/**任务状态 0：未处理  1：已处理  2：处理中*/
	public static final Integer TASK_STATUS_NOT_PROCESS = 0;
	public static final Integer TASK_STATUS_PROCESSED = 1;
	public static final Integer TASK_STATUS_PROCESSING = 2;
	
	/**订单状态 0：已提交  1：处理中  2：已完成  3:作废删除*/
	public static final Integer ORDER_STATUS_COMMIT = 0;
	public static final Integer ORDER_STATUS_PROCESSING = 1;
	public static final Integer ORDER_STATUS_COMPLETED = 2;
	public static final Integer ORDER_STATUS_DELETE = 3;

	/**汇率状态 0：正常  1：作废*/
	public static final Integer RATE_STATUS_YES = 0;
	public static final Integer RATE_STATUS_NO = 1;
	
	/**汇率类型 1：RMB&$  2：$&RMB*/
	public static final Integer RATE_TYPE_RMB_TO_US = 1;
	public static final Integer RATE_TYPE_US_TO_RMB = 2;
	
	/**手续费状态 0：正常  1：作废*/
	public static final Integer POUNDAGE_STATUS_YES = 0;
	public static final Integer POUNDAGE_STATUS_NO = 1;
	
	/**待办类型*/
	public static final Integer TASK_TYPE_COMMIT = 1;
}
