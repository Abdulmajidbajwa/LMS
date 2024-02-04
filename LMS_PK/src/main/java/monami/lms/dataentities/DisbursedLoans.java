package monami.lms.dataentities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import monami.lms.utilities.DisbursedLoanStatus;


@Entity
public class DisbursedLoans {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	@OneToOne
	private LoanApplication application;
	private String TransactionId;
	private DisbursedLoanStatus loanStatus;
	private DisbursedLoanStatus existingStatusJustBeforeFullPayment; //Used to get Previous Status Before Making Full Payment 
	private Date dueDate;
	private Date graceDueDate;
	
	@OneToOne
	private ApplicationUsers createdBy;
	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdAt;

	@OneToOne
	private ApplicationUsers updatedBy;
	@UpdateTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	private Date updatedAt;
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public LoanApplication getApplication() {
		return application;
	}
	public void setApplication(LoanApplication application) {
		this.application = application;
	}
	public String getTransactionId() {
		return TransactionId;
	}
	public void setTransactionId(String transactionId) {
		TransactionId = transactionId;
	}
	public DisbursedLoanStatus getLoanStatus() {
		return loanStatus;
	}
	public void setLoanStatus(DisbursedLoanStatus loanStatus) {
		this.loanStatus = loanStatus;
	}
	public DisbursedLoanStatus getExistingStatusJustBeforeFullPayment() {
		return existingStatusJustBeforeFullPayment;
	}
	public void setExistingStatusJustBeforeFullPayment(DisbursedLoanStatus existingStatusJustBeforeFullPayment) {
		this.existingStatusJustBeforeFullPayment = existingStatusJustBeforeFullPayment;
	}
	public Date getDueDate() {
		return dueDate;
	}
	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}
	public Date getGraceDueDate() {
		return graceDueDate;
	}
	public void setGraceDueDate(Date graceDueDate) {
		this.graceDueDate = graceDueDate;
	}
	public ApplicationUsers getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(ApplicationUsers createdBy) {
		this.createdBy = createdBy;
	}
	public Date getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
	public ApplicationUsers getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(ApplicationUsers updatedBy) {
		this.updatedBy = updatedBy;
	}
	public Date getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}
	
}
