package com.cmos.ffmpegUtil.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

public class VedioInfo {

	
    /**
     * 获取视频信息以json格式返回  
     * @param ffprobePath  Windows上ffprobe.exe的路径，liunx
     * @param filePath 获取视频信息的路径
     * @return
     */
	public static String getVedioInfo(String ffprobePath, String filePath) {
		String cmd = ffprobePath+ " -v quiet -print_format json  -show_streams  " + filePath;
		BufferedInputStream in = null;
		BufferedReader inBr = null;
 		try {
			Runtime run = Runtime.getRuntime();
			Process p = run.exec(cmd);
			in = new BufferedInputStream(p.getInputStream());
			inBr = new BufferedReader(new InputStreamReader(in));
			StringBuffer sb = new StringBuffer();
			String lineStr;
			while ((lineStr = inBr.readLine()) != null)
				sb.append(lineStr);
			if (p.waitFor() != 0) {
				if (p.exitValue() == 1)
					System.err.println("命令执行失败!" +filePath);
			}
			inBr.close();
			in.close();
			return sb.toString();
		} catch (Exception e) {
			     //----------------------打印log4j日志日志
		}finally{
			if(null != inBr){
				try {
					inBr.close();
				} catch (IOException e) {
				 //----------------------打印log4j日志日志
				}
			}
			
			if(null != in){
				try {
					in.close();
				} catch (IOException e) {
					//----------------------打印log4j日志日志
				}
			}
		}
		return "";
	}
	
	/**
	 * 
	 * 0:可以直接播放
	 * 1:需要重新编码
	 * 2:视频为无效视频
	 * @param json
	 * @return
	 */
	public static int isHtml5vedio(String json) {
		@SuppressWarnings("unchecked")
		Map<String, Object> map = JsonUtil.convertJson2Object(json, Map.class);
		if (null == map) {
			return 2;
		}

		@SuppressWarnings("unchecked")
		List<Map<String, Object>> vediolist = (List<Map<String, Object>>) map.get("streams");
	 
		for (int i = 0; null != vediolist && i < vediolist.size(); i++) {
			String codec_name = (String) vediolist.get(i).get("codec_name");
			if(null != codec_name && !"".equals(codec_name)){
				if("h264".equals(codec_name.toLowerCase()) || "vp8".equals(codec_name.toLowerCase()) || "theora".equals(codec_name.toLowerCase()) ){
					return 0;
				}
			}else{
				return 2;
			}
		}
		return 1;
	}
}
