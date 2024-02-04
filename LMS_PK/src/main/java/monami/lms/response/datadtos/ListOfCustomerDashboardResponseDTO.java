package monami.lms.response.datadtos;

import java.util.List;

import monami.lms.responceentities.BasicResponce;

public class ListOfCustomerDashboardResponseDTO extends BasicResponce{
	List<CustomerDashboardResponseDTO> data;

	public ListOfCustomerDashboardResponseDTO(List<CustomerDashboardResponseDTO> data) {
		super(true);
		this.data = data;
	}

	public List<CustomerDashboardResponseDTO> getData() {
		return data;
	}

	public void setData(List<CustomerDashboardResponseDTO> data) {
		this.data = data;
	}
}
