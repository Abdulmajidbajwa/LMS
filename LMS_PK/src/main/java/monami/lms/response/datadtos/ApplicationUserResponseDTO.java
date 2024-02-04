package monami.lms.response.datadtos;

import java.util.Collection;
import java.util.Date;
import monami.lms.dataentities.Role;

public class ApplicationUserResponseDTO {

	private int userId;
	private String displayName;
	private String username;
	private String password;
	private String category; 
	
	private String lastloginAttempt;
	private String assignToken;
	private String lastAccess;
	private String appliedTheme;
	private int loginCounter=0;

	private Collection<RoleResponseDTO> roles;
	private ApplicationUserResponseDTO createdBy;
	private String createdAt;

	private ApplicationUserResponseDTO updatedBy;
	private String updatedAt;
	
	public ApplicationUserResponseDTO() {
		super();
	}
	
	public ApplicationUserResponseDTO(int userId, String displayName) {
		super();
		this.userId = userId;
		this.displayName = displayName;
	}
	
	public ApplicationUserResponseDTO(int userId, String displayName, String username,
			String password, String category, String lastloginAttempt,
			String assignToken, String lastAccess, String appliedTheme,
			int loginCounter, Collection<RoleResponseDTO> roles,
			ApplicationUserResponseDTO createdBy, String createdAt,
			ApplicationUserResponseDTO updatedBy, String updatedAt) {
		super();
		this.userId = userId;
		this.displayName = displayName;
		this.username = username;
		this.password = password;
		this.category = category;
		this.lastloginAttempt = lastloginAttempt;
		this.assignToken = assignToken;
		this.lastAccess = lastAccess;
		this.appliedTheme = appliedTheme;
		this.loginCounter = loginCounter;
		this.roles = roles;
		this.createdBy = createdBy;
		this.createdAt = createdAt;
		this.updatedBy = updatedBy;
		this.updatedAt = updatedAt;
	}
	
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getLastloginAttempt() {
		return lastloginAttempt;
	}
	public void setLastloginAttempt(String lastloginAttempt) {
		this.lastloginAttempt = lastloginAttempt;
	}
	public String getAssignToken() {
		return assignToken;
	}
	public void setAssignToken(String assignToken) {
		this.assignToken = assignToken;
	}
	public String getLastAccess() {
		return lastAccess;
	}
	public void setLastAccess(String lastAccess) {
		this.lastAccess = lastAccess;
	}
	public String getAppliedTheme() {
		return appliedTheme;
	}
	public void setAppliedTheme(String appliedTheme) {
		this.appliedTheme = appliedTheme;
	}
	public int getLoginCounter() {
		return loginCounter;
	}
	public void setLoginCounter(int loginCounter) {
		this.loginCounter = loginCounter;
	}
	public Collection<RoleResponseDTO> getRoles() {
		return roles;
	}
	public void setRoles(Collection<RoleResponseDTO> roles) {
		this.roles = roles;
	}
	public ApplicationUserResponseDTO getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(ApplicationUserResponseDTO createdBy) {
		this.createdBy = createdBy;
	}
	public String getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}
	public ApplicationUserResponseDTO getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(ApplicationUserResponseDTO updatedBy) {
		this.updatedBy = updatedBy;
	}

	public String getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(String updatedAt) {
		this.updatedAt = updatedAt;
	}
	
}
