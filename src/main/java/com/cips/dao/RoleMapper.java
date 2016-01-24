package com.cips.dao;

import java.util.List;
import java.util.Map;

import com.cips.model.Role;

public interface RoleMapper {
    int deleteByPrimaryKey(String id);

    int insert(Role record);

    int insertSelective(Role record);

    Role selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Role record);

    int updateByPrimaryKey(Role record);
    
    List<Role> toPageGetRoleList(Map<String,Object> param);
    
    List<Role> getRoleListByUserId(String userId);
    
    Role selectRoleByName(String roleName);
    
    List<Role> getRoleList();
}