package cn.utils;

public class StringUtils {
    public static String getPathSeparator() {
        if (System.getProperty("os.name").toLowerCase().contains("windows")) {
            return "D:\\opt\\cloud\\temp\\";
        } else {
            return "/opt/cloud/temp/";
        }
    }
}
