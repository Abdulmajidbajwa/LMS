package monami.lms.dataentities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "LoanApplicationSelectedKYCQuestionsForReview",
uniqueConstraints = { @UniqueConstraint( columnNames = { "question_id", "loanapplication_id", "reviewSequenceId" } ) } )
public class LoanApplicationSelectedKYCQuestionsForReview {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	@OneToOne
	private KYCQuestion question;
	@OneToOne	
	private LoanApplication loanapplication;

	private String reviewComments;
	private int reviewSequenceId;

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public KYCQuestion getQuestion() {
		return question;
	}
	public void setQuestion(KYCQuestion question) {
		this.question = question;
	}
	public LoanApplication getLoanapplication() {
		return loanapplication;
	}
	public void setLoanapplication(LoanApplication loanapplication) {
		this.loanapplication = loanapplication;
	}
	public int getReviewSequenceId() {
		return reviewSequenceId;
	}
	public void setReviewSequenceId(int reviewSequenceId) {
		this.reviewSequenceId = reviewSequenceId;
	}
	public String getReviewComments() {
		return reviewComments;
	}
	public void setReviewComments(String reviewComments) {
		this.reviewComments = reviewComments;
	}
}
