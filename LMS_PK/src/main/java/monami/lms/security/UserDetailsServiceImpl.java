package monami.lms.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import monami.lms.datadaos.ApplicationUserDAO;
import monami.lms.dataentities.ApplicationUsers;
import monami.lms.dataentities.Privilege;
import monami.lms.dataentities.Role;
import monami.lms.serverutils.CurrentLoggedInUser;

@Service("customUserDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private ApplicationUserDAO objApplicationUSerDAo;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		try {

			boolean enabled = true;
			boolean accountNonExpired = true;
			boolean credentialsNonExpired = true;
			boolean accountNonLocked = true;

			List<ApplicationUsers> applicationUsers = objApplicationUSerDAo.getAllUsers();

			for (ApplicationUsers users : applicationUsers) {

				if (users.getUsername().equals(username)) {

					CurrentLoggedInUser currentLoggedInUser = CurrentLoggedInUser.getInstance();
					currentLoggedInUser.setUserId(users.getUserId());

					CustomUser customUser = new CustomUser(users.getUsername(), users.getPassword(), enabled,
							accountNonExpired, credentialsNonExpired, accountNonLocked,
							getAuthorities(users.getRoles()), users.getUserId());

					
					return customUser;

				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		// If user not found. Throw this exception.
		throw new UsernameNotFoundException("Username: " + username + " not found");
	}

	private Collection<? extends GrantedAuthority> getAuthorities(Collection<Role> roles) {

		return getGrantedPrivileges(getPrivileges(roles));
	}

	private List<String> getPrivileges(Collection<Role> roles) {

		List<String> privileges = new ArrayList<>();
		List<Privilege> collection = new ArrayList<>();
		for (Role role : roles) {
			collection.addAll(role.getPrivileges());
		}
		for (Privilege item : collection) {
			privileges.add(item.getPrivilegeName());
			
		}
		
		
		return privileges;
	}

	private List<GrantedAuthority> getGrantedPrivileges(List<String> privileges) {
		List<GrantedAuthority> authorities = new ArrayList<>();
		for (String privilege : privileges) {
			authorities.add(new SimpleGrantedAuthority(privilege));
		}
		return authorities;
	}

}
