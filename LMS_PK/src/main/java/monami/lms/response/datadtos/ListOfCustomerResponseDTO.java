package monami.lms.response.datadtos;

import java.util.List;

import monami.lms.responceentities.BasicResponce;

public class ListOfCustomerResponseDTO extends BasicResponce{
	List<CustomerResponseDTO> data;

	public ListOfCustomerResponseDTO(List<CustomerResponseDTO> data) {
		super(true);
		this.data = data;
	}

	public List<CustomerResponseDTO> getData() {
		return data;
	}

	public void setData(List<CustomerResponseDTO> data) {
		this.data = data;
	}
}
