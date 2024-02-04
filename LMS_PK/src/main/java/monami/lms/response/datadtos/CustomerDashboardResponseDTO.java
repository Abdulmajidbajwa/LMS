package monami.lms.response.datadtos;

import java.util.List;

public class CustomerDashboardResponseDTO{

	private List<PreferenceResponseDTO> preferenceDataForApp;
	private LoanApplicationIncompleteProcessResponseDTO loanApplicationIncompleteProcessResponseDTO;
	private LoanSummaryResponseDTO loanSummaryDTO;
	private List<DisbursedLoanResponseDTO> disbursedDTOList;
	
	public List<PreferenceResponseDTO> getPreferenceDataForApp() {
		return preferenceDataForApp;
	}
	public void setPreferenceDataForApp(
			List<PreferenceResponseDTO> preferenceDataForApp) {
		this.preferenceDataForApp = preferenceDataForApp;
	}
	public LoanApplicationIncompleteProcessResponseDTO getLoanApplicationIncompleteProcessResponseDTO() {
		return loanApplicationIncompleteProcessResponseDTO;
	}
	public void setLoanApplicationIncompleteProcessResponseDTO(LoanApplicationIncompleteProcessResponseDTO loanApplicationIncompleteProcessResponseDTO) {
		this.loanApplicationIncompleteProcessResponseDTO = loanApplicationIncompleteProcessResponseDTO;
	}
	public LoanSummaryResponseDTO getLoanSummaryDTO() {
		return loanSummaryDTO;
	}
	public void setLoanSummaryDTO(LoanSummaryResponseDTO loanSummaryDTO) {
		this.loanSummaryDTO = loanSummaryDTO;
	}
	public List<DisbursedLoanResponseDTO> getDisbursedDTOList() {
		return disbursedDTOList;
	}
	public void setDisbursedDTOList(List<DisbursedLoanResponseDTO> disbursedDTOList) {
		this.disbursedDTOList = disbursedDTOList;
	}
}
