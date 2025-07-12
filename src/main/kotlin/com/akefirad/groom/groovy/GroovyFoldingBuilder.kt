package com.akefirad.groom.groovy

import com.akefirad.groom.intellij.PsiElementExtensions.foldingDescriptor
import com.intellij.lang.ASTNode
import com.intellij.lang.folding.CustomFoldingBuilder
import com.intellij.lang.folding.FoldingDescriptor
import com.intellij.openapi.editor.Document
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import org.jetbrains.plugins.groovy.lang.psi.GroovyFile
import org.jetbrains.plugins.groovy.lang.psi.api.auxiliary.GrListOrMap
import org.jetbrains.plugins.groovy.lang.psi.api.statements.expressions.GrNewExpression


class GroovyFoldingBuilder : CustomFoldingBuilder() {

    override fun buildLanguageFoldRegions(
        descriptors: MutableList<FoldingDescriptor>,
        root: PsiElement,
        document: Document,
        quick: Boolean,
    ) {
        if (root !is GroovyFile) return
        addCollectionFoldRegions(descriptors, root)
        addConstructorCallFoldRegions(descriptors, root)
    }

    private fun addCollectionFoldRegions(d: MutableList<FoldingDescriptor>, e: PsiElement) {
        for (child in e.children) {
            if (child is GrListOrMap)
                addCollectionFoldingDescriptor(d, child)

            // Always recurse into children to handle nested collections
            addCollectionFoldRegions(d, child)
        }
    }

    private fun addCollectionFoldingDescriptor(d: MutableList<FoldingDescriptor>, child: GrListOrMap) {
        val isProperlyClosed = child.text.trim().endsWith("]") // TODO: find a better way to do these!
        if (isProperlyClosed == false) return

        val isMultiline = child.text.contains("\n") // TODO: find a better way to do these!
        if (isMultiline == false) return

        if (child.children.isNotEmpty())
            d.add(child.foldingDescriptor())
    }

    private fun addConstructorCallFoldRegions(d: MutableList<FoldingDescriptor>, e: PsiElement) {
        for (child in e.children) {
            if (child is GrNewExpression)
                addConstructorFoldingDescriptor(d, child)

            // Always recurse into children to handle nested constructor calls
            addConstructorCallFoldRegions(d, child)
        }
    }

    private fun addConstructorFoldingDescriptor(d: MutableList<FoldingDescriptor>, child: GrNewExpression) {
        val isProperlyClosed = child.text.trim().endsWith(")") // TODO: find a better way to do these!
        if (isProperlyClosed == false) return

        val isMultiLine = child.text.contains("\n") // TODO: find a better way to do these!
        if (isMultiLine == false) return

        val args = child.argumentList
        if (args?.expressionArguments?.isNotEmpty() == true)
            d.add(args.foldingDescriptor())
    }

    public override fun getLanguagePlaceholderText(node: ASTNode, range: TextRange) = node.psi.let {
        when (it) {
            is GrListOrMap -> if (it.isMap) "[...:...]" else "[...]"
            is GrNewExpression -> "new ${it.referenceElement?.referenceName ?: ""}(...)"
            else -> {
                // Check if this is an argument list from a constructor call
                if (it.parent is GrNewExpression) "(...)"
                else throw IllegalArgumentException("Unexpected type: ${it.javaClass}")
            }
        }
    }

    override fun isRegionCollapsedByDefault(node: ASTNode) = false
}
