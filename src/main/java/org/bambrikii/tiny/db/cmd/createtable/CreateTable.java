package org.bambrikii.tiny.db.cmd.createtable;

import org.bambrikii.tiny.db.cmd.AbstractCommand;
import org.bambrikii.tiny.db.cmd.AbstractExecutorContext;
import org.bambrikii.tiny.db.cmd.CommandResult;
import org.bambrikii.tiny.db.model.TableStruct;
import org.bambrikii.tiny.db.storage.disk.DiskIO;
import org.bambrikii.tiny.db.storage.mem.MemIO;
import org.bambrikii.tiny.db.storage.tables.RelTableStructIO;

import java.util.function.Function;

import static org.bambrikii.tiny.db.cmd.none.NoCommandResult.OK_COMMAND_RESULT;

public class CreateTable extends AbstractCommand<CreateTableMessage, AbstractExecutorContext> {
    @Override
    public CommandResult exec(CreateTableMessage cmd, AbstractExecutorContext ctx) {
        var key = cmd.getName();

        var struct = new TableStruct();
        struct.setTable(cmd.getName());
        struct.getColumns().addAll(cmd.getColumns());

        ctx.getStorage().write(key, toDisk(struct), toMem(struct));

        return OK_COMMAND_RESULT;
    }

    public static Function<DiskIO, Boolean> toDisk(TableStruct struct) {
        return io -> {
            try (var rel = new RelTableStructIO(io, struct.getTable())) {
                return rel.write(struct);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        };
    }

    public static Function<MemIO, Boolean> toMem(TableStruct struct) {
        return io -> io.write("information_schema." + struct.getTable(), struct);
    }
}
