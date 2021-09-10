package cn.airanthem.xmu.query.facade;

import cn.airanthem.xmu.query.base.BaseEntity;
import cn.airanthem.xmu.query.base.Buffer;
import cn.airanthem.xmu.query.base.EntityMeta;
import cn.airanthem.xmu.query.base.QueryResult;
import cn.airanthem.xmu.query.base.RelationField;
import cn.airanthem.xmu.query.enums.BiLogic;
import cn.airanthem.xmu.query.enums.OrderTypeEnum;
import cn.airanthem.xmu.query.mapper.BaseEntityMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Sets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class XmuQuery<T extends BaseEntity> {
    private static final Logger LOG = LoggerFactory.getLogger(XmuQuery.class);

    private final QueryWrapper<T> wrapper = new QueryWrapper<>();

    private static final Set<Character> idListSupportedChars = Sets.newHashSet('1', '2', '3', '4', '5', '6', '7', '8', '9', '0', ',');

    private final BaseEntityMapper<T> mapper;

    private final EntityMeta meta;

    public XmuQuery(BaseEntityMapper<T> mapper, Class<T> clazz) {
        this.mapper = mapper;
        this.meta = BaseEntity.getEntityMeta(clazz);
    }

    /**
     * 复用缓存的元数据减少反射的构造器
     */
    public XmuQuery(BaseEntityMapper<T> mapper, EntityMeta meta) {
        this.mapper = mapper;
        this.meta = meta;
    }

    /**
     * 请在数据库更新后调用此方法刷新缓存
     */
    public static void flushBuffer() {
        Buffer.reset();
    }

    public QueryWrapper<T> getWrapper() {
        return wrapper;
    }

    public XmuQuery<T> easyAnd(String field, Object value) {
        return easyQuery(field, value, BiLogic.AND);
    }

    public XmuQuery<T> easyOr(String field, Object value) {
        return easyQuery(field, value, BiLogic.OR);
    }

    public XmuQuery<T> easyNot(String field, Object value) {
        return easyQuery(field, value, BiLogic.NOT);
    }

    public XmuQuery<T> easyQuery(String field, Object value, BiLogic logic) {
        Map<String, RelationField> relationFields = meta.getRelationFields();
        Map<String, Field> plainFields = meta.getPlainFields();
        if (plainFields.containsKey(field)) {
            switch (logic) {
                case AND:
                    return plainQueryAndLike(field, value);
                case OR:
                    return plainQueryOrLike(field, value);
                case NOT:
                    return plainQueryNotLike(field, value);
            }
        } else if (relationFields.containsKey(field)) {
            RelationField rf = relationFields.get(field);
            switch (logic) {
                case AND:
                    return relationQueryAnd(rf.getTable(), rf.getColumn(), field, value);
                case OR:
                    return relationQueryOr(rf.getTable(), rf.getColumn(), field, value);
                case NOT:
                    return relationQueryNot(rf.getTable(), rf.getColumn(), field, value);
            }
        }
        LOG.warn(String.format("easy query warning: field %s not found in [easy%s, %s, %s]", field, logic.getValue(), field, value));
        return this;
    }

    /**
     * 构造字面值的 AND (field LIKE %value%) 条件
     */
    public XmuQuery<T> plainQueryAndLike(String field, Object value) {
        wrapper.and(w -> w.like(field, value));
        return this;
    }

    /**
     * 构造字面值的 OR (field LIKE %value%) 条件
     */
    public XmuQuery<T> plainQueryOrLike(String field, Object value) {
        wrapper.or(w -> w.like(field, value));
        return this;
    }

    /**
     * 构造字面值的 NOT (field LIKE %value%) 条件
     */
    public XmuQuery<T> plainQueryNotLike(String field, Object value) {
        wrapper.not(w -> w.like(field, value));
        return this;
    }

    /**
     * 构造字面值的 AND (field = value) 条件
     */
    public XmuQuery<T> plainQueryAndEq(String field, Object value) {
        wrapper.and(w -> w.eq(field, value));
        return this;
    }

    /**
     * 构造字面值的 OR (field = value) 条件
     */
    public XmuQuery<T> plainQueryOrEq(String field, Object value) {
        wrapper.or(w -> w.eq(field, value));
        return this;
    }

    /**
     * 构造字面值的 NOT (field = value) 条件
     */
    public XmuQuery<T> plainQueryNotEq(String field, Object value) {
        wrapper.not(w -> w.eq(field, value));
        return this;
    }

    /**
     * 构造关联索引的 AND 条件
     */
    public XmuQuery<T> relationQueryAnd(String table, String column, String field, Object value) {
        wrapper.and(w -> relationQueryPart(w, table, column, field, value));
        return this;
    }

    /**
     * 构造关联索引的 OR 条件
     */
    public XmuQuery<T> relationQueryOr(String table, String column, String field, Object value) {
        wrapper.or(w -> relationQueryPart(w, table, column, field, value));
        return this;
    }

    /**
     * 构造关联索引的 NOT 条件
     */
    public XmuQuery<T> relationQueryNot(String table, String column, String field, Object value) {
        wrapper.not(w -> relationQueryPart(w, table, column, field, value));
        return this;
    }

    private void relationQueryPart(QueryWrapper<?> w, String table, String column, String field, Object value) {
        for (Long relatedId : getRelatedIds(table, column, value)) {
            w.or().like(field, relatedId);
        }
    }

    private List<Long> getRelatedIds(String table, String column, Object value) {
        List<Map<String, Object>> relatedData = queryDbByColumn(table, column, value.toString());
        ArrayList<Long> ids = new ArrayList<>();
        for (Map<String, Object> relatedDatum : relatedData) {
            Long id = parseId(relatedDatum.get("id"));
            Buffer.updateBuffer(table, id, relatedDatum);
            ids.add(id);
        }
        return ids;
    }

    /**
     * 根据关联索引列的值获取其实际值
     */
    public String getRelationObjects(String table, String column, String idList) throws RuntimeException {
        for (char c : idList.toCharArray()) {
            if (!idListSupportedChars.contains(c)) {
                throw new RuntimeException("bad id list string " + idList);
            }
        }
        List<Long> ids = Arrays.stream(idList.split(",")).map(Long::valueOf).collect(Collectors.toList());
        return getValues(table, column, ids).stream().map(Object::toString).collect(Collectors.joining(","));
    }

    /**
     * 分页获取对象，不排序
     */
    public QueryResult<T> getPage(Integer pageNo, Integer pageSize) {
        return getPage(pageNo, pageSize, null, OrderTypeEnum.ASC);
    }

    /**
     * 分页获取对象，排序
     */
    public QueryResult<T> getPage(Integer pageNo, Integer pageSize, String orderCol, OrderTypeEnum orderType) {
        order(orderCol, orderType);
        Page<T> page = mapper.selectPage(new Page<>(pageNo, pageSize), wrapper);
        page.getRecords().forEach(
                r -> r.setBaseEntityMapper(mapper)
        );
        return new QueryResult<>(page.getRecords(), page.getTotal(), meta);
    }

    /**
     * 获取所有对象，不排序
     */
    public QueryResult<T> getAll() {
        return getAll(null, OrderTypeEnum.ASC);
    }

    /**
     * 获取所有对象，排序
     */
    public QueryResult<T> getAll(String orderCol, OrderTypeEnum orderType) {
        order(orderCol, orderType);
        List<T> records = mapper.selectList(wrapper);
        records.forEach(r -> r.setBaseEntityMapper(mapper));
        return new QueryResult<>(records, ((long) records.size()), meta);
    }

    /**
     * 构造排序条件
     */
    public void order(String col, OrderTypeEnum type) {
        if (col != null) {
            if (type == OrderTypeEnum.ASC) {
                wrapper.orderByAsc(col);
            } else {
                wrapper.orderByDesc(col);
            }
        }
    }

    private List<Object> getValues(String table, String column, List<Long> ids) {
        ArrayList<Object> values = new ArrayList<>(ids.size());
        for (Long id : ids) {
            Map<String, Object> buf = Buffer.queryBufById(table, id);
            if (buf == null) {
                buf = queryDbById(table, id);
                Buffer.updateBuffer(table, id, buf);
            }
            Object val = buf.get(column);
            if (val == null) {
                throw new RuntimeException(String.format("table %s doesn't have column %s", table, column));
            }
            values.add(val);
        }
        return values;
    }

    private Long parseId(Object idObj) {
        try {
            return (Long) idObj;
        } catch (Exception e) {
            return ((Integer) idObj).longValue();
        }
    }

    private List<Map<String, Object>> queryDbByColumn(String table, String column, String value) {
        return mapper.selectResourceByColumn(table, column, value);
    }

    private Map<String, Object> queryDbById(String table, Long id) {
        return mapper.selectResourceById(table, id);
    }
}
