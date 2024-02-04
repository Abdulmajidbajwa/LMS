package monami.lms.response.datadtos;

import java.util.List;

public class RoleResponseDTO {

	private int id;
	private String roleName;
	private String roleDescription;
	private List<PrivilegeResponseDTO> privilegeDTOList;

	private ApplicationUserResponseDTO createdBy;
	private String createdAt;
	private ApplicationUserResponseDTO updatedBy;
	private String updatedAt;
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public String getRoleDescription() {
		return roleDescription;
	}
	public void setRoleDescription(String roleDescription) {
		this.roleDescription = roleDescription;
	}
	public List<PrivilegeResponseDTO> getPrivilegeDTOList() {
		return privilegeDTOList;
	}
	public void setPrivilegeDTOList(List<PrivilegeResponseDTO> privilegeDTOList) {
		this.privilegeDTOList = privilegeDTOList;
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
