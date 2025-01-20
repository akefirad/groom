package com.akefirad.groom.spock

import com.akefirad.groom.intellij.PsiElementExtensions.startOffset
import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import org.jetbrains.plugins.groovy.lang.psi.api.statements.GrLabeledStatement


class SpockLabelAnnotator : Annotator {

    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        if (element is GrLabeledStatement && SpecLabel.isLabel(element.name)) {
            annotate(element, holder)
        }
    }

    private fun annotate(e: GrLabeledStatement, h: AnnotationHolder) {
        val label = SpecLabelElement.ofLabel(e)
        val range = TextRange.from(e.startOffset, label.name.label.length + 1)
            .let { if (label.hasTitle) it.union(e.lastChild.textRange) else it }

        h.newSilentAnnotation(HighlightSeverity.INFORMATION)
            .textAttributes(DefaultLanguageHighlighterColors.HIGHLIGHTED_REFERENCE)
            .range(range)
            .create()
    }
}
