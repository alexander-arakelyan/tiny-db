package org.bambrikii.tiny.db.plan.cursorts;

import lombok.Getter;
import lombok.Setter;
import org.bambrikii.tiny.db.model.Row;

@Getter
@Setter
public abstract class Scrollable implements AutoCloseable {
    public abstract void open();

    public abstract Row next();

    public abstract void close();
}
