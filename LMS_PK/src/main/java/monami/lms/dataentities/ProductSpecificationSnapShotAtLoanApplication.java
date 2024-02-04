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
@Entity
public class ProductSpecificationSnapShotAtLoanApplication  {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;	
	@OneToOne	
	private ProductAssumption productspecificationassumption;
	@OneToOne	
	private LoanApplication loanapplication;
	private String assumptionvalue;
	
	@OneToOne
	private ApplicationUsers createdBy;
	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdAt;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public ProductAssumption getProductspecificationassumption() {
		return productspecificationassumption;
	}

	public void setProductspecificationassumption(ProductAssumption productspecificationassumption) {
		this.productspecificationassumption = productspecificationassumption;
	}

	public LoanApplication getLoanapplication() {
		return loanapplication;
	}

	public void setLoanapplication(LoanApplication loanapplication) {
		this.loanapplication = loanapplication;
	}

	public String getAssumptionvalue() {
		return assumptionvalue;
	}

	public void setAssumptionvalue(String assumptionvalue) {
		this.assumptionvalue = assumptionvalue;
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
}
