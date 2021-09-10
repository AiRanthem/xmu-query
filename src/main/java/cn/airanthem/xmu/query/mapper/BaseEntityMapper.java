package cn.airanthem.xmu.query.mapper;

import cn.airanthem.xmu.query.base.BaseEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

public interface BaseEntityMapper<T extends BaseEntity> extends BaseMapper<T> {

    @Select("select * from ${table} where id = ${id}")
    Map<String, Object> selectResourceById(
            @Param("table") String table,
            @Param("id") Long id
    );

    @Select("select * from ${table} where ${column} = \"${value}\"")
    List<Map<String, Object>> selectResourceByColumn(
            @Param("table") String table,
            @Param("column") String column,
            @Param("value") String value
    );
}
