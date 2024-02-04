package monami.lms.response.datadtos;

import java.util.List;

import monami.lms.responceentities.BasicResponce;

public class ListOfCustomerTransactionResponseDTO  extends BasicResponce{
	List<CustomerTransactionResponseDTO> obj;
	
	

	public ListOfCustomerTransactionResponseDTO(List<CustomerTransactionResponseDTO> obj) {
		super(true);
		this.obj = obj;
	}

	public List<CustomerTransactionResponseDTO> getObj() {
		return obj;
	}

	public void setObj(List<CustomerTransactionResponseDTO> obj) {
		this.obj = obj;
	}
	
	
}
