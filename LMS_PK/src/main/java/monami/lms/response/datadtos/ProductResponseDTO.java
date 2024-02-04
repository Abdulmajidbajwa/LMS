package monami.lms.response.datadtos;

import java.util.HashSet;
import java.util.Set;

public class ProductResponseDTO  {
	
	private long productId;
	private String productName;
	private String productCatagory;
	private String termsAndConditionFilePath;
	
	private Set<ProductSpecificationResponseDTO> productSpecification = new HashSet<>();
	private Set<KYCQuestionResponseDTO> questions = new HashSet<>();
	
	private ApplicationUserResponseDTO createdBy;
	private String createdAt;
	private ApplicationUserResponseDTO updatedBy;
	private String updatedAt;
	
	
	public ProductResponseDTO() {
		super();
	}

	public ProductResponseDTO(long productId,String productName, String productCatagory) {
		super();
		this.productId=productId;
		this.productName = productName;
		this.productCatagory = productCatagory;
		this.productSpecification = new HashSet<>();
		this.questions = new HashSet<>();
	}
	
	public ProductResponseDTO(long productId,String productName, String productCatagory,String termsAndConditionFilePath) {
		super();
		this.productId=productId;
		this.productName = productName;
		this.productCatagory = productCatagory;
		this.termsAndConditionFilePath=termsAndConditionFilePath;
		this.productSpecification = new HashSet<>();
		this.questions = new HashSet<>();
	}

	public ProductResponseDTO(long productId,String productName, String productCatagory, Set<ProductSpecificationResponseDTO> productSpecification,
			Set<KYCQuestionResponseDTO> questions) {
		super();
		this.productId=productId;
		this.productName = productName;
		this.productCatagory = productCatagory;
		this.productSpecification = productSpecification;
		this.questions = questions;
	}
	
	public void addKYCQuestionDTO(int questionId, String question, String questionCategory, String  answerType){
		
		questions.add(new KYCQuestionResponseDTO(questionId,question,questionCategory,answerType));
	}
	
	public void addKYCQuestionWithAnswerDTO(int questionId, String question, String questionCategory, String  answerType,String answer){
		
		questions.add(new KYCQuestionResponseDTO(questionId,question,questionCategory,answerType,answer));
	}
	
	public void addProductSpecificationDTO(int id, String name,String value){
		
		productSpecification.add(new ProductSpecificationResponseDTO(id,name,value));
	}
	
	

	public long getProductId() {
		return productId;
	}

	public void setProductId(long productId) {
		this.productId = productId;
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

	public String getTermsAndConditionFilePath() {
		return termsAndConditionFilePath;
	}

	public void setTermsAndConditionFilePath(String termsAndConditionFilePath) {
		this.termsAndConditionFilePath = termsAndConditionFilePath;
	}

	public Set<ProductSpecificationResponseDTO> getProductSpecification() {
		return productSpecification;
	}

	public void setProductSpecification(Set<ProductSpecificationResponseDTO> productSpecification) {
		this.productSpecification = productSpecification;
	}

	public Set<KYCQuestionResponseDTO> getQuestions() {
		return questions;
	}

	public void setQuestions(Set<KYCQuestionResponseDTO> questions) {
		this.questions = questions;
	}

	public ApplicationUserResponseDTO getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(ApplicationUserResponseDTO createdBy) {
		this.createdBy = createdBy;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public ApplicationUserResponseDTO getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(ApplicationUserResponseDTO updatedBy) {
		this.updatedBy = updatedBy;
	}

	public String getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(String updatedAt) {
		this.updatedAt = updatedAt;
	}
	
}
