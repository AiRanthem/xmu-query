package cn.airanthem.xmu.query.base;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
public abstract class AbstractRelationField {
    Field field;
    String table;
    String column;

    private static final Logger LOG = LoggerFactory.getLogger(AbstractRelationField.class);

    public static final Long NOT_EXIST = -1L;

    /**
     * 当获取到值有多行时，选择哪一行的ID策略
     *
     * @return 选中的行
     */
    public Long whichId(List<Map<String, Object>> rows) {
        if (rows.isEmpty()) {
            return NOT_EXIST;
        }
        try {
            return selectIdStrategy(rows);
        } catch (Exception e) {
            LOG.error("selectIdStrategy error", e);
            return NOT_EXIST;
        }
    }

    abstract Long selectIdStrategy(List<Map<String, Object>> rows);
}
