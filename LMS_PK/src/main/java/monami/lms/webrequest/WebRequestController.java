package monami.lms.webrequest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import monami.lms.responceentities.BasicResponce;
import monami.lms.responceentities.ResponceWithMessage;



@RestController
public class WebRequestController {
	@RequestMapping(method=RequestMethod.POST,value="/getApplicationName")	
	public BasicResponce getApplicationNamePost() {
		BasicResponce objBasicResponce=new ResponceWithMessage(true,"@Post Monami FinStar Backend");
		return objBasicResponce;
	}
	
	@RequestMapping(method=RequestMethod.GET,value="/getApplicationName")	
	public BasicResponce getApplicationNameGet() {
		BasicResponce objBasicResponce=new ResponceWithMessage(true,"@Get Monami FinStar Backend");
		return objBasicResponce;
	}
}
