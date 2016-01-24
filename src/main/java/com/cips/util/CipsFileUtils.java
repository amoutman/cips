package com.cips.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class CipsFileUtils {
	/**
	 * 文件复制
	 * */
	public static void copyFile(File file,String targetPath){
		if(file == null){
			return;
		}
		InputStream is = null;
		BufferedInputStream bis = null;
		OutputStream os = null;
		BufferedOutputStream bos = null;
		
		try {
			is = new FileInputStream(file);
			bis = new BufferedInputStream(is);
			
			os = new FileOutputStream(targetPath);
			bos = new BufferedOutputStream(os);
			
			byte[] bs = new byte[1024];
			int size = -1;
			while((size = is.read(bs))>-1){
				bos.write(bs, 0, size);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			close(bis,bos);
		}
		
	}
	
	public static void close(InputStream is,OutputStream os){
		if(is != null){
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if(os != null){
			try {
				os.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}

}
