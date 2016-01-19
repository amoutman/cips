package com.cips.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cips.constants.BusConstants;
import com.cips.constants.GlobalPara;
import com.cips.dao.OrderDetailsMapper;
import com.cips.dao.OrderMapper;
import com.cips.dao.OrderOperateMapper;
import com.cips.dao.RoleMapper;
import com.cips.dao.TaskMapper;
import com.cips.model.Order;
import com.cips.model.OrderDetails;
import com.cips.model.OrderOperate;
import com.cips.model.Role;
import com.cips.model.Task;
import com.cips.util.PKIDUtils;

@Service("taskService")
public class TaskServiceImpl implements TaskService {
	
	private TaskMapper taskMapper;
	
	@Autowired
	public void setTaskMapper(TaskMapper taskMapper) {
		this.taskMapper = taskMapper;
	}
	
	private RoleMapper roleMapper;

	@Autowired
	public void setRoleMapper(RoleMapper roleMapper) {
		this.roleMapper = roleMapper;
	}
	
	private OrderMapper orderMapper;

	@Autowired
	public void setOrderMapper(OrderMapper orderMapper) {
		this.orderMapper = orderMapper;
	}

	private OrderDetailsMapper orderDetailsMapper;
	
	@Autowired
	public void setOrderDetailsMapper(OrderDetailsMapper orderDetailsMapper) {
		this.orderDetailsMapper = orderDetailsMapper;
	}
	
	private OrderOperateMapper orderOperateMapper;
	
	@Autowired
	public void setOrderOperateMapper(OrderOperateMapper orderOperateMapper) {
		this.orderOperateMapper = orderOperateMapper;
	}

	@Override
	public synchronized String processingTaskById(String taskId, String userId) {
		//用来返回处理信息
		String msg = null;
		//根据ID查询任务状态 如果为未处理则修改状态及为任务分配当前用户为处理人 否则直接返回
		Task task = taskMapper.selectByPrimaryKey(taskId);
		if(task.getOperatedId() != null && !task.getOperatedId().equals(userId)){
			msg = "该待办任务已由其他人进行处理！";
		}else{
			task.setOperatedId(userId);
			task.setBeginTime(new Date());
			taskMapper.updateByPrimaryKey(task);
		}
		return msg;
	}

	@Override
	public List<Task> getTaskListByParams(Map<String, Object> params) throws Exception {
		List<Task> tasks = taskMapper.toPageTaskListByParams(params);
		for (Task task : tasks) {
			insertMsgToTask(task);
		}
		return tasks;
	}

	@Override
	public Task initNewTask(String orderId, Integer taskType) throws Exception {
		Task task = new Task();
		task.setId(PKIDUtils.getUuid());
		task.setOrderId(orderId);
		task.setBeginTime(new Date());
		task.setStatus(BusConstants.TASK_STATUS_NOT_PROCESS);
		task.setTaskType(taskType);
		
		Role role = null;
		
		switch (taskType) {
		case 1:
			task.setMsg(BusConstants.TASK_REMARK_COMMIT);
			role = roleMapper.selectRoleByName(GlobalPara.RNAME_PL_OPERATOR);
			task.setRoleId(role.getId());
			break;
		case 2:
			task.setMsg(BusConstants.TASK_REMARK_HWUSERINFO);
			role = roleMapper.selectRoleByName(GlobalPara.RNAME_PL_CHECKER);
			task.setRoleId(role.getId());
			break;
		case 3:
			task.setMsg(BusConstants.TASK_REMARK_HWUSERINFO_REJECT);
			role = roleMapper.selectRoleByName(GlobalPara.RNAME_PL_OPERATOR);
			task.setRoleId(role.getId());
			break;
		case 4:
			task.setMsg(BusConstants.TASK_REMARK_FIRST_HCPAY);
			role = roleMapper.selectRoleByName(GlobalPara.RNAME_HC_OPERATOR);
			task.setRoleId(role.getId());
			break;
		case 5:
			task.setMsg(BusConstants.TASK_REMARK_CONFIRM_FIRST_HCPAY);
			role = roleMapper.selectRoleByName(GlobalPara.RNAME_PL_OPERATOR);
			task.setRoleId(role.getId());
			break;
		case 6:
			task.setMsg(BusConstants.TASK_REMARK_FIRST_HCPAY_VOUCHER_REJECT);
			role = roleMapper.selectRoleByName(GlobalPara.RNAME_HC_OPERATOR);
			task.setRoleId(role.getId());
			break;
		case 7:
			task.setMsg(BusConstants.TASK_REMARK_FIRST_HCPAY_VOUCHER_CHECK);
			role = roleMapper.selectRoleByName(GlobalPara.RNAME_PL_CHECKER);
			task.setRoleId(role.getId());
			break;
		case 8:
			task.setMsg(BusConstants.TASK_REMARK_FIRST_HCPAY_MONEYRECEIPT);
			role = roleMapper.selectRoleByName(GlobalPara.RNAME_HW_CUSTOMER);
			task.setRoleId(role.getId());
			break;
		case 9:
			task.setMsg(BusConstants.TASK_REMARK_FIRST_MONEYRECEIPT_VOUCHER);
			role = roleMapper.selectRoleByName(GlobalPara.RNAME_HC_OPERATOR);
			task.setRoleId(role.getId());
			break;
		case 10:
			task.setMsg(BusConstants.TASK_REMARK_FIRST_MONEYRECEIPT_VOUCHER_REJECT);
			role = roleMapper.selectRoleByName(GlobalPara.RNAME_HW_CUSTOMER);
			task.setRoleId(role.getId());
			break;
		case 11:
			task.setMsg(BusConstants.TASK_REMARK_FIRST_MONEYRECEIPT_VOUCHER_REJECT_CHECK);
			role = roleMapper.selectRoleByName(GlobalPara.RNAME_PL_CHECKER);
			task.setRoleId(role.getId());
			break;
		case 12:
			task.setMsg(BusConstants.TASK_REMARK_HWJPAY);
			role = roleMapper.selectRoleByName(GlobalPara.RNAME_HWJ_OPERATOR);
			task.setRoleId(role.getId());
			break;
		case 13:
			task.setMsg(BusConstants.TASK_REMARK_HWJPAY_CONFIRM);
			role = roleMapper.selectRoleByName(GlobalPara.RNAME_PL_OPERATOR);
			task.setRoleId(role.getId());
			break;
		case 14:
			task.setMsg(BusConstants.TASK_REMARK_HWJPAY_CONFIRM_REJECT);
			role = roleMapper.selectRoleByName(GlobalPara.RNAME_HWJ_OPERATOR);
			task.setRoleId(role.getId());
			break;
		case 15:
			task.setMsg(BusConstants.TASK_REMARK_HWJPAY_CONFIRM_CHECK);
			role = roleMapper.selectRoleByName(GlobalPara.RNAME_PL_CHECKER);
			task.setRoleId(role.getId());
			break;
		case 16:
			task.setMsg(BusConstants.TASK_REMARK_HWJPAY_HCECEIPT_CONFIRM);
			role = roleMapper.selectRoleByName(GlobalPara.RNAME_HC_OPERATOR);
			task.setRoleId(role.getId());
			break;
		case 17:
			task.setMsg(BusConstants.TASK_REMARK_HWJPAY_HCRECEIPT_VOUCHER);
			role = roleMapper.selectRoleByName(GlobalPara.RNAME_PL_OPERATOR);
			task.setRoleId(role.getId());
			break;
		case 18:
			task.setMsg(BusConstants.TASK_REMARK_HWJPAY_HCRECEIPT_VOUCHER_REJECT);
			role = roleMapper.selectRoleByName(GlobalPara.RNAME_HC_OPERATOR);
			task.setRoleId(role.getId());
			break;
		case 19:
			task.setMsg(BusConstants.TASK_REMARK_HWJPAY_HCRECEIPT_VOUCHER_CHECK);
			role = roleMapper.selectRoleByName(GlobalPara.RNAME_PL_CHECKER);
			task.setRoleId(role.getId());
			break;
		case 20:
			task.setMsg(BusConstants.TASK_REMARK_SECOND_HCPAY);
			role = roleMapper.selectRoleByName(GlobalPara.RNAME_HC_OPERATOR);
			task.setRoleId(role.getId());
			break;
		case 21:
			task.setMsg(BusConstants.TASK_REMARK_SECOND_HCPAY_CONFIRM);
			role = roleMapper.selectRoleByName(GlobalPara.RNAME_PL_OPERATOR);
			task.setRoleId(role.getId());
			break;
		case 22:
			task.setMsg(BusConstants.TASK_REMARK_SECOND_HCPAY_CONFIRM_REJECT);
			role = roleMapper.selectRoleByName(GlobalPara.RNAME_HC_OPERATOR);
			task.setRoleId(role.getId());
			break;
		case 23:
			task.setMsg(BusConstants.TASK_REMARK_SECOND_HCPAY_RECHECK);
			role = roleMapper.selectRoleByName(GlobalPara.RNAME_PL_CHECKER);
			task.setRoleId(role.getId());
			break;
		case 24:
			task.setMsg(BusConstants.TASK_REMARK_SECOND_HCPAY_RECEIPT_CONFIRM);
			role = roleMapper.selectRoleByName(GlobalPara.RNAME_HW_CUSTOMER);
			task.setRoleId(role.getId());
			break;
		case 25:
			task.setMsg(BusConstants.TASK_REMARK_SECOND_HCPAY_RECEIPT_VOUCHER);
			role = roleMapper.selectRoleByName(GlobalPara.RNAME_PL_OPERATOR);
			task.setRoleId(role.getId());
			break;
		case 26:
			task.setMsg(BusConstants.TASK_REMARK_SECOND_HCPAY_RECEIPT_VOUCHER_REJECT);
			role = roleMapper.selectRoleByName(GlobalPara.RNAME_HW_CUSTOMER);
			task.setRoleId(role.getId());
			break;
		case 27:
			task.setMsg(BusConstants.TASK_REMARK_SECOND_HCPAY_RECEIPT_VOUCHER_RECHECK);
			role = roleMapper.selectRoleByName(GlobalPara.RNAME_PL_CHECKER);
			task.setRoleId(role.getId());
			break;
		case 28:
			task.setMsg(BusConstants.TASK_REMARK_FIRST_HWUSERPAY);
			role = roleMapper.selectRoleByName(GlobalPara.RNAME_HW_CUSTOMER);
			task.setRoleId(role.getId());
			break;
		case 29:
			task.setMsg(BusConstants.TASK_REMARK_FIRST_HWUSERPAY_CONFIRM);
			role = roleMapper.selectRoleByName(GlobalPara.RNAME_PL_OPERATOR);
			task.setRoleId(role.getId());
			break;
		case 30:
			task.setMsg(BusConstants.TASK_REMARK_FIRST_HWUSERPAY_REJECT);
			role = roleMapper.selectRoleByName(GlobalPara.RNAME_HW_CUSTOMER);
			task.setRoleId(role.getId());
			break;
		case 31:
			task.setMsg(BusConstants.TASK_REMARK_FIRST_HWUSERPAY_RECHECK);
			role = roleMapper.selectRoleByName(GlobalPara.RNAME_PL_CHECKER);
			task.setRoleId(role.getId());
			break;
		case 32:
			task.setMsg(BusConstants.TASK_REMARK_FIRST_HCRECEIPT);
			role = roleMapper.selectRoleByName(GlobalPara.RNAME_HC_OPERATOR);
			task.setRoleId(role.getId());
			break;
		case 33:
			task.setMsg(BusConstants.TASK_REMARK_FIRST_HCRECEIPT_CONFIRM);
			role = roleMapper.selectRoleByName(GlobalPara.RNAME_PL_OPERATOR);
			task.setRoleId(role.getId());
			break;
		case 34:
			task.setMsg(BusConstants.TASK_REMARK_FIRST_HCRECEIPT_REJECT);
			role = roleMapper.selectRoleByName(GlobalPara.RNAME_HC_OPERATOR);
			task.setRoleId(role.getId());
			break;
		case 35:
			task.setMsg(BusConstants.TASK_REMARK_FIRST_HCRECEIPT_RECHECK);
			role = roleMapper.selectRoleByName(GlobalPara.RNAME_PL_CHECKER);
			task.setRoleId(role.getId());
			break;
		case 36:
			task.setMsg(BusConstants.TASK_REMARK_HC_HWPAY);
			role = roleMapper.selectRoleByName(GlobalPara.RNAME_HC_OPERATOR);
			task.setRoleId(role.getId());
			break;
		case 37:
			task.setMsg(BusConstants.TASK_REMARK_HC_HWPAY_CONFIRM);
			role = roleMapper.selectRoleByName(GlobalPara.RNAME_PL_OPERATOR);
			task.setRoleId(role.getId());
			break;
		case 38:
			task.setMsg(BusConstants.TASK_REMARK_HC_HWPAY_REJECT);
			role = roleMapper.selectRoleByName(GlobalPara.RNAME_HC_OPERATOR);
			task.setRoleId(role.getId());
			break;
		case 39:
			task.setMsg(BusConstants.TASK_REMARK_HC_HWPAY_RECHECK);
			role = roleMapper.selectRoleByName(GlobalPara.RNAME_PL_CHECKER);
			task.setRoleId(role.getId());
			break;
		case 40:
			task.setMsg(BusConstants.TASK_REMARK_HWJ_HWRECEIPT);
			role = roleMapper.selectRoleByName(GlobalPara.RNAME_HWJ_OPERATOR);
			task.setRoleId(role.getId());
			break;
		case 41:
			task.setMsg(BusConstants.TASK_REMARK_HWJ_HWRECEIPT_CONFIRM);
			role = roleMapper.selectRoleByName(GlobalPara.RNAME_PL_OPERATOR);
			task.setRoleId(role.getId());
			break;
		case 42:
			task.setMsg(BusConstants.TASK_REMARK_HWJ_HWRECEIPT_REJECT);
			role = roleMapper.selectRoleByName(GlobalPara.RNAME_HWJ_OPERATOR);
			task.setRoleId(role.getId());
			break;
		case 43:
			task.setMsg(BusConstants.TASK_REMARK_HWJ_HWRECEIPT_RECHECK);
			role = roleMapper.selectRoleByName(GlobalPara.RNAME_PL_CHECKER);
			task.setRoleId(role.getId());
			break;
		case 44:
			task.setMsg(BusConstants.TASK_REMARK_SECOND_HWUSERPAY);
			role = roleMapper.selectRoleByName(GlobalPara.RNAME_HW_CUSTOMER);
			task.setRoleId(role.getId());
			break;
		case 45:
			task.setMsg(BusConstants.TASK_REMARK_SECOND_HWUSERPAY_CONFIRM);
			role = roleMapper.selectRoleByName(GlobalPara.RNAME_PL_OPERATOR);
			task.setRoleId(role.getId());
			break;
		case 46:
			task.setMsg(BusConstants.TASK_REMARK_SECOND_HWUSERPAY_REJECT);
			role = roleMapper.selectRoleByName(GlobalPara.RNAME_HW_CUSTOMER);
			task.setRoleId(role.getId());
			break;
		case 47:
			task.setMsg(BusConstants.TASK_REMARK_SECOND_HWUSERPAY_RECHECK);
			role = roleMapper.selectRoleByName(GlobalPara.RNAME_PL_CHECKER);
			task.setRoleId(role.getId());
			break;
		case 48:
			task.setMsg(BusConstants.TASK_REMARK_SECOND_HCRECEIPT);
			role = roleMapper.selectRoleByName(GlobalPara.RNAME_HC_OPERATOR);
			task.setRoleId(role.getId());
			break;
		case 49:
			task.setMsg(BusConstants.TASK_REMARK_SECOND_HCRECEIPT_CONFIRM);
			role = roleMapper.selectRoleByName(GlobalPara.RNAME_PL_OPERATOR);
			task.setRoleId(role.getId());
			break;
		case 50:
			task.setMsg(BusConstants.TASK_REMARK_SECOND_HCRECEIPT_REJECT);
			role = roleMapper.selectRoleByName(GlobalPara.RNAME_HC_OPERATOR);
			task.setRoleId(role.getId());
			break;
		case 51:
			task.setMsg(BusConstants.TASK_REMARK_SECOND_HCRECEIPT_RECHECK);
			role = roleMapper.selectRoleByName(GlobalPara.RNAME_PL_CHECKER);
			task.setRoleId(role.getId());
			break;
		case 52:
			task.setMsg(BusConstants.TASK_REMARK_CUSTOMER_PAY);
			role = roleMapper.selectRoleByName(GlobalPara.RNAME_CN_OTHER_CUSTOMER);
			task.setRoleId(role.getId());
			break;
		case 53:
			task.setMsg(BusConstants.TASK_REMARK_CUSTOMER_PAY_CONFIRM);
			role = roleMapper.selectRoleByName(GlobalPara.RNAME_PL_OPERATOR);
			task.setRoleId(role.getId());
			break;
		case 54:
			task.setMsg(BusConstants.TASK_REMARK_CUSTOMER_PAY_REJECT);
			role = roleMapper.selectRoleByName(GlobalPara.RNAME_CN_OTHER_CUSTOMER);
			task.setRoleId(role.getId());
			break;
		case 55:
			task.setMsg(BusConstants.TASK_REMARK_CUSTOMER_PAY_RECHECK);
			role = roleMapper.selectRoleByName(GlobalPara.RNAME_PL_CHECKER);
			task.setRoleId(role.getId());
			break;
		case 56:
			task.setMsg(BusConstants.TASK_REMARK_HWUSER_RECEIPT);
			role = roleMapper.selectRoleByName(GlobalPara.RNAME_HW_CUSTOMER);
			task.setRoleId(role.getId());
			break;
		case 57:
			task.setMsg(BusConstants.TASK_REMARK_HWUSER_RECEIPT_CONFIRM);
			role = roleMapper.selectRoleByName(GlobalPara.RNAME_PL_OPERATOR);
			task.setRoleId(role.getId());
			break;
		case 58:
			task.setMsg(BusConstants.TASK_REMARK_HWUSER_RECEIPT_REJECT);
			role = roleMapper.selectRoleByName(GlobalPara.RNAME_HW_CUSTOMER);
			task.setRoleId(role.getId());
			break;
		case 59:
			task.setMsg(BusConstants.TASK_REMARK_HWUSER_RECEIPT_RECHECK);
			role = roleMapper.selectRoleByName(GlobalPara.RNAME_PL_CHECKER);
			task.setRoleId(role.getId());
			break;
		case 60:
			task.setMsg(BusConstants.TASK_REMARK_HWUSER_PAY);
			role = roleMapper.selectRoleByName(GlobalPara.RNAME_HW_CUSTOMER);
			task.setRoleId(role.getId());
			break;
		case 61:
			task.setMsg(BusConstants.TASK_REMARK_HWUSER_PAY_CONFIRM);
			role = roleMapper.selectRoleByName(GlobalPara.RNAME_PL_OPERATOR);
			task.setRoleId(role.getId());
			break;
		case 62:
			task.setMsg(BusConstants.TASK_REMARK_HWUSER_PAY_REJECT);
			role = roleMapper.selectRoleByName(GlobalPara.RNAME_HW_CUSTOMER);
			task.setRoleId(role.getId());
			break;
		case 63:
			task.setMsg(BusConstants.TASK_REMARK_HWUSER_PAY_RECHECK);
			role = roleMapper.selectRoleByName(GlobalPara.RNAME_PL_CHECKER);
			task.setRoleId(role.getId());
			break;
		case 64:
			task.setMsg(BusConstants.TASK_REMARK_CUSTOMER_RECEIPT);
			role = roleMapper.selectRoleByName(GlobalPara.RNAME_CN_OTHER_CUSTOMER);
			task.setRoleId(role.getId());
			break;
		case 65:
			task.setMsg(BusConstants.TASK_REMARK_CUSTOMER_RECEIPT_CONFIRM);
			role = roleMapper.selectRoleByName(GlobalPara.RNAME_PL_OPERATOR);
			task.setRoleId(role.getId());
			break;
		case 66:
			task.setMsg(BusConstants.TASK_REMARK_CUSTOMER_RECEIPT_REJECT);
			role = roleMapper.selectRoleByName(GlobalPara.RNAME_CN_OTHER_CUSTOMER);
			task.setRoleId(role.getId());
			break;
		case 67:
			task.setMsg(BusConstants.TASK_REMARK_CUSTOMER_RECEIPT_RECHECK);
			role = roleMapper.selectRoleByName(GlobalPara.RNAME_PL_CHECKER);
			task.setRoleId(role.getId());
			break;
		}

		return task;
	}

	@Override
	public Task getTaskById(String taskId) throws Exception {
		Task task = taskMapper.selectByPrimaryKey(taskId);
		return insertMsgToTask(task);
	}

	private Task insertMsgToTask(Task task){
		switch (task.getTaskType()) {
		case 1:
			task.setMsg(BusConstants.TASK_REMARK_COMMIT);
			break;
		case 2:
			task.setMsg(BusConstants.TASK_REMARK_HWUSERINFO);
			break;
		case 3:
			task.setMsg(BusConstants.TASK_REMARK_HWUSERINFO_REJECT);
			break;
		case 4:
			task.setMsg(BusConstants.TASK_REMARK_FIRST_HCPAY);
			break;
		case 5:
			task.setMsg(BusConstants.TASK_REMARK_CONFIRM_FIRST_HCPAY);
			break;
		case 6:
			task.setMsg(BusConstants.TASK_REMARK_FIRST_HCPAY_VOUCHER_REJECT);
			break;
		case 7:
			task.setMsg(BusConstants.TASK_REMARK_FIRST_HCPAY_VOUCHER_CHECK);
			break;
		case 8:
			task.setMsg(BusConstants.TASK_REMARK_FIRST_HCPAY_MONEYRECEIPT);
			break;
		case 9:
			task.setMsg(BusConstants.TASK_REMARK_FIRST_MONEYRECEIPT_VOUCHER);
			break;
		case 10:
			task.setMsg(BusConstants.TASK_REMARK_FIRST_MONEYRECEIPT_VOUCHER_REJECT);
			break;
		case 11:
			task.setMsg(BusConstants.TASK_REMARK_FIRST_MONEYRECEIPT_VOUCHER_REJECT_CHECK);
			break;
		case 12:
			task.setMsg(BusConstants.TASK_REMARK_HWJPAY);
			break;
		case 13:
			task.setMsg(BusConstants.TASK_REMARK_HWJPAY_CONFIRM);
			break;
		case 14:
			task.setMsg(BusConstants.TASK_REMARK_HWJPAY_CONFIRM_REJECT);
			break;
		case 15:
			task.setMsg(BusConstants.TASK_REMARK_HWJPAY_CONFIRM_CHECK);
			break;
		case 16:
			task.setMsg(BusConstants.TASK_REMARK_HWJPAY_HCECEIPT_CONFIRM);
			break;
		case 17:
			task.setMsg(BusConstants.TASK_REMARK_HWJPAY_HCRECEIPT_VOUCHER);
			break;
		case 18:
			task.setMsg(BusConstants.TASK_REMARK_HWJPAY_HCRECEIPT_VOUCHER_REJECT);
			break;
		case 19:
			task.setMsg(BusConstants.TASK_REMARK_HWJPAY_HCRECEIPT_VOUCHER_CHECK);
			break;
		case 20:
			task.setMsg(BusConstants.TASK_REMARK_SECOND_HCPAY);
			break;
		case 21:
			task.setMsg(BusConstants.TASK_REMARK_SECOND_HCPAY_CONFIRM);
			break;
		case 22:
			task.setMsg(BusConstants.TASK_REMARK_SECOND_HCPAY_CONFIRM_REJECT);
			break;
		case 23:
			task.setMsg(BusConstants.TASK_REMARK_SECOND_HCPAY_RECHECK);
			break;
		case 24:
			task.setMsg(BusConstants.TASK_REMARK_SECOND_HCPAY_RECEIPT_CONFIRM);
			break;
		case 25:
			task.setMsg(BusConstants.TASK_REMARK_SECOND_HCPAY_RECEIPT_VOUCHER);
			break;
		case 26:
			task.setMsg(BusConstants.TASK_REMARK_SECOND_HCPAY_RECEIPT_VOUCHER_REJECT);
			break;
		case 27:
			task.setMsg(BusConstants.TASK_REMARK_SECOND_HCPAY_RECEIPT_VOUCHER_RECHECK);
			break;
		case 28:
			task.setMsg(BusConstants.TASK_REMARK_FIRST_HWUSERPAY);
			break;
		case 29:
			task.setMsg(BusConstants.TASK_REMARK_FIRST_HWUSERPAY_CONFIRM);
			break;
		case 30:
			task.setMsg(BusConstants.TASK_REMARK_FIRST_HWUSERPAY_REJECT);
			break;
		case 31:
			task.setMsg(BusConstants.TASK_REMARK_FIRST_HWUSERPAY_RECHECK);
			break;
		case 32:
			task.setMsg(BusConstants.TASK_REMARK_FIRST_HCRECEIPT);
			break;
		case 33:
			task.setMsg(BusConstants.TASK_REMARK_FIRST_HCRECEIPT_CONFIRM);
			break;
		case 34:
			task.setMsg(BusConstants.TASK_REMARK_FIRST_HCRECEIPT_REJECT);
			break;
		case 35:
			task.setMsg(BusConstants.TASK_REMARK_FIRST_HCRECEIPT_RECHECK);
			break;
		case 36:
			task.setMsg(BusConstants.TASK_REMARK_HC_HWPAY);
			break;
		case 37:
			task.setMsg(BusConstants.TASK_REMARK_HC_HWPAY_CONFIRM);
			break;
		case 38:
			task.setMsg(BusConstants.TASK_REMARK_HC_HWPAY_REJECT);
			break;
		case 39:
			task.setMsg(BusConstants.TASK_REMARK_HC_HWPAY_RECHECK);
			break;
		case 40:
			task.setMsg(BusConstants.TASK_REMARK_HWJ_HWRECEIPT);
			break;
		case 41:
			task.setMsg(BusConstants.TASK_REMARK_HWJ_HWRECEIPT_CONFIRM);
			break;
		case 42:
			task.setMsg(BusConstants.TASK_REMARK_HWJ_HWRECEIPT_REJECT);
			break;
		case 43:
			task.setMsg(BusConstants.TASK_REMARK_HWJ_HWRECEIPT_RECHECK);
			break;
		case 44:
			task.setMsg(BusConstants.TASK_REMARK_SECOND_HWUSERPAY);
			break;
		case 45:
			task.setMsg(BusConstants.TASK_REMARK_SECOND_HWUSERPAY_CONFIRM);
			break;
		case 46:
			task.setMsg(BusConstants.TASK_REMARK_SECOND_HWUSERPAY_REJECT);
			break;
		case 47:
			task.setMsg(BusConstants.TASK_REMARK_SECOND_HWUSERPAY_RECHECK);
			break;
		case 48:
			task.setMsg(BusConstants.TASK_REMARK_SECOND_HCRECEIPT);
			break;
		case 49:
			task.setMsg(BusConstants.TASK_REMARK_SECOND_HCRECEIPT_CONFIRM);
			break;
		case 50:
			task.setMsg(BusConstants.TASK_REMARK_SECOND_HCRECEIPT_REJECT);
			break;
		case 51:
			task.setMsg(BusConstants.TASK_REMARK_SECOND_HCRECEIPT_RECHECK);
			break;
		case 52:
			task.setMsg(BusConstants.TASK_REMARK_CUSTOMER_PAY);
			break;
		case 53:
			task.setMsg(BusConstants.TASK_REMARK_CUSTOMER_PAY_CONFIRM);
			break;
		case 54:
			task.setMsg(BusConstants.TASK_REMARK_CUSTOMER_PAY_REJECT);
			break;
		case 55:
			task.setMsg(BusConstants.TASK_REMARK_CUSTOMER_PAY_RECHECK);
			break;
		case 56:
			task.setMsg(BusConstants.TASK_REMARK_HWUSER_RECEIPT);
			break;
		case 57:
			task.setMsg(BusConstants.TASK_REMARK_HWUSER_RECEIPT_CONFIRM);
			break;
		case 58:
			task.setMsg(BusConstants.TASK_REMARK_HWUSER_RECEIPT_REJECT);
			break;
		case 59:
			task.setMsg(BusConstants.TASK_REMARK_HWUSER_RECEIPT_RECHECK);
			break;
		case 60:
			task.setMsg(BusConstants.TASK_REMARK_HWUSER_PAY);
			break;
		case 61:
			task.setMsg(BusConstants.TASK_REMARK_HWUSER_PAY_CONFIRM);
			break;
		case 62:
			task.setMsg(BusConstants.TASK_REMARK_HWUSER_PAY_REJECT);
			break;
		case 63:
			task.setMsg(BusConstants.TASK_REMARK_HWUSER_PAY_RECHECK);
			break;
		case 64:
			task.setMsg(BusConstants.TASK_REMARK_CUSTOMER_RECEIPT);
			break;
		case 65:
			task.setMsg(BusConstants.TASK_REMARK_CUSTOMER_RECEIPT_CONFIRM);
			break;
		case 66:
			task.setMsg(BusConstants.TASK_REMARK_CUSTOMER_RECEIPT_REJECT);
			break;
		case 67:
			task.setMsg(BusConstants.TASK_REMARK_CUSTOMER_RECEIPT_RECHECK);
			break;
		}
		return task;
	}

	@Override
	public void processTask(Order order, OrderDetails orderDetails,OrderOperate orderOperate, Task curTask, Task newTask) throws Exception {
		if(order != null){
			//更新订单状态
			orderMapper.updateByPrimaryKeySelective(order);
		}
		if(orderDetails != null){
			switch (curTask.getTaskType()) {
			case 3:
				//更新订单明细（海外用户账户信息）
				orderDetailsMapper.updateByPrimaryKeySelective(orderDetails);
				break;
			default:
				//新增订单明细（海外用户账户信息）
				orderDetailsMapper.insertSelective(orderDetails);
				break;
			}
		}
		if(orderOperate != null){
			//新增订单日志
			orderOperateMapper.insertSelective(orderOperate);
		}
		if(curTask != null){
			//更新当前待办状态
			taskMapper.updateByPrimaryKeySelective(curTask);
		}
		if(newTask != null){
			//新增待办至下个流程
			taskMapper.insertSelective(newTask);
		}
	}

	@Override
	public Task getTaskByParams(Map<String, Object> params) throws Exception {
		return taskMapper.getTaskByParams(params);
	}

	@Override
	public void saveNewTask(Task newTask) throws Exception {
		taskMapper.insertSelective(newTask);
	}

}
