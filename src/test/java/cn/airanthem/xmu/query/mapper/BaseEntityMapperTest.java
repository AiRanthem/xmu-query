package cn.airanthem.xmu.query.mapper;

import cn.airanthem.xmu.query.fake.DemoEntity;
import cn.airanthem.xmu.query.fake.DemoEntityMapperImpl;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BaseEntityMapperTest {

    @Test
    void getTClass() {
        DemoEntityMapperImpl mapper = new DemoEntityMapperImpl();
    }
}