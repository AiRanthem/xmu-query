package cn.airanthem.xmu.query.query;

import cn.airanthem.xmu.query.base.XmuQuery;
import cn.airanthem.xmu.query.fake.DemoEntity;
import cn.airanthem.xmu.query.fake.DemoEntityMapper;
import cn.airanthem.xmu.query.fake.DemoEntityMapperImpl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class XmuQueryTest {
    private DemoEntityMapper mapper;

    @BeforeEach
    void setup() {
        mapper = new DemoEntityMapperImpl();
    }

    @Test
    void testWrapper() {
        XmuQuery<DemoEntity> query = new XmuQuery<>(mapper, DemoEntity.class);
        query.plainQueryAndEq("eqand", "eqval")
                .plainQueryAndLike("likeand", "likeval");
        QueryWrapper<DemoEntity> wrapper = query.getWrapper();
        assertEquals("((eqand = ?) AND (likeand LIKE ?))", wrapper.getTargetSql());
    }

    @Test
    void testGetTClass() {
    }
}