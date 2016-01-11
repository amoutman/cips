package com.cips.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set; 

public final class PropertiesUtil {
		
	public static Map<String, String> loadPropertiesMap = new HashMap<String, String>();
	
	public static void loadFile(String filePath) {  
		  
        if (null == filePath || "".equals(filePath.trim())) {  
            System.out.println("The file path is null,return");  
            return;  
        }  
        filePath = filePath.trim();  
        // 获取资源文件  
        InputStream is = PropertiesUtil.class.getClassLoader().getResourceAsStream(filePath);  
        // 属性列表  
        Properties prop = new Properties();  
        try {  
            // 从输入流中读取属性列表  
            prop.load(is);  
        } catch (IOException e) {  
            System.out.println("load file faile." + e);  
            return;  
        } catch (Exception e) {  
            System.out.println("load file faile." + e);  
            return;  
        }  
  
        // 返回Properties中包含的key-value的Set视图  
        Set<Entry<Object, Object>> set = prop.entrySet();  
        // 返回在此Set中的元素上进行迭代的迭代器  
        Iterator<Map.Entry<Object, Object>> it = set.iterator();  
        String key = null, value = null;  
        // 循环取出key-value  
        while (it.hasNext()) {  
  
            Entry<Object, Object> entry = it.next();  
  
            key = String.valueOf(entry.getKey());  
            value = String.valueOf(entry.getValue());  
  
            key = key == null ? key : key.trim();  
            value = value == null ? value : value.trim();  
            // 将key-value放入map中  
            loadPropertiesMap.put(key, value);  
        }  
    }
	
	public static String getPropertiesValue(String key){
		return loadPropertiesMap.get(key);
	}
}
