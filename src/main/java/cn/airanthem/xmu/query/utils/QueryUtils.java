package cn.airanthem.xmu.query.utils;

import cn.airanthem.xmu.query.annotation.Relation;
import cn.airanthem.xmu.query.base.EntityMeta;
import cn.airanthem.xmu.query.base.RelationField;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.base.CaseFormat;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class QueryUtils {
    public static void relationQueryPart(QueryWrapper<?> w, String field, List<Long> relatedIds) {
        for (Long relatedId : relatedIds) {
            w.or().like(field, relatedId);
        }
    }

    public static Long parseId(Object idObj) {
        try {
            return (Long) idObj;
        } catch (Exception e) {
            return ((Integer) idObj).longValue();
        }
    }

    public static String camel2snake(String camel) {
        return CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, camel);
    }

    public static EntityMeta getEntityMeta(Class<?> clazz) {
        EntityMeta entityMeta = new EntityMeta();
        List<Field> fields = Arrays.stream(clazz.getDeclaredFields()).filter(f -> {
            if (f.isAnnotationPresent(TableField.class)) {
                TableField tableField = f.getAnnotation(TableField.class);
                return tableField.exist();
            }
            return true;
        }).collect(Collectors.toList());

        for (Field field : fields) {
            if (field.isAnnotationPresent(Relation.class)) {
                Relation relation = field.getAnnotation(Relation.class);
                entityMeta.getRelationFields().put(
                        getFieldName(field), new RelationField(field, relation.table(), relation.column())
                );
            } else {
                entityMeta.getPlainFields().put(getFieldName(field), field);
            }
        }
        return entityMeta;
    }

    public static String getFieldName(Field field) {
        if (field.isAnnotationPresent(TableField.class)) {
            TableField tableField = field.getAnnotation(TableField.class);
            return tableField.value();
        } else {
            return camel2snake(field.getName());
        }
    }
}
