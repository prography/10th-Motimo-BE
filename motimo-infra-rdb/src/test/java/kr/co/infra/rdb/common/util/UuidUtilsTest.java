package kr.co.infra.rdb.common.util;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UuidUtilsTest {
    @Test
    public void uuidV7_정상_생성() {
        // given
        UUID uuid1 = UuidUtils.randomV7();
        UUID uuid2 = UuidUtils.randomV7();

        long timestamp1 = UuidUtils.extractTimestamp(uuid1);
        long timestamp2 = UuidUtils.extractTimestamp(uuid2);

        // when & then
        assertTrue(uuid1.toString().compareTo(uuid2.toString()) < 0);
        assertThat(uuid1.version()).isEqualTo(7);
        assertThat(uuid2.version()).isEqualTo(7);
        assertThat(timestamp1 <= timestamp2).isTrue();
    }
}