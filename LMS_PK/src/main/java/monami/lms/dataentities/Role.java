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
public class Role  implements Serializable,Comparable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2264180401202437684L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int roleId;
	@Column(unique = true)
	private String roleName;
	@Column(length = 255)
	private String roleDescription;
	
    @ManyToMany(fetch = FetchType.EAGER)
    private Collection<ApplicationUsers> users;


    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
	@Fetch(value = FetchMode.SUBSELECT)
	private Collection<Privilege> privileges;  
    
    
    @OneToOne
	private ApplicationUsers createdBy;
	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdAt;

	@OneToOne
	private ApplicationUsers updatedBy;
	@UpdateTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	private Date updatedAt;
    
    
	public Role() {
		
	}
	
	public Role(String roleName, String roleDescription, Collection<Privilege> privileges) {
		super();
		this.roleName = roleName;
		this.roleDescription = roleDescription;
		this.privileges = privileges;
	}

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getRoleDescription() {
		return roleDescription;
	}

	public void setRoleDescription(String roleDescription) {
		this.roleDescription = roleDescription;
	}

	public Collection<ApplicationUsers> getUsers() {
		return users;
	}

	public void setUsers(Collection<ApplicationUsers> users) {
		this.users = users;
	}

	public Collection<Privilege> getPrivileges() {
		return privileges;
	}

	public void setPrivileges(Collection<Privilege> privileges) {
		this.privileges = privileges;
	}

	public ApplicationUsers getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(ApplicationUsers createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public ApplicationUsers getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(ApplicationUsers updatedBy) {
		this.updatedBy = updatedBy;
	}
	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((privileges == null) ? 0 : privileges.hashCode());
		result = prime * result + ((roleDescription == null) ? 0 : roleDescription.hashCode());
		result = prime * result + roleId;
		result = prime * result + ((roleName == null) ? 0 : roleName.hashCode());
		result = prime * result + ((users == null) ? 0 : users.hashCode());
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
		Role other = (Role) obj;
		if (privileges == null) {
			if (other.privileges != null)
				return false;
		} else if (!privileges.equals(other.privileges))
			return false;
		if (roleDescription == null) {
			if (other.roleDescription != null)
				return false;
		} else if (!roleDescription.equals(other.roleDescription))
			return false;
		if (roleId != other.roleId)
			return false;
		if (roleName == null) {
			if (other.roleName != null)
				return false;
		} else if (!roleName.equals(other.roleName))
			return false;
		if (users == null) {
			if (other.users != null)
				return false;
		} else if (!users.equals(other.users))
			return false;
		return true;
	}
	
	
	
	

	
	@Override
	public int compareTo(Object o) {
		// TODO Auto-generated method stub
		return Integer.valueOf(this.getRoleId()).compareTo(((Role)o).getRoleId());
	}
		

	
}