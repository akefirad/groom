package com.akefirad.groom.spock

import com.akefirad.groom.spock.SpockSpecUtils.hasAnySpecification
import com.akefirad.groom.spock.SpockSpecUtils.isSpeckLabel
import com.intellij.codeInsight.hints.declarative.InlayHintsProvider
import com.intellij.codeInsight.hints.declarative.InlayTreeSink
import com.intellij.codeInsight.hints.declarative.InlineInlayPosition
import com.intellij.codeInsight.hints.declarative.PresentationTreeBuilder
import com.intellij.codeInsight.hints.declarative.SharedBypassCollector
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.PossiblyDumbAware
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import org.jetbrains.plugins.groovy.lang.psi.api.statements.blocks.GrClosableBlock
import org.jetbrains.plugins.groovy.lang.psi.api.statements.blocks.GrOpenBlock
import org.jetbrains.plugins.groovy.lang.psi.api.statements.expressions.GrAssignmentExpression
import org.jetbrains.plugins.groovy.lang.psi.api.statements.expressions.GrExpression
import org.jetbrains.plugins.groovy.lang.psi.api.statements.expressions.path.GrMethodCallExpression
import org.jetbrains.plugins.groovy.lang.psi.api.statements.typedef.members.GrMethod
import org.jetbrains.plugins.groovy.lang.psi.util.isWhiteSpaceOrNewLine

class AssertInlayHintsProvider : InlayHintsProvider, PossiblyDumbAware {
    override fun createCollector(file: PsiFile, editor: Editor) =
        if (file.hasAnySpecification()) AssertInlayHintsCollector() else null
}

class AssertInlayHintsCollector : SharedBypassCollector {

    override fun collectFromElement(element: PsiElement, sink: InlayTreeSink) {
        if (element is GrMethod)
            collectFromMethod(element, sink)
    }

    private fun collectFromMethod(m: GrMethod, sink: InlayTreeSink) {
        m.children.forEach {
            if (it is GrOpenBlock) collectFromBlock(it, sink)
        }
    }

    private fun collectFromBlock(b: GrOpenBlock, sink: InlayTreeSink) {
        var label: SpecLabelElement? = null
        for (c in b.children) {
            if (c.isWhiteSpaceOrNewLine()) {
                continue
            }

            if (c.isSpeckLabel()) {
                val current = SpecLabelElement.ofLabel(c)
                if (current.isContinuation && label?.isExpectation == true) {
                    if (current.hasTitle == false) {
                        collectFromExpectationBlockChildren(c.lastChild, sink)
                    }
                } else if (current.isExpectation) {
                    if (current.hasTitle == false) {
                        collectFromExpectationBlockChildren(c.lastChild, sink)
                    }
                    label = current
                } else {
                    label = null
                }
            } else if (label?.isExpectation == true) {
                collectFromExpectationBlockChildren(c, sink)
            }

        }
    }

    private fun collectFromExpectationBlockChildren(e: PsiElement, sink: InlayTreeSink) {
        class AssertInlayHint : (PresentationTreeBuilder) -> Unit {
            override fun invoke(builder: PresentationTreeBuilder) = builder.text("assert")
        }

        if (e !is GrExpression || e is GrAssignmentExpression) return
        if (e.isSpockInteractionExpression()) return

        if (e.isVerifyAllExpression() || e.isWithExpression()) {
            val closure = e.lastChild as? GrClosableBlock ?: return
            closure.children.forEach { collectFromExpectationBlockChildren(it, sink) }
        } else {
            val position = InlineInlayPosition(e.textRange.startOffset, relatedToPrevious = false)
            sink.addPresentation(position, hasBackground = true, builder = AssertInlayHint())
        }
    }

    private fun GrExpression.isSpockInteractionExpression() = isMethodCallExpression("interaction")
    private fun GrExpression.isVerifyAllExpression() = isMethodCallExpression("verifyAll")
    private fun GrExpression.isWithExpression() = isMethodCallExpression("with")

    // TODO: this is too naive, we need to handle all edge cases!
    private fun GrExpression.isMethodCallExpression(method: String) =
        this is GrMethodCallExpression && callReference?.methodName == method
}
