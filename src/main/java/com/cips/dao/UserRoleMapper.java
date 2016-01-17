package com.cips.dao;

import java.util.List;

import com.cips.model.UserRole;

public interface UserRoleMapper {
    int deleteByPrimaryKey(String id);

    int insert(UserRole record);

    int insertSelective(UserRole record);

    UserRole selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(UserRole record);

    int updateByPrimaryKey(UserRole record);
    
    int insertUserRoleList(List<UserRole> urList);
    
    int deleteUserRoleByUserId(String userId);
    
    List<UserRole> getUserRoleListByUserId(String userId);
}