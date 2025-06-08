package org.bambrikii.tiny.db.model;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
public enum LogicalOpEnum {
    AND("and"),
    OR("or");

    private final String sqlRepr;

    LogicalOpEnum(String sqlRepr) {
        this.sqlRepr = sqlRepr;
    }

    public static LogicalOpEnum parse(String repr) {
        for (LogicalOpEnum op : values()) {
            if (Objects.equals(op.getSqlRepr(), repr)) {
                return op;
            }
        }
        return null;
    }

    public static List<String> sqlRepresentations() {
        int len = values().length;
        var res = new ArrayList<String>(len);
        for (int i = 0; i < len; i++) {
            res.add(values()[i].getSqlRepr());
        }
        return res;
    }
}
