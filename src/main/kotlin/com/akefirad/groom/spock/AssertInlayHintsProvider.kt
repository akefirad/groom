package com.akefirad.groom.spock

import com.akefirad.groom.intellij.PsiElementExtensions.startOffset
import com.akefirad.groom.spock.SpockSpecUtils.hasAnySpecification
import com.akefirad.groom.spock.SpockSpecUtils.isSpecification
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
import org.jetbrains.plugins.groovy.lang.psi.impl.statements.expressions.arithmetic.GrMultiplicativeExpressionImpl
import org.jetbrains.plugins.groovy.lang.psi.util.isWhiteSpaceOrNewLine

class AssertInlayHintsProvider : InlayHintsProvider, PossiblyDumbAware {
    override fun createCollector(file: PsiFile, editor: Editor): AssertInlayHintsCollector? {
        // TODO: check if it's an actual test file!
        return if (file.hasAnySpecification()) AssertInlayHintsCollector() else null
    }
}

class AssertInlayHintsCollector : SharedBypassCollector {

    override fun collectFromElement(element: PsiElement, sink: InlayTreeSink) {
        // TODO: also check if it's a test method!
        if (element is GrMethod && element.isSpecification()) {
            collectFromMethod(element, sink)
        }
    }

    private fun collectFromMethod(m: GrMethod, sink: InlayTreeSink) {
        m.children.forEach {
            if (it is GrOpenBlock) collectFromBlock(it, sink)
        }
    }

    private fun collectFromBlock(b: GrOpenBlock, sink: InlayTreeSink) {
        fun SpecLabelElement?.isExpectation() = this?.name?.isExpectation == true

        var label: SpecLabelElement? = null
        for (c in b.children) {
            if (c.isWhiteSpaceOrNewLine()) {
                continue
            }

            if (c.isSpeckLabel()) {
                val current = SpecLabelElement.ofLabel(c)
                if (current.isContinuation && label.isExpectation()) {
                    if (current.hasTitle == false) {
                        collectFromExpectationBlockChildren(c.lastChild, sink)
                    }
                } else if (current.isExpectation()) {
                    if (current.hasTitle == false) {
                        collectFromExpectationBlockChildren(c.lastChild, sink)
                    }
                    label = current
                } else {
                    label = null
                }
            } else if (label.isExpectation()) {
                collectFromExpectationBlockChildren(c, sink)
            }

        }
    }

    private fun collectFromExpectationBlockChildren(e: PsiElement, sink: InlayTreeSink) {
        if (e !is GrExpression || e is GrAssignmentExpression) return
        if (e.isSpockInteractionExpression()) return

        if (e.isSpockMockExpression()) {
            e.addAssertInlayHint(sink)
            e.lastChild?.let { collectFromClosableBlock(it.lastChild, sink) }
        } else if (e.isVerifyAllExpression()) {
            collectFromClosableBlock(e.lastChild, sink)
        } else if (e.isWithExpression()) {
            collectFromClosableBlock(e.lastChild, sink)
        } else {
            e.addAssertInlayHint(sink)
        }
    }

    private fun collectFromClosableBlock(e: PsiElement?, sink: InlayTreeSink) {
        val closure = e as? GrClosableBlock ?: return
        closure.children.forEach { collectFromExpectationBlockChildren(it, sink) }
    }

    private fun GrExpression.isVerifyAllExpression() = isMethodCallExpression("verifyAll")
    private fun GrExpression.isWithExpression() = isMethodCallExpression("with")

    private fun GrExpression.isSpockInteractionExpression() = isMethodCallExpression("interaction")
    private fun GrExpression.isSpockMockExpression(): Boolean {
        return this is GrMultiplicativeExpressionImpl // TODO: use GrBinaryExpression and check the operator!
            && rightOperand is GrMethodCallExpression // TODO: anything else to check?
            && leftOperand.text.let { it == "_" || it.matches("\\d+".toRegex()) } // FIXME: do it properly!
    }

    // TODO: this is too naive, we need to handle all edge cases!
    private fun GrExpression.isMethodCallExpression(method: String) =
        this is GrMethodCallExpression && callReference?.methodName == method

    private fun GrExpression.addAssertInlayHint(sink: InlayTreeSink) {
        class AssertInlayHint : (PresentationTreeBuilder) -> Unit {
            override fun invoke(builder: PresentationTreeBuilder) = builder.text("assert")
        }

        val position = InlineInlayPosition(startOffset, relatedToPrevious = false)
        sink.addPresentation(position, hasBackground = true, builder = AssertInlayHint())
    }
}
