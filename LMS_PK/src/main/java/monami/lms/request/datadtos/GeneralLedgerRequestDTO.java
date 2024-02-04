package monami.lms.request.datadtos;

public class GeneralLedgerRequestDTO{
	
	private int idGl;
	private String glDescription;
	private int loanId;
	private long amount;
	private boolean isDebitTransaction;
	
	public GeneralLedgerRequestDTO() {
		super();
	}
	
	public GeneralLedgerRequestDTO(int idGl, String glDescription,
			int loanId, long amount,
			boolean isDebitTransaction) {
		super();
		this.idGl = idGl;
		this.glDescription = glDescription;
		this.loanId = loanId;
		this.amount = amount;
		this.isDebitTransaction = isDebitTransaction;
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
	public int getLoanId() {
		return loanId;
	}
	public void setLoanId(int loanId) {
		this.loanId = loanId;
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
}
