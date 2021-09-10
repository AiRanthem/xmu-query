package cn.airanthem.xmu.query.base;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
public class QueryResult <T extends BaseEntity> {
    private static final Logger LOG = LoggerFactory.getLogger(QueryResult.class);

    private final List<T> records;
    private final Long total;
    private final EntityMeta meta;
    private List<Map<String, Object>> exportedRecords;

    public QueryResult(List<T> records, Long total, EntityMeta meta) {
        this.records = records;
        this.total = total;
        this.meta = meta;
    }

    public void exportAll(List<String> fields) {
        exportedRecords = records.stream().map(e -> {
            try {
                return e.export(fields, meta);
            } catch (Exception ex) {
                LOG.error("data export error: ", ex);
                throw new RuntimeException(String.format(
                        "data entity query succeed, but export fields %s failed", fields
                ), ex);
            }
        }).collect(Collectors.toList());
    }
}
