package com.cips.dao;

import java.util.List;
import java.util.Map;

import com.cips.model.Menu;

public interface MenuMapper {
    int deleteByPrimaryKey(String id);

    int insert(Menu record);

    int insertSelective(Menu record);

    Menu selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Menu record);

    int updateByPrimaryKey(Menu record);
    
    List<Menu> getMenuListByRoleId(Map<String,Object> map);
}