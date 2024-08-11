package com.sunyy.usercentor.utils;

import java.security.SecureRandom;

public class RandomStringGenerator {
    public static void main(String[] args) {
        long l = System.nanoTime();
        for (int i = 0; i < 100; i++) {
            String s = generateRandomString(6);
            System.out.println(s);
        }
        System.out.println("耗时: " + ((System.nanoTime() - l) / 1000_000) + "ms");
    }

    public static String generateRandomString(int length) {
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int randomValue = random.nextInt(62);
            if (randomValue < 26) {
                sb.append((char) ('a' + randomValue));
            } else if (randomValue < 52) {
                sb.append((char) ('A' + randomValue - 26));
            } else {
                sb.append((char) ('0' + randomValue - 52));
            }
        }

        return sb.toString();
    }
}
