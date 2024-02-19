package org.linshy.saas.admin.toolkit;

import java.util.Random;

/**
 * 分组ID随机生成器
 */
public class RandomStringGenerator {
    // 字符源，可以根据需要添加任何字符
    private static final char[] CHAR_SOURCE = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789".toCharArray();

    /**
     * 随机生成6分组ID
     * @return  分组ID
     */
    public static String generateRandom() {
        return generateRandom(6);
    }
    // 生成随机字符串的方法
    public static String generateRandom(int length) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            char c = CHAR_SOURCE[random.nextInt(CHAR_SOURCE.length)];
            sb.append(c);
        }

        return sb.toString();
    }
}
