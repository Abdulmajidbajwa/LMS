package monami.lms.response.datadtos;

import java.util.List;

import monami.lms.responceentities.BasicResponce;

public class ListOfLoanApplicationsResponseDTO extends BasicResponce{
	List<LoanApplicationsResponseDTO> data;
	
	public ListOfLoanApplicationsResponseDTO(List<LoanApplicationsResponseDTO> data) {
		super(true);
		this.data = data;
	}

	public List<LoanApplicationsResponseDTO> getData() {
		return data;
	}

	public void setData(List<LoanApplicationsResponseDTO> data) {
		this.data = data;
	}

}
