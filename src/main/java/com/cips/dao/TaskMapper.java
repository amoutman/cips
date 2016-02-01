package com.cips.dao;

import java.util.List;
import java.util.Map;

import com.cips.model.Task;

public interface TaskMapper {
    int deleteByPrimaryKey(String id);

    int insert(Task record);

    int insertSelective(Task record);

    Task selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Task record);

    int updateByPrimaryKey(Task record);
    
    List<Task> toPageTaskListByParams(Map<String,Object> params);
    
    Task getTaskByParams(Map<String, Object> params) throws Exception;
    
    List<Task> getTasksByParams(Map<String, Object> params) throws Exception;
    
    Integer getTaskNum(Map<String, Object> params);
}