package monami.lms.webclientrestcontollers;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import monami.lms.datadaos.ApplicationUserDAO;
import monami.lms.datadaos.LMSDAO;
import monami.lms.dataentities.ApplicationUsers;
import monami.lms.dataentities.Customer;
import monami.lms.dataentities.DisbursedLoans;
import monami.lms.dataentities.KYCAnswerSnapShotAtLoanApplication;
import monami.lms.dataentities.KYCQuestion;
import monami.lms.dataentities.LoanApplication;
import monami.lms.dataentities.LoanApplicationSelectedKYCQuestionsForReview;
import monami.lms.dataentities.Preferences;
import monami.lms.utilities.LoanApplicationStatus;
import monami.lms.dataentities.Product;
import monami.lms.dataentities.ProductAssumption;
import monami.lms.dataentities.ProductSpecification;
import monami.lms.dataentities.ProductSpecificationSnapShotAtLoanApplication;
import monami.lms.request.datadtos.GeneralLedgerRequestDTO;
import monami.lms.request.datadtos.KYCQuestionRequestDTO;
import monami.lms.responceentities.BasicResponce;
import monami.lms.responceentities.ReponceForWebTableData;
import monami.lms.responceentities.ResponceDTO;
import monami.lms.responceentities.ResponceWithMessage;
import monami.lms.responceentities.ResponceWithTokenAndUserDetails;
import monami.lms.responceentities.ResponceWithUserDetails;
import monami.lms.response.datadtos.ApplicationUserResponseDTO;
import monami.lms.response.datadtos.CustomerLoanSummaryResponseDTO;
import monami.lms.response.datadtos.CustomerResponseDTO;
import monami.lms.response.datadtos.DisbursedLoanResponseDTO;
import monami.lms.response.datadtos.DisbursedLoanSummaryResponseDTO;
import monami.lms.response.datadtos.GeneralLedgerResponseDTO;
import monami.lms.response.datadtos.KYCQuestionResponseDTO;
import monami.lms.response.datadtos.ListOfCustomerResponseDTO;
import monami.lms.response.datadtos.ListOfDisbursedLoansResponseDTO;
import monami.lms.response.datadtos.ListOfGeneralLedgerEntriesResponseDTO;
import monami.lms.response.datadtos.ListOfKYCQuestionResponseDTO;
import monami.lms.response.datadtos.ListOfLoanApplicationsResponseDTO;
import monami.lms.response.datadtos.ListOfProductAssumptionResponseDTO;
import monami.lms.response.datadtos.ListOfProductSpecificationResponseDTO;
import monami.lms.response.datadtos.LoanApplicationSummaryResponseDTO;
import monami.lms.response.datadtos.LoanApplicationsResponseDTO;
import monami.lms.response.datadtos.ProductAssumptionResponseDTO;
import monami.lms.response.datadtos.ProductResponseDTO;
import monami.lms.response.datadtos.ProductSpecificationResponseDTO;
import monami.lms.rest.serverwebinterface.ApplicationUserService;
import monami.lms.rest.serverwebinterface.GeneralLedgerService;
import monami.lms.rest.serverwebinterface.PrivilegeService;
import monami.lms.rest.serverwebinterface.ProductService;
import monami.lms.rest.serverwebinterface.RoleService;
import monami.lms.security.TokenUtil;
import monami.lms.serverutils.SMSandNotficationService;
import monami.lms.serverutils.ServerUtils;
import monami.lms.utilities.Constants;
import monami.lms.utilities.CustomerStatus;
import monami.lms.utilities.DisbursedLoanStatus;
import monami.lms.utilities.Utility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class WebClientRestContoller {

	Logger LOG = LoggerFactory.getLogger(WebClientRestContoller.class);

	@Autowired
	private ApplicationUserService objApplicationUserService; 

	@Autowired
	private ApplicationUserDAO objApplicationUserDAO;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	@Qualifier("customUserDetailsService")
	private UserDetailsService customUserDetailsService;

	@Autowired
	private RoleService objRoleService;

	@Autowired
	private PrivilegeService objPrivilegeService;
	
	@Autowired
	private ProductService objProductService;

	@Autowired 
	private ServerUtils objServerUtils;

	@Autowired 
	private LMSDAO objLMSDAO;

	@Autowired
	private GeneralLedgerService generalLedgerService;

	@Autowired
	private PlatformTransactionManager transactionManager;
	
	@Autowired
	private Utility utility;
	
	@Autowired
	private SMSandNotficationService objSMSandNotficationService;
	

	@RequestMapping(method=RequestMethod.POST,value="/sendSMS")
	public BasicResponce sendSMS(@RequestParam("to") String to,@RequestParam("message") String message){
		try {
			String result = objSMSandNotficationService.sendSMSAPI(to,message);

			System.out.println(result);
			ResponceWithMessage toReturn = new ResponceWithMessage(true,result);
			return toReturn ;
		} catch (Exception ex){
			ResponceWithMessage toReturn = new ResponceWithMessage(ex);
			return toReturn ;
		}

	}
	@RequestMapping(method=RequestMethod.POST,value="/LoginWithUsername")
	public BasicResponce LoginWithUsername(@RequestParam("username") String usernameforlogin,@RequestParam("password") String passwordforlogin){
		BasicResponce toReturn=null;

		LOG.info("WebClientRestContoller.LoginWithUsername--Start");
		LOG.info("Received Parameters are:");
		LOG.info("Username: {}",new Object[]{usernameforlogin});
		LOG.info("Password: {}",new Object[]{passwordforlogin});

		Date lastLoginTime = null;
		long diff = 0;
		long diffMinutes = 0;
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

		try {
			/*Preferences pref= objLMSDAO.getPreferenceValueUsingName("secretKey");
			if(pref!=null && pref.getPreferenceValue()!=null){
				String encryptedPassword= AES.encrypt(passwordforlogin, pref.getPreferenceValue());
				
				if(encryptedPassword!=null){*/
					UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(usernameforlogin, passwordforlogin);
					Authentication authentication = this.authenticationManager.authenticate(authenticationToken);
					SecurityContextHolder.getContext().setAuthentication(authentication);
					
					UserDetails userDetails = this.customUserDetailsService.loadUserByUsername(usernameforlogin);

					List<String> roles = new ArrayList<String>();

					for (GrantedAuthority authority : userDetails.getAuthorities()) {
						roles.add(authority.toString());
					}
					String token = TokenUtil.createToken();

					ApplicationUsers toUpdate=objApplicationUserDAO.getUserByUsernamePassword(userDetails.getUsername(), userDetails.getPassword());
					Date date = new Date(System.currentTimeMillis());  
					Date currentTime = formatter.parse(formatter.format(date));

					if (toUpdate.getLastloginAttempt() == null) {
						LocalDateTime now = LocalDateTime.now();
						toUpdate.setLastloginAttempt(objServerUtils.formatter.format(now));
						objApplicationUserDAO.updateUser(toUpdate);
					}
					lastLoginTime = formatter.parse(toUpdate.getLastloginAttempt());

					diff = lastLoginTime.getTime() - currentTime.getTime();
					diffMinutes = diff / (60 * 1000) % 60;

					if (toUpdate.getLoginCounter() > 3 && Math.abs(diffMinutes) < 15) {
						long remainigTime = 15-Math.abs(diffMinutes);
						if (remainigTime == 1) {
							toReturn = new ResponceWithMessage(false,"Blocked for " + remainigTime + " minute.");
						}
						else {
							toReturn = new ResponceWithMessage(true,"Blocked for " + remainigTime + " minutes.");
						}

						return toReturn ;
					}
					LocalDateTime now = LocalDateTime.now();
					toUpdate.setLastloginAttempt(objServerUtils.formatter.format(now));
					toUpdate.setLastAccess(objServerUtils.formatter.format(now));
					if (toUpdate.getAssignToken() == null || toUpdate.getAssignToken().trim().equals("")) {
						toUpdate.setAssignToken(token);
					}else {
						token = toUpdate.getAssignToken();
					}
					toUpdate.setLoginCounter(0);
					objApplicationUserDAO.updateUser(toUpdate);
					if (toUpdate.getAppliedTheme() == null) {
						toReturn = new ResponceWithTokenAndUserDetails(true,token, roles, toUpdate.getDisplayName(), userDetails.getUsername(),toUpdate.getUserId(), "Light");
					}
					else {
						toReturn = new ResponceWithTokenAndUserDetails(true,token, roles, toUpdate.getDisplayName(), userDetails.getUsername(),toUpdate.getUserId(), toUpdate.getAppliedTheme());
					}
				/*}else{
					toReturn = new ResponceWithMessage(false,"Failed to Encrypt Password");
				}
			}else{
				toReturn = new ResponceWithMessage(false,"Failed to Get Preference Value");
			}*/
		} catch (BadCredentialsException bce) {
			try {
				ApplicationUsers user =objApplicationUserDAO.getUserByUsername(usernameforlogin);
				if (user != null) {
					LocalDateTime now = LocalDateTime.now();
					user.setLastloginAttempt(objServerUtils.formatter.format(now));
					user.setLoginCounter(user.getLoginCounter()+1);

					objApplicationUserDAO.updateUser(user);
				}

				lastLoginTime = formatter.parse(user.getLastloginAttempt());

				Date date = new Date(System.currentTimeMillis());  
				Date currentTime = formatter.parse(formatter.format(date));

				diff = lastLoginTime.getTime() - currentTime.getTime();
				diffMinutes = diff / (60 * 1000) % 60;

				if (user.getLoginCounter() > 3 && Math.abs(diffMinutes) < 15) {
					long remainigTime = 15 - Math.abs(diffMinutes);
					if (remainigTime == 1) {
						toReturn = new ResponceWithMessage(false, "Blocked for " + remainigTime + " minute.");
					} else {
						toReturn = new ResponceWithMessage(false, "Blocked for " + remainigTime + " minutes.");
					}

					return toReturn;
				}
			} catch (Exception e) {
				// TODO: handle exception
			}

			toReturn = new ResponceWithMessage(false,"Invalid Credentials");
			return toReturn ;
		} catch (Exception e) {
		}finally{
			if(LOG.isInfoEnabled()){
				LOG.info("Returned Response is:");
				LOG.info("Response Requested_Action: {}",new Object[]{toReturn.getRequested_Action()});
				LOG.info("WebClientRestContoller.LoginWithUsername--End");
			}
		}
		return toReturn;
	}



	@RequestMapping(method=RequestMethod.POST, value="/LMSServer/ChangePassword")
	@PreAuthorize("hasAuthority('USER_MANAGEMENT_RW')")
	public BasicResponce ChangePassword(@RequestParam("username") String username, 
			@RequestParam("currentPassword") String currentPassword,
			@RequestParam("newPassword") String newPassword,
			@RequestParam("confirmNewPassword") String confirmNewPassword, 
			@RequestHeader("authString") String authString) {
		
		if(LOG.isInfoEnabled()){
			LOG.info("WebClientRestContoller.ChangePassword--Start");
			LOG.info("Received Parameters are:");
			LOG.info("Username: {}",new Object[]{username});
			LOG.info("Current Password: {}",new Object[]{currentPassword});
			LOG.info("New Password: {}",new Object[]{newPassword});
			LOG.info("Confirm New Password: {}",new Object[]{confirmNewPassword});
			LOG.info("authString Id: {}",new Object[]{authString});
		}

		ResponceWithMessage response=objApplicationUserService.changePassword(username, currentPassword, newPassword, confirmNewPassword, authString);

		if(LOG.isInfoEnabled()){
			LOG.info("Returned Response is:");
			LOG.info("Response Message: {}",new Object[]{response.getMessage()});
			LOG.info("Response Requested_Action: {}",new Object[]{response.getRequested_Action()});
			LOG.info("WebClientRestContoller.ChangePassword--End");
		}
		return response;
	}


	@RequestMapping(method=RequestMethod.POST, value="/LMSServer/AddUser")
	@PreAuthorize("hasAuthority('USER_MANAGEMENT_RW')")
	public BasicResponce AddUser(@RequestParam(value = "fullName") String fullName, 
			@RequestParam(value = "addusername") String addusername, 
			@RequestParam(value = "adduserpassword") String adduserpassword,
			@RequestParam(value = "adduserconfirmpassword") String adduserconfirmpassword, 
			@RequestParam(value = "newusertype") String newusertype, 
			@RequestParam(value = "newUserRole") String newUserRole, 
			@RequestHeader(value = "authString") String authString) {

		if(LOG.isInfoEnabled()){
			LOG.info("WebClientRestContoller.AddUser--Start");
			LOG.info("Received Parameters are:");
			LOG.info("Fullname: {}",new Object[]{fullName});
			LOG.info("Username: {}",new Object[]{addusername});
			LOG.info("User Password: {}",new Object[]{adduserpassword});
			LOG.info("User Confirm Password: {}",new Object[]{adduserconfirmpassword});
			LOG.info("User Type: {}",new Object[]{newusertype});
			LOG.info("User Role: {}",new Object[]{newUserRole});
			LOG.info("AuthString: {}",new Object[]{authString});
		}

		ResponceWithMessage response= objApplicationUserService.AddUser(fullName, addusername, adduserpassword, adduserconfirmpassword, newusertype,newUserRole, objLMSDAO.getApplicationUserByToken(authString));

		if(LOG.isInfoEnabled()){
			LOG.info("Returned Response is:");
			LOG.info("Response Message: {}",new Object[]{response.getMessage()});
			LOG.info("Response Requested_Action: {}",new Object[]{response.getRequested_Action()});
			LOG.info("WebClientRestContoller.AddUser--End");
		}
		return response;
	}

	@RequestMapping(method=RequestMethod.POST, value="/LMSServer/DeleteUser")
	@PreAuthorize("hasAuthority('USER_MANAGEMENT_RW')")
	public BasicResponce DeleteUser(@RequestParam(value = "userNameToDelete") String userNameToDelete, 
			@RequestParam(value = "currentLoggedInUser")String currentLoggedInUser) {

		if(LOG.isInfoEnabled()){
			LOG.info("WebClientRestContoller.DeleteUser--Start");
			LOG.info("Received Parameters are:");
			LOG.info("Username To Delete: {} ",new Object[]{userNameToDelete});
			LOG.info("Current Logged In User: {} ",new Object[]{currentLoggedInUser});
		}

		BasicResponce response=objApplicationUserService.DeleteUser(userNameToDelete, currentLoggedInUser);


		if(LOG.isInfoEnabled()){
			LOG.info("Returned Response is:");
			LOG.info("Response Requested_Action {} ",new Object[]{response.getRequested_Action()});
			LOG.info("WebClientRestContoller.DeleteUser--End");
		}
		return response;
	}

	@RequestMapping(method=RequestMethod.POST, value="/LMSServer/GetUserToUpdate")
	@PreAuthorize("hasAuthority('USER_MANAGEMENT_R') OR hasAuthority('USER_MANAGEMENT_RW')")
	public ResponceWithUserDetails GetUserToUpdate(@RequestParam(value = "userName") String userName) {

		if(LOG.isInfoEnabled()){
			LOG.info("WebClientRestContoller.GetUserToUpdate--Start");
			LOG.info("Received Parameters are:");
			LOG.info("Username: {} ",new Object[]{userName});
		}

		ResponceWithUserDetails response=objApplicationUserService.getUserToUpdate(userName);

		if(LOG.isInfoEnabled()){
			LOG.info("Returned Response is:");
			LOG.info("Response Requested_Action: {} ",new Object[]{response.getRequested_Action()});
			LOG.info("Response Username: {} ",new Object[]{response.getUsername()});
			LOG.info("Response DisplayName: {} ",new Object[]{response.getDisplayName()});
			LOG.info("Response Category: {} ",new Object[]{response.getCategory()});
			LOG.info("Response Role: {} ",new Object[]{response.getRole()});
			LOG.info("WebClientRestContoller.GetUserToUpdate--End");
		}

		return response;
	}

	@RequestMapping(method=RequestMethod.POST, value="/LMSServer/UpdateUser")
	@PreAuthorize("hasAuthority('USER_MANAGEMENT_RW')")
	public BasicResponce UpdateUser(@RequestParam(value = "username") String username, 
			@RequestParam(value = "displayName") String displayName, 
			@RequestParam(value = "category") String category, 
			@RequestParam(value = "role") String role,
			@RequestHeader(value = "authString") String authString) {

		if(LOG.isInfoEnabled()){
			LOG.info("WebClientRestContoller.UpdateUser--Start");
			LOG.info("Received Parameters are:");
			LOG.info("Username: {} ",new Object[]{username});
			LOG.info("DisplayName: {} ",new Object[]{displayName});
			LOG.info("Category: {} ",new Object[]{category});
			LOG.info("Role: {} ",new Object[]{role});
			LOG.info("Auth String: {} ",new Object[]{authString});
		}

		BasicResponce response=objApplicationUserService.UpdateUser(username, displayName ,category, role,objLMSDAO.getApplicationUserByToken(authString));

		if(LOG.isInfoEnabled()){
			LOG.info("Returned Response is:");
			LOG.info("Response Requested_Action: {} ",new Object[]{response.getRequested_Action()});
			LOG.info("WebClientRestContoller.UpdateUser--End");
		}

		return response;
	}

	@RequestMapping(method=RequestMethod.POST, value="/LMSServer/GetAllUsers")
	@PreAuthorize("hasAuthority('USER_MANAGEMENT_R') OR hasAuthority('USER_MANAGEMENT_RW')")
	public BasicResponce GetAllUsers(@RequestHeader("authString") String authString) {

		if(LOG.isInfoEnabled()){
			LOG.info("WebClientRestContoller.GetAllUsers--Start");
		}

		BasicResponce response= objApplicationUserService.getAllUsers();

		if(LOG.isInfoEnabled()){
			LOG.info("Returned Response is:");
			LOG.info("Response Requested_Action: {} ",new Object[]{response.getRequested_Action()});
			LOG.info("WebClientRestContoller.GetAllUsers--End");
		}

		return response;
	}
	
	@RequestMapping(method=RequestMethod.POST, value="/LMSServer/GetAllUsersWithDTOResponse")
	@PreAuthorize("hasAuthority('USER_MANAGEMENT_R') OR hasAuthority('USER_MANAGEMENT_RW')")
	public BasicResponce GetAllUsersWithDTOResponse(@RequestHeader("authString") String authString) {

		if(LOG.isInfoEnabled()){
			LOG.info("WebClientRestContoller.GetAlGetAllUsersWithDTOResponselUsers--Start");
		}

		BasicResponce response= objApplicationUserService.getAllUsersWithDTOResponse();

		if(LOG.isInfoEnabled()){
			LOG.info("Returned Response is:");
			LOG.info("Response Requested_Action: {} ",new Object[]{response.getRequested_Action()});
			LOG.info("WebClientRestContoller.GetAllUsersWithDTOResponse--End");
		}

		return response;
	}



	@RequestMapping(method=RequestMethod.POST, value="/LMSServer/AddRole")
	@PreAuthorize("hasAuthority('ROLE_MANAGEMENT_RW')")
	public BasicResponce AddRole(@RequestParam(value = "roleName") String roleName,
			@RequestParam(value = "roleDescription") String roleDescription, 
			@RequestParam(value = "privilegeNames") String privilegeNames,
			@RequestHeader(value = "authString") String authString) {

		if(LOG.isInfoEnabled()){
			LOG.info("WebClientRestContoller.AddRole--Start");
			LOG.info("Received Parameters are:");
			LOG.info("Role Name: {} ",new Object[]{roleName});
			LOG.info("Role Description: {} ",new Object[]{roleDescription});
			LOG.info("Privilege Names: {} ",new Object[]{privilegeNames});
			LOG.info("Auth String: {} ",new Object[]{authString});
		}

		ResponceWithMessage response=objRoleService.AddRole(roleName, roleDescription, privilegeNames,objLMSDAO.getApplicationUserByToken(authString));

		if(LOG.isInfoEnabled()){
			LOG.info("Returned Response is:");
			LOG.info("Response Message: {} ",new Object[]{response.getMessage()});
			LOG.info("Response Requested_Action: {} ",new Object[]{response.getRequested_Action()});
			LOG.info("WebClientRestContoller.AddRole--End");
		}

		return response;

	}

	//Added by Abdul Majid--Start
	@RequestMapping(method=RequestMethod.POST, value="/LMSServer/UpdateRole")
	@PreAuthorize("hasAuthority('ROLE_MANAGEMENT_RW')")
	public BasicResponce UpdateRole(@RequestParam(value = "oldRoleName") String oldRoleName, 
			@RequestParam(value = "newRoleName") String newRoleName, 
			@RequestParam(value = "roleDescription") String roleDescription,
			@RequestParam(value = "privilegeNames") String privilegeNames,
			@RequestHeader(value = "authString") String authString) {

		if(LOG.isInfoEnabled()){
			LOG.info("WebClientRestContoller.UpdateRole--Start");
			LOG.info("Received Parameters are:");
			LOG.info("Old Role Name: {} ",new Object[]{oldRoleName});
			LOG.info("New Role Name: {} ",new Object[]{newRoleName});
			LOG.info("Role Description: {} ",new Object[]{roleDescription});
			LOG.info("Privilege Name: {} ",new Object[]{privilegeNames});
			LOG.info("Auth String: {} ",new Object[]{authString});
		}		

		BasicResponce response= objRoleService.UpdateRole(oldRoleName, newRoleName ,roleDescription, privilegeNames,objLMSDAO.getApplicationUserByToken(authString));

		if(LOG.isInfoEnabled()){
			LOG.info("Returned Response is:");
			LOG.info("Response Requested_Action: {} ",new Object[]{response.getRequested_Action()});
			LOG.info("WebClientRestContoller.UpdateRole--End");
		}

		return response;
	}
	//Added by Abdul Majid--End

	//Added by Abdul Majid--Start
	@RequestMapping(method=RequestMethod.POST, value="/LMSServer/UpdateRolePermissions")
	@PreAuthorize("hasAuthority('ROLE_MANAGEMENT_RW')")
	public BasicResponce UpdateRolePermissions(@RequestParam(value = "roleName") String roleName, 
			@RequestParam(value = "roleDescription") String roleDescription,
			@RequestParam(value = "privilegeNames") String privilegeNames,
			@RequestHeader(value = "authString") String authString) {

		if(LOG.isInfoEnabled()){
			LOG.info("WebClientRestContoller.UpdateRolePermissions--Start");
			LOG.info("Received Parameters are:");
			LOG.info("Role Name: {} ",new Object[]{roleName});
			LOG.info("Role Description: {} ",new Object[]{roleDescription});
			LOG.info("Privilege Name: {} ",new Object[]{privilegeNames});
			LOG.info("Auth String: {} ",new Object[]{authString});
		}	

		ResponceWithMessage response= objRoleService.UpdateRolePermissions(roleName ,roleDescription, privilegeNames,objLMSDAO.getApplicationUserByToken(authString));


		if(LOG.isInfoEnabled()){
			LOG.info("Returned Response is:");
			LOG.info("Response Requested_Action: {} ",new Object[]{response.getRequested_Action()});
			LOG.info("Response Message: {} ",new Object[]{response.getMessage()});
			LOG.info("WebClientRestContoller.UpdateRolePermissions--End");
		}

		return response;
	}
	//Added by Abdul Majid--End



	@RequestMapping(method=RequestMethod.POST, value="/LMSServer/GetAllRoles")
	@PreAuthorize("hasAuthority('ROLE_MANAGEMENT_R') OR hasAuthority('ROLE_MANAGEMENT_RW')")
	public BasicResponce GetAllRoles(@RequestHeader("authString") String authString) {

		if(LOG.isInfoEnabled()){
			LOG.info("WebClientRestContoller.GetAllRoles--Start");
		}

		BasicResponce response= objRoleService.getAllRoles();

		if(LOG.isInfoEnabled()){
			LOG.info("Returned Response is:");
			LOG.info("Response Requested_Action: {} ",new Object[]{response.getRequested_Action()});
			LOG.info("WebClientRestContoller.GetAllRoles--End");
		}

		return response;
	}
	
	
	@RequestMapping(method=RequestMethod.POST, value="/LMSServer/GetAllRolesWithDTOResponse")
	@PreAuthorize("hasAuthority('ROLE_MANAGEMENT_R') OR hasAuthority('ROLE_MANAGEMENT_RW')")
	public BasicResponce GetAllRolesWithDTOResponse(@RequestHeader("authString") String authString) {

		if(LOG.isInfoEnabled()){
			LOG.info("WebClientRestContoller.GetAllRolesWithDTOResponse--Start");
		}

		BasicResponce response= objRoleService.getAllRolesWithDTOResponse();

		if(LOG.isInfoEnabled()){
			LOG.info("Returned Response is:");
			LOG.info("Response Requested_Action: {} ",new Object[]{response.getRequested_Action()});
			LOG.info("WebClientRestContoller.GetAllRolesWithDTOResponse--End");
		}

		return response;
	}



	@RequestMapping(method=RequestMethod.POST, value="/LMSServer/AddPrivilege")
	@PreAuthorize("hasAuthority('PRIV_MANAGEMENT_RW')")
	public BasicResponce AddPrivilege(@RequestParam(value = "privilegeName") String privilegeName,
			@RequestHeader(value = "authString") String authString) {

		if(LOG.isInfoEnabled()){
			LOG.info("WebClientRestContoller.AddPrivilege--Start");
			LOG.info("Received Parameters are:");
			LOG.info("Privilege Name: {} ",new Object[]{privilegeName});
			LOG.info("Auth String: {} ",new Object[]{authString});
		}

		ResponceWithMessage response= objPrivilegeService.AddPrivilege(privilegeName,objLMSDAO.getApplicationUserByToken(authString));

		if(LOG.isInfoEnabled()){
			LOG.info("Returned Response is:");
			LOG.info("Response Message: {} ",new Object[]{response.getMessage()});
			LOG.info("Response Requested_Action: {} ",new Object[]{response.getRequested_Action()});
			LOG.info("WebClientRestContoller.AddPrivilege--End");
		}

		return response;
	}

	@RequestMapping(method=RequestMethod.POST, value="/LMSServer/UpdatePrivilege")
	@PreAuthorize("hasAuthority('PRIV_MANAGEMENT_RW')")
	public BasicResponce UpdatePrivilege(@RequestParam(value = "privilegeName") String oldPrivilegeName, 
			@RequestParam(value = "privilegeName") String newPrivilegeName,
			@RequestHeader(value = "authString") String authString) {

		if(LOG.isInfoEnabled()){
			LOG.info("WebClientRestContoller.UpdatePrivilege--Start");
			LOG.info("Received Parameters are:");
			LOG.info("Old Privilege Name: {} ",new Object[]{oldPrivilegeName});
			LOG.info("New Privilege Name: {} ",new Object[]{newPrivilegeName});
			LOG.info("Auth String: {} ",new Object[]{authString});
		}

		BasicResponce response= objPrivilegeService.UpdatePrivilege(oldPrivilegeName, newPrivilegeName,objLMSDAO.getApplicationUserByToken(authString));

		if(LOG.isInfoEnabled()){
			LOG.info("Returned Response is:");
			LOG.info("Response Requested_Action: {} ",new Object[]{response.getRequested_Action()});
			LOG.info("WebClientRestContoller.UpdatePrivilege--End");
		}

		return response;
	}

	@RequestMapping(method=RequestMethod.POST, value="/LMSServer/DeletePrivilege")
	@PreAuthorize("hasAuthority('PRIV_MANAGEMENT_RW')")
	public BasicResponce DeletePrivilege(@RequestParam(value = "privilegeName") String privilegeName) {

		if(LOG.isInfoEnabled()){
			LOG.info("WebClientRestContoller.DeletePrivilege--Start");
			LOG.info("Received Parameters are:");
			LOG.info("Old Privilege Name: {} ",new Object[]{privilegeName});
		}

		BasicResponce response=  objPrivilegeService.DeletePrivilege(privilegeName);


		if(LOG.isInfoEnabled()){
			LOG.info("Returned Response is:");
			LOG.info("Response Requested_Action: {} ",new Object[]{response.getRequested_Action()});
			LOG.info("WebClientRestContoller.DeletePrivilege--End");
		}
		return response;
	}

	@RequestMapping(method=RequestMethod.POST, value="/LMSServer/GetAllPrivileges")
	@PreAuthorize("hasAuthority('PRIV_MANAGEMENT_R') OR hasAuthority('PRIV_MANAGEMENT_RW')")
	public BasicResponce GetAllPrivileges(@RequestHeader("authString") String authString) {
		if(LOG.isInfoEnabled()){
			LOG.info("WebClientRestContoller.GetAllPrivileges--Start");
		}

		BasicResponce response= objPrivilegeService.getAllPrivileges();

		if(LOG.isInfoEnabled()){
			LOG.info("Returned Response is:");
			LOG.info("Response Requested_Action: {} ",new Object[]{response.getRequested_Action()});
			LOG.info("WebClientRestContoller.GetAllPrivileges--End");
		}
		return response;
	}
	
	@RequestMapping(method=RequestMethod.POST, value="/LMSServer/GetAllPrivilegesWithDTOResponse")
	@PreAuthorize("hasAuthority('PRIV_MANAGEMENT_R') OR hasAuthority('PRIV_MANAGEMENT_RW')")
	public BasicResponce GetAllPrivilegesWithDTOResponse(@RequestHeader("authString") String authString) {
		if(LOG.isInfoEnabled()){
			LOG.info("WebClientRestContoller.GetAllPrivilegesWithDTOResponse--Start");
		}

		BasicResponce response= objPrivilegeService.getAllPrivilegesWithDTOResponse();

		if(LOG.isInfoEnabled()){
			LOG.info("Returned Response is:");
			LOG.info("Response Requested_Action: {} ",new Object[]{response.getRequested_Action()});
			LOG.info("WebClientRestContoller.GetAllPrivilegesWithDTOResponse--End");
		}
		return response;
	}

	@RequestMapping(method=RequestMethod.POST, value="/LMSServer/GetServerTime")
	public BasicResponce GetServerTime(@RequestHeader("authString") String authString) {

		long now = (Instant.now().toEpochMilli());

		ResponceWithMessage serverTime = new ResponceWithMessage();
		serverTime.setRequested_Action(true);
		serverTime.setMessage(String.valueOf(now));

		return serverTime;

	}

	@RequestMapping(method=RequestMethod.POST, value="/LMSServer/getCustomerList")
	@PreAuthorize("hasAuthority('CUSTOMER_MANAGEMENT_R') OR hasAuthority('CUSTOMER_MANAGEMENT_RW')")
	public BasicResponce getCustomerList(@RequestHeader("authString") String authString) {

		if(LOG.isInfoEnabled()){
			LOG.info("WebClientRestContoller.getCustomerList--Start");
		}

		BasicResponce response = null;
		try {
			List<Customer> fromDB=objLMSDAO.getAllCustomers();			
			ArrayList <String> headers=new ArrayList<String>();
			headers.add("MID");
			headers.add("CNIC");
			headers.add("CELLNO");		
			headers.add("OTP INFO");	
			headers.add("STATUS");	
			headers.add("ONELOADSTATUS");

			headers.add("CreatedAt");
			headers.add("CreatedBy");
			headers.add("UpdatedAt");
			headers.add("UpdatedBy");

			ArrayList<List<String>> table_Data = new ArrayList<List<String>>();			
			for(Customer i:fromDB) {
				ArrayList <String> dataRow=new ArrayList<String>();
				dataRow.add(i.getId()+"");
				dataRow.add(i.getPakistaniCNIC()+"");
				dataRow.add(i.getCellNoInString()+"");
				dataRow.add(i.getOtpStatus());
				dataRow.add(i.getStatus()+"");
				
				if(i.isOneLoadCustomerStatus()){
					dataRow.add("Y");
				}else{
					dataRow.add("N");
				}

				dataRow.add(i.getCreatedAt()+"");
				if(i.getCreatedBy()!=null){
					dataRow.add(i.getCreatedBy().getDisplayName());
				}else{
					dataRow.add("");
				}
				
				dataRow.add(i.getUpdatedAt()+"");
				if(i.getUpdatedBy()!=null){
					dataRow.add(i.getUpdatedBy().getDisplayName());
				}else{
					dataRow.add("");
				}

				table_Data.add(dataRow);
			}			
			response= new ReponceForWebTableData(true,"All Customers",headers,table_Data);
			return response;
		} catch (Exception ex) {
			response= new ResponceWithMessage(ex);
			return response;
		}finally{

			if(LOG.isInfoEnabled()){
				LOG.info("Returned Response is:");
				if(response!=null)
					LOG.info("Response Requested_Action: {} ",new Object[]{response.getRequested_Action()});
				LOG.info("WebClientRestContoller.getCustomerList--End");
			}
		}

	}
	
	//GetCustomerList Service With DTO Response
	@RequestMapping(method=RequestMethod.POST, value="/LMSServer/getCustomerListWithDTOResponse")
	@PreAuthorize("hasAuthority('CUSTOMER_MANAGEMENT_R') OR hasAuthority('CUSTOMER_MANAGEMENT_RW')")
	public BasicResponce getCustomerListWithDTOResponse(@RequestHeader("authString") String authString) {

		if(LOG.isInfoEnabled()){
			LOG.info("WebClientRestContoller.getCustomerListWithDTOResponse--Start");
		}

		BasicResponce authResp = null;
		try {
			List<Customer> fromDB= objLMSDAO.getAllCustomers();
			
			List<CustomerResponseDTO> toSend= new ArrayList<CustomerResponseDTO>();
			
			if(fromDB!=null && fromDB.size()>0){
				
				for(Customer i:fromDB) {

					CustomerResponseDTO toAdd=new CustomerResponseDTO();
					toAdd.setId(i.getId());
					toAdd.setFullName(i.getFullName());
					toAdd.setCellNo(i.getCellNoInString());
					toAdd.setCnic(i.getPakistaniCNIC());
					toAdd.setStatus(i.getStatus());
					toAdd.setOneLoadStatus(i.isOneLoadCustomerStatus());
					
					if(i.getCreatedAt()!=null)
						toAdd.setCreatedAt(utility.getDateFromTimeStamp(i.getCreatedAt().toString()));
					if(i.getCreatedBy()!=null)
						toAdd.setCreatedBy(new ApplicationUserResponseDTO(i.getCreatedBy().getUserId(), i.getCreatedBy().getDisplayName()));
					
					if(i.getUpdatedAt()!=null)
						toAdd.setUpdatedAt(utility.getDateFromTimeStamp(i.getUpdatedAt().toString()));
					if(i.getUpdatedBy()!=null)
						toAdd.setUpdatedBy(new ApplicationUserResponseDTO(i.getUpdatedBy().getUserId(), i.getUpdatedBy().getDisplayName()));

					toSend.add(toAdd);
				}
				
				authResp=new ListOfCustomerResponseDTO(toSend);
				return authResp;
			}else{
				authResp= new ResponceWithMessage(false,"No Data Found");
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
				LOG.info("WebClientRestContoller.getCustomerListWithDTOResponse--End");
			}
		}

	}
	

	private static int[] stringToIntArray(String str){

		String parts[]=str.split(",");

		int[] ret = new int[parts.length];
		int counter=0;
		for(String i:parts){
			ret[counter++]=(Integer.parseInt(i));
		}

		return ret;
	}

	@RequestMapping(method=RequestMethod.POST,value="/LMSServer/AddProduct")
	@PreAuthorize("hasAuthority('PRODUCT_MANAGEMENT_RW')")
	public BasicResponce AddProduct(@RequestParam(value = "productCatagory") String productCatagory, 
			@RequestParam(value = "productName") String productName,
			@RequestParam(value = "KYCQuestionIds") String KYCQuestionIds,
			@RequestParam(value = "assumptionsIds")  String AssumptionsIds,
			@RequestParam(value = "assumptionsValues")  String AssumptionsValues,
			@RequestHeader(value = "authString") String authString) {	

		if(LOG.isInfoEnabled()){
			LOG.info("WebClientRestContoller.AddProduct--Start");
			LOG.info("Received Parameters are:");
			LOG.info("Product Category: {} ",new Object[]{productCatagory});
			LOG.info("Product Name: {} ",new Object[]{productName});
			LOG.info("KYC Question IDs: {} ",new Object[]{KYCQuestionIds});
			LOG.info("Assumption IDs: {} ",new Object[]{AssumptionsIds});
			LOG.info("Assumption Values: {} ",new Object[]{AssumptionsValues});
			LOG.info("Auth String: {} ",new Object[]{authString});
		}

		BasicResponce response = null;
		try {

			int KYCQuestionIdsA[] = stringToIntArray(KYCQuestionIds);
			int AssumptionsIdsA[] = stringToIntArray(AssumptionsIds);

			String AssumptionsValuesA[] = AssumptionsValues.split(",");


			if(AssumptionsIdsA!=null && AssumptionsValuesA!=null){
				if(AssumptionsIdsA.length!=AssumptionsValuesA.length){
					response=new ResponceWithMessage(false,"Length of AssumptionsIds and AssumptionsValues should be the same");
					return response;
				}
			}

			int result=objLMSDAO.addNewProduct(productCatagory, productName,KYCQuestionIdsA,AssumptionsIdsA,AssumptionsValuesA,objLMSDAO.getApplicationUserByToken(authString));
			if(result>0){
				response=new BasicResponce(true);
				return response;
			} else if(result==-14) {
				response=new ResponceWithMessage(false,"Could not find all assumption Ids");
				return response;
			} else if(result==-15) {
				response=new ResponceWithMessage(false,"Could not find all KYCQuestion Ids");
				return response;
			} else if(result==-16) {
				response=new ResponceWithMessage(false,"Could not add Product Specification");
				return response;
			} else {
				response=new BasicResponce(false);
				return response;
			}

		} catch (Exception ex) {
			response=new ResponceWithMessage(ex);
			return response;
		}finally{
			if(LOG.isInfoEnabled()){
				LOG.info("Returned Response is:");
				if(response!=null)
					LOG.info("Response Requested_Action: {} ",new Object[]{response.getRequested_Action()});
				LOG.info("WebClientRestContoller.AddProduct--End");
			}
		}

	}
	
	
	@RequestMapping(method=RequestMethod.POST,value="/LMSServer/AddProductWithTermsAndConditionFile")
	@PreAuthorize("hasAuthority('PRODUCT_MANAGEMENT_RW')")
	public BasicResponce AddProductWithTermsAndConditionFile(@RequestParam(value = "productCatagory") String productCatagory, 
			@RequestParam(value = "productName") String productName,
			@RequestParam(value = "KYCQuestionIds") String KYCQuestionIds,
			@RequestParam(value = "assumptionsIds")  String AssumptionsIds,
			@RequestParam(value = "assumptionsValues")  String AssumptionsValues,
			@RequestParam(value = "termsAndConditionFile", required=true) MultipartFile termsAndConditionFile,
			@RequestHeader(value = "authString") String authString) {	

		if(LOG.isInfoEnabled()){
			LOG.info("WebClientRestContoller.AddProductWithTermsAndConditionFile--Start");
			LOG.info("Received Parameters are:");
			LOG.info("Product Category: {} ",new Object[]{productCatagory});
			LOG.info("Product Name: {} ",new Object[]{productName});
			LOG.info("KYC Question IDs: {} ",new Object[]{KYCQuestionIds});
			LOG.info("Assumption IDs: {} ",new Object[]{AssumptionsIds});
			LOG.info("Assumption Values: {} ",new Object[]{AssumptionsValues});
			LOG.info("Terms and Condition File : {} ",new Object[]{termsAndConditionFile!=null});
			LOG.info("Auth String: {} ",new Object[]{authString});
		}

		BasicResponce response = null;
		try {

			int KYCQuestionIdsA[] = stringToIntArray(KYCQuestionIds);
			int AssumptionsIdsA[] = stringToIntArray(AssumptionsIds);
			String AssumptionsValuesA[] = AssumptionsValues.split(",");

			if(AssumptionsIdsA!=null && AssumptionsValuesA!=null){
				if(AssumptionsIdsA.length!=AssumptionsValuesA.length){
					response=new ResponceWithMessage(false,"Length of AssumptionsIds and AssumptionsValues should be the same");
					return response;
				}
			}
			
			String requiredFilePath=objServerUtils.storeFile(termsAndConditionFile);

			int result=objLMSDAO.addNewProductWithTermsAndConditionFile(productCatagory, productName,KYCQuestionIdsA,AssumptionsIdsA,AssumptionsValuesA,requiredFilePath, objLMSDAO.getApplicationUserByToken(authString));
			if(result>0){
				response=new BasicResponce(true);
				return response;
			} else if(result==-14) {
				response=new ResponceWithMessage(false,"Could not find all assumption Ids");
				return response;
			} else if(result==-15) {
				response=new ResponceWithMessage(false,"Could not find all KYCQuestion Ids");
				return response;
			} else if(result==-16) {
				response=new ResponceWithMessage(false,"Could not add Product Specification");
				return response;
			} else {
				response=new BasicResponce(false);
				return response;
			}

		} catch (Exception ex) {
			response=new ResponceWithMessage(ex);
			return response;
		}finally{
			if(LOG.isInfoEnabled()){
				LOG.info("Returned Response is:");
				if(response!=null)
					LOG.info("Response Requested_Action: {} ",new Object[]{response.getRequested_Action()});
				LOG.info("WebClientRestContoller.AddProductWithTermsAndConditionFile--End");
			}
		}

	}
	

	//Customization Code by Abdul Majid---Start
	@RequestMapping(method=RequestMethod.POST,value="/LMSServer/UpdateProduct")
	@PreAuthorize("hasAuthority('PRODUCT_MANAGEMENT_RW')")
	public BasicResponce UpdateProduct(@RequestParam(value = "productId") String productId,
			@RequestParam(value = "productCatagory") String productCatagory, 
			@RequestParam(value = "productName") String productName,
			@RequestParam(value = "KYCQuestionIds") String KYCQuestionIds,
			@RequestParam(value = "assumptionsIds")  String AssumptionsIds,
			@RequestParam(value = "assumptionsValues")  String AssumptionsValues,
			@RequestHeader(value = "authString") String authString) {		

		if(LOG.isInfoEnabled()){
			LOG.info("WebClientRestContoller.UpdateProduct--Start");
			LOG.info("Received Parameters are:");
			LOG.info("Product Id: {} ",new Object[]{productId});
			LOG.info("Product Category: {} ",new Object[]{productCatagory});
			LOG.info("Product Name: {} ",new Object[]{productName});
			LOG.info("KYC Question IDs: {} ",new Object[]{KYCQuestionIds});
			LOG.info("Assumption IDs: {} ",new Object[]{AssumptionsIds});
			LOG.info("Assumption Values: {} ",new Object[]{AssumptionsValues});
			LOG.info("authString: {} ",new Object[]{authString});
		}


		BasicResponce response = null;
		try {

			int KYCQuestionIdsA[] = stringToIntArray(KYCQuestionIds);
			int AssumptionsIdsA[] = stringToIntArray(AssumptionsIds);

			String AssumptionsValuesA[] = AssumptionsValues.split(",");


			if(AssumptionsIdsA!=null && AssumptionsValuesA!=null){
				if(AssumptionsIdsA.length!=AssumptionsValuesA.length){
					response=new ResponceWithMessage(false,"Length of AssumptionsIds and AssumptionsValues should be the same");
					return response;
				}
			}

			int result=objLMSDAO.updateProduct(productId,productCatagory, productName,KYCQuestionIdsA,AssumptionsIdsA,AssumptionsValuesA,objLMSDAO.getApplicationUserByToken(authString));
			if(result>0){
				response=new BasicResponce(true);
				return response;
			} else if(result==-14) {
				response=new ResponceWithMessage(false,"Could not find all assumption Ids");
				return response;
			} else if(result==-15) {
				response=new ResponceWithMessage(false,"Could not find all KYCQuestion Ids");
				return response;
			} else if(result==-16) {
				response=new ResponceWithMessage(false,"Could not add Product Specification");
				return response;
			} else {
				response=new BasicResponce(false);
				return response;
			}

		} catch (Exception ex) {
			response=new ResponceWithMessage(ex);
			return response;
		}finally{
			if(LOG.isInfoEnabled()){
				LOG.info("Returned Response is:");
				if(response!=null)
					LOG.info("Response Requested_Action: {} ",new Object[]{response.getRequested_Action()});
				LOG.info("WebClientRestContoller.UpdateProduct--End");
			}
		}

	}

	@RequestMapping(method=RequestMethod.POST,value="/LMSServer/AddKYCQuestion")
	@PreAuthorize("hasAuthority('KYC_MANAGEMENT_RW')")
	public BasicResponce AddKYCQuestion(@RequestParam(value = "questionToAsk") String questionToAsk,
			@RequestParam(value = "answerType") String answerType,
			@RequestParam(value = "listOfPossibleAnswers", required=false) String listOfPossibleAnswers,
			@RequestParam(value = "catagory") String catagory,
			@RequestParam(value = "expiryInDays") Integer expiryInDays,
			@RequestParam(value = "mandatoryStatus") boolean mandatoryStatus,
			@RequestHeader(value = "authString") String authString) {		

		if(LOG.isInfoEnabled()){
			LOG.info("WebClientRestContoller.AddKYCQuestion--Start");
			LOG.info("Received Parameters are:");
			LOG.info("Question to Ask: {} ",new Object[]{questionToAsk});
			LOG.info("Answer Type: {} ",new Object[]{answerType});
			LOG.info("List of Possible Answers: {} ",new Object[]{listOfPossibleAnswers});
			LOG.info("Category: {} ",new Object[]{catagory});
			LOG.info("Expiry Days: {} ",new Object[]{expiryInDays});
			LOG.info("Mandatory Status: {} ",new Object[]{mandatoryStatus});
			LOG.info("Auth String: {} ",new Object[]{authString});
		}


		BasicResponce response = null;

		try {
			objLMSDAO.addNewKYCQuestion(questionToAsk, answerType, listOfPossibleAnswers, catagory, mandatoryStatus, expiryInDays, objLMSDAO.getApplicationUserByToken(authString));

			response=new BasicResponce(true);
			return response;
		} catch (Exception ex) {
			response=new ResponceWithMessage(ex);
			return response;
		}finally{
			if(LOG.isInfoEnabled()){
				LOG.info("Returned Response is:");
				if(response!=null)
					LOG.info("Response Requested_Action: {} ",new Object[]{response.getRequested_Action()});
				LOG.info("WebClientRestContoller.AddKYCQuestion--End");
			}
		}
	}

	@RequestMapping(method=RequestMethod.POST,value="/LMSServer/GetKYCQuestions")
	@PreAuthorize("hasAuthority('KYC_MANAGEMENT_R') OR hasAuthority('KYC_MANAGEMENT_RW')")
	public BasicResponce GetKYCQuestions(@RequestParam(value = "productId", required = false) Integer productId,
			@RequestHeader("authString") String authString) {		

		if(LOG.isInfoEnabled()){
			LOG.info("WebClientRestContoller.GetKYCQuestions--Start");
			LOG.info("Received Parameters are:");
			LOG.info("Product Id (Optional): {} ",new Object[]{productId});
		}

		BasicResponce response = null;

		try {

			ArrayList<KYCQuestion> toParse=new ArrayList<KYCQuestion>();

			if(productId==null){
				toParse=objLMSDAO.getAllKYCQuestions();
			} else {
				toParse=objLMSDAO.getKYCQuestionsAgainstProduct(productId);
			}

			ArrayList <String> headers=new ArrayList<String>();
			headers.add("QID");
			headers.add("Question");
			headers.add("AnswerType");
			headers.add("ListOfPossibleAnswer");
			headers.add("Category");
			headers.add("MandatoryStatus");
			headers.add("ExpiryTime(Days)");

			headers.add("CreatedAt");
			headers.add("CreatedBy");
			headers.add("UpdatedAt");
			headers.add("UpdatedBy");



			ArrayList<List<String>> table_Data = new ArrayList<List<String>>();	
			if(toParse!=null){
				for(KYCQuestion i:toParse) {
					ArrayList <String> dataRow=new ArrayList<String>();
					dataRow.add(i.getId()+"");
					dataRow.add(i.getQuestionToAsk()+"");

					dataRow.add(i.getAnswerType()+"");
					dataRow.add(i.getListOfPossibleAnswers()+"");
					dataRow.add(i.getCatagory()+"");

					if(i.isMandatoryStatus()){
						dataRow.add("Required");
					}else{
						dataRow.add("Optional");
					}

					dataRow.add(i.getExpiryInDays()+"");

					dataRow.add(i.getCreatedAt()+"");
					if(i.getCreatedBy()!=null){
						dataRow.add(i.getCreatedBy().getDisplayName());
					}else{
						dataRow.add("");
					}
					
					dataRow.add(i.getUpdateAt()+"");
					if(i.getUpdatedBy()!=null){
						dataRow.add(i.getUpdatedBy().getDisplayName());
					}else{
						dataRow.add("");
					}

					table_Data.add(dataRow);
				}		
			}


			response=new ReponceForWebTableData(true,productId==null ? "All Questions" : "Questions Against Product",headers,table_Data);
			return response;

		} catch (Exception ex) {
			response=new ResponceWithMessage(ex);
			return response;

		}finally{
			if(LOG.isInfoEnabled()){
				LOG.info("Returned Response is:");
				if(response!=null)
					LOG.info("Response Requested_Action: {} ",new Object[]{response.getRequested_Action()});
				LOG.info("WebClientRestContoller.GetKYCQuestions--End");
			}
		}
	}
	
	
	
	
	@RequestMapping(method=RequestMethod.POST,value="/LMSServer/GetKYCQuestionsWithDTOResponse")
	@PreAuthorize("hasAuthority('KYC_MANAGEMENT_R') OR hasAuthority('KYC_MANAGEMENT_RW')")
	public BasicResponce GetKYCQuestionsWithDTOResponse(@RequestParam(value = "productId", required = false) Integer productId, @RequestHeader("authString") String authString) {		

		if(LOG.isInfoEnabled()){
			LOG.info("WebClientRestContoller.GetKYCQuestionsWithDTOResponse--Start");
			LOG.info("Received Parameters are:");
			LOG.info("Product Id (Optional): {} ",new Object[]{productId});
		}

		BasicResponce authResp = null;

		try {

			ArrayList<KYCQuestion> fromDB=new ArrayList<KYCQuestion>();

			if(productId==null){
				fromDB=objLMSDAO.getAllKYCQuestions();
			} else {
				fromDB=objLMSDAO.getKYCQuestionsAgainstProduct(productId);
			}

			List<KYCQuestionResponseDTO> toSend= new ArrayList<KYCQuestionResponseDTO>();
			
			if(fromDB!=null && fromDB.size()>0){
				
				for(KYCQuestion i:fromDB) {

					KYCQuestionResponseDTO toAdd=new KYCQuestionResponseDTO();
					toAdd.setQuestionId(i.getId());
					toAdd.setQuestion(i.getQuestionToAsk());
					toAdd.setQuestionCategory(i.getCatagory());
					toAdd.setAnswerType(i.getAnswerType());
					toAdd.setExpiryInDays(i.getExpiryInDays());
					toAdd.setMandatoryStatus(i.isMandatoryStatus());
					toAdd.setListOfPossibleAnswers(i.getListOfPossibleAnswers());
					
					if(i.getCreatedAt()!=null)
						toAdd.setCreatedAt(utility.getDateFromTimeStamp(i.getCreatedAt().toString()));
					if(i.getCreatedBy()!=null)
						toAdd.setCreatedBy(new ApplicationUserResponseDTO(i.getCreatedBy().getUserId(), i.getCreatedBy().getDisplayName()));
					
					if(i.getUpdateAt()!=null)
						toAdd.setUpdatedAt(utility.getDateFromTimeStamp(i.getUpdateAt().toString()));
					if(i.getUpdatedBy()!=null)
						toAdd.setUpdatedBy(new ApplicationUserResponseDTO(i.getUpdatedBy().getUserId(), i.getUpdatedBy().getDisplayName()));

					toSend.add(toAdd);
				}
				
				authResp=new ListOfKYCQuestionResponseDTO(toSend);
				authResp.setRequested_Action(true);
				return authResp;
			}else{
				ResponceWithMessage toReturn = new ResponceWithMessage(false, "No Record Found");
				return toReturn;
			}
		
		} catch (Exception ex) {
			authResp=new ResponceWithMessage(ex);
			return authResp;

		}finally{
			if(LOG.isInfoEnabled()){
				LOG.info("Returned Response is:");
				if(authResp!=null)
					LOG.info("Response Requested_Action: {} ",new Object[]{authResp.getRequested_Action()});
				LOG.info("WebClientRestContoller.GetKYCQuestionsWithDTOResponse--End");
			}
		}
	}

	@RequestMapping(method=RequestMethod.POST, value="/LMSServer/UpdateKycQuestion")
	@PreAuthorize("hasAuthority('KYC_MANAGEMENT_RW')")
	public BasicResponce UpdateKycQuestion(@RequestParam(value = "kycQuestionId") String kycQuestionId, 
			@RequestParam(value = "questionToAsk") String questionToAsk, 
			@RequestParam(value = "answerType") String answerType, 
			@RequestParam(value = "listOfPossibleAnswers") String listOfPossibleAnswers, 
			@RequestParam(value = "catagory") String catagory, 
			@RequestParam(value = "mandatoryStatus") boolean mandatoryStatus, 
			@RequestParam(value = "expiryInDays") int expiryInDays,
			@RequestHeader(value = "authString") String authString) {

		if(LOG.isInfoEnabled()){
			LOG.info("WebClientRestContoller.UpdateKycQuestion--Start");
			LOG.info("Received Parameters are:");
			LOG.info("Kyc Question Id: {} ",new Object[]{kycQuestionId});
			LOG.info("Question to Ask: {} ",new Object[]{questionToAsk});
			LOG.info("Answer Type: {} ",new Object[]{answerType});
			LOG.info("List of Possible Answers: {} ",new Object[]{listOfPossibleAnswers});
			LOG.info("Category: {} ",new Object[]{catagory});
			LOG.info("Mandatory Status: {} ",new Object[]{mandatoryStatus});
			LOG.info("Expiry In Days: {} ",new Object[]{expiryInDays});
			LOG.info("Auth String: {} ",new Object[]{authString});
		}

		ResponceWithMessage response = null;


		try {

			int result=objLMSDAO.updateKYCQuestion(kycQuestionId, questionToAsk, answerType, listOfPossibleAnswers, catagory, mandatoryStatus, expiryInDays, objLMSDAO.getApplicationUserByToken(authString));

			if(result>0){
				response=new ResponceWithMessage(true,"Successfully Updated");
				return response;
			} else if (result==-5){
				response=new ResponceWithMessage(false,"Unable to find KYC Question");
				return response;
			} else {
				response=new ResponceWithMessage(false,"Unkown Error");
				return response;
			}

		} catch (Exception ex) {
			response=new ResponceWithMessage(ex);
			return response;
		}finally{
			if(LOG.isInfoEnabled()){
				LOG.info("Returned Response is:");
				if(response!=null){
					LOG.info("Response Requested_Action: {} ",new Object[]{response.getMessage()});	
					LOG.info("Response Requested_Action: {} ",new Object[]{response.getRequested_Action()});
				}
				LOG.info("WebClientRestContoller.UpdateKycQuestion--End");
			}
		}
	}

	@RequestMapping(method=RequestMethod.POST,value="/LMSServer/GetProductAssumptions")
	@PreAuthorize("hasAuthority('PRODUCT_MANAGEMENT_R') OR hasAuthority('PRODUCT_MANAGEMENT_RW')")
	public BasicResponce GetProductAssumptions(@RequestParam(value = "productId", required = false) Integer productId,
			@RequestHeader("authString") String authString) {		

		if(LOG.isInfoEnabled()){
			LOG.info("WebClientRestContoller.GetProductAssumptions--Start");
			LOG.info("Received Parameters are:");
			LOG.info("Product Id: {} ",new Object[]{productId});
		}

		BasicResponce response = null;

		try {

			ArrayList<ProductAssumption> toParse=new ArrayList<ProductAssumption>();

			if(productId==null){
				toParse=objLMSDAO.getAllProductAssumption();
			} else {
				toParse=objLMSDAO.getProductAssumptionsAgainstProduct(productId);
			}

			ArrayList <String> headers=new ArrayList<String>();
			headers.add("AssumptionId");
			headers.add("AssumptionName");
			headers.add("AssumptionDataType");

			headers.add("CreatedAt");
			headers.add("CreatedBy");
			headers.add("UpdatedAt");
			headers.add("UpdatedBy");

			ArrayList<List<String>> table_Data = new ArrayList<List<String>>();	
			if(toParse!=null){
				for(ProductAssumption i:toParse) {
					ArrayList <String> dataRow=new ArrayList<String>();

					dataRow.add(i.getId()+"");
					dataRow.add(i.getName()+"");
					dataRow.add(i.getDataType()+"");

					dataRow.add(i.getCreatedAt()+"");
					if(i.getCreatedBy()!=null){
						dataRow.add(i.getCreatedBy().getDisplayName());
					}else{
						dataRow.add("");
					}
					
					dataRow.add(i.getUpdatedAt()+"");
					if(i.getUpdatedBy()!=null){
						dataRow.add(i.getUpdatedBy().getDisplayName());
					}else{
						dataRow.add("");
					}
					
					table_Data.add(dataRow);
				}		
			}


			response=new ReponceForWebTableData(true,productId==null ? "All Product Assumptions" : "Product Assumptions Against Product",headers,table_Data);
			return response;

		} catch (Exception ex) {
			response=new ResponceWithMessage(ex);
			return response;
		}finally{
			if(LOG.isInfoEnabled()){
				LOG.info("Returned Response is:");
				if(response!=null){
					LOG.info("Response Requested_Action: {} ",new Object[]{response.getRequested_Action()});
				}
				LOG.info("WebClientRestContoller.GetProductAssumptions--End");
			}
		}
	}
	
	
	@RequestMapping(method=RequestMethod.POST,value="/LMSServer/GetProductAssumptionsWithDTOResponse")
	@PreAuthorize("hasAuthority('PRODUCT_MANAGEMENT_R') OR hasAuthority('PRODUCT_MANAGEMENT_RW')")
	public BasicResponce GetProductAssumptionsWithDTOResponse(@RequestParam(value = "productId", required = false) Integer productId, @RequestHeader("authString") String authString) {		

		if(LOG.isInfoEnabled()){
			LOG.info("WebClientRestContoller.GetProductAssumptionsWithDTOResponse--Start");
			LOG.info("Received Parameters are:");
			LOG.info("Product Id: {} ",new Object[]{productId});
		}

		BasicResponce authResp = null;

		try {

			ArrayList<ProductAssumption> fromDB=new ArrayList<ProductAssumption>();

			if(productId==null){
				fromDB=objLMSDAO.getAllProductAssumption();
			} else {
				fromDB=objLMSDAO.getProductAssumptionsAgainstProduct(productId);
			}
			
			List<ProductAssumptionResponseDTO> toSend= new ArrayList<ProductAssumptionResponseDTO>();
			
			if(fromDB!=null && fromDB.size()>0){
				
				for(ProductAssumption i:fromDB) {

					ProductAssumptionResponseDTO toAdd=new ProductAssumptionResponseDTO();
					toAdd.setId(i.getId());
					toAdd.setName(i.getName());
					toAdd.setDataType(i.getDataType());
					
					if(i.getCreatedAt()!=null)
						toAdd.setCreatedAt(utility.getDateFromTimeStamp(i.getCreatedAt().toString()));
					if(i.getCreatedBy()!=null)
						toAdd.setCreatedBy(new ApplicationUserResponseDTO(i.getCreatedBy().getUserId(), i.getCreatedBy().getDisplayName()));
					if(i.getUpdatedAt()!=null)
						toAdd.setUpdatedAt(utility.getDateFromTimeStamp(i.getUpdatedAt().toString()));
					if(i.getUpdatedBy()!=null)
						toAdd.setUpdatedBy(new ApplicationUserResponseDTO(i.getUpdatedBy().getUserId(), i.getUpdatedBy().getDisplayName()));

					toSend.add(toAdd);
				}
				
				authResp=new ListOfProductAssumptionResponseDTO(toSend);
				authResp.setRequested_Action(true);
				return authResp;
			}else{
				ResponceWithMessage toReturn = new ResponceWithMessage(false, "No Record Found");
				return toReturn;
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
				LOG.info("WebClientRestContoller.GetProductAssumptionsWithDTOResponse--End");
			}
		}
	}

	@RequestMapping(method=RequestMethod.POST,value="/LMSServer/GetProductAssumptionsAndValues")
	@PreAuthorize("hasAuthority('PRODUCT_MANAGEMENT_R') OR hasAuthority('PRODUCT_MANAGEMENT_RW')")
	public BasicResponce GetProductAssumptionsAndValues(@RequestParam(value = "productId", required = true) Integer productId,@RequestHeader("authString") String authString) {		

		if(LOG.isInfoEnabled()){
			LOG.info("WebClientRestContoller.GetProductAssumptionsAndValues()--Start");
			LOG.info("Received Parameters are:");
			LOG.info("Product Id: {} ",new Object[]{productId});
		}
		BasicResponce response = null;

		try {
			ArrayList<ProductSpecification> toParse=new ArrayList<ProductSpecification>();
			toParse=objLMSDAO.getProductSpecificationAgainstProduct(productId);

			ArrayList <String> headers=new ArrayList<String>();
			headers.add("Assumption");
			headers.add("Value");

			ArrayList<List<String>> table_Data = new ArrayList<List<String>>();	
			if(toParse!=null){
				for(ProductSpecification i:toParse) {
					ArrayList <String> dataRow=new ArrayList<String>();
					dataRow.add(i.getProductSpecificationAssumption().getName());
					dataRow.add(i.getAssumptionValue());
					table_Data.add(dataRow);
				}		
			}

			response=new ReponceForWebTableData(true, "Product Assumption And Values Against Product",headers,table_Data);
			return response;

		} catch (Exception ex) {
			response= new ResponceWithMessage(ex);
			return response;
		}finally{
			if(LOG.isInfoEnabled()){
				LOG.info("Returned Response is:");
				if(response!=null){
					LOG.info("Response Requested_Action: {} ",new Object[]{response.getRequested_Action()});
				}
				LOG.info("WebClientRestContoller.GetProductAssumptionsAndValues()--End");
			}
		}
	}
	
	
	@RequestMapping(method=RequestMethod.POST,value="/LMSServer/GetProductAssumptionsAndValuesWithDTOResponse")
	@PreAuthorize("hasAuthority('PRODUCT_MANAGEMENT_R') OR hasAuthority('PRODUCT_MANAGEMENT_RW')")
	public BasicResponce GetProductAssumptionsAndValuesWithDTOResponse(@RequestParam(value = "productId", required = true) Integer productId,@RequestHeader("authString") String authString) {		

		if(LOG.isInfoEnabled()){
			LOG.info("WebClientRestContoller.GetProductAssumptionsAndValuesWithDTOResponse()--Start");
			LOG.info("Received Parameters are:");
			LOG.info("Product Id: {} ",new Object[]{productId});
		}
		BasicResponce authResp = null;

		try {
			ArrayList<ProductSpecification> fromDB=new ArrayList<ProductSpecification>();
			fromDB=objLMSDAO.getProductSpecificationAgainstProduct(productId);
			
			List<ProductSpecificationResponseDTO> toSend= new ArrayList<ProductSpecificationResponseDTO>();
			if(fromDB!=null && fromDB.size()>0){
				Collections.sort(fromDB);
				
				for(ProductSpecification i:fromDB) {
					ProductSpecificationResponseDTO toAdd=new ProductSpecificationResponseDTO();
					toAdd.setId(i.getProductSpecificationAssumption().getId());
					toAdd.setName(i.getProductSpecificationAssumption().getName());
					toAdd.setValue(i.getAssumptionValue());
					if(i.getCreatedAt()!=null)
						toAdd.setCreatedAt(utility.getDateFromTimeStamp(i.getCreatedAt().toString()));
					if(i.getCreatedBy()!=null)
						toAdd.setCreatedBy(new ApplicationUserResponseDTO(i.getCreatedBy().getUserId(), i.getCreatedBy().getDisplayName()));
					toSend.add(toAdd);
				}
				
				authResp=new ListOfProductSpecificationResponseDTO(toSend);
				authResp.setRequested_Action(true);
				return authResp;
			}else{
				ResponceWithMessage toReturn = new ResponceWithMessage(false, "No Record Found");
				return toReturn;
			}	
			
		} catch (Exception ex) {
			authResp= new ResponceWithMessage(ex);
			return authResp;
		}finally{
			if(LOG.isInfoEnabled()){
				LOG.info("Returned Response is:");
				if(authResp!=null){
					LOG.info("Response Requested_Action: {} ",new Object[]{authResp.getRequested_Action()});
				}
				LOG.info("WebClientRestContoller.GetProductAssumptionsAndValuesWithDTOResponse()--End");
			}
		}
	}

	@RequestMapping(method=RequestMethod.POST, value="/LMSServer/UpdateAssumption")
	@PreAuthorize("hasAuthority('ASSUMPTION_MANAGEMENT_RW')")
	public BasicResponce UpdateAssumption(@RequestParam(value = "assumptionId") String assumptionId, 
			@RequestParam(value = "assumption") String assumption, 
			@RequestParam(value = "assumptionType") String assumptionType, 
			@RequestHeader(value = "authString") String authString) {

		if(LOG.isInfoEnabled()){
			LOG.info("WebClientRestContoller.UpdateAssumption()--Start");
			LOG.info("Received Parameters are:");
			LOG.info("Assumption Id: {} ",new Object[]{assumptionId});
			LOG.info("Assumption: {} ",new Object[]{assumption});
			LOG.info("Assumption Type: {} ",new Object[]{assumptionType});
			LOG.info("Auth String: {} ",new Object[]{authString});
		}
		ResponceWithMessage response = null;

		try {
			int result=objLMSDAO.updateAssumption(assumptionId, assumption, assumptionType,objLMSDAO.getApplicationUserByToken(authString));

			if(result>0){
				response=new ResponceWithMessage(true,"Successfully Updated");
				return response;
			} else if (result==-5){
				response=new ResponceWithMessage(false,"Unable to find Assumption");
				return response;
			} else {
				response=new ResponceWithMessage(false,"Unkown Error");
				return response;
			}

		} catch (Exception ex) {
			response=new ResponceWithMessage(ex);
			return response;
		}finally{
			if(LOG.isInfoEnabled()){
				LOG.info("Returned Response is:");
				if(response!=null){
					LOG.info("Response Message: {} ",new Object[]{response.getMessage()});
					LOG.info("Response Requested_Action: {} ",new Object[]{response.getRequested_Action()});
				}
				LOG.info("WebClientRestContoller.UpdateAssumption()--End");
			}
		}

	}
	 

	/*@RequestMapping(method=RequestMethod.POST,value="/LMSServer/GetKYCQuestionCatagories")
	@PreAuthorize("hasAuthority('KYC_MANAGEMENT_R') OR hasAuthority('KYC_MANAGEMENT_RW')")
	public BasicResponce GetKYCQuestionCatagories() {		

		if(LOG.isInfoEnabled()){
			LOG.info("WebClientRestContoller.GetKYCQuestionCatagories()--Start");
		}
		BasicResponce response = null;

		try {

			String hardCodedCatagories[]={"LOW RISK","MEDIUM RISK","HIGH RISK","LEVEL 1","LEVEL 2","LEVEL 3"};

			ArrayList <String> headers=new ArrayList<String>();
			headers.add("Catagories");
			ArrayList<List<String>> table_Data = new ArrayList<List<String>>();	

			for(String i:hardCodedCatagories) {
				ArrayList <String> dataRow=new ArrayList<String>();
				dataRow.add(i);


				table_Data.add(dataRow);
			}		

			response=new ReponceForWebTableData(true,"All Products",headers,table_Data);
			return response;
		} catch (Exception ex) {
			response=new ResponceWithMessage(ex);
			return response;
		}finally{
			if(LOG.isInfoEnabled()){
				LOG.info("Returned Response is:");
				if(response!=null){
					LOG.info("Response Requested_Action: {} ",new Object[]{response.getRequested_Action()});
				}
				LOG.info("WebClientRestContoller.GetKYCQuestionCatagories()--End");
			}
		}
	}*/



	@RequestMapping(method=RequestMethod.POST,value="/LMSServer/AddProductSpecificationAssumption")
	@PreAuthorize("hasAuthority('ASSUMPTION_MANAGEMENT_RW')")
	public BasicResponce AddProductSpecificationAssumption(@RequestParam(value = "assumptionName") String assumptionName,
			@RequestParam(value = "assumptionDataType") String assumptionDataType,
			@RequestHeader(value = "authString") String authString) {		

		if(LOG.isInfoEnabled()){
			LOG.info("WebClientRestContoller.AddProductSpecificationAssumption()--Start");
			LOG.info("Received Parameters are:");
			LOG.info("Assumption Name: {} ",new Object[]{assumptionName});
			LOG.info("Assumption Data Type: {} ",new Object[]{assumptionDataType});
			LOG.info("Auth String: {} ",new Object[]{authString});
		}
		BasicResponce response = null;

		try {
			objLMSDAO.addProductSpecificationAssumption(assumptionName,assumptionDataType,objLMSDAO.getApplicationUserByToken(authString));
			response=new BasicResponce(true);
			return response;
		} catch (Exception ex) {
			response=new ResponceWithMessage(ex);
			return response;
		}finally{
			if(LOG.isInfoEnabled()){
				LOG.info("Returned Response is:");
				if(response!=null){
					LOG.info("Response Requested_Action: {} ",new Object[]{response.getRequested_Action()});
				}
				LOG.info("WebClientRestContoller.AddProductSpecificationAssumption()--End");
			}
		}
	}

	@RequestMapping(method=RequestMethod.POST,value={"/LMSServer/getPendingOrCompletedLoanApplicationsByStatus"} )
	@PreAuthorize("hasAuthority('LOAN_MANAGEMENT_R') OR hasAuthority('LOAN_MANAGEMENT_RW')")
	public BasicResponce getPendingOrCompletedLoanApplicationsByStatus(@RequestParam(value = "requiredPendingRequest") boolean requiredPendingRequest) {

		if(LOG.isInfoEnabled()){
			LOG.info("WebClientRestContoller.getPendingOrCompletedLoanApplicationsByStatus()--Start");
			LOG.info("Received Parameters are:");
			LOG.info("Required Pending Request Status: {} ",new Object[]{requiredPendingRequest});
		}
		BasicResponce authResp = null;

		try {	

			List<LoanApplication> fromDB=null;		

			if(requiredPendingRequest){
				fromDB=objLMSDAO.getAllPendingLoanApplications();
			}else{
				fromDB=objLMSDAO.getAllCompletedLoanApplications();
			}
			List<LoanApplicationsResponseDTO> toSend = new ArrayList<LoanApplicationsResponseDTO>();
			
			
			if(fromDB!=null && fromDB.size()>0){
				
				for(LoanApplication i:fromDB) {

					LoanApplicationsResponseDTO toAdd=new LoanApplicationsResponseDTO();

					toAdd.setId(i.getId());

					toAdd.setCustomer(new CustomerResponseDTO(i.getCustomer().getId(),i.getCustomer().getCellNoInString(),i.getCustomer().getFullName(),i.getCustomer().getPakistaniCNIC()));
					toAdd.setRequestedamount(i.getRequestedamount());
					toAdd.setStatus(i.getStatus().name());

					Product temp=i.getProduct();
					ProductResponseDTO toPut=new ProductResponseDTO(temp.getId(),temp.getProductName(),temp.getProductCatagory(),temp.getTermsAndConditionFilePath());
					List<KYCAnswerSnapShotAtLoanApplication> objKYCAnswerSnapShotAtLoanApplication=objLMSDAO.getListKYCAnswerSnapShotForApplication(i.getId());
					if(objKYCAnswerSnapShotAtLoanApplication!=null && objKYCAnswerSnapShotAtLoanApplication.size()>0) {
						for(KYCAnswerSnapShotAtLoanApplication i2:objKYCAnswerSnapShotAtLoanApplication) {						
							if(i2!=null && i2.getQuestion()!=null)
							toPut.addKYCQuestionWithAnswerDTO(i2.getQuestion().getId(), i2.getQuestion().getQuestionToAsk(),i2.getQuestion().getCatagory(), i2.getQuestion().getAnswerType(),i2.getQuestionanswer());
						}
					}

					List<ProductSpecificationSnapShotAtLoanApplication> objListOfProductSpecificationSnapShotAtLoanApplication=objLMSDAO.getListOfProductSpecificationSnapShotAtLoanApplication(i.getId());
					if(objListOfProductSpecificationSnapShotAtLoanApplication!=null && objListOfProductSpecificationSnapShotAtLoanApplication.size()>0) {
						for(ProductSpecificationSnapShotAtLoanApplication i2:objListOfProductSpecificationSnapShotAtLoanApplication) {		
							toPut.addProductSpecificationDTO(i2.getProductspecificationassumption().getId(), i2.getProductspecificationassumption().getName(), i2.getAssumptionvalue());
						}
					}
					
					toAdd.setProduct(toPut);
					if(i.getCreatedAt()!=null)
						toAdd.setCreatedAt(utility.getDateFromTimeStamp(i.getCreatedAt().toString()));
					if(i.getCreatedBy()!=null)
						toAdd.setCreatedBy(new ApplicationUserResponseDTO(i.getCreatedBy().getUserId(), i.getCreatedBy().getDisplayName()));
					if(i.getUpdatedAt()!=null)
						toAdd.setUpdatedAt(utility.getDateFromTimeStamp(i.getUpdatedAt().toString()));
					if(i.getUpdatedBy()!=null)
						toAdd.setUpdatedBy(new ApplicationUserResponseDTO(i.getUpdatedBy().getUserId(), i.getUpdatedBy().getDisplayName()));

					toSend.add(toAdd);
				}
				
				authResp=new ListOfLoanApplicationsResponseDTO(toSend);
				return authResp;
			}else{
				authResp= new ResponceWithMessage(false,"No Data Found");
				return authResp;
			}
			
		} catch (Exception ex) {
			authResp= new ResponceWithMessage(ex);
			return authResp;
		}finally{
			if(LOG.isInfoEnabled()){
				LOG.info("Returned Response is:");
				if(authResp!=null){
					LOG.info("Response Requested_Action: {} ",new Object[]{authResp.getRequested_Action()});
				}
				LOG.info("WebClientRestContoller.getPendingOrCompletedLoanApplicationsByStatus()--End");
			}
		}
	}
	
	@RequestMapping(method=RequestMethod.POST,value={"/LMSServer/getPendingForApprovalLoanApplications"} )
	@PreAuthorize("hasAuthority('LOAN_MANAGEMENT_R') OR hasAuthority('LOAN_MANAGEMENT_RW')")
	public BasicResponce getPendingForApprovalLoanApplications() {

		if(LOG.isInfoEnabled()){
			LOG.info("WebClientRestContoller.getPendingForApprovalLoanApplications()--Start");
		}
		BasicResponce authResp = null;

		try {	

			List<LoanApplication> fromDB= objLMSDAO.getLoanApplicationsByStatus(LoanApplicationStatus.PENDINGFORAPPROVAL);
			
			List<LoanApplicationsResponseDTO> toSend = new ArrayList<LoanApplicationsResponseDTO>();
			
			if(fromDB!=null && fromDB.size()>0){
				
				for(LoanApplication i:fromDB) {

					LoanApplicationsResponseDTO toAdd=new LoanApplicationsResponseDTO();

					toAdd.setId(i.getId());
					toAdd.setCustomer(new CustomerResponseDTO(i.getCustomer().getId(),i.getCustomer().getCellNoInString(),i.getCustomer().getFullName(),i.getCustomer().getPakistaniCNIC()));
					toAdd.setRequestedamount(i.getRequestedamount());
					toAdd.setStatus(i.getStatus().name());

					Product temp=i.getProduct();
					ProductResponseDTO toPut=new ProductResponseDTO(temp.getId(),temp.getProductName(),temp.getProductCatagory(),temp.getTermsAndConditionFilePath());
					List<KYCAnswerSnapShotAtLoanApplication> objKYCAnswerSnapShotAtLoanApplication=objLMSDAO.getListKYCAnswerSnapShotForApplication(i.getId());
					if(objKYCAnswerSnapShotAtLoanApplication!=null && objKYCAnswerSnapShotAtLoanApplication.size()>0) {
						for(KYCAnswerSnapShotAtLoanApplication i2:objKYCAnswerSnapShotAtLoanApplication) {						
							if(i2!=null && i2.getQuestion()!=null)
							toPut.addKYCQuestionWithAnswerDTO(i2.getQuestion().getId(), i2.getQuestion().getQuestionToAsk(),i2.getQuestion().getCatagory(), i2.getQuestion().getAnswerType(),i2.getQuestionanswer());
						}
					}

					List<ProductSpecificationSnapShotAtLoanApplication> objListOfProductSpecificationSnapShotAtLoanApplication=objLMSDAO.getListOfProductSpecificationSnapShotAtLoanApplication(i.getId());
					if(objListOfProductSpecificationSnapShotAtLoanApplication!=null && objListOfProductSpecificationSnapShotAtLoanApplication.size()>0) {
						for(ProductSpecificationSnapShotAtLoanApplication i2:objListOfProductSpecificationSnapShotAtLoanApplication) {		
							toPut.addProductSpecificationDTO(i2.getProductspecificationassumption().getId(), i2.getProductspecificationassumption().getName(), i2.getAssumptionvalue());
						}
					}

					toAdd.setProduct(toPut);
					
					if(i.getCreatedAt()!=null)
						toAdd.setCreatedAt(utility.getDateFromTimeStamp(i.getCreatedAt().toString()));
					if(i.getCreatedBy()!=null)
						toAdd.setCreatedBy(new ApplicationUserResponseDTO(i.getCreatedBy().getUserId(), i.getCreatedBy().getDisplayName()));
					if(i.getUpdatedAt()!=null)
						toAdd.setUpdatedAt(utility.getDateFromTimeStamp(i.getUpdatedAt().toString()));
					if(i.getUpdatedBy()!=null)
						toAdd.setUpdatedBy(new ApplicationUserResponseDTO(i.getUpdatedBy().getUserId(), i.getUpdatedBy().getDisplayName()));

					toSend.add(toAdd);
				}
				
				authResp=new ListOfLoanApplicationsResponseDTO(toSend);
				return authResp;
			}else{
				authResp= new ResponceWithMessage(false,"No Data Found");
				return authResp;
			}
			
		} catch (Exception ex) {
			authResp= new ResponceWithMessage(ex);
			return authResp;
		}finally{
			if(LOG.isInfoEnabled()){
				LOG.info("Returned Response is:");
				if(authResp!=null){
					LOG.info("Response Requested_Action: {} ",new Object[]{authResp.getRequested_Action()});
				}
				LOG.info("WebClientRestContoller.getPendingForApprovalLoanApplications()--End");
			}
		}
	}
	
	@RequestMapping(method=RequestMethod.POST,value={"/LMSServer/getApprovedLoanApplications"} )
	@PreAuthorize("hasAuthority('LOAN_MANAGEMENT_R') OR hasAuthority('LOAN_MANAGEMENT_RW')")
	public BasicResponce getApprovedLoanApplications() {

		if(LOG.isInfoEnabled()){
			LOG.info("WebClientRestContoller.getApprovedLoanApplications()--Start");
		}
		BasicResponce authResp = null;

		try {	

			List<LoanApplication> fromDB= objLMSDAO.getLoanApplicationsByStatus(LoanApplicationStatus.APPROVED);
			
			List<LoanApplicationsResponseDTO> toSend = new ArrayList<LoanApplicationsResponseDTO>();
			
			if(fromDB!=null && fromDB.size()>0){
				
				for(LoanApplication i:fromDB) {

					LoanApplicationsResponseDTO toAdd=new LoanApplicationsResponseDTO();

					toAdd.setId(i.getId());
					toAdd.setCustomer(new CustomerResponseDTO(i.getCustomer().getId(),i.getCustomer().getCellNoInString(),i.getCustomer().getFullName(),i.getCustomer().getPakistaniCNIC()));
					toAdd.setRequestedamount(i.getRequestedamount());
					toAdd.setStatus(i.getStatus().name());

					Product temp=i.getProduct();
					ProductResponseDTO toPut=new ProductResponseDTO(temp.getId(),temp.getProductName(),temp.getProductCatagory(),temp.getTermsAndConditionFilePath());
					List<KYCAnswerSnapShotAtLoanApplication> objKYCAnswerSnapShotAtLoanApplication=objLMSDAO.getListKYCAnswerSnapShotForApplication(i.getId());
					if(objKYCAnswerSnapShotAtLoanApplication!=null && objKYCAnswerSnapShotAtLoanApplication.size()>0) {
						for(KYCAnswerSnapShotAtLoanApplication i2:objKYCAnswerSnapShotAtLoanApplication) {						
							if(i2!=null && i2.getQuestion()!=null)
							toPut.addKYCQuestionWithAnswerDTO(i2.getQuestion().getId(), i2.getQuestion().getQuestionToAsk(),i2.getQuestion().getCatagory(), i2.getQuestion().getAnswerType(),i2.getQuestionanswer());
						}
					}

					List<ProductSpecificationSnapShotAtLoanApplication> objListOfProductSpecificationSnapShotAtLoanApplication=objLMSDAO.getListOfProductSpecificationSnapShotAtLoanApplication(i.getId());
					if(objListOfProductSpecificationSnapShotAtLoanApplication!=null && objListOfProductSpecificationSnapShotAtLoanApplication.size()>0) {
						for(ProductSpecificationSnapShotAtLoanApplication i2:objListOfProductSpecificationSnapShotAtLoanApplication) {		
							toPut.addProductSpecificationDTO(i2.getProductspecificationassumption().getId(), i2.getProductspecificationassumption().getName(), i2.getAssumptionvalue());
						}
					}

					toAdd.setProduct(toPut);
					
					if(i.getCreatedAt()!=null)
						toAdd.setCreatedAt(utility.getDateFromTimeStamp(i.getCreatedAt().toString()));
					if(i.getCreatedBy()!=null)
						toAdd.setCreatedBy(new ApplicationUserResponseDTO(i.getCreatedBy().getUserId(), i.getCreatedBy().getDisplayName()));
					if(i.getUpdatedAt()!=null)
						toAdd.setUpdatedAt(utility.getDateFromTimeStamp(i.getUpdatedAt().toString()));
					if(i.getUpdatedBy()!=null)
						toAdd.setUpdatedBy(new ApplicationUserResponseDTO(i.getUpdatedBy().getUserId(), i.getUpdatedBy().getDisplayName()));

					toSend.add(toAdd);
				}
				
				authResp=new ListOfLoanApplicationsResponseDTO(toSend);
				return authResp;
			}else{
				authResp= new ResponceWithMessage(false,"No Data Found");
				return authResp;
			}
			
		} catch (Exception ex) {
			authResp= new ResponceWithMessage(ex);
			return authResp;
		}finally{
			if(LOG.isInfoEnabled()){
				LOG.info("Returned Response is:");
				if(authResp!=null){
					LOG.info("Response Requested_Action: {} ",new Object[]{authResp.getRequested_Action()});
				}
				LOG.info("WebClientRestContoller.getPendingOrCompletedLoanApplicationsByStatus()--End");
			}
		}
	}
	
	@RequestMapping(method=RequestMethod.POST,value={"/LMSServer/getRejectedLoanApplications"} )
	@PreAuthorize("hasAuthority('LOAN_MANAGEMENT_R') OR hasAuthority('LOAN_MANAGEMENT_RW')")
	public BasicResponce getRejectedLoanApplications() {

		if(LOG.isInfoEnabled()){
			LOG.info("WebClientRestContoller.getRejectedLoanApplications()--Start");
		}
		BasicResponce authResp = null;

		try {	

			List<LoanApplication> fromDB= objLMSDAO.getLoanApplicationsByStatus(LoanApplicationStatus.REJECTED);
			List<LoanApplicationsResponseDTO> toSend = new ArrayList<LoanApplicationsResponseDTO>();
			
			if(fromDB!=null && fromDB.size()>0){
				
				for(LoanApplication i:fromDB) {
					LoanApplicationsResponseDTO toAdd=new LoanApplicationsResponseDTO();
					toAdd.setId(i.getId());
					toAdd.setCustomer(new CustomerResponseDTO(i.getCustomer().getId(),i.getCustomer().getCellNoInString(),i.getCustomer().getFullName(),i.getCustomer().getPakistaniCNIC()));
					toAdd.setRequestedamount(i.getRequestedamount());
					toAdd.setStatus(i.getStatus().name());

					Product temp=i.getProduct();
					ProductResponseDTO toPut=new ProductResponseDTO(temp.getId(),temp.getProductName(),temp.getProductCatagory(),temp.getTermsAndConditionFilePath());
					List<KYCAnswerSnapShotAtLoanApplication> objKYCAnswerSnapShotAtLoanApplication=objLMSDAO.getListKYCAnswerSnapShotForApplication(i.getId());
					if(objKYCAnswerSnapShotAtLoanApplication!=null && objKYCAnswerSnapShotAtLoanApplication.size()>0) {
						for(KYCAnswerSnapShotAtLoanApplication i2:objKYCAnswerSnapShotAtLoanApplication) {						
							if(i2!=null && i2.getQuestion()!=null)
							toPut.addKYCQuestionWithAnswerDTO(i2.getQuestion().getId(), i2.getQuestion().getQuestionToAsk(),i2.getQuestion().getCatagory(), i2.getQuestion().getAnswerType(),i2.getQuestionanswer());
						}
					}

					List<ProductSpecificationSnapShotAtLoanApplication> objListOfProductSpecificationSnapShotAtLoanApplication=objLMSDAO.getListOfProductSpecificationSnapShotAtLoanApplication(i.getId());
					if(objListOfProductSpecificationSnapShotAtLoanApplication!=null && objListOfProductSpecificationSnapShotAtLoanApplication.size()>0) {
						for(ProductSpecificationSnapShotAtLoanApplication i2:objListOfProductSpecificationSnapShotAtLoanApplication) {		
							toPut.addProductSpecificationDTO(i2.getProductspecificationassumption().getId(), i2.getProductspecificationassumption().getName(), i2.getAssumptionvalue());
						}
					}

					toAdd.setProduct(toPut);
					
					if(i.getCreatedAt()!=null)
						toAdd.setCreatedAt(utility.getDateFromTimeStamp(i.getCreatedAt().toString()));
					if(i.getCreatedBy()!=null)
						toAdd.setCreatedBy(new ApplicationUserResponseDTO(i.getCreatedBy().getUserId(), i.getCreatedBy().getDisplayName()));
					if(i.getUpdatedAt()!=null)
						toAdd.setUpdatedAt(utility.getDateFromTimeStamp(i.getUpdatedAt().toString()));
					if(i.getUpdatedBy()!=null)
						toAdd.setUpdatedBy(new ApplicationUserResponseDTO(i.getUpdatedBy().getUserId(), i.getUpdatedBy().getDisplayName()));

					toSend.add(toAdd);
				}
				
				authResp=new ListOfLoanApplicationsResponseDTO(toSend);
				return authResp;
			}else{
				authResp= new ResponceWithMessage(false,"No Data Found");
				return authResp;
			}
			
		} catch (Exception ex) {
			authResp= new ResponceWithMessage(ex);
			return authResp;
		}finally{
			if(LOG.isInfoEnabled()){
				LOG.info("Returned Response is:");
				if(authResp!=null){
					LOG.info("Response Requested_Action: {} ",new Object[]{authResp.getRequested_Action()});
				}
				LOG.info("WebClientRestContoller.getRejectedLoanApplications()--End");
			}
		}
	}
	
	@RequestMapping(method=RequestMethod.POST,value={"/LMSServer/getAcceptedLoanApplications"} )
	@PreAuthorize("hasAuthority('LOAN_MANAGEMENT_R') OR hasAuthority('LOAN_MANAGEMENT_RW')")
	public BasicResponce getAcceptedLoanApplications() {

		if(LOG.isInfoEnabled()){
			LOG.info("WebClientRestContoller.getApprovedLoanApplications()--Start");
		}
		BasicResponce authResp = null;

		try {	

			List<LoanApplication> fromDB= objLMSDAO.getLoanApplicationsByStatus(LoanApplicationStatus.ACCEPTED);
			
			List<LoanApplicationsResponseDTO> toSend = new ArrayList<LoanApplicationsResponseDTO>();
			
			if(fromDB!=null && fromDB.size()>0){
				
				for(LoanApplication i:fromDB) {

					LoanApplicationsResponseDTO toAdd=new LoanApplicationsResponseDTO();

					toAdd.setId(i.getId());
					toAdd.setCustomer(new CustomerResponseDTO(i.getCustomer().getId(),i.getCustomer().getCellNoInString(),i.getCustomer().getFullName(),i.getCustomer().getPakistaniCNIC()));
					toAdd.setRequestedamount(i.getRequestedamount());
					toAdd.setStatus(i.getStatus().name());

					Product temp=i.getProduct();
					ProductResponseDTO toPut=new ProductResponseDTO(temp.getId(),temp.getProductName(),temp.getProductCatagory(),temp.getTermsAndConditionFilePath());
					List<KYCAnswerSnapShotAtLoanApplication> objKYCAnswerSnapShotAtLoanApplication=objLMSDAO.getListKYCAnswerSnapShotForApplication(i.getId());
					if(objKYCAnswerSnapShotAtLoanApplication!=null && objKYCAnswerSnapShotAtLoanApplication.size()>0) {
						for(KYCAnswerSnapShotAtLoanApplication i2:objKYCAnswerSnapShotAtLoanApplication) {						
							if(i2!=null && i2.getQuestion()!=null)
							toPut.addKYCQuestionWithAnswerDTO(i2.getQuestion().getId(), i2.getQuestion().getQuestionToAsk(),i2.getQuestion().getCatagory(), i2.getQuestion().getAnswerType(),i2.getQuestionanswer());
						}
					}

					List<ProductSpecificationSnapShotAtLoanApplication> objListOfProductSpecificationSnapShotAtLoanApplication=objLMSDAO.getListOfProductSpecificationSnapShotAtLoanApplication(i.getId());
					if(objListOfProductSpecificationSnapShotAtLoanApplication!=null && objListOfProductSpecificationSnapShotAtLoanApplication.size()>0) {
						for(ProductSpecificationSnapShotAtLoanApplication i2:objListOfProductSpecificationSnapShotAtLoanApplication) {		
							toPut.addProductSpecificationDTO(i2.getProductspecificationassumption().getId(), i2.getProductspecificationassumption().getName(), i2.getAssumptionvalue());
						}
					}

					toAdd.setProduct(toPut);
					
					if(i.getCreatedAt()!=null)
						toAdd.setCreatedAt(utility.getDateFromTimeStamp(i.getCreatedAt().toString()));
					if(i.getCreatedBy()!=null)
						toAdd.setCreatedBy(new ApplicationUserResponseDTO(i.getCreatedBy().getUserId(), i.getCreatedBy().getDisplayName()));
					if(i.getUpdatedAt()!=null)
						toAdd.setUpdatedAt(utility.getDateFromTimeStamp(i.getUpdatedAt().toString()));
					if(i.getUpdatedBy()!=null)
						toAdd.setUpdatedBy(new ApplicationUserResponseDTO(i.getUpdatedBy().getUserId(), i.getUpdatedBy().getDisplayName()));

					toSend.add(toAdd);
				}
				
				authResp=new ListOfLoanApplicationsResponseDTO(toSend);
				return authResp;
			}else{
				authResp= new ResponceWithMessage(false,"No Data Found");
				return authResp;
			}
			
		} catch (Exception ex) {
			authResp= new ResponceWithMessage(ex);
			return authResp;
		}finally{
			if(LOG.isInfoEnabled()){
				LOG.info("Returned Response is:");
				if(authResp!=null){
					LOG.info("Response Requested_Action: {} ",new Object[]{authResp.getRequested_Action()});
				}
				LOG.info("WebClientRestContoller.getAcceptedLoanApplications()--End");
			}
		}
	}
	
	@RequestMapping(method=RequestMethod.POST,value={"/LMSServer/getDeclinedLoanApplications"} )
	@PreAuthorize("hasAuthority('LOAN_MANAGEMENT_R') OR hasAuthority('LOAN_MANAGEMENT_RW')")
	public BasicResponce getDeclinedLoanApplications() {

		if(LOG.isInfoEnabled()){
			LOG.info("WebClientRestContoller.getDeclinedLoanApplications()--Start");
		}
		BasicResponce authResp = null;

		try {	

			List<LoanApplication> fromDB= objLMSDAO.getLoanApplicationsByStatus(LoanApplicationStatus.DECLINED);
			
			List<LoanApplicationsResponseDTO> toSend = new ArrayList<LoanApplicationsResponseDTO>();
			
			if(fromDB!=null && fromDB.size()>0){
				
				for(LoanApplication i:fromDB) {

					LoanApplicationsResponseDTO toAdd=new LoanApplicationsResponseDTO();

					toAdd.setId(i.getId());
					toAdd.setCustomer(new CustomerResponseDTO(i.getCustomer().getId(),i.getCustomer().getCellNoInString(),i.getCustomer().getFullName(),i.getCustomer().getPakistaniCNIC()));
					toAdd.setRequestedamount(i.getRequestedamount());
					toAdd.setStatus(i.getStatus().name());

					Product temp=i.getProduct();
					ProductResponseDTO toPut=new ProductResponseDTO(temp.getId(),temp.getProductName(),temp.getProductCatagory(),temp.getTermsAndConditionFilePath());
					List<KYCAnswerSnapShotAtLoanApplication> objKYCAnswerSnapShotAtLoanApplication=objLMSDAO.getListKYCAnswerSnapShotForApplication(i.getId());
					if(objKYCAnswerSnapShotAtLoanApplication!=null && objKYCAnswerSnapShotAtLoanApplication.size()>0) {
						for(KYCAnswerSnapShotAtLoanApplication i2:objKYCAnswerSnapShotAtLoanApplication) {						
							if(i2!=null && i2.getQuestion()!=null)
							toPut.addKYCQuestionWithAnswerDTO(i2.getQuestion().getId(), i2.getQuestion().getQuestionToAsk(),i2.getQuestion().getCatagory(), i2.getQuestion().getAnswerType(),i2.getQuestionanswer());
						}
					}

					List<ProductSpecificationSnapShotAtLoanApplication> objListOfProductSpecificationSnapShotAtLoanApplication=objLMSDAO.getListOfProductSpecificationSnapShotAtLoanApplication(i.getId());
					if(objListOfProductSpecificationSnapShotAtLoanApplication!=null && objListOfProductSpecificationSnapShotAtLoanApplication.size()>0) {
						for(ProductSpecificationSnapShotAtLoanApplication i2:objListOfProductSpecificationSnapShotAtLoanApplication) {		
							toPut.addProductSpecificationDTO(i2.getProductspecificationassumption().getId(), i2.getProductspecificationassumption().getName(), i2.getAssumptionvalue());
						}
					}

					toAdd.setProduct(toPut);
					
					if(i.getCreatedAt()!=null)
						toAdd.setCreatedAt(utility.getDateFromTimeStamp(i.getCreatedAt().toString()));
					if(i.getCreatedBy()!=null)
						toAdd.setCreatedBy(new ApplicationUserResponseDTO(i.getCreatedBy().getUserId(), i.getCreatedBy().getDisplayName()));
					if(i.getUpdatedAt()!=null)
						toAdd.setUpdatedAt(utility.getDateFromTimeStamp(i.getUpdatedAt().toString()));
					if(i.getUpdatedBy()!=null)
						toAdd.setUpdatedBy(new ApplicationUserResponseDTO(i.getUpdatedBy().getUserId(), i.getUpdatedBy().getDisplayName()));

					toSend.add(toAdd);
				}
				
				authResp=new ListOfLoanApplicationsResponseDTO(toSend);
				return authResp;
			}else{
				authResp= new ResponceWithMessage(false,"No Data Found");
				return authResp;
			}
			
		} catch (Exception ex) {
			authResp= new ResponceWithMessage(ex);
			return authResp;
		}finally{
			if(LOG.isInfoEnabled()){
				LOG.info("Returned Response is:");
				if(authResp!=null){
					LOG.info("Response Requested_Action: {} ",new Object[]{authResp.getRequested_Action()});
				}
				LOG.info("WebClientRestContoller.getDeclinedLoanApplications()--End");
			}
		}
	}
	
	@RequestMapping(method=RequestMethod.POST,value={"/LMSServer/getDisbursedLoanApplications"} )
	@PreAuthorize("hasAuthority('LOAN_MANAGEMENT_R') OR hasAuthority('LOAN_MANAGEMENT_RW')")
	public BasicResponce getDisbursedLoanApplications() {

		if(LOG.isInfoEnabled()){
			LOG.info("WebClientRestContoller.getDisbursedLoanApplications()--Start");
		}
		BasicResponce authResp = null;

		try {	

			List<LoanApplication> fromDB= objLMSDAO.getLoanApplicationsByStatus(LoanApplicationStatus.DISBURSED);
			
			List<LoanApplicationsResponseDTO> toSend = new ArrayList<LoanApplicationsResponseDTO>();
			
			if(fromDB!=null && fromDB.size()>0){
				
				for(LoanApplication i:fromDB) {

					LoanApplicationsResponseDTO toAdd=new LoanApplicationsResponseDTO();

					toAdd.setId(i.getId());
					toAdd.setCustomer(new CustomerResponseDTO(i.getCustomer().getId(),i.getCustomer().getCellNoInString(),i.getCustomer().getFullName(),i.getCustomer().getPakistaniCNIC()));
					toAdd.setRequestedamount(i.getRequestedamount());
					toAdd.setStatus(i.getStatus().name());

					Product temp=i.getProduct();
					ProductResponseDTO toPut=new ProductResponseDTO(temp.getId(),temp.getProductName(),temp.getProductCatagory(),temp.getTermsAndConditionFilePath());
					List<KYCAnswerSnapShotAtLoanApplication> objKYCAnswerSnapShotAtLoanApplication=objLMSDAO.getListKYCAnswerSnapShotForApplication(i.getId());
					if(objKYCAnswerSnapShotAtLoanApplication!=null && objKYCAnswerSnapShotAtLoanApplication.size()>0) {
						for(KYCAnswerSnapShotAtLoanApplication i2:objKYCAnswerSnapShotAtLoanApplication) {						
							if(i2!=null && i2.getQuestion()!=null)
							toPut.addKYCQuestionWithAnswerDTO(i2.getQuestion().getId(), i2.getQuestion().getQuestionToAsk(),i2.getQuestion().getCatagory(), i2.getQuestion().getAnswerType(),i2.getQuestionanswer());
						}
					}

					List<ProductSpecificationSnapShotAtLoanApplication> objListOfProductSpecificationSnapShotAtLoanApplication=objLMSDAO.getListOfProductSpecificationSnapShotAtLoanApplication(i.getId());
					if(objListOfProductSpecificationSnapShotAtLoanApplication!=null && objListOfProductSpecificationSnapShotAtLoanApplication.size()>0) {
						for(ProductSpecificationSnapShotAtLoanApplication i2:objListOfProductSpecificationSnapShotAtLoanApplication) {		
							toPut.addProductSpecificationDTO(i2.getProductspecificationassumption().getId(), i2.getProductspecificationassumption().getName(), i2.getAssumptionvalue());
						}
					}

					toAdd.setProduct(toPut);
					
					if(i.getCreatedAt()!=null)
						toAdd.setCreatedAt(utility.getDateFromTimeStamp(i.getCreatedAt().toString()));
					if(i.getCreatedBy()!=null)
						toAdd.setCreatedBy(new ApplicationUserResponseDTO(i.getCreatedBy().getUserId(), i.getCreatedBy().getDisplayName()));
					if(i.getUpdatedAt()!=null)
						toAdd.setUpdatedAt(utility.getDateFromTimeStamp(i.getUpdatedAt().toString()));
					if(i.getUpdatedBy()!=null)
						toAdd.setUpdatedBy(new ApplicationUserResponseDTO(i.getUpdatedBy().getUserId(), i.getUpdatedBy().getDisplayName()));

					toSend.add(toAdd);
				}
				
				authResp=new ListOfLoanApplicationsResponseDTO(toSend);
				return authResp;
			}else{
				authResp= new ResponceWithMessage(false,"No Data Found");
				return authResp;
			}
			
		} catch (Exception ex) {
			authResp= new ResponceWithMessage(ex);
			return authResp;
		}finally{
			if(LOG.isInfoEnabled()){
				LOG.info("Returned Response is:");
				if(authResp!=null){
					LOG.info("Response Requested_Action: {} ",new Object[]{authResp.getRequested_Action()});
				}
				LOG.info("WebClientRestContoller.getDisbursedLoanApplications()--End");
			}
		}
	}
	
	@RequestMapping(method=RequestMethod.POST,value={"/LMSServer/getCompletedLoanApplications"})
	@PreAuthorize("hasAuthority('LOAN_MANAGEMENT_R') OR hasAuthority('LOAN_MANAGEMENT_RW')")
	public BasicResponce getCompletedLoanApplications() {

		if(LOG.isInfoEnabled()){
			LOG.info("WebClientRestContoller.getCompletedLoanApplications()--Start");
		}
		BasicResponce authResp = null;

		try {	

			List<LoanApplication> fromDB= objLMSDAO.getLoanApplicationsByStatus(LoanApplicationStatus.COMPLETED);
			
			List<LoanApplicationsResponseDTO> toSend = new ArrayList<LoanApplicationsResponseDTO>();
			
			if(fromDB!=null && fromDB.size()>0){
				
				for(LoanApplication i:fromDB) {

					LoanApplicationsResponseDTO toAdd=new LoanApplicationsResponseDTO();

					toAdd.setId(i.getId());
					toAdd.setCustomer(new CustomerResponseDTO(i.getCustomer().getId(),i.getCustomer().getCellNoInString(),i.getCustomer().getFullName(),i.getCustomer().getPakistaniCNIC()));
					toAdd.setRequestedamount(i.getRequestedamount());
					toAdd.setStatus(i.getStatus().name());

					Product temp=i.getProduct();
					ProductResponseDTO toPut=new ProductResponseDTO(temp.getId(),temp.getProductName(),temp.getProductCatagory(),temp.getTermsAndConditionFilePath());
					List<KYCAnswerSnapShotAtLoanApplication> objKYCAnswerSnapShotAtLoanApplication=objLMSDAO.getListKYCAnswerSnapShotForApplication(i.getId());
					if(objKYCAnswerSnapShotAtLoanApplication!=null && objKYCAnswerSnapShotAtLoanApplication.size()>0) {
						for(KYCAnswerSnapShotAtLoanApplication i2:objKYCAnswerSnapShotAtLoanApplication) {						
							if(i2!=null && i2.getQuestion()!=null)
							toPut.addKYCQuestionWithAnswerDTO(i2.getQuestion().getId(), i2.getQuestion().getQuestionToAsk(),i2.getQuestion().getCatagory(), i2.getQuestion().getAnswerType(),i2.getQuestionanswer());
						}
					}

					List<ProductSpecificationSnapShotAtLoanApplication> objListOfProductSpecificationSnapShotAtLoanApplication=objLMSDAO.getListOfProductSpecificationSnapShotAtLoanApplication(i.getId());
					if(objListOfProductSpecificationSnapShotAtLoanApplication!=null && objListOfProductSpecificationSnapShotAtLoanApplication.size()>0) {
						for(ProductSpecificationSnapShotAtLoanApplication i2:objListOfProductSpecificationSnapShotAtLoanApplication) {		
							toPut.addProductSpecificationDTO(i2.getProductspecificationassumption().getId(), i2.getProductspecificationassumption().getName(), i2.getAssumptionvalue());
						}
					}

					toAdd.setProduct(toPut);
					
					if(i.getCreatedAt()!=null)
						toAdd.setCreatedAt(utility.getDateFromTimeStamp(i.getCreatedAt().toString()));
					if(i.getCreatedBy()!=null)
						toAdd.setCreatedBy(new ApplicationUserResponseDTO(i.getCreatedBy().getUserId(), i.getCreatedBy().getDisplayName()));
					if(i.getUpdatedAt()!=null)
						toAdd.setUpdatedAt(utility.getDateFromTimeStamp(i.getUpdatedAt().toString()));
					if(i.getUpdatedBy()!=null)
						toAdd.setUpdatedBy(new ApplicationUserResponseDTO(i.getUpdatedBy().getUserId(), i.getUpdatedBy().getDisplayName()));

					toSend.add(toAdd);
				}
				
				authResp=new ListOfLoanApplicationsResponseDTO(toSend);
				return authResp;
			}else{
				authResp= new ResponceWithMessage(false,"No Data Found");
				return authResp;
			}
			
		} catch (Exception ex) {
			authResp= new ResponceWithMessage(ex);
			return authResp;
		}finally{
			if(LOG.isInfoEnabled()){
				LOG.info("Returned Response is:");
				if(authResp!=null){
					LOG.info("Response Requested_Action: {} ",new Object[]{authResp.getRequested_Action()});
				}
				LOG.info("WebClientRestContoller.getCompletedLoanApplications()--End");
			}
		}
	}


	@RequestMapping(method=RequestMethod.POST,value={"/LMSServer/getPaidOrUnpaidDisbursedLoans"} )
	//	@PreAuthorize("hasAuthority('LOAN_MANAGEMENT_R') OR hasAuthority('LOAN_MANAGEMENT_RW')")
	//	public BasicResponce getDisbursedLoansByStatus(@RequestParam(value = "disbursedLoanStatus") int disbursedLoanStatus) {
	public BasicResponce getPaidOrUnpaidDisbursedLoans(@RequestParam(value = "isPaidRequired") boolean isPaidRequired) {

		if(LOG.isInfoEnabled()){
			LOG.info("WebClientRestContoller.getUnPaidDisbursedLoans()--Start");
			LOG.info("Received Parameters are:");
			//			LOG.info("Loan Disbursement Status: {} ",new Object[]{disbursedLoanStatus});
		}
		BasicResponce authResp = null;

		try {	

			//			List<DisbursedLoans> fromDB=objLMSDAO.getAllDisbursedLoansByStatus(DisbursedLoanStatus.values()[disbursedLoanStatus]);
			List<DisbursedLoans> fromDB=objLMSDAO.getAllPaidOrUnPaidDisbursedLoans(isPaidRequired);

			List<DisbursedLoanResponseDTO> toSend = new ArrayList<DisbursedLoanResponseDTO>();
			for(DisbursedLoans i:fromDB) {

				DisbursedLoanResponseDTO toAdd=new DisbursedLoanResponseDTO();

				toAdd.setId(i.getId());

				LoanApplicationsResponseDTO toPut=new LoanApplicationsResponseDTO();
				toPut.setId(i.getApplication().getId());

				/*long convertedCellNo = 0;
				if(String.valueOf(i.getApplication().getCustomer().getCellNo()).startsWith("34")){
					convertedCellNo=Long.valueOf(String.valueOf(i.getApplication().getCustomer().getCellNo()).replaceFirst("34","034")).longValue();
				}*/

				toPut.setCustomer(new CustomerResponseDTO(i.getApplication().getCustomer().getId(), i.getApplication().getCustomer().getCellNoInString(), i.getApplication().getCustomer().getFullName(), i.getApplication().getCustomer().getPakistaniCNIC(), i.getApplication().getCustomer().getStatus(), i.getApplication().getCustomer().getPinCode()));
				toPut.setProduct(new ProductResponseDTO(i.getApplication().getProduct().getId(), i.getApplication().getProduct().getProductName(), i.getApplication().getProduct().getProductCatagory(),i.getApplication().getProduct().getTermsAndConditionFilePath()));
				toPut.setRequestedamount(i.getApplication().getRequestedamount());
				toPut.setStatus(i.getApplication().getStatus().name());

				if(i.getApplication()!=null && i.getApplication().getCreatedAt()!=null)
					toPut.setCreatedAt(utility.getDateFromTimeStamp(i.getApplication().getCreatedAt().toString()));
				if(i.getApplication().getCreatedBy()!=null)
					toPut.setCreatedBy(new ApplicationUserResponseDTO(i.getApplication().getCreatedBy().getUserId(), i.getApplication().getCreatedBy().getDisplayName()));
				if(i.getApplication()!=null && i.getApplication().getUpdatedAt()!=null)	
					toPut.setUpdatedAt(utility.getDateFromTimeStamp(i.getApplication().getUpdatedAt().toString()));
				if(i.getApplication().getUpdatedBy()!=null)
					toPut.setUpdatedBy(new ApplicationUserResponseDTO(i.getApplication().getUpdatedBy().getUserId(), i.getApplication().getUpdatedBy().getDisplayName()));

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
					toAdd.setCreatedBy(new ApplicationUserResponseDTO(i.getCreatedBy().getUserId(), i.getCreatedBy().getDisplayName()));
				
				if(i.getUpdatedAt()!=null)
					toAdd.setUpdatedAt(utility.getDateFromTimeStamp(i.getUpdatedAt().toString()));
				if(i.getUpdatedBy()!=null)
					toAdd.setUpdatedBy(new ApplicationUserResponseDTO(i.getUpdatedBy().getUserId(), i.getUpdatedBy().getDisplayName()));

				toSend.add(toAdd);
			}

			authResp=new ListOfDisbursedLoansResponseDTO(toSend);
			return authResp;
		} catch (Exception ex) {
			authResp= new ResponceWithMessage(ex);
			return authResp;
		}finally{
			if(LOG.isInfoEnabled()){
				LOG.info("Returned Response is:");
				if(authResp!=null){
					LOG.info("Response Requested_Action: {} ",new Object[]{authResp.getRequested_Action()});
				}
				LOG.info("WebClientRestContoller.getUnPaidDisbursedLoans()--End");
			}
		}
	}

	@RequestMapping(method=RequestMethod.POST,value={"/LMSServer/getLoanApplicationById"} )
	@PreAuthorize("hasAuthority('LOAN_MANAGEMENT_R') OR hasAuthority('LOAN_MANAGEMENT_RW')")
	public BasicResponce getLoanApplicationById(@RequestParam(value = "loanApplicationId") int loanApplicationId) {

		if(LOG.isInfoEnabled()){
			LOG.info("WebClientRestContoller.getLoanApplicationById()--Start");
			LOG.info("Received Parameters are:");
			LOG.info("Loan Application Id: {} ",new Object[]{loanApplicationId});
		}
		BasicResponce authResp = null;
		try {	

			LoanApplication fromDB=objLMSDAO.getLoanApplicationById(loanApplicationId);
			LoanApplicationsResponseDTO toAdd=new LoanApplicationsResponseDTO();

			if(fromDB!=null){
				toAdd.setId(fromDB.getId());
				CustomerResponseDTO toAddCustomerDTO=new CustomerResponseDTO();
				toAddCustomerDTO.setId(fromDB.getCustomer().getId());
				toAddCustomerDTO.setCellNo(fromDB.getCustomer().getCellNoInString());
				toAddCustomerDTO.setFullName(fromDB.getCustomer().getFullName());
				toAddCustomerDTO.setCnic(fromDB.getCustomer().getPakistaniCNIC());
				toAddCustomerDTO.setStatus(fromDB.getCustomer().getStatus());
				toAddCustomerDTO.setIdCardFrontPath(fromDB.getCustomer().getIdCardFrontPath());
				toAddCustomerDTO.setIdCardBackPath(fromDB.getCustomer().getIdCardBackPath());
				toAddCustomerDTO.setIdCardWithFacePath(fromDB.getCustomer().getIdCardWithFacePath());
				toAdd.setCustomer(toAddCustomerDTO);

				toAdd.setRequestedamount(fromDB.getRequestedamount());
				toAdd.setStatus(fromDB.getStatus().name());
	
				Product temp=fromDB.getProduct();
				ProductResponseDTO toPut=new ProductResponseDTO(temp.getId(),temp.getProductName(),temp.getProductCatagory(),temp.getTermsAndConditionFilePath());
	
				List<KYCAnswerSnapShotAtLoanApplication> objKYCAnswerSnapShotAtLoanApplication=objLMSDAO.getListKYCAnswerSnapShotForApplication(fromDB.getId());
	
	
				if(objKYCAnswerSnapShotAtLoanApplication!=null && objKYCAnswerSnapShotAtLoanApplication.size()>0) {
					for(KYCAnswerSnapShotAtLoanApplication i2:objKYCAnswerSnapShotAtLoanApplication) {
						if(i2.getQuestion()!=null)
						toPut.addKYCQuestionWithAnswerDTO(i2.getQuestion().getId(), i2.getQuestion().getQuestionToAsk(),i2.getQuestion().getCatagory(), i2.getQuestion().getAnswerType(),i2.getQuestionanswer());
					}
				}
	
				List<ProductSpecificationSnapShotAtLoanApplication> objListOfProductSpecificationSnapShotAtLoanApplication=objLMSDAO.getListOfProductSpecificationSnapShotAtLoanApplication(fromDB.getId());
	
				if(objListOfProductSpecificationSnapShotAtLoanApplication!=null && objListOfProductSpecificationSnapShotAtLoanApplication.size()>0) {
					for(ProductSpecificationSnapShotAtLoanApplication i2:objListOfProductSpecificationSnapShotAtLoanApplication) {	
						if(i2.getProductspecificationassumption()!=null)
						toPut.addProductSpecificationDTO(i2.getProductspecificationassumption().getId(), i2.getProductspecificationassumption().getName(), i2.getAssumptionvalue());
					}
				}
				
				//getting Number of Details Here(First Loan Applications)
				List<LoanApplication> fromDBLoanApplicationList= objLMSDAO.getLoanApplicationsUsingCellNo(fromDB.getCustomer().getCellNo());
				toAdd.setCustomerLoanSummary(getLoanSummaryDetails(fromDBLoanApplicationList, fromDB.getCustomer().getId()));
				
	
				toAdd.setProduct(toPut);
				if(fromDB.getCreatedAt()!=null)
					toAdd.setCreatedAt(utility.getDateFromTimeStamp(fromDB.getCreatedAt().toString()));
				if(fromDB.getCreatedBy()!=null)
					toAdd.setCreatedBy(new ApplicationUserResponseDTO(fromDB.getCreatedBy().getUserId(),fromDB.getCreatedBy().getDisplayName()));
				if(fromDB.getUpdatedAt()!=null)
					toAdd.setUpdatedAt(utility.getDateFromTimeStamp(fromDB.getUpdatedAt().toString()));
				if(fromDB.getUpdatedBy()!=null)
					toAdd.setUpdatedBy(new ApplicationUserResponseDTO(fromDB.getUpdatedBy().getUserId(),fromDB.getUpdatedBy().getDisplayName()));
				
				toAdd.setRequested_Action(true);
	
				authResp=toAdd;
				return authResp;
			}else{
				authResp=new ResponceWithMessage(false,"Failed To Find Loan Application");
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
				LOG.info("WebClientRestContoller.getLoanApplicationById()--End");
			}
		}
	}
	
	
	
	@RequestMapping(method=RequestMethod.POST,value={"/LMSServer/getAllLoanApplications"} )
	@PreAuthorize("hasAuthority('LOAN_MANAGEMENT_R') OR hasAuthority('LOAN_MANAGEMENT_RW')")
	public BasicResponce getAllLoanApplications() {

		if(LOG.isInfoEnabled()){
			LOG.info("WebClientRestContoller.getAllLoanApplications()--Start");
		}
		BasicResponce authResp = null;
		try {	

			List<LoanApplication> objLoanApplicationList=objLMSDAO.getAllLoanApplication();
			List<LoanApplicationsResponseDTO> toSend=new ArrayList<LoanApplicationsResponseDTO>();

			if(objLoanApplicationList!=null && objLoanApplicationList.size()>0){
				for(LoanApplication fromDB:objLoanApplicationList){
					LoanApplicationsResponseDTO toAdd=new LoanApplicationsResponseDTO();
					
					toAdd.setId(fromDB.getId());
					CustomerResponseDTO toAddCustomerDTO=new CustomerResponseDTO();
					toAddCustomerDTO.setId(fromDB.getCustomer().getId());
					toAddCustomerDTO.setCellNo(fromDB.getCustomer().getCellNoInString());
					toAddCustomerDTO.setFullName(fromDB.getCustomer().getFullName());
					toAddCustomerDTO.setCnic(fromDB.getCustomer().getPakistaniCNIC());
					toAddCustomerDTO.setStatus(fromDB.getCustomer().getStatus());
					toAddCustomerDTO.setIdCardFrontPath(fromDB.getCustomer().getIdCardFrontPath());
					toAddCustomerDTO.setIdCardBackPath(fromDB.getCustomer().getIdCardBackPath());
					toAddCustomerDTO.setIdCardWithFacePath(fromDB.getCustomer().getIdCardWithFacePath());
					toAdd.setCustomer(toAddCustomerDTO);
	
					toAdd.setRequestedamount(fromDB.getRequestedamount());
					toAdd.setStatus(fromDB.getStatus().name());
		
					Product temp=fromDB.getProduct();
					ProductResponseDTO toPut=new ProductResponseDTO(temp.getId(),temp.getProductName(),temp.getProductCatagory(),temp.getTermsAndConditionFilePath());
		
					List<KYCAnswerSnapShotAtLoanApplication> objKYCAnswerSnapShotAtLoanApplication=objLMSDAO.getListKYCAnswerSnapShotForApplication(fromDB.getId());
		
		
					if(objKYCAnswerSnapShotAtLoanApplication!=null && objKYCAnswerSnapShotAtLoanApplication.size()>0) {
						for(KYCAnswerSnapShotAtLoanApplication i2:objKYCAnswerSnapShotAtLoanApplication) {
							if(i2.getQuestion()!=null)
							toPut.addKYCQuestionWithAnswerDTO(i2.getQuestion().getId(), i2.getQuestion().getQuestionToAsk(),i2.getQuestion().getCatagory(), i2.getQuestion().getAnswerType(),i2.getQuestionanswer());
						}
					}
		
					List<ProductSpecificationSnapShotAtLoanApplication> objListOfProductSpecificationSnapShotAtLoanApplication=objLMSDAO.getListOfProductSpecificationSnapShotAtLoanApplication(fromDB.getId());
		
					if(objListOfProductSpecificationSnapShotAtLoanApplication!=null && objListOfProductSpecificationSnapShotAtLoanApplication.size()>0) {
						for(ProductSpecificationSnapShotAtLoanApplication i2:objListOfProductSpecificationSnapShotAtLoanApplication) {	
							if(i2.getProductspecificationassumption()!=null)
							toPut.addProductSpecificationDTO(i2.getProductspecificationassumption().getId(), i2.getProductspecificationassumption().getName(), i2.getAssumptionvalue());
						}
					}
					
					//getting Number of Details Here(First Loan Applications)
					List<LoanApplication> fromDBLoanApplicationList= objLMSDAO.getLoanApplicationsUsingCellNo(fromDB.getCustomer().getCellNo());
					toAdd.setCustomerLoanSummary(getLoanSummaryDetails(fromDBLoanApplicationList, fromDB.getCustomer().getId()));
					
		
					toAdd.setProduct(toPut);
					if(fromDB.getCreatedAt()!=null)
						toAdd.setCreatedAt(utility.getDateFromTimeStamp(fromDB.getCreatedAt().toString()));
					if(fromDB.getCreatedBy()!=null)
						toAdd.setCreatedBy(new ApplicationUserResponseDTO(fromDB.getCreatedBy().getUserId(),fromDB.getCreatedBy().getDisplayName()));
					if(fromDB.getUpdatedAt()!=null)
						toAdd.setUpdatedAt(utility.getDateFromTimeStamp(fromDB.getUpdatedAt().toString()));
					if(fromDB.getUpdatedBy()!=null)
						toAdd.setUpdatedBy(new ApplicationUserResponseDTO(fromDB.getUpdatedBy().getUserId(),fromDB.getUpdatedBy().getDisplayName()));
					
					toAdd.setRequested_Action(true);
		
					toSend.add(toAdd);
					
				}
			
				authResp=new ListOfLoanApplicationsResponseDTO(toSend);
				return authResp;
			
			}else{
				authResp=new ResponceWithMessage(false,"No Loan Application Found");
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
				LOG.info("WebClientRestContoller.getAllLoanApplications()--End");
			}
		}
	}
	

	/*@RequestMapping(method=RequestMethod.POST,value={"/LMSServer/disburseApprovedLoan"} )
	@PreAuthorize("hasAuthority('LOAN_MANAGEMENT_RW')")
	public BasicResponce disburseApprovedLoan(@RequestParam(value = "applicationId", required = true) int applicationId,
			@RequestHeader(value = "authString") String authString) {
		try {
			BasicResponce authResp = null;
			DisbursedLoans disbursedLoan=objLMSDAO.getDisbursedLoanByApplicationId(applicationId);
			if(disbursedLoan==null){
				LoanApplication loanApplication=objLMSDAO.getLoanApplicationById(applicationId);
				List<ProductSpecification> productAssumption=objLMSDAO.getProductSpecificationAgainstProduct(loanApplication.getProduct().getId());
				String loanPeriod = null;
				String gracePeriod = null;

				for(int i=0;i<productAssumption.size();i++){
					if(productAssumption.get(i).getProductSpecificationAssumption().getName().replace(" ", "").equalsIgnoreCase("LoanDuration")){
						LOG.info("Loan Period Days: {} ",new Object[]{productAssumption.get(i).getAssumptionValue()});
						loanPeriod=productAssumption.get(i).getAssumptionValue();
					}

					if(productAssumption.get(i).getProductSpecificationAssumption().getName().replace(" ", "").equalsIgnoreCase("gracePeriod")){
						LOG.info("Grace Period Days: {} ",new Object[]{productAssumption.get(i).getAssumptionValue()});
						gracePeriod=productAssumption.get(i).getAssumptionValue();
					}
				}

				if(loanPeriod!=null || gracePeriod!=null){
					objLMSDAO.disburseNewLoan(applicationId,
							generalLedgerService.getRequiredDate(new Date(), Integer.valueOf(loanPeriod).intValue()), 
							generalLedgerService.getRequiredDate(new Date(), generalLedgerService.getSum(loanPeriod,gracePeriod)), 
							objLMSDAO.getApplicationUserByToken(authString));

					DisbursedLoans recentlyDisbursedLoan=objLMSDAO.getDisbursedLoanByApplicationId(applicationId);

					if(recentlyDisbursedLoan!=null) {

						ResponceWithMessage respOneLoadWallet=generalLedgerService.recordDisbursementFinancialCombinedEntryToGl(Constants.GENERAL_LEDGER_ENTRY_FOR_ONE_LOAD_ACCOUNT, "Principal Disbursement", recentlyDisbursedLoan.getId(), recentlyDisbursedLoan.getTransactionId(), recentlyDisbursedLoan.getApplication().getRequestedamount(), true,objLMSDAO.getApplicationUserByToken(authString));

						if(respOneLoadWallet!=null && respOneLoadWallet.getRequested_Action()){

							ResponceWithMessage respServiceFeeAccount=generalLedgerService.recordDisbursementFinancialCombinedEntryToGl(Constants.GENERAL_LEDGER_ENTRY_FOR_SERVICE_FEE_ACCOUNT, "Service Fee", recentlyDisbursedLoan.getId(), recentlyDisbursedLoan.getTransactionId(), 100, true,objLMSDAO.getApplicationUserByToken(authString));

							if(respServiceFeeAccount!=null && respServiceFeeAccount.getRequested_Action()){

								ResponceWithMessage respFedAccount=generalLedgerService.recordDisbursementFinancialCombinedEntryToGl(Constants.GENERAL_LEDGER_ENTRY_FOR_FED_ACCOUNT, "SF FED Charge", recentlyDisbursedLoan.getId(), recentlyDisbursedLoan.getTransactionId(), 16, true,objLMSDAO.getApplicationUserByToken(authString));

								if(respFedAccount!=null && respFedAccount.getRequested_Action()){

									objLMSDAO.updateLoanApplication(applicationId, LoanApplicationStatus.DISBURSED, objLMSDAO.getApplicationUserByToken(authString));

									authResp=new BasicResponce(true);
									return authResp;
								}
							}
						}	

						authResp=new ResponceWithMessage(false,"Something Went Wrong");
						return authResp;

					} else if(applicationId==-2) {
						authResp=new ResponceWithMessage(false,"must be unique.");
						return authResp;
					} else {
						authResp=new ResponceWithMessage(false,"Some constraint failed");
						return authResp;
					}
				}else{
					authResp=new ResponceWithMessage(false,"Product Assumption Not Found. Unable to Process");
					return authResp;
				}
			} else{
				return new ResponceWithMessage(false,"Loan Already Disbursed");
			}
			
			
		} catch (Exception ex){
			return new ResponceWithMessage(false,"Exception Occurred: "+ex);
		}	}*/
	
	
	@RequestMapping(method=RequestMethod.POST,value={"/LMSServer/disburseApprovedLoanUsingTransaction"} )
	@PreAuthorize("hasAuthority('LOAN_MANAGEMENT_RW')")
	public BasicResponce disburseApprovedLoanUsingTransaction(@RequestParam(value = "applicationId", required = true) int applicationId,
			@RequestHeader(value = "authString") String authString) {
		
		if(LOG.isInfoEnabled()){
			LOG.info("GeneralLedgerService.disburseApprovedLoanUsingTransaction()--Start");
			LOG.info("Received Parameters are:");
			LOG.info("Application Id: {} ",new Object[]{applicationId});
			LOG.info("Auth String: {} ",new Object[]{authString});
		}
		
		BasicResponce authResp = null;
		try{
			generalLedgerService.disburseApprovedLoanUsingTxn(applicationId, authString);
			
			authResp=new ResponceWithMessage(true,"Success");
			return authResp;
		}catch(Exception e){
			authResp=new ResponceWithMessage(false,e.getMessage());
			return authResp;
		}finally{
			if(LOG.isDebugEnabled()){
				LOG.info("GeneralLedgerService.disburseApprovedLoanUsingTransaction()--End");
			}
		}
	}


	/*@RequestMapping(method=RequestMethod.POST,value={"/LMSServer/approveALoan"} )
	@PreAuthorize("hasAuthority('LOAN_MANAGEMENT_RW')")
	public BasicResponce approveALoan(@RequestParam(value = "applicationId", required = true) int applicationId,
			@RequestHeader(value = "authString") String authString) {

		if(LOG.isInfoEnabled()){
			LOG.info("WebClientRestContoller.approveALoan()--Start");
			LOG.info("Received Parameters are:");
			LOG.info("Loan Application Id: {} ",new Object[]{applicationId});
		}

		try {
			DisbursedLoans disbursedLoan=objLMSDAO.getDisbursedLoanByApplicationId(applicationId);
			if(disbursedLoan==null){
				objLMSDAO.updateLoanApplication(applicationId, LoanApplicationStatus.APPROVED, objLMSDAO.getApplicationUserByToken(authString));
				// Generate Pushnotification , sms , email here
				return new BasicResponce(true);
			}else{
				return new ResponceWithMessage(false,"Loan Already Disbursed");
			}
		} catch (Exception ex) {
			return new ResponceWithMessage(ex);
		}finally{
		}
	}*/

	/*@RequestMapping(method=RequestMethod.POST,value={"/LMSServer/repayLoan"} )
	@PreAuthorize("hasAuthority('LOAN_MANAGEMENT_RW')")
	public BasicResponce repayLoanAmount(@RequestParam(value = "cellNo", required = true) long cellNo,
			@RequestParam(value = "repayLoanAmount") long repayLoanAmount,
			@RequestHeader(value = "authString") String authString) {

		if(LOG.isInfoEnabled()){
			LOG.info("WebClientRestContoller.repayLoanAmount()--Start");
			LOG.info("Received Parameters are:");
			LOG.info("Cell No: {} ",new Object[]{cellNo});
			LOG.info("Loan Repay Amount: {} ",new Object[]{repayLoanAmount});
			LOG.info("authString Id: {} ",new Object[]{authString});
		}
		BasicResponce authResp = null;

		try {

			Customer customer=objLMSDAO.getCustomerAgainstCellNo(cellNo);

			List<LoanApplication> loanApplicationList=objLMSDAO.getAllNonCompletedDisbursedLoanApplicationsForCustomer(customer.getId());

			if(loanApplicationList!=null && loanApplicationList.size()>0){
				DisbursedLoans disbursedLoan=objLMSDAO.getDisbursedLoanByApplicationId(loanApplicationList.get(0).getId());

				if(disbursedLoan!=null) {

					Long outstandingBalancing = generalLedgerService.getLoanOutstandingBalance(disbursedLoan.getId());

					if(repayLoanAmount>=outstandingBalancing){

						if(outstandingBalancing<=0){

							ResponceWithMessage responePayableAccount=generalLedgerService.recordFinancialEntryToGlIndividually(Constants.GENERAL_LEDGER_ENTRY_FOR_PAYABLE_ACCOUNT, "Repayment", disbursedLoan.getId(), disbursedLoan.getTransactionId(), repayLoanAmount, false,objLMSDAO.getApplicationUserByToken(authString));

							if(responePayableAccount.getRequested_Action()){
								authResp=new ResponceWithMessage(true,"Repayment is Successful");
								return authResp;
							}else{
								authResp=new ResponceWithMessage(false,"Failed to Process Repayment Successfully");
								return authResp;
							}

						}else{
							Long totalAmountForLoanReceivableAccountEntry=outstandingBalancing;
							Long totalAmountForPayableAccountEntry=repayLoanAmount-outstandingBalancing;

							ResponceWithMessage responeLoanReceivable=generalLedgerService.recordFinancialEntryToGlIndividually(Constants.GENERAL_LEDGER_ENTRY_FOR_LOAN_RECEIVABLE_ACCOUNT, "Repayment", disbursedLoan.getId(), disbursedLoan.getTransactionId(), totalAmountForLoanReceivableAccountEntry, false,objLMSDAO.getApplicationUserByToken(authString));

							if(responeLoanReceivable.getRequested_Action()){
								ResponceWithMessage responePayableAccount=generalLedgerService.recordFinancialEntryToGlIndividually(Constants.GENERAL_LEDGER_ENTRY_FOR_PAYABLE_ACCOUNT, "Repayment", disbursedLoan.getId(), disbursedLoan.getTransactionId(), totalAmountForPayableAccountEntry, false,objLMSDAO.getApplicationUserByToken(authString));

								if(responePayableAccount.getRequested_Action()){

									ResponceWithMessage responeTargetWallet=generalLedgerService.recordFinancialEntryToGlIndividually(Constants.GENERAL_LEDGER_ENTRY_FOR_ONE_LOAD_ACCOUNT, "Repayment", disbursedLoan.getId(), disbursedLoan.getTransactionId(), repayLoanAmount, true,objLMSDAO.getApplicationUserByToken(authString));

									if(responeTargetWallet.getRequested_Action()){

										Long calculateInternalLoanBookEntryAmount=generalLedgerService.getBalanceForSpecificGlAgainstLoanId(disbursedLoan.getId());

										if(calculateInternalLoanBookEntryAmount!=null && calculateInternalLoanBookEntryAmount>0){

											if(repayLoanAmount>calculateInternalLoanBookEntryAmount){
												ResponceWithMessage responeInternalBook=generalLedgerService.recordFinancialEntryToGlIndividually(Constants.GENERAL_LEDGER_ENTRY_FOR_INTERNAL_LOAN_BOOK_ACCOUNT, "Repayment", disbursedLoan.getId(), disbursedLoan.getTransactionId(), calculateInternalLoanBookEntryAmount, true,objLMSDAO.getApplicationUserByToken(authString));

												if(responeInternalBook.getRequested_Action()){

													disbursedLoan.setLoanStatus(DisbursedLoanStatus.PAID);
													if(generalLedgerService.updateLoanStatusAccordingToOutstandingBalance(disbursedLoan.getId(), disbursedLoan, objLMSDAO.getApplicationUserByToken(authString))){
														if(objLMSDAO.updateLoanApplication(disbursedLoan.getApplication().getId(), LoanApplicationStatus.COMPLETED, objLMSDAO.getApplicationUserByToken(authString))){
															LOG.debug("Repayment is Successful and Loan and Application Status is Also Changed as Full Amount is Repaid");
															authResp=new ResponceWithMessage(true,"Repayment is Successful and Loan Status is Also Changed as Full Amount is Repaid");
															return authResp;
														}else{
															LOG.debug("Repayment is Successful And Loan Status is Also changed But Application Status is not Changed");
															authResp=new ResponceWithMessage(false,"Repayment is Successful And Loan Status is Also changed But Application Status is not Changed");
															return authResp;
														}
													}else{
														LOG.debug("Repayment is Successful But Loan Status is Not Changed as Full Amount is Not Repaid Yet");
														authResp=new ResponceWithMessage(true,"Repayment is Successful But Loan Status is Not Changed as Full Amount is Not Repaid Yet");
														return authResp;
													}



												}else{
													authResp=new ResponceWithMessage(false,"Failed to Complete Repayment Process Successfully");
													return authResp;
												}
											}else{
												generalLedgerService.recordFinancialEntryToGlIndividually(Constants.GENERAL_LEDGER_ENTRY_FOR_INTERNAL_LOAN_BOOK_ACCOUNT, "Repayment", disbursedLoan.getId(), disbursedLoan.getTransactionId(), repayLoanAmount, true,objLMSDAO.getApplicationUserByToken(authString));

												disbursedLoan.setLoanStatus(DisbursedLoanStatus.PAID);
												if(generalLedgerService.updateLoanStatusAccordingToOutstandingBalance(disbursedLoan.getId(), disbursedLoan, objLMSDAO.getApplicationUserByToken(authString))){
													
													if(objLMSDAO.updateLoanApplication(disbursedLoan.getApplication().getId(), LoanApplicationStatus.COMPLETED, objLMSDAO.getApplicationUserByToken(authString))){
														LOG.debug("Repayment is Successful and Loan and Application Status is Also Changed as Full Amount is Repaid");
														authResp=new ResponceWithMessage(true,"Repayment is Successful and Loan Status is Also Changed as Full Amount is Repaid");
														return authResp;
													}else{
														LOG.debug("Repayment is Successful And Loan Status is Also changed But Application Status is not Changed");
														authResp=new ResponceWithMessage(false,"Repayment is Successful And Loan Status is Also changed But Application Status is not Changed");
														return authResp;
													}
													
												}else{
													LOG.debug("Repayment is Successful But Loan Status is Not Changed as Full Amount is Not Repaid Yet");
													authResp=new ResponceWithMessage(true,"Repayment is Successful But Loan Status is Not Changed as Full Amount is Not Repaid Yet");
													return authResp;
												}
											}

										}else{

											disbursedLoan.setLoanStatus(DisbursedLoanStatus.PAID);
											if(generalLedgerService.updateLoanStatusAccordingToOutstandingBalance(disbursedLoan.getId(), disbursedLoan, objLMSDAO.getApplicationUserByToken(authString))){
												if(objLMSDAO.updateLoanApplication(disbursedLoan.getApplication().getId(), LoanApplicationStatus.COMPLETED, objLMSDAO.getApplicationUserByToken(authString))){
													LOG.debug("Repayment is Successful and Loan and Application Status is Also Changed as Full Amount is Repaid");
													authResp=new ResponceWithMessage(true,"Repayment is Successful and Loan Status is Also Changed as Full Amount is Repaid");
													return authResp;
												}else{
													LOG.debug("Repayment is Successful And Loan Status is Also changed But Application Status is not Changed");
													authResp=new ResponceWithMessage(false,"Repayment is Successful And Loan Status is Also changed But Application Status is not Changed");
													return authResp;
												}
											}else{
												LOG.debug("Repayment is Successful But Loan Status is Not Changed as Full Amount is Not Repaid Yet");
												authResp=new ResponceWithMessage(true,"Repayment is Successful But Loan Status is Not Changed as Full Amount is Not Repaid Yet");
												return authResp;
											}

										}

									}else{
										authResp=new ResponceWithMessage(false,"Failed to Process Repayment Successfully");
										return authResp;
									}
								}else{
									authResp=new ResponceWithMessage(false,"Failed to Initiate Repayment Successfully");
									return authResp;
								}
							}else{
								authResp=new ResponceWithMessage(false,"Failed to Initiate Repayment Successfully");
								return authResp;
							}
						}


					}else{
						ResponceWithMessage responeLoanReceivable=generalLedgerService.recordFinancialEntryToGlIndividually(Constants.GENERAL_LEDGER_ENTRY_FOR_LOAN_RECEIVABLE_ACCOUNT, "Repayment", disbursedLoan.getId(), disbursedLoan.getTransactionId(), repayLoanAmount, false,objLMSDAO.getApplicationUserByToken(authString));

						if(responeLoanReceivable.getRequested_Action()){
							ResponceWithMessage responeWallet=generalLedgerService.recordFinancialEntryToGlIndividually(Constants.GENERAL_LEDGER_ENTRY_FOR_ONE_LOAD_ACCOUNT, "Repayment", disbursedLoan.getId(), disbursedLoan.getTransactionId(), repayLoanAmount, true,objLMSDAO.getApplicationUserByToken(authString));

							if(responeWallet.getRequested_Action()){

								Long calculateInterLoanBookEntryAmount=generalLedgerService.getBalanceForSpecificGlAgainstLoanId(disbursedLoan.getId());

								if(calculateInterLoanBookEntryAmount!=null && calculateInterLoanBookEntryAmount>0){

									if(repayLoanAmount>calculateInterLoanBookEntryAmount){
										ResponceWithMessage responeInternalBook=generalLedgerService.recordFinancialEntryToGlIndividually(Constants.GENERAL_LEDGER_ENTRY_FOR_INTERNAL_LOAN_BOOK_ACCOUNT, "Repayment", disbursedLoan.getId(), disbursedLoan.getTransactionId(), calculateInterLoanBookEntryAmount, true,objLMSDAO.getApplicationUserByToken(authString));

										if(responeInternalBook.getRequested_Action()){
											authResp=new ResponceWithMessage(true,"Repayment is Successfull");
											return authResp;
										}else{
											authResp=new ResponceWithMessage(false,"Failed to Complete Repayment Process Successfully");
											return authResp;
										}
									}else{
										ResponceWithMessage responeInternalBook=generalLedgerService.recordFinancialEntryToGlIndividually(Constants.GENERAL_LEDGER_ENTRY_FOR_INTERNAL_LOAN_BOOK_ACCOUNT, "Repayment", disbursedLoan.getId(), disbursedLoan.getTransactionId(), repayLoanAmount, true,objLMSDAO.getApplicationUserByToken(authString));

										if(responeInternalBook.getRequested_Action()){
											authResp=new ResponceWithMessage(true,"Repayment is Successfull");
											return authResp;
										}else{
											authResp=new ResponceWithMessage(false,"Failed to Complete Repayment Process Successfully");
											return authResp;
										}
									}

								}else{
									authResp=new ResponceWithMessage(true,"Repayment is Successfull");
									return authResp;
								}

							}else{
								authResp=new ResponceWithMessage(false,"Failed to Complete Repayment Process Successfully");
								return authResp;
							}

						}else{
							authResp=new ResponceWithMessage(false,"Failed to Initiate Repayment Successfully");
							return authResp;
						}
					}

				}else{
					authResp=new ResponceWithMessage(false,"No Disbursed Loan Found Against Given Customer");
					return authResp;
				}
			}else{
				authResp=new ResponceWithMessage(false,"No Loan Application Found Against Given Customer");
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
				LOG.info("WebClientRestContoller.repayLoanAmount()--End");
			}
		}	}
	
	*/
	
	
	
	
	@RequestMapping(method=RequestMethod.POST,value={"/LMSServer/repayLoanUsingTransaction"} )
	@PreAuthorize("hasAuthority('LOAN_MANAGEMENT_RW')")
	public BasicResponce repayLoanUsingTransaction(@RequestParam(value = "cellNo", required = true) long cellNo,
			@RequestParam(value = "repayLoanAmount") long repayLoanAmount,
			@RequestHeader(value = "authString") String authString) {
		
		if(LOG.isInfoEnabled()){
			LOG.info("WebClientRestContoller.repayLoanUsingTransaction()--Start");
			LOG.info("Received Parameters are:");
			LOG.info("Cell No: {} ",new Object[]{cellNo});
			LOG.info("Loan Repay Amount: {} ",new Object[]{repayLoanAmount});
			LOG.info("authString Id: {} ",new Object[]{authString});
		}
		
		BasicResponce authResp = null;
		try{
			generalLedgerService.repayLoanUsingTransaction(cellNo, repayLoanAmount, authString);
			
			authResp=new ResponceWithMessage(true,"Success");
			return authResp;
		}catch(Exception e){
			authResp=new ResponceWithMessage(false,e.getMessage());
			return authResp;
		}finally{
			if(LOG.isDebugEnabled()){
				LOG.info("WebClientRestContoller.repayLoanUsingTransaction()--End");
			}
		}
		
	}
	

	
	
	@RequestMapping(method=RequestMethod.POST,value={"/LMSServer/checkAndMarkOverdueEntriesAgainstCustomerUsingCellNo"} )
	@PreAuthorize("hasAuthority('LOAN_MANAGEMENT_RW')")
	public BasicResponce checkAndMarkOverdueEntriesAgainstCustomerUsingCellNo(@RequestParam(value = "cellNo", required = true) long cellNo) {
		
		if(LOG.isInfoEnabled()){
			LOG.info("WebClientRestContoller.checkAndMarkOverdueEntriesAgainstCustomerUsingCellNo()--Start");
			LOG.info("Received Parameters are:");
			LOG.info("Cell No: {} ",new Object[]{cellNo});
			
		}
		BasicResponce authResp = null;

		try {

			Customer customer=objLMSDAO.getCustomerAgainstCellNo(cellNo);
			List<LoanApplication> loanApplicationList=objLMSDAO.getAllNonCompletedDisbursedLoanApplicationsForCustomer(customer.getId());
			DisbursedLoans disbursedLoan=objLMSDAO.getDisbursedLoanByApplicationId(loanApplicationList.get(0).getId());

			if(disbursedLoan!=null) {
				//1- get disbursement Date and findout current installment status(due, gracePeriod, OverDue).
				//2- for that we need product details, and need all assumptions(Grace Period Days, Loan Period etc).
				//3- if installment is overdue then we will mark late payment fee entry(credit) in late payment fee GL.
				//4- Also will mark another entry for FED on Late Payment Fee.
				LOG.debug("Loan Disbursement Date: {} ",new Object[]{disbursedLoan.getCreatedAt()});

				List<ProductSpecification> productAssumption=objLMSDAO.getProductSpecificationAgainstProduct(loanApplicationList.get(0).getProduct().getId());
				String loanPeriod = null;
				String gracePeriod = null;
				String latePaymentFee = null;

				for(int i=0;i<productAssumption.size();i++){
					if(productAssumption.get(i).getProductSpecificationAssumption().getName().replace(" ", "").equalsIgnoreCase("LoanDuration")){
						LOG.debug("Loan Period Days: {} ",new Object[]{productAssumption.get(i).getAssumptionValue()});
						loanPeriod=productAssumption.get(i).getAssumptionValue();
					}

					if(productAssumption.get(i).getProductSpecificationAssumption().getName().replace(" ", "").equalsIgnoreCase("GracePeriod")){
						LOG.debug("Grace Period Days: {} ",new Object[]{productAssumption.get(i).getAssumptionValue()});
						gracePeriod=productAssumption.get(i).getAssumptionValue();
					}

					if(productAssumption.get(i).getProductSpecificationAssumption().getName().replace(" ", "").equalsIgnoreCase("latePaymentFee")){
						LOG.debug("Late Payment Fee: {} ",new Object[]{productAssumption.get(i).getAssumptionValue()});
						latePaymentFee=productAssumption.get(i).getAssumptionValue();
					}
				}

				Boolean loanRepaymentOverDueStatus=generalLedgerService.isLoanRepaymentIsOverDue(disbursedLoan.getCreatedAt(),loanPeriod,gracePeriod);

				if(loanRepaymentOverDueStatus){
					ResponceWithMessage responeLatePaymentFeeInLoanReceivableGl=generalLedgerService.recordFinancialEntryToGlIndividually(Constants.GENERAL_LEDGER_ENTRY_FOR_LOAN_RECEIVABLE_ACCOUNT, "Late Payment Fee", disbursedLoan.getId(), disbursedLoan.getTransactionId(), Long.valueOf(latePaymentFee).longValue(), true,null);

					if(responeLatePaymentFeeInLoanReceivableGl!=null && responeLatePaymentFeeInLoanReceivableGl.getRequested_Action()){
						ResponceWithMessage responeLatePaymentFeeInLatePaymentFeeGl=generalLedgerService.recordFinancialEntryToGlIndividually(Constants.GENERAL_LEDGER_ENTRY_FOR_LATE_PAYMENT_FEE_ACCOUNT, "Late Payment Fee", disbursedLoan.getId(), disbursedLoan.getTransactionId(), Long.valueOf(latePaymentFee).longValue(), false,null);

						if(responeLatePaymentFeeInLatePaymentFeeGl.getRequested_Action()){

							ResponceWithMessage responeLatePaymentFEDInLoanReceivableGl=generalLedgerService.recordFinancialEntryToGlIndividually(Constants.GENERAL_LEDGER_ENTRY_FOR_LOAN_RECEIVABLE_ACCOUNT, "LP FED Charge", disbursedLoan.getId(), disbursedLoan.getTransactionId(), generalLedgerService.getFedAmount(Long.valueOf(latePaymentFee).longValue(),16), true,null);

							if(responeLatePaymentFEDInLoanReceivableGl.getRequested_Action()){

								ResponceWithMessage responeLatePaymentFEDInFEDGL=generalLedgerService.recordFinancialEntryToGlIndividually(Constants.GENERAL_LEDGER_ENTRY_FOR_FED_ACCOUNT, "LP FED Charge", disbursedLoan.getId(), disbursedLoan.getTransactionId(), generalLedgerService.getFedAmount(Long.valueOf(latePaymentFee).longValue(),16), false,null);

								if(responeLatePaymentFEDInFEDGL.getRequested_Action()){
									authResp=new ResponceWithMessage(true, "Success");
									return authResp;
								}else{
									authResp=new ResponceWithMessage(true, "Late Payment Fee Account Entry is Successful. But Failed to Process Late Payment FED");
									return authResp;
								}

							}else{
								authResp=new ResponceWithMessage(true, "Late Payment Fee Account Entry is Successful. But Failed to Process Late Payment FED");
								return authResp;
							}

						}else{
							authResp=new ResponceWithMessage(true, "Failed to Process Late Payment Fee");
							return authResp;
						}


					}else{
						authResp=new ResponceWithMessage(true, "Failed to Process Late Payment Fee");
						return authResp;
					}

				}else{
					authResp=new ResponceWithMessage(false,"No Need to Mark Late Payement Fee in GL as Cut Off is not Passed");
					return authResp;
				}

			}else{
				authResp=new ResponceWithMessage(false,"No Loan Found Against Given Customer");
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
				LOG.info("WebClientRestContoller.checkAndMarkOverdueEntriesAgainstCustomerUsingCellNo()--End");
			}
		}
	}


	@RequestMapping(method=RequestMethod.POST,value={"/LMSServer/checkAndChangeLoanStatusAgainstCustomerUsingCellNo"} )
	@PreAuthorize("hasAuthority('LOAN_MANAGEMENT_RW')")
	public BasicResponce checkAndChangeLoanStatusAgainstCustomerUsingCellNo(@RequestParam(value = "cellNo", required = true) long cellNo,
			@RequestHeader(value = "authString") String authString) {

		if(LOG.isInfoEnabled()){
			LOG.info("WebClientRestContoller.checkAndChangeLoanStatusAgainstCustomerUsingCellNo()--Start");
			LOG.info("Received Parameters are:");
			LOG.info("Cell No: {} ",new Object[]{cellNo});
			LOG.info("authString Id: {} ",new Object[]{authString});
		}
		BasicResponce authResp = null;

		try {

			Customer customer=objLMSDAO.getCustomerAgainstCellNo(cellNo);
			List<LoanApplication> loanApplicationList=objLMSDAO.getAllNonCompletedDisbursedLoanApplicationsForCustomer(customer.getId());
			DisbursedLoans disbursedLoan=objLMSDAO.getNonPaidLoan(loanApplicationList.get(0).getId());

			if(disbursedLoan!=null) {

				LOG.debug("Loan Disbursement Date: {} ",new Object[]{disbursedLoan.getCreatedAt()});
				LOG.debug("Loan Grace Period Date: {} ",new Object[]{disbursedLoan.getGraceDueDate()});
				LOG.debug("Loan OverDue Date: {} ",new Object[]{disbursedLoan.getDueDate()});

				Date currentDate=new Date();

				if(currentDate.before(disbursedLoan.getDueDate())){
					LOG.debug("No Need to Change Anything as Date not Passed Yet");
					authResp=new ResponceWithMessage(true,"No Need to Change Anything as Date not Passed Yet");
					return authResp;
				}else{
					if(currentDate.after(disbursedLoan.getDueDate()) && currentDate.before(disbursedLoan.getGraceDueDate())){
						LOG.debug("Need to Change Status To Grace Period");
						disbursedLoan.setLoanStatus(DisbursedLoanStatus.GRACE_PERIOD);
						objLMSDAO.updateDisbursedLoanStatusUsingLoanId(disbursedLoan.getId(), disbursedLoan, objLMSDAO.getApplicationUserByToken(authString));
						authResp=new ResponceWithMessage(true,"Successfully Marked Grace Period Status");
						return authResp;
					}else if(currentDate.after(disbursedLoan.getGraceDueDate())){
						LOG.debug("Need to Change Status To Over Due");
						disbursedLoan.setLoanStatus(DisbursedLoanStatus.OVER_DUE);
						objLMSDAO.updateDisbursedLoanStatusUsingLoanId(disbursedLoan.getId(), disbursedLoan, objLMSDAO.getApplicationUserByToken(authString));
						authResp=new ResponceWithMessage(true,"Successfully Marked Overdue Status");
						return authResp;
					}else{
						LOG.debug("SomeThing Went Wrong, Unable to Update Loan Status");
						authResp=new ResponceWithMessage(false,"SomeThing Went Wrong, Unable to Update Loan Status");
						return authResp;
					}
				}
			}else{
				authResp=new ResponceWithMessage(false,"No Loan Found Against Given Customer");
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
				LOG.info("WebClientRestContoller.checkAndChangeLoanStatusAgainstCustomerUsingCellNo()--End");
			}
		}
	}



	@RequestMapping(method=RequestMethod.POST,value={"/LMSServer/GetAllProducts"} )
	@PreAuthorize("hasAuthority('PRODUCT_MANAGEMENT_R') OR hasAuthority('PRODUCT_MANAGEMENT_RW')")
	public BasicResponce GetAllProducts(@RequestHeader("authString") String authString) {

		if(LOG.isInfoEnabled()){
			LOG.info("WebClientRestContoller.GetAllProducts()--Start");
		}
		BasicResponce authResp = null;

		try {
			List<Product> fromDB=objLMSDAO.getAllProductsEager();		
			ArrayList <String> headers=new ArrayList<String>();
			headers.add("ID");
			headers.add("PRODUCTNAME");
			headers.add("PRODUCTCATAGORY");		
			headers.add("PRODUCTASSUMPTIONS");
			headers.add("PRODUCTASSUMPTIONVALUES");
			headers.add("PRODUCTKYCQUESTIONS");

			headers.add("CreatedAt");
			headers.add("CreatedBy");
			headers.add("UpdatedAt");
			headers.add("UpdatedBy");

			ArrayList<List<String>> table_Data = new ArrayList<List<String>>();			
			for(Product i:fromDB) {
				ArrayList <String> dataRow=new ArrayList<String>();
				dataRow.add(i.getId()+"");
				dataRow.add(i.getProductName()+"");
				dataRow.add(i.getProductCatagory()+"");


				{
					String PRODUCTASSUMPTIONS="";

					try {
						if(i.getProductSpecification()!=null && i.getProductSpecification().size()>0){
							for(ProductSpecification i2: i.getProductSpecification()){
								PRODUCTASSUMPTIONS=PRODUCTASSUMPTIONS+","+i2.getProductSpecificationAssumption().getName();
							}
						}

					} catch (Exception ex){
						PRODUCTASSUMPTIONS="Exception while getting PRODUCTASSUMPTIONS";
					}
					dataRow.add(PRODUCTASSUMPTIONS);
				}


				{
					String PRODUCTASSUMPTIONVALUES="";
					try {
						if(i.getProductSpecification()!=null && i.getProductSpecification().size()>0){
							for(ProductSpecification i2: i.getProductSpecification()){
								PRODUCTASSUMPTIONVALUES=PRODUCTASSUMPTIONVALUES+","+i2.getAssumptionValue();
							}
						}
					} catch (Exception ex){
						PRODUCTASSUMPTIONVALUES="Exception while getting PRODUCTASSUMPTIONVALUES";
					}
					dataRow.add(PRODUCTASSUMPTIONVALUES);
				}

				{
					String PRODUCTKYCQUESTIONS="";
					try {
						if(i.getQuestions()!=null && i.getQuestions().size()>0){
							for(KYCQuestion i2: i.getQuestions()){
								PRODUCTKYCQUESTIONS=PRODUCTKYCQUESTIONS+","+i2.getQuestionToAsk();
							}
						}
					} catch (Exception ex){
						PRODUCTKYCQUESTIONS="Exception while getting PRODUCTKYCQUESTIONS";
					}
					dataRow.add(PRODUCTKYCQUESTIONS);
				}

				dataRow.add(i.getCreatedAt()+"");
				if(i.getCreatedBy()!=null){
					dataRow.add(i.getCreatedBy().getDisplayName());
				}else{
					dataRow.add("");
				}
				
				dataRow.add(i.getUpdatedAt()+"");
				if(i.getUpdatedBy()!=null){
					dataRow.add(i.getUpdatedBy().getDisplayName());
				}else{
					dataRow.add("");
				}

				table_Data.add(dataRow);
			}			
			authResp=new ReponceForWebTableData(true,"All Products",headers,table_Data);
			return authResp;
		} catch (Exception ex) {
			authResp=new ResponceWithMessage(ex);
			return authResp;
		}finally{
			if(LOG.isInfoEnabled()){
				LOG.info("Returned Response is:");
				if(authResp!=null){
					LOG.info("Response Requested_Action: {} ",new Object[]{authResp.getRequested_Action()});
				}
				LOG.info("WebClientRestContoller.getLoanApplicationById()--End");
			}
		}

	}
	
	
	@RequestMapping(method=RequestMethod.POST,value={"/LMSServer/GetAllProductsWithDTOResponse"} )
	@PreAuthorize("hasAuthority('PRODUCT_MANAGEMENT_R') OR hasAuthority('PRODUCT_MANAGEMENT_RW')")
	public BasicResponce GetAllProductsWithDTOResponse(@RequestHeader("authString") String authString) {

		if(LOG.isInfoEnabled()){
			LOG.info("WebClientRestContoller.GetAllProductsWithDTOResponse--Start");
		}

		BasicResponce response= objProductService.GetAllProductsWithDTOResponse(authString);

		if(LOG.isInfoEnabled()){
			LOG.info("Returned Response is:");
			LOG.info("Response Requested_Action: {} ",new Object[]{response.getRequested_Action()});
			LOG.info("WebClientRestContoller.GetAllProductsWithDTOResponse--End");
		}

		return response;
	}

	/*public BasicResponce recordFinancialEntryToGl(@RequestParam(value = "glId") int glId,
			@RequestParam(value = "glDescription") String glDescription,
			@RequestParam(value = "loanId") int loanId,
			@RequestParam(value = "transactionId") String transactionId,
			@RequestParam(value = "transactionAmount") long transactionAmount,
			@RequestParam(value = "isDebitTransaction") boolean isDebitTransaction,
			@RequestHeader(value = "authString") String authString) {

		if (LOG.isInfoEnabled()) {
			LOG.info("WebClientRestContoller.recordFinancialEntryToGl()--Start");
			LOG.info("Received Parameters are:");
			LOG.info("GL Id: {} ", new Object[] { glId });
			LOG.info("GL Description: {} ", new Object[] { glDescription });
			LOG.info("Loan Id: {} ", new Object[] { loanId });
			LOG.info("Transaction Id: {} ", new Object[] { transactionId });
			LOG.info("Transaction Amount: {} ", new Object[] { transactionAmount });
			LOG.info("is Debit Transaction: {} ", new Object[] { isDebitTransaction });
			LOG.info("authString: {} ", new Object[] { authString });
		}
		BasicResponce authResp = null;

		authResp = generalLedgerService.recordDisbursementFinancialCombinedEntryToGl(glId,
				glDescription, loanId, transactionId, transactionAmount,
				isDebitTransaction, objLMSDAO.getApplicationUserByToken(authString));


		if (LOG.isInfoEnabled()) {
			LOG.info("Returned Response is:");
			if (authResp != null) {
				LOG.info("Response Requested_Action: {} ",new Object[] { authResp.getRequested_Action() });
			}
			LOG.info("WebClientRestContoller.recordFinancialEntryToGl()--End");
		}
		return authResp;	}*/


	@RequestMapping(method=RequestMethod.POST,value={"/LMSServer/GetGeneralLedger"} )
	@PreAuthorize("hasAuthority('GENERAL_LEDGER_MANAGEMENT_R') OR hasAuthority('GENERAL_LEDGER_MANAGEMENT_RW')")
	public BasicResponce getGeneralLedger(@RequestParam(value = "glId") int glId) {

		if (LOG.isInfoEnabled()) {
			LOG.info("WebClientRestContoller.getGeneralLedger()--Start");
			LOG.info("Received Parameters are:");
			LOG.info("GL Id: {} ", new Object[] { glId });
		}
		BasicResponce authResp = null;

		try {
			List<GeneralLedgerResponseDTO> toParse=generalLedgerService.getGeneralLedger(glId);

			ListOfGeneralLedgerEntriesResponseDTO toReturn = new ListOfGeneralLedgerEntriesResponseDTO(toParse);

			authResp=toReturn;
			return authResp;

		} catch (Exception ex) {
			authResp=new ResponceWithMessage(false,ex.getMessage());
			return authResp;
		}finally{
			if(LOG.isInfoEnabled()){
				LOG.info("Returned Response is:");
				if(authResp!=null){
					LOG.info("Response Requested_Action: {} ",new Object[]{authResp.getRequested_Action()});
				}
				LOG.info("WebClientRestContoller.getGeneralLedger()--End");
			}
		}
	}


	
	@RequestMapping(method=RequestMethod.POST,value={"/LMSServer/getCustomerBalanceByCellNo"} )
	public BasicResponce getCustomerBalanceByCellNo(@RequestParam(value = "cellNo") long cellNo) {

		if (LOG.isInfoEnabled()) {
			LOG.info("WebClientRestContoller.getCustomerBalanceByCellNo()--Start");
			LOG.info("Received Parameters are:");
			LOG.info("Cell No: {} ", new Object[] { cellNo });
		}
		BasicResponce authResp = null;

		try {
			Customer customer=objLMSDAO.getCustomerAgainstCellNo(cellNo);
			List<DisbursedLoans> disbursedLoanList=objLMSDAO.getAllDisbursedButNotCompletedLoanApplicationsForCustomer(customer.getId());

			if(disbursedLoanList!=null && disbursedLoanList.size()>0){
				Long customerOustandingBalance=generalLedgerService.getLoanOutstandingBalance(disbursedLoanList.get(0).getId());

				CustomerResponseDTO objCustomerDTO=new CustomerResponseDTO(customer.getId(), customer.getCellNoInString(), customer.getFullName(), customer.getPakistaniCNIC(),(customerOustandingBalance));

				authResp=new ResponceDTO<CustomerResponseDTO>(true,objCustomerDTO);
				return authResp;
			}else{
				CustomerResponseDTO objCustomerDTO=new CustomerResponseDTO(customer.getId(), customer.getCellNoInString(), customer.getFullName(), customer.getPakistaniCNIC(),0);

				authResp=new ResponceDTO<CustomerResponseDTO>(true,objCustomerDTO);
				return authResp;
			}


		} catch (Exception ex) {
			authResp=new ResponceWithMessage(false,"Unknown Exception");
			return authResp;
		}finally{
			if(LOG.isInfoEnabled()){
				LOG.info("Returned Response is:");
				if(authResp!=null){
					LOG.info("Response Requested_Action: {} ",new Object[]{authResp.getRequested_Action()});
				}
				LOG.info("WebClientRestContoller.getCustomerBalanceByCellNo()--End");
			}
		}
	}


	@RequestMapping(method=RequestMethod.POST,value={"/LMSServer/updateLoanApplicationStatus"} )
	public BasicResponce updateLoanApplicationStatus(@RequestParam(value = "loanApplicationId") int applicationId,
			@RequestParam(value = "loanApplicationStatus") String loanApplicationStatus,
			@RequestHeader(value = "authString") String authString) {

		if (LOG.isInfoEnabled()) {
			LOG.info("WebClientRestContoller.updateLoanApplicationStatus()--Start");
			LOG.info("Received Parameters are:");
			LOG.info("Loan Application Id: {} ", new Object[] { applicationId });
			LOG.info("Loan Application Status: {} ", new Object[] { loanApplicationStatus });
			LOG.info("Auth String: {} ", new Object[] { authString });
		}
		BasicResponce authResp = null;

		try {
			boolean response= objLMSDAO.updateLoanApplication(applicationId, LoanApplicationStatus.valueOf(loanApplicationStatus.toUpperCase()), objLMSDAO.getApplicationUserByToken(authString));

			if(response){
				authResp=new ResponceWithMessage(true,"Success");
				return authResp;
			}else{
				authResp=new ResponceWithMessage(false,"Failed to Change Loan Application Status");
				return authResp;
			}
			
		} catch (Exception ex) {
			authResp=new ResponceWithMessage(false,"Unknown Exception");
			return authResp;
		}finally{
			if(LOG.isInfoEnabled()){
				LOG.info("Returned Response is:");
				if(authResp!=null){
					LOG.info("Response Requested_Action: {} ",new Object[]{authResp.getRequested_Action()});
				}
				LOG.info("WebClientRestContoller.updateLoanApplicationStatus()--End");
			}
		}
	}
	
	
	@RequestMapping(method=RequestMethod.POST,value={"/LMSServer/updateLoanApplicationStatusToInReview"} )
	public BasicResponce updateLoanApplicationStatusToInReview(@RequestParam(value = "loanApplicationId") int applicationId,
			@RequestBody List<KYCQuestionRequestDTO> kycQuestionArray,
			@RequestHeader(value = "authString") String authString) {

		if (LOG.isInfoEnabled()) {
			LOG.info("WebClientRestContoller.updateLoanApplicationStatusToInReview()--Start");
			LOG.info("Received Parameters are:");
			LOG.info("Loan Application Id: {} ", new Object[] { applicationId });
			LOG.info("Kyc Answer Array: {} ",new Object[]{kycQuestionArray});
			LOG.info("Auth String: {} ", new Object[] { authString });
		}
		BasicResponce authResp = null;
		try {
			
			LoanApplicationSelectedKYCQuestionsForReview loanApplicationSelectedKYCQuestionsForReview = objLMSDAO.getLoanApplicationSelectedKYCQuestionsForReview(applicationId);
			int nextSequenceOrder;
			if(loanApplicationSelectedKYCQuestionsForReview!=null){
				nextSequenceOrder=loanApplicationSelectedKYCQuestionsForReview.getReviewSequenceId()+1;
			}else{
				nextSequenceOrder=1;
			}
			
			for(KYCQuestionRequestDTO kycQuestionDTO:kycQuestionArray){
				if(objLMSDAO.updateLoanApplicationSelectedQuestions(applicationId, kycQuestionDTO, nextSequenceOrder, objLMSDAO.getApplicationUserByToken(authString))){
					//good to go
				}else{
					authResp=new ResponceWithMessage(false,"Failed to Add Loan Application Selected Question Id: "+kycQuestionDTO.getQuestionId());
					return authResp;
				}
			}
			boolean response= objLMSDAO.updateLoanApplication(applicationId, LoanApplicationStatus.PENDINGFORCORRECTION, objLMSDAO.getApplicationUserByToken(authString));
			if(response){
				authResp=new ResponceWithMessage(true,"Success");
				return authResp;
			}else{
				authResp=new ResponceWithMessage(false,"Failed to Change Loan Application Status");
				return authResp;
			}
		} catch (Exception ex) {
			authResp=new ResponceWithMessage(false,"Unknown Exception");
			return authResp;
		}finally{
			if(LOG.isInfoEnabled()){
				LOG.info("Returned Response is:");
				if(authResp!=null){
					LOG.info("Response Requested_Action: {} ",new Object[]{authResp.getRequested_Action()});
				}
				LOG.info("WebClientRestContoller.updateLoanApplicationStatusToInReview()--End");
			}
		}
	}
	
	
	@RequestMapping(method=RequestMethod.POST,value={"/LMSServer/uploadTermsAndConditionFile"} )
	public BasicResponce uploadTermsAndConditionFile(@RequestParam(value = "requiredFile", required=true) MultipartFile requiredFile,
			@RequestHeader(value = "authString") String authString) {
		
		if (LOG.isInfoEnabled()) {
			LOG.info("WebClientRestContoller.uploadTermsAndConditionFile()--Start");
			LOG.info("Received Parameters are:");
			LOG.info("File Name: {} ", new Object[] { requiredFile.getOriginalFilename() });
			LOG.info("Auth String: {} ", new Object[] { authString });
		}
		BasicResponce authResp = null;
		try {
				String requiredFilePath=objServerUtils.storeFile(requiredFile);
				
				String originalFileName=requiredFile.getOriginalFilename();
				String fileNameWithoutExtension=originalFileName.substring(0,originalFileName.indexOf("."));

				boolean response=objLMSDAO.updateUploadedFilePath(fileNameWithoutExtension,requiredFilePath,objLMSDAO.getApplicationUserByToken(authString));

				if(response) {
					authResp=new ResponceWithMessage(true,"Uploaded");
					return authResp;

				} else {
					authResp=new ResponceWithMessage(false,"Failed to Upload");
					return authResp;
				}

		} catch (Exception ex) {
			authResp=new ResponceWithMessage(false,"Unknown Exception");
			return authResp;
		}finally{
			if(LOG.isInfoEnabled()){
				LOG.info("Returned Response is:");
				if(authResp!=null){
					LOG.info("Response Requested_Action: {} ",new Object[]{authResp.getRequested_Action()});
				}
				LOG.info("WebClientRestContoller.uploadTermsAndConditionFile()--End");
			}
		}
	}
	
	@RequestMapping(method=RequestMethod.POST,value={"/LMSServer/updateDisbursedLoanStatusToWriteOff"} )
	@PreAuthorize("hasAuthority('LOAN_MANAGEMENT_RW')")
	public BasicResponce updateDisbursedLoanStatusToWriteOff(@RequestParam(value = "loanId", required = true) int loanId,
			@RequestHeader(value = "authString") String authString) {

		if(LOG.isInfoEnabled()){
			LOG.info("WebClientRestContoller.updateDisbursedLoanStatusToWriteOff()--Start");
			LOG.info("Received Parameters are:");
			LOG.info("Loan id: {} ",new Object[]{loanId});
			LOG.info("authString Id: {} ",new Object[]{authString});
		}
		BasicResponce authResp = null;

		try {

			DisbursedLoans disbursedLoan= objLMSDAO.getDisbursedLoanByLoanId(loanId);
			
			if(disbursedLoan!=null){
				if(disbursedLoan.getLoanStatus()==DisbursedLoanStatus.OVER_DUE){
					disbursedLoan.setLoanStatus(DisbursedLoanStatus.WRITE_OFF);
					Boolean response= objLMSDAO.updateDisbursedLoanStatusUsingLoanId(disbursedLoan.getId(), disbursedLoan, objLMSDAO.getApplicationUserByToken(authString));
					if(response){
						authResp=new ResponceWithMessage(true,"Updated Successful");
						return authResp;
					}else{
						authResp=new ResponceWithMessage(false,"Fail to Update Loan Status to Write Off");
						return authResp;
					}
				}else{
					authResp=new ResponceWithMessage(false,"Disbursed Loan is in Invalid State");
					return authResp;
				}
			}else{
				authResp=new ResponceWithMessage(false,"No Loan Found Against Given Customer");
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
				LOG.info("WebClientRestContoller.updateDisbursedLoanStatusToWriteOff()--End");
			}
		}
	}
	
	
	@RequestMapping(method=RequestMethod.POST,value={"/LMSServer/manualGlEntries"} )
	@PreAuthorize("hasAuthority('LOAN_MANAGEMENT_RW')")
	public BasicResponce manualGlEntries(@RequestBody List<GeneralLedgerRequestDTO> generalLedgerRequestDTOArray,
			@RequestHeader(value = "authString") String authString) {

		if(LOG.isInfoEnabled()){
			LOG.info("WebClientRestContoller.manualGlEntries()--Start");
			LOG.info("Received Parameters are:");
			LOG.info("General Ledger: {} ",new Object[]{generalLedgerRequestDTOArray.size()});
			LOG.info("authString Id: {} ",new Object[]{authString});
		}
		BasicResponce authResp = null;

		try {
			//Credit Debit Sum Validation
			if(manualGLEntriesCreditDebitSumValidation(generalLedgerRequestDTOArray)){
				LOG.info("Total Sum of Credit is Equal to Total Sum of Debit, So Good to Go");
			}else{
				authResp=new ResponceWithMessage(false,"Debit and Credit Sum are Not Balanced");
				return authResp;
			}

			//Unique Gl per Entry Validation
			if(isManualGLEntriesHaveUniqueGlIdValidation(generalLedgerRequestDTOArray)){
				LOG.info("Each Entry has Unique Gl Id, So Good to Go");
			}else{
				authResp=new ResponceWithMessage(false,"Duplicates Entries Found, Unable to Process. Only Single Entry Per GL is Allowed");
				return authResp;
			}
			
			if(generalLedgerRequestDTOArray!=null && !generalLedgerRequestDTOArray.isEmpty()){
				for(GeneralLedgerRequestDTO generalLedgerRequestDTO:generalLedgerRequestDTOArray){
					
					DisbursedLoans disbursedLoan= objLMSDAO.getDisbursedLoanByLoanId(generalLedgerRequestDTO.getLoanId());
					if(disbursedLoan!=null){
						generalLedgerService.recordFinancialEntryToGlIndividually(generalLedgerRequestDTO.getIdGl(), generalLedgerRequestDTO.getGlDescription(), generalLedgerRequestDTO.getLoanId(), disbursedLoan.getTransactionId(), generalLedgerRequestDTO.getAmount(), generalLedgerRequestDTO.isDebitTransaction(), objLMSDAO.getApplicationUserByToken(authString));
					}else{
						LOG.info("Disbursed Loan Not Found. Unable to Process Record With Loan Id: {}",generalLedgerRequestDTO.getLoanId());
					}
					
				}
				authResp=new ResponceWithMessage(true,"Successfully Added");
				return authResp;
			}else{
				authResp=new ResponceWithMessage(false,"Invalid Input Parameters");
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
				LOG.info("WebClientRestContoller.manualGlEntries()--End");
			}
		}
	}
	
	
	public boolean manualGLEntriesCreditDebitSumValidation(List<GeneralLedgerRequestDTO> generalLedgerRequestDTOArray){
		long debitSum = 0;
		long creditSum = 0;
		//Need to Validate First(Credit Sum = Debit Sum)
		for(GeneralLedgerRequestDTO generalLedgerRequestDTO:generalLedgerRequestDTOArray){
			if(generalLedgerRequestDTO.isDebitTransaction()){
				debitSum=debitSum+generalLedgerRequestDTO.getAmount();
			}else{
				if(!generalLedgerRequestDTO.isDebitTransaction()){
					creditSum=creditSum+generalLedgerRequestDTO.getAmount();
				}
			}
		}

		if(debitSum==creditSum){
			LOG.info("Good to Go");
			return true;
		}else{
			LOG.info("Debit and Credit Sum are Not Balanced");
			return false;
		}
	}
	
	public boolean isManualGLEntriesHaveUniqueGlIdValidation(List<GeneralLedgerRequestDTO> generalLedgerRequestDTOArray){
		Map<Integer,Integer> glIdWithDescription = new HashMap<>();
		
		// build hash table with count
		for(GeneralLedgerRequestDTO generalLedgerRequestDTO:generalLedgerRequestDTOArray){
            Integer count = glIdWithDescription.get(generalLedgerRequestDTO.getIdGl());
            if (count == null) {
            	glIdWithDescription.put(generalLedgerRequestDTO.getIdGl(), 1);
            } else {
            	glIdWithDescription.put(generalLedgerRequestDTO.getIdGl(), ++count);
            }
        }

        // Getting duplicate elements from array and Returning Response
        Set<Entry<Integer, Integer>> entrySet = glIdWithDescription.entrySet();
        for (Entry<Integer, Integer> entry : entrySet) {
            if (entry.getValue() > 1) {
            	return false;
            }
        }
        
        return true;
	}
	
	
	private CustomerLoanSummaryResponseDTO getLoanSummaryDetails(List<LoanApplication> fromDBLoanApplicationList, int customerId) throws Exception{
		
		int loanApplicationWithPendingForApprovalState=0;
		int loanApplicationWithApprovedState=0;
		int loanApplicationWithRejectedState=0;
		int loanApplicationWithAcceptedState=0;
		int loanApplicationWithDeclinedState=0;
		int loanApplicationWithDisbursedState=0;
		int loanApplicationWithCompletedState=0;
		
		for(LoanApplication toCheck:fromDBLoanApplicationList){
			if(toCheck.getStatus()==LoanApplicationStatus.PENDINGFORAPPROVAL){
				loanApplicationWithPendingForApprovalState++;
			}else if(toCheck.getStatus()==LoanApplicationStatus.APPROVED){
				loanApplicationWithApprovedState++;
			}else if(toCheck.getStatus()==LoanApplicationStatus.REJECTED){
				loanApplicationWithRejectedState++;
			}else if(toCheck.getStatus()==LoanApplicationStatus.ACCEPTED){
				loanApplicationWithAcceptedState++;
			}else if(toCheck.getStatus()==LoanApplicationStatus.DECLINED){
				loanApplicationWithDeclinedState++;
			}else if(toCheck.getStatus()==LoanApplicationStatus.DISBURSED){
				loanApplicationWithDisbursedState++;
			}else{
				if(toCheck.getStatus()==LoanApplicationStatus.COMPLETED){
					loanApplicationWithCompletedState++;
				}
			}
		}
		
		CustomerLoanSummaryResponseDTO toAddCustomerLoanSummary=new CustomerLoanSummaryResponseDTO();
		LoanApplicationSummaryResponseDTO toAddLoanApplicationSummary=new LoanApplicationSummaryResponseDTO();
		toAddLoanApplicationSummary.setNumberOfLoanApplications(fromDBLoanApplicationList.size());
		toAddLoanApplicationSummary.setNumberOfPendingForApprovalLoanApplications(loanApplicationWithPendingForApprovalState);
		toAddLoanApplicationSummary.setNumberOfApprovedLoanApplications(loanApplicationWithApprovedState);
		toAddLoanApplicationSummary.setNumberOfRejectedLoanApplications(loanApplicationWithRejectedState);
		toAddLoanApplicationSummary.setNumberOfAcceptedLoanApplications(loanApplicationWithAcceptedState);
		toAddLoanApplicationSummary.setNumberOfDeclinedLoanApplications(loanApplicationWithDeclinedState);
		toAddLoanApplicationSummary.setNumberOfDisbursedLoanApplications(loanApplicationWithDisbursedState);
		toAddLoanApplicationSummary.setNumberOfCompletedLoanApplications(loanApplicationWithCompletedState);
		toAddCustomerLoanSummary.setLoanApplicationSummary(toAddLoanApplicationSummary);
		
		
		List<DisbursedLoans> fromDBDisbursedLoanList= objLMSDAO.getAllDisbursedLoansForCustomer(customerId);
		
		int paidInActiveStateCounter=0;
		int paidInGracePeriodStateCounter=0;
		int paidInOverDueStateCounter=0;
		
		for(DisbursedLoans toCheck:fromDBDisbursedLoanList){
			if(toCheck.getExistingStatusJustBeforeFullPayment()!=null){
				if(toCheck.getExistingStatusJustBeforeFullPayment()==DisbursedLoanStatus.ACTIVE){
					paidInActiveStateCounter++;
				}else if(toCheck.getExistingStatusJustBeforeFullPayment()==DisbursedLoanStatus.GRACE_PERIOD){
					paidInGracePeriodStateCounter++;
				}else{
					if(toCheck.getExistingStatusJustBeforeFullPayment()==DisbursedLoanStatus.OVER_DUE){
						paidInOverDueStateCounter++;
					}
				}
			}
		}
		
		DisbursedLoanSummaryResponseDTO toAddDisbursedLoanSummary=new DisbursedLoanSummaryResponseDTO();
		toAddDisbursedLoanSummary.setNumberOfPaidDisbursedLoansInDueDate(paidInActiveStateCounter);
		toAddDisbursedLoanSummary.setNumberOfPaidDisbursedLoansInGracePeriodDate(paidInGracePeriodStateCounter);
		toAddDisbursedLoanSummary.setNumberOfPaidDisbursedLoansAfterDueDate(paidInOverDueStateCounter);
		toAddCustomerLoanSummary.setDisbursedLoanSummary(toAddDisbursedLoanSummary);
		
		toAddCustomerLoanSummary.setDisbursedLoanSummary(toAddDisbursedLoanSummary);
		
		
		return toAddCustomerLoanSummary;
	}
	
	
	/*@RequestMapping(method=RequestMethod.POST,value={"/LMSServer/markLoanApplication"} )
	@PreAuthorize("hasAuthority('LOAN_MANAGEMENT_RW')")
	public BasicResponce manualGlEntries(@RequestBody List<GeneralLedgerRequestDTO> generalLedgerRequestDTOArray,
			@RequestHeader(value = "authString") String authString) {

		if(LOG.isInfoEnabled()){
			LOG.info("WebClientRestContoller.manualGlEntries()--Start");
			LOG.info("Received Parameters are:");
			LOG.info("General Ledger: {} ",new Object[]{generalLedgerRequestDTOArray.size()});
			LOG.info("authString Id: {} ",new Object[]{authString});
		}
		BasicResponce authResp = null;

		try {
			//Credit Debit Sum Validation
			if(manualGLEntriesCreditDebitSumValidation(generalLedgerRequestDTOArray)){
				LOG.info("Total Sum of Credit is Equal to Total Sum of Debit, So Good to Go");
			}else{
				authResp=new ResponceWithMessage(false,"Debit and Credit Sum are Not Balanced");
				return authResp;
			}

			//Unique Gl per Entry Validation
			if(isManualGLEntriesHaveUniqueGlIdValidation(generalLedgerRequestDTOArray)){
				LOG.info("Each Entry has Unique Gl Id, So Good to Go");
			}else{
				authResp=new ResponceWithMessage(false,"Duplicates Entries Found, Unable to Process. Only Single Entry Per GL is Allowed");
				return authResp;
			}
			
			if(generalLedgerRequestDTOArray!=null && !generalLedgerRequestDTOArray.isEmpty()){
				for(GeneralLedgerRequestDTO generalLedgerRequestDTO:generalLedgerRequestDTOArray){
					
					DisbursedLoans disbursedLoan= objLMSDAO.getDisbursedLoanByLoanId(generalLedgerRequestDTO.getLoanId());
					if(disbursedLoan!=null){
						generalLedgerService.recordFinancialEntryToGlIndividually(generalLedgerRequestDTO.getIdGl(), generalLedgerRequestDTO.getGlDescription(), generalLedgerRequestDTO.getLoanId(), disbursedLoan.getTransactionId(), generalLedgerRequestDTO.getAmount(), generalLedgerRequestDTO.isDebitTransaction(), objLMSDAO.getApplicationUserByToken(authString));
					}else{
						LOG.info("Disbursed Loan Not Found. Unable to Process Record With Loan Id: {}",generalLedgerRequestDTO.getLoanId());
					}
					
				}
				authResp=new ResponceWithMessage(true,"Successfully Added");
				return authResp;
			}else{
				authResp=new ResponceWithMessage(false,"Invalid Input Parameters");
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
				LOG.info("WebClientRestContoller.manualGlEntries()--End");
			}
		}
	}*/
	
	//This api will only use when Backend team needs to re-activate blocked or suspended customer
	@RequestMapping(method=RequestMethod.POST,value={"/LMSServer/updateCustomerStatusToActive"} )
	public BasicResponce updateCustomerStatusToActive(@RequestParam(value = "cellNo") long cellNo,
			@RequestParam(value = "activationReason") String activationReason) {

		if(LOG.isInfoEnabled()){
			LOG.info("WebClientRestContollerAndroidApplication.updateCustomerStatusToActive()--Start");
			LOG.info("Received Parameters are:");
			LOG.info("Cell No: {} ",new Object[]{cellNo});
			LOG.info("Activation Reason: {} ",new Object[]{activationReason});
		}
		BasicResponce response = null;
		try {
			Customer customer= objLMSDAO.getCustomerAgainstCellNo(cellNo);
			
			if(customer!=null && (customer.getStatus()==CustomerStatus.SUSPENDED || customer.getStatus()==CustomerStatus.BLOCKED)){
				if(objLMSDAO.updateCustomerStatus(cellNo, CustomerStatus.ACTIVE,activationReason)){
					response=new BasicResponce(true);
					return response;
				}else{
					response=new ResponceWithMessage(false,"Failed to Update Suspended Status");
					return response;
				}
			}else {
				response=new ResponceWithMessage(false,"Customer Not Found or Is in Invalid State");
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
				LOG.info("WebClientRestContollerAndroidApplication.updateCustomerStatusToActive()--End");
			}
		}
	}
	
	
	@RequestMapping(method=RequestMethod.POST,value={"/LMSServer/updateCustomerStatusToSuspended"} )
	public BasicResponce updateCustomerStatusToSuspended(@RequestParam(value = "cellNo") long cellNo,
			@RequestParam(value = "suspensionReason") String suspensionReason) {

		if(LOG.isInfoEnabled()){
			LOG.info("WebClientRestContollerAndroidApplication.updateCustomerStatusToSuspended()--Start");
			LOG.info("Received Parameters are:");
			LOG.info("Cell No: {} ",new Object[]{cellNo});
			LOG.info("Suspension Reason: {} ",new Object[]{suspensionReason});
		}
		BasicResponce response = null;
		try {
			Customer customer= objLMSDAO.getCustomerAgainstCellNo(cellNo);
			
			if(customer!=null && (customer.getStatus()==CustomerStatus.ACTIVE)){
				if(objLMSDAO.updateCustomerStatus(cellNo, CustomerStatus.SUSPENDED,suspensionReason)){
					response=new BasicResponce(true);
					return response;
				}else{
					response=new ResponceWithMessage(false,"Failed to Update Suspended Status");
					return response;
				}
			}else {
				response=new ResponceWithMessage(false,"Customer Not Found or Is in Invalid State");
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
				LOG.info("WebClientRestContollerAndroidApplication.updateCustomerStatusToSuspended()--End");
			}
		}
	}
	
	
	@RequestMapping(method=RequestMethod.POST,value={"/LMSServer/updateCustomerStatusToBlocked"} )
	public BasicResponce updateCustomerStatusToBlocked(@RequestParam(value = "cellNo") long cellNo,
			@RequestParam(value = "blockingReason") String blockingReason) {

		if(LOG.isInfoEnabled()){
			LOG.info("WebClientRestContollerAndroidApplication.updateCustomerStatusToBlocked()--Start");
			LOG.info("Received Parameters are:");
			LOG.info("Cell No: {} ",new Object[]{cellNo});
			LOG.info("Blocking Reason: {} ",new Object[]{blockingReason});
		}
		BasicResponce response = null;
		try {
			Customer customer= objLMSDAO.getCustomerAgainstCellNo(cellNo);
			
			if(customer!=null && (customer.getStatus()==CustomerStatus.ACTIVE || customer.getStatus()==CustomerStatus.SUSPENDED)){
				if(objLMSDAO.updateCustomerStatus(cellNo, CustomerStatus.BLOCKED,blockingReason)){
					response=new BasicResponce(true);
					return response;
				}else{
					response=new ResponceWithMessage(false,"Failed to Update Blocked Status");
					return response;
				}
			}else {
				response=new ResponceWithMessage(false,"Customer Not Found or Customer Is in Invalid State");
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
				LOG.info("WebClientRestContollerAndroidApplication.updateCustomerStatusToBlocked()--End");
			}
		}
	}
	
	@RequestMapping(method=RequestMethod.POST,value="/LMSServer/isCustomerEverAppliedForLoanApplication")
	@PreAuthorize("hasAuthority('LOAN_MANAGEMENT_R') OR hasAuthority('LOAN_MANAGEMENT_RW')")
	public BasicResponce isCustomerEverAppliedForLoanApplication(@RequestParam(value = "cellNo") long cellNo) {

		if(LOG.isInfoEnabled()){
			LOG.info("WebClientRestContollerAndroidApplication.isCustomerEverAppliedForLoanApplication--Start");
			LOG.info("Received Parameters are:");
			LOG.info("Cell No: {} ",new Object[]{cellNo});
		}

		BasicResponce authResp = null;
		try {
				List<LoanApplication> loanApplicationList=objLMSDAO.getLoanApplicationsUsingCellNo(cellNo);
				if(loanApplicationList!=null && loanApplicationList.size()>0){
					authResp=new ResponceWithMessage(true,"Loan Application/Applications Found");
					return authResp;
				}else{
					authResp=new ResponceWithMessage(false,"No Loan Application Found");
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
				LOG.info("WebClientRestContollerAndroidApplication.isCustomerEverAppliedForLoanApplication--End");
			}
		}
	}
}
