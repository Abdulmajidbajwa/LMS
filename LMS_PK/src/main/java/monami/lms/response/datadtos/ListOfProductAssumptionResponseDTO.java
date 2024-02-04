package monami.lms.response.datadtos;

import java.util.List;

import monami.lms.responceentities.BasicResponce;

public class ListOfProductAssumptionResponseDTO extends BasicResponce{
	List<ProductAssumptionResponseDTO> data;

	public ListOfProductAssumptionResponseDTO(List<ProductAssumptionResponseDTO> data) {
		super(true);
		this.data = data;
	}

	public List<ProductAssumptionResponseDTO> getData() {
		return data;
	}

	public void setData(List<ProductAssumptionResponseDTO> data) {
		this.data = data;
	}
}
