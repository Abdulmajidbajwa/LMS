package monami.lms.serverutils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import monami.lms.webclientrestcontollers.WebClientRestContollerAndroidApplication;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class SMSandNotficationService {
	Logger LOG = LoggerFactory.getLogger(WebClientRestContollerAndroidApplication.class);
	
	public  String sendFCMMessage(String to,String title,String messagetosend) {
		try {

			final String apiKey="AAAAHc6y1zQ:APA91bFaSzHUr1HlRw1Z3RRUCp5c0SxlETa73ycKm-y7S8vjBUp1ITeVdTDxVX2K0hIYE2tyudj1v3MXT1EsWSkwnfXd62YBm2-yhhgcJIgA1jyEBDt6lxyfUVjycKLxt5mBUxw0HXBx";
			URL url = new URL("https://fcm.googleapis.com/fcm/send");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Authorization", "key=" + apiKey);
			conn.setDoOutput(true);
			JSONObject message = new JSONObject();
			message.put("to", to);
			message.put("priority", "high");

			JSONObject other = new JSONObject();			
			other.put("body", messagetosend);
			other.put("title", title);
			other.put("click_action", "FCM_PLUGIN_ACTIVITY");
			
			message.put("notification", other);
			
			
			OutputStream os = conn.getOutputStream();
			os.write(message.toString().getBytes());
			os.flush();
			os.close();

			int responseCode = conn.getResponseCode();
			System.out.println("\nSending 'POST' request to URL : " + url);
			System.out.println("Post parameters : " + message.toString());
			System.out.println("Response Code : " + responseCode);
			System.out.println("Response Code : " + conn.getResponseMessage());

			BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			// print result
			System.out.println(response.toString());
			return response.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "error";
	}
	public static void main(String args[]) throws UnsupportedEncodingException {
		//sendFCMMessage("dKcrq9bj_sw:APA91bGLIZaI3ZIIb9ZAy0cDSPevbdkSuPlt6jG4g0-6P2-DatDSJkSTkwPs6goRrXpzvNV6byE6FIFgRuZksKAQ-hpXkywDvjX8UHvKu5_qlJ_P9of5uQotPCP-_8wGhYjU09jnnehm","MONAMI","Your loan application was approved.");
		//sendSMSAPI("03238867429","<#> Your ExampleApp code is: 1238 \r\n AWe2tStmnaR");
	}
	public String sendSMSAPI(String to,String message) throws UnsupportedEncodingException{
		
		if(LOG.isInfoEnabled()){
			LOG.info("WebClientRestContollerAndroidApplication.RegisterNewCustomer--Start");
			LOG.info("Received Parameters are:");
			LOG.info("To: {} ",new Object[]{to});
			LOG.info("Message: {} ",new Object[]{message});
		}

		final String hostname="test99095api.eocean.us";
		final String port="24555";
		final String userID="test_99095";
		final String password="Tt845162";
		final String sender="99095";

		/*Map<String, String> params = new HashMap<String, String>();
		params.put("username", userID);
		params.put("password", password);
		params.put("recipient", to);
		params.put("originator", sender);
		params.put("messagedata", message);

		String uri="http://"+hostname+":"+port+"/api?action=sendmessage&username={username}&password={password}&"
				+ "recipient={recipient}&originator={originator}&messagedata={messagedata}";


		RestTemplate restTemplate = new RestTemplate();
		String result=restTemplate.getForObject(uri, 
		    String.class, params);*/



		//http://test99095api.eocean.us:24555/api?action=sendmessage&username=test_99095&password=DSCXVFRMN&recipient=923472980857&originator=99095&messagedata=Test123.
//		message = URLEncoder.encode(message, "UTF-8");
		
		String uriw="http://"+hostname+":"+port+"/api?action=sendmessage&username="+userID+"&password="+password+"&recipient="+to+"&originator="+sender+"&messagedata="+message;

		//URI uri = URI.create(uriw);
		RestTemplate restTemplate = new RestTemplate();
		String result = restTemplate.getForObject(uriw, String.class);	

		return result;
	}

}
