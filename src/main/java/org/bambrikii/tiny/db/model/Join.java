package org.bambrikii.tiny.db.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Join {
    private String table;
    private JoinTypeEnum type;
    private String alias;
}
