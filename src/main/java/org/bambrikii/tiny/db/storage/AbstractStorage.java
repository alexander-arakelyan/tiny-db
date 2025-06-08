package org.bambrikii.tiny.db.storage;

import java.util.function.Predicate;

public interface AbstractStorage {
    void write(String key, Object obj);

    <T> T read(String key);

    void append(String key, Object obj);

    void drop(String key);

    void delete(String key, Predicate<Boolean> filter);
}
