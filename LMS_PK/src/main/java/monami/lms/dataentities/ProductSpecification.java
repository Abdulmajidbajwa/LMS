package monami.lms.dataentities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.CreationTimestamp;



@Entity
public class ProductSpecification implements Serializable,Comparable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;	
	@OneToOne	
	private ProductAssumption productSpecificationAssumption;
	private String assumptionValue;
	
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

	public ProductAssumption getProductSpecificationAssumption() {
		return productSpecificationAssumption;
	}

	public void setProductSpecificationAssumption(ProductAssumption productSpecificationAssumption) {
		this.productSpecificationAssumption = productSpecificationAssumption;
	}

	public String getAssumptionValue() {
		return assumptionValue;
	}

	public void setAssumptionValue(String assumptionValue) {
		this.assumptionValue = assumptionValue;
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
	
	@Override
	public int compareTo(Object o) {
		// TODO Auto-generated method stub
		return Integer.valueOf(this.getId()).compareTo(((ProductSpecification)o).getId());
	}
	
}
