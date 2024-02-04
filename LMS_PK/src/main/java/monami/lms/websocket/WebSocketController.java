package monami.lms.websocket;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import monami.lms.responceentities.ResponceWithMessage;
import monami.lms.socketmessages.SocketMessage;


@Controller
public class WebSocketController {
	Logger logger = LoggerFactory.getLogger(WebSocketController.class);
	@Autowired
	private SimpMessageSendingOperations messagingTemplate;
	
	@SuppressWarnings("unused")
	@EventListener
	public void handleWebSocketConnectListener(SessionConnectedEvent event) {
		StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
		//String username = (String) headerAccessor.getSessionAttributes().get("username");
		//logger.info("Received a new web socket connection");
	}

	@SuppressWarnings("unused")
	@EventListener
	public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
		StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage()); 
		//String username = (String) headerAccessor.getSessionAttributes().get("username");         
		//if(username != null) {
			//logger.info(" web socket connection Disconnected : " ); 
			//ResponceWithMessage chatMessage = new ResponceWithMessage("True","sdansdlnaskndasnkd"); 
			//messagingTemplate.convertAndSend("/topic/publicChatRoom", chatMessage);
		//}
	}
	
	//@Scheduled(fixedRate = 5000)
	public void sendDataToBrowser(SocketMessage objSocketMessage) {
		//logger.info("New Message To Be Send Over Socket Towards Browser");
		
		//ResponceWithMessage tk=new ResponceWithMessage("False","Generate by "+randomNumberInRange(10,100));
		try {
			messagingTemplate.convertAndSend("/topic/publicChatRoom", objSocketMessage);
		} catch (Exception ex){
			logger.error("Error While Sending Data Over WebSocket",ex);
			
		}
		
	}

	@MessageMapping("/chat.sendMessage")
	@SendTo("/topic/publicChatRoom")
	public ResponceWithMessage getMessageFromBrowser(@Payload ResponceWithMessage chatMessage) {
		//logger.info("getMessageFromBrowser getMessageFromBrowser");
		
		ResponceWithMessage tk=new ResponceWithMessage(false,"Responce To anyMessage");
		return tk;
	}

	@MessageMapping("/chat.addUser")
	@SendTo("/topic/publicChatRoom")
	public ResponceWithMessage addUser(@Payload ResponceWithMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {
		// Add username in web socket session
		//logger.info("addUser addUser");
		//headerAccessor.getSessionAttributes().put("clientCounter", ++clientCounter);
		ResponceWithMessage tk=new ResponceWithMessage(true,"Responce To addUser");
		return tk;
	}
	
	public static int randomNumberInRange(int min, int max) {
        Random random = new Random();
        return random.nextInt((max - min) + 1) + min;
    }


}
