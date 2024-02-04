package monami.lms.rest.filters;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import monami.lms.datadaos.ApplicationUserDAO;
import monami.lms.dataentities.ApplicationUsers;

@Service
public class AuthTokenFilter extends OncePerRequestFilter {

	private UserDetailsService customUserDetailsService;

	private String authTokenHeaderName = "authString";

//	public AuthTokenFilter() {
//	}
//
//	public void setcustomUserDetailsService(UserDetailsService objuserDetailsService) {
//		this.customUserDetailsService = objuserDetailsService;
//	}

	public AuthTokenFilter(UserDetailsService userDetailsService) {
		this.customUserDetailsService = userDetailsService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
			FilterChain filterChain) throws ServletException, IOException {
		try {

			httpServletResponse.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");
			httpServletResponse.setHeader("Access-Control-Allow-Credentials", "true");
			httpServletResponse.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, OPTIONS, DELETE");
			httpServletResponse.setHeader("Access-Control-Max-Age", "3600");
			httpServletResponse.setHeader("Access-Control-Allow-Headers",
					"x-auth-token, Content-Type, Accept, X-Requested-With, remember-me, authString");

			String authToken = httpServletRequest.getHeader(authTokenHeaderName);
			if (StringUtils.hasText(authToken)) {

				ServletContext servletContext = httpServletRequest.getServletContext();
				WebApplicationContext webApplicationContext = WebApplicationContextUtils
						.getWebApplicationContext(servletContext);
				ApplicationUserDAO objApplicationUserDAO = webApplicationContext.getBean(ApplicationUserDAO.class);

				ApplicationUsers applicationUsers = objApplicationUserDAO.getUserAgainstToken(authToken);
				if (applicationUsers == null) {
					
					httpServletResponse.setContentType("application/json");
					httpServletResponse.setCharacterEncoding("UTF-8");
					httpServletResponse.getWriter().write("{\"Message\":\"" + "Not a valid user."
							+ "\",\"Requested_Action\":\"False\"}");
					httpServletResponse.getWriter().flush();
					return;
				}
				double diffHours=0;
				try {

					String pattern = "yyyy/MM/dd HH:mm:ss";
					SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
					

					Date lastAccess = simpleDateFormat.parse(applicationUsers.getLastAccess());
					Date currentTime = new Date();

					double diff = currentTime.getTime() - lastAccess.getTime();
					diffHours = diff / (60 * 60 * 1000) % 24;
				} catch (Exception ex){
					
				}


				if (applicationUsers != null && diffHours < 6) {
					String pattern = "yyyy/MM/dd HH:mm:ss";
					SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
					Date currentTime = new Date();
					applicationUsers.setLastAccess(simpleDateFormat.format(currentTime));

					objApplicationUserDAO.updateUser(applicationUsers);

					UserDetails userDetails = customUserDetailsService
							.loadUserByUsername(applicationUsers.getUsername());

					UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userDetails,
							userDetails.getPassword(), userDetails.getAuthorities());
					SecurityContextHolder.getContext().setAuthentication(auth);
				} else {
					httpServletResponse.setContentType("application/json");
					httpServletResponse.setCharacterEncoding("UTF-8");
					httpServletResponse.getWriter().write("{\"Message\":\"" + "Expired authentication token was sent"
							+ "\",\"Requested_Action\":\"False\"}");
					httpServletResponse.getWriter().flush();
					return;

				}

			} else {
				httpServletResponse.setContentType("application/json");
				httpServletResponse.setCharacterEncoding("UTF-8");
				httpServletResponse.getWriter().write(
						"{\"Message\":\"" + "Authentication token not found" + "\",\"Requested_Action\":\"False\"}");
				httpServletResponse.getWriter().flush();
				return;
			}
			filterChain.doFilter(httpServletRequest, httpServletResponse);
		} catch (ServletException e) {
			try {
				httpServletResponse.setContentType("application/json");
				httpServletResponse.setCharacterEncoding("UTF-8");
				httpServletResponse.getWriter().write("{\"Message\":\"" + "User is not authorized to access this."
						+ "\",\"Requested_Action\":\"False\"}");
				httpServletResponse.getWriter().flush();
				return;
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.printStackTrace();
			SecurityContextHolder.clearContext();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) {
		String path = request.getServletPath();
		return !path.contains("/LMSServer/");
		// do not filter any this that doesnot contain LMSServer -- that 
	}
}
