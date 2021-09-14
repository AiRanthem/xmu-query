package cn.airanthem.xmu.query.factory;

import cn.airanthem.xmu.query.base.AbstractRelationField;
import cn.airanthem.xmu.query.base.BaseEntity;
import cn.airanthem.xmu.query.base.EntityMeta;
import cn.airanthem.xmu.query.base.RelationField;
import cn.airanthem.xmu.query.mapper.BaseEntityMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static cn.airanthem.xmu.query.base.AbstractRelationField.NOT_EXIST;

public class BaseEntityFactory<T extends BaseEntity> {

    private static final Logger LOG = LoggerFactory.getLogger(BaseEntityFactory.class);

    private final BaseEntityMapper<T> mapper;

    private final Class<T> clazz;

    private final EntityMeta meta;

    private final static String SUF_COL = "suffix";

    public BaseEntityFactory(BaseEntityMapper<T> mapper, Class<T> clazz, EntityMeta meta) {
        this.mapper = mapper;
        this.clazz = clazz;
        this.meta = meta;
    }

    /**
     * get a entity instance from map. please make sure you have already inserted sub tables before this job.
     *
     * @param map a map
     * @return an entity
     */
    public T getFromMap(Map<String, Object> map) {
        try {
            T instance = clazz.getDeclaredConstructor().newInstance();
            ArrayList<String> missingFields = new ArrayList<>();
            for (Map.Entry<String, Field> ent : meta.getPlainFields().entrySet()) {
                String key = ent.getKey();
                Object value = map.get(key);
                if (value == null) {
                    missingFields.add(key);
                    continue;
                }
                Field field = ent.getValue();
                field.setAccessible(true);
                field.set(instance, value);
            }
            // table.column.value -> id
            HashMap<String, Long> buf = new HashMap<>();
            for (Map.Entry<String, RelationField> ent : meta.getRelationFields().entrySet()) {
                String key = ent.getKey();
                Object valueObj = map.get(key);
                if (!(valueObj instanceof String)) {
                    missingFields.add(key);
                    continue;
                }

                AbstractRelationField rf = ent.getValue();
                String[] values = ((String) valueObj).split(",");
                String indices = Arrays.stream(values).map(value -> {
                    String searchKey = String.join(".", rf.getTable(), rf.getColumn(), value);
                    Long id = buf.get(searchKey);
                    if (id == null) {
                        List<Map<String, Object>> rows = mapper.selectResourceByColumn(rf.getTable(), rf.getColumn(), value);
                        id = rf.whichId(rows);
                        buf.put(searchKey, id);
                    }
                    return id;
                }).filter(l -> !l.equals(NOT_EXIST)).map(Objects::toString).collect(Collectors.joining(","));

                Field field = rf.getField();
                field.setAccessible(true);
                field.set(instance, indices);
            }

            if (!missingFields.isEmpty()) {
                LOG.warn(String.format("fields [%s] missing in map, set to default, probably null", String.join(",", missingFields)));
            }
            return instance;
        } catch (InvocationTargetException | InstantiationException | NoSuchMethodException | IllegalAccessException e) {
            LOG.error(String.format("cannot make an %s entity from map %s", clazz.getName(), map), e);
            return null;
        }
    }
}
