package org.bambrikii.tiny.db.plan;

import org.bambrikii.tiny.db.cmd.NavigableStreamReader;
import org.bambrikii.tiny.db.cmd.selectrows.SelectRowsMessage;
import org.bambrikii.tiny.db.cmd.selectrows.SelectRowsParser;
import org.bambrikii.tiny.db.io.disk.DiskIO;
import org.bambrikii.tiny.db.io.disk.FileOps;
import org.bambrikii.tiny.db.io.mem.MemIO;
import org.bambrikii.tiny.db.log.DbLogger;
import org.bambrikii.tiny.db.model.Row;
import org.bambrikii.tiny.db.storage.StorageContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class PlannerTest {
    @Mock
    private DiskIO disk;
    @Mock
    private FileOps fileOps;

    @Test
    void shouldBuildMultiLevelPlan() throws IOException {
        // given
        doReturn(fileOps).when(disk).openRead(anyString());
        doReturn(List.of(Path.of("file1"))).when(disk).find(any(), any());
        doReturn("rowid").when(fileOps).readStr();

        var mem = new MemIO();
        var ctx = new StorageContext(disk, mem);
        var builder = new Planner(ctx);
        try (
                var bis = new ByteArrayInputStream("select t1.col1 from t1 t1 where t1.col1 = 1".getBytes(StandardCharsets.UTF_8));
                var nis = new NavigableStreamReader(bis);
        ) {
            var parser = new SelectRowsParser();
            var msg = parser.parse(nis);
            assertThat(msg).isInstanceOf(SelectRowsMessage.class);
            var selectMsg = (SelectRowsMessage) msg;

            // when
            try (var scroll = builder.execute(selectMsg.getFrom(), selectMsg.getWhere())) {

                // then
                scroll.open();
                Row row;
                while ((row = scroll.next()) != null) {
                    DbLogger.print(row, "t1.col1");
                }
            }
        }
    }
}
