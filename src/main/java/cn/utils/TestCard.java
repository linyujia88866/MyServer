package cn.utils;

import static cn.utils.IdcardValidator.isValidatedAllIdcard;

public class TestCard {
    private static final int[] w = {7,9,10,5,8,4,2,1,6,
            3,7,9,10,5,8,4,2};

    public static void main(String[] args) {
        String id = "11111111111";
        System.out.println(isValidCardId(id));

    }

    public static boolean isValidCardId(String id) {
        return isCard(id) && isValidatedAllIdcard(id);
    }

    public static boolean isCard(String id)
    {
        char[] c=id.toCharArray();
        int sum=0;
        for (int i = 0; i < w.length; i++) {
            sum+=(c[i]-'0')*w[i];
        }
        char[] verifyCode="10X98765432".toCharArray();
        char ch =verifyCode[sum%11];
        return c[17]==ch;

    }

}