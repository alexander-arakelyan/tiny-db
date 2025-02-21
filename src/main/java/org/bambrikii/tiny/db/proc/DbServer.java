package org.bambrikii.tiny.db.proc;

import lombok.SneakyThrows;
import org.bambrikii.tiny.db.cmd.altertable.AlterTableParser;
import org.bambrikii.tiny.db.cmd.createtable.CreateTableParser;
import org.bambrikii.tiny.db.cmd.createtable.NavigableStreamReader;
import org.bambrikii.tiny.db.cmd.deleterows.DeleteRowsParser;
import org.bambrikii.tiny.db.cmd.droptable.DropTableParser;
import org.bambrikii.tiny.db.cmd.insertrows.InsertRowsParser;
import org.bambrikii.tiny.db.cmd.selectrows.SelectRowsParser;
import org.bambrikii.tiny.db.cmd.CommandResult;
import org.bambrikii.tiny.db.cmd.shutdownproc.ShutdownProcParser;
import org.bambrikii.tiny.db.cmd.updaterows.UpdateRowsParser;
import org.bambrikii.tiny.db.query.QueryExecutor;
import org.bambrikii.tiny.db.query.QueryExecutorContext;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class DbServer {
    private QueryExecutor exec;
    private QueryExecutorContext ctx;
    private DbServerConfig config;

    public static void main(String[] args) {
        var ps = new DbServer();
        var config = new DbServerConfig("localhost", 4001);
        ps.configure(config);
        ps.listen();
    }

    void configure(DbServerConfig config) {
        this.config = config;
        this.ctx = new QueryExecutorContext();
        this.exec = new QueryExecutor(
                new ShutdownProcParser(),
                new CreateTableParser(),
                new AlterTableParser(),
                new DropTableParser(),
                new SelectRowsParser(),
                new InsertRowsParser(),
                new UpdateRowsParser(),
                new DeleteRowsParser()
        );
    }

    @SneakyThrows
    void listen() {
        try (var socket = new Socket(config.getHost(), config.getPort());

             var bis = new BufferedInputStream(socket.getInputStream());
             var nis = new NavigableStreamReader(bis);

             var bos = new BufferedOutputStream(socket.getOutputStream());
             var osw = new OutputStreamWriter(bos);
             var bow = new BufferedWriter(osw);
        ) {
            while (ctx.shouldRun()) {
                var res = exec.parse(nis).exec(ctx);
                respond(res, bow);
            }
        }
    }

    private void respond(CommandResult res, BufferedWriter bow) {
    }
}
