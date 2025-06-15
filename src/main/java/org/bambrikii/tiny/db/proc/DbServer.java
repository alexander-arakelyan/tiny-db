package org.bambrikii.tiny.db.proc;

import lombok.SneakyThrows;
import org.bambrikii.tiny.db.cmd.AbstractCommand;
import org.bambrikii.tiny.db.cmd.AbstractCommandParser;
import org.bambrikii.tiny.db.cmd.AbstractExecutorContext;
import org.bambrikii.tiny.db.cmd.AbstractMessage;
import org.bambrikii.tiny.db.cmd.CommandResult;
import org.bambrikii.tiny.db.cmd.CommandStack;
import org.bambrikii.tiny.db.cmd.NavigableStreamReader;
import org.bambrikii.tiny.db.cmd.altertable.AlterTable;
import org.bambrikii.tiny.db.cmd.altertable.AlterTableParser;
import org.bambrikii.tiny.db.cmd.createtable.CreateTable;
import org.bambrikii.tiny.db.cmd.createtable.CreateTableParser;
import org.bambrikii.tiny.db.cmd.deleterows.DeleteRows;
import org.bambrikii.tiny.db.cmd.deleterows.DeleteRowsParser;
import org.bambrikii.tiny.db.cmd.droptable.DropTable;
import org.bambrikii.tiny.db.cmd.droptable.DropTableParser;
import org.bambrikii.tiny.db.cmd.insertrows.InsertRows;
import org.bambrikii.tiny.db.cmd.insertrows.InsertRowsParser;
import org.bambrikii.tiny.db.cmd.selectrows.SelectRows;
import org.bambrikii.tiny.db.cmd.selectrows.SelectRowsParser;
import org.bambrikii.tiny.db.cmd.shutdownproc.ShutdownProc;
import org.bambrikii.tiny.db.cmd.shutdownproc.ShutdownProcParser;
import org.bambrikii.tiny.db.cmd.updaterows.UpdateRows;
import org.bambrikii.tiny.db.cmd.updaterows.UpdateRowsParser;
import org.bambrikii.tiny.db.disk.DiskStorage;
import org.bambrikii.tiny.db.mem.MemStorage;
import org.bambrikii.tiny.db.query.CommandParserFacade;
import org.bambrikii.tiny.db.query.QueryExecutorContext;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class DbServer {
    private CommandParserFacade parser;
    private QueryExecutorContext ctx;
    private DbServerConfig config;
    private CommandExecutorFacade executor;
    private DiskStorage diskStorage;
    private MemStorage memStorage;

    public static void main(String[] args) {
        var ps = new DbServer();
        var config = new DbServerConfig("localhost", 4001);
        ps.configure(config);
        ps.listen();
    }

    void configure(DbServerConfig config) {
        this.config = config;

        this.ctx = new QueryExecutorContext(
                diskStorage,
                memStorage
        );
        this.parser = new CommandParserFacade();
        this.executor = new CommandExecutorFacade(ctx);

        for (var pair : new CommandStack[]{
                stack(new ShutdownProcParser(), new ShutdownProc()),
                stack(new CreateTableParser(), new CreateTable()),
                stack(new AlterTableParser(), new AlterTable()),
                stack(new DropTableParser(), new DropTable()),
                stack(new SelectRowsParser(), new SelectRows()),
                stack(new InsertRowsParser(), new InsertRows()),
                stack(new UpdateRowsParser(), new UpdateRows()),
                stack(new DeleteRowsParser(), new DeleteRows())
        }) {
            parser.init(pair.getParser());
            executor.init(pair);
        }
    }

    private static <
            P extends AbstractCommandParser,
            M extends AbstractMessage,
            X extends AbstractExecutorContext,
            C extends AbstractCommand<M, X>
            > CommandStack stack(P parser, C exec) {
        return new CommandStack(parser, exec);
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
                var cmd = parser.parse(nis);
                var res = executor.exec(cmd);
                respond(res, bow);
            }
        }
    }

    private void respond(CommandResult res, BufferedWriter bow) {
    }
}
