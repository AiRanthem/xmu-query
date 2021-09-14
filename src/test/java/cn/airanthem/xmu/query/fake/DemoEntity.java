package cn.airanthem.xmu.query.fake;


import cn.airanthem.xmu.query.annotation.Relation;
import cn.airanthem.xmu.query.base.BaseEntity;

public class DemoEntity extends BaseEntity {

    @Relation(table = "table", column = "column")
    private String relation;

    private String plain;
}
