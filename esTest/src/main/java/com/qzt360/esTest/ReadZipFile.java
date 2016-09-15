package com.qzt360.esTest;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import org.apache.log4j.Logger;

public class ReadZipFile {
	private Logger logger = Logger.getLogger(ReadZipFile.class);

	/**
	 * 读取zip文件，遍历一级目录中的文件，逐行显示内容<br>
	 * 压缩命令：zip abc.txt.zip abc.txt<br>
	 * 解压命令：
	 * 
	 * @param strFilePath
	 * @throws Exception
	 */
	public void readZipFile(String strFilePath) throws Exception {
		ZipFile zf = new ZipFile(strFilePath);
		InputStream in = new BufferedInputStream(new FileInputStream(strFilePath));
		ZipInputStream zin = new ZipInputStream(in);
		ZipEntry ze;
		while ((ze = zin.getNextEntry()) != null) {
			if (ze.isDirectory()) {
			} else {
				logger.info("file - " + ze.getName() + " : " + ze.getSize() + " bytes");
				long size = ze.getSize();
				if (size > 0) {
					BufferedReader br = new BufferedReader(new InputStreamReader(zf.getInputStream(ze)));
					String line;
					while ((line = br.readLine()) != null) {
						logger.info(line);
					}
					br.close();
				}
			}
		}
		zin.closeEntry();
	}

	/**
	 * 读取gzip文件，遍历一级目录中的文件，逐行显示内容<br>
	 * 压缩命令：gzip -c abc.txt > abc.txt.gz<br>
	 * 解压命令：gunzip -c abc.txt.gz > abc.txt<br>
	 * 
	 * @param strFilePath
	 * @throws Exception
	 */
	public void readGZipFile(String strFilePath) throws Exception {
		BufferedReader br = new BufferedReader(
				new InputStreamReader(new GZIPInputStream(new FileInputStream(strFilePath))));
		String line;
		while ((line = br.readLine()) != null) {
			logger.info(line);
		}
		br.close();
	}
}
