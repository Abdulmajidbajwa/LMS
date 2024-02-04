package monami.lms.response.datadtos;

public class LoanSummaryResponseDTO {

	private String dueDate;
	private int daysRemainingTillDueDate;
	
	private long totalDueAmount;	//Total Amount
	private long totalPaidAmount;	//Total Paid Amount
	private long outstandingBalance;	//Total Payable Amount
	private long lastPaymentAmount; //Last Payment Amount
	
	
	public String getDueDate() {
		return dueDate;
	}
	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}
	public int getDaysRemainingTillDueDate() {
		return daysRemainingTillDueDate;
	}
	public void setDaysRemainingTillDueDate(int daysRemainingTillDueDate) {
		this.daysRemainingTillDueDate = daysRemainingTillDueDate;
	}
	public long getTotalDueAmount() {
		return totalDueAmount;
	}
	public void setTotalDueAmount(long totalDueAmount) {
		this.totalDueAmount = totalDueAmount;
	}
	public long getTotalPaidAmount() {
		return totalPaidAmount;
	}
	public void setTotalPaidAmount(long totalPaidAmount) {
		this.totalPaidAmount = totalPaidAmount;
	}
	public long getOutstandingBalance() {
		return outstandingBalance;
	}
	public void setOutstandingBalance(long outstandingBalance) {
		this.outstandingBalance = outstandingBalance;
	}
	public long getLastPaymentAmount() {
		return lastPaymentAmount;
	}
	public void setLastPaymentAmount(long lastPaymentAmount) {
		this.lastPaymentAmount = lastPaymentAmount;
	}
	
	
}
