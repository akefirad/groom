package com.akefirad.groom.spock

import com.akefirad.groom.spock.SpockSpecUtils.hasAnySpecification
import com.akefirad.groom.spock.SpockSpecUtils.isContinuationLabel
import com.akefirad.groom.spock.SpockSpecUtils.isExpectationLabel
import com.akefirad.groom.spock.SpockSpecUtils.isSpockLabel
import com.intellij.codeInsight.hints.declarative.InlayHintsCollector
import com.intellij.codeInsight.hints.declarative.InlayHintsProvider
import com.intellij.codeInsight.hints.declarative.InlayTreeSink
import com.intellij.codeInsight.hints.declarative.InlineInlayPosition
import com.intellij.codeInsight.hints.declarative.PresentationTreeBuilder
import com.intellij.codeInsight.hints.declarative.SharedBypassCollector
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import org.jetbrains.plugins.groovy.lang.psi.api.statements.blocks.GrClosableBlock
import org.jetbrains.plugins.groovy.lang.psi.api.statements.blocks.GrOpenBlock
import org.jetbrains.plugins.groovy.lang.psi.api.statements.expressions.GrAssignmentExpression
import org.jetbrains.plugins.groovy.lang.psi.api.statements.expressions.GrExpression
import org.jetbrains.plugins.groovy.lang.psi.api.statements.expressions.path.GrMethodCallExpression
import org.jetbrains.plugins.groovy.lang.psi.api.statements.typedef.members.GrMethod

class AssertInlayHintsProvider : InlayHintsProvider {

    override fun createCollector(file: PsiFile, editor: Editor): InlayHintsCollector? {
        if (file.hasAnySpecification() == false) return null
        return AssertInlayHintsCollector()
    }

}

class AssertInlayHintsCollector : SharedBypassCollector {
    override fun collectFromElement(element: PsiElement, sink: InlayTreeSink) {
        if (element !is GrMethod) return
        val body = element.children.find { it is GrOpenBlock } as? GrOpenBlock ?: return
        val children = body.children.iterator()
        while (children.hasNext()) {
            while (children.hasNext() && children.next().isExpectationLabel() == false) continue
            collectFromExpectationBlockChildren(children, sink)
        }
    }

    private fun collectFromExpectationBlockChildren(children: Iterator<PsiElement>, sink: InlayTreeSink) {
        class AssertInlayHint : (PresentationTreeBuilder) -> Unit {
            override fun invoke(builder: PresentationTreeBuilder) = builder.text("assert")
        }

        while (children.hasNext()) {
            val e = children.next()
            if (e.isSpockLabel() && e.isContinuationLabel() == false) return
            if (e !is GrExpression || e is GrAssignmentExpression) continue
            if (e.isInteractionExpression()) continue
            if (e.isVerifyAllExpression() || e.isWithExpression()) {
                collectFromMethodCallExpression(e, sink)
            } else {
                val position = InlineInlayPosition(e.textRange.startOffset, relatedToPrevious = false)
                sink.addPresentation(position, hasBackground = true, builder = AssertInlayHint())
            }
        }
    }

    private fun collectFromMethodCallExpression(e: GrExpression, sink: InlayTreeSink) {
        val closure = e.lastChild as? GrClosableBlock ?: return
        collectFromExpectationBlockChildren(closure.children.iterator(), sink)
    }

    private fun GrExpression.isInteractionExpression() = isMethodCallExpression("interaction")
    private fun GrExpression.isVerifyAllExpression() = isMethodCallExpression("verifyAll")
    private fun GrExpression.isWithExpression() = isMethodCallExpression("with")

    // TODO: this is too naive, we need to handle all edge cases!
    private fun GrExpression.isMethodCallExpression(method: String) =
        this is GrMethodCallExpression && callReference?.methodName == method
}
