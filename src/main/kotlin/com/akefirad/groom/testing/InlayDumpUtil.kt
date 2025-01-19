package com.akefirad.groom.testing

import com.intellij.openapi.editor.Document
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.EditorCustomElementRenderer
import com.intellij.openapi.editor.Inlay
import com.intellij.psi.PsiFile
import java.util.function.BiFunction
import com.intellij.codeInsight.hints.InlayDumpUtil as OriginalInlayDumpUtil

@Suppress("UnstableApiUsage")
object InlayDumpUtil {
    @JvmStatic
    fun removeHints(text: String) = OriginalInlayDumpUtil.removeHints(text)

    @JvmStatic
    fun dumpHintsInternal(
        sourceText: String,
        file: PsiFile,
        editor: Editor,
        document: Document,
        renderer: BiFunction<EditorCustomElementRenderer, Inlay<*>, String>,
    ) = OriginalInlayDumpUtil.dumpHintsInternal(
        sourceText = sourceText,
        filter = null,
        renderer = { r, i -> renderer.apply(r, i) },
        file = file,
        editor = editor,
        document = document,
        offsetShift = 0,
    )
}
