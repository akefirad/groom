package com.akefirad.groom.spock

import com.akefirad.groom.spock.SpockSpecLabel.AND
import com.akefirad.groom.spock.SpockSpecLabel.CLEANUP
import com.akefirad.groom.spock.SpockSpecLabel.EXPECT
import com.akefirad.groom.spock.SpockSpecLabel.THEN
import com.akefirad.groom.spock.SpockSpecLabel.WHEN
import com.akefirad.groom.spock.SpockSpecLabel.WHERE
import org.junit.Assert.assertEquals
import org.junit.Test

class SpockSpecLabelTest {

    @Test
    fun testAndLabel() {
        val label = AND
        assertEquals("and", label.label)
        assertEquals("and", label.toString())
        assertEquals(setOf(AND, EXPECT, WHEN, THEN, CLEANUP, WHERE), label.successors)
    }

    @Test
    fun testCleanupLabel() {
        val label = CLEANUP
        assertEquals("cleanup", label.label)
        assertEquals("cleanup", label.toString())
        assertEquals(setOf(AND, WHERE), label.successors)
    }

    @Test
    fun testExpectLabel() {
        val label = EXPECT
        assertEquals("expect", label.label)
        assertEquals("expect", label.toString())
        assertEquals(setOf(AND, WHEN, CLEANUP, WHERE), label.successors)
    }

    @Test
    fun testGivenLabel() {
        val label = SpockSpecLabel.GIVEN
        assertEquals("given", label.label)
        assertEquals("given", label.toString())
        assertEquals(setOf(AND, EXPECT, WHEN, CLEANUP, WHERE), label.successors)
    }

    @Test
    fun testSetupLabel() {
        val label = SpockSpecLabel.SETUP
        assertEquals("setup", label.label)
        assertEquals("setup", label.toString())
        assertEquals(setOf(AND, EXPECT, WHEN, CLEANUP, WHERE), label.successors)
    }

    @Test
    fun testThenLabel() {
        val label = THEN
        assertEquals("then", label.label)
        assertEquals("then", label.toString())
        assertEquals(setOf(AND, EXPECT, WHEN, THEN, CLEANUP, WHERE), label.successors)
    }

    @Test
    fun testWhenLabel() {
        val label = WHEN
        assertEquals("when", label.label)
        assertEquals("when", label.toString())
        assertEquals(setOf(AND, THEN), label.successors)
    }

    @Test
    fun testWhereLabel() {
        val label = WHERE
        assertEquals("where", label.label)
        assertEquals("where", label.toString())
        assertEquals(setOf(AND), label.successors)
    }
}
