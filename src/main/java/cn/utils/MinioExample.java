package cn.utils;

import io.minio.MinioClient;
import io.minio.Result;
import io.minio.messages.Item;

import java.util.ArrayList;
import java.util.List;

public class MinioExample {
    public static void main(String[] args) throws Exception {
        // 初始化MinIO客户端
        MinioClient minioClient = new MinioClient("http://47.109.79.50:9000",
                "minioadmin", "Liyan@9802");
        // 桶名称
        String bucketName = "test";
        // 文件夹/前缀
        String prefix = "/";

        // 列举对象
        Iterable<Result<Item>> items = minioClient.listObjects(bucketName, prefix, false);


        // 获取对象名称列表
        List<String> objectNames = new ArrayList<>();
        for (Result<Item> item : items) {
            System.out.println(item.get().isDir());
            objectNames.add(item.get().objectName());

        }

        // 打印对象名称
        for (String objectName : objectNames) {
            System.out.println(objectName);
        }
    }
}