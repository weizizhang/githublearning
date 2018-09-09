package com.tom.app.eip.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tom.app.eip.service.DemoService;

@RestController
@RequestMapping("/demo")
public class DemoController {
	
	@Autowired
	private DemoService demoService;
	
	@RequestMapping(value="/download/{id}", method=RequestMethod.GET)
	public void download(@PathVariable("id") String id, HttpServletRequest request, HttpServletResponse response) {
		String filePath = System.getProperty("java.io.tmpdir")+id;
		String fileName = id+".zip";
		// 设置响应头和客户端保存文件名
		response.setCharacterEncoding("utf-8");
		response.setContentType("multipart/form-data");
		response.setHeader("Content-Disposition", "attachment;fileName=" + fileName);
		try {
			// 创建文件并压缩
			demoService.download(id, filePath, fileName);
			
			// 准备下载
			File file = new File(filePath+"\\"+fileName);
			// 打开本地文件流
			InputStream is = new FileInputStream(file);
			// 激活下载操作
			OutputStream os = response.getOutputStream();
			// 循环写入输出流
			byte[] b = new byte[2048];
			int length;
			while ((length = is.read(b)) > 0) {
				os.write(b, 0, length);
			}
			
			// 这里主要关闭。
			os.close();
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
