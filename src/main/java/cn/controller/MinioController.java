package cn.controller;


import cn.config.MinioConfig;
import cn.result.Result;
import cn.utils.JWTUtils;
import cn.utils.MinioDownloadUtil;
import cn.utils.UuidUtil;
import cn.vo.FileVo;
import io.minio.errors.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import static cn.utils.requestUtils.getTokenFromRequest;

@RestController
@CrossOrigin
@RequestMapping("/api/minio")
@Slf4j
public class MinioController {

    @Autowired
    MinioConfig minioConfig;

    @Autowired
    MinioDownloadUtil minioDownloadUtil;

    @Value("${app.message}")
    private String message;

    @Value("${app.host}")
    private String host;

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

    @PostMapping("/upload-art-pic")
    public Object upload3(HttpServletRequest request, @RequestParam("file") MultipartFile multipartFile) throws Exception {
        String token = getTokenFromRequest(request);
        String username = JWTUtils.parseJWT(token);
        String finalPath= username + "/";
        String filename = UuidUtil.getUuid() + multipartFile.getOriginalFilename();
        String res =  this.minioConfig.putObject(multipartFile, finalPath, filename, "pic-link");
        return res.replace("47.109.79.50:9000", this.host + "/share")
                .replace("172.19.15.159:9000", this.host + "/share");
    }

    @PostMapping("/upload2")
    public Object upload2(HttpServletRequest request, @RequestParam("file") MultipartFile multipartFile
            , @RequestParam(value = "filepath") String filepath
            , @RequestParam(value = "filename") String filename) throws Exception {
        String token = getTokenFromRequest(request);
        String username = JWTUtils.parseJWT(token);
        String finalPath;
        if(filepath.equals("/")){
            finalPath = username + "/";
        } else {
            finalPath = username + "/" + filepath;
        }
        return this.minioConfig.putObject(multipartFile, finalPath, filename);
    }

    @GetMapping("/get-shared-link")
    public Result<String> getSharedLink(HttpServletRequest request,@RequestParam String bucketName, @RequestParam String objectName, @RequestParam int expiresSeconds) throws InvalidBucketNameException, InsufficientDataException, ErrorResponseException, InvalidExpiresRangeException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        String token = getTokenFromRequest(request);
        String username = JWTUtils.parseJWT(token);
        String finalPath = username + "/" + objectName;
        String sharedLink = minioConfig.getSharedLink(bucketName, finalPath, expiresSeconds);
        return Result.success(sharedLink.replace("47.109.79.50:9000", this.host + "/share")
                .replace("172.19.15.159:9000", this.host + "/share"));
    }

    @PostMapping("/createDir")
    public Object createDir(HttpServletRequest request, @RequestParam(value = "filepath") String filepath) throws Exception {
        String token = getTokenFromRequest(request);
        String username = JWTUtils.parseJWT(token);
        String finalPath;
        if(filepath.equals("/")){
            finalPath = username + "/";
        } else {
            finalPath = username + "/" + filepath;
        }
        return this.minioConfig.createDir(finalPath);
    }

    // 下载文件
    @GetMapping("/download")
    public void download(HttpServletRequest request, @RequestParam("fileName")String fileName, HttpServletResponse response) {
        String token = getTokenFromRequest(request);
        String username = JWTUtils.parseJWT(token);
        String finalPath = username + "/" + fileName;
        this.minioConfig.download(finalPath,response);
    }

    @GetMapping("/preview")
    public void preview(HttpServletRequest request, @RequestParam("fileName")String fileName, HttpServletResponse response) throws RegionConflictException, InvalidBucketNameException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        String token = getTokenFromRequest(request);
        String username = JWTUtils.parseJWT(token);
        String finalPath = username + "/" + fileName;
        this.minioConfig.preview(finalPath,response);
    }

    @GetMapping("/downloadDir")
    public String downloadDir(HttpServletRequest request, @RequestParam("fileName")String fileName, HttpServletResponse response) throws Exception {
        String token = getTokenFromRequest(request);
        String username = JWTUtils.parseJWT(token);
        String finalPath = username + "/" + fileName;
        List<String> filePaths = this.minioConfig.listObjectNames("test", finalPath, true);
        //以下代码为获取图片inputStream
        filePaths.removeIf(entity -> entity.endsWith("/") || entity.endsWith("_#*#*dirMask"));
        log.info("下载清单列表{}", filePaths);
        if(!filePaths.isEmpty()){
            minioDownloadUtil.downloadZip("linyujia", filePaths, response, username);
            return  "download successfully!";
        }else {
            response.setStatus(222);
            return  "Nothing to download!";
        }

    }

    // 列出存储桶中的所有对象名称
    @PostMapping("/listObjectNames/{bucketName}")
    public List<String> listObjectNames(@PathVariable String bucketName) throws Exception {
        return this.minioConfig.listObjectNames(bucketName, "");
    }

    @PostMapping("/listObjectNamesInDir/{bucketName}")
    public List<String> listObjectNamesInDir(HttpServletRequest request, @PathVariable String bucketName, @RequestParam(value = "prefix") String prefix) throws Exception {
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

    @PostMapping("/listObjectsInDir/{bucketName}")
    public List<FileVo> listObjectsInDir(HttpServletRequest request, @PathVariable String bucketName, @RequestParam(value = "prefix") String prefix) throws Exception {
        String token = getTokenFromRequest(request);
        String username = JWTUtils.parseJWT(token);
        String finalPath;
        if(prefix.equals("/")){
            finalPath = username + "/";
        } else {
            finalPath = username + "/" + prefix;
        }
        return this.minioConfig.listObjectProperties(bucketName, finalPath);
    }

    @PostMapping("/moveObject")
    public boolean moveObject(HttpServletRequest request,@RequestParam(value = "srcpath") String srcpath,
                              @RequestParam(value = "despath") String despath) throws Exception {
        String token = getTokenFromRequest(request);
        String username = JWTUtils.parseJWT(token);
        String finalSrcPath= username + "/" + srcpath;
        String finalDesPath= username + "/" + despath;
        String bucketName = "test";
        return this.minioConfig.moveObject(bucketName, finalDesPath, finalSrcPath);
    }

    @PostMapping("/moveDir")
    public boolean moveDir(HttpServletRequest request,@RequestParam(value = "srcpath") String srcpath,
                              @RequestParam(value = "despath") String despath) throws Exception {
        String token = getTokenFromRequest(request);
        String username = JWTUtils.parseJWT(token);
        String finalSrcPath= username + "/" + srcpath;
        String finalDesPath= username + "/" + despath;
        String bucketName = "test";
        return this.minioConfig.moveDir(bucketName, finalDesPath, finalSrcPath);
    }


    @PostMapping("/copyObject")
    public boolean copyObject(HttpServletRequest request,@RequestParam(value = "srcpath") String srcpath,
                              @RequestParam(value = "despath") String despath) throws Exception {
        String token = getTokenFromRequest(request);
        String username = JWTUtils.parseJWT(token);
        String finalSrcPath= username + "/" + srcpath;
        String finalDesPath= username + "/" + despath;
        String bucketName = "test";
        return this.minioConfig.copyObject(bucketName, finalDesPath, finalSrcPath);
    }

    @PostMapping("/copyDir")
    public boolean copyDir(HttpServletRequest request,@RequestParam(value = "srcpath") String srcpath,
                              @RequestParam(value = "despath") String despath) throws Exception {
        String token = getTokenFromRequest(request);
        String username = JWTUtils.parseJWT(token);
        String finalSrcPath= username + "/" + srcpath;
        String finalDesPath= username + "/" + despath;
        String bucketName = "test";
        return this.minioConfig.copyDir(bucketName, finalDesPath, finalSrcPath);
    }


    // 删除一个对象
    @PostMapping("/removeObject")
    public boolean removeObject(HttpServletRequest request,@RequestParam(value = "filepath") String filepath) throws Exception {
        String token = getTokenFromRequest(request);
        String username = JWTUtils.parseJWT(token);
        String finalPath= username + "/" + filepath;
        return this.minioConfig.removeObject(finalPath);
    }


    @PostMapping("/removeDir")
    public boolean removeDir(HttpServletRequest request,@RequestParam(value = "filepath") String dirPath) throws Exception {
        String token = getTokenFromRequest(request);
        String username = JWTUtils.parseJWT(token);
        String finalPath;
        if(dirPath.equals("/")){
            finalPath = username + "/";
        } else {
            finalPath = username + "/" + dirPath;
        }
        if(!finalPath.endsWith("/")){
            finalPath = finalPath + "/";
        }
        List<String> fileList = this.minioConfig.listObjectNames("test", finalPath, true);
        for(String file : fileList){
            this.minioConfig.removeObject(file);
        }
        return true;
    }
}

