package monami.lms.responceentities;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ResponceWithCustomerToken extends BasicResponce{
	String token;
	public ResponceWithCustomerToken(boolean requestAction,String t) {
		super(requestAction);
		this.token=t;
	}
	
	@JsonProperty("Token")
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	
}
