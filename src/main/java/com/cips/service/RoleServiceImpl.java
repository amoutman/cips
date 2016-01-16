package com.cips.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cips.dao.RoleMapper;
import com.cips.dao.UserRoleMapper;
import com.cips.model.Role;
import com.cips.model.UserRole;

@Service("roleService")
public class RoleServiceImpl implements RoleService {

	private RoleMapper roleMapper;
	
	private UserRoleMapper userRoleMapper;
	
	@Autowired
	public void setUserRoleMapper(UserRoleMapper userRoleMapper) {
		this.userRoleMapper = userRoleMapper;
	}

	@Autowired
	public void setRoleMapper(RoleMapper roleMapper) {
		this.roleMapper = roleMapper;
	}
	
	@Override
	public List<Role> getRoleList() {
		return roleMapper.getRoleList();
	}

	@Override
	public void insertRole(Role role) {
		roleMapper.insertSelective(role);
	}

	@Override
	public void updateRole(Role role) {
		roleMapper.updateByPrimaryKeySelective(role);
	}

	@Override
	public void deleteRoleById(String roleId) {
		roleMapper.deleteByPrimaryKey(roleId);
	}

	@Override
	public Role selectRoleById(String roleId) {
		return roleMapper.selectByPrimaryKey(roleId);
	}
	
	public List<Role> getRoleListByUserId(String userId){
		return roleMapper.getRoleListByUserId(userId);
	}
	
	public void insertUserRoleList(List<UserRole> urList){
		userRoleMapper.insertUserRoleList(urList);
	}
    
    public void deleteUserRoleByUserId(String userId){
    	userRoleMapper.deleteUserRoleByUserId(userId);
    }
    
    public List<UserRole> getUserRoleListByUserId(String userId){
    	return userRoleMapper.getUserRoleListByUserId(userId);
    }


	@Override
	public Role selectRoleByName(String roleName) {
		return roleMapper.selectRoleByName(roleName);
	}

}
