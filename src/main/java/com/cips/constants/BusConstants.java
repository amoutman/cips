package com.cips.constants;

/**
 * 业务常量类
 */
public class BusConstants {

	/**任务状态 0：未处理  1：已处理  2：处理中  3：已驳回*/
	public static final Integer TASK_STATUS_NOT_PROCESS = 0;
	public static final Integer TASK_STATUS_PROCESSED = 1;
	public static final Integer TASK_STATUS_PROCESSING = 2;
	
	/**订单状态 0：已提交  1：处理中  2：已完成  3:作废删除*/
	public static final Integer ORDER_STATUS_COMMIT = 0;
	public static final Integer ORDER_STATUS_PROCESSING = 1;
	public static final Integer ORDER_STATUS_COMPLETED = 2;
	public static final Integer ORDER_STATUS_DELETE = 3;

	/**订单明细账户类型 1：用户申请时填写的海外账户信息  2：海外用户国内账户信息  3：华创国内账户  4：华创海外账户*/
	public static final Integer ORDERDETAILS_TYPE_CUSTOMER_HWACC = 1;
	public static final Integer ORDERDETAILS_TYPE_HWUSER_LOCACC = 2;
	public static final Integer ORDERDETAILS_TYPE_HC_LOCACC = 3;
	public static final Integer ORDERDETAILS_TYPE_HC_HWACC = 4;
	
	/**汇率状态 0：正常  1：作废*/
	public static final Integer RATE_STATUS_YES = 0;
	public static final Integer RATE_STATUS_NO = 1;
	
	/**汇率类型 1：RMB&$  2：$&RMB*/
	public static final Integer RATE_TYPE_RMB_TO_US = 1;
	public static final Integer RATE_TYPE_US_TO_RMB = 2;
	
	/**手续费状态 0：正常  1：作废*/
	public static final Integer POUNDAGE_STATUS_YES = 0;
	public static final Integer POUNDAGE_STATUS_NO = 1;
	
	/**待办类型及待办信息*/
	//订单已提交发送到平台操作员进行信息维护
	public static final Integer TASK_TYPE_COMMIT = 1; 
	public static final String TASK_REMARK_COMMIT = "请为此新订单进行海外用户信息维护";
	//订单海外用户信息已维护发送审核员复审
	public static final Integer TASK_TYPE_HWUSERINFO = 2; 
	public static final String TASK_REMARK_HWUSERINFO = "请复核海外用户收款账户信息";
	//订单海外用户信息维护有误 驳回
	public static final Integer TASK_TYPE_HWUSERINFO_REJECT = 3;
	public static final String TASK_REMARK_HWUSERINFO_REJECT = "海外用户收款账户信息有误，请核实";
	//订单海外用户信息已由审核员复审后发送待办至华创操作员进行打款
	public static final Integer TASK_TYPE_FIRST_HCPAY = 4; 
	public static final String TASK_REMARK_FIRST_HCPAY = "请为该订单进行人民币支付";
	//打款完成后发送待办至平台操作员进行第一次打款确认
	public static final Integer TASK_TYPE_CONFIRM_FIRST_HCPAY = 5; 
	public static final String TASK_REMARK_CONFIRM_FIRST_HCPAY = "请确认华创用户第1次打款信息";
	//平台操作员确认创用户第1次付款凭证有误 驳回
	public static final Integer TASK_TYPE_FIRST_HCPAY_VOUCHER_REJECT = 6; 
	public static final String TASK_REMARK_FIRST_HCPAY_VOUCHER_REJECT = "付款凭证有误，请核实";
	//平台操作员确认华创用户第1次付款凭证无误后，发送待办至平台复核员进行复核
	//平台审核员复核凭证有问题，直接驳回至华创操作员 注意：类型及信息同类型6
	public static final Integer TASK_TYPE_FIRST_HCPAY_VOUCHER_CHECK = 7; 
	public static final String TASK_REMARK_FIRST_HCPAY_VOUCHER_CHECK = "请复核华创用户第1次打款信息";
	//平台审核员审核无误后，发送待办至海外用户进行收款确认并上传凭证
	public static final Integer TASK_TYPE_FIRST_MONEYRECEIPT = 8; 
	public static final String TASK_REMARK_FIRST_HCPAY_MONEYRECEIPT = "请对该订单进行收款确认";
	//海外用户收款确认后发送待办至平台操作员进行收款凭证确认
	public static final Integer TASK_TYPE_FIRST_MONEYRECEIPT_VOUCHER = 9; 
	public static final String TASK_REMARK_FIRST_MONEYRECEIPT_VOUCHER = "海外用户已确认收到华创第1次打款，请核验收款凭证";
	//平台操作员确认收款凭证有问题，则驳回
	public static final Integer TASK_TYPE_FIRST_MONEYRECEIPT_VOUCHER_REJECT = 10; 
	public static final String TASK_REMARK_FIRST_MONEYRECEIPT_VOUCHER_REJECT = "收款凭证有误，请核实";
	//平台操作员确认收款凭证没有问题，则发送待办至平台审核员进行复核
	//如果有问题则直接驳回给海外用户进行重新凭证提交，注意：类型及信息同类型10
	public static final Integer TASK_TYPE_FIRST_MONEYRECEIPT_VOUCHER_REJECT_CHECK = 11; 
	public static final String TASK_REMARK_FIRST_MONEYRECEIPT_VOUCHER_REJECT_CHECK = "请复核海外用户收款确认信息";
	//如果平台审核员审核无误，则发送待办至好望角操作员进行向华创打款，并上传打款凭证
	public static final Integer TASK_TYPE_HWJPAY = 12; 
	public static final String TASK_REMARK_HWJPAY = "请为该订单进行人民币支付";
	//好望角打款并上传凭证后，发送待办至平台操作员进行打款确认
	public static final Integer TASK_TYPE_HWJPAY_CONFIRM = 13; 
	public static final String TASK_REMARK_HWJPAY_CONFIRM = "好望角用户已打款，请确认打款凭证信息";
	//平台操作员接收到好望角打款确认待办进行确认，如果有问题则驳回
	public static final Integer TASK_TYPE_HWJPAY_CONFIRM_REJECT = 14; 
	public static final String TASK_REMARK_HWJPAY_CONFIRM_REJECT = "打款凭证有误，请核实";
	//平台操作员接收到好望角打款确认待办进行确认，如果没有问题则发送待办至平台审核员进行复核
	//如果有问题则直接驳回至好望角操作员，注意：类型及信息同类型14
	public static final Integer TASK_TYPE_HWJPAY_CONFIRM_CHECK = 15; 
	public static final String TASK_REMARK_HWJPAY_CONFIRM_CHECK = "好望角用户已打款，请确认打款凭证信息";
	//平台审核员如果审核好望角打款确认凭证无误，则发送待办至华创操作员进行收款确认并上传收款凭证
	public static final Integer TASK_TYPE_HWJPAY_HCRECEIPT_CONFIRM = 16; 
	public static final String TASK_REMARK_HWJPAY_HCECEIPT_CONFIRM = "你收到一笔新来的款项，请确认";
	//华创用户上传收款凭证后，发送待办至平台操作员进行信息核实
	public static final Integer TASK_TYPE_HWJPAY_HCRECEIPT_VOUCHER = 17; 
	public static final String TASK_REMARK_HWJPAY_HCRECEIPT_VOUCHER = "华创客户已上传凭证，请确认收款凭证信息";
	//华创用户上传收款凭证如果有误，则驳回
	public static final Integer TASK_TYPE_HWJPAY_HCRECEIPT_VOUCHER_REJECT = 18; 
	public static final String TASK_REMARK_HWJPAY_HCRECEIPT_VOUCHER_REJECT = "收款凭证有误，请核实收款凭证信息";
	//华创用户上传收款凭证如果没有问题，则发送待办至平台审核员进行复核
	//如果收款凭证有问题，则直接驳回给华创用户 注意：类型及信息同18
	public static final Integer TASK_TYPE_HWJPAY_HCRECEIPT_VOUCHER_CHECK = 19; 
	public static final String TASK_REMARK_HWJPAY_HCRECEIPT_VOUCHER_CHECK = "华创客户已上传凭证，请核实收款凭证信息";
	//平台审核员审核华创收款凭证没有问题，则发送待办至华创操作员进行第二次打款
	public static final Integer TASK_TYPE_SECOND_HCPAY = 20; 
	public static final String TASK_REMARK_SECOND_HCPAY = "请对已收款项进行2次打款操作";
	//华创2次打款完成，并上传凭证后发送待办至平台操作员进行核验打款凭证
	public static final Integer TASK_TYPE_SECOND_HCPAY_CONFIRM = 21; 
	public static final String TASK_REMARK_SECOND_HCPAY_CONFIRM = "华创用户已经进行2次打款操作，请核验打款凭证信息";
	//平台操作员核验华创2次打款凭证后，如有问题则驳回
	public static final Integer TASK_TYPE_SECOND_HCPAY_CONFIRM_REJECT = 22; 
	public static final String TASK_REMARK_SECOND_HCPAY_CONFIRM_REJECT = "打款凭证有误，请核实";
	//平台操作员核验华创2次打款凭证后,如果没有问题则发送待办至平台审核员进行复核
	//如果有问题直接驳回至华创用户 注意：类型及信息同22
	public static final Integer TASK_TYPE_SECOND_HCPAY_RECHECK = 23; 
	public static final String TASK_REMARK_SECOND_HCPAY_RECHECK = "华创用户已经进行2次打款操作，请核实打款凭证信息";
	//平台审核员复核华创2次打款凭证无误则发送待办至海外用户进行确认收款
	public static final Integer TASK_TYPE_SECOND_HCPAY_RECEIPT_CONFIRM = 24; 
	public static final String TASK_REMARK_SECOND_HCPAY_RECEIPT_CONFIRM = "已进行了第2次打款，请确认收款";
	//海外用户确认收款后上传凭证并发送待办至平台操作员进行凭证确认核实
	public static final Integer TASK_TYPE_SECOND_HCPAY_RECEIPT_VOUCHER = 25; 
	public static final String TASK_REMARK_SECOND_HCPAY_RECEIPT_VOUCHER = "海外用户已确认第2次收款，请确认收款凭证信息";
	//平台操作员核实海外用户二次收款凭证有问题，则驳回
	public static final Integer TASK_TYPE_SECOND_HCPAY_RECEIPT_VOUCHER_REJECT = 26; 
	public static final String TASK_REMARK_SECOND_HCPAY_RECEIPT_VOUCHER_REJECT = "收款凭证存在问题，请核实";
	//平台操作员核实海外用户二次收款凭证没有问题，则维护华创客户海外账户信息至订单并发送待办至平台审核员进行复核
	//如果复核凭证有问题则驳回至海外用户 注意：类型及信息同26
	public static final Integer TASK_TYPE_SECOND_HCPAY_RECEIPT_VOUCHER_RECHECK = 27; 
	public static final String TASK_REMARK_SECOND_HCPAY_RECEIPT_VOUCHER_RECHECK = "海外用户已确认第2次收款，请核实收款凭证信息";
	//平台审核员复核海外用户二次收款凭证及华创海外账户信息没有问题，则发送待办至海外用户进行第一次海外打款
	public static final Integer TASK_TYPE_FIRST_HWUSERPAY = 28; 
	public static final String TASK_REMARK_FIRST_HWUSERPAY = "请进行第1次海外打款";
	//海外用户第一次打款完成并上传打款凭证，则发送待办至平台操作员进行打款信息核实
	public static final Integer TASK_TYPE_FIRST_HWUSERPAY_CONFIRM = 29; 
	public static final String TASK_REMARK_FIRST_HWUSERPAY_CONFIRM = "海外用户已给华创海外账户第一次打款，请确认打款凭证信息";
	//平台操作员审核海外用户第一次打款凭证有问题，则驳回至海外用户
	public static final Integer TASK_TYPE_FIRST_HWUSERPAY_REJECT = 30; 
	public static final String TASK_REMARK_FIRST_HWUSERPAY_REJECT = "第一次海外打款凭证有问题，请核实";
	//平台操作员审核海外用户第一次打款凭证没有问题，则发送待办至平台审核员进行复核
	//有问题则直接驳回至海外用户，注意：类型及信息同30
	public static final Integer TASK_TYPE_FIRST_HWUSERPAY_RECHECK = 31; 
	public static final String TASK_REMARK_FIRST_HWUSERPAY_RECHECK = "海外用户已给华创海外账户第一次打款，请核实打款凭证信息";
	//平台审核员复核海外用户第一次打款凭证无误后，发送待办至华创用户进行收款确认并上传凭证
	public static final Integer TASK_TYPE_FIRST_HCRECEIPT = 32; 
	public static final String TASK_REMARK_FIRST_HCRECEIPT = "海外用户已完成第一次打款，请确认收款信息";
	//华创用户上传收款凭证后发送待办至平台操作员进行收款凭证核实
	public static final Integer TASK_TYPE_FIRST_HCRECEIPT_CONFIRM = 33; 
	public static final String TASK_REMARK_FIRST_HCRECEIPT_CONFIRM= "已确认海外用户的第一打款，已上传凭证请确认";
	//平台操作员核实收款凭证有问题则驳回至华创用户
	public static final Integer TASK_TYPE_FIRST_HCRECEIPT_REJECT = 34; 
	public static final String TASK_REMARK_FIRST_HCRECEIPT_REJECT = "确认收款凭证有误，请核实";
	//平台操作员核实收款凭证无误，则发送待办至平台审核员进行复核
	//有问题则直接驳回至华创用户 注意：类型及信息同34
	public static final Integer TASK_TYPE_FIRST_HCRECEIPT_RECHECK = 35; 
	public static final String TASK_REMARK_FIRST_HCRECEIPT_RECHECK = "已确认海外用户的第一打款，已上传凭证请复核";
	//平台审核员审核凭证无误，则发送待办至华创操作员进行海外账户转账
	public static final Integer TASK_TYPE_HC_HWPAY = 36; 
	public static final String TASK_REMARK_HC_HWPAY = "请根据订单信息进行海外账户转账操作";
	//华创操作员转账打款完成并上传凭证后，发送待办至平台操作员进行信息核实
	public static final Integer TASK_TYPE_HC_HWPAY_CONFIRM = 37; 
	public static final String TASK_REMARK_HC_HWPAY_CONFIRM = "华创用户已进行海外账户的打款，请核验打款凭证信息";
	//平台操作员核实华创海外打款凭证有问题，则驳回
	public static final Integer TASK_TYPE_HC_HWPAY_REJECT = 38; 
	public static final String TASK_REMARK_HC_HWPAY_REJECT = "国外账户打款凭证有问题，请核实";
	//平台操作员核实华创海外打款凭证无误，则发送待办至平台审核员进行复核
	//如果有问题则直接驳回至华创操作员 注意：类型及信息同38
	public static final Integer TASK_TYPE_HC_HWPAY_RECHECK = 39; 
	public static final String TASK_REMARK_HC_HWPAY_RECHECK = "华创用户已进行海外账户的打款，请复核打款凭证信息";
	//平台复核员如果审核无误，则发送待办至好望角操作员进行收款确认
	public static final Integer TASK_TYPE_HWJ_HWRECEIPT = 40; 
	public static final String TASK_REMARK_HWJ_HWRECEIPT = "已完成海外账户打款，请确认收款";
	//好望角操作员确认收款并上传收款凭证，发送待办至平台操作员进行凭证核实
	public static final Integer TASK_TYPE_HWJ_HWRECEIPT_CONFIRM = 41; 
	public static final String TASK_REMARK_HWJ_HWRECEIPT_CONFIRM = "好望角已完成确认海外账户收款，请核验收款凭证";
	//平台操作员审核好望角客户收款凭证，存在问题则驳回
	public static final Integer TASK_TYPE_HWJ_HWRECEIPT_REJECT = 42; 
	public static final String TASK_REMARK_HWJ_HWRECEIPT_REJECT = "您上传的海外账户收款凭证有误，请核实";
	//平台操作员审核好望角客户收款凭证，没有问题则发送待办至平台审核员进行复核
	//如果有问题则直接驳回至好望角操作员 注意：类型及信息同42
	public static final Integer TASK_TYPE_HWJ_HWRECEIPT_RECHECK = 43; 
	public static final String TASK_REMARK_HWJ_HWRECEIPT_RECHECK = "好望角已完成确认海外账户收款，请核实收款凭证";
	//平台审核员审核好望角用户收款凭证，没有问题则发送待办至海外用户进行海外账户2次打款
	public static final Integer TASK_TYPE_SECOND_HWUSERPAY = 44; 
	public static final String TASK_REMARK_SECOND_HWUSERPAY = "请进行海外账户第二次打款";
	//海外用户第二次打款完成并上传打款凭证，发送待办至平台操作员进行凭证核实
	public static final Integer TASK_TYPE_SECOND_HWUSERPAY_CONFIRM = 45; 
	public static final String TASK_REMARK_SECOND_HWUSERPAY_CONFIRM = "海外用户已给华创用户海外账户二次打款完毕，请核验打款凭证信息";
	//平台操作员核验海外用户2次打款凭证，存在问题，则驳回
	public static final Integer TASK_TYPE_SECOND_HWUSERPAY_REJECT = 46; 
	public static final String TASK_REMARK_SECOND_HWUSERPAY_REJECT = "海外用户二次打款凭证有误，请核实";
	//平台操作员核验海外用户2次打款凭证，没有问题，则发送待办至平台审核员进行复核
	//如果有问题则直接驳回至海外用户 注意：类型及信息同46
	public static final Integer TASK_TYPE_SECOND_HWUSERPAY_RECHECK = 47; 
	public static final String TASK_REMARK_SECOND_HWUSERPAY_RECHECK = "海外用户已给华创用户海外账户二次打款完毕，请复核打款凭证信息";
	//平台审核员复核海外用户2次打款凭证，没有问题，则发送待办至华创操作员进行2次收款确认
	public static final Integer TASK_TYPE_SECOND_HCRECEIPT = 48; 
	public static final String TASK_REMARK_SECOND_HCRECEIPT = "已完成海外账户的二次打款，请确认收款";
	//华创操作员确认海外账户2次打款，并上传收款凭证，发送待办至平台操作员进行收款凭证核实
	public static final Integer TASK_TYPE_SECOND_HCRECEIPT_CONFIRM = 49; 
	public static final String TASK_REMARK_SECOND_HCRECEIPT_CONFIRM = "华创用户海外账户已确认二次来款，请核验";
	//平台操作员核验华创2次收款凭证，存在问题则驳回
	public static final Integer TASK_TYPE_SECOND_HCRECEIPT_REJECT = 50; 
	public static final String TASK_REMARK_SECOND_HCRECEIPT_REJECT = "您上传的海外账户二次收款凭证有误，请核实";
	//平台操作员核验华创2次收款凭证，没有问题则发送待办至平台审核员进行复核
	//复核有问题则直接驳回至华创操作员 注意：类型及信息同50
	public static final Integer TASK_TYPE_SECOND_HCRECEIPT_RECHECK = 51; 
	public static final String TASK_REMARK_SECOND_HCRECEIPT_RECHECK = "华创用户海外账户已确认二次来款，请复核";
	//平台审核员核验华创2次收款凭证，没有问题，则点击订单完成，并修改相应状态
	
	/**
	 * 普通业务流程
	 */
	//在第二步或第三步平台审核员审核完成后，发送待办至角色为其他客户 
	public static final Integer TASK_TYPE_CUSTOMER_PAY = 52; 
	public static final String TASK_REMARK_CUSTOMER_PAY = "请根据订单信息进行打款";
	//其他用户打款上传凭证后，发送待办平台操作员进行信息核实
	public static final Integer TASK_TYPE_CUSTOMER_PAY_CONFIRM = 53; 
	public static final String TASK_REMARK_CUSTOMER_PAY_CONFIRM = "已完成向海外用户打款，请核验打款凭证信息";
	//打款凭证有问题则驳回至其他客户
	public static final Integer TASK_TYPE_CUSTOMER_PAY_REJECT = 54; 
	public static final String TASK_REMARK_CUSTOMER_PAY_REJECT = "打款凭证有误，请核实";
	//打款凭证没有问题则发待办至平台审核员进行复核
	//有问题则直接驳回至其他客户 注意：类型及信息同54
	public static final Integer TASK_TYPE_CUSTOMER_PAY_RECHECK = 55; 
	public static final String TASK_REMARK_CUSTOMER_PAY_RECHECK = "已给海外用户打款，请复核打款凭证信息";
	//平台审核员复核打款凭证没有问题 则发送待办至海外用户进行收款确认
	public static final Integer TASK_TYPE_HWUSER_RECEIPT = 56; 
	public static final String TASK_REMARK_HWUSER_RECEIPT = "已向您的国内账户打款，请确认收款";
	//海外用户上传凭证，发送待办至平台操作员进行收款凭证信息核实
	public static final Integer TASK_TYPE_HWUSER_RECEIPT_CONFIRM = 57; 
	public static final String TASK_REMARK_HWUSER_RECEIPT_CONFIRM = "海外用户已确认收款，请核实收款凭证信息";
	//平台操作员核实收款凭证信息有误 则驳回
	public static final Integer TASK_TYPE_HWUSER_RECEIPT_REJECT = 58; 
	public static final String TASK_REMARK_HWUSER_RECEIPT_REJECT = "收款凭证有误，请核实";
	//如果没有问题，则发送待办至平台审核员进行审核 如果有问题则直接驳回至海外用户 类型及信息同58
	public static final Integer TASK_TYPE_HWUSER_RECEIPT_RECHECK = 59; 
	public static final String TASK_REMARK_HWUSER_RECEIPT_RECHECK = "海外用户已确认收款，请复核收款凭证信息";
	//如果复核收款信息没有问题，则发送待办至海外用户进行海外打款
	public static final Integer TASK_TYPE_HWUSER_PAY = 60; 
	public static final String TASK_REMARK_HWUSER_PAY = "请根据订单信息进行海外打款";
	//海外用户上传打款凭证，发送待办至平台操作员进行信息核实
	public static final Integer TASK_TYPE_HWUSER_PAY_CONFIRM = 61; 
	public static final String TASK_REMARK_HWUSER_PAY_CONFIRM = "海外用户已完成海外打款，请核验打款凭证信息";
	//如果有问题 则驳回
	public static final Integer TASK_TYPE_HWUSER_PAY_REJECT = 62; 
	public static final String TASK_REMARK_HWUSER_PAY_REJECT = "打款凭证有误，请核实";
	//如果没有问题。则发送待办至平台审核员进行复核 如果有问题则直接驳回至海外用户 类型及信息同62
	public static final Integer TASK_TYPE_HWUSER_PAY_RECHECK = 63; 
	public static final String TASK_REMARK_HWUSER_PAY_RECHECK = "海外用户已完成海外打款，请复核打款凭证信息";
	//如果复核没有问题，则发送待办至其他客户进行收款确认
	public static final Integer TASK_TYPE_CUSTOMER_RECEIPT = 64; 
	public static final String TASK_REMARK_CUSTOMER_RECEIPT = "已完成海外打款，请确认收款";
	//其他客户上传收款凭证，发送待办至平台操作员进行收款信息核实
	public static final Integer TASK_TYPE_CUSTOMER_RECEIPT_CONFIRM = 65; 
	public static final String TASK_REMARK_CUSTOMER_RECEIPT_CONFIRM = "客户已经完成海外收款确认，请核实收款凭证信息";
	//核实有问题 则驳回
	public static final Integer TASK_TYPE_CUSTOMER_RECEIPT_REJECT = 66; 
	public static final String TASK_REMARK_CUSTOMER_RECEIPT_REJECT = "收款凭证有误，请核实";
	//如果没问题则发送待办至审核员进行复核 有问题则直接驳回至其他客户 类型及信息同66
	public static final Integer TASK_TYPE_CUSTOMER_RECEIPT_RECHECK = 67; 
	public static final String TASK_REMARK_CUSTOMER_RECEIPT_RECHECK = "客户已经完成海外收款确认，请复核收款凭证信息";
	//审核员审核收款凭证无误 则修改订单状态为已完成
}
