package monami.lms.dataentities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class ServerConfigurations implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1957922850482746003L;
	
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int serverConfigId;
	
	
	
	public ServerConfigurations() {
	}
	
	


	public int getServerConfigId() {
		return serverConfigId;
	}
	public void setServerConfigId(int serverConfigId) {
		this.serverConfigId = serverConfigId;
	}
	
	
	
	
}
