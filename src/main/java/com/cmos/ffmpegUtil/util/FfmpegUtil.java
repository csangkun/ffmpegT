package com.cmos.ffmpegUtil.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * 
 *
 */
public class FfmpegUtil {

	/**
	 *  同步截取图片方法
	 * @param ffmpegHomePath ffmpegPath bin目录
	 * @param voidPath       视频路径
	 * @param image_path     图片输出路径
	 * @param cutTime        截图视频时间点
	 * @return  boolean      如果是true则截图成功，否则失败
	 */
	public static boolean cutImage(String ffmpegHomePath, String voidPath, String image_path, String cutTime) {
	    ProcessBuilder builder = new ProcessBuilder();
		Process process=null;
		List<String> commands = new ArrayList<String>();
		commands.add(ffmpegHomePath);
		commands.add("-ss");// 跳到关键帧命令
		commands.add(cutTime);// 这个参数是设置截取视频多少秒时的画面
		commands.add("-i");// filename 指定输入文件名
		commands.add(voidPath);// 视频路径
		commands.add("-f");// 指定格式(音频或视频格式)
		commands.add("mjpeg");// 图片格式
		commands.add("-y");// 覆盖原有图片
		commands.add(image_path);// 图片输出路径
		try {
			  
			builder.command(commands);// 执行截图
			process= builder.start();
			doWaitFor(process);
			process.destroy();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}finally{
			if(  null != process && process.exitValue() != 0) {  
	            process.destroy();
	        }
		}
	}

	/**
	 * 获取视频总时间
	 * @param   ffmpegPath ffmpeg路径
	 * @param   videoPath  视频路径    
	 * @return  时长                   int类型          
	 */
	public static int getVideoTime(String videoPath, String ffmpegPath) {
		List<String> commands = new java.util.ArrayList<String>();
		BufferedReader br = null;
		commands.add(ffmpegPath);
		commands.add("-i");
		commands.add(videoPath);
		try {
			ProcessBuilder builder = new ProcessBuilder();
			builder.command(commands);
			final Process p = builder.start();
			// 从输入流中读取视频信息
			br = new BufferedReader(new InputStreamReader(p.getErrorStream()));
			StringBuffer sb = new StringBuffer();
			String line = "";
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
			br.close();
			// 从视频信息中解析时长
			String regexDuration = "Duration: (.*?), start: (.*?), bitrate: (\\d*) kb\\/s";
			Pattern pattern = Pattern.compile(regexDuration);
			Matcher m = pattern.matcher(sb.toString());
			if (m.find()) {
				int time = getTimelen(m.group(1));
				return time;
			}
		} catch (Exception e) {
			 // TODO 输出日志信息
		} finally {
			if (null != br) {
				try {
					br.close();
				} catch (IOException e) {
					// TODO 输出日志信息
				}
			}
		}
		return 0;
	}

	private static int getTimelen(String timelen) {
		int min = 0;
		String strs[] = timelen.split(":");
		if (strs[0].compareTo("0") > 0) {
			min += Integer.valueOf(strs[0]) * 60 * 60;// 秒
		}
		if (strs[1].compareTo("0") > 0) {
			min += Integer.valueOf(strs[1]) * 60;
		}
		if (strs[2].compareTo("0") > 0) {
			min += Math.round(Float.valueOf(strs[2]));
		}
		return min;
	}
	
	public static int doWaitFor(Process p) {
        InputStream in = null;
        InputStream err = null;
        int exitValue = -1;  
        try {
            System.out.println("comeing");
            in = p.getInputStream();
            err = p.getErrorStream();
            boolean finished = false;  
  
            while (!finished) {
                try {
                    while (in.available() > 0) {
                        Character c = new Character((char) in.read());
                        System.out.print(c);
                    }
                    while (err.available() > 0) {
                        Character c = new Character((char) err.read());
                        System.out.print(c);
                    }
  
                    exitValue = p.exitValue();
                    finished = true;
  
                } catch (IllegalThreadStateException e) {
                   //-----------------打印log4j日志
                }
            }
        } catch (Exception e) {
        	//-----------------打印log4j日志
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
  
            } catch (IOException e) {
            	//-----------------打印log4j日志
            }
            if (err != null) {
                try {
                    err.close();
                } catch (IOException e) {
                	//-----------------打印log4j日志
                }
            }
        }
        return exitValue;
    }
}
