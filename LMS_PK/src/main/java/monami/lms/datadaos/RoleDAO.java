package monami.lms.datadaos;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import monami.lms.dataentities.Role;


@Repository
@Scope("prototype")
public class RoleDAO {

	@Autowired DAO obj;
	
	public int addRole(Role role) throws Exception{
		synchronized (DAO.class){ try {
			obj.startTask();
			return obj.saveObject(role);
		} catch (Exception e) {
			return obj.catchException(e);
		}finally{
			obj.endTask();
		}}
	}
	
	
	public Role getRoleByRoleName(String rolename) throws Exception{
		synchronized (DAO.class){ try {
			obj.startTask();
			return (Role)obj.getRowByAttribute(Role.class, "roleName", rolename);
		} catch (Exception e) {
			throw e;
		}finally{
			obj.endTask();
		}}
	}
	
	public Role getRoleById(int id) throws Exception{
		try {
			obj.startTask();
			return (Role)obj.getObjectById(Role.class, id);
		} catch (Exception e) {
			throw e;
		}finally{
			obj.endTask();
		}
	}
	
	public void persistRole() throws Exception{
		try {
			obj.persistObject(Role.class);;
		} catch (Exception e) {
			throw e;
		}finally{
			obj.endTask();
		}
	}
	
	public boolean updateRole(Role role) throws Exception{
		try {
			obj.startTask();
			return obj.updateObject(role);
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}finally{
			obj.endTask();
		}
	}
	
	public int deleteRole(String roleName){
		try {
			obj.startTask();
			Role role = (Role)obj.getRowByAttribute(Role.class, "roleName", roleName);
			if(role != null){
				return (obj.deleteObject(role)) ? 1 : -1;
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
	public List<Role> getAllRoles() throws Exception{
		try {
			obj.startTask();

			List<Role> toReturn=obj.getAll(Role.class);
			
			Collections.sort(toReturn);
			
			return toReturn;
		}catch(Exception e){
			e.printStackTrace();
			throw e;
		}finally{
			obj.endTask();

		}}
	
	public void saveOrUpdateRole(Role role) throws Exception{
		synchronized (DAO.class){ try {
			obj.startTask();
			obj.saveOrUpdateObject(role);
		} catch (Exception e) {
			obj.catchException(e);
		}finally{
			obj.endTask();
		}}
	}
}
