package org.bambrikii.tiny.db.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Filter {
    private String left;
    private ComparisonOpEnum op;
    private String right;
}
