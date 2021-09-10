package cn.airanthem.xmu.query.base;

import cn.airanthem.xmu.query.fake.DemoEntity;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BaseEntityTest {

    @Test
    void getEntityMeta() {
        EntityMeta entityMeta = BaseEntity.getEntityMeta(DemoEntity.class);
        assertEquals(
                "id", entityMeta.getIdField().getName()
        );
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