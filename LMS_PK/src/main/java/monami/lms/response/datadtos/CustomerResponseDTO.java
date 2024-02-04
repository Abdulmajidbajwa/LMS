package monami.lms.response.datadtos;

import monami.lms.utilities.CustomerStatus;

public class CustomerResponseDTO {
	
	private int id;
	private String cellNo;
	private String fullName;
	private long cnic;
	private long balance;
	
	private String pinCode;
	private CustomerStatus status;
	private String fcmToken;
	
	private String idCardFrontPath;
	private String idCardBackPath;
	private String idCardWithFacePath;
	
	private boolean oneLoadStatus;
	
	private String createdAt;
	private ApplicationUserResponseDTO createdBy;
	private String updatedAt;
	private ApplicationUserResponseDTO updatedBy;
	
	
	public CustomerResponseDTO() {
		super();
	}

	public CustomerResponseDTO(int id, String cellNo, String fullName,long cnic) {
		super();
		this.id = id;
		this.cellNo = cellNo;
		this.fullName = fullName;
		this.cnic=cnic;
	}
	
	public CustomerResponseDTO(int id, String cellNo, String fullName,long cnic,long balance) {
		super();
		this.id = id;
		this.cellNo = cellNo;
		this.fullName = fullName;
		this.cnic=cnic;
		this.balance = balance;
	}
	
	public CustomerResponseDTO(int id, String cellNo, String fullName,long cnic, CustomerStatus status, String pinCode) {
		super();
		this.id = id;
		this.cellNo = cellNo;
		this.fullName = fullName;
		this.cnic=cnic;
		this.status=status;
		this.pinCode=pinCode;
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

	public long getBalance() {
		return balance;
	}


	public void setBalance(long balance) {
		this.balance = balance;
	}

	public boolean isOneLoadStatus() {
		return oneLoadStatus;
	}

	public void setOneLoadStatus(boolean oneLoadStatus) {
		this.oneLoadStatus = oneLoadStatus;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getCellNo() {
		return cellNo;
	}
	public void setCellNo(String cellNo) {
		this.cellNo = cellNo;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public long getCnic() {
		return cnic;
	}

	public void setCnic(long cnic) {
		this.cnic = cnic;
	}

	public String getPinCode() {
		return pinCode;
	}

	public void setPinCode(String pinCode) {
		this.pinCode = pinCode;
	}

	public CustomerStatus getStatus() {
		return status;
	}

	public void setStatus(CustomerStatus status) {
		this.status = status;
	}
	public String getFcmToken() {
		return fcmToken;
	}

	public void setFcmToken(String fcmToken) {
		this.fcmToken = fcmToken;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public ApplicationUserResponseDTO getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(ApplicationUserResponseDTO createdBy) {
		this.createdBy = createdBy;
	}

	public String getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(String updatedAt) {
		this.updatedAt = updatedAt;
	}

	public ApplicationUserResponseDTO getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(ApplicationUserResponseDTO updatedBy) {
		this.updatedBy = updatedBy;
	}
	
	
	

}
