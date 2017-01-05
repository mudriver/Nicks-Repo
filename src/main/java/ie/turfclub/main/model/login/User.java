package ie.turfclub.main.model.login;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Formula;
import org.hibernate.validator.constraints.NotEmpty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.google.common.base.Objects;

@Entity
@Table(name = "user_users", catalog = "turfclub_users")
public class User implements UserDetails {
	private static final long serialVersionUID = 6311364761937265306L;
	static Logger logger = LoggerFactory.getLogger(User.class);

	@Id
	@GeneratedValue
	@Column(name = "users_id")
	private Integer user_id;
	
	@NotNull(message = "{error.user.username.null}")
	@NotEmpty(message = "{error.user.username.empty}")
	@Size(max = 50, message = "{error.user.username.max}")
	@Column(name = "users_email", length = 50)
	private String user_login;

	@NotNull(message = "{error.user.password.null}")
	@NotEmpty(message = "{error.user.password.empty}")
	@Column(name = "users_pwd", length = 200)
	private String user_pwd;

	@Column(name = "users_first_name", length = 200)
	private String firstname;

	@Column(name = "users_surname", length = 200)
	private String surname;
	
	@Formula(value = " concat(users_surname, ' ', users_first_name) ")
	private String fullName;

	@Transient
	private String confirmPassword;
	
	@Transient
	private Integer roleId;

	@Transient
	private Collection<? extends GrantedAuthority> authorities = new ArrayList<>();

	public Integer getId() {
		return user_id;
	}

	public void setId(Integer user_id) {
		this.user_id = user_id;
	}

	public Integer getUser_id() {
		return user_id;
	}

	public void setUser_id(Integer user_id) {
		this.user_id = user_id;
	}

	public String getUser_login() {
		return user_login;
	}

	public void setUser_login(String user_login) {
		this.user_login = user_login;
	}

	public String getUser_pwd() {
		return user_pwd;
	}

	public void setUser_pwd(String user_pwd) {
		this.user_pwd = user_pwd;
	}

	@Override
	public String getUsername() {
		return user_login;
	}

	public void setUsername(String username) {
		this.user_login = username;
	}

	@Override
	public String getPassword() {
		return user_pwd;
	}

	public void setPassword(String password) {
		this.user_pwd = password;
	}

	public boolean getEnabled() {
		return true;
	}

	public void setEnabled(boolean enabled) {

	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	@Override
	public String toString() {
		return String.format("%s(id=%d, username=%s, password=%s, enabled=%b)",
				this.getClass().getSimpleName(), this.getId(),
				this.getUsername(), this.getPassword(), this.getEnabled());
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null)
			return false;

		if (o instanceof User) {
			final User other = (User) o;
			return Objects.equal(getId(), other.getId())
					&& Objects.equal(getUsername(), other.getUsername())
					&& Objects.equal(getPassword(), other.getPassword())
					&& Objects.equal(getEnabled(), other.getEnabled());
		}
		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(getId(), getUsername(), getPassword(),
				getEnabled());
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.authorities;
	}

	// TODO temporary authorities implementation - will revise in the next
	// example
	public void setAuthorities(
			Collection<? extends GrantedAuthority> authorities) {
		this.authorities = authorities;
	}

	@Override
	public boolean isAccountNonExpired() {
		// return true = account is valid / not expired
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// return true = account is not locked
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// return true = password is valid / not expired
		return true;
	}

	@Override
	public boolean isEnabled() {
		return this.getEnabled();
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

}