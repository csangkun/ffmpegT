package com.cmos.ffmpegUtil;

import java.io.File;
import java.util.List;

import org.junit.Test;

import com.cmos.ffmpegUtil.util.VideoConver;

public class VideoConverTest {
    @Test
	public void testVedioInfo() {
		List<File> list = FileUtil.getFilesByPath(new File("G://vedio1"));// 获取要处理的文件t
		for (int i = 0; i < list.size(); i++) {
		    String filerealname = list.get(i).getName().substring(0, list.get(i).getName().lastIndexOf(".")).toLowerCase();
		    String newFileName=filerealname+".mp4";
			VideoConver.beginConver(list.get(i).getAbsolutePath(), "G://vedio1-conver/"+newFileName);
		}
    }
}
