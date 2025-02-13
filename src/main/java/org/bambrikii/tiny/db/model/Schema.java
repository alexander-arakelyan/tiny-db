package org.bambrikii.tiny.db.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Schema {
    private String name;
    private final List<Table> tables = new ArrayList<>();
}
