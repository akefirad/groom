package com.akefirad.groom.spock

import com.akefirad.groom.intellij.PsiElementExtensions.startOffset
import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.editor.markup.TextAttributes
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import org.jetbrains.plugins.groovy.lang.psi.api.statements.GrLabeledStatement
import java.awt.Font


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

        val boldAttributesKey = TextAttributesKey.createTextAttributesKey(
            "SPOCK_LABEL_BOLD",
            DefaultLanguageHighlighterColors.HIGHLIGHTED_REFERENCE
        )

        h.newSilentAnnotation(HighlightSeverity.INFORMATION)
            .textAttributes(boldAttributesKey)
            .range(range)
            .enforcedTextAttributes(TextAttributes().apply {
                val base = DefaultLanguageHighlighterColors.HIGHLIGHTED_REFERENCE.defaultAttributes
                foregroundColor = base.foregroundColor
                backgroundColor = base.backgroundColor
                effectType = base.effectType
                effectColor = base.effectColor
                fontType = Font.BOLD
            })
            .create()
    }
}
