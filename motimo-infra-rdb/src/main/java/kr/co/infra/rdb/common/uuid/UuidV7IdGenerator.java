package kr.co.infra.rdb.common.uuid;

import kr.co.infra.rdb.common.util.UuidUtils;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

public class UuidV7IdGenerator implements IdentifierGenerator {
    @Override
    public Object generate(SharedSessionContractImplementor session, Object object) {
        return UuidUtils.randomV7();
    }
}
