package cn.utils;

import java.util.Base64;
import java.util.stream.Collectors;
public class UnicodeUtil {

    public static String encodeToString(String value) {
        return Base64.getEncoder().encodeToString(value.getBytes());
    }

    public static String decodeToString(String encodedValue) {
        byte[] decodedBytes = Base64.getDecoder().decode(encodedValue);
        return new String(decodedBytes);
    }

    public static String decodeUnicode(String unicode) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < unicode.length();) {
            if (unicode.charAt(i) == '\\' && unicode.charAt(i + 1) == 'u') {
                String unicodeHex = unicode.substring(i + 2, i + 6);
                sb.append((char) Integer.parseInt(unicodeHex, 16));
                i += 6;
            } else {
                sb.append(unicode.charAt(i++));
            }
        }
        return sb.toString();
    }

    public static String encodeToUnicode(String s) {
        return s.chars()
                .mapToObj(c -> String.format("\\u%04x", (int) c))
                .collect(Collectors.joining());
    }

    public static void main(String[] args) {
        String originalText = "Hello, World!";
        String encodedText = encodeToString(originalText);
        System.out.println("Encoded Text: " + encodedText);

        String decodedText = decodeToString(encodedText);
        System.out.println("Decoded Text: " + decodedText);

        String originalString = "Hello, World!";
        String unicodeString = encodeToUnicode(originalString);
        System.out.println(unicodeString);
        System.out.println(decodeUnicode("\\u0048\\u0065\\u006c\\u006c\\u006f\\u002c\\u0020\\u0057\\u006f\\u0072\\u006c\\u0064\\u0021"));
    }
}