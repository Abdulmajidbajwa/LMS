package monami.lms.dataentities;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
public class Privilege implements Serializable,Comparable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1034658950743216153L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int privilegeId;
	@Column(unique = true)
	private String privilegeName;
	
	private String privilegeDescription;
	
	@ManyToMany(mappedBy = "privileges")
	@Cascade(CascadeType.ALL)
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
	private Date updatedAt;
	

	public Privilege() {

	}

	public Privilege(String privilegeName) {
		this.privilegeName = privilegeName;
	}

	public int getPrivilegeId() {
		return privilegeId;
	}

	public void setPrivilegeId(int privilegeId) {
		this.privilegeId = privilegeId;
	}

	public String getPrivilegeName() {
		return privilegeName;
	}

	public void setPrivilegeName(String privilegeName) {
		this.privilegeName = privilegeName;
	}

	

	public Collection<Role> getRoles() {
		return roles;
	}

	public void setRoles(Collection<Role> roles) {
		this.roles = roles;
	}
	
	public String getPrivilegeDescription() {
		return privilegeDescription;
	}

	public void setPrivilegeDescription(String privilegeDescription) {
		this.privilegeDescription = privilegeDescription;
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
		result = prime * result + ((privilegeDescription == null) ? 0 : privilegeDescription.hashCode());
		result = prime * result + privilegeId;
		result = prime * result + ((privilegeName == null) ? 0 : privilegeName.hashCode());
		
		result = prime * result + ((roles == null) ? 0 : roles.hashCode());
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
		Privilege other = (Privilege) obj;
		if (privilegeDescription == null) {
			if (other.privilegeDescription != null)
				return false;
		} else if (!privilegeDescription.equals(other.privilegeDescription))
			return false;
		if (privilegeId != other.privilegeId)
			return false;
		if (privilegeName == null) {
			if (other.privilegeName != null)
				return false;
		} else if (!privilegeName.equals(other.privilegeName))
			return false;
		
		if (roles == null) {
			if (other.roles != null)
				return false;
		} else if (!roles.equals(other.roles))
			return false;
		return true;
	}

	@Override
	public int compareTo(Object o) {
		// TODO Auto-generated method stub
		return Integer.valueOf(this.getPrivilegeId()).compareTo(((Privilege)o).getPrivilegeId());
	}

}
