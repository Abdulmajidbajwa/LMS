package monami.lms.datadaos;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import monami.lms.dataentities.ApplicationUsers;

@Repository
@Scope("prototype")
public class ApplicationUserDAO  {
	
	
	@Autowired
	private ApplicationContext context;

	public int addUser(ApplicationUsers user) throws Exception {
		DAO obj = context.getBean(DAO.class);
		try {
			obj.startTask();
			return obj.saveObject(user);
		} catch (Exception e) {
			return obj.catchException(e);
		} finally {
			obj.endTask();
		}
	}
	

	public boolean updateUser(ApplicationUsers user) throws Exception {
		DAO obj = context.getBean(DAO.class);
		try {
			obj.startTask();
			obj.updateObject(user);
			return true;
		} catch (Exception e) {
			throw e;
		} finally {
			obj.endTask();
		}
	}
	
	public ApplicationUsers getUserById(int id) throws Exception {
		DAO obj = context.getBean(DAO.class);
		try {
			obj.startTask();
			return (ApplicationUsers) obj.getObjectById(ApplicationUsers.class, id);
		} catch (Exception e) {
			throw e;
		} finally {
			obj.endTask();
		}
	}
	
	
	public ApplicationUsers getUserByUsername(String username) throws Exception {
		DAO obj = context.getBean(DAO.class);
		try {
			obj.startTask();
			return (ApplicationUsers) obj.getRowByAttribute(ApplicationUsers.class, "username", username);
		} catch (Exception e) {
			throw e;
		} finally {
			obj.endTask();
		}
	}
	
	public String getAssignToken(String username) throws Exception {
		DAO obj = context.getBean(DAO.class);
		try {
			obj.startTask();
			return obj.getColumnByAttribute(ApplicationUsers.class, "username", username).toString();
		} catch (Exception e) {
			throw e;
		} finally {
			obj.endTask();
		}
	}
	
	public ApplicationUsers getUserAgainstToken(String token) throws Exception {
		DAO obj = context.getBean(DAO.class);
		try {
			obj.startTask();
			@SuppressWarnings("unchecked")
			List<ApplicationUsers> list =  obj.getAllOnCriteria(ApplicationUsers.class, "assignToken", token);
			if (list != null && list.size() > 0) {
				return (ApplicationUsers) list.get(0);
			}
			return null;
		} catch (Exception e) {
			throw e;
		} finally {
			obj.endTask();
		}
	}
	
	@SuppressWarnings("unchecked")
	public ApplicationUsers getUserByUsernamePassword(String username, String password) throws Exception {
		DAO obj = context.getBean(DAO.class);
		try {
			obj.startTask();
			List<ApplicationUsers> list = obj.getAllOnCriteria(ApplicationUsers.class, "username", username, "password",
					password);

			if (list != null && list.size() > 0) {
				return (ApplicationUsers) list.get(0);
			}
			return null;
		} catch (Exception e) {
			throw e;
		} finally {
			obj.endTask();
		}
	}
	
	public boolean deleteUser(String userNameToDelete) throws Exception {
		DAO obj = context.getBean(DAO.class);
		try {
			obj.startTask();
			ApplicationUsers applicationUser = (ApplicationUsers) obj.getRowByAttribute(ApplicationUsers.class, "username", userNameToDelete);
			obj.deleteObject(applicationUser);
			return true;
		} catch (Exception e) {
			return false;
		} finally {
			obj.endTask();
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<ApplicationUsers> getAllUsers() throws Exception {
		DAO obj = context.getBean(DAO.class);
		try {
			obj.startTask();

			List<ApplicationUsers> toReturn = obj.getAll(ApplicationUsers.class);
			
			Collections.sort(toReturn);
			
			return toReturn;

		} catch (Exception e) {
			obj.catchException(e);
			throw e;
		} finally {
			obj.endTask();
		}

	}

	




	
}
