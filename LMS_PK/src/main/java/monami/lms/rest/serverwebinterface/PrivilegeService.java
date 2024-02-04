package monami.lms.rest.serverwebinterface;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import monami.lms.datadaos.PrivilegeDAO;
import monami.lms.dataentities.ApplicationUsers;
import monami.lms.dataentities.Privilege;
import monami.lms.responceentities.BasicResponce;
import monami.lms.responceentities.ReponceForWebTableData;
import monami.lms.responceentities.ResponceWithMessage;
import monami.lms.response.datadtos.ApplicationUserResponseDTO;
import monami.lms.response.datadtos.ListOfPrivilegeResponseDTO;
import monami.lms.response.datadtos.PrivilegeResponseDTO;
import monami.lms.utilities.Utility;

@Service
public class PrivilegeService {
	
	@Autowired
	private PrivilegeDAO objPrivilegeDAO;

	//TODO: This Variable has zero Reference	
	/*@Autowired 
	RoleDAO objRoleDAO;*/
	
	@Autowired
	private Utility utility;
	
	public ResponceWithMessage AddPrivilege(String privilegeName,ApplicationUsers loggedInUser) {

		ResponceWithMessage obj = null;
		try {
			
			Privilege privilege = new Privilege();
			privilege.setPrivilegeName(privilegeName);
			privilege.setCreatedBy(loggedInUser);
			
			int operationResult = objPrivilegeDAO.addPrivilege(privilege);

			if (operationResult > 0) {

				obj = new ResponceWithMessage(true, "New Privilege added successfully.");
			} else {
				obj = new ResponceWithMessage(false, "Error while adding Privilege");
			}

		} catch (Exception e) {
		}
		return obj;
	}
	
	public BasicResponce UpdatePrivilege(String oldPrivilegeName, String newPrivilegeName,ApplicationUsers loggedInUser) {

		ResponceWithMessage obj = null;
		try {

			if (oldPrivilegeName != null && !oldPrivilegeName.isEmpty() && newPrivilegeName != null && !newPrivilegeName.isEmpty()) {

				Privilege privilege = objPrivilegeDAO.findByName(oldPrivilegeName);
				
				privilege.setPrivilegeName(newPrivilegeName);
				privilege.setUpdatedBy(loggedInUser);

				boolean updated = objPrivilegeDAO.updatePrivilege(privilege);

				if (updated) {
					obj = new ResponceWithMessage(true, "Privilege Updated");

				} else {
					obj = new ResponceWithMessage(false, "Failed to Update Privilege.");
				}
			} else {
				obj = new ResponceWithMessage(false, "Missing Params");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return obj;
	}
	
	public BasicResponce DeletePrivilege(String privilegeName) {

		ResponceWithMessage obj = null;
		try {

			int result = objPrivilegeDAO.deletePrivilege(privilegeName);

			if (result == 1) {
				obj = new ResponceWithMessage(true, "Privilege Deleted");
			} else {
				obj = new ResponceWithMessage(false, "Failed to delete Privilege.");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return obj;
	}
	
	public BasicResponce getAllPrivileges() {

		try {

			List<Privilege> allPrivileges = null;
			try {
				allPrivileges = objPrivilegeDAO.getAllPrivileges();
			} catch (Exception e) {
				e.printStackTrace();
			}

			List<String> Table_Headers_ROW = new ArrayList<>();
			Table_Headers_ROW.add("Count");
			Table_Headers_ROW.add("Privilege ID");
			Table_Headers_ROW.add("Privilege Name");
			Table_Headers_ROW.add("Privilege Type");
			Table_Headers_ROW.add("Privilege Description");
			
			Table_Headers_ROW.add("CreatedBy");
			Table_Headers_ROW.add("CreatedAt");

			Table_Headers_ROW.add("UpdatedBy");
			Table_Headers_ROW.add("UpdatedAt");
			

			if (allPrivileges == null) {
				ResponceWithMessage toReturn = new ResponceWithMessage(true, "No Privileges found");
				return toReturn;
			} else {
				List<List<String>> Table_Data_List = new ArrayList<>();
				int counter = 1;
				for (Privilege privilege: allPrivileges) {
					ArrayList<String> row = new ArrayList<String>();
					row.add(counter + "");
					counter++;
					row.add(privilege.getPrivilegeId() + "");
					row.add(privilege.getPrivilegeName());
					row.add(privilege.getPrivilegeDescription());
					
					if(privilege.getCreatedBy()!=null){
						row.add(privilege.getCreatedBy().getDisplayName());
					}else{
						row.add("");
					}
					row.add(privilege.getCreatedAt()+"");
					
					if(privilege.getUpdatedBy()!=null){
						row.add(privilege.getUpdatedBy().getDisplayName());
					}else{
						row.add("");
					}
					row.add(privilege.getUpdatedAt()+"");
					
					Table_Data_List.add(row);
				}

				ReponceForWebTableData toReturn = new ReponceForWebTableData(true, "", Table_Headers_ROW,Table_Data_List);
				return toReturn;

			}

		} catch (Exception ex) {
			ResponceWithMessage toReturn = new ResponceWithMessage(false, ex.getMessage());
			return toReturn;

		}
	}
	
	public BasicResponce getAllPrivilegesWithDTOResponse() {

		try {
			BasicResponce authResp=null;
			List<Privilege> fromDB = null;
			try {
				fromDB = objPrivilegeDAO.getAllPrivileges();
			} catch (Exception e) {
				e.printStackTrace();
			}

			List<PrivilegeResponseDTO> toSend= new ArrayList<PrivilegeResponseDTO>();
			
			if(fromDB!=null && fromDB.size()>0){
				
				for(Privilege i:fromDB) {

					PrivilegeResponseDTO toAdd=new PrivilegeResponseDTO();
					toAdd.setPrivilegeId(i.getPrivilegeId());
					toAdd.setPrivilegeName(i.getPrivilegeName());
					toAdd.setPrivilegeDescription(i.getPrivilegeDescription());
					
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
				
				authResp=new ListOfPrivilegeResponseDTO(toSend);
				authResp.setRequested_Action(true);
				return authResp;
			}else{
				ResponceWithMessage toReturn = new ResponceWithMessage(false, "No Record Found");
				return toReturn;
			}
		} catch (Exception ex) {
			ResponceWithMessage toReturn = new ResponceWithMessage(false, ex.getMessage());
			return toReturn;
		}
	}
	
}
