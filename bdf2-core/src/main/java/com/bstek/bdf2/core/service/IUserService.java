package com.bstek.bdf2.core.service;

import java.util.Collection;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.bstek.bdf2.core.business.IUser;
import com.bstek.dorado.data.provider.Criteria;
import com.bstek.dorado.data.provider.Page;

/**
 * @since 2013-1-18
 * @author Jacky.gao
 */
public interface IUserService extends UserDetailsService {
	public static final String BEAN_ID="bdf2.userService";
	/**
	 * 分页加载用户数据
	 * @param page Dorado7分页对象，其中包含pageNo,pageSize，分页后的数据也填充到这个page对象当中，该参数不可为空
	 * @param companyId 要加载哪个companyId下的用户信息，该参数不可为空
	 * @param criteria Dorado7条件对象，可从中取到相应的条件值，该参数可为空
	 */
	void loadPageUsers(Page<IUser> page,String companyId,Criteria criteria);
	
	/**
	 * 加载指定部门ID下的用户信息
	 * @param deptId 隶属的部门ID，该参数不可为空
	 * @return 返回取到的用户集合
	 */
	Collection<IUser> loadUsersByDeptId(String deptId);
	
	/**
	 * 检查用户密码是否正确，如果不正确返回错误消息，如正确则返回null
	 * @param username 用户名
	 * @param password 要检查未加密的密码
	 * @return 不正确返回错误消息，如正确则返回null
	 */
	String checkPassword(String username,String password);
	
	/**
	 * 修改指定用户的密码信息
	 * @param username 用户名
	 * @param newPassword 新密码
	 */
	void changePassword(String username,String newPassword);
	
	/**
	 * 注册一个系统管理员账号
	 * @param username 用于登录的用户名
	 * @param cname 中文名
	 * @param ename 英文名
	 * @param password 密码
	 * @param mobile 手机号	
	 * @param email 电子邮件
	 * @param companyId 所在公司ID
	 */
	void registerAdministrator(String username,String cname,String ename,String password,String email,String mobile,String companyId);
	
	/**
	 * 根据用户名，实现化一个空的用户对象供系统使用，实例化的用户对象，只需要将给定的用户名填充进去即可
	 * @param username 用户名
	 * @return 实例化后的用户对象
	 */
	IUser newUserInstance(String username);
}
