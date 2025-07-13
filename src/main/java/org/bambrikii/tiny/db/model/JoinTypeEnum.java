package org.bambrikii.tiny.db.model;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
public enum JoinTypeEnum {
    LEFT("left", 0), RIGHT("right", 1), CROSS("cross", 2), FULL("full", 3);
    private final String sqlRepr;
    private final int priority;

    JoinTypeEnum(String sqlRepr, int priority) {
        this.sqlRepr = sqlRepr;
        this.priority = priority;
    }

    public static JoinTypeEnum parse(String repr) {
        for (var op : values()) {
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
