package com.cips.dao;

import java.util.List;
import java.util.Map;

import com.cips.model.AccountFr;

public interface AccountFrMapper {
    int deleteByPrimaryKey(String id);

    int insert(AccountFr record);

    int insertSelective(AccountFr record);

    AccountFr selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(AccountFr record);

    int updateByPrimaryKey(AccountFr record);
    
    List<AccountFr> toPageGetAccountFrList(Map<String,Object> map);
    
    AccountFr getAccountFrByCode(String accountCode);
}