package com.alfresco.utilities.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alfresco.utilities.model.RequestParameters;
import com.alfresco.utilities.service.AlfrescoContentService;

/**
 * @author Plusyoursoftech
 *
 */
@RestController
@RequestMapping("/utilities")
public class UserSyncController {
	Logger logger = LoggerFactory.getLogger(UserSyncController.class);
	@Autowired
	private AlfrescoContentService alfrescoContentService;
	
	@PostMapping(path = "/syncuser", consumes = "application/json", produces = "application/json")
	public String syncUser(@RequestBody RequestParameters requestParameters) {
		try {
			
		   alfrescoContentService.syncUser(requestParameters);
		} catch (Exception e) {
			logger.error("Error",e);
			return "Internal error occurs";
		}
		return "successfully merged";
	}

}
