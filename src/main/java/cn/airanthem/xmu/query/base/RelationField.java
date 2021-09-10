package cn.airanthem.xmu.query.base;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.lang.reflect.Field;

/**
 * 标注@Relation的域
 * @author zhongtianyun
 * @version 1.0
 * @created 2021/9/7 16:01
 */
@Data
@AllArgsConstructor
public class RelationField {
    Field field;
    String table;
    String column;
}
