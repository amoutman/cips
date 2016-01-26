package com.cips.dao;

import com.cips.model.TaskCert;

public interface TaskCertMapper {
    int deleteByPrimaryKey(String id);

    int insert(TaskCert record);

    int insertSelective(TaskCert record);

    TaskCert selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(TaskCert record);

    int updateByPrimaryKey(TaskCert record);
}