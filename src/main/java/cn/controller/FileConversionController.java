package cn.controller;

import cn.utils.WordToPdfConverter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@Controller
@RequestMapping("/w2p")
public class FileConversionController {

    /**
     * 跳转 w2p 页面，提交文件
     * @return
     */
    @GetMapping
    public String w2p(){
        return "/w2p/w2p";
    }

    /**
     * 文件转换：word 装换为 PDF
     *
     * @param file 源 word 文件
     * @return
     */
    @PostMapping("/convert")
    public ResponseEntity<byte[]> convertWordToPdf(@RequestParam("file") MultipartFile file) {
        try {
            // 创建 word 临时文件对象
            File wordFile = File.createTempFile("word", ".docx");
            // 临时 word 文件写入磁盘
            file.transferTo(wordFile);

            // 建 pdf 临时文件对象
            File pdfFile = File.createTempFile("pdf", ".pdf");

            // 调用转换工具类
            WordToPdfConverter converter = new WordToPdfConverter();
            // 转换 PDF
            converter.convertToPdf(wordFile, pdfFile);


            /* PDF 文件下载 */
            FileInputStream fis = new FileInputStream(pdfFile);
            byte[] bytes = new byte[fis.available()];
            fis.read(bytes);


            // 删除 word 临时文件
            wordFile.delete();
            fis.close();
            pdfFile.delete();

            // 设置下载的响应头信息
            HttpHeaders hh = new HttpHeaders();
            hh.setContentDispositionFormData("attachement", pdfFile.getName());

            return new ResponseEntity<byte[]>(bytes,  hh, HttpStatus.OK);
            /* PDF 文件下载 */
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
