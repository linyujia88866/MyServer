package cn.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.util.ArrayList;
import java.util.List;

public class HtmlImageExtractor {
    public static void main(String[] args) {
        String html = "<html><head><title>Example</title></head>"
                + "<body><img src='image1.jpg' /><img src='image2.png' /></body></html>";

        List<String> imageUrls = extractImageUrls(html);
        imageUrls.forEach(System.out::println);
    }

    public static List<String> extractImageUrls(String html) {
        List<String> imageUrls = new ArrayList<>();
        Document doc = Jsoup.parse(html);
        Elements imgElements = doc.select("img[src]");

        for (Element imgElement : imgElements) {
            String imageUrl = imgElement.attr("src");
            if(!imageUrl.contains("pic-link/")){
                continue;
            }
            imageUrls.add(imageUrl.substring(imageUrl.indexOf("pic-link/")).replace("%2F","/"));
        }

        return imageUrls;
    }
}