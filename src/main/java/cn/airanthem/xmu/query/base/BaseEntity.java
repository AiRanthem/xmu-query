package cn.airanthem.xmu.query.base;

import cn.airanthem.xmu.query.mapper.BaseEntityMapper;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cn.airanthem.xmu.query.utils.QueryUtils.getEntityMeta;

/**
 * @author zhongtianyun
 * @version 1.0
 * @created 2021/9/7 12:06
 */
public class BaseEntity {
    @TableId(type = IdType.AUTO)
    private Long id;

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
        XmuQuery<?> query = new XmuQuery<>(__mapper, meta);

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
                    map.put(f, query.getRelationObjects(
                            field.getTable(), field.getColumn(), (String) field.getField().get(this)));
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("Unknown error", e);
                }
            }
        }
        return map;
    }
}
