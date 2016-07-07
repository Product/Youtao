package com.youyou.uumall.utils;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @author 任东卫
 * @version 1.0
 * @fileName FileUtils.java
 * @package com.immomo.momo.android.util
 * @description 文件工具类
 * @email 86930007@qq.com
 */
public class FileUtils {

    private static MyLogger log = MyLogger.getLogger("FileUtils");

    /**
     * 判断SD是否可以
     *
     * @return
     */
    public static boolean isSdcardExist() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
    }

    /**
     * 创建根目录
     *
     * @param path 目录路径
     */
    public static void createDirFile(String path) {
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    /**
     * 创建文件
     *
     * @param path 文件路径
     * @return 创建的文件
     */
    public static File createNewFile(String path) {
        File file = new File(path);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                return null;
            }
        }
        return file;
    }

    /**
     * 删除文件夹
     *
     * @param folderPath 文件夹的路径
     */
    public static void delFolder(String folderPath) {
        delAllFile(folderPath);
        String filePath = folderPath;
        File myFilePath = new File(folderPath);
        myFilePath.delete();
    }

    /**
     * 删除文件
     *
     * @param path 文件的路径
     */
    public static void delAllFile(String path) {
        File file = new File(path);
        if (!file.exists()) {
            return;
        }
        if (!file.isDirectory()) {
            return;
        }
        String[] tempList = file.list();
        File temp = null;
        for (int i = 0; i < tempList.length; i++) {
            if (path.endsWith(File.separator)) {
                temp = new File(path + tempList[i]);
            } else {
                temp = new File(path + File.separator + tempList[i]);
            }
            if (temp.isFile()) {
                temp.delete();
            }
            if (temp.isDirectory()) {
                delAllFile(path + "/" + tempList[i]);
                delFolder(path + "/" + tempList[i]);
            }
        }
    }

    /**
     * 获取文件的Uri
     *
     * @param path 文件的路径
     * @return
     */
    public static Uri getUriFromFile(String path) {
        File file = new File(path);
        return Uri.fromFile(file);
    }

    /**
     * 换算文件大小
     *
     * @param size
     * @return
     */
    public static String formatFileSize(long size) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        if (size < 1024) {
            fileSizeString = df.format((double) size) + "B";
            if (TextUtils.equals(".00B",fileSizeString)){
                fileSizeString = "0.00B";
            }
        } else if (size < 1048576) {
            fileSizeString = df.format((double) size / 1024) + "K";
        } else if (size < 1073741824) {
            fileSizeString = df.format((double) size / 1048576) + "M";
        } else {
            fileSizeString = df.format((double) size / 1073741824) + "G";
        }
        return fileSizeString;
    }

    /**
     * 创建对象文件
     *
     * @param context
     * @return
     */
    public static File createTmpFile(Context context) {

        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            // 已挂载
            File pic = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA).format(new Date());
            String fileName = "multi_image_" + timeStamp + "";
            File tmpFile = new File(pic, fileName + ".jpg");
            return tmpFile;
        } else {
            File cacheDir = context.getCacheDir();
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA).format(new Date());
            String fileName = "multi_image_" + timeStamp + "";
            File tmpFile = new File(cacheDir, fileName + ".jpg");
            return tmpFile;
        }
    }

    /**
     * 获取文件夹大小
     *
     * @param file File实例
     * @return long 单位为M
     * @throws Exception
     */
    public static long getFolderSize(File file) throws Exception {
        long size = 0;
        File[] fileList = file.listFiles();
        for (int i = 0; i < fileList.length; i++) {
            if (fileList[i].isDirectory()) {
                size = size + getFolderSize(fileList[i]);
            } else {
                size = size + fileList[i].length();
            }
        }
        return size;
    }

    /**
     * 删除指定目录下文件及目录
     *
     * @param deleteThisPath
     * @param filePath
     * @return
     */
    public void deleteFolderFile(String filePath, boolean deleteThisPath)
            throws IOException {
        if (!TextUtils.isEmpty(filePath)) {
            File file = new File(filePath);

            if (file.isDirectory()) {// 处理目录
                File files[] = file.listFiles();
                for (int i = 0; i < files.length; i++) {
                    deleteFolderFile(files[i].getAbsolutePath(), true);
                }
            }
            if (deleteThisPath) {
                if (!file.isDirectory()) {// 如果是文件，删除
                    file.delete();
                } else {// 目录
                    if (file.listFiles().length == 0) {// 目录下没有文件或者目录，删除
                        file.delete();
                    }
                }
            }
        }
    }


    /**
     * 调用此方法自动计算指定文件或指定文件夹的大小
     *
     * @param filePath 文件路径
     * @return 计算好的带B、KB、MB、GB的字符串
     */
    public static String getAutoFileOrFilesSize(String filePath) {
        File file = new File(filePath);
        long blockSize = 0;
        try {
            if (file.isDirectory()) {
                blockSize = getFolderSize(file);
            } else {
                blockSize = getFileSize(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.e("获取文件大小失败!");
        }
        return formatFileSize(blockSize);
    }

    /**
     * 获取指定文件大小
     *
     * @param file
     * @return
     * @throws Exception
     */
    private static long getFileSize(File file) throws Exception {
        long size = 0;
        if (file.exists()) {
            FileInputStream fis = null;
            fis = new FileInputStream(file);
            size = fis.available();
        } else {
            file.createNewFile();
            log.e("文件不存在!");
        }
        return size;
    }
}
