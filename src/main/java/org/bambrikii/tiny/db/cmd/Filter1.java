package org.bambrikii.tiny.db.cmd;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Filter1 {
    private String left;
    private String operator;
    private String right;
}
