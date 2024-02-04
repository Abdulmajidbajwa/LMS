package monami.lms.rest.serverwebinterface;

import java.util.HashSet;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Service;

@SuppressWarnings("unused")
@Service
public class UserSessionManager {
	private static HashSet<String> sessionStore= new HashSet<String>();
	
	public void addSession(String uuid){
		sessionStore.add(uuid);
	}
	public boolean isValidSession(String uuid){
		if(sessionStore.contains(uuid)){
			return true;
		}
		return false;
	}
}
