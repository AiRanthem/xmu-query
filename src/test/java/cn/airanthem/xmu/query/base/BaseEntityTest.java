package cn.airanthem.xmu.query.base;

import cn.airanthem.xmu.query.fake.DemoEntity;
import cn.airanthem.xmu.query.utils.QueryUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BaseEntityTest {

    @Test
    void getEntityMeta() {
        EntityMeta entityMeta = QueryUtils.getEntityMeta(DemoEntity.class);
        assertEquals(
                1, entityMeta.getRelationFields().size()
        );
        assertEquals(
                1, entityMeta.getPlainFields().size()
        );
        assertTrue(
                entityMeta.getPlainFields().containsKey("plain")
        );
        assertTrue(
                entityMeta.getRelationFields().containsKey("relation")
        );
    }
}