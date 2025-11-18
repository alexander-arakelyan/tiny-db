package org.bambrikii.tiny.db.plan;

import lombok.SneakyThrows;
import org.bambrikii.tiny.db.io.disk.DiskIO;
import org.bambrikii.tiny.db.io.mem.MemIO;
import org.bambrikii.tiny.db.query.QueryExecutorContext;
import org.bambrikii.tiny.db.utils.TableTestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.MessageFormat;

class SelectPlanTest {
    private QueryExecutorContext ctx;

    @BeforeEach
    void beforeEach() {
        ctx = new QueryExecutorContext(new DiskIO(), new MemIO());
    }

    @SneakyThrows
    @Test
    void shouldSelect() {
        var t1 = "build/sel-plan-t1";
        var t2 = "build/sel-plan-t2";

        new TableTestUtils(ctx)
                .dropTable(t1)
                .createTable(t1, msg -> {
                    msg.addColumn("col1", "str", 0, 0, false, false);
                    msg.addColumn("col2", "str", 0, 0, false, false);
                    msg.addColumn("col3", "str", 0, 0, false, false);
                })
                .insert(t1, msg -> {
                    msg.columnValue("col1", "val1");
                    msg.columnValue("col2", "val2");
                    msg.columnValue("col3", "val3");
                })
                .dropTable(t2)
                .createTable(t2, msg -> {
                    msg.addColumn("col21", "str", 0, 0, false, false);
                    msg.addColumn("col22", "str", 0, 0, false, false);
                    msg.addColumn("col23", "str", 0, 0, false, false);
                })
                .insert(t2, msg -> {
                    msg.columnValue("col21", "val1");
                    msg.columnValue("col22", "val2");
                    msg.columnValue("col23", "val3");
                })
                .insert(t2, msg -> {
                    msg.columnValue("col21", "val21");
                    msg.columnValue("col22", "val22");
                    msg.columnValue("col23", "val23");
                })
                .select(MessageFormat.format("select t1.col1, t2.col21 "
                                + " from \"{0}\" t1 "
                                + " inner join \"{1}\" t2 on t1.col1 = t2.col21 "
                                + " where t2.col23 = ''val3'' ",
                        t1, t2
                ));
    }
}
