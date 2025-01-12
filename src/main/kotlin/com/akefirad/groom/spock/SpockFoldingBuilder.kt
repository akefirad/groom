package com.akefirad.groom.spock

import com.akefirad.groom.spock.SpockSpecUtils.hasAnySpecification
import com.akefirad.groom.spock.SpockSpecUtils.isSpockLabel
import com.intellij.lang.ASTNode
import com.intellij.lang.folding.CustomFoldingBuilder
import com.intellij.lang.folding.FoldingDescriptor
import com.intellij.openapi.editor.Document
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.impl.source.tree.LeafPsiElement
import org.jetbrains.plugins.groovy.lang.parser.GroovyElementTypes
import org.jetbrains.plugins.groovy.lang.psi.api.statements.typedef.members.GrMethod

class SpockFoldingBuilder : CustomFoldingBuilder() {

    override fun buildLanguageFoldRegions(
        descriptors: MutableList<FoldingDescriptor>,
        root: PsiElement,
        document: Document,
        quick: Boolean,
    ) {
        if (root.hasAnySpecification() == false) return
        addSpockLabelFoldRegions(Context(), descriptors, root)
    }

    // TODO: this is far from perfect, but it's a start! All edge cases need to be covered.
    private fun addSpockLabelFoldRegions(c: Context, d: MutableList<FoldingDescriptor>, e: PsiElement) {
        var ctx = c
        for (child in e.children) {
            if (child is GrMethod) {
                ctx = ctx.copy(method = child)
            } else if (child.isSpockLabel()) {
                val children = child.children
                val hasNothing = children.isEmpty() || children.first().isSpockLabel() // Can it be empty?
                if (hasNothing == false)
                    addSpockLabelFoldRegion(ctx, d, child)
            }

            addSpockLabelFoldRegions(ctx, d, child)
        }
    }

    private fun addSpockLabelFoldRegion(ctx: Context, d: MutableList<FoldingDescriptor>, e: PsiElement) {
        var next = e.nextSibling

        val start = e.textRange.startOffset
        var end = e.textRange.endOffset

        while (next != null && !(next.isSpockLabel())) {
            if (!next.isWhiteSpace() && next.textRange.endOffset < ctx.methodEndOffset())
                end = next.textRange.endOffset

            next = next.nextSibling
        }

        if (end > start) {
            val range = TextRange(start, end)
            d.add(FoldingDescriptor(e.node, range))
        }
    }

    private fun PsiElement.isWhiteSpace() =
        this is LeafPsiElement && text.trim().isEmpty()

    override fun getLanguagePlaceholderText(node: ASTNode, range: TextRange): String {
        val hasTitle = node.lastChildNode.elementType == GroovyElementTypes.LITERAL
        return if (hasTitle) node.text else (node.firstChildNode.text + ": ...")
    }

    override fun isRegionCollapsedByDefault(node: ASTNode) = false

    private data class Context(val method: PsiElement? = null) {
        fun methodEndOffset() = method?.textRange?.endOffset ?: throw IllegalStateException()
    }
}
