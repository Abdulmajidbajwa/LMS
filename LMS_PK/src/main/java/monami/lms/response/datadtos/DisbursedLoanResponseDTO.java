package monami.lms.response.datadtos;

import monami.lms.utilities.DisbursedLoanStatus;

public class DisbursedLoanResponseDTO {

	private int id;
	private LoanApplicationsResponseDTO application;
	private String transactionId;
	private DisbursedLoanStatus loanStatus;
	private String dueDate;
	private String graceDueDate;
	
	private Long outstandingAmount;
	
	private String createdAt;
	private ApplicationUserResponseDTO createdBy;
	private String updatedAt;
	private ApplicationUserResponseDTO updatedBy;
	
	public DisbursedLoanResponseDTO() {
		super();
	}
	public DisbursedLoanResponseDTO(int id, LoanApplicationsResponseDTO application,
			String transactionId, DisbursedLoanStatus loanStatus,
			String dueDate, String graceDueDate, Long outstandingAmount,
			String createdAt, ApplicationUserResponseDTO createdBy, String updatedAt,
			ApplicationUserResponseDTO updatedBy) {
		super();
		this.id = id;
		this.application = application;
		this.transactionId = transactionId;
		this.loanStatus = loanStatus;
		this.dueDate = dueDate;
		this.graceDueDate = graceDueDate;
		this.outstandingAmount = outstandingAmount;
		this.createdAt = createdAt;
		this.createdBy = createdBy;
		this.updatedAt = updatedAt;
		this.updatedBy = updatedBy;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public LoanApplicationsResponseDTO getApplication() {
		return application;
	}
	public void setApplication(LoanApplicationsResponseDTO application) {
		this.application = application;
	}
	public String getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(String transactionId) {
		transactionId = transactionId;
	}
	public DisbursedLoanStatus getLoanStatus() {
		return loanStatus;
	}
	public void setLoanStatus(DisbursedLoanStatus loanStatus) {
		this.loanStatus = loanStatus;
	}
	
	public String getDueDate() {
		return dueDate;
	}
	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}
	public String getGraceDueDate() {
		return graceDueDate;
	}
	public void setGraceDueDate(String graceDueDate) {
		this.graceDueDate = graceDueDate;
	}
	public Long getOutstandingAmount() {
		return outstandingAmount;
	}
	public void setOutstandingAmount(Long outstandingAmount) {
		this.outstandingAmount = outstandingAmount;
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
	
	
}
