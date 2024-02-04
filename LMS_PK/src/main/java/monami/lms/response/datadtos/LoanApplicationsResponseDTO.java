package monami.lms.response.datadtos;

import monami.lms.responceentities.BasicResponce;

public class LoanApplicationsResponseDTO extends BasicResponce{
	private int id;
	private ProductResponseDTO product;
	private CustomerResponseDTO customer;
	private int walletTypeId;
	private int requestedamount;
	private String status;
	
	private CustomerLoanSummaryResponseDTO customerLoanSummary;

	
	private String createdAt;
	private ApplicationUserResponseDTO createdBy;
	private String updatedAt;
	private ApplicationUserResponseDTO updatedBy;
	
	
	public LoanApplicationsResponseDTO() {
		
	}
	

	public LoanApplicationsResponseDTO(int id, ProductResponseDTO product, CustomerResponseDTO customer, int requestedamount, String status) {
		super();
		this.id = id;
		this.product = product;
		this.customer = customer;
		this.requestedamount = requestedamount;
		this.status = status;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public ProductResponseDTO getProduct() {
		return product;
	}

	public void setProduct(ProductResponseDTO product) {
		this.product = product;
	}

	public CustomerResponseDTO getCustomer() {
		return customer;
	}

	public void setCustomer(CustomerResponseDTO customer) {
		this.customer = customer;
	}

	public int getWalletTypeId() {
		return walletTypeId;
	}
	public void setWalletTypeId(int walletTypeId) {
		this.walletTypeId = walletTypeId;
	}
	public int getRequestedamount() {
		return requestedamount;
	}
	public void setRequestedamount(int requestedamount) {
		this.requestedamount = requestedamount;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public CustomerLoanSummaryResponseDTO getCustomerLoanSummary() {
		return customerLoanSummary;
	}
	public void setCustomerLoanSummary(
			CustomerLoanSummaryResponseDTO customerLoanSummary) {
		this.customerLoanSummary = customerLoanSummary;
	}
	public ApplicationUserResponseDTO getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(ApplicationUserResponseDTO createdBy) {
		this.createdBy = createdBy;
	}
	public ApplicationUserResponseDTO getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(ApplicationUserResponseDTO updatedBy) {
		this.updatedBy = updatedBy;
	}
	public String getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}
	public String getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(String updatedAt) {
		this.updatedAt = updatedAt;
	}
	
	
}
