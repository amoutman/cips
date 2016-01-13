package com.cips.service;

import java.util.List;
import java.util.Map;

import com.cips.model.Menu;

public interface MenuService {
	List<Menu> getMenuListByRoleId(Map<String,Object> map);

}
