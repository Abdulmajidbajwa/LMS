package monami.lms.response.datadtos;

import monami.lms.responceentities.BasicResponce;

public class GeneralLedgerResponseDTO extends BasicResponce{
	
	private int idGl;
	private String glDescription;
	/*private long loanId;*/
	
	private DisbursedLoanResponseDTO disbursedLoanDTO;
	
	private String txnId;
	private long amount;
	private boolean isDebitTransaction;
	
	private String createdAt;
	private ApplicationUserResponseDTO createdBy;
	
	
	public GeneralLedgerResponseDTO() {
		super();
	}
	
	public GeneralLedgerResponseDTO(int idGl, String glDescription,
			DisbursedLoanResponseDTO disbursedLoanDTO, String txnId, long amount,
			boolean isDebitTransaction, String createdAt, ApplicationUserResponseDTO createdBy) {
		super();
		this.idGl = idGl;
		this.glDescription = glDescription;
		this.disbursedLoanDTO = disbursedLoanDTO;
		this.txnId = txnId;
		this.amount = amount;
		this.isDebitTransaction = isDebitTransaction;
		this.createdAt = createdAt;
		this.createdBy = createdBy;
	}
	public int getIdGl() {
		return idGl;
	}
	public void setIdGl(int idGl) {
		this.idGl = idGl;
	}
	public String getGlDescription() {
		return glDescription;
	}
	public void setGlDescription(String glDescription) {
		this.glDescription = glDescription;
	}
	/*public long getLoanId() {
		return loanId;
	}
	public void setLoanId(long loanId) {
		this.loanId = loanId;
	}*/
	
	public DisbursedLoanResponseDTO getDisbursedLoanDTO() {
		return disbursedLoanDTO;
	}
	public void setDisbursedLoanDTO(DisbursedLoanResponseDTO disbursedLoanDTO) {
		this.disbursedLoanDTO = disbursedLoanDTO;
	}
	public String getTxnId() {
		return txnId;
	}
	public void setTxnId(String txnId) {
		this.txnId = txnId;
	}
	public long getAmount() {
		return amount;
	}
	public void setAmount(long amount) {
		this.amount = amount;
	}
	public boolean isDebitTransaction() {
		return isDebitTransaction;
	}
	public void setDebitTransaction(boolean isDebitTransaction) {
		this.isDebitTransaction = isDebitTransaction;
	}
	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public ApplicationUserResponseDTO getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(ApplicationUserResponseDTO createdBy) {
		this.createdBy = createdBy;
	}
	
}
