package monami.lms.socketmessages;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonProperty;

public class NewTableUpdate extends SocketMessage{
	
	
	 // thinClientsTable table to update
	private String typesName;   // Default Group id(selection critaria) of the row to update // tableNametoupdate
	private String changeOn;     // MacAddress 
	private String changeOnValue;
	private ArrayList<String> rowData;		  // Data For Tc table
	public NewTableUpdate(String updateType, String typesName, String changeOn, String changeOnValue, ArrayList<String> rowData) {
		super(updateType);
		this.typesName = typesName;
		this.changeOn = changeOn;
		this.changeOnValue = changeOnValue;
		this.rowData = rowData;
	}
	@JsonProperty("typesName")
	public String getTypesName() {
		return typesName;
	}
	public void setTypesName(String typesName) {
		this.typesName = typesName;
	}
	@JsonProperty("changeOn")
	public String getChangeOn() {
		return changeOn;
	}
	public void setChangeOn(String changeOn) {
		this.changeOn = changeOn;
	}
	@JsonProperty("changeOnValue")
	public String getChangeOnValue() {
		return changeOnValue;
	}
	public void setChangeOnValue(String changeOnValue) {
		this.changeOnValue = changeOnValue;
	}
	@JsonProperty("rowData")
	public ArrayList<String> getRowData() {
		return rowData;
	}
	public void setRowData(ArrayList<String> rowData) {
		this.rowData = rowData;
	}
	
	
	
	
	

}
