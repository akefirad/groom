package com.akefirad.groom.spock

import com.intellij.psi.PsiElement
import org.jetbrains.plugins.groovy.lang.psi.api.statements.GrLabeledStatement
import kotlin.contracts.contract

object SpockSpecUtils {
    /*
    private const val SPOCK_SPEC_CLASS: String = "spock.lang.Specification"

    @JvmStatic
    fun PsiElement.hasAnySpecification(): Boolean {
        return this is GroovyFile && children
            .filterIsInstance<PsiClass>()
            .any(SpockSpecUtils::hasSpecificationSuperClass)
    }

    fun PsiElement.isSpecification(): Boolean {
        val clazz = PsiTreeUtil.findFirstParent(this, true) { it is PsiClass }
        return clazz != null && hasSpecificationSuperClass(clazz as PsiClass)
    }

    private fun hasSpecificationSuperClass(clazz: PsiClass) =
        generateSequence(clazz) { it.superClass }
            .any { it.qualifiedName == SPOCK_SPEC_CLASS }
    */

    fun PsiElement.isSpeckLabel(): Boolean {
        contract {
            returns(true) implies (this@isSpeckLabel is GrLabeledStatement)
        }
        return this is GrLabeledStatement && SpecLabel.entries.any { it.label == name }
    }

}
