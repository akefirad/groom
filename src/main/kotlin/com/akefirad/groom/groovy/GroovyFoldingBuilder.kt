package com.akefirad.groom.groovy

import com.akefirad.groom.intellij.PsiElementExtensions.endOffset
import com.akefirad.groom.intellij.PsiElementExtensions.startOffset
import com.intellij.lang.ASTNode
import com.intellij.lang.folding.CustomFoldingBuilder
import com.intellij.lang.folding.FoldingDescriptor
import com.intellij.openapi.editor.Document
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import org.jetbrains.plugins.groovy.lang.psi.GroovyFile
import org.jetbrains.plugins.groovy.lang.psi.api.auxiliary.GrListOrMap

class GroovyFoldingBuilder : CustomFoldingBuilder() {

    override fun buildLanguageFoldRegions(
        descriptors: MutableList<FoldingDescriptor>,
        root: PsiElement,
        document: Document,
        quick: Boolean,
    ) {
        if (root !is GroovyFile) return
        addCollectionFoldRegions(descriptors, root)
    }

    private fun addCollectionFoldRegions(d: MutableList<FoldingDescriptor>, e: PsiElement) {
        for (child in e.children) {
            if (child !is GrListOrMap) {
                addCollectionFoldRegions(d, child)
                continue
            }

            if (child.hasNoChildren()) continue
            if (child.isSingleLine()) continue
            if (child.hasLeftOpen()) continue

            val start = child.startOffset
            val end = child.endOffset
            val range = TextRange(start, end)
            d.add(FoldingDescriptor(child.node, range))
        }
    }

    private fun GrListOrMap.hasNoChildren() = children.isEmpty()
    private fun GrListOrMap.isSingleLine() = !text.contains("\n") // TODO: find a better way to do these!
    private fun GrListOrMap.hasLeftOpen() = !text.trim().endsWith("]") // TODO: find a better way to do these!

    public override fun getLanguagePlaceholderText(node: ASTNode, range: TextRange) = node.psi.let {
        check(it is GrListOrMap) { "Unexpected type: ${it.javaClass}" }
        if (it.isMap) "[...:...]" else "[...]"
    }

    override fun isRegionCollapsedByDefault(node: ASTNode) = false
}
