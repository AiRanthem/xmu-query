package cn.airanthem.xmu.query.fake;


import cn.airanthem.xmu.query.annotation.Id;
import cn.airanthem.xmu.query.annotation.Relation;
import cn.airanthem.xmu.query.base.BaseEntity;

public class DemoEntity extends BaseEntity {
    @Id
    private Long id;

    @Relation(table = "table", column = "column")
    private String relation;

    private String plain;
}
