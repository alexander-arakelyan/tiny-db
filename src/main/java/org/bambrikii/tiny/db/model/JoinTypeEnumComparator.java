package org.bambrikii.tiny.db.model;

import java.util.Comparator;

public class JoinTypeEnumComparator implements Comparator<JoinTypeEnum> {
    @Override
    public int compare(JoinTypeEnum o1, JoinTypeEnum o2) {
        return o1.getPriority() - o2.getPriority();
    }
}
