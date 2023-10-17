package net.openio.opendb.tool;

import java.io.File;

public class FileUtils {

  public static void createDirectoryIfNotExists(String directoryPath) {
    File directory = new File(directoryPath);
    if (!directory.exists()) {
      boolean success = directory.mkdirs();
      if (success) {
        System.out.println("目录已创建：" + directoryPath);
      } else {
        System.err.println("无法创建目录：" + directoryPath);
      }
    }
  }


  public static void createFileIfNotExists(String filePath) {
    File file = new File(filePath);
    if (!file.exists()) {
      File parentDirectory = file.getParentFile();
      if (parentDirectory != null) {
        createDirectoryIfNotExists(parentDirectory.getPath());
      }

      try {
        boolean success = file.createNewFile();
        if (success) {
          System.out.println("文件已创建：" + filePath);
        } else {
          System.err.println("无法创建文件：" + filePath);
        }
      } catch (Exception e) {
        System.err.println("创建文件时出现异常：" + e.getMessage());
      }
    }
  }
}
