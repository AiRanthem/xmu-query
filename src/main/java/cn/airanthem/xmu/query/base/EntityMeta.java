package cn.airanthem.xmu.query.base;

import lombok.Data;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Entity反射元数据
 * @author zhongtianyun
 * @version 1.0
 * @created 2021/9/7 15:27
 */
@Data
public class EntityMeta {
    Field idField;
    Map<String, RelationField> relationFields = new HashMap<>();
    Map<String, Field> plainFields = new HashMap<>();
}
