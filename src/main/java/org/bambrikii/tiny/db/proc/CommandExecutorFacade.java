package org.bambrikii.tiny.db.proc;

import lombok.RequiredArgsConstructor;
import org.bambrikii.tiny.db.cmd.AbstractCommand;
import org.bambrikii.tiny.db.cmd.AbstractCommandParser;
import org.bambrikii.tiny.db.cmd.AbstractExecutorContext;
import org.bambrikii.tiny.db.cmd.AbstractMessage;
import org.bambrikii.tiny.db.cmd.CommandResult;
import org.bambrikii.tiny.db.cmd.CommandStack;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class CommandExecutorFacade {
    private final AbstractExecutorContext ctx;
    private final Map<
            Class<? extends AbstractCommandParser>,
            AbstractCommand<? extends AbstractMessage, ? extends AbstractExecutorContext>
            > executors = new HashMap<>();

    public void init(CommandStack stack) {
        executors.put(stack.getParser().getClass(), stack.getExecutor());
    }

    public <
            T extends AbstractExecutorContext,
            P extends AbstractCommandParser,
            C extends AbstractMessage,
            E extends AbstractCommand<C, T>
            > CommandResult exec(AbstractMessage cmd) {
        var executor = (AbstractCommand<C, T>) executors.get(cmd.getClass());
        return executor.exec((C) cmd, (T) ctx);
    }
}
