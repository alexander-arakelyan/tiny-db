package org.bambrikii.tiny.db;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class LogTest {
    @Test
    public void shouldStore() throws IOException {
        // given
        var fileName = "build/test.log";
        var log = new Log(fileName);
        log.destroy();
        log.store("name1", "val1");
        log.store("name2", "val2");
        log.store("name2", "val3");

        // when
        List<String> lines = Files.readAllLines(Path.of(fileName));

        // then
        assertThat(lines).hasSize(3);
        assertThat(lines.get(0)).isEqualTo("name1=val1");
        assertThat(lines.get(1)).isEqualTo("name2=val2");
        assertThat(lines.get(2)).isEqualTo("name2=val3");
    }

    @Test
    public void shouldLoad() throws IOException {
        // given
        var fileName = "build/test.log";
        var log = new Log(fileName);
        log.destroy();
        log.store("name1", "val1");
        log.store("name2", "val2");
        log.store("name2", "val3");
        log.store("name3", "val3");
        log.store("name3", null);

        // when
        var state = new State();
        log.load(state);

        // then
        assertThat(state.find("name1")).isEqualTo("val1");
        assertThat(state.find("name2")).isEqualTo("val3");
        assertThat(state.find("name3")).isNull();
    }
}
