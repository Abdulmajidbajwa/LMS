package monami.lms.utilities;

public enum LoanApplicationStatus {
	
	PENDINGFORAPPROVAL,		//Customer Applied for Loan
	APPROVED,				//Loan Application Approved by Staff
	REJECTED,				//Loan Application Rejected by Staff
	DECLINED,				//Offer Decline by Customer
	ACCEPTED,				//(Accepted/ReadyToDisburse)Offer Accepted by Customer and Requested Loan Amount Provided by Customer
	DISBURSED,				//Loan Application Disbursed by Staff
	COMPLETED,				//Disbursed Amount Repaid by Customer
	PENDINGFORCORRECTION	//status updated by staff, so that customer can make Correction on its loan application
}
