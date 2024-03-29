package com.plusyoursoftech.utilities.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.plusyoursoftech.utilities.config.ApplicationConfig;

/**
 * @author Plusyoursoftech
 *
 */
@RestController
public class ApplicationController {
	@Autowired
	ApplicationConfig applicationConfig;

	@RequestMapping("/")
	public String SpringBootESExample() {
		return "Welcome to plusyoursoftech-utilities Service";
	}
	
}
