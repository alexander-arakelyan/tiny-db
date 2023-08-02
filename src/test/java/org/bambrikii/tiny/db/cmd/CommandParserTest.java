package org.bambrikii.tiny.db.cmd;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class CommandParserTest {
    @Test
    public void shouldParseCommand1() {
        // given
        var parser = new CommandParser();

        // when
        var cmd = parser.parse("v1:find:name1");

        // then
        assertThat(cmd).isNotNull();
        assertThat(cmd).extracting("ver").contains("v1");
        assertThat(cmd).extracting("cmd").contains("find");
        assertThat(cmd).extracting("args").contains(Arrays.asList("name1"));
    }

    @Test
    public void shouldParseCommand2() {
        // given
        var parser = new CommandParser();

        // when
        var cmd = parser.parse("v1:mod:name1:val1");

        // then
        assertThat(cmd).isNotNull();
        assertThat(cmd).extracting("ver").contains("v1");
        assertThat(cmd).extracting("cmd").contains("mod");
        assertThat(cmd).extracting("args").contains(Arrays.asList("name1", "val1"));
    }
}
