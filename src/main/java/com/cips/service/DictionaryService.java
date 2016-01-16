package com.cips.service;

import com.cips.model.Dictionary;

public interface DictionaryService {
	
	public void saveDictionary(Dictionary dictionary) throws Exception;
	
	public void updateDictionary(Dictionary dictionary) throws Exception;
	
	public Dictionary getDictionaryByCode(String code) throws Exception;
	
}
