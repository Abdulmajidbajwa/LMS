package monami.lms.response.datadtos;

import java.util.List;

import monami.lms.responceentities.BasicResponce;

public class ListOfProductResponseDTOs extends BasicResponce {
	List<ProductResponseDTO> data;

	public ListOfProductResponseDTOs(List<ProductResponseDTO> productDTOs) {
		super(true);
		data = productDTOs;
	}

	public List<ProductResponseDTO> getData() {
		return data;
	}

	public void setData(List<ProductResponseDTO> data) {
		this.data = data;
	}
	
}
