package kr.co.infra.rdb.common.util;

import lombok.experimental.UtilityClass;

import java.security.SecureRandom;
import java.util.UUID;

@UtilityClass
public class UuidUtils {
    private static final SecureRandom RANDOM = new SecureRandom();
    private static final long VERSION_7_MASK = 0x7000L;
    private static final long VARIANT_MASK = 0x8000000000000000L;
    private static final long RANDOM_B_MASK = 0x3FFFFFFFFFFFFFFFL;
    private static final int TIMESTAMP_SHIFT = 16;
    private static final int RANDOM_A_BOUND = 4096; // 2^12

    public static UUID randomV7() {
        long timestamp = getMicrosecondTimestamp();
        int randomA = RANDOM.nextInt(RANDOM_A_BOUND);
        long randomB = RANDOM.nextLong() & RANDOM_B_MASK;

        long mostSigBits = (timestamp << TIMESTAMP_SHIFT) | VERSION_7_MASK | randomA;
        long leastSigBits = VARIANT_MASK | randomB;

        return new UUID(mostSigBits, leastSigBits);
    }

    public static long extractTimestamp(UUID uuid) {
        return uuid.getMostSignificantBits() >>> TIMESTAMP_SHIFT;
    }

    private static long getMicrosecondTimestamp() {
        long millis = System.currentTimeMillis();
        long nanos = System.nanoTime();

        long micros = (nanos / 1000) % 1000;
        return millis * 1000 + micros;
    }
}
