package monami.lms.datadaos;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import monami.lms.dataentities.Privilege;

@Repository
@Scope("prototype")
public class PrivilegeDAO {

	@Autowired DAO obj;
	
	public int addPrivilege(Privilege privilege) throws Exception{
		synchronized (DAO.class){ try {
			obj.startTask();
			return obj.saveObject(privilege);
		} catch (Exception e) {
			return obj.catchException(e);
		}finally{
			obj.endTask();
		}}
	}

	public Privilege findByName(String privilegeName) throws Exception {
		synchronized (DAO.class){ try {
			obj.startTask();
			return (Privilege)obj.getRowByAttribute(Privilege.class, "privilegeName", privilegeName);
		} catch (Exception e) {
			throw e;
		}finally{
			obj.endTask();
		}}
	}
	
	
	
	public boolean updatePrivilege(Privilege privilege) throws Exception{
		try {
			obj.startTask();
			return obj.updateObject(privilege);
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}finally{
			obj.endTask();
		}
	}
	
	public int deletePrivilege(String privilegeName){
		try {
			obj.startTask();
			Privilege privilege = (Privilege)obj.getRowByAttribute(Privilege.class, "privilegeName", privilegeName);
			if(privilege != null){
				return (obj.deleteObject(privilege)) ? 1 : -1;
			}
			return -1;
		}catch(Exception e){
			e.printStackTrace();
			return -1;
		}finally{

			try{
				obj.endTask();
			}catch(Exception e){
				return obj.catchException(e);
			}

		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Privilege> getAllPrivileges() throws Exception{
		try {
			obj.startTask();

			List<Privilege> toReturn=obj.getAll(Privilege.class);
			
			Collections.sort(toReturn);
			
			return toReturn;
		}catch(Exception e){
			e.printStackTrace();
			throw e;
		}finally{
			obj.endTask();

		}}
	
	/*@SuppressWarnings("unchecked")
	public List<String> getAllPrivilegeTypes() throws Exception{
		try {
			obj.startTask();

			List<String> toReturn=obj.getAllByGroup(Privilege.class, "privilegeType");
			return toReturn;
		}catch(Exception e){
			e.printStackTrace();
			throw e;
		}finally{
			obj.endTask();

		}}*/
	
	public Privilege saveOrUpdatePrivilege(String privilegeName, String privilegeDescription) throws Exception{
		synchronized (DAO.class){ try {
			obj.startTask();
			Privilege privilege = (Privilege)obj.getRowByAttribute(Privilege.class, "privilegeName", privilegeName);
			if (privilege == null) {
				privilege = new Privilege();
				privilege.setPrivilegeName(privilegeName);
			}
			privilege.setPrivilegeDescription(privilegeDescription);
			
			obj.saveOrUpdateObject(privilege);
			return privilege;
		} catch (Exception e) {
			obj.catchException(e);
		}finally{
			obj.endTask();
		}}
		return null;
	}
}
