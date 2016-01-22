package com.cips.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class DownloadUtil {
	public ResponseEntity<byte[]> download(String filePath, String fileName) throws IOException {
		File file = new File(filePath);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		headers.setContentDispositionFormData("attachment", fileName);
		return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file), headers, HttpStatus.CREATED);
	}

	public void download(String filePath, String fileName, HttpServletResponse response) throws IOException {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		response.setContentType("multipart/form-data");
		String userAgent = request.getHeader("USER-AGENT");
		try {
			String finalFileName = null;
			if (userAgent.toLowerCase().indexOf("firefox") > 0) {// google,火狐浏览器
				finalFileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
			} else {
				finalFileName = URLEncoder.encode(fileName, "UTF-8");// 其他浏览器
			}
			response.addHeader("Content-Disposition", "attachment;filename=" + finalFileName);
		} catch (UnsupportedEncodingException e) {

		}

		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		File file = new File(filePath);
		long fileLength = file.length();
		response.setHeader("Content-Length", String.valueOf(fileLength));
		try {
			bis = new BufferedInputStream(new FileInputStream(filePath));
			bos = new BufferedOutputStream(response.getOutputStream());

			int b = 0;
			byte[] buffer = new byte[2048];
			while (-1 != (b = bis.read(buffer, 0, buffer.length))) {

				bos.write(buffer, 0, b);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (bis != null) {
				bis.close();
			}
			if (bos != null) {
				bos.close();
			}
		}
	}
}
