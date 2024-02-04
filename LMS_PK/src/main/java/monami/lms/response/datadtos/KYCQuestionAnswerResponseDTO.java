package monami.lms.response.datadtos;


public class KYCQuestionAnswerResponseDTO implements Comparable {
	
	int questionId;
	private String questionToAsk;
	private String category;
	private String answerType;
	private String listOfPossibleAnswers;
	private String answer;
	private boolean mandatoryStatus;
	private int expiryInDays;
	
	
	public KYCQuestionAnswerResponseDTO() {
		super();
	}

	public KYCQuestionAnswerResponseDTO(int questionId, String answer) {
		super();
		this.questionId = questionId;
		this.answer = answer;
	}

	public int getQuestionId() {
		return questionId;
	}

	public void setQuestionId(int questionId) {
		this.questionId = questionId;
	}

	public String getQuestionToAsk() {
		return questionToAsk;
	}

	public void setQuestionToAsk(String questionToAsk) {
		this.questionToAsk = questionToAsk;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
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

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public boolean isMandatoryStatus() {
		return mandatoryStatus;
	}

	public void setMandatoryStatus(boolean mandatoryStatus) {
		this.mandatoryStatus = mandatoryStatus;
	}

	public int getExpiryInDays() {
		return expiryInDays;
	}

	public void setExpiryInDays(int expiryInDays) {
		this.expiryInDays = expiryInDays;
	}

	@Override
	public int compareTo(Object o) {
		// TODO Auto-generated method stub
		return Integer.valueOf(this.getQuestionId()).compareTo(
				((KYCQuestionAnswerResponseDTO) o).getQuestionId());
	}
	
}
