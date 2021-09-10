package cn.airanthem.xmu.query.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ID字段注解
 * @author zhongtianyun
 * @version 1.0
 * @created 2021/9/7 12:09
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Relation {
    String table();
    String column();
}
