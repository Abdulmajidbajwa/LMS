package monami.lms.response.datadtos;

import java.util.List;

import monami.lms.responceentities.BasicResponce;

public class ListOfPreferenceResponseDTO extends BasicResponce{
	List<PreferenceResponseDTO> data;

	public ListOfPreferenceResponseDTO(List<PreferenceResponseDTO> data) {
		super(true);
		this.data = data;
	}

	public List<PreferenceResponseDTO> getData() {
		return data;
	}

	public void setData(List<PreferenceResponseDTO> data) {
		this.data = data;
	}
}
