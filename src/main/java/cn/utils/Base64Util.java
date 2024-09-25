package cn.utils;

import java.util.Base64;

public class Base64Util {

    public static String encodeToString(String value) {
        return Base64.getEncoder().encodeToString(value.getBytes());
    }

    public static String decodeToString(String encodedValue) {
        byte[] decodedBytes = Base64.getDecoder().decode(encodedValue);
        return new String(decodedBytes);
    }

    public static void main(String[] args) {
        String originalText = "Hello, World!";
        String encodedText = encodeToString(originalText);
        System.out.println("Encoded Text: " + encodedText);

        String decodedText = decodeToString(encodedText);
        System.out.println("Decoded Text: " + decodedText);
    }
}