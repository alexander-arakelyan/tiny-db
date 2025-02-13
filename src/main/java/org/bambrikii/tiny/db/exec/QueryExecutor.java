package org.bambrikii.tiny.db.exec;

import org.bambrikii.tiny.db.cmd.ddl.AlterTableExecutor;
import org.bambrikii.tiny.db.cmd.ddl.CreateTableExecutor;
import org.bambrikii.tiny.db.cmd.ddl.DropTableExecutor;
import org.bambrikii.tiny.db.cmd.dml.DeleteRowsExecutor;
import org.bambrikii.tiny.db.cmd.dml.InsertRowsExecutor;
import org.bambrikii.tiny.db.cmd.dml.SelectRowsExecutor;
import org.bambrikii.tiny.db.cmd.dml.UpdateRowsExecutor;
import org.bambrikii.tiny.db.cmd.proc.ShutdownProcExecutor;

import java.util.Arrays;
import java.util.List;

public class QueryExecutor {
    private final List<AbstractDbExecutor> executors;

    public QueryExecutor() {
        executors = Arrays.asList(
                new CreateTableExecutor(),
                new AlterTableExecutor(),
                new DropTableExecutor(),
                new SelectRowsExecutor(),
                new InsertRowsExecutor(),
                new UpdateRowsExecutor(),
                new DeleteRowsExecutor(),
                new ShutdownProcExecutor()
        );
    }

    public ExecutionResult exec(AbstractDbCommand cmd, AbstractExecutorContext ctx) {
        for (var executor : executors) {
            var result = executor.tryExec(cmd, ctx);
            if (result != null) {
                return result;
            }
        }
        return null;
    }
}
