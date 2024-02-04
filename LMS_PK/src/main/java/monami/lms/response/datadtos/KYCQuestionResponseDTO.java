package monami.lms.response.datadtos;

public class KYCQuestionResponseDTO {
	
	int questionId;
	String question;
	String questionCategory;
	String answerType;	
	String answer;
	
	private int expiryInDays;
	private String listOfPossibleAnswers;
	private boolean mandatoryStatus;
	
	private ApplicationUserResponseDTO createdBy;
	private String createdAt;
	private ApplicationUserResponseDTO updatedBy;
	private String updatedAt;
	
	
	public KYCQuestionResponseDTO() {
		super();
	}

	public KYCQuestionResponseDTO(int questionId, String question, String questionCategory, String  answerType) {
		super();
		this.questionId = questionId;
		this.question = question;
		this.questionCategory=questionCategory;
		this.answerType = answerType;
	}

	public KYCQuestionResponseDTO(int questionId, String question,String questionCategory, String  answerType, String answer) {
		super();
		this.questionId = questionId;
		this.question = question;
		this.questionCategory=questionCategory;
		this.answerType = answerType;
		this.answer = answer;
	}





	public int getQuestionId() {
		return questionId;
	}





	public void setQuestionId(int questionId) {
		this.questionId = questionId;
	}





	public String getQuestion() {
		return question;
	}





	public void setQuestion(String question) {
		this.question = question;
	}





	public String getQuestionCategory() {
		return questionCategory;
	}





	public void setQuestionCategory(String questionCategory) {
		this.questionCategory = questionCategory;
	}





	public String getAnswerType() {
		return answerType;
	}





	public void setAnswerType(String answerType) {
		this.answerType = answerType;
	}





	public String getAnswer() {
		return answer;
	}





	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public int getExpiryInDays() {
		return expiryInDays;
	}

	public void setExpiryInDays(int expiryInDays) {
		this.expiryInDays = expiryInDays;
	}

	public String getListOfPossibleAnswers() {
		return listOfPossibleAnswers;
	}

	public void setListOfPossibleAnswers(String listOfPossibleAnswers) {
		this.listOfPossibleAnswers = listOfPossibleAnswers;
	}

	public boolean isMandatoryStatus() {
		return mandatoryStatus;
	}

	public void setMandatoryStatus(boolean mandatoryStatus) {
		this.mandatoryStatus = mandatoryStatus;
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
