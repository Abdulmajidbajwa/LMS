package monami.lms.response.datadtos;

public class CustomerLoanSummaryResponseDTO{

	LoanApplicationSummaryResponseDTO loanApplicationSummary;
	DisbursedLoanSummaryResponseDTO disbursedLoanSummary;
	public LoanApplicationSummaryResponseDTO getLoanApplicationSummary() {
		return loanApplicationSummary;
	}
	public void setLoanApplicationSummary(
			LoanApplicationSummaryResponseDTO loanApplicationSummary) {
		this.loanApplicationSummary = loanApplicationSummary;
	}
	public DisbursedLoanSummaryResponseDTO getDisbursedLoanSummary() {
		return disbursedLoanSummary;
	}
	public void setDisbursedLoanSummary(
			DisbursedLoanSummaryResponseDTO disbursedLoanSummary) {
		this.disbursedLoanSummary = disbursedLoanSummary;
	}
}