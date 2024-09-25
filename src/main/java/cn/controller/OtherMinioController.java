package cn.controller;


import cn.config.MinioConfig;
import cn.utils.MinioDownloadUtil;
import io.minio.Result;
import io.minio.messages.Item;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/minio")
@Slf4j
public class OtherMinioController {

    @Autowired
    MinioConfig minioConfig;

    @Autowired
    MinioDownloadUtil minioDownloadUtil;


    // 列出所有存储桶名称
    @PostMapping("/list")
    public List<String> list() throws Exception {
        return this.minioConfig.listBucketNames();
    }

    // 创建存储桶
    @PostMapping("/createBucket")
    public boolean createBucket(String bucketName) throws Exception {
        return this.minioConfig.makeBucket(bucketName);
    }

    // 删除存储桶
    @PostMapping("/deleteBucket")
    public boolean deleteBucket(String bucketName) throws Exception {
        return this.minioConfig.removeBucket(bucketName);
    }

    @PostMapping("/listObjectObjects/{bucketName}")
    public Iterable<Result<Item>> listObjects(@PathVariable String bucketName) throws Exception {
        return this.minioConfig.listObjects(bucketName);
    }

    // 文件访问路径
    @PostMapping("/getObjectUrl")
    public String getObjectUrl(String bucketName, String objectName) throws Exception {
        return this.minioConfig.getObjectUrl(bucketName, objectName);
    }
}

