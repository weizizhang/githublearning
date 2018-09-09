package com.tom.app.eip.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.tom.app.eip.model.Portal;
import com.tom.app.eip.service.DemoService;
import com.tom.app.eip.util.FileUtil;
@Service
public class DemoServiceImpl implements DemoService {

	@Override
	public String download(String id, String filePath, String fileName) {
		String msg = "";
		Portal portal = new Portal();
		portal.setServiceId("portalRegister");
		portal.setServiceName("服务注册");
		portal.setName("portalRegister");
		portal.setDescript("服务注册功能");
		portal.setVersion("1.0.0");
		portal.setAuthor("作者");
		
		// config.js内容
		List<String> config = new ArrayList<String>();
		config.add("module.exports = {");
		config.add("\t// 配置微服务应用的id，只允许使用字母和数字，注意要不要和其他微服务id重复");
		config.add("\tSERVICEID: '{"+portal.getServiceId()+"}',");
		config.add("\tSERVICENAME: '{"+portal.getServiceName()+"}'");
		config.add("}");
		// package.json内容
		List<String> pack = new ArrayList<String>();
		pack.add("{");
		pack.add("\t\"name\": \""+portal.getName()+"\",");
		pack.add("\t\"description\": \""+portal.getDescript()+"\",");
		pack.add("\t\"version\": \""+portal.getVersion()+"\",");
		pack.add("\t\"author\": \""+portal.getAuthor()+"\",");
		pack.add("\t\"license\": \"ISC\",");
		
		try {
			//创建文件夹
			File dir = new File(filePath);
			if(dir.exists()) {
				// 删除临时文件夹内文件
				boolean flag = FileUtil.deleteDir(dir);
				System.out.println(flag);
			}
			dir.mkdirs();
			// 创建文件
			File configJs = new File(filePath+"\\config.js");
			if(!configJs.exists()) {
				configJs.createNewFile();
			}
			File packageJson = new File(filePath+"\\package.json");
			if(!packageJson.exists()) {
				packageJson.createNewFile();
			}
			
			//写入内容
			FileUtil.writeFile(filePath+"\\config.js", config);
			FileUtil.writeFile(filePath+"\\package.json", pack);
			
			//压缩文件
			msg = FileUtil.fileToZip(filePath, filePath, fileName);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return msg;
	}

}
