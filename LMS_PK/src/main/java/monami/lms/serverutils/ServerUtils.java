package monami.lms.serverutils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.format.DateTimeFormatter;
import java.util.Enumeration;
import javax.annotation.PostConstruct;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ProtocolCommandListener;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.net.ftp.FTPSClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;






@Service
public class ServerUtils {
	private boolean preferIpv4 = true;
	public final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
	//public final String LOCAL_IMAGE_NAME = "LocalImage";
	public String TOTAL_SIZE;

	public String SERVERSSHBASELOCATION ="";
	public String SERVERSSHUSERNAME ="";
	public String SERVERSSHPASSWORD ="";
	String uploadLocation="/var/lib/jenkins/workspace/lmsdata";

	@Autowired
	private Environment environment;



	static Logger logger = LoggerFactory.getLogger(ServerUtils.class);
	@PostConstruct
	public void initialize() {
		SERVERSSHBASELOCATION=environment.getProperty("base.folder.location.for.java.uploads");
		SERVERSSHUSERNAME=environment.getProperty("username.for.base.folder.location.of.java.uploads");
		SERVERSSHPASSWORD=environment.getProperty("password.for.base.folder.location.of.java.uploads");
	}

	public String storeFile(MultipartFile file) {
		if(file==null) {
			return null;
		}	
		Path fileStorageLocation = Paths.get(uploadLocation).toAbsolutePath().normalize();

		try {
			Files.createDirectories(fileStorageLocation);
		} catch (Exception ex) {
			System.out.println("Could not create the directory where the uploaded files will be stored.");
		}


		String orignalFileName = StringUtils.cleanPath(file.getOriginalFilename());
		String ext = FilenameUtils.getExtension(orignalFileName);
		String randomFileName=generateRandomStringOfLength(30)+"."+ext;
		
		try {
			

			// Copy file to the target location (Replacing existing file with the same name)
			Path targetLocation = fileStorageLocation.resolve(randomFileName);
			Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

			return randomFileName;
		} catch (IOException ex) {
			System.out.println("Could not store file " + orignalFileName + " as "+randomFileName+". Please try again!");
		}
		
		return null;
	}
	
	public Resource getFileFromDisk(String fileName) {
        try {
        	Path fileStorageLocation = Paths.get(uploadLocation).toAbsolutePath().normalize();
            Path filePath = fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if(resource.exists()) {
                return resource;
            } else {
                return null;
            }
        } catch (MalformedURLException ex) {
        	  return null;
        }
    }
	
	public String fileToExposedURL(String file) {
		if(file==null || file.trim().isEmpty()) {
			return null;
		}
		return environment.getProperty("protocol.type")
				+"://"
				+environment.getProperty("app.world.ip")
				+":"
				+environment.getProperty("server.port")
				+"/getUploadFile/"
				+file;
	}

	public static boolean initComplete=false;





	public static String removePipeAndSpace(String input){
		input=input.replaceAll("\\|", ",");
		if(input.lastIndexOf(',') == input.length()-1){
			input = input.substring(0, input.length()-1);
		}
		input=input.replaceAll("-", "");
		input=input.trim();

		if(input.indexOf(",") == 0  ){
			input=input.replaceFirst(",", "" );
		}


		return input;
	}

	@SuppressWarnings("rawtypes")
	public String getFirstNonLoopbackAddress()  {
		Enumeration en;
		try {
			en = NetworkInterface.getNetworkInterfaces();
			while (en.hasMoreElements()) {
				NetworkInterface i = (NetworkInterface) en.nextElement();
				for (Enumeration en2 = i.getInetAddresses(); en2.hasMoreElements();) {
					InetAddress addr = (InetAddress) en2.nextElement();
					if (!addr.isLoopbackAddress()) {
						if (addr instanceof Inet4Address) {
							if (!preferIpv4) {
								continue;
							}
							return addr.toString();
						}
						if (addr instanceof Inet6Address) {
							if (preferIpv4) {
								continue;
							}
							return addr.toString();
						}
					}
				}
			}
		} catch (SocketException e) {

			e.printStackTrace();
		}

		return null;
	}

	public String getMacAddress(String ipAddr)    throws UnknownHostException, SocketException {
		InetAddress addr = InetAddress.getByName(ipAddr);
		NetworkInterface ni = NetworkInterface.getByInetAddress(addr);
		if (ni == null)
			return null;

		byte[] mac = ni.getHardwareAddress();
		if (mac == null)
			return null;

		StringBuilder sb = new StringBuilder(18);
		for (byte b : mac) {
			if (sb.length() > 0)
				sb.append(':');
			sb.append(String.format("%02x", b));
		}
		return sb.toString();
	}





	public static String convertString(String... ar) throws Exception{
		if(ar.length%2!=0)
			throw new Exception();

		StringBuilder b=new StringBuilder();

		for(int i=0;i<ar.length;i++){
			if(i==0){
				b.append(ar[i]+"=");
			} else if(i%2==0){
				b.append("&"+ar[i]+"=");
			} else {
				b.append( URLEncoder.encode(ar[i], "UTF-8") );
			}



		}

		return b.toString();

	}


	//	public static String getTotalNetworkConnectionSpeed() {
	//		String command1 = "wmic NIC where NetEnabled=true get Speed";
	//
	//		String output1 = null;
	//
	//		try {
	//			output1 = getCommandOutput(command1);
	//
	//		} catch (Exception e) {
	//			// TODO Auto-generated catch block
	//			e.printStackTrace();
	//		}
	//		if(output1!=null){
	//			String lines1[] = output1.split("\\r?\\n");
	//
	//			long toReturn = 0 ;
	//
	//			for(int i=0;i<lines1.length;i++){
	//				try {
	//					long temp1=Long.parseLong(lines1[i].trim());
	//
	//					toReturn=toReturn+(temp1/1000000);
	//
	//				} catch (Exception ex){
	//
	//				}
	//
	//			}
	//
	//			return (toReturn)+"";
	//		}else {
	//			return "N/A";
	//		}
	//
	//	}



	public static long getTotalDiskSpaceInGbUbuntu(){


		String command = "df -h | grep \"/dev/sd\" | awk '{print $2}'";
		String output = null;
		try {
			output = getCommandOutput(command);

			if(output!=null){
				long intASD=0;
				String lines[] = output.split("\\r?\\n");
				for(String iter:lines){
					if(iter.contains("G")){
						iter=iter.replace("G", "").trim();
						if(iter.contains(".")){
							intASD=(long) (intASD+Float.parseFloat(iter));
						} else {
							intASD=intASD+Integer.parseInt(iter);
						}

					}
				}


				return intASD;



			}else {
				return 0;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;


	}





	public static String getTotalCPU() {
		String command1 = "cat /proc/cpuinfo | grep \"cpu MHz\"";
		String command2 = "cat /proc/cpuinfo | grep processor | wc -l";
		String output1 = null;
		String output2 = null;
		try {
			output1 = getCommandOutput(command1);
			output2 = getCommandOutput(command2);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(output1!=null && output2!=null){



			try {
				String lines1[] = output1.split("\\r?\\n");
				String lines2[] = output2.split("\\r?\\n");
				int mhz= (int) Float.parseFloat((lines1[0].split(":"))[1].trim());
				int totalCPUCount = Integer.parseInt(lines2[0].trim());

				return (mhz*totalCPUCount)+"";
			} catch (Exception ex){
				return "N/A";
			}




		}else {
			return "N/A";
		}

	}


	public static String getCPUUsageUbuntu() {
		String command = "vmstat 1 2|tail -1|awk '{print $15}'";
		String output = null;
		try {
			output = getCommandOutput(command);

			if(output!=null){
				String lines[] = output.split("\\r?\\n");
				String toReturn = (100-Integer.parseInt(lines[0].trim()))+"";

				return toReturn;



			}else {
				return "N/A";
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "1";

	}



	public static String getCommandOutput(String command) {
		/*    */       try {
			/* 24 */          Process p = null;
			/* 25 */          String os = System.getProperty("os.name");
			/* 26 */          if (os.startsWith("Windows")) {
				/* 27 */             p = Runtime.getRuntime().exec(command);
			/* 28 */          } else if (os.startsWith("Linux")) {
				/* 29 */             p = Runtime.getRuntime().exec(new String[]{"sh", "-c", command});
			/*    */          }
			/* 31 */          p.waitFor();
			/* 32 */          BufferedReader buf = new BufferedReader(new InputStreamReader(p.getInputStream()));
			/* 33 */          String line = "";         String output;
			/* 34 */          for(output = ""; (line = buf.readLine()) != null; output = output + line + "\n") {
				/*    */             ;
			/*    */          }
			/*    */ 
			/* 38 */          return output;
		/* 39 */       } catch (Exception var6) {
			/* 40 */          return null;
		/*    */       }
	/*    */    }



	public static String getDiskUsageInPercentageUbuntu(){


		String command = "df -h | grep \"/dev/sd\" | awk '{print $5}'";
		String output = null;
		try {
			output = getCommandOutput(command);

			if(output!=null){
				String lines[] = output.split("\\r?\\n");

				String toReturn = lines[0].replace("%", "").trim();

				return toReturn;



			}else {
				return "N/A";
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "1";


	}

	public static boolean isNameValid(String name) {


		boolean isValid = false;
		try {

			String regex = "^[a-zA-Z0-9_\\-\\s]*$";

			if (name.matches(regex)) {
				isValid = true;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return isValid;
	}

	public static boolean isFtpHostValid(String ftpHost) {


		boolean isValid = false;
		try {

			String IpRegex = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
					"([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
					"([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
					"([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";;
					String regex = "^[a-zA-Z]*$";

					if (ftpHost.matches(IpRegex) || ftpHost.matches(regex)) {
						isValid = true;
					}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return isValid;
	}
	
	static String generateRandomStringOfLength(int n) 
    { 
  
        
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                                    + "0123456789"
                                    + "abcdefghijklmnopqrstuvxyz"; 
  
        
        StringBuilder sb = new StringBuilder(n); 
  
        for (int i = 0; i < n; i++) { 
  
            
            int index 
                = (int)(AlphaNumericString.length() 
                        * Math.random()); 
  
           
            sb.append(AlphaNumericString 
                          .charAt(index)); 
        } 
  
        return sb.toString(); 
    } 


}
