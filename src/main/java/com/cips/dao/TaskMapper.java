package com.cips.dao;

import java.util.List;
import java.util.Map;

import com.cips.model.Task;

public interface TaskMapper {
    int deleteByPrimaryKey(String id);

    int insert(Task record);

    int insertSelective(Task record);

    Task selectByPrimaryKey(String id);

    int updateByPrimaryKey(Task record);
    
    List<Task> toPageTaskListByParams(Map<String,Object> params);
}