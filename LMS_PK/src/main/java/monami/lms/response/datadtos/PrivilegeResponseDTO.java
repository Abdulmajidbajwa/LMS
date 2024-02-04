package monami.lms.response.datadtos;

import java.util.Date;

import monami.lms.dataentities.ApplicationUsers;

public class PrivilegeResponseDTO {

	private int privilegeId;
	private String privilegeName;
	private String privilegeDescription;

	private ApplicationUserResponseDTO createdBy;
	private String createdAt;
	private ApplicationUserResponseDTO updatedBy;
	private String updatedAt;
	
	public PrivilegeResponseDTO() {
		super();
	}
	public PrivilegeResponseDTO(int privilegeId, String privilegeName,
			String privilegeDescription, ApplicationUserResponseDTO createdBy,
			String createdAt, ApplicationUserResponseDTO updatedBy,
			String updatedAt) {
		super();
		this.privilegeId = privilegeId;
		this.privilegeName = privilegeName;
		this.privilegeDescription = privilegeDescription;
		this.createdBy = createdBy;
		this.createdAt = createdAt;
		this.updatedBy = updatedBy;
		this.updatedAt = updatedAt;
	}
	public int getPrivilegeId() {
		return privilegeId;
	}
	public void setPrivilegeId(int privilegeId) {
		this.privilegeId = privilegeId;
	}
	public String getPrivilegeName() {
		return privilegeName;
	}
	public void setPrivilegeName(String privilegeName) {
		this.privilegeName = privilegeName;
	}
	public String getPrivilegeDescription() {
		return privilegeDescription;
	}
	public void setPrivilegeDescription(String privilegeDescription) {
		this.privilegeDescription = privilegeDescription;
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
