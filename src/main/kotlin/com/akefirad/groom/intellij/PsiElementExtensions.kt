package com.akefirad.groom.intellij

import com.intellij.psi.PsiElement

object PsiElementExtensions {
    val PsiElement.startOffset get() = textRange.startOffset
    val PsiElement.endOffset get() = textRange.endOffset
}
