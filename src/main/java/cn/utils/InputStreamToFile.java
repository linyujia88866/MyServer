package cn.utils;

import java.io.*;

public class InputStreamToFile {
    public static void saveToFile(InputStream inStream, String targetPath) throws IOException {
        File file = new File(targetPath);
        if (!file.exists()) {
            file.createNewFile(); // 创建文件
        }

        try (OutputStream outStream = new FileOutputStream(file)) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, length);
            }
        }
    }

//    public static void main(String[] args) {
//        // 假设inputStream是已经获取到的InputStream对象
//        // 假设"path/to/local/file.txt"是你想要保存到的本地文件路径
//        InputStream inputStream = ...; // 获取InputStream实例
//        String targetPath = "path/to/local/file.txt";
//
//        try {
//            saveToFile(inputStream, targetPath);
//            System.out.println("文件保存成功！");
//        } catch (IOException e) {
//            e.printStackTrace();
//            System.out.println("文件保存失败！");
//        }
//    }
}