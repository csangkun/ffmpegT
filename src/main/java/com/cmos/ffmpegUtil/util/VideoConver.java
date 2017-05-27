package com.cmos.ffmpegUtil.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
/**
 * 同步视频转码
 * @author Administrator
 *
 */
public class VideoConver {
   
    
    /**
     * @param filePath
     * @param outPath
     * @return
     */
    public static boolean beginConver(String filePath,String newFilepath) {
        if (!checkfile(filePath)) {
            System.out.println(filePath + "文件不存在" + " ");
            return false;
        }
        
        deleteFile(newFilepath);//如果要转换的视频存在则删除
        if (process(filePath,newFilepath)) {
            return true;
        } else {
        	filePath = null;
            return false;
        }
    }
  
   
  
    private static boolean process(String filePath,String newFilepath) {
        int type = checkContentType( filePath);
        boolean status = false;
        if (type == 0) {
            status = processH264(filePath ,newFilepath);
        }  
        return status;
    }
  
    private static int checkContentType(String filePath) {
        String type = filePath.substring(filePath.lastIndexOf(".") + 1, filePath.length()).toLowerCase();
        // ffmpeg能解析的格式：（asx，asf，mpg，wmv，3gp，mp4，mov，avi，flv等）
        if (type.equals("avi")) {
            return 0;
        } else if (type.equals("mpg")) {
            return 0;
        } else if (type.equals("wmv")) {
            return 0;
        } else if (type.equals("3gp")) {
            return 0;
        } else if (type.equals("mov")) {
            return 0;
        } else if (type.equals("mp4")) {
            return 0;
        } else if (type.equals("asf")) {
            return 0;
        } else if (type.equals("asx")) {
            return 0;
        } else if (type.equals("flv")) {
            return 0;
        }
        // 对ffmpeg无法解析的文件格式(wmv9，rm，rmvb等),
        // 可以先用别的工具（mencoder）转换为avi(ffmpeg能解析的)格式.
        else if (type.equals("wmv9")) {
            return 1;
        } else if (type.equals("rm")) {
            return 1;
        } else if (type.equals("rmvb")) {
            return 1;
        }
        return 9;
    }
  
    private static boolean checkfile(String path) {
        File file = new File(path);
        if (!file.isFile()) {
            return false;
        } else {
            return true;
        }
    }
    
    /**
     * 删除文件
     * @param filepath
     */
    public static void deleteFile(String filepath) {
        File file = new File(filepath);
        if(file.exists()){
        	file.delete();
        }
    }
  
   
    
    // ffmpeg能解析的格式：（asx，asf，mpg，wmv，3gp，mp4，mov，avi，flv等）
    private static boolean processH264(String oldFilepath,String newFilepath) {
  
        if (!checkfile(oldFilepath)) {
            System.out.println(oldFilepath + " is not file");
            return false;
        }
        List <String>commend = new ArrayList<String>();
        commend.add(Constants.FFMPEG_HOMEPATH);
        commend.add("-i");
        commend.add(oldFilepath);
        commend.add("-ac");
        commend.add("1");
        commend.add("-vcodec");
        commend.add("libx264");
        commend.add(newFilepath);
        try {
            ProcessBuilder builder = new ProcessBuilder();
            builder.command(commend);
            Process p = builder.start();
            doWaitFor(p);
            p.destroy();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
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
