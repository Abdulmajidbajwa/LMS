package monami.lms.response.datadtos;

import java.util.List;

import monami.lms.responceentities.BasicResponce;

public class ListOfDisbursedLoansResponseDTO extends BasicResponce{
	List<DisbursedLoanResponseDTO> data;
	
	public ListOfDisbursedLoansResponseDTO(List<DisbursedLoanResponseDTO> t) {
		super(true);
		data = t;
	}

	public List<DisbursedLoanResponseDTO> getData() {
		return data;
	}

	public void setData(List<DisbursedLoanResponseDTO> data) {
		this.data = data;
	}

	

}
