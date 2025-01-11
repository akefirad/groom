package com.akefirad.groom.spock

import com.intellij.psi.PsiClass
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import org.jetbrains.plugins.groovy.lang.psi.GroovyFile
import org.jetbrains.plugins.groovy.lang.psi.api.statements.GrLabeledStatement

object SpockSpecUtils {
    const val SPOCK_SPEC_CLASS: String = "spock.lang.Specification"

    fun PsiElement.hasAnySpecification(): Boolean {
        return this is GroovyFile && children
            .filterIsInstance<PsiClass>()
            .any(SpockSpecUtils::hasSpecificationSuperClass)
    }

    /*
    fun PsiElement.isSpecification(): Boolean {
        val clazz = PsiTreeUtil.findFirstParent(this, true) { it is PsiClass }
        return clazz != null && hasSpecificationSuperClass(clazz as PsiClass)
    }
    */

    private fun hasSpecificationSuperClass(clazz: PsiClass) =
        generateSequence(clazz) { it.superClass }
            .any { it.qualifiedName == SPOCK_SPEC_CLASS }


    fun PsiElement.isSpockLabel() =
        this is GrLabeledStatement && SpockSpecLabel.entries.any { it.label == name }

}
