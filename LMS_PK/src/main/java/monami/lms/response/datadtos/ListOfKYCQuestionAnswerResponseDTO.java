package monami.lms.response.datadtos;

import java.util.List;

import monami.lms.responceentities.BasicResponce;

public class ListOfKYCQuestionAnswerResponseDTO extends BasicResponce{
	List<KYCQuestionAnswerResponseDTO> data;

	public ListOfKYCQuestionAnswerResponseDTO(List<KYCQuestionAnswerResponseDTO> data) {
		super(true);
		this.data = data;
	}

	public List<KYCQuestionAnswerResponseDTO> getData() {
		return data;
	}

	public void setData(List<KYCQuestionAnswerResponseDTO> data) {
		this.data = data;
	}

}
