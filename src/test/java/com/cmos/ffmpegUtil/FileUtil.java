package com.cmos.ffmpegUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {
	 
	public static List<File> getFilesByPath(File f) {
		File[] f1 = new File[] {};
		List<File> list = new ArrayList<File>();
		if (f.isDirectory()) {
			f1 = f.listFiles();
			int len = f1.length;
			for (int i = 0; i < len; i++) {
				list.add(f1[i]);
			}
		}
		return list;
	}
}
