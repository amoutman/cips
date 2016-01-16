package com.cips.service;

import java.util.List;

import com.cips.model.Role;
import com.cips.model.UserRole;

public interface RoleService {
	public List<Role> getRoleList();
	
	public void insertRole(Role role);
	
	public void updateRole(Role role);
	
	public void deleteRoleById(String roleId);
	
	public Role selectRoleById(String roleId);
	
	public List<Role> getRoleListByUserId(String userId);
	
	public Role selectRoleByName(String roleName);

	public void insertUserRoleList(List<UserRole> urList);
    
    public void deleteUserRoleByUserId(String userId);
    
    public List<UserRole> getUserRoleListByUserId(String userId);
	
}
