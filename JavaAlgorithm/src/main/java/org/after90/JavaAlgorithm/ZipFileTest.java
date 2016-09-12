package org.after90.JavaAlgorithm;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.util.zip.Adler32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ZipFileTest {
	public void zipMultiFile() {
		String strZipFile = "/Users/zhaogj/tmp/zipFile/dest.zip";
		String strScpFile1 = "123.scp";
		String strContent1 = "this is content.1";
		String strScpFile2 = "234.scp";
		String strContent2 = "this is content.2";

		try {
			FileOutputStream fos = new FileOutputStream(strZipFile);
			CheckedOutputStream cos = new CheckedOutputStream(fos, new Adler32());
			ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(cos));
			ZipEntry entry = new ZipEntry(strScpFile1);
			out.putNextEntry(entry);
			out.write(strContent1.getBytes());
			out.write(strContent1.getBytes());
			entry = new ZipEntry(strScpFile2);
			out.putNextEntry(entry);
			out.write(strContent2.getBytes());
			out.close();
		} catch (Exception e) {
			log.error("", e);
		}
	}
}
