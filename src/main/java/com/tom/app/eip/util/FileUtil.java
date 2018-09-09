package com.tom.app.eip.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class FileUtil {

	public static void readFile(String filePath) throws IOException {
		File file = new File(filePath);
		FileInputStream fis = new FileInputStream(file);
		InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
		BufferedReader br = new BufferedReader(isr);
		// 简写如下
		// BufferedReader br = new BufferedReader(new InputStreamReader(
		// new FileInputStream("E:/phsftp/evdokey/evdokey_201103221556.txt"), "UTF-8"));
		String line = "";
		while ((line = br.readLine()) != null) { //一行一行读取
			System.out.println(line);
		}
		br.close();
		isr.close();
		fis.close();
	}

	public static void writeFile(String filePath, List<String> content) throws IOException {
		File file = new File(filePath);
		// 写入中文字符时解决中文乱码问题
		FileOutputStream fos = new FileOutputStream(file);
		OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
		BufferedWriter bw = new BufferedWriter(osw);
		// 简写如下：
		// BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
		// new FileOutputStream(new File("E:/phsftp/evdokey/evdokey_201103221556.txt")),
		// "UTF-8"));

		for (String str : content) { //一行一行写入
			bw.write(str + "\n");
		}

		// 注意关闭的先后顺序，先打开的后关闭，后打开的先关闭
		bw.close();
		osw.close();
		fos.close();
	}

	public static String fileToZip(String sourceFilePath, String zipFilePath, String fileName) {
		String msg = "";
		File sourceFile = new File(sourceFilePath);
		FileInputStream fis = null;
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		FileOutputStream fos = null;
		ZipOutputStream zos = null;

		if (!sourceFile.exists()) {
			msg = "待压缩的文件目录：" + sourceFilePath + "不存在.";
		} else {
			try {
				File zipFile = new File(zipFilePath + "/" + fileName);
				if (zipFile.exists()) {
					msg = zipFilePath + "目录下存在名字为:" + fileName + "压缩文件.";
				} else {
					File[] sourceFiles = sourceFile.listFiles();
					if (sourceFiles == null || sourceFiles.length < 1) {
						msg = "待压缩的文件目录：" + sourceFilePath + "里面不存在文件，无需压缩.";
					} else {
						fos = new FileOutputStream(zipFile);
						bos = new BufferedOutputStream(fos);
						zos = new ZipOutputStream(bos);
						byte[] bufs = new byte[1024 * 2];
						for (int i = 0; i < sourceFiles.length; i++) {
							// 创建ZIP实体，并添加进压缩包
							ZipEntry zipEntry = new ZipEntry(sourceFiles[i].getName());
							zos.putNextEntry(zipEntry);
							// 读取待压缩的文件并写进压缩包里
							fis = new FileInputStream(sourceFiles[i]);
							bis = new BufferedInputStream(fis, 1024 * 10);
							int read = 0;
							while ((read = bis.read(bufs, 0, 1024 * 2)) != -1) {
								zos.write(bufs, 0, read);
							}
							if (fis != null)
								fis.close();
							if (bis != null)
								bis.close();
						}
					}
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			} catch (IOException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			} finally {
				// 关闭流
				try {
					if (zos != null)
						zos.close();
					if (bos != null)
						bos.close();
					if (fos !=null)
						fos.close();
				} catch (IOException e) {
					e.printStackTrace();
					throw new RuntimeException(e);
				}
			}
		}
		return msg;
	}
	
	public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            //递归删除目录中的子目录下
            for (int i=0; i<children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        // 目录此时为空，可以删除
        return dir.delete();
    }
	
	public static void main(String[] args) {
//		List<String> content = new ArrayList<String>();
//		content.add("{");
//		content.add("\t"+"SERVICEID:"+"abc");
//		content.add("\t"+"SERVICENAME:"+"注册");
//		content.add("}");
//		String filePath = "D:\\\\zip\\config.js";
//		try {
//			writeFile(filePath, content);
//			readFile(filePath);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		
//		String sourceFilePath = "D:\\\\zip\\1001";
//		String zipFilePath = "D:\\\\zip\\1001";
//		String fileName = "a100";
//		fileToZip(sourceFilePath, zipFilePath, fileName);
		
//		System.out.println(System.getProperty("java.io.tmpdir"));
		
		File file = new File(System.getProperty("java.io.tmpdir")+"181001");
		if(file.exists()) {
			System.out.println(deleteDir(file));
		}
	}

}
