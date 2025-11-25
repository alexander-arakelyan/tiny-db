package org.bambrikii.tiny.db.cmd.droptable;

import lombok.Getter;
import org.bambrikii.tiny.db.cmd.AbstractMessage;

@Getter
public class DropTableMessage implements AbstractMessage {
    private String name;

    public DropTableMessage name(String name) {
        this.name = name;
        return this;
    }
}
