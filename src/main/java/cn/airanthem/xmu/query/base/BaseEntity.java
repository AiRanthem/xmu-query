package cn.airanthem.xmu.query.base;

import cn.airanthem.xmu.query.annotation.Id;
import cn.airanthem.xmu.query.annotation.Relation;
import cn.airanthem.xmu.query.facade.XmuQuery;
import cn.airanthem.xmu.query.mapper.BaseEntityMapper;
import com.baomidou.mybatisplus.annotation.TableField;
import com.google.common.base.CaseFormat;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhongtianyun
 * @version 1.0
 * @created 2021/9/7 12:06
 */
public class BaseEntity {
    @TableField(exist = false)
    private BaseEntityMapper<?> __mapper;

    public void setBaseEntityMapper(BaseEntityMapper<?> mapper) {
        this.__mapper = mapper;
    }

    public Map<String, Object> export(List<String> fields) {
        return export(fields, getEntityMeta(getClass()));
    }

    public Map<String, Object> export(List<String> fields, EntityMeta meta) {
        Map<String, Field> plainFields = meta.getPlainFields();
        Map<String, RelationField> relationFields = meta.getRelationFields();

        HashMap<String, Object> map = new HashMap<>();
        for (String f : fields) {
            if (plainFields.containsKey(f)) {
                Field field = plainFields.get(f);
                field.setAccessible(true);
                try {
                    map.put(f, field.get(this));
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("Unknown error", e);
                }
            } else if (relationFields.containsKey(f)) {
                RelationField field = relationFields.get(f);
                field.getField().setAccessible(true);
                try {
                    map.put(f, new XmuQuery<>(__mapper, meta).getRelationObjects(
                            field.getTable(), field.getColumn(), (String) field.getField().get(this)));
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("Unknown error", e);
                }
            }
        }
        return map;
    }

    public static EntityMeta getEntityMeta(Class<?> clazz) {
        EntityMeta entityMeta = new EntityMeta();
        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(Id.class)) {
                entityMeta.setIdField(field);
            } else if (field.isAnnotationPresent(Relation.class)) {
                Relation relation = field.getAnnotation(Relation.class);
                entityMeta.getRelationFields().put(
                        camel2snake(field.getName()), new RelationField(field, relation.table(), relation.column())
                );
            } else {
                if (field.isAnnotationPresent(TableField.class)) {
                    TableField tableField = field.getAnnotation(TableField.class);
                    if (tableField.exist()) {
                        entityMeta.getPlainFields().put(
                                tableField.value(), field
                        );
                    }
                } else {
                    entityMeta.getPlainFields().put(
                            camel2snake(field.getName()), field
                    );
                }
            }
        }
        return entityMeta;
    }

    private static String camel2snake(String camel) {
        return CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, camel);
    }
}
