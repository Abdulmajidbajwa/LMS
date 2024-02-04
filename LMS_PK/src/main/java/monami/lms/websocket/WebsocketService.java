package monami.lms.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import monami.lms.serverutils.ServerUtils;
import monami.lms.socketmessages.NewDashboardUpdate;
import monami.lms.socketmessages.NewNotificationAlert;
import monami.lms.socketmessages.NewTableUpdate;

@Service
public class WebsocketService {
	@Autowired
	private WebSocketController objWebSocketController;

	@Autowired ServerUtils objServerUtils;
	static Logger logger = LoggerFactory.getLogger(WebsocketService.class);
	
	
	public boolean sendDashboardUpdate(NewDashboardUpdate toSend){
		
		objWebSocketController.sendDataToBrowser(toSend);
		return true;
		
	}
	
	public boolean createUpdateString(NewTableUpdate toSend){
		
		objWebSocketController.sendDataToBrowser(toSend);
		return true;
		
	}
	
	public boolean sendAndSaveNotification(NewNotificationAlert toSend){	
		try {			
			
			  
			objWebSocketController.sendDataToBrowser(toSend);
			return true;
		} catch (Exception ex){
			logger.error("Error",ex);
		}
		return false;
		
		
			
		
	}
	
	
	
}
