package monami.lms.rest.serverwebinterface;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import monami.lms.datadaos.ApplicationUserDAO;
import monami.lms.datadaos.LMSDAO;
import monami.lms.datadaos.RoleDAO;
import monami.lms.dataentities.ApplicationUsers;
import monami.lms.dataentities.Preferences;
import monami.lms.dataentities.Privilege;
import monami.lms.dataentities.Role;
import monami.lms.responceentities.BasicResponce;
import monami.lms.responceentities.ReponceForWebTableData;
import monami.lms.responceentities.ResponceWithMessage;
import monami.lms.responceentities.ResponceWithUserDetails;
import monami.lms.response.datadtos.ApplicationUserResponseDTO;
import monami.lms.response.datadtos.ListOfApplicationUserResponseDTO;
import monami.lms.response.datadtos.ListOfRoleResponseDTO;
import monami.lms.response.datadtos.PrivilegeResponseDTO;
import monami.lms.response.datadtos.RoleResponseDTO;
import monami.lms.security.CustomUser;
import monami.lms.serverutils.ServerUtils;
import monami.lms.socketmessages.NewTableUpdate;

import monami.lms.utilities.Utility;
import monami.lms.websocket.WebsocketService;

@Service
public class ApplicationUserService {
	Logger LOG = LoggerFactory.getLogger(ApplicationUserService.class);
	@Autowired
	private ApplicationUserDAO objApplicationUserDAO;

	@Autowired
	private RoleDAO objRoleDAO;

	@Autowired
	private ServerUtils objServerUtils;

	@Autowired
	private WebsocketService objWebsocketService;
	
	@Autowired 
	private LMSDAO objLMSDAO;
	
	@Autowired
	private Utility utility;
	
	

//TODO: This Method has zero Reference
	public boolean isValidUser(String username,String password) {

		try {
			if(objApplicationUserDAO.getUserByUsernamePassword(username, password) !=null){
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			LOG.error("Error",e);
		}
		return false;
	} 

	public ResponceWithMessage changePassword(String username,String currentPassword,String newPassword,String confirmNewPassword, String authString) {

		try {			
			ApplicationUsers toUpdate=objApplicationUserDAO.getUserByUsernamePassword(username, currentPassword);
			if( toUpdate!=null){

				if(!newPassword.equals(confirmNewPassword)){
					ResponceWithMessage obj=new ResponceWithMessage(false,"Password doesn't match");
					return obj;
				}
				else if (!newPassword.matches("((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[\\W_]).{8,15})")) {
					ResponceWithMessage obj = new ResponceWithMessage(false,
							"Password must be 8-15 characters long with at least one number and one uppercase letter.");
					return obj;
				}
				else {
					toUpdate.setPassword(newPassword);
					objApplicationUserDAO.updateUser(toUpdate);
					ResponceWithMessage obj=new ResponceWithMessage(true,"Password Updated.");

					return obj;
				}
			} else {
				ResponceWithMessage obj=new ResponceWithMessage(false,"Invalid Credentials");
				return obj;
			}
		} catch (Exception e) {
			LOG.error("Error",e);
		}
		ResponceWithMessage obj=new ResponceWithMessage(false,"ENDOFFUNCTION");
		return obj;


	} 

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ResponceWithMessage AddUser(String fullName,String addusername,String adduserpassword,String confirmNewPassword, String newUsertype, String newUserRole, ApplicationUsers loggedInUser) {


		ResponceWithMessage obj = null;
		try {

			if (!addusername.matches("[A-Za-z0-9@._-[\\s]]+")) {
				obj = new ResponceWithMessage(false, "Please enter a valid username.(Alphanumeric,hyphen,underscore and space is allowed)");
				return obj;
			} else if (!adduserpassword.equals(confirmNewPassword)) {
				obj = new ResponceWithMessage(false, "Password doesn't match");
				return obj;
			} else if (newUsertype.isEmpty() || newUsertype == null || newUsertype.equals("")) {
				obj = new ResponceWithMessage(false, "Please select user type");
				return obj;
				//				^((?!.*[\\s])(?=.*[A-Z])(?=.*\\d).{8,15})
			}else if (newUserRole.isEmpty() || newUserRole == null || newUserRole.equals("")) {
				obj = new ResponceWithMessage(false, "Please select user Role");
				return obj;
				//				^((?!.*[\\s])(?=.*[A-Z])(?=.*\\d).{8,15})
			} else if (!adduserpassword.matches("((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[\\W_]).{8,15})")) {
				obj = new ResponceWithMessage(false,
						"Password must be 8-15 characters long with at least one number, one uppercase letter, one lower case letter and a special character.");
				return obj;
			}

			else {
				ApplicationUsers existingUsers = objApplicationUserDAO.getUserByUsername(addusername);

				if (existingUsers != null) {
					obj = new ResponceWithMessage(false, "Username not available");
				}else {
					
					/*Preferences pref= objLMSDAO.getPreferenceValueUsingName("filePathOneLoad");
					if(pref!=null && pref.getPreferenceValue()!=null){
						String encryptedPassword= AES.encrypt(adduserpassword, pref.getPreferenceValue());
						if(encryptedPassword!=null){*/
							ApplicationUsers applicationUsers = new ApplicationUsers();

							applicationUsers.setDisplayName(fullName);
							applicationUsers.setUsername(addusername);
							applicationUsers.setPassword(adduserpassword);
							applicationUsers.setAppliedTheme("Light");
							applicationUsers.setCategory(newUsertype);
							
							applicationUsers.setRoles(Arrays.asList(objRoleDAO.getRoleByRoleName(newUserRole)));
							applicationUsers.setCreatedBy(loggedInUser);
						
							
							int operationResult = objApplicationUserDAO.addUser(applicationUsers);
							if (operationResult > 0) {
								objWebsocketService.createUpdateString(new NewTableUpdate("refreshTable", "", "User Management", "", new ArrayList()));
								obj = new ResponceWithMessage(true, "New User added successfully.");
							} else {
								obj = new ResponceWithMessage(false, "Error while adding user");
							}
						/*}else{
							obj = new ResponceWithMessage(false, "Failed to Encrypt Password");
						}
					}else{
						obj = new ResponceWithMessage(false, "Failed to Get Preference Value");
					}*/
				}
			}

		} catch (Exception e) {
			LOG.error("Error",e);
		}
		return obj;
	}

	public BasicResponce getAllUsers() {

		try {

			List<ApplicationUsers> allUsers = null;
			try {
				allUsers = objApplicationUserDAO.getAllUsers();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				LOG.info("" + e);
			}

			List<String> Table_Headers_ROW = new ArrayList<>();
			Table_Headers_ROW.add("Count");
			Table_Headers_ROW.add("User ID");
			Table_Headers_ROW.add("User Name");
			Table_Headers_ROW.add("Name");
			Table_Headers_ROW.add("Category");
			Table_Headers_ROW.add("User Role");
			Table_Headers_ROW.add("Permissions");
			Table_Headers_ROW.add("Last Login");
			
			
			Table_Headers_ROW.add("CreatedBy");
			Table_Headers_ROW.add("CreatedAt");
			Table_Headers_ROW.add("UpdatedBy");
			Table_Headers_ROW.add("UpdatedAt");
			
			
			
			
			

			if (allUsers == null) {
				ResponceWithMessage toReturn = new ResponceWithMessage(true, "No user found");
				return toReturn;
			} else {
				List<List<String>> Table_Data_List = new ArrayList<>();
				int counter = 1;
				for (ApplicationUsers applicationUsers : allUsers) {
					ArrayList<String> row = new ArrayList<String>();
					row.add(counter + "");
					counter++;
					row.add(applicationUsers.getUserId() + "");
					row.add(applicationUsers.getUsername());
					row.add(applicationUsers.getDisplayName());
					row.add(applicationUsers.getCategory());				
					String roleString="";

					for (Role role : applicationUsers.getRoles()) {
						roleString=roleString+","+role.getRoleName();
					}
					row.add(roleString);
					String privilegeString="";
					
					for (Role role : applicationUsers.getRoles()) {
						for (Privilege objPrivilege :role.getPrivileges()) {
							privilegeString=privilegeString+","+objPrivilege.getPrivilegeName();
						}
						
					}
					row.add(privilegeString);

					try {
						String dbTimeString=applicationUsers.getLastAccess();
						LocalDateTime dbTimeObj= LocalDateTime.parse(dbTimeString, objServerUtils.formatter);
						DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
						String thisIs = formatter.format(dbTimeObj);
						row.add(thisIs);
					} catch (Exception e) {
						row.add("");
					}					
					
					if(applicationUsers.getCreatedBy()!=null){
						row.add(applicationUsers.getCreatedBy().getDisplayName());
					}else{
						row.add("");
					}
					
					row.add(applicationUsers.getCreatedAt()+"");
					
					if(applicationUsers.getUpdatedBy()!=null){
						row.add(applicationUsers.getUpdatedBy().getDisplayName());
					}else{
						row.add("");
					}
								
					row.add(applicationUsers.getUpdateAt()+"");
					
					Table_Data_List.add(row);
				}

				ReponceForWebTableData toReturn = new ReponceForWebTableData(true, "", Table_Headers_ROW,
						Table_Data_List);
				return toReturn;

			}

		} catch (Exception ex) {
			ex.printStackTrace();
			ResponceWithMessage toReturn = new ResponceWithMessage(false, "UNKNOWN ERROR");
			return toReturn;

		}
	}
	
	
	
	public BasicResponce getAllUsersWithDTOResponse() {

		try {
			
			BasicResponce authResp=null;
			List<ApplicationUsers> fromDB = null;
			try {
				fromDB = objApplicationUserDAO.getAllUsers();
			} catch (Exception e) {
				e.printStackTrace();
				LOG.info("" + e);
			}
			
			List<ApplicationUserResponseDTO> toSend= new ArrayList<ApplicationUserResponseDTO>();
			if(fromDB!=null && fromDB.size()>0){
				
				for(ApplicationUsers applicationUser:fromDB) {

					ApplicationUserResponseDTO toAdd=new ApplicationUserResponseDTO();
					toAdd.setUserId(applicationUser.getUserId());
					toAdd.setUsername(applicationUser.getUsername());
					toAdd.setDisplayName(applicationUser.getDisplayName());
					toAdd.setCategory(applicationUser.getCategory());
					toAdd.setLastloginAttempt(applicationUser.getLastloginAttempt());
					
					List<RoleResponseDTO> toSendRoleResponseDTOList=new ArrayList<RoleResponseDTO>();
					
					for (Role role : applicationUser.getRoles()) {
						RoleResponseDTO toAddRoleResponseDTO=new RoleResponseDTO();
						toAddRoleResponseDTO.setId(role.getRoleId());
						toAddRoleResponseDTO.setRoleName(role.getRoleName());
						toAddRoleResponseDTO.setRoleDescription(role.getRoleDescription());
						
						
						List<PrivilegeResponseDTO> toSendPrivResponseDTOList=new ArrayList<PrivilegeResponseDTO>();
						for (Privilege priv : role.getPrivileges()) {
							PrivilegeResponseDTO toAddPrivilegeResponseDTO=new PrivilegeResponseDTO();
							toAddPrivilegeResponseDTO.setPrivilegeId(priv.getPrivilegeId());
							toAddPrivilegeResponseDTO.setPrivilegeName(priv.getPrivilegeName());
							toAddPrivilegeResponseDTO.setPrivilegeDescription(priv.getPrivilegeDescription());
							
							if(priv.getCreatedAt()!=null)
								toAddPrivilegeResponseDTO.setCreatedAt(utility.getDateFromTimeStamp(priv.getCreatedAt().toString()));
							if(priv.getCreatedBy()!=null)
								toAddPrivilegeResponseDTO.setCreatedBy(new ApplicationUserResponseDTO(priv.getCreatedBy().getUserId(), priv.getCreatedBy().getDisplayName()));
							
							if(priv.getUpdatedAt()!=null)
								toAddPrivilegeResponseDTO.setUpdatedAt(utility.getDateFromTimeStamp(priv.getUpdatedAt().toString()));
							if(priv.getUpdatedBy()!=null)
								toAddPrivilegeResponseDTO.setUpdatedBy(new ApplicationUserResponseDTO(priv.getUpdatedBy().getUserId(), priv.getUpdatedBy().getDisplayName()));
						
							toSendPrivResponseDTOList.add(toAddPrivilegeResponseDTO);
						}
						
						toAddRoleResponseDTO.setPrivilegeDTOList(toSendPrivResponseDTOList);
						
						if(role.getCreatedAt()!=null)
							toAddRoleResponseDTO.setCreatedAt(utility.getDateFromTimeStamp(role.getCreatedAt().toString()));
						if(role.getCreatedBy()!=null)
							toAddRoleResponseDTO.setCreatedBy(new ApplicationUserResponseDTO(role.getCreatedBy().getUserId(), role.getCreatedBy().getDisplayName()));
						
						if(role.getUpdatedAt()!=null)
							toAddRoleResponseDTO.setUpdatedAt(utility.getDateFromTimeStamp(role.getUpdatedAt().toString()));
						if(role.getUpdatedBy()!=null)
							toAddRoleResponseDTO.setUpdatedBy(new ApplicationUserResponseDTO(role.getUpdatedBy().getUserId(), role.getUpdatedBy().getDisplayName()));
						
						
						toSendRoleResponseDTOList.add(toAddRoleResponseDTO);
					}
					
					toAdd.setRoles(toSendRoleResponseDTOList);
					
					if(applicationUser.getCreatedAt()!=null)
						toAdd.setCreatedAt(utility.getDateFromTimeStamp(applicationUser.getCreatedAt().toString()));
					if(applicationUser.getCreatedBy()!=null)
						toAdd.setCreatedBy(new ApplicationUserResponseDTO(applicationUser.getCreatedBy().getUserId(), applicationUser.getCreatedBy().getDisplayName()));
					if(applicationUser.getUpdateAt()!=null)
						toAdd.setUpdatedAt(utility.getDateFromTimeStamp(applicationUser.getUpdateAt().toString()));
					if(applicationUser.getUpdatedBy()!=null)
						toAdd.setUpdatedBy(new ApplicationUserResponseDTO(applicationUser.getUpdatedBy().getUserId(), applicationUser.getUpdatedBy().getDisplayName()));
					
					toSend.add(toAdd);
				}	
				
				authResp=new ListOfApplicationUserResponseDTO(toSend);
				authResp.setRequested_Action(true);
				return authResp;
			}else{
				ResponceWithMessage toReturn = new ResponceWithMessage(false, "No Record Found");
				return toReturn;
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			ResponceWithMessage toReturn = new ResponceWithMessage(false, "UNKNOWN ERROR");
			return toReturn;

		}
	}

//TODO: This Method has zero Reference	
	public BasicResponce UpdateTheme(String appliedTheme) {

		try {
			SecurityContext securityContext = SecurityContextHolder.getContext();
			CustomUser user = null;
			ApplicationUsers applicationUser = null;
			if(securityContext.getAuthentication() != null){
				user = (CustomUser) securityContext.getAuthentication().getPrincipal();
				applicationUser = objApplicationUserDAO.getUserById(user.getUserId());
			}
			if (applicationUser != null) {
				applicationUser.setAppliedTheme(appliedTheme);

				if (objApplicationUserDAO.updateUser(applicationUser)) {
					ResponceWithMessage toReturn = new ResponceWithMessage(true, "Theme Changed");
					return toReturn;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			ResponceWithMessage toReturn = new ResponceWithMessage(false, "UNKNOWN ERROR");
			return toReturn;
		}

		return null;
	}

	public ResponceWithUserDetails getUserToUpdate(String userName) {

		ResponceWithUserDetails obj = new ResponceWithUserDetails();

		try {
			ApplicationUsers applicationUsers = objApplicationUserDAO.getUserByUsername(userName);

			if (applicationUsers != null) {
				obj.setDisplayName(applicationUsers.getDisplayName());
				obj.setUsername(applicationUsers.getUsername());
				for (Role role : applicationUsers.getRoles()) {
					obj.setRole(role.getRoleName());
				}
				obj.setCategory(applicationUsers.getCategory());
				
				obj.setRequested_Action(true);
			}
			else {
				obj.setRequested_Action(false);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return obj;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public BasicResponce UpdateUser(String username, String displayName, String category, String role, ApplicationUsers loggedInUser) {

		ResponceWithMessage obj = null;
		try {

			if (username != null && !username.isEmpty() && displayName != null && !displayName.isEmpty() && role != null && !role.isEmpty()) {

				ApplicationUsers applicationUser = objApplicationUserDAO.getUserByUsername(username);
				applicationUser.setDisplayName(displayName);
				applicationUser.setCategory(category);
				applicationUser.setRoles(Arrays.asList(objRoleDAO.getRoleByRoleName(role)));
				applicationUser.setUpdatedBy(loggedInUser);
				
				boolean updated = objApplicationUserDAO.updateUser(applicationUser);
				
				if (updated) {
					obj = new ResponceWithMessage(true, "User Updated");
					objWebsocketService.createUpdateString(new NewTableUpdate("refreshTable", "", "EditUserManagement", username+","+displayName, new ArrayList()));
				} else {
					obj = new ResponceWithMessage(false, "Failed to Update User.");
				}
			} else {
				obj = new ResponceWithMessage(false, "Missing Params");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return obj;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public BasicResponce DeleteUser(String userNameToDelete, String currentLoggedInUser) {

		ResponceWithMessage obj = null;

		try {
			if (userNameToDelete.equals("Administrator")) {
				obj = new ResponceWithMessage(false, "Administrator user can't be deleted");
				return obj;
			}

			if (userNameToDelete.equals(currentLoggedInUser)) {
				obj = new ResponceWithMessage(false, "Current Logged in user can't be deleted");
				return obj;
			}


			boolean result = objApplicationUserDAO.deleteUser(userNameToDelete);

			if (result) {
				obj = new ResponceWithMessage(true, "User Deleted");
				objWebsocketService.createUpdateString(new NewTableUpdate("refreshTable", "", "User Management", userNameToDelete, new ArrayList()));
			} else {
				obj = new ResponceWithMessage(false, "Failed to delete User.");
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return obj;
	}
	
	
}
