package org.bambrikii.tiny.db.cmd;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class CommandStack {
    private final AbstractCommandParser parser;
    private final AbstractCommand<? extends AbstractMessage, ? extends AbstractExecutorContext> executor;
}
