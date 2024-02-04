package monami.lms.responceentities;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ResponceWithTokenAndUserDetails extends BasicResponce {
	
	String token;
	String username;
	long userId;
	String displayName;
	String appliedTheme;
	List<String> roles;

	public ResponceWithTokenAndUserDetails(){

	}
	
	public ResponceWithTokenAndUserDetails(boolean requestAction,String t) {
		super(requestAction);
		this.token=t;
	}
	
	public ResponceWithTokenAndUserDetails(boolean requestAction, String createToken, List<String> roles, String displayName, String username, String appliedTheme) {
		super(requestAction);
		this.token=createToken;
		this.roles = roles;
		this.displayName = displayName;
		this.username = username;
		this.appliedTheme = appliedTheme;
	}
	
	public ResponceWithTokenAndUserDetails(boolean requestAction, String createToken, List<String> roles, String displayName, String username,long userId, String appliedTheme) {
		super(requestAction);
		this.token=createToken;
		this.roles = roles;
		this.displayName = displayName;
		this.username = username;
		this.userId=userId;
		this.appliedTheme = appliedTheme;
	}

	

	@JsonProperty("Token")
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	

	@JsonProperty("roles")
	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}
	
	@JsonProperty("displayName")
	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	
	@JsonProperty("username")
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	@JsonProperty("userId")
	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	@JsonProperty("appliedTheme")
	public String getAppliedTheme() {
		return appliedTheme;
	}

	public void setAppliedTheme(String appliedTheme) {
		this.appliedTheme = appliedTheme;
	}


}
