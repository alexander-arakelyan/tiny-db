package org.bambrikii.tiny.db.cmd;

import lombok.Getter;
import lombok.Setter;
import org.bambrikii.tiny.db.storage.StorageFacade;

@Getter
@Setter
public abstract class AbstractExecutorContext {
    private StorageFacade storage;
}

