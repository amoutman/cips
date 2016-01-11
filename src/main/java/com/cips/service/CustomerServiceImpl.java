package com.cips.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cips.model.Task;

@Service("customerService")
public class CustomerServiceImpl implements CustomerService {

	@Override
	public List<Task> getTaskListByCustomerId(String customerId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void viewTask(String taskId) {
		// TODO Auto-generated method stub

	}

}
