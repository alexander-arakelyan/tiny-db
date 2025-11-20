package org.bambrikii.tiny.db.plan;

import lombok.SneakyThrows;
import org.bambrikii.tiny.db.cmd.CommandResult;
import org.bambrikii.tiny.db.query.QueryExecutorContext;
import org.bambrikii.tiny.db.storage.disk.DiskIO;
import org.bambrikii.tiny.db.storage.mem.MemIO;
import org.bambrikii.tiny.db.utils.TableTestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.MessageFormat;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;

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
                .createTable(t1, msg -> msg
                        .addColumn("col1", "str", 0, 0, false, false)
                        .addColumn("col2", "str", 0, 0, false, false)
                        .addColumn("col3", "str", 0, 0, false, false)
                )
                .insert(t1, msg -> msg
                        .columnValue("col1", "val1")
                        .columnValue("col2", "val2")
                        .columnValue("col3", "val3")
                )
                .insert(t1, msg -> msg
                        .columnValue("col1", "val12")
                        .columnValue("col2", "val22")
                        .columnValue("col3", "val32")
                )
                .dropTable(t2)
                .createTable(t2, msg -> msg
                        .addColumn("col21", "str", 0, 0, false, false)
                        .addColumn("col22", "str", 0, 0, false, false)
                        .addColumn("col23", "str", 0, 0, false, false)
                )
                .insert(t2, msg -> msg
                        .columnValue("col21", "val1")
                        .columnValue("col22", "val2")
                        .columnValue("col23", "val3")
                )
                .insert(t2, msg -> msg
                        .columnValue("col21", "val12")
                        .columnValue("col22", "val22")
                        .columnValue("col23", "val32")
                )
                .select(MessageFormat.format("clauses t1.col1, t2.col21, t2.col23 "
                                        + " from \"{0}\" t1 "
                                        + " inner join \"{1}\" t2 on t1.col1 = t2.col21 "
                                        + " nodes t2.col23 = ''val32'' ",
                                t1, t2
                        ),
                        (Consumer<CommandResult>) res -> assertThat(res.toString()).contains("val32")
                );
    }
}
