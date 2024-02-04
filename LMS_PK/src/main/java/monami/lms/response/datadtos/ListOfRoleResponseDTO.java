package monami.lms.response.datadtos;

import java.util.List;

import monami.lms.responceentities.BasicResponce;

public class ListOfRoleResponseDTO extends BasicResponce{
	List<RoleResponseDTO> data;

	public ListOfRoleResponseDTO(List<RoleResponseDTO> data) {
		super(true);
		this.data = data;
	}

	public List<RoleResponseDTO> getData() {
		return data;
	}

	public void setData(List<RoleResponseDTO> data) {
		this.data = data;
	}
}
