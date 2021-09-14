package cn.airanthem.xmu.query.base;

import cn.airanthem.xmu.query.utils.QueryUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 标注@Relation的域
 *
 * @author zhongtianyun
 * @version 1.0
 * @created 2021/9/7 16:01
 */
public class RelationField extends AbstractRelationField {

    public RelationField(Field field, String table, String column) {
        super(field, table, column);
    }

    @Override
    Long selectIdStrategy(List<Map<String, Object>> rows) {
        return QueryUtils.parseId(rows.get(0).get(XmuQuery.ID_COL));
    }
}
