package com.cips.service;

import java.util.List;
import java.util.Map;

import com.cips.model.AccountFr;

public interface AccountFrService {
	public List<AccountFr> getAccountFrList(Map<String,Object> map);
	
	public AccountFr getAccountFrById(String id);
	
	public void insertAccountFr(AccountFr af);
	
	public void updateAccountFr(AccountFr fr);
	
	public void deleteAccountFrById(String id);
	
	public AccountFr getAccountFrByCode(String accountCode);
	
}
