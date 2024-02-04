package monami.lms.rest.serverwebinterface;

import org.springframework.stereotype.Service;
import monami.lms.responceentities.BasicResponce;
import monami.lms.responceentities.ResponceWithMessage;

@Service
public class AliveServerService {
	
	
	public BasicResponce isServerReadyAndAlive(){
		BasicResponce toReturn=new BasicResponce(true);
		
		return toReturn;
	} 
	
	public ResponceWithMessage isServerReadyAndAlive2(){
		ResponceWithMessage toReturn=new ResponceWithMessage(true,"IAMAVAILABLE");
		
		return toReturn;
	} 
	
	
	
}
