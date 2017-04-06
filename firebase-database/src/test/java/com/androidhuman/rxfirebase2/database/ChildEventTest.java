package com.androidhuman.rxfirebase2.database;

import com.google.firebase.database.DataSnapshot;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;

public class ChildEventTest {

    @Mock
    DataSnapshot mockDataSnapshot1;

    @Mock
    DataSnapshot mockDataSnapshot2;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testChildAddEvent_equals_dataSnapshot() {
        ChildAddEvent a = ChildAddEvent.create(mockDataSnapshot1, "foo");
        ChildAddEvent b = ChildAddEvent.create(mockDataSnapshot1, "foo");
        ChildAddEvent c = ChildAddEvent.create(mockDataSnapshot2, "foo");

        assertThat(a)
                .isEqualTo(a)
                .isNotEqualTo(null)
                .isEqualTo(b)
                .isNotEqualTo(c);

        assertThat(a.dataSnapshot())
                .isEqualTo(mockDataSnapshot1)
                .isNotEqualTo(mockDataSnapshot2);

        assertThat(a.hashCode())
                .isEqualTo(b.hashCode())
                .isNotEqualTo(c.hashCode());
    }

    @Test
    public void testChildAddEvent_equals_previousChildName() {
        ChildAddEvent a = ChildAddEvent.create(mockDataSnapshot1, "foo");
        ChildAddEvent b = ChildAddEvent.create(mockDataSnapshot1, "foo");
        ChildAddEvent c = ChildAddEvent.create(mockDataSnapshot1, "bar");
        ChildAddEvent d = ChildAddEvent.create(mockDataSnapshot1, null);

        assertThat(a)
                .isEqualTo(a)
                .isNotEqualTo(null)
                .isEqualTo(b)
                .isNotEqualTo(c)
                .isNotEqualTo(d);

        assertThat(a.hashCode())
                .isEqualTo(b.hashCode())
                .isNotEqualTo(c.hashCode())
                .isNotEqualTo(d.hashCode());
    }

    @Test
    public void testChildAddEvent_toString() {
        ChildAddEvent e = ChildAddEvent.create(mockDataSnapshot1, "foo");

        assertThat(e.toString())
                .startsWith("ChildAddEvent");
    }

    @Test
    public void testChildChangeEvent_equals_dataSnapshot() {
        ChildChangeEvent a = ChildChangeEvent.create(mockDataSnapshot1, "foo");
        ChildChangeEvent b = ChildChangeEvent.create(mockDataSnapshot1, "foo");
        ChildChangeEvent c = ChildChangeEvent.create(mockDataSnapshot2, "foo");

        assertThat(a)
                .isEqualTo(b)
                .isNotEqualTo(c);

        assertThat(a.hashCode())
                .isEqualTo(b.hashCode())
                .isNotEqualTo(c.hashCode());
    }

    @Test
    public void testChildChangeEvent_equals_previousChildName() {
        ChildChangeEvent a = ChildChangeEvent.create(mockDataSnapshot1, "foo");
        ChildChangeEvent b = ChildChangeEvent.create(mockDataSnapshot1, "foo");
        ChildChangeEvent c = ChildChangeEvent.create(mockDataSnapshot1, "bar");
        ChildChangeEvent d = ChildChangeEvent.create(mockDataSnapshot1, null);

        assertThat(a)
                .isEqualTo(b)
                .isNotEqualTo(c)
                .isNotEqualTo(d);

        assertThat(a.hashCode())
                .isEqualTo(b.hashCode())
                .isNotEqualTo(c.hashCode())
                .isNotEqualTo(d.hashCode());
    }

    @Test
    public void testChildChangeEvent_toString() {
        ChildChangeEvent e = ChildChangeEvent.create(mockDataSnapshot1, "foo");

        assertThat(e.toString())
                .startsWith("ChildChangeEvent");
    }

    @Test
    public void testChildMoveEvent_equals_dataSnapshot() {
        ChildMoveEvent a = ChildMoveEvent.create(mockDataSnapshot1, "foo");
        ChildMoveEvent b = ChildMoveEvent.create(mockDataSnapshot1, "foo");
        ChildMoveEvent c = ChildMoveEvent.create(mockDataSnapshot2, "foo");

        assertThat(a)
                .isEqualTo(b)
                .isNotEqualTo(c);

        assertThat(a.hashCode())
                .isEqualTo(b.hashCode())
                .isNotEqualTo(c.hashCode());
    }

    @Test
    public void testChildMoveEvent_equals_previousChildName() {
        ChildMoveEvent a = ChildMoveEvent.create(mockDataSnapshot1, "foo");
        ChildMoveEvent b = ChildMoveEvent.create(mockDataSnapshot1, "foo");
        ChildMoveEvent c = ChildMoveEvent.create(mockDataSnapshot1, "bar");
        ChildMoveEvent d = ChildMoveEvent.create(mockDataSnapshot1, null);

        assertThat(a)
                .isEqualTo(b)
                .isNotEqualTo(c)
                .isNotEqualTo(d);

        assertThat(a.hashCode())
                .isEqualTo(b.hashCode())
                .isNotEqualTo(c.hashCode())
                .isNotEqualTo(d.hashCode());
    }

    @Test
    public void testChildMoveEvent_toString() {
        ChildMoveEvent e = ChildMoveEvent.create(mockDataSnapshot1, "foo");

        assertThat(e.toString())
                .startsWith("ChildMoveEvent");
    }

    @Test
    public void testChildRemoveEvent_equals() {
        ChildRemoveEvent a = ChildRemoveEvent.create(mockDataSnapshot1);
        ChildRemoveEvent b = ChildRemoveEvent.create(mockDataSnapshot1);
        ChildRemoveEvent c = ChildRemoveEvent.create(mockDataSnapshot2);

        assertThat(a)
                .isEqualTo(b)
                .isNotEqualTo(c);

        assertThat(a.hashCode())
                .isEqualTo(b.hashCode())
                .isNotEqualTo(c.hashCode());
    }

    @Test
    public void testChildRemoveEvent_toString() {
        ChildRemoveEvent e = ChildRemoveEvent.create(mockDataSnapshot1);

        assertThat(e.toString())
                .startsWith("ChildRemoveEvent");
    }

}
