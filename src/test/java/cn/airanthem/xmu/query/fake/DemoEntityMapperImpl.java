package cn.airanthem.xmu.query.fake;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class DemoEntityMapperImpl implements DemoEntityMapper{
    @Override
    public Map<String, Object> selectResourceById(String table, Long id) {
        return null;
    }

    @Override
    public List<Map<String, Object>> selectResourceByColumn(String table, String column, String value) {
        return null;
    }

    @Override
    public int insert(DemoEntity entity) {
        return 0;
    }

    @Override
    public int deleteById(Serializable id) {
        return 0;
    }

    @Override
    public int deleteById(DemoEntity entity) {
        return 0;
    }

    @Override
    public int deleteByMap(Map<String, Object> columnMap) {
        return 0;
    }

    @Override
    public int delete(Wrapper<DemoEntity> queryWrapper) {
        return 0;
    }

    @Override
    public int deleteBatchIds(Collection<? extends Serializable> idList) {
        return 0;
    }

    @Override
    public int updateById(DemoEntity entity) {
        return 0;
    }

    @Override
    public int update(DemoEntity entity, Wrapper<DemoEntity> updateWrapper) {
        return 0;
    }

    @Override
    public DemoEntity selectById(Serializable id) {
        return null;
    }

    @Override
    public List<DemoEntity> selectBatchIds(Collection<? extends Serializable> idList) {
        return null;
    }

    @Override
    public List<DemoEntity> selectByMap(Map<String, Object> columnMap) {
        return null;
    }

    @Override
    public Long selectCount(Wrapper<DemoEntity> queryWrapper) {
        return null;
    }

    @Override
    public List<DemoEntity> selectList(Wrapper<DemoEntity> queryWrapper) {
        return null;
    }

    @Override
    public List<Map<String, Object>> selectMaps(Wrapper<DemoEntity> queryWrapper) {
        return null;
    }

    @Override
    public List<Object> selectObjs(Wrapper<DemoEntity> queryWrapper) {
        return null;
    }

    @Override
    public <P extends IPage<DemoEntity>> P selectPage(P page, Wrapper<DemoEntity> queryWrapper) {
        return null;
    }

    @Override
    public <P extends IPage<Map<String, Object>>> P selectMapsPage(P page, Wrapper<DemoEntity> queryWrapper) {
        return null;
    }
}
