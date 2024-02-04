package monami.lms.dataentities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import monami.lms.utilities.CustomerStatus;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
public class Customer implements Serializable,Comparable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id; //MONAMI ID
	@Column(unique = true)
	private long cellNo;
	@Column(unique = true)
	private long pakistaniCNIC;
	private long partnerId; // used to identify the source of the customer Careem,One load
	private String otpStatus;
	private String sessionToken;
	private String pinCode;
	private CustomerStatus status;
	private String statusReason; //Will used for suspended accounts // Suspended Accounts will need reason for suspension
	
	private String fullName;
	
	private Integer wrongPinCounter;
	
	private boolean oneLoadCustomerStatus;
	
	private String idCardFrontPath;
	private String idCardBackPath;
	private String idCardWithFacePath;
	
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
	
	private String fcmToken;
	
	
	
	public String getFcmToken() {
		return fcmToken;
	}
	public void setFcmToken(String fcmToken) {
		this.fcmToken = fcmToken;
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
	public String getIdCardFrontPath() {
		return idCardFrontPath;
	}
	public void setIdCardFrontPath(String idCardFrontPath) {
		this.idCardFrontPath = idCardFrontPath;
	}
	public String getIdCardBackPath() {
		return idCardBackPath;
	}
	public void setIdCardBackPath(String idCardBackPath) {
		this.idCardBackPath = idCardBackPath;
	}
	public String getIdCardWithFacePath() {
		return idCardWithFacePath;
	}
	public void setIdCardWithFacePath(String idCardWithFacePath) {
		this.idCardWithFacePath = idCardWithFacePath;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public boolean isOneLoadCustomerStatus() {
		return oneLoadCustomerStatus;
	}
	public void setOneLoadCustomerStatus(boolean oneLoadCustomerStatus) {
		this.oneLoadCustomerStatus = oneLoadCustomerStatus;
	}
	public String getSessionToken() {
		return sessionToken;
	}
	public void setSessionToken(String sessionToken) {
		this.sessionToken = sessionToken;
	}
	public String getPinCode() {
		return pinCode;
	}
	public void setPinCode(String pinCode) {
		this.pinCode = pinCode;
	}
	public String getOtpStatus() {
		return otpStatus;
	}
	public void setOtpStatus(String otpStatus) {
		this.otpStatus = otpStatus;
	}
	public CustomerStatus getStatus() {
		return status;
	}
	public void setStatus(CustomerStatus status) {
		this.status = status;
	}
	public String getStatusReason() {
		return statusReason;
	}
	public void setStatusReason(String statusReason) {
		this.statusReason = statusReason;
	}
	public Integer getWrongPinCounter() {
		return wrongPinCounter;
	}
	public void setWrongPinCounter(Integer wrongPinCounter) {
		this.wrongPinCounter = wrongPinCounter;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public long getCellNo() {
		return cellNo;
	}
	
	public String getCellNoInString() {
		if(String.valueOf(cellNo).startsWith("03")){
			return String.valueOf(cellNo);
		}else if(String.valueOf(cellNo).startsWith("3")){
			return String.valueOf(cellNo).replaceFirst("3", "03");
		}else{
			return String.valueOf(cellNo);
		}
	}
	public void setCellNo(long cellNo) {
		this.cellNo = cellNo;
	}
	public long getPakistaniCNIC() {
		return pakistaniCNIC;
	}
	public void setPakistaniCNIC(long pakistaniCNIC) {
		this.pakistaniCNIC = pakistaniCNIC;
	}
	public long getPartnerId() {
		return partnerId;
	}
	public void setPartnerId(long partnerId) {
		this.partnerId = partnerId;
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
		return Integer.valueOf(this.getId()).compareTo(((Customer)o).getId());
	}
	
	
}
