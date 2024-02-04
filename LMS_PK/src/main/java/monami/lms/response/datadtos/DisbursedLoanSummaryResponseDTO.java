package monami.lms.response.datadtos;

public class DisbursedLoanSummaryResponseDTO{

	private Integer numberOfPaidDisbursedLoansInDueDate;
	private Integer numberOfPaidDisbursedLoansInGracePeriodDate;
	private Integer numberOfPaidDisbursedLoansAfterDueDate;
	
	public Integer getNumberOfPaidDisbursedLoansInDueDate() {
		return numberOfPaidDisbursedLoansInDueDate;
	}
	public void setNumberOfPaidDisbursedLoansInDueDate(
			Integer numberOfPaidDisbursedLoansInDueDate) {
		this.numberOfPaidDisbursedLoansInDueDate = numberOfPaidDisbursedLoansInDueDate;
	}
	public Integer getNumberOfPaidDisbursedLoansInGracePeriodDate() {
		return numberOfPaidDisbursedLoansInGracePeriodDate;
	}
	public void setNumberOfPaidDisbursedLoansInGracePeriodDate(
			Integer numberOfPaidDisbursedLoansInGracePeriodDate) {
		this.numberOfPaidDisbursedLoansInGracePeriodDate = numberOfPaidDisbursedLoansInGracePeriodDate;
	}
	public Integer getNumberOfPaidDisbursedLoansAfterDueDate() {
		return numberOfPaidDisbursedLoansAfterDueDate;
	}
	public void setNumberOfPaidDisbursedLoansAfterDueDate(
			Integer numberOfPaidDisbursedLoansAfterDueDate) {
		this.numberOfPaidDisbursedLoansAfterDueDate = numberOfPaidDisbursedLoansAfterDueDate;
	}
}