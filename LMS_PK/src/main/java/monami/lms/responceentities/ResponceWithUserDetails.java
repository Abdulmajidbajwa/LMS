package monami.lms.responceentities;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ResponceWithUserDetails extends BasicResponce {

	String username;
	String displayName;
	//Customized by Abdul Majid--Start (With Getter/Setter)
	String category;
	//Customized by Abdul Majid--End
	String role;
	
	
	@JsonProperty("displayName")
	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	
	@JsonProperty("category")
	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	@JsonProperty("username")
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	@JsonProperty("role")
	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

}
