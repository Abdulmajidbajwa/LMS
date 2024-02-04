package monami.lms.responceentities;

import java.io.PrintWriter;
import java.io.StringWriter;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ResponceWithMessage extends BasicResponce{
	String Message;

	public ResponceWithMessage(){

	}

	public ResponceWithMessage(boolean requestAction, String message) {
		super(requestAction);
		this.Message=message;
		// TODO Auto-generated constructor stub
	}
	
	public ResponceWithMessage(Exception ex) {
		super(false);
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		ex.printStackTrace(pw);
		String message = sw.toString(); // stack trace as a string
		System.out.println(message);
		
		this.Message=message;
		// TODO Auto-generated constructor stub
	}
	@JsonProperty("Message")
	public String getMessage() {
		return Message;
	}

	public void setMessage(String message) {
		Message = message;
	}

	



}
