package com.cips.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cips.dao.MenuMapper;
import com.cips.model.Menu;

@Service("menuService")
public class MenuServiceImpl implements MenuService {
	
	private MenuMapper menuMapper;

	@Autowired
	public void setMenuMapper(MenuMapper menuMapper) {
		this.menuMapper = menuMapper;
	}

	@Override
	public List<Menu> getMenuListByRoleId(Map<String,Object> map) {
		// TODO Auto-generated method stub
		return menuMapper.getMenuListByRoleId(map);
	}

}
