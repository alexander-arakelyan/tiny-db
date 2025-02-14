package org.bambrikii.tiny.db.query;

import org.bambrikii.tiny.db.cmd.ddl.AlterTableExecutor;
import org.bambrikii.tiny.db.cmd.ddl.CreateTableExecutor;
import org.bambrikii.tiny.db.cmd.ddl.DropTableExecutor;
import org.bambrikii.tiny.db.cmd.dml.DeleteRowsExecutor;
import org.bambrikii.tiny.db.cmd.dml.InsertRowsExecutor;
import org.bambrikii.tiny.db.cmd.dml.SelectRowsExecutor;
import org.bambrikii.tiny.db.cmd.dml.UpdateRowsExecutor;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class QueryExecutor {
    private final List<AbstractDbExecutor> executors;

    public QueryExecutor(AbstractDbExecutor... moreExecutors) {
        executors = Stream
                .concat(Arrays.stream(new AbstractDbExecutor[]{
                                new CreateTableExecutor(),
                                new AlterTableExecutor(),
                                new DropTableExecutor(),
                                new SelectRowsExecutor(),
                                new InsertRowsExecutor(),
                                new UpdateRowsExecutor(),
                                new DeleteRowsExecutor()
                        }),
                        Arrays.stream(moreExecutors)
                )
                .collect(Collectors.toList());
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
