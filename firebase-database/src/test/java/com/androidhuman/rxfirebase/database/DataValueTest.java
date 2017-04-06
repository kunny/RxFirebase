package com.androidhuman.rxfirebase.database;

import com.androidhuman.rxfirebase.database.model.DataValue;

import org.junit.Test;

import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.failBecauseExceptionWasNotThrown;

public class DataValueTest {

    @Test
    public void testDataValue_equals_hashCode() {
        DataValue<String> a = DataValue.of("Lorem ipsum dolor");
        DataValue<String> a1 = DataValue.of("Lorem ipsum dolor");
        DataValue<String> b = DataValue.of("LOREM IPSUM DOLOR");

        assertThat(a)
                .isEqualTo(a)
                .isEqualTo(a1)
                .isNotEqualTo(b);

        assertThat(a.hashCode())
                .isEqualTo(a1.hashCode())
                .isNotEqualTo(b.hashCode());
    }

    @Test
    public void testOf_nonNullValue() {
        DataValue<String> value = DataValue.of("foo");

        assertThat(value.isPresent())
                .isTrue();

        assertThat(value.get())
                .isEqualTo("foo");

        assertThat(value.toString())
                .isEqualTo("Some{value=foo}");
    }

    @Test
    public void testOf_nullValue() {
        DataValue<?> value = DataValue.of(null);

        assertThat(value.isPresent())
                .isFalse();

        assertThat(value.toString())
                .isEqualTo("Empty DataValue");

        try {
            value.get();
            failBecauseExceptionWasNotThrown(NoSuchElementException.class);
        } catch (NoSuchElementException e) {
            // do nothing
        }
    }
}
