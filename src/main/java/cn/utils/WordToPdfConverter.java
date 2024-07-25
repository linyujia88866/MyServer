package cn.utils;

import org.apache.poi.xwpf.converter.pdf.PdfConverter;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.*;

public class WordToPdfConverter {
    public void convertToPdf(File wordFile, File pdfFile) throws IOException {
        InputStream inputStream = null;
        OutputStream outputStream = null;
        XWPFDocument document = null;
        try {
            // 文件输入流
            inputStream = new FileInputStream(wordFile);
            // 文件输出流
            outputStream = new FileOutputStream(pdfFile);

            document = new XWPFDocument(inputStream);
            PdfConverter.getInstance().convert(document, outputStream, null);
        } catch (IOException e){
            e.printStackTrace();
        } finally {
            // 释放资源
            document.close();
            outputStream.close();
            inputStream.close();
        }
    }
}
