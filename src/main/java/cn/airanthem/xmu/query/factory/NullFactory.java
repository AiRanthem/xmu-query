package cn.airanthem.xmu.query.factory;

import cn.airanthem.xmu.query.base.BaseEntity;

import java.util.Map;

public class NullFactory <T extends BaseEntity> extends BaseEntityFactory<T>{

    public NullFactory() {
        super(null, null, null);
    }

    @Override
    public T getFromMap(Map<String, Object> map) {
        return null;
    }
}
