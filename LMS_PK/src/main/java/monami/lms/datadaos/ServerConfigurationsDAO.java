package monami.lms.datadaos;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import monami.lms.dataentities.ServerConfigurations;

@Repository
@Scope("prototype")
public class ServerConfigurationsDAO {

	
	@Autowired DAO obj;

	public int addServerConfigurations(ServerConfigurations serverConfigurations) throws Exception{
		synchronized (DAO.class){ try {
			obj.startTask();
			return obj.saveObject(serverConfigurations);
		} catch (Exception e) {
			return obj.catchException(e);
		}finally{
			obj.endTask();
		}}
	}
	
	public boolean updateServerConfigurations(ServerConfigurations serverConfigurations) throws Exception{
		synchronized (DAO.class){ try {
			obj.startTask();
			obj.updateObject(serverConfigurations);
			return true;
		} catch (Exception e) {
			throw e;
		}finally{
			obj.endTask();
		}}
	}
	

	@SuppressWarnings("unchecked")
	public List<ServerConfigurations> getAllServerConfigurations() throws Exception {
		
		 try {
			obj.startTask();

			List<ServerConfigurations> toReturn = 	obj.getAll(ServerConfigurations.class);
			return toReturn;

		} catch (Exception e) {
			obj.catchException(e);
			throw e;
		} finally {
			obj.endTask();
		}
		}
}
