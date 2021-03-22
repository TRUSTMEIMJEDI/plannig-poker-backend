package com.marcinfriedrich.planningpoker.util;

import org.apache.commons.lang3.RandomStringUtils;

import java.security.SecureRandom;

public final class RandomUtil {
    private static final int DEF_COUNT = 5;
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    static {
        SECURE_RANDOM.nextBytes(new byte[64]);
    }

    private RandomUtil() {
    }

    public static String generateRoomKey() {
        return generateRandomAlphanumericString(5);
    }

    public static String generateUserKey() {
        return generateRandomAlphanumericString(10);
    }

    private static String generateRandomAlphanumericString(int count) {
        return RandomStringUtils.random(count, 0, 0, true, true, null, SECURE_RANDOM);
    }
}
