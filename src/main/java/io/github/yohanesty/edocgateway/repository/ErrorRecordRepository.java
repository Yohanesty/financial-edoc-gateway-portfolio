package io.github.yohanesty.edocgateway.repository;

import io.github.yohanesty.edocgateway.model.ErrorRecord;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ErrorRecordRepository {
    private final Map<String, ErrorRecord> store = new ConcurrentHashMap<String, ErrorRecord>();

    public ErrorRecord save(ErrorRecord record) {
        store.put(record.getId(), record);
        return record;
    }

    public ErrorRecord find(String id) {
        return store.get(id);
    }

    public Collection<ErrorRecord> findAll() {
        return store.values();
    }
}
