package com.cmos.ffmpegUtil;

import java.io.File;

import org.junit.Test;

import com.cmos.ffmpegUtil.util.Constants;
import com.cmos.ffmpegUtil.util.FfmpegUtil;

public class FfmpegUtilTest {
	@Test
	public void testCutImage() {
		long start =System.currentTimeMillis();
		String fileName ="G:/"+System.currentTimeMillis()+".jpg";
		FfmpegUtil.cutImage(Constants.FFMPEG_HOMEPATH,"G:/vedio1-conver/boss371108520170404103756219054_vedio.mp4",fileName, "1");
		System.out.println();
		File file=new File(fileName);
		System.out.println(file.exists());
		long end =System.currentTimeMillis();
		System.out.println(end-start);

	}
}
