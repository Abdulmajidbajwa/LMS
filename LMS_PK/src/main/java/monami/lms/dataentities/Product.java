package monami.lms.dataentities;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
public class Product implements Serializable,Comparable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	private String productName;
	private String productCatagory;
	
	@OneToMany(fetch = FetchType.EAGER)
    private Set<ProductSpecification> productSpecification = new HashSet<>();
	
	@ManyToMany(fetch = FetchType.EAGER)
    private Set<KYCQuestion> questions = new HashSet<>();
	
	private String termsAndConditionFilePath;
	
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
	
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getProductCatagory() {
		return productCatagory;
	}
	public void setProductCatagory(String productCatagory) {
		this.productCatagory = productCatagory;
	}
	public Set<KYCQuestion> getQuestions() {
		return questions;
	}
	public void setQuestions(Set<KYCQuestion> questions) {
		this.questions = questions;
	}
	public Set<ProductSpecification> getProductSpecification() {
		return productSpecification;
	}
	public void setProductSpecification(Set<ProductSpecification> productSpecification) {
		this.productSpecification = productSpecification;
	}
	
	public boolean doesContain(ProductSpecification toCheck) {
		if(productSpecification!=null) {
			for(ProductSpecification i:productSpecification) {
				if(i.getId()==toCheck.getId()) {
					return true;
				}
			}
			
		}
		
		return false;
	
	}
	
	public String getTermsAndConditionFilePath() {
		return termsAndConditionFilePath;
	}
	public void setTermsAndConditionFilePath(String termsAndConditionFilePath) {
		this.termsAndConditionFilePath = termsAndConditionFilePath;
	}
	public Date getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
	public Date getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
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
		return Integer.valueOf(this.getId()).compareTo(((Product)o).getId());
	}
}
