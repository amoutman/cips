package com.cips.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cips.dao.AccountFrMapper;
import com.cips.model.AccountFr;

@Service("accountFrService")
public class AccountFrServiceImpl implements AccountFrService {
	
	private AccountFrMapper accountFrMapper;

	@Autowired
	public void setAccountFrMapper(AccountFrMapper accountFrMapper) {
		this.accountFrMapper = accountFrMapper;
	}

	@Override
	public List<AccountFr> getAccountFrList(Map<String,Object> map) {
		// TODO Auto-generated method stub
		return accountFrMapper.toPageGetAccountFrList(map);
	}

	@Override
	public AccountFr getAccountFrById(String id) {
		// TODO Auto-generated method stub
		return accountFrMapper.selectByPrimaryKey(id);
	}

	@Override
	public void insertAccountFr(AccountFr af) {
		// TODO Auto-generated method stub
		accountFrMapper.insertSelective(af);
	}

	@Override
	public void updateAccountFr(AccountFr fr) {
		// TODO Auto-generated method stub
		accountFrMapper.updateByPrimaryKeySelective(fr);
	}

	@Override
	public void deleteAccountFrById(String id) {
		// TODO Auto-generated method stub
		accountFrMapper.deleteByPrimaryKey(id);
	}
	
	public AccountFr getAccountFrByCode(String accountCode){
		return accountFrMapper.getAccountFrByCode(accountCode);
	}

}
