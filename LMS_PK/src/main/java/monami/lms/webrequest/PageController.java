package monami.lms.webrequest;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PageController {
	
	public PageController(){
		System.out.println("PageController()");
	}
	
	
	@RequestMapping("/new-ui")
	public String adminhome(Map<String, Object> model) {
		System.out.println("adminhome");
	    return "new-portal/index.html";
	}
	
//	@RequestMapping("/admin/**")
//	public String adminhome2(Map<String, Object> model) {
//		try {
//			System.out.println("adminhome2");
//			return "redirect:/admin";
//		} catch (Exception ex){
//			System.out.print("e");
//			return "";
//		}
//	}
	
	
//	@RequestMapping("/landing")
//	public String clienthome(Map<String, Object> model) {
//		System.out.println("adminhome");
//	    return "client/index.html";
//	}
//	
//	@RequestMapping("/landing/**")
//	public String clienthome2(Map<String, Object> model) {
//		try {
//			System.out.println("clienthome2");
//			return "redirect:/landing";
//		} catch (Exception ex){
//			System.out.print("e");
//			return "";
//		}
//	}
	
	
//	
//	@RequestMapping("/")
//	public String root(Map<String, Object> model) {
//		System.out.println("root");
//		return "redirect:/landing";
//	}
	

	
	


}
