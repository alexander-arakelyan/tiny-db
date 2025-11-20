package org.bambrikii.tiny.db.parser;

import lombok.Setter;
import lombok.ToString;
import org.bambrikii.tiny.db.cmd.AbstractMessage;
import org.bambrikii.tiny.db.model.predicates.WherePredicate;

@ToString
@Setter
class TestWhereMessage implements AbstractMessage {
    private WherePredicate where;
}
