package com.cips.dao;

import java.util.List;
import java.util.Map;

import com.cips.model.User;

public interface UserMapper {
    int deleteByPrimaryKey(String id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    User loginUser(User user);
    
    List<User> toPageGetUserList(Map<String,Object> map);
    
    List<User> toPageGetFrUserList(Map<String,Object> map);
    
    User getUserByUserInfo(Map<String,Object> map);
}