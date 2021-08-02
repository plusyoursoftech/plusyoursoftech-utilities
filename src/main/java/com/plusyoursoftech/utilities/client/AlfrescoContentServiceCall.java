package com.plusyoursoftech.utilities.client;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import feign.Headers;


/**
 * @author Plusyoursoftech
 *
 */
public interface AlfrescoContentServiceCall {

	@RequestMapping(method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE, value = "api/-default-/public/alfresco/versions/1/people?skipCount={skipCount}&maxItems={maxItems}")
	public ResponseEntity<String> getUsers(@RequestHeader("Authorization") String authHeader, @PathVariable("skipCount") String skipCount, @PathVariable("maxItems") String maxItems);
	
	@RequestMapping(method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE, value = "api/-default-/public/alfresco/versions/1/groups/{groupName}/members?skipCount=0&maxItems=999999")
	public ResponseEntity<String> getUsersFromGroup(@RequestHeader("Authorization") String authHeader, @PathVariable("groupName") String groupName);

	
	@RequestMapping(method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, value = "service/api/people/{personId}")
	@Headers({
		"Content-Type: " + MediaType.APPLICATION_JSON_VALUE,
		   "Accept:" + MediaType.APPLICATION_JSON_VALUE
		 })
	public ResponseEntity<String> updateUser(@RequestHeader("Authorization") String authHeader, @PathVariable("personId") String personId, @RequestBody String requestBody);

	
	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, value = "service/api/people")
	@Headers({
		"Content-Type: " + MediaType.APPLICATION_JSON_VALUE,
		   "Accept:" + MediaType.APPLICATION_JSON_VALUE
		 })
	public ResponseEntity<String> createUser(@RequestHeader("Authorization") String authHeader, @RequestBody String requestBody);
		
	@RequestMapping(method = RequestMethod.DELETE, consumes = MediaType.APPLICATION_JSON_VALUE, value = "service/api/people/{userName}")
	public ResponseEntity<String> deleteUser(@RequestHeader("Authorization") String authHeader,@PathVariable("userName") String userName);
	
	
	@RequestMapping(method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, value = "api/-default-/public/alfresco/versions/1/nodes/{nodeid}")
	@Headers({
		"Content-Type: " + MediaType.APPLICATION_JSON_VALUE,
		   "Accept:" + MediaType.APPLICATION_JSON_VALUE
		 })
	public ResponseEntity<String> updateNode(@RequestHeader("Authorization") String authHeader, @PathVariable("nodeid") String nodeid, @RequestBody String requestBody);
	
	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, value = "api/-default-/public/alfresco/versions/1/groups")
	@Headers({
		"Content-Type: " + MediaType.APPLICATION_JSON_VALUE,
		   "Accept:" + MediaType.APPLICATION_JSON_VALUE
		 })
	public ResponseEntity<String> createGroup(@RequestHeader("Authorization") String authHeader, @RequestBody String requestBody);
	
	
	
	@RequestMapping(method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE, value = "api/-default-/public/alfresco/versions/1/groups/{groupId}")
	@Headers({
		"Content-Type: " + MediaType.APPLICATION_JSON_VALUE,
		   "Accept:" + MediaType.APPLICATION_JSON_VALUE
		 })
	public ResponseEntity<String> getGroupById(@RequestHeader("Authorization") String authHeader, @PathVariable("groupId") String groupId);

	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, value = "api/-default-/public/alfresco/versions/1/groups/{groupId}/members")
	@Headers({
		"Content-Type: " + MediaType.APPLICATION_JSON_VALUE,
		   "Accept:" + MediaType.APPLICATION_JSON_VALUE
		 })
	public ResponseEntity<String> addMemberToGroup(@RequestHeader("Authorization") String authHeader,@PathVariable("groupId") String groupId, @RequestBody String requestBody);

	@RequestMapping(method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE, value = "service/api/people/{userId}?groups=true")
	@Headers({
		"Content-Type: " + MediaType.APPLICATION_JSON_VALUE,
		   "Accept:" + MediaType.APPLICATION_JSON_VALUE
		 })
	public ResponseEntity<String> getGroupByMemberId(@RequestHeader("Authorization") String authHeader, @PathVariable("userId") String userId);

	
	
}
