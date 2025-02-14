package org.bambrikii.tiny.db.proc;

import org.bambrikii.tiny.db.cmd.ddl.AlterTableExecutor;
import org.bambrikii.tiny.db.cmd.ddl.CreateTableExecutor;
import org.bambrikii.tiny.db.cmd.ddl.DropTableExecutor;
import org.bambrikii.tiny.db.cmd.dml.DeleteRowsExecutor;
import org.bambrikii.tiny.db.cmd.dml.InsertRowsExecutor;
import org.bambrikii.tiny.db.cmd.dml.SelectRowsExecutor;
import org.bambrikii.tiny.db.cmd.dml.UpdateRowsExecutor;
import org.bambrikii.tiny.db.cmd.proc.ShutdownProcExecutor;
import org.bambrikii.tiny.db.query.AbstractDbCommand;
import org.bambrikii.tiny.db.query.ExecutionResult;
import org.bambrikii.tiny.db.query.QueryExecutor;
import org.bambrikii.tiny.db.query.QueryExecutorContext;

import java.util.Arrays;

public class DbServer {
    private QueryExecutor exec;
    private QueryExecutorContext ctx;

    public static void main(String[] args) {
        var ps = new DbServer();
        ps.configure();
        ps.listen();
    }

    void configure() {
        this.ctx = new QueryExecutorContext();
        this.exec = new QueryExecutor(
                new ShutdownProcExecutor()
        );
    }

    void listen() {
        while (ctx.shouldRun()) {
            var cmd = parseCommand();
            var res = exec.exec(cmd, ctx);
            sendResult(res);
        }
    }

    private void sendResult(ExecutionResult res) {
    }

    private AbstractDbCommand parseCommand() {
        return null;
    }
}
