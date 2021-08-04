package com.alfresco.utilities.service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.tomcat.util.json.JSONParser;
import org.apache.tomcat.util.json.ParseException;
import org.opensaml.ws.message.decoder.MessageDecodingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;

import com.alfresco.utilities.AuthenticationUtils;
import com.alfresco.utilities.JSONResponseParsing;
import com.alfresco.utilities.client.AlfrescoContentServiceCall;
import com.alfresco.utilities.config.ApplicationConfig;
import com.alfresco.utilities.model.RequestParameters;
import com.alfresco.utilities.model.UserData;
import com.alfresco.utilities.saml.DecodeSAMLResponse;
import com.alfresco.utilities.saml.SAMLAssertionUserMapping;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author plusyoursoftech
 */
@Component
public class AlfrescoContentService {
	Logger logger = LoggerFactory.getLogger(AlfrescoContentService.class);

	@Autowired
	private AuthenticationUtils authenticationUtils;
	@Autowired
	private AlfrescoContentServiceCall alfrescoContentServiceCall;
	@Autowired
	private ApplicationConfig applicationConfig;
	
	private String basicAuth;
	private void buildBasicAuth()
	{
		basicAuth = authenticationUtils.getAcsLogin();
	}
	Boolean hasMoreItems;

	private Map<String, UserData> getAllUsersFromAlfresco() {
		int skipCount = 0;
		int maxCount = 1000;
		hasMoreItems = true;
		Map<String, UserData> usersFromAlfresco = new HashMap<>();
		logger.info(" while running while Has More Items: " + hasMoreItems);
		while (hasMoreItems) {
			logger.info(" After while running while Has More Items: " + hasMoreItems);
			usersFromAlfresco.putAll(getUsersFromAlfresco(skipCount, maxCount));
			skipCount = skipCount + 1000;
			logger.info("Counting User from Alfresco: " + usersFromAlfresco.size());

		}

		return usersFromAlfresco;
	}

	private Map<String, UserData> getUsersFromAlfresco(int skipCount, int maxCount) {
		logger.info("skipCount: " + skipCount + " maxCount:" + maxCount + " hasMoreItems: " + hasMoreItems);

		ResponseEntity<String> userFromAlfresco = alfrescoContentServiceCall.getUsers(basicAuth, "" + skipCount,
				"" + maxCount);
		String responseData = userFromAlfresco.getBody();

		JSONParser parser = new JSONParser(responseData);
		Map<String, UserData> usersFromAlfresco = null;
		try {
			LinkedHashMap<String, Object> parseObject = parser.parseObject();
			Map listOfData = (Map) parseObject.get("list");
			usersFromAlfresco = JSONResponseParsing.getJsonParsedDataForList(listOfData);
			Map pagination = (Map) listOfData.get("pagination");
			BigInteger totalItems = (BigInteger) pagination.get("totalItems");
			logger.info("Total Items: " + totalItems.intValue());
			hasMoreItems = (Boolean) pagination.get("hasMoreItems");
			logger.info("Has More Items: " + hasMoreItems);

		} catch (ParseException e) {
			logger.error("Error: ", e);
		}

		return usersFromAlfresco;
	}

	private Map<String, UserData> getUsersFromGroupAlfresco(String groupName) {
		ResponseEntity<String> userFromAlfresco = alfrescoContentServiceCall.getUsersFromGroup(basicAuth,
				"GROUP_" + groupName);
		logger.info(" getUsersFromGroupAlfresco : " + userFromAlfresco.getBody());
		Map<String, UserData> usersFromAlfresco = JSONResponseParsing
				.getJsonParsedGroupUsers(userFromAlfresco.getBody());
		return usersFromAlfresco;
	}

	/**
	 * @param groupName
	 */
	private void createGroupIfNotExist(String groupName) {
		try {
			alfrescoContentServiceCall.getGroupById(basicAuth, "GROUP_" + groupName);
			return;
		} catch (Exception e) {
			String groupBody = "{ \"id\": \"" + groupName + "\"}";
			logger.info("Create group --- > " + groupBody);
			alfrescoContentServiceCall.createGroup(basicAuth, groupBody);
		}
	}

	/**
	 * @param userDTO
	 * @return
	 */
	private Map<String, Object> parseUserObject(UserData userDTO) {
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			String requestBody = objectMapper.writeValueAsString(userDTO);
			JSONParser parser = new JSONParser(requestBody);
			return parser.parseObject();
		} catch (ParseException | JsonProcessingException e) {
			logger.error("Exception", e);
		}
		return null;
	}

	/**
	 * @param userDTO
	 */
	private void updateUser(UserData userDTO) {
		ObjectMapper objectMapper = new ObjectMapper();
		Map<String, Object> parseObject = parseUserObject(userDTO);
		try {
			parseObject.remove("userName");
			parseObject.remove("password");

			removeNuLLValue(parseObject);

			String requestBody = objectMapper.writeValueAsString(parseObject);
			logger.info(userDTO.getUserName() + " ->  Request Body to update user--- > " + requestBody);
			alfrescoContentServiceCall.updateUser(basicAuth, userDTO.getUserName(), requestBody);

		} catch (JsonProcessingException e) {
			logger.error("JsonProcessingException", e);
		} catch (Exception e) {
			logger.error("Exception", e);
		}

	}

	/**
	 * @param parseObject
	 */
	private void removeNuLLValue(Map<String, Object> parseObject) {
		List<String> valueToRemove = new ArrayList<>();
		parseObject.forEach((key, value) -> {
			if (value == null || "null".equalsIgnoreCase(value.toString())) {
				valueToRemove.add(key);
			}
		});
		valueToRemove.forEach(key -> {
			parseObject.remove(key);
		});
	}

	/**
	 * @param userDTO
	 */
	private void createUser(UserData userDTO) {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			Map<String, Object> parseObject = parseUserObject(userDTO);
			parseObject.put("password", "DefaultPassword");
			removeNuLLValue(parseObject);

			String requestBody = objectMapper.writeValueAsString(parseObject);
			logger.info(" Create User --- > " + requestBody);
			alfrescoContentServiceCall.createUser(basicAuth, requestBody);
		} catch (Exception e) {
			logger.error("Exception", e);
		}
	}

	public String deleteUsers() {
		ResponseEntity<String> userFromAlfresco = alfrescoContentServiceCall.getUsers(authenticationUtils.getAcsLogin(),
				"" + 0, "" + 1000);

		Map<String, UserData> usersFromAlfresco = JSONResponseParsing
				.getJsonParsedDataForList(userFromAlfresco.getBody());
		List<String> doNotDelete = new ArrayList<>();
		doNotDelete.add("admin");
		doNotDelete.add("guest");
		usersFromAlfresco.forEach((userId, userDTO) -> {
			if (!doNotDelete.contains(userId)) {
				logger.info("User is being deleted : " + userId);
				alfrescoContentServiceCall.deleteUser(basicAuth, userId);
			}

		});

		return "Users are deleted";
	}

	public void syncUser(RequestParameters requestParameters) {
		buildBasicAuth();
		logger.info(" syncUser started  --- > " + requestParameters);
		DecodeSAMLResponse decodeSAMLResponse =new DecodeSAMLResponse();
		
		try {
			SAMLAssertionUserMapping userMapping = decodeSAMLResponse.getBase64DecodedMessage(requestParameters.getSamlResponse());
			UserData userData = mapUserWithSamlResponse(userMapping);
			createUser(userData);
			
		} catch (MessageDecodingException e) {
			logger.error("MessageDecodingException :",e);
		}
	}
	private UserData mapUserWithSamlResponse(SAMLAssertionUserMapping userMapping){
		UserData userData = new UserData();
		userData.setUserName(userMapping.getId());
		userData.setEmail(userMapping.getEmail());
		userData.setFirstName(userMapping.getFirstName());
		userData.setLastName(userMapping.getLastName());
		return userData;
	}
}
