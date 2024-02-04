package monami.lms.responceentities;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BasicResponce {
	private boolean Requested_Action;
	
	public BasicResponce(){
	}

	public BasicResponce(boolean requested_Action) {
		super();
		Requested_Action = requested_Action;
	}
	@JsonProperty("Requested_Action")
	public boolean getRequested_Action() {
		return Requested_Action;
	}

	public void setRequested_Action(boolean requested_Action) {
		Requested_Action = requested_Action;
	}
	
	
	
	
}
