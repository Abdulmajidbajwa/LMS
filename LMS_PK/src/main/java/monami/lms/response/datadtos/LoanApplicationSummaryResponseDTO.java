package monami.lms.response.datadtos;

public class LoanApplicationSummaryResponseDTO{

	private Integer numberOfLoanApplications;
	private Integer numberOfPendingForApprovalLoanApplications;
	private Integer numberOfApprovedLoanApplications;
	private Integer numberOfRejectedLoanApplications;
	private Integer numberOfAcceptedLoanApplications;
	private Integer numberOfDeclinedLoanApplications;
	private Integer numberOfDisbursedLoanApplications;
	private Integer numberOfCompletedLoanApplications;
	
	public Integer getNumberOfLoanApplications() {
		return numberOfLoanApplications;
	}
	public void setNumberOfLoanApplications(Integer numberOfLoanApplications) {
		this.numberOfLoanApplications = numberOfLoanApplications;
	}
	public Integer getNumberOfPendingForApprovalLoanApplications() {
		return numberOfPendingForApprovalLoanApplications;
	}
	public void setNumberOfPendingForApprovalLoanApplications(
			Integer numberOfPendingForApprovalLoanApplications) {
		this.numberOfPendingForApprovalLoanApplications = numberOfPendingForApprovalLoanApplications;
	}
	public Integer getNumberOfApprovedLoanApplications() {
		return numberOfApprovedLoanApplications;
	}
	public void setNumberOfApprovedLoanApplications(
			Integer numberOfApprovedLoanApplications) {
		this.numberOfApprovedLoanApplications = numberOfApprovedLoanApplications;
	}
	public Integer getNumberOfRejectedLoanApplications() {
		return numberOfRejectedLoanApplications;
	}
	public void setNumberOfRejectedLoanApplications(
			Integer numberOfRejectedLoanApplications) {
		this.numberOfRejectedLoanApplications = numberOfRejectedLoanApplications;
	}
	public Integer getNumberOfAcceptedLoanApplications() {
		return numberOfAcceptedLoanApplications;
	}
	public void setNumberOfAcceptedLoanApplications(
			Integer numberOfAcceptedLoanApplications) {
		this.numberOfAcceptedLoanApplications = numberOfAcceptedLoanApplications;
	}
	public Integer getNumberOfDeclinedLoanApplications() {
		return numberOfDeclinedLoanApplications;
	}
	public void setNumberOfDeclinedLoanApplications(
			Integer numberOfDeclinedLoanApplications) {
		this.numberOfDeclinedLoanApplications = numberOfDeclinedLoanApplications;
	}
	public Integer getNumberOfDisbursedLoanApplications() {
		return numberOfDisbursedLoanApplications;
	}
	public void setNumberOfDisbursedLoanApplications(
			Integer numberOfDisbursedLoanApplications) {
		this.numberOfDisbursedLoanApplications = numberOfDisbursedLoanApplications;
	}
	public Integer getNumberOfCompletedLoanApplications() {
		return numberOfCompletedLoanApplications;
	}
	public void setNumberOfCompletedLoanApplications(
			Integer numberOfCompletedLoanApplications) {
		this.numberOfCompletedLoanApplications = numberOfCompletedLoanApplications;
	}
}