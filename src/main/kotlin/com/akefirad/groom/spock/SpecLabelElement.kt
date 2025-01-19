package com.akefirad.groom.spock

import com.intellij.openapi.util.TextRange
import org.jetbrains.plugins.groovy.lang.psi.api.statements.GrLabeledStatement
import org.jetbrains.plugins.groovy.lang.psi.api.statements.expressions.literals.GrLiteral
import java.util.EnumSet

enum class SpecLabel {
    AND,
    CLEANUP,
    EXPECT,
    GIVEN,
    SETUP,
    THEN,
    WHEN,
    WHERE,
    ;

    val label = name.lowercase()
    val isContinuation get() = this == AND
    val isExpectation get() = this == EXPECT || this == THEN

    val successors: EnumSet<SpecLabel>
        get() = when (this) {
            AND -> labels(AND, EXPECT, WHEN, THEN, CLEANUP, WHERE)
            CLEANUP -> labels(AND, WHERE)
            EXPECT -> labels(AND, WHEN, CLEANUP, WHERE)
            GIVEN -> labels(AND, EXPECT, WHEN, CLEANUP, WHERE)
            SETUP -> labels(AND, EXPECT, WHEN, CLEANUP, WHERE)
            THEN -> labels(AND, EXPECT, WHEN, THEN, CLEANUP, WHERE)
            WHEN -> labels(AND, THEN)
            WHERE -> labels(AND)
        }

    override fun toString() = label

    companion object {
        private fun labels(e: SpecLabel, vararg es: SpecLabel): EnumSet<SpecLabel> = EnumSet.of(e, *es)

        fun isLabel(text: String): Boolean = entries.any { it.label == text }

        fun ofLabel(text: String): SpecLabel = valueOf(text.uppercase())
    }
}

data class SpecLabelElement(val element: GrLabeledStatement) {
    val name = SpecLabel.ofLabel(element.name)
    val range: TextRange = element.textRange
    val title = element.title
    val hasTitle = title != null
    val isContinuation = name.isContinuation
    val isExpectation = name.isExpectation

    override fun toString() = "$name${if (hasTitle) ": '$title'" else ""}"

    companion object {
        @JvmStatic
        fun ofLabel(e: GrLabeledStatement): SpecLabelElement {
            return SpecLabelElement(e)
        }

        val GrLabeledStatement.title
            get() = if (text.contains('\n')) null else lastChild.let {
                if (it is GrLiteral && it.isString) it.value as String else null
            }
    }
}
