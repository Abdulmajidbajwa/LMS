package monami.lms.webclientrestcontollers;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.aspectj.weaver.patterns.TypePatternQuestions.Question;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import ch.qos.logback.core.joran.util.StringToObjectConverter;
import monami.lms.datadaos.GeneralLedgerAccountDAO;
import monami.lms.datadaos.LMSDAO;
import monami.lms.dataentities.Customer;
import monami.lms.dataentities.CustomerUnStructuredData;
import monami.lms.dataentities.DisbursedLoans;
import monami.lms.dataentities.KYCAnswer;
import monami.lms.dataentities.KYCAnswerSnapShotAtLoanApplication;
import monami.lms.dataentities.KYCQuestion;
import monami.lms.dataentities.LoanApplication;
import monami.lms.dataentities.ProductSpecificationSnapShotAtLoanApplication;
import monami.lms.utilities.AES;
import monami.lms.utilities.CustomerStatus;
import monami.lms.utilities.LoanApplicationStatus;
import monami.lms.utilities.Utility;
import monami.lms.dataentities.Preferences;
import monami.lms.dataentities.Product;
import monami.lms.dataentities.ProductSpecification;
import monami.lms.request.datadtos.KYCAnswerRequestDTO;
import monami.lms.responceentities.BasicResponce;
import monami.lms.responceentities.ResponceDTO;
import monami.lms.responceentities.ResponceWithCustomerToken;
import monami.lms.responceentities.ResponceWithMessage;
import monami.lms.response.datadtos.ApplicationUserResponseDTO;
import monami.lms.response.datadtos.CustomerResponseDTO;
import monami.lms.response.datadtos.CustomerDashboardResponseDTO;
import monami.lms.response.datadtos.DisbursedLoanResponseDTO;
import monami.lms.response.datadtos.GeneralLedgerResponseDTO;
import monami.lms.response.datadtos.KYCQuestionAnswerResponseDTO;
import monami.lms.response.datadtos.KYCQuestionResponseDTO;
import monami.lms.response.datadtos.ListOfCustomerDashboardResponseDTO;
import monami.lms.response.datadtos.ListOfCustomerResponseDTO;
import monami.lms.response.datadtos.ListOfDisbursedLoansResponseDTO;
import monami.lms.response.datadtos.ListOfGeneralLedgerEntriesResponseDTO;
import monami.lms.response.datadtos.ListOfKYCQuestionAnswerResponseDTO;
import monami.lms.response.datadtos.ListOfProductResponseDTOs;
import monami.lms.response.datadtos.ListOfUnStructureDataForCustomerResponseDTO;
import monami.lms.response.datadtos.LoanApplicationIncompleteProcessResponseDTO;
import monami.lms.response.datadtos.LoanApplicationsResponseDTO;
import monami.lms.response.datadtos.LoanSummaryResponseDTO;
import monami.lms.response.datadtos.ProductResponseDTO;
import monami.lms.response.datadtos.PreferenceResponseDTO;
import monami.lms.response.datadtos.UnStrunctredDataResponseDTO;
import monami.lms.rest.serverwebinterface.GeneralLedgerService;
import monami.lms.serverutils.SMSandNotficationService;
import monami.lms.serverutils.ServerUtils;
import monami.lms.utilities.Constants;

@RestController
public class WebClientRestContollerAndroidApplication {
	Logger LOG = LoggerFactory.getLogger(WebClientRestContollerAndroidApplication.class);

	@Autowired
	@Qualifier("customUserDetailsService")
	private UserDetailsService customUserDetailsService;

	@Autowired 
	ServerUtils objServerUtils;

	@Autowired 
	private LMSDAO objLMSDAO;

	@Autowired
	private GeneralLedgerService generalLedgerService;

	@Autowired
	private GeneralLedgerAccountDAO generalLedgerDao;

	@Autowired
	private Utility utility;

	@Autowired
	private SMSandNotficationService objSMSandNotficationService;

	private BasicResponce mobileUserAuthenticate(String tokenAsHeader,String tokenAsParameter,long cellNo) {

		if( ( tokenAsHeader==null || tokenAsHeader.trim().equals("")  )
				&&
				( tokenAsParameter==null || tokenAsParameter.trim().equals("") )	
				){
			return new ResponceWithMessage(false,"No Token Provided");
		}
		String token=null;
		if(tokenAsHeader!=null && !tokenAsHeader.trim().equals("")){
			token=tokenAsHeader;
		} else {
			if(tokenAsParameter!=null && !tokenAsParameter.trim().equals("")){
				token=tokenAsParameter;
			} else {

			}

		}
		if(token==null){
			return new ResponceWithMessage(false,"No Token Provided");
		}
		if(objLMSDAO.verifyToken(cellNo, token)){
			return null;
		} else {
			return new ResponceWithMessage(false,"Invalid Token");
		}
	}


	/*@RequestMapping(method=RequestMethod.POST,value={"/Mobile/resendOTP"} )
	public BasicResponce resendOTP(@RequestParam(value = "cellNo") long cellNo) {

		if(LOG.isInfoEnabled()){
			LOG.info("WebClientRestContollerAndroidApplication.resendOTP--Start");
			LOG.info("Received Parameters are:");
			LOG.info("Cell No: {} ",new Object[]{cellNo});
		}

		BasicResponce response = null;
		try {
				LOG.info("Now We Need to Generate OTP");
				String generatedOTP = generateOTP();

				Customer objCustomer= objLMSDAO.getCustomerAgainstCellNo(cellNo);
				if(objCustomer.getStatus()==CustomerStatus.PENDING 
						|| objCustomer.getStatus()==CustomerStatus.OTP_GENERATED){
					if(objLMSDAO.updateCustomerOTPAndStatus(cellNo, generatedOTP,CustomerStatus.OTP_GENERATED)) {
						objSMSandNotficationService.sendSMSAPI(String.valueOf(cellNo),"Your OTP code is : "+generatedOTP+" --AWe2tStmnaR--");
						response=new BasicResponce(true);
						return response;
					}else {
						response=new BasicResponce(false);
						return response;
					}
				}else{
					response=new ResponceWithMessage(false,"Customer is in Invalid State");
					return response;
				}

		} catch (Exception ex) {
			response= new ResponceWithMessage(ex);
			return response;
		}finally{
			if(LOG.isInfoEnabled()){
				LOG.info("Returned Response is:");
				if(response!=null)
					LOG.info("Response Requested_Action: {} ",new Object[]{response.getRequested_Action()});
				LOG.info("WebClientRestContollerAndroidApplication.resendOTP--End");
			}
		}
	}*/
	@RequestMapping(method=RequestMethod.POST,value={"/Mobile/generateAndSendOTP"} )
	public BasicResponce generateAndSendOTP(@RequestParam(value = "cellNo") long cellNo,
			@RequestParam(value="fcmToken",required=false) String fcmToken) {

		if(LOG.isInfoEnabled()){
			LOG.info("WebClientRestContollerAndroidApplication.generateAndSendOTP--Start");
			LOG.info("Received Parameters are:");
			LOG.info("Cell No: {} ",new Object[]{cellNo});
			LOG.info("FCM Token: {} ",new Object[]{fcmToken});
		}

		BasicResponce response = null;
		try {
			Customer objCustomer= objLMSDAO.getCustomerAgainstCellNo(cellNo);

			if(objCustomer!=null){
				if(objCustomer.getStatus()!=null && (objCustomer.getStatus()==CustomerStatus.INITIATED || objCustomer.getStatus()==CustomerStatus.OTP_GENERATED_BEFORE_ACTIVATION)){
					LOG.info("Now We Need to Generate OTP");
					String generatedOTP = generateOTP();
					if(objLMSDAO.updateCustomerOTPAndStatus(cellNo, generatedOTP,CustomerStatus.OTP_GENERATED_BEFORE_ACTIVATION)) {
						if(fcmToken!=null){
							objSMSandNotficationService.sendFCMMessage(fcmToken, "Notification","An OTP Code Sent to you");
						}
						objSMSandNotficationService.sendSMSAPI(String.valueOf(cellNo),"Your OTP code is : "+generatedOTP+" --AWe2tStmnaR--");
						response=new BasicResponce(true);
						return response;
					}else {
						response=new BasicResponce(false);
						return response;
					}
				}else if(objCustomer.getStatus()!=null && (objCustomer.getStatus()==CustomerStatus.ACTIVE || objCustomer.getStatus()==CustomerStatus.OTP_GENERATED_AFTER_ACTIVATION)){
					LOG.info("Now We Need to Generate OTP");
					String generatedOTP = generateOTP();
					if(objLMSDAO.updateCustomerOTPAndStatus(cellNo, generatedOTP,CustomerStatus.OTP_GENERATED_AFTER_ACTIVATION)) {
						if(fcmToken!=null){
							objSMSandNotficationService.sendFCMMessage(fcmToken, "Notification","An OTP Code Sent to you");
						}
						objSMSandNotficationService.sendSMSAPI(String.valueOf(cellNo),"Your OTP code is : "+generatedOTP+" --AWe2tStmnaR--");
						response=new BasicResponce(true);
						return response;
					}else {
						response=new BasicResponce(false);
						return response;
					}
				}else{
					response=new ResponceWithMessage(false,"Customer is in Invalid State");
					return response;
				}
			}else{
				response=new ResponceWithMessage(false,"Customer Not Found");
				return response;
			}

		} catch (Exception ex) {
			response= new ResponceWithMessage(ex);
			return response;
		}finally{
			if(LOG.isInfoEnabled()){
				LOG.info("Returned Response is:");
				if(response!=null)
					LOG.info("Response Requested_Action: {} ",new Object[]{response.getRequested_Action()});
				LOG.info("WebClientRestContollerAndroidApplication.generateAndSendOTP--End");
			}
		}
	}

	@RequestMapping(method=RequestMethod.POST,value={"/Mobile/verifyOTP"} )
	public BasicResponce verifyOTP(@RequestParam(value = "cellNo") long cellNo,
			@RequestParam(value = "otp") String otp) {

		if(LOG.isInfoEnabled()){
			LOG.info("WebClientRestContollerAndroidApplication.verifyOTP()--Start");
			LOG.info("Received Parameters are:");
			LOG.info("Cell No: {} ",new Object[]{cellNo});
			LOG.info("Pin Code: {} ",new Object[]{otp});
		}
		BasicResponce response = null;
		try {
			Customer objCustomer= objLMSDAO.getCustomerAgainstCellNo(cellNo);

			if(objCustomer!=null){
				if(objLMSDAO.verifyOTP(cellNo,otp)){

					if(objCustomer.getStatus()==CustomerStatus.OTP_GENERATED_BEFORE_ACTIVATION){
						if(objLMSDAO.updateCustomerStatus(cellNo, CustomerStatus.OTP_VERIFIED_BEFORE_ACTIVATION,null)){
							response=new BasicResponce(true);
							return response;
						}else{
							response=new ResponceWithMessage(false,"OTP Verified But Failed to Update Customer Status to OTP_VERIFIED_BEFORE_ACTIVATION");
							return response;
						}
					}else if(objCustomer.getStatus()==CustomerStatus.OTP_GENERATED_AFTER_ACTIVATION){
						if(objLMSDAO.updateCustomerStatus(cellNo, CustomerStatus.OTP_VERIFIED_AFTER_ACTIVATION,null)){
							response=new BasicResponce(true);
							return response;
						}else{
							response=new ResponceWithMessage(false,"OTP Verified But Failed to Update Customer Status to OTP_VERIFIED_AFTER_ACTIVATION");
							return response;
						}
					}else{
						response=new ResponceWithMessage(false,"Customer is in Invalid Status");
						return response;
					}
				}else {
					response=new ResponceWithMessage(false,"Wrong OTP");
					return response;
				}
			}else{
				response=new ResponceWithMessage(false,"Customer Not Found");
				return response;
			}	

		} catch (Exception ex) {
			response=new ResponceWithMessage(ex);
			return response;
		}finally{
			if(LOG.isInfoEnabled()){
				LOG.info("Returned Response is:");
				if(response!=null){
					LOG.info("Response Requested_Action: {} ",new Object[]{response.getRequested_Action()});
				}
				LOG.info("WebClientRestContollerAndroidApplication.verifyOTP()--End");
			}
		}
	}


	/*@RequestMapping(method=RequestMethod.POST,value={"/Mobile/SetPinCode"} )
	public BasicResponce SetPinCode(@RequestParam(value = "cellNo") long cellNo,
			@RequestParam(value = "pinCode") String pinCode) {

		if(LOG.isInfoEnabled()){
			LOG.info("WebClientRestContollerAndroidApplication.SetPinCode()--Start");
			LOG.info("Received Parameters are:");
			LOG.info("Cell No: {} ",new Object[]{cellNo});
			LOG.info("Pin Code: {} ",new Object[]{pinCode});
		}
		BasicResponce response = null;
		try {
			Customer objCustomer= objLMSDAO.getCustomerAgainstCellNo(cellNo);
			if(objCustomer!=null){
				if(objCustomer.getStatus()==CustomerStatus.OTP_VERIFIED_BEFORE_ACTIVATION && objCustomer.getPinCode()==null){

					Preferences pref= objLMSDAO.getPreferenceValueUsingName("secretKey");
					String encryptedPinCode= AES.encrypt(pinCode, pref.getPreferenceValue());
					if(encryptedPinCode!=null){
						int result=objLMSDAO.updatePinCodeForCustomer(cellNo,encryptedPinCode);
						if(result>0){
							if(objLMSDAO.updateCustomerStatus(cellNo, CustomerStatus.ACTIVE,null)){
								response=new BasicResponce(true);
								return response;
							}else{
								response=new ResponceWithMessage(false,"Customer PIN Code Successfully Updated But Failed to Update Customer Status");
								return response;
							}
						} else if (result==-5){
							response=new ResponceWithMessage(false,"Unable to find cellNo");
							return response;
						} else {
							response=new ResponceWithMessage(false,"Unkown Error");
							return response;
						}
					}else{
						response=new ResponceWithMessage(false,"Failed to Encrypt Credentials");
						return response;
					}

				}else if(objCustomer.getStatus()==CustomerStatus.OTP_VERIFIED_AFTER_ACTIVATION && objCustomer.getPinCode()!=null){

					if(!objCustomer.getPinCode().equalsIgnoreCase(pinCode)){
						int result=objLMSDAO.updatePinCodeForCustomer(cellNo,pinCode);
						if(result>0){
							if(objLMSDAO.updateCustomerStatus(cellNo, CustomerStatus.ACTIVE,null)){
								response=new BasicResponce(true);
								return response;
							}else{
								response=new ResponceWithMessage(false,"Customer PIN Code Successfully Updated But Failed to Update Customer Status");
								return response;
							}
						} else if (result==-5){
							response=new ResponceWithMessage(false,"Unable to find cellNo");
							return response;
						} else {
							response=new ResponceWithMessage(false,"Unkown Error");
							return response;
						}
					}else{
						response=new ResponceWithMessage(false,"New Pin Code Must be Different From Last One");
						return response;
					}
				}else{
					response=new ResponceWithMessage(false,"Customer is in Invalid State, Unable to Proceed");
					return response;
				}
			}else{
				response=new ResponceWithMessage(false,"Customer Not Found");
				return response;
			}
		} catch (Exception ex) {
			response=new ResponceWithMessage(ex);
			return response;
		}finally{
			if(LOG.isInfoEnabled()){
				LOG.info("Returned Response is:");
				if(response!=null){
					LOG.info("Response Requested_Action: {} ",new Object[]{response.getRequested_Action()});
				}
				LOG.info("WebClientRestContollerAndroidApplication.SetPinCode()--End");
			}
		}
	}*/
	
	@RequestMapping(method=RequestMethod.POST,value={"/Mobile/SetPinCode"} )
	public BasicResponce SetPinCode(@RequestParam(value = "cellNo") long cellNo,
			@RequestParam(value = "pinCode") String pinCode) {

		if(LOG.isInfoEnabled()){
			LOG.info("WebClientRestContollerAndroidApplication.SetPinCode()--Start");
			LOG.info("Received Parameters are:");
			LOG.info("Cell No: {} ",new Object[]{cellNo});
			LOG.info("Pin Code: {} ",new Object[]{pinCode});
		}
		BasicResponce response = null;
		try {
			Customer objCustomer= objLMSDAO.getCustomerAgainstCellNo(cellNo);
			if(objCustomer!=null){
				if(objCustomer.getStatus()==CustomerStatus.OTP_VERIFIED_BEFORE_ACTIVATION && objCustomer.getPinCode()==null){
					
					Preferences pref= objLMSDAO.getPreferenceValueUsingName("secretKey");
					if(pref!=null){
						String encryptedPinCode= AES.encrypt(pinCode, pref.getPreferenceValue());
						if(encryptedPinCode!=null){
							int result=objLMSDAO.updatePinCodeForCustomer(cellNo,encryptedPinCode);
							if(result>0){
								if(objLMSDAO.updateCustomerStatus(cellNo, CustomerStatus.ACTIVE)){
									response=new BasicResponce(true);
									return response;
								}else{
									response=new ResponceWithMessage(false,"Customer PIN Code Successfully Updated But Failed to Update Customer Status");
									return response;
								}
							} else if (result==-5){
								response=new ResponceWithMessage(false,"Unable to find cellNo");
								return response;
							} else {
								response=new ResponceWithMessage(false,"Unkown Error");
								return response;
							}
						}else{
							response=new ResponceWithMessage(false,"Failed to Encrypt Credentials");
							return response;
						}
					}else{
						response=new ResponceWithMessage(false,"Failed to get Preference Value With Preference Name: secretKey");
						return response;
					}
				}else if(objCustomer.getStatus()==CustomerStatus.OTP_VERIFIED_AFTER_ACTIVATION && objCustomer.getPinCode()!=null){
					
					if(!objCustomer.getPinCode().equalsIgnoreCase(pinCode)){
						int result=objLMSDAO.updatePinCodeForCustomer(cellNo,pinCode);
						if(result>0){
							if(objLMSDAO.updateCustomerStatus(cellNo, CustomerStatus.ACTIVE)){
								response=new BasicResponce(true);
								return response;
							}else{
								response=new ResponceWithMessage(false,"Customer PIN Code Successfully Updated But Failed to Update Customer Status");
								return response;
							}
						} else if (result==-5){
							response=new ResponceWithMessage(false,"Unable to find cellNo");
							return response;
						} else {
							response=new ResponceWithMessage(false,"Unkown Error");
							return response;
						}
					}else{
						response=new ResponceWithMessage(false,"New Pin Code Must be Different From Last One");
						return response;
					}
				}else{
					response=new ResponceWithMessage(false,"Customer is in Invalid State, Unable to Proceed");
					return response;
				}
			}else{
				response=new ResponceWithMessage(false,"Customer Not Found");
				return response;
			}
		} catch (Exception ex) {
			response=new ResponceWithMessage(ex);
			return response;
		}finally{
			if(LOG.isInfoEnabled()){
				LOG.info("Returned Response is:");
				if(response!=null){
					LOG.info("Response Requested_Action: {} ",new Object[]{response.getRequested_Action()});
				}
				LOG.info("WebClientRestContollerAndroidApplication.SetPinCode()--End");
			}
		}
	}



	/*@RequestMapping(method=RequestMethod.POST,value={"/Mobile/changePinCode"} )
	public BasicResponce changePinCode(@RequestParam(value = "cellNo") long cellNo,
			@RequestParam(value = "oldPinCode") String oldPinCode,
			@RequestParam(value = "newPinCode") String newPinCode,
			@RequestParam(value = "confirmNewPinCode") String confirmNewPinCode) {

		if(LOG.isInfoEnabled()){
			LOG.info("WebClientRestContollerAndroidApplication.changePinCode()--Start");
			LOG.info("Received Parameters are:");
			LOG.info("Cell No: {} ",new Object[]{cellNo});
			LOG.info("Old Pin Code: {} ",new Object[]{oldPinCode});
			LOG.info("New Pin Code: {} ",new Object[]{newPinCode});
			LOG.info("Confirm Pin Code: {} ",new Object[]{confirmNewPinCode});
		}
		BasicResponce response = null;
		try {

			if(oldPinCode!=null && newPinCode!=null && confirmNewPinCode!=null){
				if(newPinCode.equalsIgnoreCase(confirmNewPinCode) && newPinCode.matches("[0-9]+") && newPinCode.length()==4){
					if(!newPinCode.equalsIgnoreCase(oldPinCode)){
						Customer objCustomer= objLMSDAO.getCustomerAgainstCellNo(cellNo);

						if(objCustomer!=null && objCustomer.getStatus()==CustomerStatus.ACTIVE){
							Preferences pref= objLMSDAO.getPreferenceValueUsingName("secretKey");

							if(pref==null){
								response=new ResponceWithMessage(true,"Failed to Get SecretKey to Decrypt PINCODE");
								return response;
							}

							if(objCustomer.getPinCode()!=null){
								String decryptedPinCode= objCustomer.getPinCode();
								if(decryptedPinCode!=null){
									if(decryptedPinCode.equals(oldPinCode)){
										String encryptedPinCode= newPinCode;
										if(encryptedPinCode!=null){
											int result=objLMSDAO.updatePinCodeForCustomer(cellNo,encryptedPinCode);
											if(result>0){
												response=new ResponceWithMessage(true,"Successfully Changed PinCode");
												return response;
											} else if (result==-5){
												response=new ResponceWithMessage(false,"Unable to find cellNo");
												return response;
											} else {
												response=new ResponceWithMessage(false,"Unkown Error");
												return response;
											}
										}else{
											response=new ResponceWithMessage(false,"Failed to Encrypt Credentials");
											return response;
										}
									}else{
										response=new ResponceWithMessage(false,"Wrong Pin Code");
										return response;
									}
								}else{
									response=new ResponceWithMessage(false,"Failed To Decrypt Credentials");
									return response;
								}
							}else{
								response=new ResponceWithMessage(false,"PinCode Not Found");
								return response;
							}

						}else{
							response=new ResponceWithMessage(false,"Customer Not Found or Customer is in Invalid State");
							return response;
						}
					}else{
						response=new ResponceWithMessage(false,"New PinCode and Old PinCode Must be Different");
						return response;
					}
				}else{
					response=new ResponceWithMessage(false,"New PinCode and Confirm New PinCode Must be Same, Must be Digits Having Correct Length");
					return response;
				}
			}else{
				response=new ResponceWithMessage(false,"Bad Request");
				return response;
			}

		} catch (Exception ex) {
			response=new ResponceWithMessage(ex);
			return response;
		}finally{
			if(LOG.isInfoEnabled()){
				LOG.info("Returned Response is:");
				if(response!=null){
					LOG.info("Response Requested_Action: {} ",new Object[]{response.getRequested_Action()});
				}
				LOG.info("WebClientRestContollerAndroidApplication.changePinCode()--End");
			}
		}
	}*/
	
	@RequestMapping(method=RequestMethod.POST,value={"/Mobile/changePinCode"} )
	public BasicResponce changePinCode(@RequestParam(value = "cellNo") long cellNo,
			@RequestParam(value = "oldPinCode") String oldPinCode,
			@RequestParam(value = "newPinCode") String newPinCode,
			@RequestParam(value = "confirmNewPinCode") String confirmNewPinCode) {

		if(LOG.isInfoEnabled()){
			LOG.info("WebClientRestContollerAndroidApplication.changePinCode()--Start");
			LOG.info("Received Parameters are:");
			LOG.info("Cell No: {} ",new Object[]{cellNo});
			LOG.info("Old Pin Code: {} ",new Object[]{oldPinCode});
			LOG.info("New Pin Code: {} ",new Object[]{newPinCode});
			LOG.info("Confirm Pin Code: {} ",new Object[]{confirmNewPinCode});
		}
		BasicResponce response = null;
		try {
			
			if(oldPinCode!=null && newPinCode!=null && confirmNewPinCode!=null){
				if(newPinCode.equalsIgnoreCase(confirmNewPinCode) && newPinCode.matches("[0-9]+") && newPinCode.length()==4){
					if(!newPinCode.equalsIgnoreCase(oldPinCode)){
						Customer objCustomer= objLMSDAO.getCustomerAgainstCellNo(cellNo);
						
						if(objCustomer!=null && objCustomer.getStatus()==CustomerStatus.ACTIVE){
							Preferences pref= objLMSDAO.getPreferenceValueUsingName("secretKey");
							
							if(pref==null){
								response=new ResponceWithMessage(true,"Failed to Get SecretKey to Decrypt PINCODE");
								return response;
							}
							
							if(objCustomer.getPinCode()!=null){
								String decryptedPinCode= AES.decrypt(objCustomer.getPinCode(),pref.getPreferenceValue());
								if(decryptedPinCode!=null){
									if(decryptedPinCode.equals(oldPinCode)){
										String encryptedPinCode= AES.encrypt(newPinCode,pref.getPreferenceValue());
										if(encryptedPinCode!=null){
											int result=objLMSDAO.updatePinCodeForCustomer(cellNo,encryptedPinCode);
											if(result>0){
													response=new ResponceWithMessage(true,"Successfully Changed PinCode");
													return response;
											} else if (result==-5){
												response=new ResponceWithMessage(false,"Unable to find cellNo");
												return response;
											} else {
												response=new ResponceWithMessage(false,"Unkown Error");
												return response;
											}
										}else{
											response=new ResponceWithMessage(false,"Failed to Encrypt Credentials");
											return response;
										}
									}else{
										response=new ResponceWithMessage(false,"Wrong Pin Code");
										return response;
									}
								}else{
									response=new ResponceWithMessage(false,"Failed To Decrypt Credentials");
									return response;
								}
							}else{
								response=new ResponceWithMessage(false,"PinCode Not Found");
								return response;
							}

						}else{
							response=new ResponceWithMessage(false,"Customer Not Found or Customer is in Invalid State");
							return response;
						}
					}else{
						response=new ResponceWithMessage(false,"New PinCode and Old PinCode Must be Different");
						return response;
					}
				}else{
					response=new ResponceWithMessage(false,"New PinCode and Confirm New PinCode Must be Same, Must be Digits Having Correct Length");
					return response;
				}
			}else{
				response=new ResponceWithMessage(false,"Bad Request");
				return response;
			}

		} catch (Exception ex) {
			response=new ResponceWithMessage(ex);
			return response;
		}finally{
			if(LOG.isInfoEnabled()){
				LOG.info("Returned Response is:");
				if(response!=null){
					LOG.info("Response Requested_Action: {} ",new Object[]{response.getRequested_Action()});
				}
				LOG.info("WebClientRestContollerAndroidApplication.changePinCode()--End");
			}
		}
	}


	@RequestMapping(method=RequestMethod.POST,value={"/Mobile/CustomerLogin"} )
	public BasicResponce CustomerLogin(@RequestParam(value = "cellNo") long cellNo,
			@RequestParam(value = "pinCode") String pinCode) {

		if(LOG.isInfoEnabled()){
			LOG.info("WebClientRestContollerAndroidApplication.CustomerLogin()--Start");
			LOG.info("Received Parameters are:");
			LOG.info("Cell No: {} ",new Object[]{cellNo});
			LOG.info("Pin Code: {} ",new Object[]{pinCode});
		}
		BasicResponce response = null;


		try {
			String result=objLMSDAO.loginWithPinCodeForCustomer(cellNo,pinCode);

			if(result!=null){
				response=new ResponceWithCustomerToken(true,result);
				return response;
			}else {
				response=new ResponceWithMessage(false,"Unknown Error");
				return response;
			} 

		} catch (Exception ex) {
			response=new ResponceWithMessage(false,ex.getMessage());
			return response;

		}finally{
			if(LOG.isInfoEnabled()){
				LOG.info("Returned Response is:");
				if(response!=null){
					LOG.info("Response Requested_Action: {} ",new Object[]{response.getRequested_Action()});
				}
				LOG.info("WebClientRestContollerAndroidApplication.CustomerLogin()--End");
			}
		}
	}

	/*private String sendSMSAPI(String to,String message){
		final String hostname="test99095api.eocean.us";
		final String port="24555";
		final String userID="test_99095";
		final String password="Tt845162";
		final String sender="99095";
		//http://test99095api.eocean.us:24555/api?action=sendmessage&username=test_99095&password=DSCXVFRMN&recipient=923472980857&originator=99095&messagedata=Test123.

		String uri="http://"+hostname+":"+port+"/api?action=sendmessage&username="+userID+"&password="+password+"&recipient="+to+"&originator="+sender+"&messagedata="+message;

		RestTemplate restTemplate = new RestTemplate();
		String result = restTemplate.getForObject(uri, String.class);
		return result;
	}*/

	/*@RequestMapping(method=RequestMethod.POST,value="/Mobile/RegisterNewCustomer")
	public BasicResponce RegisterNewCustomer(@RequestParam(value = "pakistaniCNIC") long pakistaniCNIC, 
			@RequestParam(value = "cellNo") long cellNo) {

		if(LOG.isInfoEnabled()){
			LOG.info("WebClientRestContollerAndroidApplication.RegisterNewCustomer--Start");
			LOG.info("Received Parameters are:");
			LOG.info("CNIC: {} ",new Object[]{pakistaniCNIC});
			LOG.info("Cell No: {} ",new Object[]{cellNo});
		}

		BasicResponce response = null;
		try {

			Preferences pref= objLMSDAO.getPreferenceValueUsingName("filePathOneLoad");

			if(pref!=null){
				LOG.info("One Load File Path Pref Name: {} ",new Object[]{pref.getPreferenceName()});
				LOG.info("One Load File Path Pref Value: {} ",new Object[]{pref.getPreferenceValue()});
				File file = new File(pref.getPreferenceValue());

				//Here we Need to Check Customer in OneLoad File
				String targetFileString= FileUtils.readFileToString(file);

				Boolean oneLoadCustomerFlagStatus;
				if(targetFileString!=null){
					oneLoadCustomerFlagStatus=targetFileString.contains(String.valueOf(cellNo).replaceFirst("03", "3"));
				}else{
					response= new ResponceWithMessage(false,"Unable to Read Target File");
					return response;
				}

				LOG.info("One Load Status: {} ",new Object[]{oneLoadCustomerFlagStatus});

				int result=objLMSDAO.addNewCustomer(pakistaniCNIC, cellNo,oneLoadCustomerFlagStatus);
				if(result>0) {
					Random r=new Random();
					int otp=10000+r.nextInt()%20000;
					sendSMSAPI(cellNo+"","Monami Login OPT : "+otp);
					response= new BasicResponce(true);
					return response;
				} else if(result==-2) {
					response= new ResponceWithMessage(false,"pakistaniCNIC & cellNo must be unique.");
					return response;
				} else {
					response= new ResponceWithMessage(false,"Some Constraint Failed");
					return response;
				}
			}else{
				response= new ResponceWithMessage(false,"No Preference Value Found for One Load File Path");
				return response;
			}

		} catch (Exception ex) {
			response= new ResponceWithMessage(ex);
			return response;
		}finally{
			if(LOG.isInfoEnabled()){
				LOG.info("Returned Response is:");
				if(response!=null)
					LOG.info("Response Requested_Action: {} ",new Object[]{response.getRequested_Action()});
				LOG.info("WebClientRestContollerAndroidApplication.RegisterNewCustomer--End");
			}
		}
	}*/

	@RequestMapping(method=RequestMethod.POST,value="/Mobile/UpdateFCMToken")
	public BasicResponce UpdateFCMToken(@RequestParam(value = "cellNo") long cellNo,
			@RequestParam(value="fcmToken",required=false) String fcmToken ) {



		BasicResponce response = null;


		try {
			int result=objLMSDAO.updateFCMTokenForCustomer(cellNo,fcmToken);
			if(result==1){

				response=new ResponceWithMessage(false,"Customer FCM Token Successfully Updated");
				return response;

			} else if (result==-5){
				response=new ResponceWithMessage(false,"Unable to find cellNo");
				return response;
			} else {
				response=new ResponceWithMessage(false,"Unkown Error");
				return response;
			}

		} catch (Exception ex) {
			response=new ResponceWithMessage(ex);
			return response;

		}finally{

		}


	}

	@RequestMapping(method=RequestMethod.POST,value={"/Mobile/saveCustomersUnStructuredData"} )
	public ResponceDTO<Object> saveCustomersUnStructuredData(@RequestParam(value = "Token") String Token,
			@RequestParam(value = "dataStartDate") String dataStartDate,
			@RequestParam(value = "dateEndData") String dateEndData,
			@RequestParam(value = "jsonData") String jsonData) {	
		ResponceDTO<Object> response = null;

		try {
			Customer c=objLMSDAO.loginInfoByToken(Token);			
			if(c==null){
				response=new ResponceDTO<Object>(false,"Unable to find session token");
				return response;
			} else {
				objLMSDAO.addCustomerUnStructuredData(c,dataStartDate,dateEndData,jsonData);
				response=new ResponceDTO<Object>(true,"Data Saved");
				return response;
			} 

		} catch (Exception ex) {
			response=new ResponceDTO<Object>(false,"Unknown Exception");
			return response;
		}finally{

		}
	}

	@RequestMapping(method=RequestMethod.POST,value={"/Mobile/getCustomersUnStructuredData"} )
	public BasicResponce getCustomersUnStructuredData(@RequestParam(value = "Token") String Token) {	

		try {
			Customer c=objLMSDAO.loginInfoByToken(Token);			
			if(c==null){
				ResponceDTO<Object> response=new ResponceDTO<Object>(false,"Unable to find session token");
				return response;
			} else {
				List<CustomerUnStructuredData> objList=objLMSDAO.getCustomerUnStructuredDataByCustomer(c);

				List<UnStrunctredDataResponseDTO> toSend=new ArrayList<UnStrunctredDataResponseDTO>();

				for(CustomerUnStructuredData iter:objList) {
					UnStrunctredDataResponseDTO temp=new UnStrunctredDataResponseDTO();
					temp.setEndData(iter.getEndDate());
					temp.setStartDate(iter.getStartDate());
					temp.setJsonData(iter.getJsonData());
					toSend.add(temp);
				}

				ListOfUnStructureDataForCustomerResponseDTO response= new ListOfUnStructureDataForCustomerResponseDTO(toSend);
				return response;
			} 

		} catch (Exception ex) {
			ResponceDTO<Object> response=new ResponceDTO<Object>(false,"Unknown Exception");
			return response;
		}finally{

		}
	}



	/*@RequestMapping(method=RequestMethod.POST,value="/Mobile/RegisterNewCustomer")
	public BasicResponce RegisterNewCustomer(@RequestParam(value = "pakistaniCNIC") long pakistaniCNIC, 
			@RequestParam(value = "cellNo") long cellNo,
			@RequestParam(value="fcmToken",required=false) String fcmToken ) {

		if(LOG.isInfoEnabled()){
			LOG.info("WebClientRestContollerAndroidApplication.RegisterNewCustomer--Start");
			LOG.info("Received Parameters are:");
			LOG.info("CNIC: {} ",new Object[]{pakistaniCNIC});
			LOG.info("Cell No: {} ",new Object[]{cellNo});
		}

		BasicResponce response = null;
		try {

			Preferences pref= objLMSDAO.getPreferenceValueUsingName("filePathOneLoad");

			if(pref!=null){
				LOG.info("One Load File Path Pref Name: {} ",new Object[]{pref.getPreferenceName()});
				LOG.info("One Load File Path Pref Value: {} ",new Object[]{pref.getPreferenceValue()});
				File file = new File(pref.getPreferenceValue());

				//Here we Need to Check Customer in OneLoad File
				String targetFileString= FileUtils.readFileToString(file);
				Boolean oneLoadCustomerFlagStatus;
				if(targetFileString!=null){
					oneLoadCustomerFlagStatus=targetFileString.contains(String.valueOf(cellNo).replaceFirst("03", "3"));
				}else{
					response= new ResponceWithMessage(false,"Unable to Read Target File");
					return response;
				}

				LOG.info("One Load Status: {} ",new Object[]{oneLoadCustomerFlagStatus});

				LOG.info("Now We Need to Generate OTP");
				String generatedOTP = generateOTP();
				int result=objLMSDAO.addNewCustomer(pakistaniCNIC, cellNo, oneLoadCustomerFlagStatus, generatedOTP, CustomerStatus.OTP_GENERATED_BEFORE_ACTIVATION,fcmToken);

				Customer objCustomer = objLMSDAO.getCustomerAgainstCellNo(cellNo);
				List<CustomerResponseDTO> toSend=new ArrayList<CustomerResponseDTO>();
				CustomerResponseDTO toAdd=new CustomerResponseDTO();
				toAdd.setId(objCustomer.getId());
				toAdd.setStatus(objCustomer.getStatus());
				toAdd.setCellNo(objCustomer.getCellNoInString());
				toAdd.setCnic(objCustomer.getPakistaniCNIC());
				toAdd.setFullName(objCustomer.getFullName());
				toSend.add(toAdd);

				if(result>0) {
					objSMSandNotficationService.sendFCMMessage(fcmToken, "Notification","An OTP code was sent to you");
					objSMSandNotficationService.sendSMSAPI(String.valueOf(cellNo),"Your OTP code is : "+generatedOTP+" --AWe2tStmnaR--");

					response= new ListOfCustomerResponseDTO(toSend);
					response.setRequested_Action(true);
					return response;
				} else if(result==-2) {
					response= new ListOfCustomerResponseDTO(toSend);
					response.setRequested_Action(false);
					return response;
				} else {
					response= new ListOfCustomerResponseDTO(toSend);
					response.setRequested_Action(false);
					return response;
				}
			}else{
				response= new ResponceWithMessage(false,"No Preference Value Found for One Load File Path");
				return response;
			}

		} catch (Exception ex) {
			response= new ResponceWithMessage(ex);
			return response;
		}finally{
			if(LOG.isInfoEnabled()){
				LOG.info("Returned Response is:");
				if(response!=null)
					LOG.info("Response Requested_Action: {} ",new Object[]{response.getRequested_Action()});
				LOG.info("WebClientRestContollerAndroidApplication.RegisterNewCustomer--End");
			}
		}
	}*/
	@RequestMapping(method=RequestMethod.POST,value="/Mobile/RegisterNewCustomer")
	public BasicResponce RegisterNewCustomer(@RequestParam(value = "pakistaniCNIC") long pakistaniCNIC, 
			@RequestParam(value = "cellNo") long cellNo,
			@RequestParam(value="fcmToken",required=false) String fcmToken ) {

		if(LOG.isInfoEnabled()){
			LOG.info("WebClientRestContollerAndroidApplication.RegisterNewCustomer--Start");
			LOG.info("Received Parameters are:");
			LOG.info("CNIC: {} ",new Object[]{pakistaniCNIC});
			LOG.info("Cell No: {} ",new Object[]{cellNo});
		}

		BasicResponce response = null;
		try {

			if(!String.valueOf(pakistaniCNIC).matches("^([0-9]{13})$")){
				response = new ResponceWithMessage(false, "Invalid CNIC");
				return response;
			}else if(!(String.valueOf(cellNo).startsWith("3"))){
				response = new ResponceWithMessage(false, "Invalid Cell Number Format");
				return response;
			}else{
				if(!String.valueOf(cellNo).matches("^([0-9]{10})$")){
					response = new ResponceWithMessage(false, "Invalid Cell Number Lenght");
					return response;
				}
			}


			Preferences pref= objLMSDAO.getPreferenceValueUsingName("filePathOneLoad");

			if(pref!=null){
				LOG.info("One Load File Path Pref Name: {} ",new Object[]{pref.getPreferenceName()});
				LOG.info("One Load File Path Pref Value: {} ",new Object[]{pref.getPreferenceValue()});
				File file = new File(pref.getPreferenceValue());

				//Here we Need to Check Customer in OneLoad File
				String targetFileString= FileUtils.readFileToString(file);
				Boolean oneLoadCustomerFlagStatus;
				if(targetFileString!=null){
					oneLoadCustomerFlagStatus=targetFileString.contains(String.valueOf(cellNo).replaceFirst("03", "3"));
				}else{
					response= new ResponceWithMessage(false,"Unable to Read Target File");
					return response;
				}

				LOG.info("One Load Status: {} ",new Object[]{oneLoadCustomerFlagStatus});

				int result=objLMSDAO.addNewCustomer(pakistaniCNIC, cellNo, oneLoadCustomerFlagStatus, CustomerStatus.INITIATED,fcmToken);

				Customer objCustomer = objLMSDAO.getCustomerAgainstCellNo(cellNo);
				List<CustomerResponseDTO> toSend=new ArrayList<CustomerResponseDTO>();
				CustomerResponseDTO toAdd=new CustomerResponseDTO();
				toAdd.setId(objCustomer.getId());
				toAdd.setStatus(objCustomer.getStatus());
				toAdd.setCellNo(objCustomer.getCellNoInString());
				toAdd.setCnic(objCustomer.getPakistaniCNIC());
				toAdd.setFullName(objCustomer.getFullName());
				toAdd.setFcmToken(objCustomer.getFcmToken());
				toSend.add(toAdd);

				if(result>0) {
					//					objSMSandNotficationService.sendFCMMessage(fcmToken, "Notification","An OTP code was sent to you");
					//					objSMSandNotficationService.sendSMSAPI(String.valueOf(cellNo),"Your OTP code is : "+generatedOTP+" --AWe2tStmnaR--");

					response= new ListOfCustomerResponseDTO(toSend);
					response.setRequested_Action(true);
					return response;
				} else if(result==-2) {
					response= new ListOfCustomerResponseDTO(toSend);
					response.setRequested_Action(false);
					return response;
				} else {
					response= new ListOfCustomerResponseDTO(toSend);
					response.setRequested_Action(false);
					return response;
				}
			}else{
				response= new ResponceWithMessage(false,"No Preference Value Found for One Load File Path");
				return response;
			}

		} catch (Exception ex) {
			response= new ResponceWithMessage(ex);
			return response;
		}finally{
			if(LOG.isInfoEnabled()){
				LOG.info("Returned Response is:");
				if(response!=null)
					LOG.info("Response Requested_Action: {} ",new Object[]{response.getRequested_Action()});
				LOG.info("WebClientRestContollerAndroidApplication.RegisterNewCustomer--End");
			}
		}
	}

	public static String generateOTP(){  //int randomPin declared to store the otp 
		//since we using Math.random() hence we have to type cast it int 
		//because Math.random() returns decimal value 
		int randomPin   =(int) (Math.random()*9000)+1000; 
		String otp  = String.valueOf(randomPin); 
		return otp; //returning value of otp 
	} 






	@RequestMapping(method=RequestMethod.POST,value="/Mobile/getCustomerByIdentification")
	public BasicResponce getCustomerByIdentification(@RequestParam(value = "identificationType") long identificationType,
			@RequestParam(value = "identificationValue") long identificationValue) {

		if(LOG.isInfoEnabled()){
			LOG.info("WebClientRestContollerAndroidApplication.getCustomerByIdentification--Start");
			LOG.info("Received Parameters are:");
			LOG.info("Identification Type: {} ",new Object[]{identificationType});
			LOG.info("Identification Value: {} ",new Object[]{identificationValue});
		}

		BasicResponce response = null;
		Customer customer = null;
		try {
			if(identificationType==Constants.IDENTIFICATION_TYPE_CELL_NO){
				customer=objLMSDAO.getCustomerAgainstCellNo(identificationValue);
			}else{
				if(identificationType==Constants.IDENTIFICATION_TYPE_CNIC){
					customer=objLMSDAO.getCustomerAgainstCnic(identificationValue);
				}
			}

			if(customer!=null){
				CustomerResponseDTO objCustomerDTO=new CustomerResponseDTO(customer.getId(),customer.getCellNoInString(),customer.getFullName(),customer.getPakistaniCNIC(),customer.getStatus(),customer.getPinCode());

				response=new ResponceDTO<CustomerResponseDTO>(true,objCustomerDTO);
				return response;
			}else{
				response=new ResponceDTO<String>(false,"Customer Not Found");
				return response;
			}

		} catch (Exception ex) {
			response= new ResponceWithMessage(ex);
			return response;
		}finally{
			if(LOG.isInfoEnabled()){
				LOG.info("Returned Response is:");
				if(response!=null)
					LOG.info("Response Requested_Action: {} ",new Object[]{response.getRequested_Action()});
				LOG.info("WebClientRestContollerAndroidApplication.getCustomerByIdentification--End");
			}
		}
	}




	@RequestMapping(method=RequestMethod.POST,value="/Mobile/isCustomerEligibleForNewLoan")
	public BasicResponce isCustomerEligibleForNewLoan(@RequestParam(value = "identificationType") long identificationType,
			@RequestParam(value = "identificationValue") long identificationValue) {

		if(LOG.isInfoEnabled()){
			LOG.info("WebClientRestContollerAndroidApplication.isCustomerEligibleForNewLoan--Start");
			LOG.info("Received Parameters are:");
			LOG.info("Identification Type: {} ",new Object[]{identificationType});
			LOG.info("Identification Value: {} ",new Object[]{identificationValue});
		}

		BasicResponce authResp = null;
		Customer customer = null;
		try {
			if(identificationType==Constants.IDENTIFICATION_TYPE_CELL_NO){
				customer=objLMSDAO.getCustomerAgainstCellNo(identificationValue);
			}else{
				if(identificationType==Constants.IDENTIFICATION_TYPE_CNIC){
					customer=objLMSDAO.getCustomerAgainstCnic(identificationValue);
				}
			}

			if(customer!=null){
				List<LoanApplication> loanApplicationList=objLMSDAO.getAllNonCompletedDisbursedLoanApplicationsForCustomer(customer.getId());

				if(loanApplicationList!=null && loanApplicationList.size()>0){
					authResp=new ResponceWithMessage(false,"Already have Active Loan, Customer is Not Eligible for New Loan Application");
					return authResp;
				}else{
					authResp=new ResponceWithMessage(true,"No Active Loan, Customer is Eligible for New Loan Application");
					return authResp;
				}
			}else{
				authResp=new ResponceWithMessage(false,"Customer Not Found");
				return authResp;
			}


		} catch (Exception ex) {
			authResp= new ResponceWithMessage(ex);
			return authResp;
		}finally{
			if(LOG.isInfoEnabled()){
				LOG.info("Returned Response is:");
				if(authResp!=null)
					LOG.info("Response Requested_Action: {} ",new Object[]{authResp.getRequested_Action()});
				LOG.info("WebClientRestContollerAndroidApplication.isCustomerEligibleForNewLoan--End");
			}
		}
	}

	@RequestMapping(method=RequestMethod.POST,value={"/Mobile/AddUpdateKYCAnswer"} )
	public BasicResponce AddUpdateKYCAnswer(@RequestHeader(value="tokenAsHeader", required = false) String tokenAsHeader,
			@RequestParam(value = "tokenAsParameter", required = false) String tokenAsParameter,
			@RequestParam(value = "cellNo", required = true) long cellNo,
			@RequestBody List<KYCAnswerRequestDTO> kycAnswerArray) {

		if(LOG.isInfoEnabled()){
			LOG.info("WebClientRestContollerAndroidApplication.AddUpdateKYCAnswer()--Start");
			LOG.info("Received Parameters are:");
			LOG.info("Token Header: {} ",new Object[]{tokenAsHeader});
			LOG.info("Token As Header: {} ",new Object[]{tokenAsParameter});
			LOG.info("Cell No: {} ",new Object[]{cellNo});
			LOG.info("Kyc Answer Array: {} ",new Object[]{kycAnswerArray});
		}
		BasicResponce response = null;

		BasicResponce authResp=mobileUserAuthenticate(tokenAsHeader,tokenAsParameter,cellNo);		
		if(authResp!=null) {
			return authResp;
		}
		try {

			Customer objCustomer=objLMSDAO.getCustomerAgainstCellNo(cellNo);
			if(objCustomer!=null) {

				objLMSDAO.addNewKYCAnswer(objCustomer,kycAnswerArray,objLMSDAO.getApplicationUserByToken(tokenAsParameter));

				response=new BasicResponce(true);
				return response;
			}

			response=new BasicResponce(false);
			return response;
		} catch (Exception ex){
			response=new ResponceWithMessage(ex);
			return response;
		}finally{
			if(LOG.isInfoEnabled()){
				LOG.info("Returned Response is:");
				if(response!=null){
					LOG.info("Response Requested_Action: {} ",new Object[]{response.getRequested_Action()});
				}
				LOG.info("WebClientRestContollerAndroidApplication.AddUpdateKYCAnswer()--End");
			}
		}
	}
	
	@RequestMapping(method=RequestMethod.POST,value={"/Mobile/AddCustomerKYCAnswerAgainstSpecificProduct"} )
	public BasicResponce AddCustomerKYCAnswerAgainstSpecificProduct(@RequestHeader(value="tokenAsHeader", required = false) String tokenAsHeader,
			@RequestParam(value = "tokenAsParameter", required = false) String tokenAsParameter,
			@RequestParam(value = "cellNo", required = true) long cellNo,
			@RequestParam(value = "productId", required = true) int productId,
			@RequestBody List<KYCAnswerRequestDTO> kycAnswerArray) {

		if(LOG.isInfoEnabled()){
			LOG.info("WebClientRestContollerAndroidApplication.AddCustomerKYCAnswerAgainstSpecificProduct()--Start");
			LOG.info("Received Parameters are:");
			LOG.info("Token Header: {} ",new Object[]{tokenAsHeader});
			LOG.info("Token As Header: {} ",new Object[]{tokenAsParameter});
			LOG.info("Cell No: {} ",new Object[]{cellNo});
			LOG.info("Product Id: {} ",new Object[]{productId});
			LOG.info("Kyc Answer Array: {} ",new Object[]{kycAnswerArray});
		}
		BasicResponce response = null;

		BasicResponce authResp=mobileUserAuthenticate(tokenAsHeader,tokenAsParameter,cellNo);		
		if(authResp!=null) {
			return authResp;
		}
		try {

			Customer objCustomer=objLMSDAO.getCustomerAgainstCellNo(cellNo);
			
			Product objProduct= objLMSDAO.getProduct(productId);
			
			if(objCustomer!=null && objProduct!=null) {

				objLMSDAO.addNewKYCAnswerAgainstSpecificProduct(objCustomer,objProduct,kycAnswerArray,objLMSDAO.getApplicationUserByToken(tokenAsParameter));

				response=new BasicResponce(true);
				return response;
			}

			response=new BasicResponce(false);
			return response;
		} catch (Exception ex){
			response=new ResponceWithMessage(ex);
			return response;
		}finally{
			if(LOG.isInfoEnabled()){
				LOG.info("Returned Response is:");
				if(response!=null){
					LOG.info("Response Requested_Action: {} ",new Object[]{response.getRequested_Action()});
				}
				LOG.info("WebClientRestContollerAndroidApplication.AddCustomerKYCAnswerAgainstSpecificProduct()--End");
			}
		}
	}

	@RequestMapping(method=RequestMethod.POST,value={"/Mobile/GetCustomerKYCQuestionAnswer"} )
	public BasicResponce GetCustomerKYCQuestionAnswer(@RequestHeader(value="tokenAsHeader", required = false) String tokenAsHeader,
			@RequestParam(value = "tokenAsParameter", required = false) String tokenAsParameter,
			@RequestParam(value = "cellNo", required = true) long cellNo) {

		if(LOG.isInfoEnabled()){
			LOG.info("WebClientRestContollerAndroidApplication.GetCustomerKYCQuestionAnswer()--Start");
			LOG.info("Received Parameters are:");
			LOG.info("Token Header: {} ",new Object[]{tokenAsHeader});
			LOG.info("Token As Header: {} ",new Object[]{tokenAsParameter});
			LOG.info("Cell No: {} ",new Object[]{cellNo});
		}
		BasicResponce response = null;

		BasicResponce authResp=mobileUserAuthenticate(tokenAsHeader,tokenAsParameter,cellNo);		
		if(authResp!=null) {
			return authResp;
		}
		try {

			List<KYCAnswer> objKYCAnswerList=objLMSDAO.getCustomerKycAnswer(cellNo);
			List<KYCQuestionAnswerResponseDTO> toSend=new ArrayList<KYCQuestionAnswerResponseDTO>();

			if(objKYCAnswerList!=null && !(objKYCAnswerList.isEmpty())){
				for(KYCAnswer fromDB:objKYCAnswerList){
					KYCQuestionAnswerResponseDTO toAddKYCQuestionAnswer=new KYCQuestionAnswerResponseDTO();
					toAddKYCQuestionAnswer.setQuestionId(fromDB.getQuestion().getId());
					toAddKYCQuestionAnswer.setAnswer(fromDB.getQuestionAnswer());
					toSend.add(toAddKYCQuestionAnswer);
				}
				
				Customer objCustomer= objLMSDAO.getCustomerAgainstCellNo(cellNo);
				KYCQuestionAnswerResponseDTO toAddKYCQuestionAnswer=new KYCQuestionAnswerResponseDTO();
				toAddKYCQuestionAnswer.setQuestionId(1);
				toAddKYCQuestionAnswer.setAnswer(objCustomer.getIdCardFrontPath());
				toSend.add(toAddKYCQuestionAnswer);
				
				toAddKYCQuestionAnswer=new KYCQuestionAnswerResponseDTO();
				toAddKYCQuestionAnswer.setQuestionId(2);
				toAddKYCQuestionAnswer.setAnswer(objCustomer.getIdCardBackPath());
				toSend.add(toAddKYCQuestionAnswer);
				
				toAddKYCQuestionAnswer=new KYCQuestionAnswerResponseDTO();
				toAddKYCQuestionAnswer.setQuestionId(3);
				toAddKYCQuestionAnswer.setAnswer(objCustomer.getIdCardWithFacePath());
				toSend.add(toAddKYCQuestionAnswer);
				
				Collections.sort(toSend);
				authResp= new ListOfKYCQuestionAnswerResponseDTO(toSend);
				return authResp;

			}else{
				response=new BasicResponce(false);
				return response;
			}
		} catch (Exception ex){
			response=new ResponceWithMessage(ex);
			return response;
		}finally{
			if(LOG.isInfoEnabled()){
				LOG.info("Returned Response is:");
				if(response!=null){
					LOG.info("Response Requested_Action: {} ",new Object[]{response.getRequested_Action()});
				}
				LOG.info("WebClientRestContollerAndroidApplication.GetCustomerKYCQuestionAnswer()--End");
			}
		}
	}


	@RequestMapping(method=RequestMethod.POST,value={"/Mobile/GetCustomerProductKYCQuestionAnswer"} )
	public BasicResponce GetCustomerKYCQuestionAnswer(@RequestHeader(value="tokenAsHeader", required = false) String tokenAsHeader,
			@RequestParam(value = "tokenAsParameter", required = false) String tokenAsParameter,
			@RequestParam(value = "cellNo", required = true) long cellNo,
			@RequestParam(value = "productId", required = true) int productId) {

		if(LOG.isInfoEnabled()){
			LOG.info("WebClientRestContollerAndroidApplication.GetCustomerProductKYCQuestionAnswer()--Start");
			LOG.info("Received Parameters are:");
			LOG.info("Token Header: {} ",new Object[]{tokenAsHeader});
			LOG.info("Token As Header: {} ",new Object[]{tokenAsParameter});
			LOG.info("Cell No: {} ",new Object[]{cellNo});
			LOG.info("Product Id: {} ",new Object[]{productId});
		}
		BasicResponce response = null;

		BasicResponce authResp=mobileUserAuthenticate(tokenAsHeader,tokenAsParameter,cellNo);		
		if(authResp!=null) {
			return authResp;
		}
		try {
			Customer objCustomer= objLMSDAO.getCustomerAgainstCellNo(cellNo);
			Product objProduct= objLMSDAO.getProduct(productId);

			List<KYCAnswer> objKYCAnswerList=objLMSDAO.getCustomerKycAnswer(cellNo);
			List<KYCQuestionAnswerResponseDTO> toSend=new ArrayList<KYCQuestionAnswerResponseDTO>();

			if(objProduct!=null && objProduct.getQuestions()!=null && !objProduct.getQuestions().isEmpty()){
				for(KYCQuestion fromDB:objProduct.getQuestions()){
						if(fromDB.getId()!=19 && fromDB.getId()!=20 && fromDB.getId()!=21){
						KYCQuestionAnswerResponseDTO toAddKYCQuestionAnswer=new KYCQuestionAnswerResponseDTO();
						toAddKYCQuestionAnswer.setQuestionId(fromDB.getId());
						toAddKYCQuestionAnswer.setQuestionToAsk(fromDB.getQuestionToAsk());
						toAddKYCQuestionAnswer.setCategory(fromDB.getCatagory());
						toAddKYCQuestionAnswer.setAnswerType(fromDB.getAnswerType());
						toAddKYCQuestionAnswer.setListOfPossibleAnswers(fromDB.getListOfPossibleAnswers());
						toAddKYCQuestionAnswer.setMandatoryStatus(fromDB.isMandatoryStatus());
						toAddKYCQuestionAnswer.setExpiryInDays(fromDB.getExpiryInDays());
						
						if(fromDB.isMandatoryStatus()){
							for(KYCAnswer objKYCAnswer:objKYCAnswerList){
								if(fromDB.getId()==objKYCAnswer.getQuestion().getId()){
									toAddKYCQuestionAnswer.setAnswer(objKYCAnswer.getQuestionAnswer());
								}
							}
						}else{
							if(fromDB.isMandatoryStatus()==false){
								
								for(KYCAnswer objKYCAnswer:objKYCAnswerList){
									if(fromDB.getId()==objKYCAnswer.getQuestion().getId()){
										if(fromDB.getCreatedAt()!=null && fromDB.getExpiryInDays()>0){
											Date currentDate=new Date();
											
											//add expiry days in creation date and check if it is beyond current date then no need to set answer else set it
											if(currentDate.compareTo(generalLedgerService.getRequiredDate(objKYCAnswer.getCreatedAt(), fromDB.getExpiryInDays()))==0){
												toAddKYCQuestionAnswer.setAnswer(objKYCAnswer.getQuestionAnswer());
											}else if(currentDate.compareTo(generalLedgerService.getRequiredDate(objKYCAnswer.getCreatedAt(), fromDB.getExpiryInDays()))<0){
												toAddKYCQuestionAnswer.setAnswer(objKYCAnswer.getQuestionAnswer());
											}else{
												if(currentDate.compareTo(generalLedgerService.getRequiredDate(objKYCAnswer.getCreatedAt(), fromDB.getExpiryInDays()))>0){
													toAddKYCQuestionAnswer.setAnswer(null);
												}
											}
										}
									}
								}
							}
						}
						
						toSend.add(toAddKYCQuestionAnswer);
					}else{
						LOG.info("Will skip as Picture will be added Separately");
						//Picture are saved in different table in db, So we need to add them separately in response.
						//And picture as kyc question, It will always be mandatory
						Preferences objPreferences=objLMSDAO.getPreferenceValueUsingName("serverBaseUrlForCustomerPicture");
						
						if(fromDB.getId()==19){
							KYCQuestionAnswerResponseDTO toAddKYCQuestionAnswer=new KYCQuestionAnswerResponseDTO();
							toAddKYCQuestionAnswer.setQuestionId(fromDB.getId());
							toAddKYCQuestionAnswer.setQuestionToAsk(fromDB.getQuestionToAsk());
							toAddKYCQuestionAnswer.setCategory(fromDB.getCatagory());
							toAddKYCQuestionAnswer.setAnswerType(fromDB.getAnswerType());
							toAddKYCQuestionAnswer.setListOfPossibleAnswers(fromDB.getListOfPossibleAnswers());
							toAddKYCQuestionAnswer.setMandatoryStatus(fromDB.isMandatoryStatus());
							toAddKYCQuestionAnswer.setExpiryInDays(fromDB.getExpiryInDays());
							if(objPreferences!=null && objPreferences.getPreferenceValue()!=null && objCustomer!=null  && objCustomer.getIdCardFrontPath()!=null){
								toAddKYCQuestionAnswer.setAnswer(objPreferences.getPreferenceValue()+""+objCustomer.getIdCardFrontPath());
							}else{
								toAddKYCQuestionAnswer.setAnswer(objCustomer.getIdCardFrontPath());
							}
							toSend.add(toAddKYCQuestionAnswer);
						}else if(fromDB.getId()==20){
							KYCQuestionAnswerResponseDTO toAddKYCQuestionAnswer=new KYCQuestionAnswerResponseDTO();
							toAddKYCQuestionAnswer.setQuestionId(fromDB.getId());
							toAddKYCQuestionAnswer.setQuestionToAsk(fromDB.getQuestionToAsk());
							toAddKYCQuestionAnswer.setCategory(fromDB.getCatagory());
							toAddKYCQuestionAnswer.setAnswerType(fromDB.getAnswerType());
							toAddKYCQuestionAnswer.setListOfPossibleAnswers(fromDB.getListOfPossibleAnswers());
							toAddKYCQuestionAnswer.setMandatoryStatus(fromDB.isMandatoryStatus());
							toAddKYCQuestionAnswer.setExpiryInDays(fromDB.getExpiryInDays());
							if(objPreferences!=null && objPreferences.getPreferenceValue()!=null && objCustomer!=null  && objCustomer.getIdCardBackPath()!=null){
								toAddKYCQuestionAnswer.setAnswer(objPreferences.getPreferenceValue()+""+objCustomer.getIdCardBackPath());
							}else{
								toAddKYCQuestionAnswer.setAnswer(objCustomer.getIdCardBackPath());
							}
							toSend.add(toAddKYCQuestionAnswer);
						}else{
							if(fromDB.getId()==21){
								KYCQuestionAnswerResponseDTO toAddKYCQuestionAnswer=new KYCQuestionAnswerResponseDTO();
								toAddKYCQuestionAnswer.setQuestionId(fromDB.getId());
								toAddKYCQuestionAnswer.setQuestionToAsk(fromDB.getQuestionToAsk());
								toAddKYCQuestionAnswer.setCategory(fromDB.getCatagory());
								toAddKYCQuestionAnswer.setAnswerType(fromDB.getAnswerType());
								toAddKYCQuestionAnswer.setListOfPossibleAnswers(fromDB.getListOfPossibleAnswers());
								toAddKYCQuestionAnswer.setMandatoryStatus(fromDB.isMandatoryStatus());
								toAddKYCQuestionAnswer.setExpiryInDays(fromDB.getExpiryInDays());
								if(objPreferences!=null && objPreferences.getPreferenceValue()!=null && objCustomer!=null  && objCustomer.getIdCardWithFacePath()!=null){
									toAddKYCQuestionAnswer.setAnswer(objPreferences.getPreferenceValue()+""+objCustomer.getIdCardWithFacePath());
								}else{
									toAddKYCQuestionAnswer.setAnswer(objCustomer.getIdCardWithFacePath());
								}
								toSend.add(toAddKYCQuestionAnswer);
							}
						}
					}
				}
				
				Collections.sort(toSend);
				authResp= new ListOfKYCQuestionAnswerResponseDTO(toSend);
				return authResp;

			}else{
				response=new BasicResponce(false);
				return response;
			}
		} catch (Exception ex){
			response=new ResponceWithMessage(ex);
			return response;
		}finally{
			if(LOG.isInfoEnabled()){
				LOG.info("Returned Response is:");
				if(response!=null){
					LOG.info("Response Requested_Action: {} ",new Object[]{response.getRequested_Action()});
				}
				LOG.info("WebClientRestContollerAndroidApplication.GetCustomerProductKYCQuestionAnswer()--End");
			}
		}
	}


	@RequestMapping(method=RequestMethod.POST,value={"/Mobile/GetAllProducts"} )
	public BasicResponce GetAllProducts(@RequestHeader(value="tokenAsHeader", required = false) String tokenAsHeader,
			@RequestParam(value = "tokenAsParameter", required = false) String tokenAsParameter,
			@RequestParam(value = "cellNo", required = true) long cellNo) {

		if(LOG.isInfoEnabled()){
			LOG.info("WebClientRestContollerAndroidApplication.GetAllProducts()--Start");
			LOG.info("Received Parameters are:");
			LOG.info("Token Header: {} ",new Object[]{tokenAsHeader});
			LOG.info("Token As Header: {} ",new Object[]{tokenAsParameter});
			LOG.info("Cell No: {} ",new Object[]{cellNo});
		}
		BasicResponce authResp = null;

		authResp=mobileUserAuthenticate(tokenAsHeader,tokenAsParameter,cellNo);		
		if(authResp!=null) {
			return authResp;
		}
		try {
			List<Product> fromDB=objLMSDAO.getAllProductsEager();		
			List<ProductResponseDTO> toSend = new ArrayList<ProductResponseDTO>();
			for(Product i:fromDB) {
				ProductResponseDTO toAdd;
				Preferences objPreference = objLMSDAO.getPreferenceValueUsingName("serverBaseUrlPathForTermsAndConditionsFile");
				if(objPreference!=null && i.getTermsAndConditionFilePath()!=null){
					toAdd=new ProductResponseDTO(i.getId(),i.getProductName(),i.getProductCatagory(),objPreference.getPreferenceValue()+""+i.getTermsAndConditionFilePath());
				}else{
					toAdd=new ProductResponseDTO(i.getId(),i.getProductName(),i.getProductCatagory(),null);
				}
				
				if(i.getProductSpecification()!=null && i.getProductSpecification().size()>0){
					for(ProductSpecification i2: i.getProductSpecification()){
						toAdd.addProductSpecificationDTO(i2.getProductSpecificationAssumption().getId(), i2.getProductSpecificationAssumption().getName(), i2.getAssumptionValue());
					}
				}
				if(i.getQuestions()!=null && i.getQuestions().size()>0){
					for(KYCQuestion i2: i.getQuestions()){
						KYCQuestionResponseDTO kycQuestion=new KYCQuestionResponseDTO();
						kycQuestion.setQuestionId(i2.getId());
						kycQuestion.setQuestion(i2.getQuestionToAsk());
						kycQuestion.setQuestionCategory(i2.getCatagory());
						kycQuestion.setAnswerType(i2.getAnswerType());
						kycQuestion.setListOfPossibleAnswers(i2.getListOfPossibleAnswers());
						kycQuestion.setMandatoryStatus(i2.isMandatoryStatus());
						kycQuestion.setExpiryInDays(i2.getExpiryInDays());
						toAdd.getQuestions().add(kycQuestion);
					}
				}
				toSend.add(toAdd);
			}
			authResp= new ListOfProductResponseDTOs(toSend);
			return authResp;
		} catch (Exception ex){
			authResp=  new BasicResponce(false);
			return authResp;
		}finally{
			if(LOG.isInfoEnabled()){
				LOG.info("Returned Response is:");
				if(authResp!=null){
					LOG.info("Response Requested_Action: {} ",new Object[]{authResp.getRequested_Action()});
				}
				LOG.info("WebClientRestContollerAndroidApplication.GetAllProducts()--End");
			}
		}
	}

	/*@RequestMapping(method=RequestMethod.POST,value={"/Mobile/setLoanAmountOfApprovedLoan"} )
	public BasicResponce setLoanAmountOfApprovedLoan(@RequestHeader(value="tokenAsHeader", required = false) String tokenAsHeader,
			@RequestParam(value = "tokenAsParameter", required = false) String tokenAsParameter,
			@RequestParam(value = "cellNo", required = true) long cellNo,
			@RequestParam(value = "requestedAmount", required = true) int requestedAmount,
			@RequestParam(value = "loanApplicationId", required = true) int loanApplicationId) {

		try {
			BasicResponce authResp = null;

			authResp=mobileUserAuthenticate(tokenAsHeader,tokenAsParameter,cellNo);		
			if(authResp!=null) {
				return authResp;
			}

			objLMSDAO.updateLoanApplication(loanApplicationId, LoanApplicationStatus.READYTODISBURSE,requestedAmount, null);

			return new BasicResponce(true);

		} catch (Exception ex){
			return new ResponceWithMessage(ex);

		}
	}*/

	@RequestMapping(method=RequestMethod.POST,value={"/Mobile/updateLoanApplicationToAcceptedStatus"} )
	public BasicResponce updateLoanApplicationToAcceptedStatus(@RequestHeader(value="tokenAsHeader", required = false) String tokenAsHeader,
			@RequestParam(value = "tokenAsParameter", required = false) String tokenAsParameter,
			@RequestParam(value = "cellNo", required = true) long cellNo,
			@RequestParam(value = "requestedAmount", required = true) int requestedAmount,
			@RequestParam(value = "termsAndConditionAcceptanceStatus", required = false) boolean termsAndConditionAcceptanceStatus,
			@RequestParam(value = "loanApplicationId", required = true) int loanApplicationId) {

		try {
			BasicResponce authResp = null;

			authResp=mobileUserAuthenticate(tokenAsHeader,tokenAsParameter,cellNo);		
			if(authResp!=null) {
				return authResp;
			}

			LoanApplication loanApp=objLMSDAO.getLoanApplicationById(loanApplicationId);

			if(loanApp!=null && loanApp.getStatus()==LoanApplicationStatus.APPROVED){
				objLMSDAO.updateLoanApplication(loanApplicationId, LoanApplicationStatus.ACCEPTED,requestedAmount,termsAndConditionAcceptanceStatus, null);
				authResp=new BasicResponce(true);
				return authResp;
			}else{
				authResp= new ResponceWithMessage(false,"Loan Application is in Invalid Status, Unable to Process");
				return authResp;
			}
		} catch (Exception ex){
			return new ResponceWithMessage(ex);
		}
	}


	@RequestMapping(method=RequestMethod.POST,value={"/Mobile/updateLoanApplicationToDeclineStatus"} )
	public BasicResponce updateLoanApplicationToDeclineStatus(@RequestHeader(value="tokenAsHeader", required = false) String tokenAsHeader,
			@RequestParam(value = "tokenAsParameter", required = false) String tokenAsParameter,
			@RequestParam(value = "cellNo", required = true) long cellNo,
			@RequestParam(value = "requestedAmount", required = false) Integer requestedAmount,
			@RequestParam(value = "termsAndConditionAcceptanceStatus", required = false) boolean termsAndConditionAcceptanceStatus,
			@RequestParam(value = "loanApplicationId", required = true) int loanApplicationId) {

		try {
			BasicResponce authResp = null;

			authResp=mobileUserAuthenticate(tokenAsHeader,tokenAsParameter,cellNo);		
			if(authResp!=null) {
				return authResp;
			}

			LoanApplication loanApp=objLMSDAO.getLoanApplicationById(loanApplicationId);

			if(loanApp!=null && loanApp.getStatus()==LoanApplicationStatus.APPROVED){
				objLMSDAO.updateLoanApplication(loanApplicationId, LoanApplicationStatus.DECLINED, requestedAmount, termsAndConditionAcceptanceStatus, null);
				return new BasicResponce(true);
			}else{
				return new ResponceWithMessage(false,"Loan Application is in Invalid Status, Unable to Process");
			}
		} catch (Exception ex){
			return new ResponceWithMessage(ex);
		}
	}

	@RequestMapping(method=RequestMethod.POST,value={"/Mobile/applyForLoan"} )
	public BasicResponce applyForLoan(@RequestParam(value = "cellNo", required = true) long cellNo
			,@RequestParam(value = "productId", required = true) int productId
			,@RequestParam(value = "walletTypeId", required = true) int walletTypeId) {

		if(LOG.isInfoEnabled()){
			LOG.info("WebClientRestContollerAndroidApplication.applyForLoan()--Start");
			LOG.info("Received Parameters are:");
			LOG.info("Cell No: {} ",new Object[]{cellNo});
			LOG.info("Product Id: {} ",new Object[]{productId});
			LOG.info("Wallet Type Id: {} ",new Object[]{walletTypeId});
		}
		BasicResponce authResp = null;

		try {
			int result;

			Customer customer=objLMSDAO.getCustomerAgainstCellNo(cellNo);

			if(customer!=null){
				List<LoanApplication> loanApplicationList;
				loanApplicationList=objLMSDAO.getAllNonCompletedLoanApplicationsUsingDisbursedLoansForCustomer(customer.getId());
	
				if(loanApplicationList.isEmpty()){
					LOG.info("As Disbursed Loan Not Found, Now we Need to Check Loan Applcation");
					loanApplicationList=objLMSDAO.getAllNonCompletedLoanApplicationsForCustomer(customer.getId());
				}else{
					LOG.info("No Disbursed Loan Found");
				}
	
				if(loanApplicationList!=null && loanApplicationList.size()>0){
					LOG.info("Already Have Loan Application in Cycle");
	
					if(loanApplicationList.get(0)!=null){
						LOG.info("Loan Application Info details");
						LOG.info("Loan App Id"+loanApplicationList.get(0).getId());
						LOG.info("Loan App Status"+loanApplicationList.get(0).getStatus());	
					}else{
						LOG.info("Loan Application Returned List Has no Record");
					}
					authResp=new ResponceWithMessage(false,"Already have Active Loans, Unable to Process New Loan");
					return authResp;
				}else{
					result=objLMSDAO.createNewLoanApplication(cellNo,productId,walletTypeId);
				}
	
				if(result>0) {
					authResp=new BasicResponce(true);
					return authResp;
				} else if(result==-2) {
					authResp=new ResponceWithMessage(false,"Must Be Unique");
					return authResp;
				} else {
					authResp=new ResponceWithMessage(false,"Some Constraint Failed");
					return authResp;
				}
			
			}else{
				authResp=new ResponceWithMessage(false,"Customer Not Found");
				return authResp;
			}

		} catch (Exception ex) {
			authResp=new ResponceWithMessage(ex);
			return authResp;
		}finally{
			if(LOG.isInfoEnabled()){
				LOG.info("Returned Response is:");
				if(authResp!=null){
					LOG.info("Response Requested_Action: {} ",new Object[]{authResp.getRequested_Action()});
				}
				LOG.info("WebClientRestContollerAndroidApplication.applyForLoan()--End");
			}
		}
	}




	@RequestMapping(method=RequestMethod.POST,value={"/Mobile/GetCustomerInformations"} )
	public ResponceDTO<Object> GetCustomerInformations(@RequestParam(value = "Token") String Token) {

		if (LOG.isInfoEnabled()) {
			LOG.info("WebClientRestContollerAndroidApplication.GetCustomerInformations()--Start");
			LOG.info("Received Parameters are:");
			LOG.info("Token: {} ", new Object[] { Token });
		}
		ResponceDTO<Object> response = null;

		try {
			Customer result=objLMSDAO.loginInfoByToken(Token);			
			if(result==null){
				response=new ResponceDTO<Object>(false,"Unable to find session token");
				return response;
			} else {
				CustomerResponseDTO objCustomerDTO=new CustomerResponseDTO(result.getId(),result.getCellNoInString(),result.getFullName(),result.getPakistaniCNIC(),objLMSDAO.getCustomerBalance(result.getId()));
				objCustomerDTO.setIdCardBackPath(objServerUtils.fileToExposedURL(result.getIdCardBackPath()));
				objCustomerDTO.setIdCardFrontPath(objServerUtils.fileToExposedURL(result.getIdCardFrontPath()));
				objCustomerDTO.setIdCardWithFacePath(objServerUtils.fileToExposedURL(result.getIdCardWithFacePath()));
				response=new ResponceDTO<Object>(true,objCustomerDTO);
				return response;
			} 

		} catch (Exception ex) {
			response=new ResponceDTO<Object>(false,"Unknown Exception");
			return response;
		}finally{
			if(LOG.isInfoEnabled()){
				LOG.info("Returned Response is:");
				if(response!=null){
					LOG.info("Response Requested_Action: {} ",new Object[]{response.getRequested_Action()});
				}
				LOG.info("WebClientRestContollerAndroidApplication.GetCustomerInformations()--End");
			}
		}
	}


	/*@RequestMapping(method=RequestMethod.POST,value={"/Mobile/GetCustomerTransactionHistory"} )
	public BasicResponce GetCustomerTransactionHistory(@RequestParam(value = "Token") String Token) {

		if (LOG.isInfoEnabled()) {
			LOG.info("WebClientRestContollerAndroidApplication.GetCustomerTransactionHistory()--Start");
			LOG.info("Received Parameters are:");
			LOG.info("Token: {} ", new Object[] { Token });
		}
		BasicResponce response = null;

		try {
			Customer result=objLMSDAO.loginInfoByToken(Token);			
			if(result==null){
				return new ResponceWithMessage(false,"Unable to find session token");
			} else {

				List<CustomerTransactionDTO> toAddInto=new ArrayList<CustomerTransactionDTO>();

				List<LoanApplication> toParse=objLMSDAO.getAllDisbursedLoanApplicationsForCustomer(result.getId());

				for(LoanApplication i:toParse){
					CustomerTransactionDTO toAdd=new CustomerTransactionDTO();
					toAdd.setAmount(i.getRequestedamount());
					toAdd.setDateTime(LocalDateTime.now().toString());
					toAddInto.add(toAdd);
				}

				ListOfCustomerTransactionDTO toReturn = new ListOfCustomerTransactionDTO(toAddInto);

				response=toReturn;
				return response;
			} 

		} catch (Exception ex) {
			response=new ResponceWithMessage(false,"Unknown Exception");
			return response;
		}finally{
			if(LOG.isInfoEnabled()){
				LOG.info("Returned Response is:");
				if(response!=null){
					LOG.info("Response Requested_Action: {} ",new Object[]{response.getRequested_Action()});
				}
				LOG.info("WebClientRestContollerAndroidApplication.GetCustomerTransactionHistory()--End");
			}
		}
	}*/


	/*@RequestMapping(method=RequestMethod.POST,value={"/Mobile/GetCustomerDashboardData"} )
	public BasicResponce GetCustomerDashboardData(@RequestParam(value = "Token") String Token) {

		if (LOG.isInfoEnabled()) {
			LOG.info("WebClientRestContollerAndroidApplication.GetCustomerDashboardData()--Start");
			LOG.info("Received Parameters are:");
			LOG.info("Token: {} ", new Object[] { Token });
		}
		BasicResponce response = null;

		try {
			Customer customer=objLMSDAO.loginInfoByToken(Token);			
			if(customer==null){
				return new ResponceWithMessage(false,"Unable to find session token");
			} else {

				List<DisbursedLoans> disbursedLoanList= objLMSDAO.getAllDisbursedButNotCompletedLoanApplicationsForCustomer(customer.getId());
				if(disbursedLoanList!=null && disbursedLoanList.size()>0){

					CustomerDashboardDTO customerDashboardInfoDTO=new CustomerDashboardDTO();

					customerDashboardInfoDTO.setId(customer.getId());
					customerDashboardInfoDTO.setCellNo(customer.getCellNo());
					customerDashboardInfoDTO.setCnic(customer.getPakistaniCNIC());
					customerDashboardInfoDTO.setFullName(customer.getFullName());
					customerDashboardInfoDTO.setPinCode(customer.getPinCode());
					customerDashboardInfoDTO.setStatus(customer.getStatus());

					customerDashboardInfoDTO.setDueDate(disbursedLoanList.get(0).getDueDate()+"");
					customerDashboardInfoDTO.setDaysRemainingTillDueDate(generalLedgerService.getDifferenceBetweenTwoDateInDays(new Date(),disbursedLoanList.get(0).getDueDate()).intValue());
					customerDashboardInfoDTO.setOustandingBalance(generalLedgerService.getLoanOutstandingBalance(disbursedLoanList.get(0).getId()));

					List<GeneralLedgerDTO> oneLoadList=generalLedgerService.getLoanEntriesFromGlByLoanId(Constants.GENERAL_LEDGER_ENTRY_FOR_ONE_LOAD_ACCOUNT,disbursedLoanList.get(0).getId());

					if(oneLoadList!=null && oneLoadList.size()>0){
						if(oneLoadList.get(oneLoadList.size()-1).isDebitTransaction()){
							customerDashboardInfoDTO.setLastPaymentAmount(oneLoadList.get(oneLoadList.size()-1).getAmount());
						}else{
							customerDashboardInfoDTO.setLastPaymentAmount(oneLoadList.get(oneLoadList.size()-1).getAmount());
						}
					}else{
						customerDashboardInfoDTO.setLastPaymentAmount(0);
					}

					response=customerDashboardInfoDTO;
					response.setRequested_Action(true);
					return response;

				}else{
					CustomerDashboardDTO customerDashboardInfoDTO=new CustomerDashboardDTO();

					customerDashboardInfoDTO.setId(customer.getId());
					customerDashboardInfoDTO.setCellNo(customer.getCellNo());
					customerDashboardInfoDTO.setCnic(customer.getPakistaniCNIC());
					customerDashboardInfoDTO.setFullName(customer.getFullName());
					customerDashboardInfoDTO.setPinCode(customer.getPinCode());
					customerDashboardInfoDTO.setStatus(customer.getStatus());

					response=customerDashboardInfoDTO;
					response.setRequested_Action(true);
					return response;
				}	
			} 

		} catch (Exception ex) {
			response=new ResponceWithMessage(false,"Exception Occurred: "+ex.getMessage());
			return response;
		}finally{
			if(LOG.isInfoEnabled()){
				LOG.info("Returned Response is:");
				if(response!=null){
					LOG.info("Response Requested_Action: {} ",new Object[]{response.getRequested_Action()});
				}
				LOG.info("WebClientRestContollerAndroidApplication.GetCustomerTransactionHistory()--End");
			}
		}
	}*/

	@RequestMapping(method=RequestMethod.POST,value={"/Mobile/GetCustomerDashboardData"} )
	public BasicResponce GetCustomerDashboardData(@RequestParam(value = "Token") String Token) {

		if (LOG.isInfoEnabled()) {
			LOG.info("WebClientRestContollerAndroidApplication.GetCustomerDashboardData()--Start");
			LOG.info("Received Parameters are:");
			LOG.info("Token: {} ", new Object[] { Token });
		}
		BasicResponce response = null;
		List<CustomerDashboardResponseDTO> customerDashboardDTOList=new ArrayList<CustomerDashboardResponseDTO>(); 
		
		CustomerDashboardResponseDTO customerDashboardDTO=new CustomerDashboardResponseDTO();
		
		try {
			Customer customer=objLMSDAO.loginInfoByToken(Token);			
			if(customer==null){
				return new ResponceWithMessage(false,"Unable to find session token");
			} else {
				
				
				Preferences objPreferences= objLMSDAO.getPreferenceValueUsingName("activeCurrencyUnit");
				List<PreferenceResponseDTO> toSendPreference=new ArrayList<PreferenceResponseDTO>();
				
				if(objPreferences!=null){
					PreferenceResponseDTO toAdd = new PreferenceResponseDTO();
					toAdd.setPreferenceId(objPreferences.getPreferenceId());
					toAdd.setPreferenceName(objPreferences.getPreferenceName());
					toAdd.setPreferenceValue(objPreferences.getPreferenceValue());
					toSendPreference.add(toAdd);
				}else{
					LOG.info("Failed to get Preference Value With Preference Name: activeCurrencyUnit");
				}
				customerDashboardDTO.setPreferenceDataForApp(toSendPreference);
				
				LoanApplicationIncompleteProcessResponseDTO loanApplicationIncompleteProcessResponseApplicationDTO=new LoanApplicationIncompleteProcessResponseDTO();
				//Getting Most Recent KYC Record to Identify In Process Loan Application Status
				KYCAnswer mostRecentKYCAnswer= objLMSDAO.getCustomerMostRecentKycAnswer(customer);
				
				List<KYCAnswer> objKycAnswerList = null;
				Product objProduct= null;
				if(mostRecentKYCAnswer!=null){
					if(mostRecentKYCAnswer.getProduct()!=null){
						//Now Getting All Given KYC Answers against Recently Selected Product by Customer
						objKycAnswerList= objLMSDAO.getCustomerAllKycAnswer(customer, mostRecentKYCAnswer.getProduct());
						//Now Getting Selected Product Detail, We will check KYC Questions attached with product. And will get all Unanswer Question 
						objProduct= objLMSDAO.getProduct(mostRecentKYCAnswer.getProduct().getId());
						
						if(objKycAnswerList!=null && !objKycAnswerList.isEmpty() && objProduct!=null){
							boolean presenceFlag=false;
							for(KYCQuestion objKYCQuestion: objProduct.getQuestions()){
								presenceFlag=false;
								
								for(KYCAnswer kycAnswer: objKycAnswerList){
									if(objKYCQuestion.getId()==kycAnswer.getQuestion().getId() && kycAnswer.getQuestionAnswer()!=null){
										presenceFlag=true;
										break;
									}else{
										if((objKYCQuestion.getId()==19 && customer.getIdCardFrontPath()!=null) 
												|| (objKYCQuestion.getId()==20 && customer.getIdCardBackPath()!=null)
												|| (objKYCQuestion.getId()==21 && customer.getIdCardWithFacePath()!=null)){
											presenceFlag=true;
											break;
										}
									}
								}
								if(presenceFlag){
									//Good to go
								}else{
									LOG.info("Current Question Record Not Found In KYC Answer List, QID: "+objKYCQuestion.getId());
									break;
								}
							}
							
							if(presenceFlag){
								loanApplicationIncompleteProcessResponseApplicationDTO.setApplicationInProcess(!presenceFlag);
								loanApplicationIncompleteProcessResponseApplicationDTO.setProductId(mostRecentKYCAnswer.getProduct().getId());
								loanApplicationIncompleteProcessResponseApplicationDTO.setMessage("All KYC Question With Answer Are Existed against Current Customer");
							}else{
								loanApplicationIncompleteProcessResponseApplicationDTO.setApplicationInProcess(!presenceFlag);
								loanApplicationIncompleteProcessResponseApplicationDTO.setProductId(mostRecentKYCAnswer.getProduct().getId());
								loanApplicationIncompleteProcessResponseApplicationDTO.setMessage("Some Question's Answers are Missing.");
							}
						}else{
							loanApplicationIncompleteProcessResponseApplicationDTO.setApplicationInProcess(false);
							loanApplicationIncompleteProcessResponseApplicationDTO.setProductId(mostRecentKYCAnswer.getProduct().getId());
							loanApplicationIncompleteProcessResponseApplicationDTO.setMessage("Unable to get Further Required Data. Unable to Determine Loan Aplication Completion Status");
						}
					}else{
						loanApplicationIncompleteProcessResponseApplicationDTO.setApplicationInProcess(false);
						loanApplicationIncompleteProcessResponseApplicationDTO.setProductId(null);
						loanApplicationIncompleteProcessResponseApplicationDTO.setMessage("Product Mapping Against KYC Question is Missing");
					}
				}else{
					loanApplicationIncompleteProcessResponseApplicationDTO.setApplicationInProcess(false);
					loanApplicationIncompleteProcessResponseApplicationDTO.setProductId(null);
					loanApplicationIncompleteProcessResponseApplicationDTO.setMessage("No Kyc Answer Data Found");
				}
				
				customerDashboardDTO.setLoanApplicationIncompleteProcessResponseDTO(loanApplicationIncompleteProcessResponseApplicationDTO);
				
				
				
				List<DisbursedLoans> disbursedLoanList= objLMSDAO.getAllDisbursedButNotCompletedLoanApplicationsForCustomer(customer.getId());
				if(disbursedLoanList!=null && disbursedLoanList.size()>0){

//					CustomerDashboardResponseDTO customerDashboardDTO=new CustomerDashboardResponseDTO();

					LoanSummaryResponseDTO loanSummaryDTO=new LoanSummaryResponseDTO();

					if(disbursedLoanList.get(0)!=null && disbursedLoanList.get(0).getDueDate()!=null)
						loanSummaryDTO.setDueDate(utility.getDateFromTimeStamp(disbursedLoanList.get(0).getDueDate().toString()));

					loanSummaryDTO.setDaysRemainingTillDueDate(generalLedgerService.getDifferenceBetweenTwoDateInDays(new Date(),disbursedLoanList.get(0).getDueDate()).intValue()+1);

					long totalDueAmount= generalLedgerService.getLoanDueAmount(disbursedLoanList.get(0).getId());
					long totalOutstandingAmount= generalLedgerService.getLoanDueAmount(disbursedLoanList.get(0).getId());

					loanSummaryDTO.setTotalDueAmount(totalDueAmount);
					loanSummaryDTO.setOutstandingBalance(totalOutstandingAmount);
					loanSummaryDTO.setTotalPaidAmount(totalDueAmount - totalOutstandingAmount);// Paid amount= due amount - payable amount
					
					
					List<GeneralLedgerResponseDTO> oneLoadList=generalLedgerService.getLoanEntriesFromGlByLoanId(Constants.GENERAL_LEDGER_ENTRY_FOR_ONE_LOAD_ACCOUNT,disbursedLoanList.get(0).getId());
					if(oneLoadList!=null && oneLoadList.size()>0){
						if(oneLoadList.get(oneLoadList.size()-1).isDebitTransaction()){
							loanSummaryDTO.setLastPaymentAmount(Long.valueOf(oneLoadList.get(oneLoadList.size()-1).getAmount()));
						}else{
							loanSummaryDTO.setLastPaymentAmount(0);
						}
					}else{
						loanSummaryDTO.setLastPaymentAmount(0);
					}
					customerDashboardDTO.setLoanSummaryDTO(loanSummaryDTO);

					List<DisbursedLoans> fromDB= objLMSDAO.getAllDisbursedLoansForCustomer(customer.getId());
					List<DisbursedLoanResponseDTO> toSend = new ArrayList<DisbursedLoanResponseDTO>();
					for(DisbursedLoans i:fromDB) {
						DisbursedLoanResponseDTO toAdd=new DisbursedLoanResponseDTO();
						toAdd.setId(i.getId());
						LoanApplicationsResponseDTO toPutLoanApplication=new LoanApplicationsResponseDTO();
						toPutLoanApplication.setId(i.getApplication().getId());

						if(i.getApplication()!=null && i.getApplication().getCustomer()!=null)
							toPutLoanApplication.setCustomer(new CustomerResponseDTO(i.getApplication().getCustomer().getId(), i.getApplication().getCustomer().getCellNoInString(), i.getApplication().getCustomer().getFullName(), i.getApplication().getCustomer().getPakistaniCNIC(), i.getApplication().getCustomer().getStatus(), i.getApplication().getCustomer().getPinCode()));

						ProductResponseDTO toPutProduct=new ProductResponseDTO();
						if(i.getApplication()!=null && i.getApplication().getProduct()!=null)
						{
							Preferences objPreference = objLMSDAO.getPreferenceValueUsingName("serverBaseUrlPathForTermsAndConditionsFile");
							
							toPutProduct.setProductId(i.getApplication().getProduct().getId());
							toPutProduct.setProductName(i.getApplication().getProduct().getProductName());
							toPutProduct.setProductCatagory(i.getApplication().getProduct().getProductCatagory());
							
							if(objPreference!=null && i.getApplication().getProduct().getTermsAndConditionFilePath()!=null){
								toPutProduct.setTermsAndConditionFilePath(objPreference.getPreferenceValue()+""+i.getApplication().getProduct().getTermsAndConditionFilePath());
							}else{
								toPutProduct.setTermsAndConditionFilePath(null);
							}
							

							List<KYCAnswerSnapShotAtLoanApplication> objKYCAnswerSnapShotAtLoanApplication=objLMSDAO.getListKYCAnswerSnapShotForApplication(i.getApplication().getId());
							if(objKYCAnswerSnapShotAtLoanApplication!=null && objKYCAnswerSnapShotAtLoanApplication.size()>0) {
								for(KYCAnswerSnapShotAtLoanApplication i2:objKYCAnswerSnapShotAtLoanApplication) {
									if(i2.getQuestion()!=null)
										toPutProduct.addKYCQuestionWithAnswerDTO(i2.getQuestion().getId(), i2.getQuestion().getQuestionToAsk(),i2.getQuestion().getCatagory(), i2.getQuestion().getAnswerType(),i2.getQuestionanswer());
								}
							}

							List<ProductSpecificationSnapShotAtLoanApplication> objListOfProductSpecificationSnapShotAtLoanApplication=objLMSDAO.getListOfProductSpecificationSnapShotAtLoanApplication(i.getApplication().getId());
							if(objListOfProductSpecificationSnapShotAtLoanApplication!=null && objListOfProductSpecificationSnapShotAtLoanApplication.size()>0) {
								for(ProductSpecificationSnapShotAtLoanApplication i2:objListOfProductSpecificationSnapShotAtLoanApplication) {	
									if(i2.getProductspecificationassumption()!=null)
										toPutProduct.addProductSpecificationDTO(i2.getProductspecificationassumption().getId(), i2.getProductspecificationassumption().getName(), i2.getAssumptionvalue());
								}
							}

							toPutLoanApplication.setProduct(toPutProduct);
						}

						toPutLoanApplication.setWalletTypeId(i.getApplication().getWalletTypeId());
						toPutLoanApplication.setRequestedamount(i.getApplication().getRequestedamount());
						toPutLoanApplication.setStatus(i.getApplication().getStatus().name());

						if(i.getApplication()!=null && i.getApplication().getCreatedAt()!=null)
							toPutLoanApplication.setCreatedAt(utility.getDateFromTimeStamp(i.getApplication().getCreatedAt().toString()));	
						if(i.getApplication()!=null && i.getApplication().getCreatedBy()!=null)
							toPutLoanApplication.setCreatedBy(new ApplicationUserResponseDTO(i.getApplication().getCreatedBy().getUserId(),i.getApplication().getCreatedBy().getDisplayName()));
						if(i.getApplication()!=null && i.getApplication().getUpdatedAt()!=null)
							toPutLoanApplication.setUpdatedAt(utility.getDateFromTimeStamp(i.getApplication().getUpdatedAt().toString()));	
						if(i.getApplication()!=null && i.getApplication().getUpdatedBy()!=null)
							toPutLoanApplication.setUpdatedBy(new ApplicationUserResponseDTO(i.getApplication().getUpdatedBy().getUserId(),i.getApplication().getUpdatedBy().getDisplayName()));

						toAdd.setApplication(toPutLoanApplication);

						toAdd.setTransactionId(i.getTransactionId());
						toAdd.setLoanStatus(i.getLoanStatus());
						toAdd.setDueDate(i.getDueDate()+"");
						toAdd.setGraceDueDate(i.getGraceDueDate()+"");
						toAdd.setOutstandingAmount(generalLedgerService.getLoanOutstandingBalance(i.getId()));

						if(i.getCreatedAt()!=null)
							toAdd.setCreatedAt(utility.getDateFromTimeStamp(i.getCreatedAt().toString()));	
						if(i.getCreatedBy()!=null)
							toAdd.setCreatedBy(new ApplicationUserResponseDTO(i.getCreatedBy().getUserId(),i.getCreatedBy().getDisplayName()));
						if(i.getUpdatedAt()!=null)
							toAdd.setUpdatedAt(utility.getDateFromTimeStamp(i.getUpdatedAt().toString()));	
						if(i.getUpdatedBy()!=null)
							toAdd.setUpdatedBy(new ApplicationUserResponseDTO(i.getUpdatedBy().getUserId(),i.getUpdatedBy().getDisplayName()));


						toPutLoanApplication.setRequested_Action(true);
						toSend.add(toAdd);
					}

					customerDashboardDTO.setDisbursedDTOList(toSend);
					customerDashboardDTOList.add(customerDashboardDTO);
					response=new ListOfCustomerDashboardResponseDTO(customerDashboardDTOList);
					response.setRequested_Action(true);
					return response;

				}else{
//					List<CustomerDashboardResponseDTO> customerDashboardDTOList1=new ArrayList<CustomerDashboardResponseDTO>(); 
//					CustomerDashboardResponseDTO customerDashboardDTO=new CustomerDashboardResponseDTO();
					List<DisbursedLoanResponseDTO> toSend = new ArrayList<DisbursedLoanResponseDTO>();

					List<LoanApplication> loanAppList= objLMSDAO.getAllPendingLoanApplications();
					LoanApplication currentLoanApplication=null;

					Collections.sort(loanAppList, Collections.reverseOrder());
					for(LoanApplication i:loanAppList) {
						if(i.getCustomer().getId() == customer.getId()){
							currentLoanApplication=i;
							break;
						}
					}

					if(currentLoanApplication!=null){
						DisbursedLoanResponseDTO disbLoanDTO=new DisbursedLoanResponseDTO();

						LoanApplicationsResponseDTO toPutLoanApplication=new LoanApplicationsResponseDTO();
						toPutLoanApplication.setId(currentLoanApplication.getId());

						if(currentLoanApplication!=null && currentLoanApplication.getCustomer()!=null)
							toPutLoanApplication.setCustomer(new CustomerResponseDTO(currentLoanApplication.getCustomer().getId(), currentLoanApplication.getCustomer().getCellNoInString(), currentLoanApplication.getCustomer().getFullName(), currentLoanApplication.getCustomer().getPakistaniCNIC(), currentLoanApplication.getCustomer().getStatus(), currentLoanApplication.getCustomer().getPinCode()));

						ProductResponseDTO toPutProduct=new ProductResponseDTO();
						if(currentLoanApplication!=null && currentLoanApplication.getProduct()!=null)
						{
							Preferences objPreference = objLMSDAO.getPreferenceValueUsingName("serverBaseUrlPathForTermsAndConditionsFile");
							
							toPutProduct.setProductId(currentLoanApplication.getProduct().getId());
							toPutProduct.setProductName(currentLoanApplication.getProduct().getProductName());
							toPutProduct.setProductCatagory(currentLoanApplication.getProduct().getProductCatagory());
							
							if(objPreference!=null && currentLoanApplication.getProduct().getTermsAndConditionFilePath()!=null){
								toPutProduct.setTermsAndConditionFilePath(objPreference.getPreferenceValue()+""+currentLoanApplication.getProduct().getTermsAndConditionFilePath());
							}else{
								toPutProduct.setTermsAndConditionFilePath(null);
							}
							
							
							List<KYCAnswerSnapShotAtLoanApplication> objKYCAnswerSnapShotAtLoanApplication=objLMSDAO.getListKYCAnswerSnapShotForApplication(currentLoanApplication.getId());
							if(objKYCAnswerSnapShotAtLoanApplication!=null && objKYCAnswerSnapShotAtLoanApplication.size()>0) {
								for(KYCAnswerSnapShotAtLoanApplication i2:objKYCAnswerSnapShotAtLoanApplication) {
									if(i2.getQuestion()!=null)
										toPutProduct.addKYCQuestionWithAnswerDTO(i2.getQuestion().getId(), i2.getQuestion().getQuestionToAsk(),i2.getQuestion().getCatagory(), i2.getQuestion().getAnswerType(),i2.getQuestionanswer());
								}
							}

							List<ProductSpecificationSnapShotAtLoanApplication> objListOfProductSpecificationSnapShotAtLoanApplication=objLMSDAO.getListOfProductSpecificationSnapShotAtLoanApplication(currentLoanApplication.getId());
							if(objListOfProductSpecificationSnapShotAtLoanApplication!=null && objListOfProductSpecificationSnapShotAtLoanApplication.size()>0) {
								for(ProductSpecificationSnapShotAtLoanApplication i2:objListOfProductSpecificationSnapShotAtLoanApplication) {	
									if(i2.getProductspecificationassumption()!=null)
										toPutProduct.addProductSpecificationDTO(i2.getProductspecificationassumption().getId(), i2.getProductspecificationassumption().getName(), i2.getAssumptionvalue());
								}
							}

							toPutLoanApplication.setProduct(toPutProduct);
						}

						toPutLoanApplication.setWalletTypeId(currentLoanApplication.getWalletTypeId());
						toPutLoanApplication.setRequestedamount(currentLoanApplication.getRequestedamount());
						toPutLoanApplication.setStatus(currentLoanApplication.getStatus().name());

						if(currentLoanApplication!=null && currentLoanApplication.getCreatedAt()!=null)
							toPutLoanApplication.setCreatedAt(utility.getDateFromTimeStamp(currentLoanApplication.getCreatedAt().toString()));	
						if(currentLoanApplication!=null && currentLoanApplication.getCreatedBy()!=null)
							toPutLoanApplication.setCreatedBy(new ApplicationUserResponseDTO(currentLoanApplication.getCreatedBy().getUserId(),currentLoanApplication.getCreatedBy().getDisplayName()));
						if(currentLoanApplication!=null && currentLoanApplication.getUpdatedAt()!=null)
							toPutLoanApplication.setUpdatedAt(utility.getDateFromTimeStamp(currentLoanApplication.getUpdatedAt().toString()));	
						if(currentLoanApplication!=null && currentLoanApplication.getUpdatedBy()!=null)
							toPutLoanApplication.setUpdatedBy(new ApplicationUserResponseDTO(currentLoanApplication.getUpdatedBy().getUserId(),currentLoanApplication.getUpdatedBy().getDisplayName()));

						disbLoanDTO.setApplication(toPutLoanApplication);
						toSend.add(disbLoanDTO);

						customerDashboardDTO.setDisbursedDTOList(toSend);
						customerDashboardDTOList.add(customerDashboardDTO);

						response=new ListOfCustomerDashboardResponseDTO(customerDashboardDTOList);
						response.setRequested_Action(true);
						return response;
					}
					
						customerDashboardDTOList.add(customerDashboardDTO);
						
						response=new ListOfCustomerDashboardResponseDTO(customerDashboardDTOList);
						response.setRequested_Action(true);
						return response;
					

				}	
			} 

		} catch (Exception ex) {
			response=new ResponceWithMessage(false,"Exception Occurred: "+ex.getMessage());
			return response;
		}finally{
			if(LOG.isInfoEnabled()){
				LOG.info("Returned Response is:");
				if(response!=null){
					LOG.info("Response Requested_Action: {} ",new Object[]{response.getRequested_Action()});
				}
				LOG.info("WebClientRestContollerAndroidApplication.GetCustomerDashboardData()--End");
			}
		}
	}



	@RequestMapping(method=RequestMethod.POST,value={"/Mobile/UpdateCustomerPictures"} )
	public BasicResponce UpdateCustomerPictures(@RequestParam(value = "Token") String Token,			
			@RequestParam(value = "idCardFront", required=false) MultipartFile idCardFront,
			@RequestParam(value = "idCardBack", required=false) MultipartFile idCardBack,
			@RequestParam(value = "idCardWithFace", required=false) MultipartFile idCardWithFace			
			) {

		if (LOG.isInfoEnabled()) {
			LOG.info("WebClientRestContollerAndroidApplication.UpdateCustomerPictures()--Start");
			LOG.info("Received Parameters are:");
			LOG.info("Token: {} ", new Object[] { Token });
		}
		BasicResponce response = null;
		try {
			Customer result=objLMSDAO.loginInfoByToken(Token);			
			if(result==null){
				return new ResponceWithMessage(false,"Unable to find session token");				
			} else {
				String idCardFrontPath=objServerUtils.storeFile(idCardFront);
				String idCardBackPath=objServerUtils.storeFile(idCardBack);
				String idCardWithFacePath=objServerUtils.storeFile(idCardWithFace);

				boolean result2=objLMSDAO.updatePicturePaths(Token, idCardFrontPath, idCardBackPath, idCardWithFacePath);

				if(result2) {
					response=new ResponceWithMessage(true,"Updated");
					return response;

				} else {
					response=new ResponceWithMessage(false,"Failed");
					return response;
				}
			} 

		} catch (Exception ex) {
			return new ResponceWithMessage(false,"Unknown Exception");

		}finally{
			if(LOG.isInfoEnabled()){
				LOG.info("Returned Response is:");
				if(response!=null){
					LOG.info("Response Requested_Action: {} ",new Object[]{response.getRequested_Action()});
				}
				LOG.info("WebClientRestContollerAndroidApplication.UpdateCustomerPictures()--End");
			}
		}
	}

	//Will remove
	@RequestMapping(method=RequestMethod.POST,value={"/Mobile/UpdateCustomerPicturesCustom"} )
	public BasicResponce UpdateCustomerPicturesCustom(@RequestParam(value = "Token") String Token,			
			@RequestParam(value = "idCardFront", required=true) MultipartFile idCardFront,
			@RequestParam(value = "idCardBack", required=true) MultipartFile idCardBack,
			@RequestParam(value = "idCardWithFace", required=true) MultipartFile idCardWithFace			
			) {

		if (LOG.isInfoEnabled()) {
			LOG.info("WebClientRestContollerAndroidApplication.UpdateCustomerPicturesCustom()--Start");
			LOG.info("Received Parameters are:");
			LOG.info("Token: {} ", new Object[] { Token });
		}
		BasicResponce response = null;
		try {
			Customer result=objLMSDAO.loginInfoByToken(Token);			
			if(result==null){
				return new ResponceWithMessage(false,"Unable to find session token");				
			} else {
				String idCardFrontPath=objServerUtils.storeFile(idCardFront);
				String idCardBackPath=objServerUtils.storeFile(idCardBack);
				String idCardWithFacePath=objServerUtils.storeFile(idCardWithFace);

				boolean result2=objLMSDAO.updatePicturePaths(Token, idCardFrontPath, idCardBackPath, idCardWithFacePath);

				if(result2) {
					response=new ResponceWithMessage(true,"Updated");
					return response;

				} else {
					response=new ResponceWithMessage(false,"Failed");
					return response;
				}
			} 

		} catch (Exception ex) {
			return new ResponceWithMessage(false,"Unknown Exception");

		}finally{
			if(LOG.isInfoEnabled()){
				LOG.info("Returned Response is:");
				if(response!=null){
					LOG.info("Response Requested_Action: {} ",new Object[]{response.getRequested_Action()});
				}
				LOG.info("WebClientRestContollerAndroidApplication.UpdateCustomerPicturesCustom()--End");
			}
		}
	}

	@GetMapping("/getUploadFile/{fileName:.+}")
	public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) {
		// Load file as Resource
		Resource resource = objServerUtils.getFileFromDisk(fileName);

		// Try to determine file's content type
		String contentType = null;
		try {
			contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
		} catch (IOException ex) {

		}

		// Fallback to the default content type if type could not be determined
		if(contentType == null) {
			contentType = "application/octet-stream";
		}

		return ResponseEntity.ok()
				.contentType(MediaType.parseMediaType(contentType))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
				.body(resource);
	}
	
	@GetMapping("/getCustomerPictures/{fileName:.+}")
	public ResponseEntity<Resource> getCustomerPictures(
			@PathVariable String fileName) {

			Resource resource = objServerUtils.getFileFromDisk(fileName);

			// Try to determine file's content type
			String contentType = null;

			// Fallback to the default content type if type could not be
			// determined
			if (contentType == null) {
				contentType = "application/octet-stream";
			}

			HttpHeaders headers = new HttpHeaders();
			headers.add("Content-Disposition", "inline; filename=\"" + resource.getFilename() + "\"");

			return ResponseEntity
					.ok()
					.headers(headers)
					.contentType(MediaType.IMAGE_JPEG)
					.body(resource);
	}



	@GetMapping("/downloadTermsAndConditionFile/{fileName:.+}")
	public ResponseEntity<Resource> downloadTermsAndConditionFile(
			@PathVariable String fileName) {
		// Load file as Resource

		Preferences preferences = null;
		try {
			preferences = objLMSDAO.getPreferenceValueUsingName(fileName);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (preferences != null) {
			Resource resource = objServerUtils.getFileFromDisk(preferences
					.getPreferenceValue());

			// Try to determine file's content type
			String contentType = null;

			// Fallback to the default content type if type could not be
			// determined
			if (contentType == null) {
				contentType = "application/octet-stream";
			}

			HttpHeaders headers = new HttpHeaders();
			headers.add("Content-Disposition", "inline; filename=\"" + resource.getFilename() + "\"");

			return ResponseEntity
					.ok()
					.headers(headers)
					.contentType(MediaType.APPLICATION_PDF)
					.body(resource);

		}else{
			return null;
		}

	}


	@RequestMapping(method=RequestMethod.POST,value={"/Mobile/GetCustomerLoansInfo"} )
	public BasicResponce GetCustomerLoansInfo(@RequestParam(value = "Token") String Token) {

		if (LOG.isInfoEnabled()) {
			LOG.info("WebClientRestContollerAndroidApplication.GetCustomerLoansInfo()--Start");
			LOG.info("Received Parameters are:");
			LOG.info("Token: {} ", new Object[] { Token });
		}
		BasicResponce response = null;

		try {
			Customer customer=objLMSDAO.loginInfoByToken(Token);			
			if(customer==null){
				return new ResponceWithMessage(false,"Unable to find session token");
			} else {

				List<DisbursedLoans> fromDB= objLMSDAO.getAllDisbursedLoansForCustomer(customer.getId());


				List<DisbursedLoanResponseDTO> toSend = new ArrayList<DisbursedLoanResponseDTO>();
				for(DisbursedLoans i:fromDB) {

					DisbursedLoanResponseDTO toAdd=new DisbursedLoanResponseDTO();

					toAdd.setId(i.getId());

					LoanApplicationsResponseDTO toPut=new LoanApplicationsResponseDTO();
					toPut.setId(i.getApplication().getId());

					toPut.setCustomer(new CustomerResponseDTO(i.getApplication().getCustomer().getId(), i.getApplication().getCustomer().getCellNoInString(), i.getApplication().getCustomer().getFullName(), i.getApplication().getCustomer().getPakistaniCNIC(), i.getApplication().getCustomer().getStatus(), i.getApplication().getCustomer().getPinCode()));
					
					Preferences objPreference = objLMSDAO.getPreferenceValueUsingName("serverBaseUrlPathForTermsAndConditionsFile");
					if(objPreference!=null && i.getApplication().getProduct().getTermsAndConditionFilePath()!=null){
						toPut.setProduct(new ProductResponseDTO(i.getApplication().getProduct().getId(), i.getApplication().getProduct().getProductName(), i.getApplication().getProduct().getProductCatagory(),objPreference.getPreferenceValue()+""+i.getApplication().getProduct().getTermsAndConditionFilePath()));
					}else{
						toPut.setProduct(new ProductResponseDTO(i.getApplication().getProduct().getId(), i.getApplication().getProduct().getProductName(), i.getApplication().getProduct().getProductCatagory(),null));
					}
					
					
					toPut.setWalletTypeId(i.getApplication().getWalletTypeId());
					toPut.setRequestedamount(i.getApplication().getRequestedamount());
					toPut.setStatus(i.getApplication().getStatus().name());

					if(i.getApplication()!=null && i.getApplication().getCreatedAt()!=null)
						toPut.setCreatedAt(utility.getDateFromTimeStamp(i.getApplication().getCreatedAt().toString()));
					if(i.getApplication()!=null && i.getApplication().getCreatedBy()!=null)
						toPut.setCreatedBy(new ApplicationUserResponseDTO(i.getApplication().getCreatedBy().getUserId(),i.getApplication().getCreatedBy().getDisplayName()));

					if(i.getApplication()!=null && i.getApplication().getUpdatedAt()!=null)
						toPut.setUpdatedAt(utility.getDateFromTimeStamp(i.getApplication().getUpdatedAt().toString()));
					if(i.getApplication()!=null && i.getApplication().getUpdatedBy()!=null)
						toPut.setUpdatedBy(new ApplicationUserResponseDTO(i.getApplication().getUpdatedBy().getUserId(),i.getApplication().getUpdatedBy().getDisplayName()));

					toAdd.setApplication(toPut);
					toAdd.setTransactionId(i.getTransactionId());
					toAdd.setLoanStatus(i.getLoanStatus());

					if(i.getDueDate()!=null)
						toAdd.setDueDate(utility.getDateFromTimeStamp(i.getDueDate().toString()));

					if(i.getGraceDueDate()!=null)
						toAdd.setGraceDueDate(utility.getDateFromTimeStamp(i.getGraceDueDate().toString()));

					toAdd.setOutstandingAmount(generalLedgerService.getLoanOutstandingBalance(i.getId()));

					if(i.getCreatedAt()!=null)
						toAdd.setCreatedAt(utility.getDateFromTimeStamp(i.getCreatedAt().toString()));
					if(i.getCreatedBy()!=null)
						toPut.setCreatedBy(new ApplicationUserResponseDTO(i.getCreatedBy().getUserId(),i.getCreatedBy().getDisplayName()));

					if(i.getUpdatedAt()!=null)
						toAdd.setUpdatedAt(utility.getDateFromTimeStamp(i.getUpdatedAt().toString()));
					if(i.getUpdatedBy()!=null)
						toPut.setUpdatedBy(new ApplicationUserResponseDTO(i.getUpdatedBy().getUserId(),i.getUpdatedBy().getDisplayName()));

					toSend.add(toAdd);
				}

				response=new ListOfDisbursedLoansResponseDTO(toSend);
				response.setRequested_Action(true);
				return response;
			} 

		} catch (Exception ex) {
			response=new ResponceWithMessage(false,"Exception Occurred: "+ex.getMessage());
			return response;
		}finally{
			if(LOG.isInfoEnabled()){
				LOG.info("Returned Response is:");
				if(response!=null){
					LOG.info("Response Requested_Action: {} ",new Object[]{response.getRequested_Action()});
				}
				LOG.info("WebClientRestContollerAndroidApplication.GetCustomerTransactionHistory()--End");
			}
		}
	}

	//Return only repayment Transactions
	@RequestMapping(method=RequestMethod.POST,value={"/Mobile/GetTransactionHistoryAgainstLoanId"} )
	public BasicResponce GetTransactionHistoryAgainstLoanId(@RequestParam(value = "Token") String Token,
			@RequestParam(value = "loanId") long loanId,
			@RequestParam(value = "walletTypeId") int walletTypeId) {

		if (LOG.isInfoEnabled()) {
			LOG.info("WebClientRestContollerAndroidApplication.GetTransactionHistoryAgainstLoanId()--Start");
			LOG.info("Received Parameters are:");
			LOG.info("Token: {} ", new Object[] { Token });
			LOG.info("Loan Id: {} ", new Object[] { loanId });
			LOG.info("Wallet Type Id: {} ", new Object[] { walletTypeId });
		}
		BasicResponce response = null;

		try {
			Customer result=objLMSDAO.loginInfoByToken(Token);			
			if(result==null){
				return new ResponceWithMessage(false,"Unable to find session token");
			} else {

				if(walletTypeId==1){
					List<GeneralLedgerResponseDTO> requiredGlEntries=generalLedgerService.getLoanEntriesFromGlByLoanId(Constants.GENERAL_LEDGER_ENTRY_FOR_ONE_LOAD_ACCOUNT, loanId);
					response=new ListOfGeneralLedgerEntriesResponseDTO(requiredGlEntries,generalLedgerDao.getTotalRepaymentAmountAgainstSpecificOneLoadWallet(loanId));
					response.setRequested_Action(true);
					return response;
				}else if(walletTypeId==2){
					//Right Now Total Amount is not Calculated incase of SIM SIM Wallet
					List<GeneralLedgerResponseDTO> requiredGlEntries=generalLedgerService.getLoanEntriesFromGlByLoanId(Constants.GENERAL_LEDGER_ENTRY_FOR_SIMSIM_ACCOUNT, loanId);
					response=new ListOfGeneralLedgerEntriesResponseDTO(requiredGlEntries);
					response.setRequested_Action(true);
					return response;
				}else{
					if(walletTypeId==3){
						//Right Now Total Amount is not Calculated incase of Jazz Cash Wallet
						List<GeneralLedgerResponseDTO> requiredGlEntries=generalLedgerService.getLoanEntriesFromGlByLoanId(Constants.GENERAL_LEDGER_ENTRY_FOR_JAZZCASH_ACCOUNT, loanId);
						response=new ListOfGeneralLedgerEntriesResponseDTO(requiredGlEntries);
						response.setRequested_Action(true);
						return response;
					}else{
						response=new ResponceWithMessage(false,"Unknown Wallet Type");
						return response;
					}
				}

			}	

		} catch (Exception ex) {
			response=new ResponceWithMessage(false,"Exception Occurred: "+ex.getMessage());
			return response;
		}finally{
			if(LOG.isInfoEnabled()){
				LOG.info("Returned Response is:");
				if(response!=null){
					LOG.info("Response Requested_Action: {} ",new Object[]{response.getRequested_Action()});
				}
				LOG.info("WebClientRestContollerAndroidApplication.GetTransactionHistoryAgainstLoanId()--End");
			}
		}
	}

	@RequestMapping(method=RequestMethod.POST,value={"/Mobile/GetAllTransactionHistoryAgainstLoanId"} )
	public BasicResponce GetAllTransactionHistoryAgainstLoanId(@RequestParam(value = "Token") String Token,
			@RequestParam(value = "loanId") long loanId,
			@RequestParam(value = "walletTypeId") int walletTypeId) {

		if (LOG.isInfoEnabled()) {
			LOG.info("WebClientRestContollerAndroidApplication.GetAllTransactionHistoryAgainstLoanId()--Start");
			LOG.info("Received Parameters are:");
			LOG.info("Token: {} ", new Object[] { Token });
			LOG.info("Loan Id: {} ", new Object[] { loanId });
			LOG.info("Wallet Type Id: {} ", new Object[] { walletTypeId });
		}
		BasicResponce response = null;
		List<GeneralLedgerResponseDTO> requiredGlEntries = new ArrayList<GeneralLedgerResponseDTO>();

		try {
			Customer result=objLMSDAO.loginInfoByToken(Token);			
			if(result==null){
				return new ResponceWithMessage(false,"Unable to find session token");
			} else {

				if(walletTypeId==1){
					requiredGlEntries.addAll(generalLedgerService.getLoanEntriesFromGlByLoanId(Constants.GENERAL_LEDGER_ENTRY_FOR_ONE_LOAD_ACCOUNT, loanId));
					requiredGlEntries.addAll(generalLedgerService.getLoanEntriesFromLoanReceivableGLByLoanId(loanId));

					response=new ListOfGeneralLedgerEntriesResponseDTO(requiredGlEntries,generalLedgerDao.getTotalPayableAmountFromLoanReceivableAgainstLoanId(loanId) ,generalLedgerDao.getTotalRepaymentAmountAgainstSpecificOneLoadWallet(loanId), generalLedgerDao.getOutstandingBalanceAgainstLoan(loanId));
					response.setRequested_Action(true);
					return response;

				}else if(walletTypeId==2){
					//Right Now Total Amount is not Calculated incase of SIM SIM Wallet
					requiredGlEntries=generalLedgerService.getLoanEntriesFromGlByLoanId(Constants.GENERAL_LEDGER_ENTRY_FOR_SIMSIM_ACCOUNT, loanId);

					List<GeneralLedgerResponseDTO> requiredLoanReceivableGlEntries=generalLedgerService.getLoanEntriesFromLoanReceivableGLByLoanId(loanId);
					requiredGlEntries.addAll(requiredLoanReceivableGlEntries);
					response=new ListOfGeneralLedgerEntriesResponseDTO(requiredGlEntries);
					response.setRequested_Action(true);
					return response;
				}else{
					if(walletTypeId==3){
						//Right Now Total Amount is not Calculated incase of Jazz Cash Wallet
						requiredGlEntries=generalLedgerService.getLoanEntriesFromGlByLoanId(Constants.GENERAL_LEDGER_ENTRY_FOR_JAZZCASH_ACCOUNT, loanId);

						List<GeneralLedgerResponseDTO> requiredLoanReceivableGlEntries=generalLedgerService.getLoanEntriesFromLoanReceivableGLByLoanId(loanId);
						requiredGlEntries.addAll(requiredLoanReceivableGlEntries);
						response=new ListOfGeneralLedgerEntriesResponseDTO(requiredGlEntries);
						response.setRequested_Action(true);
						return response;

					}else{
						response=new ResponceWithMessage(false,"Unknown Wallet Type");
						return response;
					}
				}
			}	
		} catch (Exception ex) {
			response=new ResponceWithMessage(false,"Exception Occurred: "+ex.getMessage());
			return response;
		}finally{
			if(LOG.isInfoEnabled()){
				LOG.info("Returned Response is:");
				if(response!=null){
					LOG.info("Response Requested_Action: {} ",new Object[]{response.getRequested_Action()});
				}
				LOG.info("WebClientRestContollerAndroidApplication.GetAllTransactionHistoryAgainstLoanId()--End");
			}
		}
	}
	
	
}
