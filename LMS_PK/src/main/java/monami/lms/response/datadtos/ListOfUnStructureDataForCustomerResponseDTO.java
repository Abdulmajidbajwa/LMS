package monami.lms.response.datadtos;

import java.util.List;

import monami.lms.responceentities.BasicResponce;

public class ListOfUnStructureDataForCustomerResponseDTO  extends BasicResponce{
	List<UnStrunctredDataResponseDTO> data;

	public ListOfUnStructureDataForCustomerResponseDTO(List<UnStrunctredDataResponseDTO> t) {
		super(true);
		data = t;
	}

	public List<UnStrunctredDataResponseDTO> getData() {
		return data;
	}

	public void setData(List<UnStrunctredDataResponseDTO> data) {
		this.data = data;
	}
}
