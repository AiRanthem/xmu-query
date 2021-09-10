package cn.airanthem.xmu.query.fake;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class FakeBaseEntityMapperImpl implements FakeBaseEntityMapper {
    @Override
    public Map<String, Object> selectResourceById(String table, Long id) {
        return null;
    }

    @Override
    public List<Map<String, Object>> selectResourceByColumn(String table, String column, String value) {
        return null;
    }

    @Override
    public int insert(EmptyEntity entity) {
        return 0;
    }

    @Override
    public int deleteById(Serializable id) {
        return 0;
    }

    @Override
    public int deleteById(EmptyEntity entity) {
        return 0;
    }

    @Override
    public int deleteByMap(Map<String, Object> columnMap) {
        return 0;
    }

    @Override
    public int delete(Wrapper<EmptyEntity> queryWrapper) {
        return 0;
    }

    @Override
    public int deleteBatchIds(Collection<? extends Serializable> idList) {
        return 0;
    }

    @Override
    public int updateById(EmptyEntity entity) {
        return 0;
    }

    @Override
    public int update(EmptyEntity entity, Wrapper<EmptyEntity> updateWrapper) {
        return 0;
    }

    @Override
    public EmptyEntity selectById(Serializable id) {
        return null;
    }

    @Override
    public List<EmptyEntity> selectBatchIds(Collection<? extends Serializable> idList) {
        return null;
    }

    @Override
    public List<EmptyEntity> selectByMap(Map<String, Object> columnMap) {
        return null;
    }

    @Override
    public Long selectCount(Wrapper<EmptyEntity> queryWrapper) {
        return null;
    }

    @Override
    public List<EmptyEntity> selectList(Wrapper<EmptyEntity> queryWrapper) {
        return null;
    }

    @Override
    public List<Map<String, Object>> selectMaps(Wrapper<EmptyEntity> queryWrapper) {
        return null;
    }

    @Override
    public List<Object> selectObjs(Wrapper<EmptyEntity> queryWrapper) {
        return null;
    }

    @Override
    public <P extends IPage<EmptyEntity>> P selectPage(P page, Wrapper<EmptyEntity> queryWrapper) {
        return null;
    }

    @Override
    public <P extends IPage<Map<String, Object>>> P selectMapsPage(P page, Wrapper<EmptyEntity> queryWrapper) {
        return null;
    }
}
