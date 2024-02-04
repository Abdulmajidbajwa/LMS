package monami.lms.rest.serverwebinterface;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import monami.lms.datadaos.PrivilegeDAO;
import monami.lms.datadaos.RoleDAO;
import monami.lms.dataentities.ApplicationUsers;
import monami.lms.dataentities.Privilege;
import monami.lms.dataentities.Role;
import monami.lms.responceentities.BasicResponce;
import monami.lms.responceentities.ReponceForWebTableData;
import monami.lms.responceentities.ResponceWithMessage;
import monami.lms.response.datadtos.ApplicationUserResponseDTO;
import monami.lms.response.datadtos.ListOfPrivilegeResponseDTO;
import monami.lms.response.datadtos.ListOfRoleResponseDTO;
import monami.lms.response.datadtos.PrivilegeResponseDTO;
import monami.lms.response.datadtos.RoleResponseDTO;
import monami.lms.socketmessages.NewTableUpdate;
import monami.lms.utilities.Utility;
import monami.lms.websocket.WebsocketService;

@Service
public class RoleService {

	Logger LOG = LoggerFactory.getLogger(RoleService.class);

	@Autowired
	private RoleDAO objRoleDAO;

	@Autowired
	private PrivilegeDAO objPrivilegeDAO;
	
	@Autowired
	private WebsocketService objWebsocketService;
	
	@Autowired
	private Utility utility;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ResponceWithMessage AddRole(String roleName, String roleDescription, String privilegeNames, ApplicationUsers loggedInUser) {

		ResponceWithMessage obj = null;
		try {
		
			
			Role role = objRoleDAO.getRoleByRoleName(roleName);
			
			if (role != null) {
				obj = new ResponceWithMessage(false, "Role with this name already exist.");
				return obj;
			}
			else {
				int operationResult = 0;
				List<Privilege> privilegesToAdd = new ArrayList<>();

				if (privilegeNames.contains(",")) {
					String[] allPrivileges = privilegeNames.split(",");
					for (int i = 0; i < allPrivileges.length; i++) {
						String privilegeName = allPrivileges[i];
						
						Privilege privilege = objPrivilegeDAO.findByName(privilegeName);
						
						privilegesToAdd.add(privilege);
					}
					
//					Role roleToAdd=new Role(roleName, roleDescription, privilegesToAdd);
					Role roleToAdd=new Role();
					roleToAdd.setRoleName(roleName);
					roleToAdd.setRoleDescription(roleDescription);
					roleToAdd.setPrivileges(privilegesToAdd);
					roleToAdd.setCreatedBy(loggedInUser);
					
					operationResult = objRoleDAO.addRole(roleToAdd);
				}else {
					Privilege privilege = objPrivilegeDAO.findByName(privilegeNames);
					
					Role roleToAdd=new Role(roleName, roleDescription, Arrays.asList(privilege));
				
					
					operationResult = objRoleDAO.addRole(roleToAdd);
				}
			

				if (operationResult > 0) {
					obj = new ResponceWithMessage(true, "New role added successfully.");
					objWebsocketService.createUpdateString(new NewTableUpdate("refreshTable", "", "Role Management", "", new ArrayList()));
				} else {
					obj = new ResponceWithMessage(false, "Error while adding role");
				}
			}
			

		} catch (Exception e) {
			LOG.error("Error", e);
		}
		return obj;
	}

	public BasicResponce getAllRoles() {

		try {

			List<Role> allRoles = null;
			try {
				allRoles = objRoleDAO.getAllRoles();
				allRoles = allRoles.stream().distinct().collect(Collectors.toList());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				LOG.info("" + e);
			}

			List<String> Table_Headers_ROW = new ArrayList<>();
			Table_Headers_ROW.add("Count");
			Table_Headers_ROW.add("Role ID");
			Table_Headers_ROW.add("Role Name");
			Table_Headers_ROW.add("Role Description");
			Table_Headers_ROW.add("Permissions");
			
			Table_Headers_ROW.add("CreatedBy");
			Table_Headers_ROW.add("CreatedAt");

			Table_Headers_ROW.add("UpdatedBy");
			Table_Headers_ROW.add("UpdatedAt");
			
		

			if (allRoles == null) {
				ResponceWithMessage toReturn = new ResponceWithMessage(true, "No roles found");
				return toReturn;
			} else {
				List<List<String>> Table_Data_List = new ArrayList<>();
				int counter = 1;
				for (Role role : allRoles) {
					ArrayList<String> row = new ArrayList<String>();
					row.add(counter + "");
					counter++;
					row.add(role.getRoleId() + "");
					row.add(role.getRoleName());
					row.add(role.getRoleDescription());
					
					String privilegeString="";
					for (Privilege priv : role.getPrivileges()) {
							privilegeString=privilegeString+","+priv.getPrivilegeName();
					}
					row.add(privilegeString);
					
					
					if(role.getCreatedBy()!=null){
						row.add(role.getCreatedBy().getDisplayName());
					}else{
						row.add("");
					}
					row.add(role.getCreatedAt()+"");
					
					if(role.getUpdatedBy()!=null){
						row.add(role.getUpdatedBy().getDisplayName());
					}else{
						row.add("");
					}
					
					row.add(role.getUpdatedAt()+"");
					
					Table_Data_List.add(row);
				}

				ReponceForWebTableData toReturn = new ReponceForWebTableData(true, "", Table_Headers_ROW,
						Table_Data_List);
				return toReturn;

			}

		} catch (Exception ex) {
			ResponceWithMessage toReturn = new ResponceWithMessage(false, "UNKNOWN ERROR");
			return toReturn;

		}
	}
	
	public BasicResponce getAllRolesWithDTOResponse() {

		try {

			BasicResponce authResp=null;
			List<Role> fromDB = null;
			try {
				fromDB = objRoleDAO.getAllRoles();
				fromDB = fromDB.stream().distinct().collect(Collectors.toList());
			} catch (Exception e) {
				e.printStackTrace();
				LOG.info("" + e);
			}

			List<RoleResponseDTO> toSend= new ArrayList<RoleResponseDTO>();
			if(fromDB!=null && fromDB.size()>0){
				
				for(Role role:fromDB) {

					RoleResponseDTO toAdd=new RoleResponseDTO();
					toAdd.setId(role.getRoleId());
					toAdd.setRoleName(role.getRoleName());
					toAdd.setRoleDescription(role.getRoleDescription());
					
					
					List<PrivilegeResponseDTO> toSendPrivilegeResponseDTOList=new ArrayList<PrivilegeResponseDTO>();
					for (Privilege priv : role.getPrivileges()) {
						PrivilegeResponseDTO toAddPrivRespDTO=new PrivilegeResponseDTO();
						
						toAddPrivRespDTO.setPrivilegeId(priv.getPrivilegeId());
						toAddPrivRespDTO.setPrivilegeName(priv.getPrivilegeName());
						toAddPrivRespDTO.setPrivilegeDescription(priv.getPrivilegeDescription());
						
						if(priv.getCreatedAt()!=null)
							toAddPrivRespDTO.setCreatedAt(utility.getDateFromTimeStamp(priv.getCreatedAt().toString()));
						if(priv.getCreatedBy()!=null)
							toAddPrivRespDTO.setCreatedBy(new ApplicationUserResponseDTO(priv.getCreatedBy().getUserId(), priv.getCreatedBy().getDisplayName()));
						
						if(priv.getUpdatedAt()!=null)
							toAddPrivRespDTO.setUpdatedAt(utility.getDateFromTimeStamp(priv.getUpdatedAt().toString()));
						if(priv.getUpdatedBy()!=null)
							toAddPrivRespDTO.setUpdatedBy(new ApplicationUserResponseDTO(priv.getUpdatedBy().getUserId(), priv.getUpdatedBy().getDisplayName()));
						
						toSendPrivilegeResponseDTOList.add(toAddPrivRespDTO);
					}
					
					toAdd.setPrivilegeDTOList(toSendPrivilegeResponseDTOList);
					
					if(role.getCreatedAt()!=null)
						toAdd.setCreatedAt(utility.getDateFromTimeStamp(role.getCreatedAt().toString()));
					if(role.getCreatedBy()!=null)
						toAdd.setCreatedBy(new ApplicationUserResponseDTO(role.getCreatedBy().getUserId(), role.getCreatedBy().getDisplayName()));
					
					if(role.getUpdatedAt()!=null)
						toAdd.setUpdatedAt(utility.getDateFromTimeStamp(role.getUpdatedAt().toString()));
					if(role.getUpdatedBy()!=null)
						toAdd.setUpdatedBy(new ApplicationUserResponseDTO(role.getUpdatedBy().getUserId(), role.getUpdatedBy().getDisplayName()));

					toSend.add(toAdd);
				}
				
				authResp=new ListOfRoleResponseDTO(toSend);
				authResp.setRequested_Action(true);
				return authResp;
			}else{
				ResponceWithMessage toReturn = new ResponceWithMessage(false, "No Record Found");
				return toReturn;
			}
					
		} catch (Exception ex) {
			ResponceWithMessage toReturn = new ResponceWithMessage(false, "UNKNOWN ERROR");
			return toReturn;
		}
	}
	
	
	

	public BasicResponce UpdateRole(String oldRoleName, String newRoleName, String roleDescription,String privilegeNames, ApplicationUsers loggedInUser) {

		ResponceWithMessage obj = null;
		try {

			if (newRoleName != null && !newRoleName.isEmpty() && privilegeNames != null && !privilegeNames.isEmpty()) {

				if (!oldRoleName.equals(newRoleName)) {
					Role existingRole = objRoleDAO.getRoleByRoleName(newRoleName);
					
					if (existingRole != null) {
						obj = new ResponceWithMessage(false, "Role with this name already exist.");
						return obj;
					}
				}
				
				Role role = objRoleDAO.getRoleByRoleName(oldRoleName);
				role.setRoleName(newRoleName);
				role.setRoleDescription(roleDescription);
				role.setUpdatedBy(loggedInUser);
				
				
				List<Privilege> privilegesToAdd = new ArrayList<>();
				boolean updated = false;
				if (privilegeNames.contains(",")) {
					String[] allPrivileges = privilegeNames.split(",");
					for (int i = 0; i < allPrivileges.length; i++) {
						String privilegeName = allPrivileges[i];
						
						Privilege privilege = objPrivilegeDAO.findByName(privilegeName);
						
						privilegesToAdd.add(privilege);
					}
					role.setPrivileges(privilegesToAdd);
					
					updated = objRoleDAO.updateRole(role);
				}else {
					Privilege privilege = objPrivilegeDAO.findByName(privilegeNames);
					role.setPrivileges(Arrays.asList(privilege));
					
					updated = objRoleDAO.updateRole(role);
				}

				if (updated) {
					obj = new ResponceWithMessage(true, "Role Updated");

				} else {
					obj = new ResponceWithMessage(false, "Failed to Update Role.");
				}
			} else {
				obj = new ResponceWithMessage(false, "Missing Params");
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return obj;
	}

	public ResponceWithMessage UpdateRolePermissions(String roleName, String roleDescription,String privilegeNames, ApplicationUsers loggedInUser) {

		ResponceWithMessage obj = null;
		try {

			if (roleName != null && !roleName.isEmpty() && privilegeNames != null && !privilegeNames.isEmpty()) {

				Role role = objRoleDAO.getRoleByRoleName(roleName);
					
				//Validation
			if (role != null) {
				//GOod to go
			}else{
				obj = new ResponceWithMessage(false, "Role With This Name Not Found.");
				return obj;
			}
				
				
				role.setRoleName(roleName);
				role.setRoleDescription(roleDescription);
				role.setUpdatedBy(loggedInUser);
		
				
				List<Privilege> privilegesToAdd = new ArrayList<>();
				boolean updated = false;
				if (privilegeNames.contains(",")) {
					String[] allPrivileges = privilegeNames.split(",");
					for (int i = 0; i < allPrivileges.length; i++) {
						String privilegeName = allPrivileges[i];
						
						Privilege privilege = objPrivilegeDAO.findByName(privilegeName);
						
						privilegesToAdd.add(privilege);
					}
					role.setPrivileges(privilegesToAdd);
					updated = objRoleDAO.updateRole(role);
				}
				else {
					Privilege privilege = objPrivilegeDAO.findByName(privilegeNames);
					role.setPrivileges(Arrays.asList(privilege));
					
					updated = objRoleDAO.updateRole(role);
				}

				if (updated) {
					obj = new ResponceWithMessage(true, "Role Updated");

				} else {
					obj = new ResponceWithMessage(false, "Failed to Update Role.");
				}
			} else {
				obj = new ResponceWithMessage(false, "Missing Params");
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return obj;
	}
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public BasicResponce DeleteRole(String roleName) {

		ResponceWithMessage obj = null;
		try {

			if (roleName != null && roleName.equals("ROLE_ADMIN")) {
				obj = new ResponceWithMessage(false, "Cannot delete Admin Role");
				return obj;
			}
			Role role = objRoleDAO.getRoleByRoleName(roleName);
			Hibernate.initialize(role.getUsers());
			if (role != null && role.getUsers().size() > 0) {
				obj = new ResponceWithMessage(false, "Cannot delete role. It is associated with user(s).");
				return obj;
			}
			
 			int result = objRoleDAO.deleteRole(roleName);

			if (result == 1) {
				obj = new ResponceWithMessage(true, "Role Deleted");
				objWebsocketService.createUpdateString(new NewTableUpdate("refreshTable", "", "Role Management", "", new ArrayList()));
			} else {
				obj = new ResponceWithMessage(false, "Failed to delete Role.");
			}

		} catch (Exception e) {
			
			e.printStackTrace();
		}
		return obj;
	}
	
}
