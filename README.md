# XMU-Query

适用于各种查询系统的组件

## Cases

### case1 单表查询

业务集中与单表查询。如：学生名册、流水账等。

| 学号 | 姓名 | 性别 | 年级 | 专业 |
| --- | --- | --- | --- | --- | 
| 24320172203601| 张三 | 男 | 2017 | 软件工程
| 24320172203602| 李四 | 男 | 2017 | 人工智能
| 24320172203603| 王五 | 女 | 2017 | 数字媒体技术

### case2 主次表联系查询

业务集中与一张主表记录所有信息（存在逻辑外键）。如：选课信息等。

**主表**

主表中包含了需要增删改查的所有信息，部分列可能存在逻辑外键的情况（如下表的教师、学生）

| 课程号 | 教师 | 学生 | 课程名
| --- | --- | --- | --- 
| 101 | 1 | 1,2 | 算法设计与分析
| 102 | 2 | 1,3 | 计算机体系结构

**副表**

副表由主表的逻辑外键进行查询

| 学号 | 姓名 | 专业
| --- | --- | ---
| 1 | 小王 | 软件工程
| 2 | 小张 | 软件工程
| 3 | 小李 | 数字媒体技术

| 教工号 | 姓名 | 职称
| --- | --- | ---
| 1 | 白老师 | 副教授
| 2 | 钱老师 | 教授

## Feature

`xmu-query` 针对上面两种情况进行特别优化，完全封装查询逻辑，并且提供大量实用方法以供定制。

## Installation

首先在本地maven仓库进行安装

```shell
git clone http://out.gitlab.airanthem.cn/AiRanthem/xmu-query.git
cd xmu-query
mvn clean compile install
```

然后在目标项目中添加依赖。注意：该依赖包含了 `mybatis-plus-boot-starter`

```xml

<dependency>
    <groupId>cn.airanthem.xmu</groupId>
    <artifactId>xmu-query-boot-starter</artifactId>
    <version>0.0.1-SNAPSHOT</version>
</dependency>
```

## Quick Start

### 单表查询

单表查询的情况下，本质上就是传统ORM框架做的事情。以如下数据为例：

| 学号 | 姓名 | 性别 | 年级 | 专业 |
| --- | --- | --- | --- | --- | 
| 24320172203601| 张三 | 男 | 2017 | 软件工程
| 24320172203602| 李四 | 男 | 2017 | 人工智能
| 24320172203603| 王五 | 女 | 2017 | 数字媒体技术

首先，建立数据表后，编写实体类：

```java
import cn.airanthem.xmu.query.base.BaseEntity;

class Student extends BaseEntity {
    /**
     * 学号
     */
    String stuNo;

    /**
     * 姓名
     */
    String name;

    /**
     * 性别
     */
    String sexture;

    /**
     * 年级
     */
    Integer grade;

    /**
     * 专业
     */
    String department;
}
```

之后，编写Mapper类。这个Mapper其实继承自BaseMapper，具有Mybatis-Plus的所有功能，可以直接使用。

```java
import cn.airanthem.xmu.query.mapper.BaseEntityMapper;

interface StudentMapper extends BaseEntityMapper<Student> {
}
```

最后，使用`XmuQuery`对象进行查询。完整的方法见方法文档（ 我还没写:) ）

```java
import cn.airanthem.xmu.query.base.QueryResult;
import cn.airanthem.xmu.query.facade.XmuQuery;

@Service
class SomeService() {
    @Autowired
    private StudentMapper studentMapper;

    public void doQuery() {
        XmuQuery<Student> query = new XmuQuery<>(studentMapper, Student.class);
        QueryResult<Student> result = query.easyAnd("stu_no", "243")
                .easyOr("name", "张") // 搜索学号包含243 OR 姓名包含张。查询条件是数据库列名。
                .getAll(); // 执行检索
        List<Student> records = result.getRecords(); // 取出所有的实体
        Long total = result.getTotal(); // 取出总数

        // 将一些字段按照数据库列名导出为Map
        result.exportAll(Arrays.asList("stu_no", "name"));
        List<Map<String, Object>> exportedRecords = result.getExportedRecords();
    }
}
```

### 多表查询

以下面的主副表为例

**主表**

|自增主键| 课程号 | 教师 | 学生 | 课程名
|--- | --- | --- | --- | --- 
|10001 | 101 | 1 | 1,2 | 算法设计与分析
|10002 | 102 | 2 | 1,3 | 计算机体系结构

**副表**

副表请保证存在名为`id`的一列以供主表唯一定位。

| id | 姓名 | 专业
| --- | --- | ---
| 1 | 小王 | 软件工程
| 2 | 小张 | 软件工程
| 3 | 小李 | 数字媒体技术

| id | 姓名 | 职称
| --- | --- | ---
| 1 | 白老师 | 副教授
| 2 | 钱老师 | 教授

首先为主表编写实体类，副表不需要考虑。

```java
import cn.airanthem.xmu.query.annotation.Id;
import cn.airanthem.xmu.query.annotation.Relation;
import cn.airanthem.xmu.query.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;

class Course extends BaseEntity {
    // 标注为Id的字段无法导出
    @Id
    private Long cid;

    // 可以通过MP的标准注解来指定列。
    @TableField(value = "course_no")
    private Long cno;

    // Relation标签对应逻辑外键列，目标数据存在的取数的表与列。注意：Relation字段需要是字符串
    @Relation(table = "teacher", column = "name")
    private String teachers;

    @Relation(table = "student", column = "name")
    private String students;

    // 字面值字段依旧不做处理
    private String name;
}
```

同样编写Mapper

```java
import cn.airanthem.xmu.query.mapper.BaseEntityMapper;

interface CourseMapper extends BaseEntityMapper<Course> {
}
```

最后与单表查询一样的进行查询

```java
import cn.airanthem.xmu.query.base.QueryResult;
import cn.airanthem.xmu.query.facade.XmuQuery;

@Service
class SomeService() {
    @Autowired
    private CourseMapper courseMapper;

    public void doQuery() {
        XmuQuery<Student> query = new XmuQuery<>(courseMapper, Course.class);
        QueryResult<Student> result = query.easyAnd("students", "小王")
                .easyOr("teachers", "白老师") // 直接通过字面值进行搜索，不需要考虑细节
                .getAll(); // 执行检索
        /*
         * 取出所有的实体。此时的实体的联系字段还是保存的原始的123
         * Course(teachers = "1", students = "1,2")
         */
        List<Course> records = result.getRecords();

        /*
         * 将一些字段按照数据库列名导出为Map。导出后，联系字段会转化为实际值。
         * {
         *      "students": "小王,小张",
         *      "teachers": "白老师",
         *      "name": "算法设计与分析"
         * }
         */
        result.exportAll(Arrays.asList("students", "teachers", "name"));
        List<Map<String, Object>> exportedRecords = result.getExportedRecords();
    }
}
```

## To-Do

1. 增加增删改功能，完全屏蔽Mybatis-Plus
2. 更多定制化的查询功能