package monami.lms.socketmessages;

import com.fasterxml.jackson.annotation.JsonProperty;

abstract public class SocketMessage {
	String updateType;
	
	

	public SocketMessage(String updateType) {
		super();
		this.updateType = updateType;
	}
	@JsonProperty("updateType")
	public String getUpdateType() {
		return updateType;
	}

	public void setUpdateType(String updateType) {
		this.updateType = updateType;
	}
	
	
}
