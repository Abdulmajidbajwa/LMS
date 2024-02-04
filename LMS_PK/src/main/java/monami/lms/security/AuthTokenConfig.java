package monami.lms.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import monami.lms.rest.filters.AuthTokenFilter;

@Component("authTokenConfig")
public class AuthTokenConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

	@Autowired
	@Qualifier("customUserDetailsService")
	private UserDetailsService customUserDetailsService;

//	@Autowired
//	private AuthTokenFilter objAuthTokenFilter;

	@Override
	public void configure(HttpSecurity http) throws Exception {

//		objAuthTokenFilter.setcustomUserDetailsService(customUserDetailsService);

		AuthTokenFilter customFilter = new AuthTokenFilter(customUserDetailsService);
		http.addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class);

	}

}
