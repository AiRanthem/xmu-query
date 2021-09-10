package cn.airanthem.xmu.query.base;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Buffer {
    private final static Map<String, Map<Long, Map<String, Object>>> buffer = new ConcurrentHashMap<>();

    public static void reset() {
        buffer.clear();
    }

    public static void updateBuffer(String table, Long id, Map<String, Object> value) {
        Map<Long, Map<String, Object>> tableBuf = buffer.computeIfAbsent(table, k -> new HashMap<>());
        tableBuf.put(id, value);
    }

    public static Map<String, Object> queryBufById(String table, Long id) {
        Map<Long, Map<String, Object>> tableBuf = buffer.get(table);
        if (tableBuf == null) {
            return null;
        }
        return tableBuf.get(id);
    }
}
