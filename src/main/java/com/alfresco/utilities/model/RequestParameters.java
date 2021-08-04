package com.alfresco.utilities.model;

/**
 * @author PlusYourSoftech
 *
 */
public class RequestParameters {
	private String samlResponse;

	public String getSamlResponse() {
		return samlResponse;
	}

	public void setSamlResponse(String samlResponse) {
		this.samlResponse = samlResponse;
	}

	@Override
	public String toString() {
		return "RequestParameters [samlResponse=" + samlResponse + "]";
	}
	
}
