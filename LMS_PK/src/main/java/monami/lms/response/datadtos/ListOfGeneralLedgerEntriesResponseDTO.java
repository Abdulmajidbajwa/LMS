package monami.lms.response.datadtos;

import java.util.List;

import monami.lms.responceentities.BasicResponce;

public class ListOfGeneralLedgerEntriesResponseDTO  extends BasicResponce{
	List<GeneralLedgerResponseDTO> data;
	private Long totalPayableAmount;
	private Long totalRepaidAmount;
	private Long outstandingBalance;
	
	
	public ListOfGeneralLedgerEntriesResponseDTO(List<GeneralLedgerResponseDTO> data) {
		super(true);
		this.data = data;
	}
	
	public ListOfGeneralLedgerEntriesResponseDTO(List<GeneralLedgerResponseDTO> data ,Long totalRepaymentAmount) {
		super(true);
		this.data = data;
		this.totalRepaidAmount=totalRepaymentAmount;
	}
	
	public ListOfGeneralLedgerEntriesResponseDTO(List<GeneralLedgerResponseDTO> data,Long totalPayableAmount ,Long totalRepaymentAmount, Long outstandingBalance) {
		super(true);
		this.data = data;
		this.totalPayableAmount=totalPayableAmount;
		this.totalRepaidAmount=totalRepaymentAmount;
		this.outstandingBalance=outstandingBalance;
	}
	public List<GeneralLedgerResponseDTO> getData() {
		return data;
	}
	public void setData(List<GeneralLedgerResponseDTO> data) {
		this.data = data;
	}
	public Long getTotalPayableAmount() {
		return totalPayableAmount;
	}
	public void setTotalPayableAmount(Long totalPayableAmount) {
		this.totalPayableAmount = totalPayableAmount;
	}
	public Long getTotalRepaidAmount() {
		return totalRepaidAmount;
	}
	public void setTotalRepaidAmount(Long totalRepaidAmount) {
		this.totalRepaidAmount = totalRepaidAmount;
	}
	public Long getOutstandingBalance() {
		return outstandingBalance;
	}
	public void setOutstandingBalance(Long outstandingBalance) {
		this.outstandingBalance = outstandingBalance;
	}
}
