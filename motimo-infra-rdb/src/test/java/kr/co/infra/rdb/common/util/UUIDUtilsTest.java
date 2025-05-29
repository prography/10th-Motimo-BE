package kr.co.infra.rdb.common.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

class UUIDUtilsTest {

    @Test
    public void uuidV7_정상_생성() throws InterruptedException {
        // given
        UUID uuid1 = UUIDUtils.createUUIDv7();
        Thread.sleep(1);
        UUID uuid2 = UUIDUtils.createUUIDv7();

        long timestamp1 = UUIDUtils.extractTimestamp(uuid1);
        long timestamp2 = UUIDUtils.extractTimestamp(uuid2);
        long uuid1MostSigBits = uuid1.getMostSignificantBits();
        long uuid2MostSigBits = uuid2.getMostSignificantBits();

        // when & then
        assertThat(uuid1.toString().compareTo(uuid2.toString())).isLessThan(0);
        assertThat(uuid1.version()).isEqualTo(7);
        assertThat(uuid2.version()).isEqualTo(7);
        assertThat(timestamp1).isLessThanOrEqualTo(timestamp2);

        int timestampOrder = Long.compareUnsigned(uuid1MostSigBits, uuid2MostSigBits);

        assertThat(timestampOrder).isLessThan(0);
    }

    @Test
    public void uuidV7_같은_밀리초내_순서_보장() {
        // given
        UUID[] uuids = new UUID[100];
        for (int i = 0; i < 100; i++) {
            uuids[i] = UUIDUtils.createUUIDv7();
        }

        // when & then
        for (int i = 1; i < uuids.length; i++) {
            long prevMostSigBits = uuids[i - 1].getMostSignificantBits();
            long currMostSigBits = uuids[i].getMostSignificantBits();
            int timestampOrder = Long.compareUnsigned(prevMostSigBits, currMostSigBits);
            assertThat(timestampOrder).isLessThanOrEqualTo(0);
        }
    }

    @Nested
    @DisplayName("클럭 시퀀스 테스트")
    class ClockSequenceTests {
        @Test
        void 같은_밀리초_내에서_클럭_시퀀스가_증가하는지_확인() {
            // given
            List<UUID> uuids = IntStream.range(0, 50)
                    .mapToObj(i -> UUIDUtils.createUUIDv7())
                    .toList();

            // when
            Map<Long, List<UUID>> timestampGroups = uuids.stream()
                    .collect(Collectors.groupingBy(UUIDUtils::extractTimestamp));

            // then
            timestampGroups.values().stream()
                    .filter(group -> group.size() > 1)
                    .forEach(group -> {
                        Set<Integer> seqs = group.stream()
                                .map(this::extractClockSequence)
                                .collect(Collectors.toSet());

                        // 시퀀스가 모두 같다면 그룹 내 개수만큼 고유 시퀀스가 생기지 않은 것이므로 실패
                        assertThat(seqs.size())
                                .as("같은 타임스탬프 내 고유 시퀀스 개수")
                                .isGreaterThan(1);
                    });
        }

        private int extractClockSequence(UUID uuid) {
            long msb = uuid.getMostSignificantBits();
            return (int) (msb & 0xFFF); // 하위 12비트
        }
    }

    @Nested
    @DisplayName("UUID 생성 동시성 테스트")
    @Execution(ExecutionMode.CONCURRENT)
    class ConcurrencyTests {

        @Test
        void 멀티스레드_환경에서_UUID가_고유한지를_보장() throws InterruptedException {
            // given
            int threadCount = 10;
            int uuidsPerThread = 1000;
            ExecutorService executor = Executors.newFixedThreadPool(threadCount);

            Set<UUID> allUuids = Collections.synchronizedSet(new HashSet<>());
            CountDownLatch latch = new CountDownLatch(threadCount);

            // when
            for (int i = 0; i < threadCount; i++) {
                executor.submit(() -> {
                    try {
                        Set<UUID> threadUuids = new HashSet<>();
                        for (int j = 0; j < uuidsPerThread; j++) {
                            UUID uuid = UUIDUtils.createUUIDv7();
                            threadUuids.add(uuid);
                        }
                        allUuids.addAll(threadUuids);
                    } finally {
                        latch.countDown();
                    }
                });
            }
            executor.shutdown();

            // then
            int expectedTotal = threadCount * uuidsPerThread;
            assertThat(latch.await(10, TimeUnit.SECONDS)).isTrue();
            assertThat(expectedTotal).isEqualTo(allUuids.size());
        }

        @Test
        void 동시_생성된_UUID_타임스탬프_순서_검증() throws InterruptedException {
            // given
            int threadCount = 5;
            int uuidsPerThread = 200;
            ExecutorService executor = Executors.newFixedThreadPool(threadCount);

            List<UUID> allUuids = Collections.synchronizedList(new ArrayList<>());
            CountDownLatch startLatch = new CountDownLatch(1);
            CountDownLatch endLatch = new CountDownLatch(threadCount);

            // when
            for (int i = 0; i < threadCount; i++) {
                executor.submit(() -> {
                    try {
                        startLatch.await(); // 모든 스레드가 동시에 시작하도록
                        List<UUID> threadUuids = new ArrayList<>();
                        for (int j = 0; j < uuidsPerThread; j++) {
                            threadUuids.add(UUIDUtils.createUUIDv7());
                        }
                        allUuids.addAll(threadUuids);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    } finally {
                        endLatch.countDown();
                    }
                });
            }

            startLatch.countDown(); // 모든 스레드 시작
            executor.shutdown();

            List<Long> timestamps = allUuids.stream()
                    .map(UUIDUtils::extractTimestamp)
                    .sorted()
                    .toList();

            // then
            assertThat(endLatch.await(10, TimeUnit.SECONDS)).isTrue();
            for (int i = 1; i < timestamps.size(); i++) {
                assertThat(timestamps.get(i) >= timestamps.get(i - 1)).isTrue();
            }
        }
    }
}