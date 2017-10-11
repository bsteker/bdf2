package com.bstek.bdf2.core.model;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.bstek.bdf2.core.business.AbstractUser;
import com.bstek.bdf2.core.business.IDept;
import com.bstek.bdf2.core.business.IPosition;
/**
 * @author Jacky
 * 默认用户实现 类
 */
@Entity
@Table(name="BDF2_USER")
public class DefaultUser extends AbstractUser implements java.io.Serializable{
	private static final long serialVersionUID = -1363793377621174845L;
	@Id
	@Column(name="USERNAME_",length=60)
	private String username;	
	@Column(name="PASSWORD_",length=70,nullable=false)
	private String password;
	@Column(name="SALT_",length=10,nullable=false)
	private String salt;
	@Column(name="CNAME_",length=60,nullable=false)
	private String cname;
	@Column(name="ENAME_",length=60)
	private String ename;
	@Column(name="MALE_",nullable=false)
	private boolean male=true;
	@Column(name="ENABLED_",nullable=false)
	private boolean enabled=true;
	@Column(name="ADMINISTRATOR_",nullable=false)
	private boolean administrator=false;
	@Column(name="BIRTHDAY_")
	private Date birthday;
	@Column(name="MOBILE_",length=20)
	private String mobile;
	@Column(name="ADDRESS_",length=120)
	private String address;
	@Column(name="EMAIL_",length=60)
	private String email;
	@Column(name="COMPANY_ID_",length=60,nullable=false)
	private String companyId;
	@Column(name="CREATE_DATE_")
	private Date createDate;
	
	@Transient
	private List<IPosition> positions;
	
	@Transient
	private List<IDept> depts;
	
	@Transient
	private List<Group> groups;
	
	@Transient
	private List<Role> roles;
	
	public DefaultUser(){}
	public DefaultUser(String username){
		this.username=username;
	}
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getCname() {
		return cname;
	}
	public void setCname(String cname) {
		this.cname = cname;
	}
	public String getEname() {
		return ename;
	}
	public void setEname(String ename) {
		this.ename = ename;
	}
	public boolean isMale() {
		return male;
	}
	public void setMale(boolean male) {
		this.male = male;
	}
	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public List<IDept> getDepts() {
		return depts;
	}
	public void setDepts(List<IDept> depts) {
		this.depts = depts;
	}
	public List<IPosition> getPositions() {
		return positions;
	}
	public void setPositions(List<IPosition> positions) {
		this.positions = positions;
	}
	public List<Group> getGroups() {
		return groups;
	}
	public void setGroups(List<Group> groups) {
		this.groups = groups;
	}
	public List<Role> getRoles() {
		return roles;
	}
	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}
	public String getSalt() {
		return salt;
	}
	public void setSalt(String salt) {
		this.salt = salt;
	}
	public Date getBirthday() {
		return birthday;
	}
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
	public boolean isAdministrator() {
		return administrator;
	}
	public void setAdministrator(boolean administrator) {
		this.administrator = administrator;
	}
	public String getCompanyId() {
		return companyId;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Map<String, Object> getMetadata() {
		return null;
	}
	
	public String toString(){
		return "companyId:"+companyId+", name:"+username + ", administrator:"+administrator + ", enabled:"+ enabled;
	}
}
