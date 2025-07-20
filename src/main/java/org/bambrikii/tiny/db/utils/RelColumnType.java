package org.bambrikii.tiny.db.utils;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public enum RelColumnType {
    INT("int"),
    STRING("str"),
    OBJECT("obj");

    private static final Map<String, RelColumnType> typesByAlias;

    private final List<String> aliases;

    RelColumnType(String... aliases) {
        this.aliases = Arrays
                .stream(aliases)
                .map(String::toLowerCase)
                .collect(Collectors.toList());
    }

    static {
        var vals = new HashMap<String, RelColumnType>();
        for (var val : values()) {
            for (var alias : val.aliases) {
                vals.put(alias, val);
            }
        }
        typesByAlias = Collections.unmodifiableMap(vals);
    }

    public static RelColumnType findByAlias(String name) {
        return name == null
                ? null
                : typesByAlias.getOrDefault(name.toLowerCase(), null);
    }
}
