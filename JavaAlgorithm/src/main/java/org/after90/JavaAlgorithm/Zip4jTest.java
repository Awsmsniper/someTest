package org.after90.JavaAlgorithm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.model.FileHeader;

public class Zip4jTest {

	public static void main(String[] args) {
		(new Zip4jTest()).readZipFile();
	}

	private void readZipFile() {
		ZipFile zipFile = null;
		BufferedReader br = null;
		try {
			zipFile = new ZipFile("/Users/zhaogj/tmp/zip4j/userInfo_1470726737_iadh5u.zip");
			if (zipFile.isValidZipFile()) {
				zipFile.setPassword("Crm2016ss");
				zipFile.extractAll("/Users/zhaogj/tmp/zip4j/tmp/");
				for (FileHeader fileHeader : (List<FileHeader>) zipFile.getFileHeaders()) {
					System.out.println("filename: " + fileHeader.getFileName());
					br = new BufferedReader(new InputStreamReader(zipFile.getInputStream(fileHeader)));
					String strLine = null;
					while ((strLine = br.readLine()) != null) {
						System.out.println(strLine);
					}
				}
			} else {
				System.out.println("压缩文件不合法,可能被损坏 ");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (Exception e) {
					System.out.println(e);
				}
			}
			zipFile = null;
		}
	}
}
