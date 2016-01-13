package com.cips.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cips.dao.UserMapper;
import com.cips.model.User;

@Service("userService")
public class UserServiceImpl implements UserService {
	
	private UserMapper userMapper;

	@Autowired
	public void setUserMapper(UserMapper userMapper) {
		this.userMapper = userMapper;
	}

	@Override
	public User loginUser(User user) {
		// TODO Auto-generated method stub
		return userMapper.loginUser(user);
	}

	@Override
	public void updateUser(User user) {
		// TODO Auto-generated method stub
		userMapper.updateByPrimaryKeySelective(user);
	}

	@Override
	public void deleteUser(String userId) {
		// TODO Auto-generated method stub
		userMapper.deleteByPrimaryKey(userId);
	}

	@Override
	public void insertUser(User user) {
		// TODO Auto-generated method stub
		userMapper.insert(user);
	}

	@Override
	public List<User> getUserList(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return userMapper.toPageGetUserList(map);
	}
	
	public List<User> selectUserInfo(String userInfo){
		return userMapper.toPageSelectUserInfo(userInfo);
	}

}
