package monami.lms;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import monami.lms.datadaos.ApplicationUserDAO;
import monami.lms.datadaos.PrivilegeDAO;
import monami.lms.datadaos.RoleDAO;
import monami.lms.datadaos.ServerConfigurationsDAO;
import monami.lms.dataentities.ApplicationUsers;
import monami.lms.dataentities.Privilege;
import monami.lms.dataentities.Role;
import monami.lms.dataentities.ServerConfigurations;
import monami.lms.serverutils.ServerUtils;

@SpringBootApplication
public class Driver {
	@Autowired
	ApplicationUserDAO objApplicationUserDAO;
	
	@Autowired
	ServerUtils objServerUtils;

	@Autowired
	private Environment environment;
	
	@Autowired 
	private ServerConfigurationsDAO objServerConfigurationsDAO;

	@Autowired
	RoleDAO objRoleDAO;
	
	@Autowired
	PrivilegeDAO objPrivilegeDAO;

	static Logger LOG = LoggerFactory.getLogger(Driver.class);
	
	
	@Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/*").allowedOrigins("*");
            }
        };
    }

	
	
	@EventListener(ApplicationReadyEvent.class)
	public void doSomethingAfterStartup() {
		LOG.info("hello world, I have just started up");
		
		//http://localhost:8080/swagger-ui.html
		try {
			
			Privilege USER_MANAGEMENT_R = objPrivilegeDAO.saveOrUpdatePrivilege("USER_MANAGEMENT_R", "USER MANAGEMENT READONLY");
			Privilege USER_MANAGEMENT_RW = objPrivilegeDAO.saveOrUpdatePrivilege("USER_MANAGEMENT_RW", "USER MANAGEMENT");

//			Privilege NOTIFICATION_R = objPrivilegeDAO.saveOrUpdatePrivilege("NOTIFICATION_R", "NOTIFICATION READONLY");
//			Privilege NOTIFICATION_RW = objPrivilegeDAO.saveOrUpdatePrivilege("NOTIFICATION_RW", "NOTIFICATION");
			
//			Privilege REG_CUSTOMER_LIST_R = objPrivilegeDAO.saveOrUpdatePrivilege("REG_CUSTOMER_LIST_R", "REG CUSTOMER LIST READONLY");
//			Privilege REG_CUSTOMER_LIST_RW = objPrivilegeDAO.saveOrUpdatePrivilege("REG_CUSTOMER_LIST_RW", "REG CUSTOMER LIST");
			
//			Privilege KYC_VALIDATION_R = objPrivilegeDAO.saveOrUpdatePrivilege("KYC_VALIDATION_R", "KYC VALIDATION READONLY");
//			Privilege KYC_VALIDATION_RW = objPrivilegeDAO.saveOrUpdatePrivilege("KYC_VALIDATION_RW", "KYC VALIDATION");
			
//			Privilege LOAN_DISPERSAL_R = objPrivilegeDAO.saveOrUpdatePrivilege("LOAN_DISPERSAL_R", "LOAN DISPERSAL READONLY");
//			Privilege LOAN_DISPERSAL_RW = objPrivilegeDAO.saveOrUpdatePrivilege("LOAN_DISPERSAL_RW", "LOAN DISPERSAL");
			
			Privilege CUSTOMER_MANAGEMENT_R = objPrivilegeDAO.saveOrUpdatePrivilege("CUSTOMER_MANAGEMENT_R", "CUSTOMER MANAGEMENT READONLY");
			Privilege CUSTOMER_MANAGEMENT_RW = objPrivilegeDAO.saveOrUpdatePrivilege("CUSTOMER_MANAGEMENT_RW", "CUSTOMER MANAGEMENT");

			Privilege PRODUCT_MANAGEMENT_R = objPrivilegeDAO.saveOrUpdatePrivilege("PRODUCT_MANAGEMENT_R", "PRODUCT MANAGEMENT READONLY");
			Privilege PRODUCT_MANAGEMENT_RW = objPrivilegeDAO.saveOrUpdatePrivilege("PRODUCT_MANAGEMENT_RW", "PRODUCT MANAGEMENT");

			Privilege ASSUMPTION_MANAGEMENT_R = objPrivilegeDAO.saveOrUpdatePrivilege("ASSUMPTION_MANAGEMENT_R", "ASSUMPTION MANAGEMENT READONLY");
			Privilege ASSUMPTION_MANAGEMENT_RW = objPrivilegeDAO.saveOrUpdatePrivilege("ASSUMPTION_MANAGEMENT_RW", "ASSUMPTION MANAGEMENT");

			Privilege KYC_MANAGEMENT_R = objPrivilegeDAO.saveOrUpdatePrivilege("KYC_MANAGEMENT_R", "KYC MANAGEMENT READONLY");
			Privilege KYC_MANAGEMENT_RW = objPrivilegeDAO.saveOrUpdatePrivilege("KYC_MANAGEMENT_RW", "KYC MANAGEMENT");
			
			Privilege ROLE_MANAGEMENT_R = objPrivilegeDAO.saveOrUpdatePrivilege("ROLE_MANAGEMENT_R", "ROLE MANAGEMENT READONLY");
			Privilege ROLE_MANAGEMENT_RW = objPrivilegeDAO.saveOrUpdatePrivilege("ROLE_MANAGEMENT_RW", "ROLE MANAGEMENT");
			
			Privilege PRIV_MANAGEMENT_R = objPrivilegeDAO.saveOrUpdatePrivilege("PRIV_MANAGEMENT_R", "PRIV MANAGEMENT READONLY");
			Privilege PRIV_MANAGEMENT_RW = objPrivilegeDAO.saveOrUpdatePrivilege("PRIV_MANAGEMENT_RW", "PRIV MANAGEMENT");
			
			Privilege GENERAL_LEDGER_MANAGEMENT_R = objPrivilegeDAO.saveOrUpdatePrivilege("GENERAL_LEDGER_MANAGEMENT_R", "GENERAL LEDGER MANAGEMENT READONLY");
			Privilege GENERAL_LEDGER_MANAGEMENT_RW = objPrivilegeDAO.saveOrUpdatePrivilege("GENERAL_LEDGER_MANAGEMENT_RW", "GENERAL LEDGER MANAGEMENT");
			
			Privilege LOAN_MANAGEMENT_R = objPrivilegeDAO.saveOrUpdatePrivilege("LOAN_MANAGEMENT_R", "LOAN MANAGEMENT READONLY");
			Privilege LOAN_MANAGEMENT_RW = objPrivilegeDAO.saveOrUpdatePrivilege("LOAN_MANAGEMENT_RW", "LOAN MANAGEMENT");
			
			
			List<Privilege> adminPrivleges = Arrays.asList(USER_MANAGEMENT_R, USER_MANAGEMENT_RW,
//					NOTIFICATION_R,NOTIFICATION_RW,
//					REG_CUSTOMER_LIST_R,REG_CUSTOMER_LIST_RW,
//					KYC_VALIDATION_R,KYC_VALIDATION_RW,
//					LOAN_DISPERSAL_R,LOAN_DISPERSAL_RW,
					CUSTOMER_MANAGEMENT_R,CUSTOMER_MANAGEMENT_RW,
					PRODUCT_MANAGEMENT_R,PRODUCT_MANAGEMENT_RW,
					ASSUMPTION_MANAGEMENT_R,ASSUMPTION_MANAGEMENT_RW,
					KYC_MANAGEMENT_R,KYC_MANAGEMENT_RW,
					ROLE_MANAGEMENT_R,ROLE_MANAGEMENT_RW,
					PRIV_MANAGEMENT_R,PRIV_MANAGEMENT_RW,
					GENERAL_LEDGER_MANAGEMENT_R,GENERAL_LEDGER_MANAGEMENT_RW,
					LOAN_MANAGEMENT_R,LOAN_MANAGEMENT_RW);
			
			List<Privilege> userManagerPrivleges = Arrays.asList(USER_MANAGEMENT_R, USER_MANAGEMENT_RW);

			Role objRole = objRoleDAO.getRoleByRoleName("ROLE_ADMIN");
			
			Role objRole2 = objRoleDAO.getRoleByRoleName("USER_MANAGER");
			
			if(objRole == null) {
				objRole = new Role("ROLE_ADMIN", "This role has all the rights.", adminPrivleges);
			} else {

			}
			
			if(objRole2 == null) {
				objRole2 = new Role("USER_MANAGER", "This role is a limit to user management only.", userManagerPrivleges);
			} else {

			}
			
			objRoleDAO.saveOrUpdateRole(objRole);
			objRoleDAO.saveOrUpdateRole(objRole2);
			
			try {
				
				//Custom Abdul Majid--Start
//				objApplicationUserDAO.addUser(new ApplicationUsers("CC Admin", "Administrator", "Monami@1", null, null, Arrays.asList(objRoleDAO.getRoleByRoleName("ROLE_ADMIN"))));
//				objApplicationUserDAO.addUser(new ApplicationUsers("Syed Iftekhar Ahmed", "iftekhar", "iftekhar@1", null, null, Arrays.asList(objRoleDAO.getRoleByRoleName("USER_MANAGER"))));
				
				objApplicationUserDAO.addUser(new ApplicationUsers("CC Admin", "Administrator", "Monami@1","Admin" , null, null, Arrays.asList(objRoleDAO.getRoleByRoleName("ROLE_ADMIN"))));
				objApplicationUserDAO.addUser(new ApplicationUsers("Syed Iftekhar Ahmed", "iftekhar", "iftekhar@1","Staff",  null, null, Arrays.asList(objRoleDAO.getRoleByRoleName("USER_MANAGER"))));
				//Custom Abdul Majid--End
				
				
			} catch (Exception ex){
				ex.printStackTrace();
				LOG.debug("Init App", ex);
			}

		} catch (Exception ex){
			ex.printStackTrace();
			LOG.debug("Init App", ex);
		}
		
		try {
			String ipAddress = objServerUtils.getFirstNonLoopbackAddress();
			if(ipAddress==null || ipAddress.trim().isEmpty()){
				LOG.error("No Network Info");
				System.exit(1);
			}
			ipAddress=ipAddress.replaceAll("/", "");
			String mac=objServerUtils.getMacAddress(ipAddress);
			
		} catch (Exception ex){
			LOG.debug("Init App", ex);
		}

	
	



	}

	public static void main(String args[]){
		try {
			LOG.info("Starting LMS Server");
			
			SpringApplication.run(Driver.class, args);
		} catch (Exception ex){
			ex.printStackTrace();
		}
	}
	

}
