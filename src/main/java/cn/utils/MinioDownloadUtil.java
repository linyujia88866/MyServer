package cn.utils;

import cn.entity.MediaFileEntity;
import cn.hutool.core.util.ZipUtil;
import io.minio.MinioClient;
import io.minio.ObjectStat;
import io.minio.errors.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Component
public class MinioDownloadUtil {
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

//    private MinioClient minioClient;

    /**
     * 下载多个文件转zip压缩包
     *
     * @param mediaFileEntityList
     * @throws Exception
     */
    public File dowloadToZip(List<String> mediaFileEntityList, String username) throws Exception {
        int i = 0;
        //如果有附件 进行zip处理
        if (mediaFileEntityList != null && mediaFileEntityList.size() > 0) {
            try {
                //被压缩文件流集合
                InputStream[] srcFiles = new InputStream[mediaFileEntityList.size()];
                //被压缩文件名称
                String[] srcFileNames = new String[mediaFileEntityList.size()];
                MinioClient minioClient = new MinioClient(host, accessKey, secretKey);
                for (String entity : mediaFileEntityList) {
                    //以下代码为获取图片inputStream
                    if(entity.endsWith("/")){
                        continue;
                    }
                    ObjectStat stat = minioClient.statObject(bucket, entity);
                    InputStream ins = minioClient.getObject(this.bucket, entity);
                    if (ins == null) {
                        continue;
                    }
                    //塞入流数组中
                    srcFiles[i] = ins;
                    srcFileNames[i] = entity.replace(username + "/", "");
                    i++;
                }
                //多个文件压缩成压缩包返回
                return ZipUtil.zip(new File("下载.zip"), srcFileNames, srcFiles);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public void downloadZip(String name, List<String> filePaths, HttpServletResponse response, String username) throws Exception {
        File zipFile = dowloadToZip(filePaths, username);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            FileInputStream ins = new FileInputStream(zipFile);
            WritableByteChannel writableByteChannel = Channels.newChannel(os);
            FileChannel fileChannel = ins.getChannel();
            fileChannel.transferTo(0, fileChannel.size(), writableByteChannel);
            fileChannel.close();
            response.setCharacterEncoding("UTF-8");
            name = URLEncoder.encode(name, "UTF-8");
            response.setContentType("application/octet-stream");
            response.addHeader("Content-Disposition", "attachment;filename=" + new String(name.getBytes(StandardCharsets.UTF_8)));
            response.setContentLength(os.size());
            response.setHeader("filename", name);
            response.addHeader("Content-Length", "" + os.size());
            ServletOutputStream outputstream = response.getOutputStream();
            os.writeTo(outputstream);
            os.flush();
            os.close();
            outputstream.flush();
            outputstream.close();
            writableByteChannel.close();
            if(zipFile.exists()){
                //删除临时文件
                zipFile.delete();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
