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
import org.hibernate.annotations.GeneratorType;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
public class GlForJazzCashWalletAccount{

	@Id 
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int idGl;
	private String glDescription;	
	@OneToOne
	private DisbursedLoans disbursedLoans;	
	private String txnId;
	private Long amountDebit;
	private Long amountCredit;
	
	@OneToOne
	private ApplicationUsers createdBy;
	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdAt;

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

	public DisbursedLoans getDisbursedLoans() {
		return disbursedLoans;
	}

	public void setDisbursedLoans(DisbursedLoans disbursedLoans) {
		this.disbursedLoans = disbursedLoans;
	}

	public String getTxnId() {
		return txnId;
	}

	public void setTxnId(String txnId) {
		this.txnId = txnId;
	}

	public Long getAmountDebit() {
		return amountDebit;
	}

	public void setAmountDebit(Long amountDebit) {
		this.amountDebit = amountDebit;
	}

	public Long getAmountCredit() {
		return amountCredit;
	}

	public void setAmountCredit(Long amountCredit) {
		this.amountCredit = amountCredit;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public ApplicationUsers getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(ApplicationUsers createdBy) {
		this.createdBy = createdBy;
	}

}
