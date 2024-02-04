package monami.lms.response.datadtos;

import java.util.List;

import monami.lms.responceentities.BasicResponce;

public class ListOfPrivilegeResponseDTO extends BasicResponce{
	List<PrivilegeResponseDTO> data;

	public ListOfPrivilegeResponseDTO(List<PrivilegeResponseDTO> data) {
		super(true);
		this.data = data;
	}

	public List<PrivilegeResponseDTO> getData() {
		return data;
	}

	public void setData(List<PrivilegeResponseDTO> data) {
		this.data = data;
	}
}
