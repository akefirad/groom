package com.akefirad.groom.spock

import java.util.EnumSet

enum class SpockSpecLabel {
    AND {
        override val successors get() = labels(AND, EXPECT, WHEN, THEN, CLEANUP, WHERE)
    },

    CLEANUP {
        override val successors get() = labels(AND, WHERE)
    },

    EXPECT {
        override val successors get() = labels(AND, WHEN, CLEANUP, WHERE)
    },

    GIVEN {
        override val successors get() = labels(AND, EXPECT, WHEN, CLEANUP, WHERE)
    },

    SETUP {
        override val successors get() = GIVEN.successors
    },

    THEN {
        override val successors get() = labels(AND, EXPECT, WHEN, THEN, CLEANUP, WHERE)
    },

    WHEN {
        override val successors get() = labels(AND, THEN)
    },

    WHERE {
        override val successors get() = labels(AND)
    };

    val label = name.lowercase()

    abstract val successors: EnumSet<SpockSpecLabel>

    override fun toString() = name.lowercase()

    companion object {
        private fun labels(e: SpockSpecLabel, vararg es: SpockSpecLabel): EnumSet<SpockSpecLabel> = EnumSet.of(e, *es)

        fun isLabel(text: String): Boolean = entries.any { it.label == text }

        fun ofLabel(text: String): SpockSpecLabel = valueOf(text.uppercase())
    }
}
