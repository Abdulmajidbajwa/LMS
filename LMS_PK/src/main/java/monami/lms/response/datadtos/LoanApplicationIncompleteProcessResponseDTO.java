package monami.lms.response.datadtos;

public class LoanApplicationIncompleteProcessResponseDTO  {
	
	private boolean applicationInProcess;
	private Integer productId;
	private String message;

	public boolean isApplicationInProcess() {
		return applicationInProcess;
	}
	public void setApplicationInProcess(boolean applicationInProcess) {
		this.applicationInProcess = applicationInProcess;
	}
	public Integer getProductId() {
		return productId;
	}
	public void setProductId(Integer productId) {
		this.productId = productId;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
}
