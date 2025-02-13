package org.bambrikii.tiny.db.exec;

public class DbProcess {
    private QueryExecutor exec;
    private QueryExecutorContext ctx;

    public static void main(String[] args) {
        var ps = new DbProcess();
        ps.configure();
        ps.listen();
    }

    void configure() {
        this.ctx = new QueryExecutorContext();
        this.exec = new QueryExecutor();
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
