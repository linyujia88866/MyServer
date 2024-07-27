package cn.utils;

import cn.hutool.core.util.StrUtil;

import java.awt.*;
import java.awt.image.BufferedImage;

import com.documents4j.api.DocumentType;
import com.documents4j.api.IConverter;
import com.documents4j.job.LocalConverter;
import com.itextpdf.text.DocumentException;
import org.apache.poi.xslf.usermodel.*;
import org.apache.poi.hslf.usermodel.*;
import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.Image;
import org.apache.poi.xwpf.usermodel.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class FileConvertUtil {

    /**
     * pptToPdf
     * @param pptPath PPT文件路径
     * @return
     */
    public static boolean pptToPdf(String pptPath) throws IOException {

        if (StrUtil.isEmpty(pptPath)) {
            throw new RuntimeException("word文档路径不能为空");
        }
        String pdfPath =  pptPath.substring(0, pptPath.lastIndexOf(StrUtil.DOT)) + StrUtil.DOT + "pdf";
        File pdf = new File(pdfPath);
        if(!pdf.exists()){
            pdf.createNewFile();
        }
        Document document = null;
        HSLFSlideShow hslfSlideShow = null;
        FileOutputStream fileOutputStream = null;
        PdfWriter pdfWriter = null;
        try {
            hslfSlideShow = new HSLFSlideShow(new FileInputStream(pptPath));

            // 获取ppt文件页面
            Dimension dimension = hslfSlideShow.getPageSize();

            fileOutputStream = new FileOutputStream(pdfPath);

            document = new Document();

            // pdfWriter实例
            pdfWriter = PdfWriter.getInstance(document, fileOutputStream);

            document.open();

            PdfPTable pdfPTable = new PdfPTable(1);

            List<HSLFSlide> hslfSlideList = hslfSlideShow.getSlides();

            for (int i=0; i < hslfSlideList.size(); i++) {
                HSLFSlide hslfSlide = hslfSlideList.get(i);
//                // 设置字体, 解决中文乱码
//                for (HSLFShape shape : hslfSlide.getShapes()) {
//                    HSLFTextShape textShape = (HSLFTextShape) shape;
//
//                    for (HSLFTextParagraph textParagraph : textShape.getTextParagraphs()) {
//                        for (HSLFTextRun textRun : textParagraph.getTextRuns()) {
//                            textRun.setFontFamily("宋体");
//                        }
//                    }
//                }
                BufferedImage bufferedImage = new BufferedImage((int)dimension.getWidth(), (int)dimension.getHeight(), BufferedImage.TYPE_INT_RGB);

                Graphics2D graphics2d = bufferedImage.createGraphics();

                graphics2d.setPaint(Color.white);
                graphics2d.setFont(new java.awt.Font("宋体", java.awt.Font.PLAIN, 12));

                hslfSlide.draw(graphics2d);

                graphics2d.dispose();

                Image image = Image.getInstance(bufferedImage, null);
                image.scalePercent(50f);

                // 写入单元格
                pdfPTable.addCell(new PdfPCell(image, true));
                document.add(image);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (document != null) {
                    document.close();
                }
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
                if (pdfWriter != null) {
                    pdfWriter.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    /**
     *
     * @Title: pptxToPdf
     * @param pptPath PPT文件路径
     */
    public static boolean pptxToPdf(String pptPath) {

        if (StrUtil.isEmpty(pptPath)) {
            throw new RuntimeException("word文档路径不能为空");
        }



        String pdfPath =  pptPath.substring(0, pptPath.lastIndexOf(StrUtil.DOT)) + StrUtil.DOT + "pdf";
        Document document = null;

        XMLSlideShow slideShow = null;


        FileOutputStream fileOutputStream = null;

        PdfWriter pdfWriter = null;


        try {

            slideShow = new XMLSlideShow(new FileInputStream(pptPath));

            Dimension dimension = slideShow.getPageSize();

            fileOutputStream = new FileOutputStream(pdfPath);

            document = new Document();

            pdfWriter = PdfWriter.getInstance(document, fileOutputStream);

            document.open();

            PdfPTable pdfPTable = new PdfPTable(1);

            List<XSLFSlide> slideList = slideShow.getSlides();

            for (int i = 0, row = slideList.size(); i < row; i++) {

                XSLFSlide slide = slideList.get(i);

                BufferedImage bufferedImage = new BufferedImage((int)dimension.getWidth(), (int)dimension.getHeight(), BufferedImage.TYPE_INT_RGB);

                Graphics2D graphics2d = bufferedImage.createGraphics();

                graphics2d.setPaint(Color.white);
                graphics2d.setFont(new java.awt.Font("宋体", java.awt.Font.PLAIN, 12));

                slide.draw(graphics2d);

                graphics2d.dispose();

                Image image = Image.getInstance(bufferedImage, null);
                image.scalePercent(50f);

                // 写入单元格
                pdfPTable.addCell(new PdfPCell(image, true));
                document.add(image);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (document != null) {
                    document.close();
                }
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
                if (pdfWriter != null) {
                    pdfWriter.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }


    /**
     * word转pdf
     * @param filePath
     * @throws IOException
     */
    public static void wordToPdf(String filePath) throws IOException {
        //如果为文档类型 则生成同样的文件名的PDF
        //创建word文件流
        FileInputStream fileInputStreamWord = null;
        FileOutputStream os = null;
        try{
            File  upFile = new File(filePath);
            fileInputStreamWord =new FileInputStream(upFile);
            File pdfFile=new File(filePath.substring(0, filePath.lastIndexOf(".")) + ".pdf");
            if (!pdfFile.exists()){
                pdfFile.createNewFile();
            }
            os = new FileOutputStream(pdfFile);
            IConverter converter = LocalConverter.builder().build();
            converter.convert(fileInputStreamWord)
                    .as(DocumentType.DOCX)
                    .to(os)
                    .as(DocumentType.PDF).execute();
            // 关闭
            converter.shutDown();
            // 关闭
            fileInputStreamWord.close();
            // 关闭
            os.close();

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (fileInputStreamWord != null) {
                fileInputStreamWord.close();
            }
            if (os != null) {
                os.close();
            }
        }
    }


    /**
     * excel转pdf
     * @param filePath
     * @throws IOException
     */
    public static void excelToPdf(String filePath) throws IOException {
        //如果为文档类型 则生成同样的文件名的PDF
        //创建word文件流
        FileInputStream fileInputStream = null;
        FileOutputStream fileOutputStream = null;
        try{
            File  upFile = new File(filePath);
            fileInputStream =new FileInputStream(upFile);
            File pdfFile=new File(filePath.substring(0, filePath.lastIndexOf(".")) + ".pdf");
            if (!pdfFile.exists()){
                pdfFile.createNewFile();
            }
            fileOutputStream = new FileOutputStream(pdfFile);
            IConverter converter = LocalConverter.builder().build();
            converter.convert(fileInputStream)
                    .as(DocumentType.XLSX)
                    .to(fileOutputStream)
                    .as(DocumentType.PDF).execute();
            // 关闭
            converter.shutDown();
            // 关闭
            fileInputStream.close();
            // 关闭
            fileOutputStream.close();

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (fileInputStream != null) {
                fileInputStream.close();
            }
            if (fileOutputStream != null) {
                fileOutputStream.close();
            }
        }
    }

}