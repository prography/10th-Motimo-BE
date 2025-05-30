package kr.co.infra.rdb.common.util;

import lombok.experimental.UtilityClass;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

@UtilityClass
public class UUIDUtils {

    private static final SecureRandom RANDOM = new SecureRandom();
    private static final AtomicInteger CLOCK_SEQUENCE = new AtomicInteger(0);

    /**
     * TIMESTAMP_MASK = 0xFFFFFFFFFFFFL (48개의 1비트) 용도: 48비트 타임스탬프 추출/보존
     */
    private static final long TIMESTAMP_MASK = 0xFFFFFFFFFFFFL;

    /**
     * VERSION_7_MASK = 0x7000L 용도: UUID 버전 7 설정
     */
    private static final long VERSION_7_MASK = 0x7000L;

    /**
     * CLOCK_SEQ_MASK = 0xFFF (12개의 1비트) 용도: 12비트 클럭 시퀀스, 같은 밀리초 내 UUID 순서 보장용
     */
    private static final long CLOCK_SEQ_MASK = 0xFFF;

    /**
     * VARIANT_MASK = 0x8000000000000000L 용도: RFC 4122 variant 설정 (10xxxxxx), LSB의 최상위 2비트
     */
    private static final long VARIANT_MASK = 0x8000000000000000L;

    /**
     * RANDOM_B_MASK = 0x3FFFFFFFFFFFFFFFL (62개의 1비트) 용도: LSB에서의 variant 2비트를 제외한 나머지 62비트, 랜덤 값
     */
    private static final long RANDOM_B_MASK = 0x3FFFFFFFFFFFFFFFL;

    /**
     * TIMESTAMP_SHIFT = 16 용도: 타임스탬프는 MSB의 상위 48비트로 이동, 64비트 - 48비트 = 16비트 시프트
     */
    private static final int TIMESTAMP_SHIFT = 16;


    /**
     * CLOCK_SEQ_RANGE = 4096 (2^12) 용도: 클럭 시퀀스 최대값 + 1
     */
    private static final int CLOCK_SEQ_RANGE = 4096;
    private static final int CLOCK_SEQ_MAX = CLOCK_SEQ_RANGE - 1;

    private static volatile long lastTimestampMs = 0;

    public static UUID createUUIDv7() {
        Instant now = Instant.now();
        long nowMs = now.toEpochMilli();
        long clockSeq = getClockSequence(nowMs);

        long msb = 0;
        msb |= (nowMs & TIMESTAMP_MASK) << TIMESTAMP_SHIFT;
        msb |= VERSION_7_MASK;
        msb |= (clockSeq & CLOCK_SEQ_MASK);

        long lsb = 0;
        lsb |= VARIANT_MASK;
        lsb |= (RANDOM.nextLong() & RANDOM_B_MASK);

        return new UUID(msb, lsb);
    }

    private static long getClockSequence(long currentMs) {
        synchronized (CLOCK_SEQUENCE) {
            if (currentMs != lastTimestampMs) {
                lastTimestampMs = currentMs;
                CLOCK_SEQUENCE.set(RANDOM.nextInt(CLOCK_SEQ_RANGE));
            } else {
                CLOCK_SEQUENCE.compareAndSet(CLOCK_SEQ_MAX, 0);
            }
            return CLOCK_SEQUENCE.getAndIncrement();
        }
    }

    public static long extractTimestamp(UUID uuid) {
        return (uuid.getMostSignificantBits() >>> TIMESTAMP_SHIFT) & TIMESTAMP_MASK;
    }
}
