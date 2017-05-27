package com.cmos.ffmpegUtil;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.cmos.ffmpegUtil.util.JsonUtil;
import com.cmos.ffmpegUtil.util.VedioInfo;

public class VedioInfoTest {
	/**
	 * 获取整个目录信息
	 */
    @Test
	public void testVedioInfo() {
		List<File> list = FileUtil.getFilesByPath(new File("C:/Users/Administrator/Desktop/1111"));// 获取要处理的文件t
		for (int i = 0; i < list.size(); i++) {
			//-------需要配置路径
			String json = VedioInfo.getVedioInfo("D:/Program Files/ffmpeg/bin/ffprobe.exe",list.get(i).getAbsolutePath());
			@SuppressWarnings("unchecked")
			Map<String, Object> map = JsonUtil.convertJson2Object(json,Map.class);
			if (null != map) {
				@SuppressWarnings("unchecked")
				List<Map<String, Object>> vediolist = (List<Map<String, Object>>) map
						.get("streams");
				if (null == vediolist || vediolist.size() < 1) {
					System.out.println(json+"@@@@"+list.get(i).getAbsolutePath());
				} else {
					 System.out.println(vediolist.get(0).get("codec_name")+"---"+vediolist.get(1).get("codec_name") +"::" +list.get(i).getAbsolutePath());
				}
			}
		}
	}
    
    @Test
	public void testVedioInfo1() {
			String json = VedioInfo.getVedioInfo("D:/Program Files/ffmpeg/bin/ffprobe.exe","C:/Users/Administrator/Desktop/1111/108520170307111227784241.mp4");
		    System.out.println(json);
			int i=VedioInfo.isHtml5vedio(json);
		    System.out.println("----"+i);
			 
	}
}
