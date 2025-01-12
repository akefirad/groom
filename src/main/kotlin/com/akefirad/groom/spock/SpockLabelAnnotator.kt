package com.akefirad.groom.spock

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import org.jetbrains.plugins.groovy.lang.psi.api.statements.GrLabeledStatement
import org.jetbrains.plugins.groovy.lang.psi.api.statements.expressions.literals.GrLiteral


class SpockLabelAnnotator : Annotator {

    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        if (element is GrLabeledStatement && SpockSpecLabel.isLabel(element.name)) {
            annotate(element, holder)
        }
    }

    private fun annotate(e: GrLabeledStatement, h: AnnotationHolder) {
        val label = SpockSpecLabel.ofLabel(e.name)
        val labelRange = TextRange.from(e.textRange.startOffset, label.label.length + 1)

        assert(e.children.size == 1) { "Unexpected number of children: ${e.children.size}" }

        // Is calling GrLiteral.isString needed here?
        val title = e.children.first() as? GrLiteral

        if (title == null)
            h.newAnnotation(HighlightSeverity.WARNING, "Spock labels should have a title!")
                .range(labelRange)
                // .textAttributes(DefaultLanguageHighlighterColors.HIGHLIGHTED_REFERENCE)
                .create()
        else
            h.newSilentAnnotation(HighlightSeverity.INFORMATION)
                .range(labelRange.union(title.textRange))
                .textAttributes(DefaultLanguageHighlighterColors.HIGHLIGHTED_REFERENCE)
                .create()
    }
}
