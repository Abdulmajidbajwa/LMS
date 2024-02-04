package monami.lms.socketmessages;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class NewDashboardUpdate extends SocketMessage {
	
	Vector<Map<String, String>> deviceAlertStatus;
	String pollsReceived;
	String totalMachines;
	String linuxMachineCount;
	String highestUsedImage;
	String highestUsedImageCount;
	String secondHighestUsedImage;
	String secondHighestUsedImageCount;
	
	String totalOnlineMachines;
	String totalSchedule;
	String pendingSchedule;
	String pendingImages;
	List<String> tableHeader; //Table_Headers
	LinkedHashMap<String,String> serverStats;
	String noOfRunningTasks;
	List<List<String>> runninTasksRows;
	
	
	
	
	
	public NewDashboardUpdate(){
		super("dashboardData");
	}







	public String getNoOfRunningTasks() {
		return noOfRunningTasks;
	}







	public void setNoOfRunningTasks(String noOfRunningTasks) {
		this.noOfRunningTasks = noOfRunningTasks;
	}







	public List<List<String>> getRunninTasksRows() {
		return runninTasksRows;
	}







	public void setRunninTasksRows(List<List<String>> runninTasksRows) {
		this.runninTasksRows = runninTasksRows;
	}













	public List<String> getTableHeader() {
		return tableHeader;
	}



	public void setTableHeader(List<String> tableHeader) {
		this.tableHeader = tableHeader;
	}



	public String getTotalOnlineMachines() {
		return totalOnlineMachines;
	}



	public void setTotalOnlineMachines(String totalOnlineMachines) {
		this.totalOnlineMachines = totalOnlineMachines;
	}



	public String getTotalSchedule() {
		return totalSchedule;
	}



	public void setTotalSchedule(String totalSchedule) {
		this.totalSchedule = totalSchedule;
	}



	public String getPendingSchedule() {
		return pendingSchedule;
	}



	public void setPendingSchedule(String pendingSchedule) {
		this.pendingSchedule = pendingSchedule;
	}



	public String getPendingImages() {
		return pendingImages;
	}



	public void setPendingImages(String pendingImages) {
		this.pendingImages = pendingImages;
	}



	



	public LinkedHashMap<String, String> getServerStats() {
		return serverStats;
	}



	public void setServerStats(LinkedHashMap<String, String> serverStats) {
		this.serverStats = serverStats;
	}



	public String getHighestUsedImage() {
		return highestUsedImage;
	}



	public void setHighestUsedImage(String highestUsedImage) {
		this.highestUsedImage = highestUsedImage;
	}



	public String getHighestUsedImageCount() {
		return highestUsedImageCount;
	}



	public void setHighestUsedImageCount(String highestUsedImageCount) {
		this.highestUsedImageCount = highestUsedImageCount;
	}



	public String getSecondHighestUsedImage() {
		return secondHighestUsedImage;
	}



	public void setSecondHighestUsedImage(String secondHighestUsedImage) {
		this.secondHighestUsedImage = secondHighestUsedImage;
	}



	public String getSecondHighestUsedImageCount() {
		return secondHighestUsedImageCount;
	}



	public void setSecondHighestUsedImageCount(String secondHighestUsedImageCount) {
		this.secondHighestUsedImageCount = secondHighestUsedImageCount;
	}













	public Vector<Map<String, String>> getDeviceAlertStatus() {
		return deviceAlertStatus;
	}







	public void setDeviceAlertStatus(Vector<Map<String, String>> deviceAlertStatus) {
		this.deviceAlertStatus = deviceAlertStatus;
	}







	public String getPollsReceived() {
		return pollsReceived;
	}



	public void setPollsReceived(String pollsReceived) {
		this.pollsReceived = pollsReceived;
	}



	public String getTotalMachines() {
		return totalMachines;
	}



	public void setTotalMachines(String totalMachines) {
		this.totalMachines = totalMachines;
	}



	public String getLinuxMachineCount() {
		return linuxMachineCount;
	}



	public void setLinuxMachineCount(String linuxMachineCount) {
		this.linuxMachineCount = linuxMachineCount;
	}
	
	
	

}
