package cn.config;

import cn.vo.FileVo;
import io.minio.MinioClient;
import io.minio.ObjectStat;
import io.minio.PutObjectOptions;
import io.minio.Result;
import io.minio.errors.XmlParserException;
import io.minio.messages.Bucket;
import io.minio.messages.Item;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


@Component
public class MinioConfig implements InitializingBean {

    @Value(value = "${minio.bucket}")
    private String bucket;

    @Value(value = "${minio.host}")
    private String host;

    @Value(value = "${minio.url}")
    private String url;

    @Value(value = "${minio.access-key}")
    private String accessKey;

    @Value(value = "${minio.secret-key}")
    private String secretKey;

    private MinioClient minioClient;

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.hasText(url, "Minio url 为空");
        Assert.hasText(accessKey, "Minio accessKey为空");
        Assert.hasText(secretKey, "Minio secretKey为空");
        this.minioClient = new MinioClient(this.host, this.accessKey, this.secretKey);
    }



    /**
     * 上传
     */
    public String putObject(MultipartFile multipartFile) throws Exception {
        // bucket 不存在，创建
        if (!minioClient.bucketExists(this.bucket)) {
            minioClient.makeBucket(this.bucket);
        }
        try (InputStream inputStream = multipartFile.getInputStream()) {
            // 上传文件的名称
            String fileName = multipartFile.getOriginalFilename();
            // PutObjectOptions，上传配置(文件大小，内存中文件分片大小)
            PutObjectOptions putObjectOptions = new PutObjectOptions(multipartFile.getSize(), PutObjectOptions.MIN_MULTIPART_SIZE);
            // 文件的ContentType
            putObjectOptions.setContentType(multipartFile.getContentType());
            minioClient.putObject(this.bucket, fileName, inputStream, putObjectOptions);
            // 返回访问路径
            return this.url + UriUtils.encode(fileName, StandardCharsets.UTF_8);
        }
    }

    public String putObject(MultipartFile multipartFile, String filepath) throws Exception {
        // bucket 不存在，创建
        if (!minioClient.bucketExists(this.bucket)) {
            minioClient.makeBucket(this.bucket);
        }
        try (InputStream inputStream = multipartFile.getInputStream()) {
            // 上传文件的名称
            String fileName = multipartFile.getOriginalFilename();
            // PutObjectOptions，上传配置(文件大小，内存中文件分片大小)
            PutObjectOptions putObjectOptions = new PutObjectOptions(multipartFile.getSize(), PutObjectOptions.MIN_MULTIPART_SIZE);
            // 文件的ContentType
            putObjectOptions.setContentType(multipartFile.getContentType());
            minioClient.putObject(this.bucket, filepath +fileName, inputStream, putObjectOptions);
            // 返回访问路径
            return this.url + UriUtils.encode(fileName, StandardCharsets.UTF_8);
        }
    }

    public String createDir(String filepath) throws Exception {
        // bucket 不存在，创建
        if (!minioClient.bucketExists(this.bucket)) {
            minioClient.makeBucket(this.bucket);
        }
        int lastIndex = filepath.lastIndexOf("/");
        String fileName = filepath.substring(lastIndex + 1);
        String content = "HelloWorld";
        InputStream inputStream = new ByteArrayInputStream(content.getBytes());
        // 使用这个空的byte数组创建一个ByteArrayInputStream
        PutObjectOptions putObjectOptions = new PutObjectOptions(10,PutObjectOptions.MIN_MULTIPART_SIZE);
        putObjectOptions.setContentType("text/plain");
        String objectName = filepath + "/" + fileName + "_#*#*dirMask";
        minioClient.putObject(this.bucket, objectName, inputStream, putObjectOptions);
        return "Folder created successfully.";
    }

    /**
     * 文件下载
     */
    public void download(String fileName, HttpServletResponse response){
        // 从链接中得到文件名
        InputStream inputStream;
        try {
            MinioClient minioClient = new MinioClient(host, accessKey, secretKey);
            ObjectStat stat = minioClient.statObject(bucket, fileName);
            System.out.println(fileName);
            inputStream = minioClient.getObject(bucket, fileName);
            response.setContentType(stat.contentType());
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
            IOUtils.copy(inputStream, response.getOutputStream());
            inputStream.close();
        } catch (Exception e){
            e.printStackTrace();
            System.out.println("有异常：" + e);
        }
    }

    public void downloadDir(String dirPath) throws XmlParserException {
        Iterator<Result<Item>> iterator =  minioClient.listObjects(this.bucket, dirPath, true).iterator();

    }

    /**
     * 列出所有存储桶名称
     *
     * @return
     * @throws Exception
     */
    public List<String> listBucketNames()
            throws Exception {
        List<Bucket> bucketList = listBuckets();
        List<String> bucketListName = new ArrayList<>();
        for (Bucket bucket : bucketList) {
            bucketListName.add(bucket.name());
        }
        return bucketListName;
    }

    /**
     * 查看所有桶
     *
     * @return
     * @throws Exception
     */
    public List<Bucket> listBuckets()
            throws Exception {
        return minioClient.listBuckets();
    }

    /**
     * 检查存储桶是否存在
     *
     * @param bucketName
     * @return
     * @throws Exception
     */
    public boolean bucketExists(String bucketName) throws Exception {
        boolean flag = minioClient.bucketExists(bucketName);
        if (flag) {
            return true;
        }
        return false;
    }

    /**
     * 创建存储桶
     *
     * @param bucketName
     * @return
     * @throws Exception
     */
    public boolean makeBucket(String bucketName)
            throws Exception {
        boolean flag = bucketExists(bucketName);
        if (!flag) {
            minioClient.makeBucket(bucketName);
            return true;
        } else {
            return false;
        }
    }

    /**
     * 删除桶
     *
     * @param bucketName
     * @return
     * @throws Exception
     */
    public boolean removeBucket(String bucketName)
            throws Exception {
        boolean flag = bucketExists(bucketName);
        if (flag) {
            Iterable<Result<Item>> myObjects = listObjects(bucketName);
            for (Result<Item> result : myObjects) {
                Item item = result.get();
                // 有对象文件，则删除失败
                if (item.size() > 0) {
                    return false;
                }
            }
            // 删除存储桶，注意，只有存储桶为空时才能删除成功。
            minioClient.removeBucket(bucketName);
            flag = bucketExists(bucketName);
            if (!flag) {
                return true;
            }

        }
        return false;
    }

    /**
     * 列出存储桶中的所有对象
     *
     * @param bucketName 存储桶名称
     * @return
     * @throws Exception
     */
    public Iterable<Result<Item>> listObjects(String bucketName) throws Exception {
        boolean flag = bucketExists(bucketName);
        if (flag) {
            return minioClient.listObjects(bucketName);
        }
        return null;
    }

    public Iterable<Result<Item>> listObjects(String bucketName, String prefix) throws Exception {
        boolean flag = bucketExists(bucketName);
        if (flag) {
            return minioClient.listObjects(bucketName, prefix, false);
        }
        return null;
    }

    public Iterable<Result<Item>> listObjects(String bucketName, String prefix, boolean withSub) throws Exception {
        boolean flag = bucketExists(bucketName);
        if (flag) {
            return minioClient.listObjects(bucketName, prefix, withSub);
        }
        return null;
    }

    /**
     * 列出存储桶中的所有对象名称
     *
     * @param bucketName 存储桶名称
     * @return
     * @throws Exception
     */
    public List<String> listObjectNames(String bucketName) throws Exception {
        List<String> listObjectNames = new ArrayList<>();
        boolean flag = bucketExists(bucketName);
        if (flag) {
            Iterable<Result<Item>> myObjects = listObjects(bucketName);
            for (Result<Item> result : myObjects) {
                Item item = result.get();
                listObjectNames.add(item.objectName());
            }
        }
        return listObjectNames;
    }

    /**
     * 列出存储桶中的所有对象名称
     *
     * @param bucketName 存储桶名称
     * @return
     * @throws Exception
     */
    public List<String> listObjectNames(String bucketName, String prefix) throws Exception {
        List<String> listObjectNames = new ArrayList<>();
        boolean flag = bucketExists(bucketName);
        if (flag) {
            Iterable<Result<Item>> myObjects = listObjects(bucketName, prefix);
            for (Result<Item> result : myObjects) {
                Item item = result.get();
                listObjectNames.add(item.objectName());
            }
        }
        return listObjectNames;
    }


    public List<FileVo> listObjectProperties(String bucketName, String prefix) throws Exception {
        List<String> listObjectNames = new ArrayList<>();
        List<FileVo> listObjectProperties = new ArrayList<>();
        boolean flag = bucketExists(bucketName);
        if (flag) {
            Iterable<Result<Item>> myObjects = listObjects(bucketName, prefix);
            for (Result<Item> result : myObjects) {
                Item item = result.get();
                listObjectNames.add(item.objectName());
                FileVo fileVo = new FileVo();
                fileVo.setName(item.objectName());
                fileVo.setSize(item.size());
                fileVo.setTime(getTime(item));
                listObjectProperties.add(fileVo);
            }
        }
        return listObjectProperties;
    }

    public ZonedDateTime getTime(Item item) {
        try {
            return item.lastModified();
        } catch (Exception e){
            return null;
        }
    }

    public List<String> listObjectNames(String bucketName, String prefix, boolean withSub) throws Exception {
        List<String> listObjectNames = new ArrayList<>();
        boolean flag = bucketExists(bucketName);
        if (flag) {
            Iterable<Result<Item>> myObjects = listObjects(bucketName, prefix, withSub);
            for (Result<Item> result : myObjects) {
                Item item = result.get();
                listObjectNames.add(item.objectName());
            }
        }
        return listObjectNames;
    }

    /**
     * 删除一个对象
     *
     * @param bucketName 存储桶名称
     * @param objectName 存储桶里的对象名称
     * @throws Exception
     */
    public boolean removeObject(String bucketName, String objectName) throws Exception {
        boolean flag = bucketExists(bucketName);
        if (flag) {
            List<String> objectList = listObjectNames(bucketName);
            for (String s : objectList) {
                if(s.equals(objectName)){
                    minioClient.removeObject(bucketName, objectName);
                    return true;
                }
            }
        }
        return false;
    }


    /**
     * 删除一个对象
     *
     * @param objectName 存储桶里的对象名称
     * @throws Exception
     */
    public boolean removeObject(String objectName) throws Exception {
        return  removeObject(this.bucket, objectName);
    }

    /**
     * 文件访问路径
     *
     * @param bucketName 存储桶名称
     * @param objectName 存储桶里的对象名称
     * @return
     * @throws Exception
     */
    public String getObjectUrl(String bucketName, String objectName) throws Exception {
        boolean flag = bucketExists(bucketName);
        String url = "";
        if (flag) {
            url = minioClient.getObjectUrl(bucketName, objectName);
        }
        return url;
    }

}

