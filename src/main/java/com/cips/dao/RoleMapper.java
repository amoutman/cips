package com.cips.dao;

import java.util.List;

import com.cips.model.Role;

public interface RoleMapper {
    int deleteByPrimaryKey(String id);

    int insert(Role record);

    int insertSelective(Role record);

    Role selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Role record);

    int updateByPrimaryKey(Role record);
    
    List<Role> getRoleList();
    
    List<Role> getRoleListByUserId(String userId);
    
    Role selectRoleByName(String roleName);
}