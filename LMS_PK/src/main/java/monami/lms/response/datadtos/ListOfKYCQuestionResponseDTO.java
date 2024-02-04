package monami.lms.response.datadtos;

import java.util.List;

import monami.lms.responceentities.BasicResponce;

public class ListOfKYCQuestionResponseDTO extends BasicResponce{
	List<KYCQuestionResponseDTO> data;

	public ListOfKYCQuestionResponseDTO(List<KYCQuestionResponseDTO> data) {
		super(true);
		this.data = data;
	}

	public List<KYCQuestionResponseDTO> getData() {
		return data;
	}

	public void setData(List<KYCQuestionResponseDTO> data) {
		this.data = data;
	}
}
