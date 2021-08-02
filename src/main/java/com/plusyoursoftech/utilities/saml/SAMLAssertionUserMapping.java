package com.plusyoursoftech.utilities.saml;

public class SAMLAssertionUserMapping {
     private String id;
     private String email;
     private String firstName;
     private String lastName;
     private String uuid;
     private String permissions;
     private String authenticationDomain;
     
	public String getAuthenticationDomain() {
		return authenticationDomain;
	}
	public void setAuthenticationDomain(String authenticationDomain) {
		this.authenticationDomain = authenticationDomain;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getPermissions() {
		return permissions;
	}
	public void setPermissions(String permissions) {
		this.permissions = permissions;
	}
	@Override
	public String toString() {
		return "SAMLAssertionUserMapping [id=" + id + ", email=" + email + ", firstName=" + firstName + ", lastName="
				+ lastName + ", uuid=" + uuid + ", permissions=" + permissions + ", authenticationDomain="
				+ authenticationDomain + "]";
	}
	
	
	
}
