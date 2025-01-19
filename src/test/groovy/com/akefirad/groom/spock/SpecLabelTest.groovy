package com.akefirad.groom.spock

import org.junit.Test

import static com.akefirad.groom.spock.SpecLabel.AND
import static com.akefirad.groom.spock.SpecLabel.CLEANUP
import static com.akefirad.groom.spock.SpecLabel.EXPECT
import static com.akefirad.groom.spock.SpecLabel.GIVEN
import static com.akefirad.groom.spock.SpecLabel.SETUP
import static com.akefirad.groom.spock.SpecLabel.THEN
import static com.akefirad.groom.spock.SpecLabel.WHEN
import static com.akefirad.groom.spock.SpecLabel.WHERE
import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertFalse
import static org.junit.Assert.assertTrue

class SpecLabelTest {

    @Test
    void 'and label'() {
        def label = AND
        assertEquals("and", label.label)
        assertEquals("and", label.toString())
        assertEquals([AND, EXPECT, WHEN, THEN, CLEANUP, WHERE] as Set, label.successors)
        assertTrue(label.isContinuation())
        assertFalse(label.isExpectation())
    }

    @Test
    void 'cleanup label'() {
        def label = CLEANUP
        assertEquals("cleanup", label.label)
        assertEquals("cleanup", label.toString())
        assertEquals([AND, WHERE] as Set, label.successors)
        assertFalse(label.isContinuation())
        assertFalse(label.isExpectation())
    }

    @Test
    void 'expect label'() {
        def label = EXPECT
        assertEquals("expect", label.label)
        assertEquals("expect", label.toString())
        assertEquals([AND, WHEN, CLEANUP, WHERE] as Set, label.successors)
        assertFalse(label.isContinuation())
        assertTrue(label.isExpectation())
    }

    @Test
    void 'given label'() {
        def label = GIVEN
        assertEquals("given", label.label)
        assertEquals("given", label.toString())
        assertEquals([AND, EXPECT, WHEN, CLEANUP, WHERE] as Set, label.successors)

    }

    @Test
    void 'setup label'() {
        def label = SETUP
        assertEquals("setup", label.label)
        assertEquals("setup", label.toString())
        assertEquals([AND, EXPECT, WHEN, CLEANUP, WHERE] as Set, label.successors)
        assertFalse(label.isContinuation())
        assertFalse(label.isExpectation())
    }

    @Test
    void 'then label'() {
        def label = THEN
        assertEquals("then", label.label)
        assertEquals("then", label.toString())
        assertEquals([AND, EXPECT, WHEN, THEN, CLEANUP, WHERE] as Set, label.successors)
        assertFalse(label.isContinuation())
        assertTrue(label.isExpectation())
    }

    @Test
    void 'when label'() {
        def label = WHEN
        assertEquals("when", label.label)
        assertEquals("when", label.toString())
        assertEquals([AND, THEN] as Set, label.successors)
        assertFalse(label.isContinuation())
        assertFalse(label.isExpectation())
    }

    @Test
    void 'where label'() {
        def label = WHERE
        assertEquals("where", label.label)
        assertEquals("where", label.toString())
        assertEquals([AND] as Set, label.successors)
        assertFalse(label.isContinuation())
        assertFalse(label.isExpectation())
    }
}
