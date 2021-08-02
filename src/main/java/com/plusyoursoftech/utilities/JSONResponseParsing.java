package com.plusyoursoftech.utilities;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.tomcat.util.json.JSONParser;
import org.apache.tomcat.util.json.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.plusyoursoftech.utilities.model.UserData;

/**
 * @author plusyoursoftech-utilities
 *
 */
public class JSONResponseParsing {
	static Logger logger = LoggerFactory.getLogger(JSONResponseParsing.class);
	@SuppressWarnings("unchecked")
	public static ArrayList<Map<String, Object>> getJsonParsedData(String responseData) {
		JSONParser parser = new JSONParser(responseData);

		Object historyTaskdata = null;
		try {
			LinkedHashMap<String, Object> parseObject = parser.parseObject();

			historyTaskdata = parseObject.get("data");
			if (historyTaskdata == null || !(historyTaskdata instanceof ArrayList)
					|| ((ArrayList<Map<String, Object>>) historyTaskdata).size() == 0) {
				return null;
			}
		} catch (ParseException e) {
			logger.error("Exception",e);
		}
		return (ArrayList<Map<String, Object>>) historyTaskdata;
	}
@SuppressWarnings("unchecked")
public static Map<String,UserData> getJsonParsedGroupUsers(String responseData) {
	JSONParser parser = new JSONParser(responseData);

	Map<String, UserData> userlist = new HashMap<>();
	try {
		LinkedHashMap<String, Object> parseObject = parser.parseObject();

		Map listOfData = (Map)parseObject.get("list");
		Map pagination = (Map)listOfData.get("pagination");
		BigInteger totalItems = (BigInteger)pagination.get("totalItems");
		
		List<?> entries = (ArrayList)listOfData.get("entries");
		entries.forEach(entry->{
			Map entryMap = (Map)entry;
			Map entryData = (Map)entryMap.get("entry");
			UserData UserData =new UserData();
			UserData.setUserName(((String)entryData.get("id")).trim().toLowerCase());
			
			userlist.put(UserData.getUserName(),UserData);
		});
		
		
	} catch (ParseException e) {
		logger.error("Exception",e);
	}
	return userlist;
}
	@SuppressWarnings("unchecked")
	public static Map<String,UserData> getJsonParsedDataForList(String responseData) {
		JSONParser parser = new JSONParser(responseData);

		Map<String, UserData> userlist = new HashMap<>();
		try {
			LinkedHashMap<String, Object> parseObject = parser.parseObject();

			Map listOfData = (Map)parseObject.get("list");
			List<?> entries = (ArrayList)listOfData.get("entries");
			entries.forEach(entry->{
				Map entryMap = (Map)entry;
				Map entryData = (Map)entryMap.get("entry");
				UserData userData =new UserData();
				userData.setUserName((String)entryData.get("id"));
				userData.setEmail((String)entryData.get("email"));
				userData.setFirstName((String)entryData.get("firstName"));
				userData.setLastName((String)entryData.get("lastName"));
				userData.setDisabledAccount((Boolean)entryData.get("disableAccount"));
				userlist.put(userData.getUserName(),userData);
			});
			
			
		} catch (ParseException e) {
			logger.error("Exception",e);
		}
		return userlist;
	}
	
	
	@SuppressWarnings("unchecked")
	public static Map<String,UserData> getJsonParsedDataForList(Map listOfData) {
		Map<String, UserData> userlist = new HashMap<>();
			List<?> entries = (ArrayList)listOfData.get("entries");
			entries.forEach(entry->{
				Map entryMap = (Map)entry;
				Map entryData = (Map)entryMap.get("entry");
				UserData UserData =new UserData();
				UserData.setUserName((String)entryData.get("id"));
				UserData.setEmail((String)entryData.get("email"));
				UserData.setFirstName((String)entryData.get("firstName"));
				UserData.setLastName((String)entryData.get("lastName"));
				UserData.setDisabledAccount((Boolean)entryData.get("disableAccount"));
				userlist.put(UserData.getUserName(),UserData);
			});
		return userlist;
	}
	
	
	@SuppressWarnings("unchecked")
	public static List<String> getJsonParsedGroupName(String responseData) {
		JSONParser parser = new JSONParser(responseData);

		List<String> groupList = new ArrayList<>();
		try {
			LinkedHashMap<String, Object> parseObject = parser.parseObject();
		
			List<?> groups = (ArrayList)parseObject.get("groups");
			groups.forEach(entry->{
				Map entryMap = (Map)entry;
				groupList.add(entryMap.get("itemName").toString());
		});
			
		} catch (ParseException e) {
			logger.error("Exception",e);
		}
		return groupList;
	}
	
}
