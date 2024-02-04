package monami.lms.response.datadtos;

import java.util.List;

import monami.lms.responceentities.BasicResponce;

public class ListOfProductSpecificationResponseDTO extends BasicResponce{
	List<ProductSpecificationResponseDTO> data;

	public ListOfProductSpecificationResponseDTO(List<ProductSpecificationResponseDTO> data) {
		super(true);
		this.data = data;
	}

	public List<ProductSpecificationResponseDTO> getData() {
		return data;
	}

	public void setData(List<ProductSpecificationResponseDTO> data) {
		this.data = data;
	}
}
