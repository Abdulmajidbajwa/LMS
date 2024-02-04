package monami.lms.datadaos;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import org.hibernate.criterion.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import monami.lms.dataentities.ApplicationUsers;
import monami.lms.dataentities.Customer;
import monami.lms.dataentities.CustomerUnStructuredData;
import monami.lms.dataentities.DisbursedLoans;
import monami.lms.dataentities.GlForFedLiabilityAccount;
import monami.lms.dataentities.GlForInternalLoanBook;
import monami.lms.dataentities.GlForLoanReceivableAccount;
import monami.lms.dataentities.GlForOneLoadWalletAccount;
import monami.lms.dataentities.GlForServiceFeeIncomeAccount;
import monami.lms.dataentities.KYCAnswer;
import monami.lms.dataentities.KYCAnswerSnapShotAtLoanApplication;
import monami.lms.dataentities.KYCQuestion;
import monami.lms.dataentities.LoanApplication;
import monami.lms.dataentities.LoanApplicationSelectedKYCQuestionsForReview;
import monami.lms.request.datadtos.KYCAnswerRequestDTO;
import monami.lms.request.datadtos.KYCQuestionRequestDTO;
import monami.lms.utilities.AES;
import monami.lms.utilities.Constants;
import monami.lms.utilities.CustomerStatus;
import monami.lms.utilities.LoanApplicationStatus;
import monami.lms.dataentities.Preferences;
import monami.lms.dataentities.Product;
import monami.lms.dataentities.ProductAssumption;
import monami.lms.dataentities.ProductSpecification;
import monami.lms.dataentities.ProductSpecificationSnapShotAtLoanApplication;
import monami.lms.utilities.DisbursedLoanStatus;


@Repository
@Scope("prototype")
public class LMSDAO {
	
	Logger LOG = LoggerFactory.getLogger(LMSDAO.class);
	
	@Autowired DAO obj;

	public boolean verifyOTP(long customerCellNo,String otp)  {		
		synchronized (DAO.class){ try {
			obj.startTask();	

			Customer objCustomer = (Customer) 	obj.getRowByAttribute(Customer.class, "cellNo", customerCellNo);	

			if(objCustomer==null){
				return false;
			}

			if(objCustomer.getOtpStatus().equals(otp)){
				return true;
			}		

		} catch (Exception e) {
			obj.catchException(e);
			return false;
		}finally{
			try {
				obj.endTask();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}}
		return false;		
	}
	
	public boolean verifyToken(long customerCellNo,String token)  {		
		synchronized (DAO.class){ try {
			obj.startTask();	

			Customer objCustomer = (Customer) 	obj.getRowByAttribute(Customer.class, "cellNo", customerCellNo);	

			if(objCustomer==null){
				return false;
			}

			if(objCustomer.getSessionToken().equals(token)){
				return true;
			}		

		} catch (Exception e) {
			obj.catchException(e);
			return false;
		}finally{
			try {
				obj.endTask();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}}
		return false;		
	}

	public Customer getCustomerAgainstCellNo(long customerCellNo) {
		synchronized (DAO.class){ try {
			obj.startTask();	

			Customer objCustomer = (Customer) 	obj.getRowByAttribute(Customer.class, "cellNo", customerCellNo);	

			return objCustomer;

		} catch (Exception e) {
			obj.catchException(e);

		}finally{
			try {
				obj.endTask();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}}
		return null;
	}

	public Customer getCustomerAgainstCnic(long customerCnic) {
		synchronized (DAO.class){ try {
			obj.startTask();	

			Customer objCustomer = (Customer) 	obj.getRowByAttribute(Customer.class, "pakistaniCNIC", customerCnic);	

			return objCustomer;

		} catch (Exception e) {
			obj.catchException(e);

		}finally{
			try {
				obj.endTask();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}}
		return null;
	}

	public String loginWithPinCodeForCustomer(long customerCellNo,String pinCode) throws Exception {		
		synchronized (DAO.class){ try {
			obj.startTask();	

			Customer objCustomer = (Customer) 	obj.getRowByAttribute(Customer.class, "cellNo", customerCellNo);	

			if(objCustomer==null){
				throw new Exception("Customer Not Found");
			}

			Preferences objPreferences= (Preferences) obj.getRowByAttribute(Preferences.class, "preferenceName", "secretKey");
			
			if(objPreferences==null){
				throw new Exception("Failed to Get Details. Unable to Process Login Request");
			}
			
			if(objCustomer!=null && objCustomer.getPinCode()!=null){
				
				if(objCustomer.getStatus()==CustomerStatus.ACTIVE){
					String decryptedPinCode= AES.decrypt(objCustomer.getPinCode(),objPreferences.getPreferenceValue());;
					if(decryptedPinCode!=null){
						if(decryptedPinCode.equals(pinCode)){
							UUID uuid = UUID.randomUUID();
							objCustomer.setSessionToken(uuid.toString());		

							obj.updateObject(objCustomer);
							return uuid.toString();
						} else {
							if(objCustomer.getWrongPinCounter()!=null){
								if(objCustomer.getWrongPinCounter()>=5){ //Right now wrong credential max limit is 5. To make it dynamically, Just save one preference value in preference table and get while login such as getting secret key from preference table  
									objCustomer.setWrongPinCounter(objCustomer.getWrongPinCounter()+1);
									objCustomer.setStatusReason("Multiple Wrong Credentials");
									objCustomer.setStatus(CustomerStatus.SUSPENDED);
									obj.updateObject(objCustomer);
									
									throw new Exception("Customer Suspended due to Multiple Wrong Login Attempts");
								}else{
									objCustomer.setWrongPinCounter(objCustomer.getWrongPinCounter()+1);
									obj.updateObject(objCustomer);
									throw new Exception("Wrong Credentials");
								}
								
							}else{
								objCustomer.setWrongPinCounter(1);
								obj.updateObject(objCustomer);
								throw new Exception("Wrong Credentials");
							}
						}
					}else{
						throw new Exception("Unable to Decrypt Credentials");
					}
				}else{
					throw new Exception("Customer is in Invalid State");
				}
			}else{
				throw new Exception("Customer Details Not Found");
			}
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}finally{
			obj.endTask();
		}}		
	}


	public int updatePinCodeForCustomer(long customerCellNo,String pinCode) throws Exception {		
		synchronized (DAO.class){ try {
			obj.startTask();	

			Customer objCustomer = (Customer) 	obj.getRowByAttribute(Customer.class, "cellNo", customerCellNo);	

			if(objCustomer==null){
				return -5;
			}

			objCustomer.setPinCode(pinCode);
			obj.updateObject(objCustomer);
			return 1;
		} catch (Exception e) {
			return obj.catchException(e);
		}finally{
			obj.endTask();
		}}		
	}
	
	public int updateFCMTokenForCustomer(long customerCellNo,String fcmToken) throws Exception {		
		synchronized (DAO.class){ try {
			obj.startTask();	

			Customer objCustomer = (Customer) 	obj.getRowByAttribute(Customer.class, "cellNo", customerCellNo);	

			if(objCustomer==null){
				return -5;
			}

			objCustomer.setFcmToken(fcmToken);
			obj.updateObject(objCustomer);
			return 1;
		} catch (Exception e) {
			return obj.catchException(e);
		}finally{
			obj.endTask();
		}}		
	}
	
	
	public boolean updateCustomerStatus(long customerCellNo,CustomerStatus customerStatus, String reason) throws Exception {		
		synchronized (DAO.class){ try {
			obj.startTask();	

			Customer objCustomer = (Customer) 	obj.getRowByAttribute(Customer.class, "cellNo", customerCellNo);	
			objCustomer.setStatus(customerStatus);
			objCustomer.setStatusReason(reason);
			
			if(customerStatus==CustomerStatus.ACTIVE){
				objCustomer.setWrongPinCounter(0);
			}
			
			obj.updateObject(objCustomer);
			
			if(obj.updateObject(objCustomer)){
				return true;
			}else{
				return false;
			}

		} catch (Exception e) {
			return false;
		}finally{
			obj.endTask();
		}}		
	}
	
	public boolean updateCustomerStatus(long customerCellNo,CustomerStatus customerStatus) throws Exception {		
		synchronized (DAO.class){ try {
			obj.startTask();	

			Customer objCustomer = (Customer) 	obj.getRowByAttribute(Customer.class, "cellNo", customerCellNo);	
			objCustomer.setStatus(customerStatus);
			
			
			if(customerStatus==CustomerStatus.ACTIVE){
				objCustomer.setWrongPinCounter(0);
			}
			
			obj.updateObject(objCustomer);
			
			if(obj.updateObject(objCustomer)){
				return true;
			}else{
				return false;
			}

		} catch (Exception e) {
			return false;
		}finally{
			obj.endTask();
		}}		
	}
	
	public boolean updateCustomerOTPAndStatus(long customerCellNo,String generatedOTP,CustomerStatus customerStatus) throws Exception {		
		synchronized (DAO.class){ try {
			obj.startTask();	

			Customer objCustomer = (Customer) 	obj.getRowByAttribute(Customer.class, "cellNo", customerCellNo);	
			objCustomer.setOtpStatus(generatedOTP);
			objCustomer.setStatus(customerStatus);
			obj.updateObject(objCustomer);
			
			if(obj.updateObject(objCustomer)){
				return true;
			}else{
				return false;
			}

		} catch (Exception e) {
			return false;
		}finally{
			obj.endTask();
		}}		
	}

	public int addProductSpecificationAssumption(String name,String dataType, ApplicationUsers loggedInUser) throws Exception {		
		synchronized (DAO.class){ try {
			obj.startTask();			
			ProductAssumption toSave=new ProductAssumption();
			toSave.setName(name);

			if(dataType!=null)
			toSave.setDataType(dataType.toUpperCase());
			toSave.setCreatedBy(loggedInUser);

			return obj.saveObject(toSave);
		} catch (Exception e) {
			return obj.catchException(e);
		}finally{
			obj.endTask();
		}}		
	}

	public int addNewKYCQuestion(String questionToAsk,String answerType, String listOfPossibleAnswers,
			String catagory,boolean mandatoryStatus, int expiryInDays, ApplicationUsers loggedInUser) throws Exception {		
		synchronized (DAO.class){ try {
			obj.startTask();			
			KYCQuestion toSave=new KYCQuestion();
			toSave.setQuestionToAsk(questionToAsk);

			if(answerType!=null)
				toSave.setAnswerType(answerType.toUpperCase());

			if(listOfPossibleAnswers!=null)
				toSave.setListOfPossibleAnswers(listOfPossibleAnswers);

			if(catagory!=null)
				toSave.setCatagory(catagory.toUpperCase());

			toSave.setMandatoryStatus(mandatoryStatus);
			toSave.setExpiryInDays(expiryInDays);
			toSave.setCreatedBy(loggedInUser);

			return obj.saveObject(toSave);
		} catch (Exception e) {
			return obj.catchException(e);
		}finally{
			obj.endTask();
		}}		
	}
	
	public int addCustomerUnStructuredData(Customer c,String startDate,String endDate,String jsonData) throws Exception {		
		synchronized (DAO.class){ try {
			obj.startTask();			
			CustomerUnStructuredData toSave=new CustomerUnStructuredData();
			toSave.setStartDate(startDate);
			toSave.setEndDate(endDate);
			toSave.setJsonData(jsonData);
			toSave.setCustomerID(c.getId());
			return obj.saveObject(toSave);
		} catch (Exception e) {
			return obj.catchException(e);
		}finally{
			obj.endTask();
		}}		
	}
	
	
	public List<CustomerUnStructuredData> getCustomerUnStructuredDataByCustomer(Customer c) throws Exception{
		synchronized (DAO.class){ try {
			obj.startTask();			
			
			List<CustomerUnStructuredData> objCustomerUnStructuredDataList = (List<CustomerUnStructuredData>) 	obj.getAllOnCriteria(CustomerUnStructuredData.class, "customerid", c.getId());

			
			return objCustomerUnStructuredDataList;			
		} finally{
			obj.endTask();
		}}	
	}

	@SuppressWarnings("unchecked")
	public int addNewKYCAnswer(Customer objCustomer,List<KYCAnswerRequestDTO> kycAnswerArray, ApplicationUsers loggedInUser) throws Exception {		
		synchronized (DAO.class){
			try {
				obj.startTask();

				if(objCustomer!=null) {

					for(int i=0;i<kycAnswerArray.size();i++){

						Customer objCus = (Customer) obj.getRowByAttribute(Customer.class, "id", objCustomer.getId());
						int questionId=Integer.valueOf(String.valueOf(kycAnswerArray.get(i).getQuestionId())).intValue();
						KYCQuestion objQues = (KYCQuestion) obj.getRowByAttribute(KYCQuestion.class, "id", questionId);
						List<KYCAnswer> list = obj.getAllOnCriteria(KYCAnswer.class,"customer", objCus, "question", objQues);

						if (list != null && list.size() > 0) {
							KYCAnswer temp=list.get(0);
							temp.setQuestionAnswer(kycAnswerArray.get(i).getAnswerValue());
							//Audit Log
							obj.getCreatedSession().saveOrUpdate(temp);

						} else {

							KYCAnswer toSave = new KYCAnswer();
							int questionIdForSave=Integer.valueOf(String.valueOf(kycAnswerArray.get(i).getQuestionId())).intValue();
							KYCQuestion objQuestion = (KYCQuestion) obj.getRowByAttribute(KYCQuestion.class, "id",questionIdForSave);
							Customer objCusForSave = (Customer) obj.getRowByAttribute(Customer.class, "id", objCustomer.getId());

							toSave.setCustomer(objCusForSave);
							toSave.setQuestion(objQuestion);
							toSave.setQuestionAnswer(kycAnswerArray.get(i).getAnswerValue());
							//Audit Log
							

							obj.getCreatedSession().saveOrUpdate(toSave);
						}
					}
				}

				return 1;


				//will change it as function will throw exception incase of exception
			} catch (Exception e) {
				return obj.catchException(e);
			}finally{
				obj.endTask();
			}}		
	}
	
	
	
	@SuppressWarnings("unchecked")
	public int addNewKYCAnswerAgainstSpecificProduct(Customer objCustomer, Product objProduct, List<KYCAnswerRequestDTO> kycAnswerArray, ApplicationUsers loggedInUser) throws Exception {		
		synchronized (DAO.class){
			try {
				obj.startTask();

				if(objCustomer!=null) {

					for(int i=0;i<kycAnswerArray.size();i++){

						int questionId=Integer.valueOf(String.valueOf(kycAnswerArray.get(i).getQuestionId())).intValue();
						KYCQuestion objQues = (KYCQuestion) obj.getRowByAttribute(KYCQuestion.class, "id", questionId);
						List<KYCAnswer> list = obj.getAllOnCriteria(KYCAnswer.class,"customer", objCustomer, "question", objQues, "product", objProduct);

						if (list != null && list.size() > 0) {
							KYCAnswer temp=list.get(0);
							temp.setQuestionAnswer(kycAnswerArray.get(i).getAnswerValue());
							obj.getCreatedSession().saveOrUpdate(temp);

						} else {

							KYCAnswer toSave = new KYCAnswer();
							int questionIdForSave=Integer.valueOf(String.valueOf(kycAnswerArray.get(i).getQuestionId())).intValue();
							KYCQuestion objQuestion = (KYCQuestion) obj.getRowByAttribute(KYCQuestion.class, "id",questionIdForSave);

							toSave.setCustomer(objCustomer);
							toSave.setQuestion(objQuestion);
							toSave.setProduct(objProduct);
							toSave.setQuestionAnswer(kycAnswerArray.get(i).getAnswerValue());

							obj.getCreatedSession().saveOrUpdate(toSave);
						}
					}
				}

				return 1;


				//will change it as function will throw exception incase of exception
			} catch (Exception e) {
				return obj.catchException(e);
			}finally{
				obj.endTask();
			}}		
	}


	@SuppressWarnings("unchecked")
	public KYCAnswer getKycAnswer(int questionId) throws Exception {		



		try {
			obj.startTask();		

			KYCQuestion objKycQuestion = (KYCQuestion) 	obj.getRowByAttribute(KYCQuestion.class, "id", questionId);

			KYCAnswer objKycAnswer = (KYCAnswer) 	obj.getRowByAttribute(KYCAnswer.class, "question", objKycQuestion);	


			return objKycAnswer;

		} catch (Exception e) {
			return null;
		}finally{
			obj.endTask();
		}	

	}
	
	@SuppressWarnings("unchecked")
	public KYCAnswer getCustomerMostRecentKycAnswer(Customer objCustomer) throws Exception {		
		try {
			obj.startTask();		

			KYCAnswer objKycAnswer = (KYCAnswer) 	obj.getRowByAttributeAndOrder(KYCAnswer.class, "customer", objCustomer, Order.desc("createdAt"));	

			return objKycAnswer;

		} catch (Exception e) {
			return null;
		}finally{
			obj.endTask();
		}	
	}
	
	
	@SuppressWarnings("unchecked")
	public List<KYCAnswer> getCustomerAllKycAnswer(Customer objCustomer, Product objProduct) throws Exception {		
		try {
			obj.startTask();		

			List<KYCAnswer> objKycAnswer = (List<KYCAnswer>) 	obj.getAllOnCriteria(KYCAnswer.class, "customer", objCustomer, "product", objProduct);	

			return objKycAnswer;

		} catch (Exception e) {
			return null;
		}finally{
			obj.endTask();
		}	
	}
	
	@SuppressWarnings("unchecked")
	public List<KYCAnswer> getCustomerKycAnswer(long cellNo) throws Exception {		
		try {
			obj.startTask();		

			Customer objCustomer= (Customer) 	obj.getRowByAttribute(Customer.class, "cellNo", cellNo);
			
			List<KYCAnswer> objKycQuestionList = (List<KYCAnswer>) 	obj.getAllOnCriteria(KYCAnswer.class, "customer", objCustomer);

			return objKycQuestionList;

		} catch (Exception e) {
			return null;
		}finally{
			obj.endTask();
		}	
	}



	/*@SuppressWarnings("unchecked")
	public int addSingleNewKYCAnswer(Customer objCustomer,int questionId,String answer) throws Exception {		
//		synchronized (DAO.class) {
			try {

				Customer objCus = (Customer) obj.getRowByAttribute(Customer.class, "id", objCustomer.getId());
				KYCQuestion objQues = (KYCQuestion) obj.getRowByAttribute(KYCQuestion.class, "id", questionId);

				List<KYCAnswer> list = obj.getAllOnCriteria(KYCAnswer.class,"customer", objCus, "question", objQues);

				if (list != null && list.size() > 0) {
					KYCQuestion objQuestion = (KYCQuestion) obj.getRowByAttribute(KYCQuestion.class, "id",objCustomer.getId());

					list.get(0).setCustomer(objCustomer);
					list.get(0).setQuestion(objQuestion);
					list.get(0).setQuestionAnswer(answer);

					obj.persistObject(list.get(0));

					if (obj.updateObject(list.get(0))) {
						return 1;
					} else {
						return -1;
					}

				} else {

					KYCAnswer toSave = new KYCAnswer();
					KYCQuestion objQuestion = (KYCQuestion) obj.getRowByAttribute(KYCQuestion.class, "id",questionId);

					toSave.setCustomer(objCustomer);
					toSave.setQuestion(objQuestion);
					toSave.setQuestionAnswer(answer);

					return obj.saveObject(toSave);
				}

				// will change it as function will throw exception incase of
				// exception
			} catch (Exception e) {
				return obj.catchException(e);
			} finally {
				// obj.endTask();
			}
//		}	
	}*/

	/*@SuppressWarnings("unchecked")
	public int addSingleNewKYCAnswer(Customer objCustomer,int questionId,String answer) throws Exception {		
		synchronized (DAO.class) {
			try {
				obj.startTask();

				Customer objCus = (Customer) obj.getRowByAttribute(Customer.class, "id", objCustomer.getId());
				KYCQuestion objQues = (KYCQuestion) obj.getRowByAttribute(KYCQuestion.class, "id", questionId);

				List<KYCAnswer> list = obj.getAllOnCriteria(KYCAnswer.class,"customer", objCus, "question", objQues);

				if (list != null && list.size() > 0) {
					KYCQuestion objQuestion = (KYCQuestion) obj.getRowByAttribute(KYCQuestion.class, "id",objCustomer.getId());

					list.get(0).setCustomer(objCustomer);
					list.get(0).setQuestion(objQuestion);
					list.get(0).setQuestionAnswer(answer);

					if (obj.updateObject(list.get(0))) {
						return 1;
					} else {
						return -1;
					}

				} else {

					KYCAnswer toSave = new KYCAnswer();
					KYCQuestion objQuestion = (KYCQuestion) obj.getRowByAttribute(KYCQuestion.class, "id",questionId);

					toSave.setCustomer(objCustomer);
					toSave.setQuestion(objQuestion);
					toSave.setQuestionAnswer(answer);

					return obj.saveObject(toSave);
				}

				// will change it as function will throw exception incase of
				// exception
			} catch (Exception e) {
				return obj.catchException(e);
			} finally {
				 obj.endTask();
			}
		}	
	}*/
	

	public ApplicationUsers getApplicationUserByToken(String token){
		synchronized (DAO.class){ try {
			obj.startTask();		
			ApplicationUsers loanApproverUser = (ApplicationUsers) 	obj.getRowByAttribute(ApplicationUsers.class, "assignToken", token);
			return loanApproverUser;
		} catch (Exception e) {
			return null;
		}finally{
			try {
				obj.endTask();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}}		
	}
	public int updateKYCQuestion(String kycQuestionId,String questionToAsk,String answerType, String listOfPossibleAnswers, String catagory,boolean mandatoryStatus, int expiryInDays, ApplicationUsers loggedInUser) throws Exception {		
		synchronized (DAO.class){ try {
			obj.startTask();		

			KYCQuestion objKycQuestion = (KYCQuestion) 	obj.getRowByAttribute(KYCQuestion.class, "id", Integer.valueOf(kycQuestionId).intValue());	
			
			if(objKycQuestion==null){
				return -5;
			}

			objKycQuestion.setQuestionToAsk(questionToAsk);
			if(answerType!=null)
			objKycQuestion.setAnswerType(answerType.toUpperCase());

			if(listOfPossibleAnswers!=null)
			objKycQuestion.setListOfPossibleAnswers(listOfPossibleAnswers);

			if(catagory!=null)
			objKycQuestion.setCatagory(catagory.toUpperCase());
			
			objKycQuestion.setMandatoryStatus(mandatoryStatus);
			objKycQuestion.setExpiryInDays(expiryInDays);
			objKycQuestion.setUpdatedBy(loggedInUser);

			obj.updateObject(objKycQuestion);
			return 1;

		} catch (Exception e) {
			return obj.catchException(e);
		}finally{
			obj.endTask();
		}}		
	}



	public int addNewProduct(String productCatagory,String productName,
			int KYCQuestionIds[],
			int assumptionsIds[],
			String AssumptionsValues[],
			ApplicationUsers loggedInUser) throws Exception {		
		synchronized (DAO.class){ try {
			obj.startTask();			


			ArrayList<ProductAssumption> listOfAssumptions=new ArrayList<ProductAssumption>();

			{

				for(int i:assumptionsIds){
					ProductAssumption objProductAssumption =  (ProductAssumption) obj.getCreatedSession().get(ProductAssumption.class, i);
					if(objProductAssumption!=null){
						listOfAssumptions.add(objProductAssumption);
					}
				}	
				if(listOfAssumptions.size()!=assumptionsIds.length){
					System.err.println("Count Mismatch");
					return -14;
				}		
			}




			HashSet<KYCQuestion> listOfQuestions=new HashSet<KYCQuestion>();
			{
				for(int i:KYCQuestionIds){
					KYCQuestion objKYCQuestion =  (KYCQuestion) obj.getCreatedSession().get(KYCQuestion.class, i);
					if(objKYCQuestion!=null){
						listOfQuestions.add(objKYCQuestion);
					}
				}	

				if(listOfQuestions.size()!=KYCQuestionIds.length){
					System.err.println("Count Mismatch");
					return -15;
				}	
			}

			Product toSave=new Product();
			toSave.setProductCatagory(productCatagory);
			toSave.setProductName(productName);
			toSave.setQuestions(listOfQuestions);
			toSave.setCreatedBy(loggedInUser);


			int toReturn=obj.saveObject(toSave);

			HashSet<ProductSpecification> objListOfProductSpecification= new HashSet<ProductSpecification>();

			int counter=0;
			for(ProductAssumption i:listOfAssumptions){
				ProductSpecification newProductSpecification=new ProductSpecification();
				newProductSpecification.setProductSpecificationAssumption(i);
				newProductSpecification.setAssumptionValue(AssumptionsValues[counter]);	

			

				//newProductSpecification.setProduct(toSave);
				counter++;
				if(obj.saveObject(newProductSpecification) > 0){
					objListOfProductSpecification.add(newProductSpecification);
				} else {
					System.err.println("Failed to Add ProductSpecification ");
					return -16;
				}

			}

			toSave.setProductSpecification(objListOfProductSpecification);

			obj.saveOrUpdateObject(toSave);

			return toReturn;
		} catch (Exception e) {
			return obj.catchException(e);
		}finally{
			obj.endTask();
		}}		
	}
	
	public int addNewProductWithTermsAndConditionFile(String productCatagory,String productName,
			int KYCQuestionIds[],
			int assumptionsIds[],
			String AssumptionsValues[],
			String termsAndConditionFilePath,
			ApplicationUsers loggedInUser) throws Exception {		
		synchronized (DAO.class){ try {
			obj.startTask();			


			ArrayList<ProductAssumption> listOfAssumptions=new ArrayList<ProductAssumption>();

			{
				for(int i:assumptionsIds){
					ProductAssumption objProductAssumption =  (ProductAssumption) obj.getCreatedSession().get(ProductAssumption.class, i);
					if(objProductAssumption!=null){
						listOfAssumptions.add(objProductAssumption);
					}
				}	
				if(listOfAssumptions.size()!=assumptionsIds.length){
					System.err.println("Count Mismatch");
					return -14;
				}		
			}




			HashSet<KYCQuestion> listOfQuestions=new HashSet<KYCQuestion>();
			{
				for(int i:KYCQuestionIds){
					KYCQuestion objKYCQuestion =  (KYCQuestion) obj.getCreatedSession().get(KYCQuestion.class, i);
					if(objKYCQuestion!=null){
						listOfQuestions.add(objKYCQuestion);
					}
				}	

				if(listOfQuestions.size()!=KYCQuestionIds.length){
					System.err.println("Count Mismatch");
					return -15;
				}	
			}

			Product toSave=new Product();
			toSave.setProductCatagory(productCatagory);
			toSave.setProductName(productName);
			toSave.setQuestions(listOfQuestions);
			toSave.setTermsAndConditionFilePath(termsAndConditionFilePath);
			toSave.setCreatedBy(loggedInUser);


			int toReturn=obj.saveObject(toSave);

			HashSet<ProductSpecification> objListOfProductSpecification= new HashSet<ProductSpecification>();

			int counter=0;
			for(ProductAssumption i:listOfAssumptions){
				ProductSpecification newProductSpecification=new ProductSpecification();
				newProductSpecification.setProductSpecificationAssumption(i);
				newProductSpecification.setAssumptionValue(AssumptionsValues[counter]);	

			

				//newProductSpecification.setProduct(toSave);
				counter++;
				if(obj.saveObject(newProductSpecification) > 0){
					objListOfProductSpecification.add(newProductSpecification);
				} else {
					System.err.println("Failed to Add ProductSpecification ");
					return -16;
				}

			}

			toSave.setProductSpecification(objListOfProductSpecification);

			obj.saveOrUpdateObject(toSave);

			return toReturn;
		} catch (Exception e) {
			return obj.catchException(e);
		}finally{
			obj.endTask();
		}}		
	}

	public int updateProduct(String productId,
			String productCatagory,
			String productName,
			int KYCQuestionIds[],
			int assumptionsIds[],
			String AssumptionsValues[],
			 ApplicationUsers loggedInUser
			) throws Exception {		

		synchronized (DAO.class){ try {
			obj.startTask();

			Product objProduct = (Product) 	obj.getRowByAttribute(Product.class, "id", Integer.valueOf(productId).intValue());	

			if(objProduct==null){
				return -5;
			}


			ArrayList<ProductAssumption> listOfAssumptions=new ArrayList<ProductAssumption>();

			{

				for(int i:assumptionsIds){
					ProductAssumption objProductAssumption =  (ProductAssumption) obj.getCreatedSession().get(ProductAssumption.class, i);
					if(objProductAssumption!=null){
						listOfAssumptions.add(objProductAssumption);
					}
				}	
				if(listOfAssumptions.size()!=assumptionsIds.length){
					System.err.println("Count Mismatch");
					return -14;
				}		
			}




			HashSet<KYCQuestion> listOfQuestions=new HashSet<KYCQuestion>();
			{
				for(int i:KYCQuestionIds){
					KYCQuestion objKYCQuestion =  (KYCQuestion) obj.getCreatedSession().get(KYCQuestion.class, i);
					if(objKYCQuestion!=null){
						listOfQuestions.add(objKYCQuestion);
					}
				}	

				if(listOfQuestions.size()!=KYCQuestionIds.length){
					System.err.println("Count Mismatch");
					return -15;
				}	
			}

			objProduct.setProductCatagory(productCatagory);
			objProduct.setProductName(productName);
			objProduct.setQuestions(listOfQuestions);
			objProduct.setUpdatedBy(loggedInUser);
		
			

			int toReturn=obj.saveObject(objProduct);

			HashSet<ProductSpecification> objListOfProductSpecification= new HashSet<ProductSpecification>();

			int counter=0;
			for(ProductAssumption i:listOfAssumptions){
				ProductSpecification newProductSpecification=new ProductSpecification();
				newProductSpecification.setProductSpecificationAssumption(i);
				newProductSpecification.setAssumptionValue(AssumptionsValues[counter]);	

				//					objProduct.setUpdatedBy(Long.valueOf(callerId).longValue());
				//					objProduct.setDatLastUpdated(new Date());

				//newProductSpecification.setProduct(toSave);
				counter++;
				if(obj.saveObject(newProductSpecification) > 0){
					objListOfProductSpecification.add(newProductSpecification);
				} else {
					System.err.println("Failed to Add ProductSpecification ");
					return -16;
				}
			}

			objProduct.setProductSpecification(objListOfProductSpecification);

			obj.updateObject(objProduct);

			return toReturn;
		} catch (Exception e) {
			return obj.catchException(e);
		}finally{
			obj.endTask();
		}}		
	}


	public ArrayList<KYCQuestion> getListOfKYCQuestionsByIds(int ids[]) throws Exception{
		synchronized (DAO.class){ try {
			obj.startTask();			
			ArrayList<KYCQuestion> listToReturn= new ArrayList<KYCQuestion>();

			for(int i:ids){
				KYCQuestion objProductAssumption =  (KYCQuestion) obj.getCreatedSession().get(KYCQuestion.class, i);
				if(objProductAssumption!=null){
					listToReturn.add(objProductAssumption);
				}
			}	
			return listToReturn;			
		} finally{
			obj.endTask();
		}}	
	}

	public ArrayList<ProductAssumption> getListOfProductAssumptionsByIds(int ids[]) throws Exception{
		synchronized (DAO.class){ try {
			obj.startTask();			
			ArrayList<ProductAssumption> listToReturn= new ArrayList<ProductAssumption>();

			for(int i:ids){
				ProductAssumption objProductAssumption =  (ProductAssumption) obj.getCreatedSession().get(ProductAssumption.class, i);
				if(objProductAssumption!=null){
					listToReturn.add(objProductAssumption);
				}
			}	
			return listToReturn;			
		} finally{
			obj.endTask();
		}}	
	}

	public int disburseNewLoan(int applicationid, Date calculatedDueDate, Date calculatedGraceDueDate, ApplicationUsers loggedInUser) {

		synchronized (DAO.class){ try {
			obj.startTask();			
			LoanApplication objLoanApplication;
			try {
				objLoanApplication = (LoanApplication) 	obj.getRowByAttribute(LoanApplication.class, "id", applicationid);
				
			
				if(objLoanApplication!=null) {

					DisbursedLoans objDisbursedLoans=new DisbursedLoans();
					objDisbursedLoans.setApplication(objLoanApplication);
					objDisbursedLoans.setTransactionId(UUID.randomUUID().toString());
					objDisbursedLoans.setLoanStatus(DisbursedLoanStatus.ACTIVE);
					objDisbursedLoans.setDueDate(calculatedDueDate);
					objDisbursedLoans.setGraceDueDate(calculatedGraceDueDate);
					objDisbursedLoans.setCreatedBy(loggedInUser);

					int applicationId = obj.saveObject(objDisbursedLoans);

					return applicationId;
				}


			} catch (Exception e) {
				e.printStackTrace();
			}		
			return -7;			
		} finally{
			try {
				obj.endTask();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}}	


	}

	public DisbursedLoans getDisbursedLoanByApplicationId(int applicationid) {

		synchronized (DAO.class){ try {
			obj.startTask();			
			DisbursedLoans objDisbursedLoans;
			try {

				LoanApplication objLoanApp = (LoanApplication) 	obj.getRowByAttribute(LoanApplication.class, "id", applicationid);

				objDisbursedLoans = (DisbursedLoans) 	obj.getRowByAttribute(DisbursedLoans.class, "application", objLoanApp);
				if(objDisbursedLoans!=null) {
					return objDisbursedLoans;
				}

			} catch (Exception e) {
				e.printStackTrace();
			}		
			return null;			
		} finally{
			try {
				obj.endTask();
			} catch (Exception e) {
				e.printStackTrace();
				LOG.info("Exception Occurred : "+e);
			}
		}}	
	}
	
	public DisbursedLoans getDisbursedLoanByLoanId(int loanId) {

		synchronized (DAO.class){ try {
			obj.startTask();			
			DisbursedLoans objDisbursedLoans;
			try {
				objDisbursedLoans = (DisbursedLoans) 	obj.getRowByAttribute(DisbursedLoans.class, "id", loanId);
				if(objDisbursedLoans!=null) {
					return objDisbursedLoans;
				}

			} catch (Exception e) {
				e.printStackTrace();
			}		
			return null;			
		} finally{
			try {
				obj.endTask();
			} catch (Exception e) {
				e.printStackTrace();
				LOG.info("Exception Occurred : "+e);
			}
		}}	
	}


	public DisbursedLoans getNonPaidLoan(int applicationid) {

		synchronized (DAO.class){ try {
			obj.startTask();			
			DisbursedLoans objDisbursedLoans;
			try {

				LoanApplication objLoanApp = (LoanApplication) 	obj.getRowByAttribute(LoanApplication.class, "id", applicationid);

				objDisbursedLoans = (DisbursedLoans) 	obj.getRowByAttribute(DisbursedLoans.class, "application", objLoanApp);

				if(objDisbursedLoans!=null) {
					if(objDisbursedLoans.getLoanStatus()==DisbursedLoanStatus.PAID){
						return null;
					}else{
						return objDisbursedLoans;
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			}		
			return null;			
		} finally{
			try {
				obj.endTask();
			} catch (Exception e) {
				e.printStackTrace();
				LOG.info("Exception Occurred : "+e);
			}
		}}	
	}	


	public Boolean updateLoanApplication(int loanApplicationId, LoanApplicationStatus status,Integer requestedAmount,boolean termsAndConditionAcceptanceStatus, ApplicationUsers loggedInUser) {

		synchronized (DAO.class){ try {
			obj.startTask();			
			LoanApplication objLoanApplication;
			try {
			
				objLoanApplication = (LoanApplication) 	obj.getRowByAttribute(LoanApplication.class, "id", loanApplicationId);

				if(objLoanApplication!=null) {
					if(requestedAmount!=null){
						objLoanApplication.setRequestedamount(requestedAmount);
					}else{
						objLoanApplication.setRequestedamount(0);
					}
					objLoanApplication.setStatus(status);
					objLoanApplication.setTermsAndConditionAcceptanceStatus(termsAndConditionAcceptanceStatus);
					objLoanApplication.setUpdatedBy(loggedInUser);
				}

				return obj.updateObject(objLoanApplication); 

			} catch (Exception e) {
				e.printStackTrace();
			}		
			return false;			
		} finally{
			try {
				obj.endTask();
			} catch (Exception e) {
				LOG.info("Exception Occurred : "+e);
			}
		}}	
	}
	
	public Boolean updateLoanApplication(int loanId, LoanApplicationStatus status, ApplicationUsers loggedInUser) {

		synchronized (DAO.class){ try {
			obj.startTask();			
			LoanApplication objLoanApplication;
			try {

				objLoanApplication = (LoanApplication) 	obj.getRowByAttribute(LoanApplication.class, "id", loanId);
				if(objLoanApplication!=null) {
					objLoanApplication.setStatus(status);
					objLoanApplication.setUpdatedBy(loggedInUser);
					
					//We Need to Save Loan Approver/Rejecter Details(We Cannot Save this Info in UpdatedBy, coz UpdatedBy Changed Frequently on Multiple Update Operations) 
					if(status==LoanApplicationStatus.APPROVED || status==LoanApplicationStatus.REJECTED){
						objLoanApplication.setLoanApprover(loggedInUser);
					}
					
				}

				return obj.updateObject(objLoanApplication); 

			} catch (Exception e) {
				e.printStackTrace();
			}		
			return false;			
		} finally{
			try {
				obj.endTask();
			} catch (Exception e) {
				LOG.info("Exception Occurred : "+e);
			}
		}}	
	}




	public Boolean updateDisbursedLoanStatusUsingLoanId(int loanId, DisbursedLoans disbursedLoan,ApplicationUsers loggedInUser) {

		synchronized (DAO.class){ try {
			obj.startTask();			
			DisbursedLoans objDisbursedLoan;
			try {

				objDisbursedLoan = (DisbursedLoans) 	obj.getRowByAttribute(DisbursedLoans.class, "id", loanId);
				objDisbursedLoan.setLoanStatus(disbursedLoan.getLoanStatus());

				return obj.updateObject(objDisbursedLoan); 
			} catch (Exception e) {
				e.printStackTrace();
			}		
			return false;			
		} finally{
			try {
				obj.endTask();
			} catch (Exception e) {
				LOG.info("Exception Occurred : "+e);
			}
		}}	
	}


	public int createNewLoanApplication(long cellNo,int productId,int walletTypeId) throws Exception {	
		LOG.info("LMSDAO.createNewLoanApplication()--Start");
		
		synchronized (DAO.class){ try {
			obj.startTask();
			int applicationId = -1;

			Customer objCustomer = (Customer) 	obj.getRowByAttribute(Customer.class, "cellNo", cellNo);	
			Product objProduct = (Product) 	obj.getRowByAttribute(Product.class, "id", productId);		

			if(objProduct!=null && objCustomer!=null) {
				LoanApplication toSave=new LoanApplication();
				toSave.setCustomer(objCustomer);
				toSave.setProduct(objProduct);
				toSave.setWalletTypeId(walletTypeId);
				toSave.setRequestedamount(0);
				toSave.setStatus(LoanApplicationStatus.PENDINGFORAPPROVAL);
				applicationId = obj.saveObject(toSave);
				
				if(applicationId>0) {
					List<KYCAnswer> listOfAllKYCAnswers=new ArrayList<KYCAnswer>(new HashSet<KYCAnswer>(obj.getAllOnCriteria(KYCAnswer.class,"customer",objCustomer,"product",objProduct))) ;

					for(KYCAnswer iter:listOfAllKYCAnswers) {
						if(iter.getCustomer().getId()==objCustomer.getId()) {
							KYCAnswerSnapShotAtLoanApplication toS=new KYCAnswerSnapShotAtLoanApplication();

							toS.setLoanapplication(toSave);
							toS.setQuestion(iter.getQuestion());
							toS.setQuestionanswer(iter.getQuestionAnswer());
							int temp=obj.saveObject(toS);

							if(temp<0) {
								LOG.info("SHOULD NOT HAVE HAPPEND 2");
								applicationId=-1;
								throw new Exception();
							}
						}
					}

					List<ProductSpecification> listOfAllProductSpecification=obj.getAll(ProductSpecification.class);

					for(ProductSpecification iter:listOfAllProductSpecification) {					
						if(objProduct.doesContain(iter)) {
							ProductSpecificationSnapShotAtLoanApplication toS=new ProductSpecificationSnapShotAtLoanApplication();
							toS.setAssumptionvalue(iter.getAssumptionValue());
							toS.setLoanapplication(toSave);
							toS.setProductspecificationassumption(iter.getProductSpecificationAssumption());

							LOG.info("Calling saveObject(ProductSpecificationSnapShotAtLoanApplication) method");
							int temp=obj.saveObject(toS);
							LOG.info("Response is: "+temp);
							
							if(temp<0) {
								LOG.info("SHOULD NOT HAVE HAPPEND 1");
								applicationId=-1;
								throw new Exception();
							}
						}
					}
				}
			}else{
				throw new Exception("Customer or Selected Product Not Found");
			}
			return applicationId;
		} catch (Exception e) {
			return obj.catchException(e);
		}finally{
			LOG.info("LMSDAO.createNewLoanApplication()--End");
			obj.endTask();
		}}		
	}

	public int addNewCustomer(long pakistaniCNIC,long cellNo, boolean oneLoadCustomerFlagStatus, CustomerStatus customerStatus,String fcmToken) throws Exception {		
		synchronized (DAO.class){ try {
			obj.startTask();			
			Customer toSave=new Customer();
			toSave.setCellNo(cellNo);
			toSave.setPakistaniCNIC(pakistaniCNIC);
			toSave.setStatus(customerStatus);
			toSave.setPartnerId(1);	
			toSave.setOneLoadCustomerStatus(oneLoadCustomerFlagStatus);
			toSave.setFcmToken(fcmToken);
			return obj.saveObject(toSave);
		} catch (Exception e) {
			return obj.catchException(e);
		}finally{
			obj.endTask();
		}}		
	}

	public List<Product> getAllProductsLazy() throws Exception {		
		try {
			obj.startTask();

			List<Product> toReturn=obj.getAll(Product.class);
			return toReturn;
		}catch(Exception e){
			e.printStackTrace();
			throw e;
		}finally{
			obj.endTask();
		}
	}
	
	public Product getProduct(int productId) throws Exception {		
		try {
			obj.startTask();

			Product toReturn=(Product) obj.getRowByAttribute(Product.class, "id", productId);
			return toReturn;
		}catch(Exception e){
			e.printStackTrace();
			throw e;
		}finally{
			obj.endTask();
		}
	}

	/*@SuppressWarnings("unchecked")
	public List<LoanApplication> getAllLoanApplicationsEager() throws Exception {	
		try {
			obj.startTask();			

			@SuppressWarnings("unchecked")
			List<LoanApplication> toReturn=obj.getAll(LoanApplication.class);

			LOG.info("ResulSet(getAllLoanApplicatiosEager) Size: "+toReturn.size());
			Collections.sort(toReturn);
			return toReturn;			
		}catch(Exception e){
			e.printStackTrace();
			throw e;
		}finally{
			obj.endTask();
		}
	}*/

	@SuppressWarnings("unchecked")
	public LoanApplication getLoanApplicationById(int loanApplicationId) throws Exception {	
		try {
			obj.startTask();			

			LoanApplication toReturn = (LoanApplication) 	obj.getRowByAttribute(LoanApplication.class, "id", loanApplicationId);	

			return toReturn;

		}catch(Exception e){
			e.printStackTrace();
			throw e;
		}finally{
			obj.endTask();
		}
	}
	
	
	@SuppressWarnings("unchecked")
	public List<LoanApplication> getAllLoanApplication() throws Exception {	
		try {
			obj.startTask();			

			List<LoanApplication> toReturn=new ArrayList<LoanApplication>(new HashSet<LoanApplication>(obj.getAll(LoanApplication.class))) ;

			return toReturn;

		}catch(Exception e){
			e.printStackTrace();
			throw e;
		}finally{
			obj.endTask();
		}
	}


	/*@SuppressWarnings("unchecked")
	public List<LoanApplication> getAllPendingLoanApplicationsEager() throws Exception {	
		try {
			obj.startTask();

			List<LoanApplication> toReturn=new ArrayList<LoanApplication>(new HashSet<LoanApplication>(obj.getAll(LoanApplication.class))) ;	
			List<DisbursedLoans> listOfDisbursedLoans=obj.getAll(DisbursedLoans.class);
			if(listOfDisbursedLoans!=null && listOfDisbursedLoans.size()>0){
				for(DisbursedLoans i:listOfDisbursedLoans){
					toReturn.remove(i.getApplication());	
				}
			}
			Collections.sort(toReturn);
			return toReturn;
		}catch(Exception e){
			e.printStackTrace();
			throw e;
		}finally{
			obj.endTask();
		}
	}*/
	
	@SuppressWarnings("unchecked")
	public List<LoanApplication> getAllPendingLoanApplications() throws Exception {	
		try {
			obj.startTask();

			List<LoanApplication> toReturn=new ArrayList<LoanApplication>(new HashSet<LoanApplication>(obj.getAll(LoanApplication.class))) ;
			
			List<DisbursedLoans> listOfDisbursedLoans=obj.getAll(DisbursedLoans.class);
			if(listOfDisbursedLoans!=null && listOfDisbursedLoans.size()>0){
				for(DisbursedLoans i:listOfDisbursedLoans){
					toReturn.remove(i.getApplication());	
				}
			}
			Collections.sort(toReturn);
			return toReturn;
		}catch(Exception e){
			e.printStackTrace();
			throw e;
		}finally{
			obj.endTask();
		}
	}

	public List<LoanApplication> getAllDisbursedLoanApplicationsForCustomer(int customerId) throws Exception{
		synchronized (DAO.class){ try {
			obj.startTask();			

			List<DisbursedLoans> listOfDisbursedLoans=obj.getAll(DisbursedLoans.class);
			List<LoanApplication> toReturn = new ArrayList<LoanApplication>();
			int total=0;
			if(listOfDisbursedLoans!=null && listOfDisbursedLoans.size()>0){
				for(DisbursedLoans i:listOfDisbursedLoans){
					if(i.getApplication().getCustomer().getId()==customerId){
						toReturn.add(i.getApplication());
					}			
				}
			}
			return toReturn;			
		} finally{
			obj.endTask();
		}}	
	}
	
	public List<DisbursedLoans> getAllDisbursedLoansForCustomer(int customerId) throws Exception{
		synchronized (DAO.class){ try {
			obj.startTask();			

			List<DisbursedLoans> listOfDisbursedLoans=obj.getAll(DisbursedLoans.class);
			List<DisbursedLoans> toReturn = new ArrayList<DisbursedLoans>();
			if(listOfDisbursedLoans!=null && listOfDisbursedLoans.size()>0){
				for(DisbursedLoans i:listOfDisbursedLoans){
					if(i.getApplication().getCustomer().getId()==customerId){
						toReturn.add(i);
					}			
				}
			}
			return toReturn;			
		} finally{
			obj.endTask();
		}}	
	}


	public List<LoanApplication> getAllNonCompletedDisbursedLoanApplicationsForCustomer(int customerId) throws Exception{
		
		LOG.info("LMSDAO.getAllNonCompletedDisbursedLoanApplicationsForCustomer()--Start");
		synchronized (DAO.class){ try {
			obj.startTask();			

			List<DisbursedLoans> listOfDisbursedLoans=obj.getAll(DisbursedLoans.class);
			List<LoanApplication> toReturn = new ArrayList<LoanApplication>();
			int total=0;
			if(listOfDisbursedLoans!=null && listOfDisbursedLoans.size()>0){ //first we will check for disbursed pending loan in disbursedLoan(waiting for completion of repayment) 
				for(DisbursedLoans i:listOfDisbursedLoans){
					if(i.getApplication().getCustomer().getId()==customerId && !(i.getApplication().getStatus()==LoanApplicationStatus.COMPLETED)){
						toReturn.add(i.getApplication());
					}			
				}
			}else{// if we found nothing from disbursed loans then we will check for pending loans in loanApplicationTable(waiting for dispursement)
				List<LoanApplication> listOfLoanApplication=obj.getAll(LoanApplication.class);
				for(LoanApplication i:listOfLoanApplication){
					if(i.getCustomer().getId()==customerId && !(i.getStatus()==LoanApplicationStatus.COMPLETED)){
						toReturn.add(i);
					}			
				}
			}
			return toReturn;			
		} finally{
			obj.endTask();
			LOG.info("LMSDAO.getAllNonCompletedDisbursedLoanApplicationsForCustomer()--End");
		}}	
	}

	public List<DisbursedLoans> getAllDisbursedButNotCompletedLoanApplicationsForCustomer(int customerId) throws Exception{
		synchronized (DAO.class){ try {
			obj.startTask();			

			List<DisbursedLoans> listOfDisbursedLoans=obj.getAll(DisbursedLoans.class);
			List<DisbursedLoans> toReturn=new ArrayList<DisbursedLoans>();

			if(listOfDisbursedLoans!=null && listOfDisbursedLoans.size()>0){ //first we will check for disbursed pending loan in disbursedLoan(waiting for completion of repayment) 
				for(DisbursedLoans i:listOfDisbursedLoans){
					if(i.getApplication().getCustomer().getId()==customerId && !(i.getApplication().getStatus()==LoanApplicationStatus.COMPLETED)){
						toReturn.add(i);
					}			
				}
			}
			return toReturn;			
		} finally{
			obj.endTask();
		}}	
	}
	
	public List<LoanApplication> getAllNonCompletedLoanApplicationsUsingDisbursedLoansForCustomer(int customerId) throws Exception{
		synchronized (DAO.class){ try {
			obj.startTask();			

			List<DisbursedLoans> listOfDisbursedLoans=obj.getAll(DisbursedLoans.class);
			List<LoanApplication> toReturn=new ArrayList<LoanApplication>();

			if(listOfDisbursedLoans!=null && listOfDisbursedLoans.size()>0){ //first we will check for disbursed pending loan in disbursedLoan(waiting for completion of repayment) 
				for(DisbursedLoans i:listOfDisbursedLoans){
					if(i.getApplication().getCustomer().getId()==customerId && !(i.getApplication().getStatus()==LoanApplicationStatus.COMPLETED)){
						toReturn.add(i.getApplication());
					}			
				}
			}
			return toReturn;			
		} finally{
			obj.endTask();
		}}	
	}


	/*@SuppressWarnings("unchecked")
	public List<DisbursedLoans> getAllDisbursedLoansByStatus(DisbursedLoanStatus loanStatus) throws Exception{
		synchronized (DAO.class){ try {
			obj.startTask();			

			List<DisbursedLoans> listOfDisbursedLoans=obj.getAllOnCriteria(DisbursedLoans.class,"loanStatus",loanStatus);

			return listOfDisbursedLoans;			
		} finally{
			obj.endTask();
		}}	
	}*/


	@SuppressWarnings("unchecked")
	public List<DisbursedLoans> getAllPaidOrUnPaidDisbursedLoans(boolean isPaidRequired) throws Exception{
		synchronized (DAO.class){ try {
			obj.startTask();			

			List<DisbursedLoans> listOfDisbursedLoans=obj.getAll(DisbursedLoans.class);
			List<DisbursedLoans> toReturn=new ArrayList<DisbursedLoans>();
			if(isPaidRequired){
				for(DisbursedLoans disp:listOfDisbursedLoans){
					if(disp.getLoanStatus()==DisbursedLoanStatus.PAID){
						toReturn.add(disp);
					}
				}
			}else{
				for(DisbursedLoans disp:listOfDisbursedLoans){
					if(disp.getLoanStatus()!=DisbursedLoanStatus.PAID){
						toReturn.add(disp);
					}
				}
			}


			return toReturn;			
		} finally{
			obj.endTask();
		}}	
	}


	public List<LoanApplication> getAllNonCompletedLoanApplicationsForCustomer(int customerId) throws Exception{
		LOG.info("LMSDAO.getAllNonCompletedLoanApplicationsForCustomer()--Start");
		synchronized (DAO.class){ try {
			obj.startTask();			

			List<LoanApplication> toReturn = new ArrayList<LoanApplication>();
			Customer customer=(Customer) obj.getRowByAttribute(Customer.class, "id", customerId);
			
			List<LoanApplication> listOfLoanApplication=new ArrayList<LoanApplication>(new HashSet<LoanApplication>(obj.getAllOnCriteria(LoanApplication.class,"customer",customer)));
			for(LoanApplication i:listOfLoanApplication){
				if(i.getCustomer().getId()==customerId 
						&& !(i.getStatus()==LoanApplicationStatus.COMPLETED || i.getStatus()==LoanApplicationStatus.DECLINED || i.getStatus()==LoanApplicationStatus.REJECTED)){
					toReturn.add(i);
				}			
			}
			return toReturn;			
		} finally{
			obj.endTask();
			LOG.info("LMSDAO.getAllNonCompletedLoanApplicationsForCustomer()--Start");
		}}	
	}

	public List<Product> getAllProductsEager() throws Exception {	
		try {
			obj.startTask();

			List<Product> toReturn=new ArrayList<Product>(new HashSet<Product>(obj.getAll(Product.class))) ;	

			Collections.sort(toReturn);

			return toReturn;
		}catch(Exception e){
			e.printStackTrace();
			throw e;
		}finally{
			obj.endTask();
		}
	}

	public List<KYCAnswerSnapShotAtLoanApplication> getListKYCAnswerSnapShotForApplication(int applicationId) throws Exception {		
		try {
			obj.startTask();
			ArrayList<KYCAnswerSnapShotAtLoanApplication> toReturnFrom=(ArrayList<KYCAnswerSnapShotAtLoanApplication>) obj.getAll(KYCAnswerSnapShotAtLoanApplication.class);

			ArrayList<KYCAnswerSnapShotAtLoanApplication> toReturn=new ArrayList<KYCAnswerSnapShotAtLoanApplication>();

			for(KYCAnswerSnapShotAtLoanApplication i:toReturnFrom) {
				if(i.getLoanapplication().getId()==applicationId ) {
					toReturn.add(i);
				}

			}
			return toReturn;
		}catch(Exception e){
			e.printStackTrace();
			throw e;
		}finally{
			obj.endTask();
		}
	}


	public List<ProductSpecificationSnapShotAtLoanApplication> getListOfProductSpecificationSnapShotAtLoanApplication(int applicationId) throws Exception {		
		try {
			obj.startTask();
			ArrayList<ProductSpecificationSnapShotAtLoanApplication> toReturnFrom=(ArrayList<ProductSpecificationSnapShotAtLoanApplication>) obj.getAll(ProductSpecificationSnapShotAtLoanApplication.class);

			ArrayList<ProductSpecificationSnapShotAtLoanApplication> toReturn=new ArrayList<ProductSpecificationSnapShotAtLoanApplication>();

			for(ProductSpecificationSnapShotAtLoanApplication i:toReturnFrom) {
				if(i.getLoanapplication().getId()==applicationId ) {
					toReturn.add(i);
				}

			}
			return toReturn;
		}catch(Exception e){
			e.printStackTrace();
			throw e;
		}finally{
			obj.endTask();
		}
	}


	public ArrayList<KYCQuestion> getAllKYCQuestions() throws Exception {		
		try {
			obj.startTask();

			ArrayList<KYCQuestion> toReturn=(ArrayList<KYCQuestion>) obj.getAll(KYCQuestion.class);
			return toReturn;
		}catch(Exception e){
			e.printStackTrace();
			throw e;
		}finally{
			obj.endTask();
		}
	}

	public ArrayList<ProductAssumption> getAllProductAssumption() throws Exception {		
		try {
			obj.startTask();

			ArrayList<ProductAssumption> toReturn=(ArrayList<ProductAssumption>) obj.getAll(ProductAssumption.class);

			Collections.sort(toReturn);

			return toReturn;
		}catch(Exception e){
			e.printStackTrace();
			throw e;
		}finally{
			obj.endTask();
		}
	}

	public ArrayList<ProductSpecification> getProductSpecificationAgainstProduct(int productId) throws Exception {	
		try {
			obj.startTask();
			Product objProduct = (Product) 	obj.getRowByAttribute(Product.class, "id", productId);				
			if(objProduct!=null){			

				HashSet<ProductSpecification> toParse=new HashSet<ProductSpecification>(objProduct.getProductSpecification());

				if(toParse!=null){
					return new ArrayList<ProductSpecification>(toParse);

				}				
			}			
		}catch(Exception e){
			e.printStackTrace();
			throw e;
		}finally{
			obj.endTask();
		}
		return null;
	}

	public int updateAssumption(String assumptionId, String assumptionName, String assumptionType, ApplicationUsers loggedInUser) throws Exception {		
		synchronized (DAO.class){ try {
			obj.startTask();		
			ProductAssumption objAssumption = (ProductAssumption) 	obj.getRowByAttribute(ProductAssumption.class, "id", Integer.valueOf(assumptionId).intValue());	

			if(objAssumption==null){
				return -5;
			}

			objAssumption.setName(assumptionName);
			if(assumptionType!=null)
			objAssumption.setDataType(assumptionType.toUpperCase());
			
			objAssumption.setUpdatedBy(loggedInUser);

			obj.updateObject(objAssumption);
			return 1;

		} catch (Exception e) {
			return obj.catchException(e);
		}finally{
			obj.endTask();
		}}		
	}

	public ArrayList<ProductAssumption> getProductAssumptionsAgainstProduct(int productId) throws Exception {		
		try {
			obj.startTask();
			Product objProduct = (Product) 	obj.getRowByAttribute(Product.class, "id", productId);			
			ArrayList<ProductAssumption> toReturn = new ArrayList<ProductAssumption>();			
			if(objProduct!=null){			
				Set<ProductSpecification> toParse= objProduct.getProductSpecification();				
				if(toParse!=null){
					for(ProductSpecification i:toParse){
						toReturn.add(i.getProductSpecificationAssumption());
					}
					return toReturn;
				}				
			}			
		}catch(Exception e){
			e.printStackTrace();
			throw e;
		}finally{
			obj.endTask();
		}
		return null;
	}

	public ArrayList<KYCQuestion> getKYCQuestionsAgainstProduct(int productId) throws Exception {		
		try {
			obj.startTask();
			Product objProduct = (Product) 	obj.getRowByAttribute(Product.class, "id", productId);

			if(objProduct!=null){
				return new ArrayList<KYCQuestion>(objProduct.getQuestions()) ;
			}		

		}catch(Exception e){
			e.printStackTrace();
			throw e;
		}finally{
			obj.endTask();
		}
		return null;
	}

	public List<Customer> getAllCustomers() throws Exception {		
		try {
			obj.startTask();

			List<Customer> toReturn=obj.getAll(Customer.class);
			Collections.sort(toReturn);
			return toReturn;
		}catch(Exception e){
			e.printStackTrace();
			throw e;
		}finally{
			obj.endTask();
		}
	}

	public int getCustomerBalance(int customerId) throws Exception{
		synchronized (DAO.class){ try {
			obj.startTask();			

			List<DisbursedLoans> listOfDisbursedLoans=obj.getAll(DisbursedLoans.class);

			int total=0;

			if(listOfDisbursedLoans!=null && listOfDisbursedLoans.size()>0){
				for(DisbursedLoans i:listOfDisbursedLoans){
					if(i.getApplication().getCustomer().getId()==customerId){
						total=total+i.getApplication().getRequestedamount();
					}			
				}
			}

			return total;			
		} finally{
			obj.endTask();
		}}	
	}




	/*@SuppressWarnings("unchecked")
	public List<LoanApplication> getAllDisbursedLoanApplications() throws Exception{
		synchronized (DAO.class){ try {
			obj.startTask();			

			@SuppressWarnings("unchecked")
			List<DisbursedLoans> listOfDisbursedLoans=obj.getAll(DisbursedLoans.class);

			List<LoanApplication> toReturn = new ArrayList<LoanApplication>();

			if(listOfDisbursedLoans!=null && listOfDisbursedLoans.size()>0){
				for(DisbursedLoans i:listOfDisbursedLoans){
					toReturn.add(i.getApplication());	
				}
			}
			Collections.sort(toReturn);

			return toReturn;			
		} finally{
			obj.endTask();
		}}	
	}*/
	
	@SuppressWarnings("unchecked")
	public List<LoanApplication> getAllCompletedLoanApplications() throws Exception{
		synchronized (DAO.class){ try {
			obj.startTask();			

			@SuppressWarnings("unchecked")
			List<DisbursedLoans> listOfDisbursedLoans=obj.getAll(DisbursedLoans.class);

			List<LoanApplication> toReturn = new ArrayList<LoanApplication>();

			if(listOfDisbursedLoans!=null && listOfDisbursedLoans.size()>0){
				for(DisbursedLoans i:listOfDisbursedLoans){
					toReturn.add(i.getApplication());	
				}
			}
			Collections.sort(toReturn);

			return toReturn;			
		} finally{
			obj.endTask();
		}}	
	}


	public Customer loginInfoByToken(String sessionToken) throws Exception {		
		synchronized (DAO.class){ try {
			obj.startTask();	

			Customer objCustomer = (Customer) 	obj.getRowByAttribute(Customer.class, "sessionToken", sessionToken);	

			if(objCustomer==null){
				return null;
			}

			return objCustomer;

		} catch (Exception e) {
			return null;
		}finally{
			obj.endTask();
		}}		
	}
	
	public boolean updatePicturePaths(String sessionToken,String idCardFrontPath,String idCardBackPath,String idCardWithFacePath) throws Exception {		
		synchronized (DAO.class){ try {
			obj.startTask();	

			Customer objCustomer = (Customer) 	obj.getRowByAttribute(Customer.class, "sessionToken", sessionToken);	

			if(objCustomer==null){
				return false;
			}
			
			if(idCardFrontPath!=null) {
				objCustomer.setIdCardFrontPath(idCardFrontPath);
			}
			
			if(idCardBackPath!=null) {
				objCustomer.setIdCardBackPath(idCardBackPath);
			}
			
			if(idCardWithFacePath!=null) {
				objCustomer.setIdCardWithFacePath(idCardWithFacePath);
			}
			
			if(obj.updateObject(objCustomer)){
				return true;
			}else{
				return false;
			}

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}finally{
			obj.endTask();
		}}		
	}
	
	
	public boolean updateUploadedFilePath(String fileName, String uploadedFilePath, ApplicationUsers loggedInUser) throws Exception {		
		synchronized (DAO.class){ try {
			obj.startTask();	

			Preferences preference = (Preferences) 	obj.getRowByAttribute(Preferences.class, "preferenceName", fileName);	
			if(preference==null){
				Preferences toSave=new Preferences();
				toSave.setPreferenceName(fileName);
				toSave.setPreferenceValue(uploadedFilePath);
				toSave.setCreatedBy(loggedInUser);
				
				int result= obj.saveObject(toSave);
				
				if(result>0){
					return true;
				}else{
					return false;
				}
			}else{
				preference.setPreferenceValue(uploadedFilePath);
				preference.setUpdatedBy(loggedInUser);
				
				return obj.updateObject(preference);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}finally{
			obj.endTask();
		}}		
	}
	
	
	/*public Preferences getPreferenceRecord(String prefName) throws Exception {		
		synchronized (DAO.class){ try {
			obj.startTask();	

			Preferences preference = (Preferences) 	obj.getRowByAttribute(Preferences.class, "preferenceName", prefName);	
			if(preference==null){
				return null;
			}else{
				return preference;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}finally{
			obj.endTask();
		}}		
	}*/
	
	@SuppressWarnings("unchecked")
	public Preferences getPreferenceValueUsingName(String prefName) throws Exception {	
		try {
			obj.startTask();			

			Preferences toReturn = (Preferences) 	obj.getRowByAttribute(Preferences.class, "preferenceName", prefName);	

			return toReturn;

		}catch(Exception e){
			e.printStackTrace();
			throw e;
		}finally{
			obj.endTask();
		}
	}
	
	
	public List<LoanApplication> getLoanApplicationsByStatus(LoanApplicationStatus loanApplicationStatus) throws Exception{
		synchronized (DAO.class){ try {
			obj.startTask();			
			List<LoanApplication> toReturn=new ArrayList<LoanApplication>(new HashSet<LoanApplication>(obj.getAllOnCriteria(LoanApplication.class, "status", loanApplicationStatus))) ;
			return toReturn;			
		} finally{
			obj.endTask();
		}}	
	}
	
	public List<LoanApplication> getLoanApplicationsUsingCellNo(long cellNo) throws Exception{
		synchronized (DAO.class){ try {
			obj.startTask();
			Customer customer= (Customer) obj.getRowByAttribute(Customer.class, "cellNo", cellNo);
			List<LoanApplication> toReturn=new ArrayList<LoanApplication>(new HashSet<LoanApplication>(obj.getAllOnCriteria(LoanApplication.class, "customer", customer))) ;
			return toReturn;			
		} finally{
			obj.endTask();
		}}	
	}

	public Boolean updateLoanApplicationSelectedQuestions(int loanId, KYCQuestionRequestDTO kycQuestionDTO, int nextSequenceOrder, ApplicationUsers loggedInUser) {

		synchronized (DAO.class){ try {
			obj.startTask();			
			LoanApplication objLoanApplication;
			KYCQuestion objKYCQuestion;
			try {
				objLoanApplication = (LoanApplication) 	obj.getRowByAttribute(LoanApplication.class, "id", loanId);
				if(objLoanApplication!=null) {
					objKYCQuestion= (KYCQuestion) 	obj.getRowByAttribute(KYCQuestion.class, "id", kycQuestionDTO.getQuestionId());
						if(objKYCQuestion!=null){
							LoanApplicationSelectedKYCQuestionsForReview kycQuestionLoanMapping=new LoanApplicationSelectedKYCQuestionsForReview();
							kycQuestionLoanMapping.setLoanapplication(objLoanApplication);
							kycQuestionLoanMapping.setQuestion(objKYCQuestion);
							kycQuestionLoanMapping.setReviewSequenceId(nextSequenceOrder);
							kycQuestionLoanMapping.setReviewComments(kycQuestionDTO.getReviewComments());
							
							obj.saveOrUpdateObject(kycQuestionLoanMapping);
							return true;
						}else{
							return false;
						}
				}else{
					return false;
				}
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}		
		} finally{
			try {
				obj.endTask();
			} catch (Exception e) {
				LOG.info("Exception Occurred : "+e);
			}
		}}	
	}
	
	public LoanApplicationSelectedKYCQuestionsForReview getLoanApplicationSelectedKYCQuestionsForReview(int loanId) {

		synchronized (DAO.class){ try {
			obj.startTask();			
			LoanApplication objLoanApplication;
			LoanApplicationSelectedKYCQuestionsForReview objLoanApplicationSelectedKYCQuestionsForReview;
			try {
				objLoanApplication = (LoanApplication) 	obj.getRowByAttribute(LoanApplication.class, "id", loanId);
				if(objLoanApplication!=null) {
					objLoanApplicationSelectedKYCQuestionsForReview = (LoanApplicationSelectedKYCQuestionsForReview) 	obj.getRowByAttributeAndOrder(LoanApplicationSelectedKYCQuestionsForReview.class, "loanapplication", objLoanApplication,Order.desc("id"));
					return objLoanApplicationSelectedKYCQuestionsForReview;
				}else{
					return null;
				}
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}		
		} finally{
			try {
				obj.endTask();
			} catch (Exception e) {
				LOG.info("Exception Occurred : "+e);
			}
		}}	
	}
	
}
