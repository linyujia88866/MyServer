package cn.controller;


import cn.config.MinioConfig;
import cn.utils.JWTUtils;
import io.minio.Result;
import io.minio.messages.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

import static cn.utils.requestUtils.getTokenFromRequest;

@RestController
@CrossOrigin
@RequestMapping("/api/minio")
public class MinioController {

    @Autowired
    MinioConfig minioConfig;

    // 上传
    @PostMapping("/upload")
    public Object upload(HttpServletRequest request, @RequestParam("file") MultipartFile multipartFile, @RequestParam(value = "filepath") String filepath) throws Exception {
        String token = getTokenFromRequest(request);
        String username = JWTUtils.parseJWT(token);
        String finalPath;
        if(filepath.equals("/")){
            finalPath = username + "/";
        } else {
            finalPath = username + "/" + filepath;
        }
        return this.minioConfig.putObject(multipartFile, finalPath);
    }

    // 下载文件
    @GetMapping("/download")
    public void download(@RequestParam("fileName")String fileName, HttpServletResponse response) {
        this.minioConfig.download(fileName,response);
    }

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

    // 列出存储桶中的所有对象名称
    @PostMapping("/listObjectNames/{bucketName}")
    public List<String> listObjectNames(@PathVariable String bucketName) throws Exception {
        return this.minioConfig.listObjectNames(bucketName, "");
    }

    @PostMapping("/listObjectNamesInDir/{bucketName}")
    public List<String> listObjectNames1(HttpServletRequest request, @PathVariable String bucketName, @RequestParam(value = "prefix") String prefix) throws Exception {
        String token = getTokenFromRequest(request);
        String username = JWTUtils.parseJWT(token);
        String finalPath;
        if(prefix.equals("/")){
            finalPath = username + "/";
        } else {
            finalPath = username + "/" + prefix;
        }
        return this.minioConfig.listObjectNames(bucketName, finalPath);
    }


    @PostMapping("/listObjectObjects/{bucketName}")
    public Iterable<Result<Item>> listObjects(@PathVariable String bucketName) throws Exception {
        return this.minioConfig.listObjects(bucketName);
    }

    // 删除一个对象
    @PostMapping("/removeObject")
    public boolean removeObject(String bucketName, String objectName) throws Exception {
        return this.minioConfig.removeObject(bucketName, objectName);
    }

    // 文件访问路径
    @PostMapping("/getObjectUrl")
    public String getObjectUrl(String bucketName, String objectName) throws Exception {
        return this.minioConfig.getObjectUrl(bucketName, objectName);
    }
}

