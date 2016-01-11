package com.cips.dao;

import com.cips.model.AccountFr;

public interface AccountFrMapper {
    int deleteByPrimaryKey(String id);

    int insert(AccountFr record);

    int insertSelective(AccountFr record);

    AccountFr selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(AccountFr record);

    int updateByPrimaryKey(AccountFr record);
}