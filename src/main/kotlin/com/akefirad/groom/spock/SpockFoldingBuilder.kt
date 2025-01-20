package com.akefirad.groom.spock

import com.akefirad.groom.intellij.PsiElementExtensions.endOffset
import com.akefirad.groom.spock.SpockSpecUtils.isSpeckLabel
import com.intellij.lang.ASTNode
import com.intellij.lang.folding.CustomFoldingBuilder
import com.intellij.lang.folding.FoldingDescriptor
import com.intellij.openapi.editor.Document
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiElement
import org.jetbrains.plugins.groovy.lang.psi.GroovyFile
import org.jetbrains.plugins.groovy.lang.psi.api.statements.GrLabeledStatement
import org.jetbrains.plugins.groovy.lang.psi.api.statements.blocks.GrOpenBlock
import org.jetbrains.plugins.groovy.lang.psi.api.statements.typedef.members.GrMethod
import org.jetbrains.plugins.groovy.lang.psi.impl.statements.typedef.GrTypeDefinitionBodyBase.GrClassBody
import org.jetbrains.plugins.groovy.lang.psi.util.isWhiteSpaceOrNewLine


class SpockFoldingBuilder : CustomFoldingBuilder() {

    override fun buildLanguageFoldRegions(
        descriptors: MutableList<FoldingDescriptor>,
        root: PsiElement,
        document: Document,
        quick: Boolean,
    ) {
        if (root !is GroovyFile) return
        for (cls in root.classes) {
            // val fw = TestFrameworks.detectFramework(cls)
            // TODO: skip if not a (Spock) test class
            descriptors.addAll(addSpockLabelFoldRegions(cls))
        }
    }

    private fun addSpockLabelFoldRegions(c: PsiClass): List<FoldingDescriptor> {
        return c.children.flatMap {
            when (it) {
                is GrClassBody -> addSpockLabelFoldRegions(it)
                else -> emptyList()
            }
        }
    }

    private fun addSpockLabelFoldRegions(b: GrClassBody): List<FoldingDescriptor> {
        return b.children.flatMap {
            when (it) {
                is PsiClass -> addSpockLabelFoldRegions(it)
                is GrMethod -> addSpockLabelFoldRegions(it)
                else -> emptyList()
            }
        }
    }

    private fun addSpockLabelFoldRegions(m: GrMethod): List<FoldingDescriptor> {
        return m.children.flatMap {
            when (it) {
                is GrOpenBlock -> addSpockLabelFoldRegions(m, it)
                else -> emptyList()
            }
        }
    }

    private fun addSpockLabelFoldRegions(m: GrMethod, b: GrOpenBlock): List<FoldingDescriptor> {
        val result = mutableListOf<FoldingContext>()
        var ctx = FoldingContext()
        for (c in b.children) {
            if (c.endOffset >= m.endOffset) {
                break
            }

            if (c.isWhiteSpaceOrNewLine()) {
                continue
            }

            if (c.isSpeckLabel()) {
                val label = SpecLabelElement.ofLabel(c)
                ctx = if (label.isContinuation) {
                    if (result.isEmpty()) {
                        FoldingContext.from(label).also(result::add)
                    } else {
                        result.last().nested(label)
                    }
                } else {
                    ctx.restart(label).also(result::add)
                }
            } else {
                ctx.expand(c.textRange)
            }
        }
        return result.flatMap(FoldingContext::fold)
    }

    override fun getLanguagePlaceholderText(node: ASTNode, range: TextRange): String {
        val label = SpecLabelElement.ofLabel(node.psi as GrLabeledStatement)
        return (label.name.label + if (label.hasTitle) " ${label.title}" else "...")
    }

    override fun isRegionCollapsedByDefault(node: ASTNode) = false

    private data class FoldingContext(
        private val label: SpecLabelElement? = null,
        private val nested: Boolean = false,
        private val children: MutableList<FoldingContext> = mutableListOf(),
    ) {
        private var range: TextRange = label?.range ?: TextRange.EMPTY_RANGE

        fun restart(e: SpecLabelElement): FoldingContext {
            return from(e)
        }

        fun expand(r: TextRange) {
            if (label == null) return
            range = range.let {
                assert(it.endOffset <= r.startOffset)
                TextRange(it.startOffset, r.endOffset)
            }
        }

        fun nested(e: SpecLabelElement): FoldingContext {
            assert(label != null) { "Label is null!" }
            check(e.isContinuation) { "Expected a continuation label: $e" }
            return from(e, true).also(children::add)
        }

        fun fold(): List<FoldingDescriptor> {
            if (label == null) return emptyList()
            val range = range
            return buildList {
                if (label.hasTitle) {
                    add(FoldingDescriptor(label.element, range))
                }

                for (c in children) {
                    addAll(c.fold())
                }

                if (children.isNotEmpty()) {
                    val first = label.range.startOffset
                    val last = children.last().range.endOffset
                    add(FoldingDescriptor(label.element, TextRange(first, last)))
                }
            }
        }

        companion object {
            fun from(e: SpecLabelElement, nested: Boolean = false) = FoldingContext(e, nested)
        }
    }
}
