package ie.turfclub.main.model.login;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "user_roles", catalog = "turfclub_users")
public class UserRoles implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@Column(name = "role_id")
	private Integer id;
	
	@Column(name = "role_user_id")
	private Integer userId;
	
	@Column(name = "role_roletype_id")
	private Integer roletypeId;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getRoletypeId() {
		return roletypeId;
	}

	public void setRoletypeId(Integer roletypeId) {
		this.roletypeId = roletypeId;
	}
}
