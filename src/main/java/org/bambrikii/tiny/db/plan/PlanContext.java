package org.bambrikii.tiny.db.plan;


import org.bambrikii.tiny.db.plan.iterators.Scrollable;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class PlanContext {
    private final Map<String, Scrollable> scans = new HashMap<>();
    private final Map<String, String> parents = new HashMap<>();

    public Scrollable scan() {
        return scans
                .entrySet()
                .stream()
                .findFirst()
                .orElseThrow()
                .getValue();
    }

    private String find0(String alias) {
        var pp = parents.get(parents.get(alias));
        while (!Objects.equals(parents.get(alias), pp)) {
            pp = parents.get(parents.get(alias));
            parents.put(alias, pp == null ? alias : pp);
        }
        return parents.get(alias);
    }

    private String union0(String left, String right) {
        var val = find0(right);
        parents.put(left, val);
        return val;
    }

    public Scrollable find(String alias) {
        return scans.get(find0(alias));
    }

    public void merge(String alias, Scrollable scan) {
        scans.put(alias, scan);
        parents.put(alias, alias);
    }

    public void merge(String alias, String right, Scrollable scan) {
        scans.remove(alias);
        scans.remove(right);
        union0(alias, right);
        scans.put(find0(alias), scan);
    }
}
