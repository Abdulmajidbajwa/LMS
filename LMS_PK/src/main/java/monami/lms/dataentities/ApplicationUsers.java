package monami.lms.dataentities;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.UpdateTimestamp;
@Entity
public class ApplicationUsers implements Serializable,Comparable{
	private static final long serialVersionUID = -2334797252168823133L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int userId;
	
	private String displayName;
	@Column(unique = true)
	private String username;
	private String password;
	private String category; 
	
	private String lastloginAttempt;
	private String assignToken;
	private String lastAccess;
	private String appliedTheme;
	private int loginCounter=0;

	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
	@JoinTable(name = "UsersRole", joinColumns = @JoinColumn(name = "userId"), inverseJoinColumns = @JoinColumn(name = "roleId"))
	@Fetch(value = FetchMode.SUBSELECT)
	private Collection<Role> roles;
	
	
	@OneToOne
	private ApplicationUsers createdBy;
	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdAt;

	@OneToOne
	private ApplicationUsers updatedBy;
	@UpdateTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	private Date updateAt;
	
	public ApplicationUsers(){ 
		
	}
	
	public ApplicationUsers(String displayName, String username, String password, String category, String lastlogin, String assignToken, Collection<Role> roles) {
		super();
		this.displayName = displayName;
		this.username = username;
		this.password = password;
		this.category=category;
		this.lastloginAttempt = lastlogin;
		this.assignToken = assignToken;
		this.roles = roles;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getLastloginAttempt() {
		return lastloginAttempt;
	}
	public void setLastloginAttempt(String lastloginAttempt) {
		this.lastloginAttempt = lastloginAttempt;
	}

	public Collection<Role> getRoles() {
		return roles;
	}

	public void setRoles(Collection<Role> roles) {
		this.roles = roles;
	}

	

	public String getAssignToken() {
		return assignToken;
	}

	public void setAssignToken(String assignToken) {
		this.assignToken = assignToken;
	}
	
	public String getLastAccess() {
		return lastAccess;
	}

	public void setLastAccess(String lastAccess) {
		this.lastAccess = lastAccess;
	}
	

	public String getAppliedTheme() {
		return appliedTheme;
	}

	public void setAppliedTheme(String appliedTheme) {
		this.appliedTheme = appliedTheme;
	}
	
	

	public int getLoginCounter() {
		return loginCounter;
	}

	public void setLoginCounter(int loginCounter) {
		this.loginCounter = loginCounter;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((appliedTheme == null) ? 0 : appliedTheme.hashCode());
		result = prime * result + ((assignToken == null) ? 0 : assignToken.hashCode());
		result = prime * result + ((displayName == null) ? 0 : displayName.hashCode());
		result = prime * result + ((lastAccess == null) ? 0 : lastAccess.hashCode());
		result = prime * result + ((lastloginAttempt == null) ? 0 : lastloginAttempt.hashCode());
		result = prime * result + loginCounter;
		result = prime * result + ((password == null) ? 0 : password.hashCode());
		result = prime * result + ((roles == null) ? 0 : roles.hashCode());
		result = prime * result + userId;
		result = prime * result + ((username == null) ? 0 : username.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ApplicationUsers other = (ApplicationUsers) obj;
		
		if (appliedTheme == null) {
			if (other.appliedTheme != null)
				return false;
		} else if (!appliedTheme.equals(other.appliedTheme))
			return false;
		if (assignToken == null) {
			if (other.assignToken != null)
				return false;
		} else if (!assignToken.equals(other.assignToken))
			return false;
		if (displayName == null) {
			if (other.displayName != null)
				return false;
		} else if (!displayName.equals(other.displayName))
			return false;
		if (lastAccess == null) {
			if (other.lastAccess != null)
				return false;
		} else if (!lastAccess.equals(other.lastAccess))
			return false;
		if (lastloginAttempt == null) {
			if (other.lastloginAttempt != null)
				return false;
		} else if (!lastloginAttempt.equals(other.lastloginAttempt))
			return false;
		if (loginCounter != other.loginCounter)
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (roles == null) {
			if (other.roles != null)
				return false;
		} else if (!roles.equals(other.roles))
			return false;
		if (userId != other.userId)
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}

	
	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getUpdateAt() {
		return updateAt;
	}

	public void setUpdateAt(Date updateAt) {
		this.updateAt = updateAt;
	}
	
	
	

	public ApplicationUsers getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(ApplicationUsers createdBy) {
		this.createdBy = createdBy;
	}

	public ApplicationUsers getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(ApplicationUsers updatedBy) {
		this.updatedBy = updatedBy;
	}

	@Override
	public int compareTo(Object o) {
		// TODO Auto-generated method stub
		return Integer.valueOf(this.getUserId()).compareTo(((ApplicationUsers)o).getUserId());
	}
	
	
	
}
