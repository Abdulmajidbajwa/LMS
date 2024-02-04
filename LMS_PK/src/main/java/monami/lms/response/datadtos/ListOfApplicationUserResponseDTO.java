package monami.lms.response.datadtos;

import java.util.List;

import monami.lms.responceentities.BasicResponce;

public class ListOfApplicationUserResponseDTO extends BasicResponce{
	List<ApplicationUserResponseDTO> data;

	public ListOfApplicationUserResponseDTO(List<ApplicationUserResponseDTO> data) {
		super(true);
		this.data = data;
	}

	public List<ApplicationUserResponseDTO> getData() {
		return data;
	}

	public void setData(List<ApplicationUserResponseDTO> data) {
		this.data = data;
	}
}
