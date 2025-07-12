package com.akefirad.groom.intellij

import com.intellij.lang.folding.FoldingDescriptor
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement

object PsiElementExtensions {
    val PsiElement.startOffset get() = textRange.startOffset
    val PsiElement.endOffset get() = textRange.endOffset

    fun PsiElement.foldingDescriptor(range: TextRange = textRange) =
        FoldingDescriptor(node, range)
}
