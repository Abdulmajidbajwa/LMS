package monami.lms.dataentities;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
@Entity
public class KYCQuestion implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	private String catagory;
	private int expiryInDays;
	private String answerType;
	private String listOfPossibleAnswers;
	
	private String questionToAsk;
	
	private String commaSeprateListOfPossibleAnswers;
	
	private boolean mandatoryStatus;
	
	@OneToOne
	private ApplicationUsers createdBy;
	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdAt;

	@OneToOne
	private ApplicationUsers updatedBy;
	@UpdateTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	private Date updateAt;
	
	
	public String getCatagory() {
		return catagory;
	}
	public void setCatagory(String catagory) {
		this.catagory = catagory;
	}
	public int getExpiryInDays() {
		return expiryInDays;
	}
	public void setExpiryInDays(int expiryInDays) {
		this.expiryInDays = expiryInDays;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	public String getAnswerType() {
		return answerType;
	}
	public void setAnswerType(String answerType) {
		this.answerType = answerType;
	}
	
	public String getListOfPossibleAnswers() {
		return listOfPossibleAnswers;
	}
	public void setListOfPossibleAnswers(String listOfPossibleAnswers) {
		this.listOfPossibleAnswers = listOfPossibleAnswers;
	}
	public String getQuestionToAsk() {
		return questionToAsk;
	}
	public void setQuestionToAsk(String questionToAsk) {
		this.questionToAsk = questionToAsk;
	}
	public boolean isMandatoryStatus() {
		return mandatoryStatus;
	}
	public void setMandatoryStatus(boolean mandatoryStatus) {
		this.mandatoryStatus = mandatoryStatus;
	}
	public String getCommaSeprateListOfPossibleAnswers() {
		return commaSeprateListOfPossibleAnswers;
	}
	public void setCommaSeprateListOfPossibleAnswers(
			String commaSeprateListOfPossibleAnswers) {
		this.commaSeprateListOfPossibleAnswers = commaSeprateListOfPossibleAnswers;
	}
	public Date getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
	public Date getUpdateAt() {
		return updateAt;
	}
	public void setUpdateAt(Date updateAt) {
		this.updateAt = updateAt;
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

}
