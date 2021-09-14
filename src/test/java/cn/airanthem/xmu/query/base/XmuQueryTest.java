package cn.airanthem.xmu.query.base;

import cn.airanthem.xmu.query.fake.DemoEntity;
import cn.airanthem.xmu.query.fake.DemoEntityMapperImpl;
import cn.airanthem.xmu.query.mapper.BaseEntityMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class XmuQueryTest {

    @Test
    void relationQueryOr() {
        DemoEntityMapperImpl mapper = new DemoEntityMapperImpl();
        XmuQuery<DemoEntity> query = new XmuQuery<>(mapper, DemoEntity.class);
        query.relationQueryOr("notexist", "notexist", "any", "any");
        System.out.println(query.getWrapper().getTargetSql());
    }
}