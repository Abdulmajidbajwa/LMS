package monami.lms.utilities;

import org.springframework.stereotype.Component;

@Component
public class Utility {

	public String getDateFromTimeStamp(String timeStamp){
		return timeStamp.substring(0, timeStamp.indexOf(" "));
	}
}
