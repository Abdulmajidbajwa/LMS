package monami.lms.socketmessages;

import com.fasterxml.jackson.annotation.JsonProperty;

public class NewNotificationAlert extends SocketMessage {
	String order;
	String taskDescription;
	String taskResult;
	String taskResultDescription;
	String notificationAddTime;
	
	
	
	
	public NewNotificationAlert(String taskDescription, String taskResult, String taskResultDescription) {
		super("notificationTable");
		this.order = "";
		this.taskDescription = taskDescription;
		this.taskResult = taskResult;
		this.taskResultDescription = taskResultDescription;
	}
	@JsonProperty("order")
	public String getOrder() {
		return order;
	}
	public void setOrder(String order) {
		this.order = order;
	}
	@JsonProperty("taskDescription")
	public String getTaskDescription() {
		return taskDescription;
	}
	public void setTaskDescription(String taskDescription) {
		this.taskDescription = taskDescription;
	}
	@JsonProperty("taskResult")
	public String getTaskResult() {
		return taskResult;
	}
	public void setTaskResult(String taskResult) {
		this.taskResult = taskResult;
	}
	@JsonProperty("taskResultDescription")
	public String getTaskResultDescription() {
		return taskResultDescription;
	}
	public void setTaskResultDescription(String taskResultDescription) {
		this.taskResultDescription = taskResultDescription;
	}
	@JsonProperty("notificationAddTime")
	public String getNotificationAddTime() {
		return notificationAddTime;
	}
	public void setNotificationAddTime(String notificationAddTime) {
		this.notificationAddTime = notificationAddTime;
	}
	
	
}
