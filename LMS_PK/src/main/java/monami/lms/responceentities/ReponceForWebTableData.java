package monami.lms.responceentities;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ReponceForWebTableData extends BasicResponce{
	String Message;
	List<String> Table_Headers;
	List<List<String>> Table_Data;
	
	public ReponceForWebTableData(){
		super();
	}
	
	public ReponceForWebTableData(boolean rq,String message, List<String> table_Headers, List<List<String>> table_Data) {
		super(rq);
		Message = message;
		Table_Headers = table_Headers;
		Table_Data = table_Data;
	}

	public String getMessage() {
		return Message;
	}

	public void setMessage(String message) {
		Message = message;
	}
	@JsonProperty("Table_Headers")
	public List<String> getTable_Headers() {
		return Table_Headers;
	}

	public void setTable_Headers(List<String> table_Headers) {
		Table_Headers = table_Headers;
	}
	@JsonProperty("Table_Data")
	public List<List<String>> getTable_Data() {
		return Table_Data;
	}

	public void setTable_Data(List<List<String>> table_Data) {
		Table_Data = table_Data;
	}
	

	
	
	
}
