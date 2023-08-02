package org.bambrikii.tiny.db;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class RegisterTest {
    private Register register;

    @BeforeEach
    public void beforeEach() {
        register = new Register("build/db2");
        register.destroy();
    }
    @Test
    public void shouldStoreValues() {
        // given

        // when
        register.mod("prop1", "val1");
        register.mod("prop2", "val2");
        register.mod("prop2", "val2-2");
        register.mod("prop3", "val3");
        register.mod("prop3");

        // then
        assertThat(register.find("prop1")).isEqualTo("val1");
        assertThat(register.find("prop2")).isEqualTo("val2-2");
        assertThat(register.find("prop3")).isNull();
    }

    @Test
    public void shouldRetrieveValues() {
        // given

        // when
        register.mod("prop1", "val1");
        register.mod("prop2", "val2");
        register.mod("prop2", "val2-2");
        register.mod("prop3", "val3");
        register.mod("prop3");

        var register2 = new Register("build/db2");

        // then
        assertThat(register2.find("prop1")).isEqualTo("val1");
        assertThat(register2.find("prop2")).isEqualTo("val2-2");
        assertThat(register2.find("prop3")).isNull();
    }
}
