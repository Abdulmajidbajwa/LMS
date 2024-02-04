package monami.lms.dataentities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import monami.lms.utilities.LoanApplicationStatus;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@SuppressWarnings("rawtypes")
@Entity
public class LoanApplication  implements Comparable {
	@SuppressWarnings("unused")
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@OneToOne
	private Product product;

	@OneToOne
	private Customer customer;

	private int walletTypeId;
	private int requestedamount;
	private boolean termsAndConditionAcceptanceStatus;
	
	@Enumerated (value = EnumType.STRING)
	private LoanApplicationStatus status;
	
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

	@OneToOne
	private ApplicationUsers loanApprover;

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdateAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public int getWalletTypeId() {
		return walletTypeId;
	}

	public void setWalletTypeId(int walletTypeId) {
		this.walletTypeId = walletTypeId;
	}

	public int getRequestedamount() {
		return requestedamount;
	}

	public void setRequestedamount(int requestedamount) {
		this.requestedamount = requestedamount;
	}

	public boolean isTermsAndConditionAcceptanceStatus() {
		return termsAndConditionAcceptanceStatus;
	}

	public void setTermsAndConditionAcceptanceStatus(
			boolean termsAndConditionAcceptanceStatus) {
		this.termsAndConditionAcceptanceStatus = termsAndConditionAcceptanceStatus;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	public LoanApplicationStatus getStatus() {
		return status;
	}

	public void setStatus(LoanApplicationStatus status) {
		this.status = status;
	}

	public ApplicationUsers getLoanApprover() {
		return loanApprover;
	}

	public void setLoanApprover(ApplicationUsers loanApprover) {
		this.loanApprover = loanApprover;
	}
	
	public ApplicationUsers getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(ApplicationUsers createdBy) {
		this.createdBy = createdBy;
	}

	public ApplicationUsers getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(ApplicationUsers updatedBy) {
		this.updatedBy = updatedBy;
	}

	@Override
	public int compareTo(Object o) {
		// TODO Auto-generated method stub
		return Integer.valueOf(this.getId()).compareTo(
				((LoanApplication) o).getId());
	}

}
