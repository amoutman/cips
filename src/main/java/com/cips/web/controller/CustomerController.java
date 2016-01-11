package com.cips.web.controller;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cips.service.CustomerService;

@Controller
@RequestMapping("/customer")
public class CustomerController {

	final static Logger logger = LoggerFactory.getLogger(CustomerController.class); 
	
	@Resource(name="CustomerService")
	private CustomerService customerServic;
	
	
}
