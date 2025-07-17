package com.akefirad.groom.groovy

import com.intellij.lang.ASTNode
import com.intellij.openapi.editor.Document
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiJavaFile
import com.intellij.testFramework.fixtures.LightPlatformCodeInsightFixture4TestCase
import org.jetbrains.plugins.groovy.lang.psi.api.statements.expressions.GrNewExpression
import org.jetbrains.plugins.groovy.lang.psi.api.types.GrCodeReferenceElement
import org.junit.Test

import static com.intellij.openapi.util.TextRange.EMPTY_RANGE
import static org.mockito.Mockito.mock
import static org.mockito.Mockito.times
import static org.mockito.Mockito.verify
import static org.mockito.Mockito.when

class GroovyFoldingBuilderTest extends LightPlatformCodeInsightFixture4TestCase {

    @Override
    String getTestDataPath() {
        return "src/test/resources/test-data/${this.class.simpleName}"
    }

    @Test
    void 'folding builder should fold'() {
        myFixture.testFolding("$testDataPath/ExpectedTestData.groovy")
    }

    @Test
    void 'buildFoldRegions should do nothing if file is not Groovy'() {
        // given:
        def subject = new GroovyFoldingBuilder()
        def root = mock(PsiJavaFile)
        when(root.node).thenReturn(null)

        // when:
        def result = subject.buildFoldRegions(root, mock(Document), false)

        // then:
        assert result.length == 0

        // and:
        verify(root).node
    }

    @Test
    @SuppressWarnings("GroovyAccessibility")
    void 'addConstructorCallFoldRegions should not fold constructor that is not properly closed'() {
        // given:
        def descriptors = []
        def subject = new GroovyFoldingBuilder()
        def root = mock(PsiElement)
        def constructor = mock(GrNewExpression)

        when(root.children).thenReturn([constructor] as PsiElement[])
        when(constructor.text).thenReturn("new Foo(\n  param")  // Not properly closed - missing )
        when(constructor.children).thenReturn([] as PsiElement[])

        // when:
        subject.addConstructorCallFoldRegions(descriptors, root)

        // then:
        assert descriptors.isEmpty()

        // and:
        verify(root).children
        verify(constructor).text
        verify(constructor).children
    }

    @Test
    @SuppressWarnings("GroovyAccessibility")
    void 'addConstructorCallFoldRegions should not fold constructor has no arguments (null)'() {
        // given:
        def descriptors = []
        def subject = new GroovyFoldingBuilder()
        def root = mock(PsiElement)
        def constructor = mock(GrNewExpression)

        when(root.children).thenReturn([constructor] as PsiElement[])
        when(constructor.text).thenReturn("new Foo(\n)")
        when(constructor.argumentList).thenReturn(null) // For the args check
        when(constructor.children).thenReturn([] as PsiElement[])

        // when:
        subject.addConstructorCallFoldRegions(descriptors, root)

        // then:
        assert descriptors.isEmpty()

        // and:
        verify(root).children
        verify(constructor, times(2)).text
        verify(constructor).argumentList
        verify(constructor).children
    }

    @Test
    void 'getLanguagePlaceholderText should fail when node is not supported type'() {
        // given:
        def subject = new GroovyFoldingBuilder()
        def node = mock(ASTNode)
        when(node.psi).thenReturn(mock(PsiElement))

        // expect:
        assertThrows(IllegalArgumentException) { subject.getLanguagePlaceholderText(node, EMPTY_RANGE) }

        // and:
        verify(node).psi
    }

    @Test
    void 'getLanguagePlaceholderText should handle GrNewExpression with null reference element'() {
        // given:
        def subject = new GroovyFoldingBuilder()
        def node = mock(ASTNode)
        def constructor = mock(GrNewExpression)

        when(node.psi).thenReturn(constructor)
        when(constructor.referenceElement).thenReturn(null)

        // when:
        def result = subject.getLanguagePlaceholderText(node, EMPTY_RANGE)

        // then:
        assert result == "new (...)"

        // and:
        verify(node).psi
        verify(constructor).referenceElement
    }

    @Test
    void 'getLanguagePlaceholderText should handle GrNewExpression with null reference name'() {
        // given:
        def subject = new GroovyFoldingBuilder()
        def node = mock(ASTNode)
        def constructor = mock(GrNewExpression)
        def reference = mock(GrCodeReferenceElement)

        when(node.psi).thenReturn(constructor)
        when(constructor.referenceElement).thenReturn(reference)
        when(reference.referenceName).thenReturn(null)

        // when:
        def result = subject.getLanguagePlaceholderText(node, EMPTY_RANGE)

        // then:
        assert result == "new (...)"

        // and:
        verify(node).psi
        verify(constructor).referenceElement
        verify(reference).referenceName
    }

    @Test
    void 'getLanguagePlaceholderText should return correct text for argument list'() {
        // given:
        def subject = new GroovyFoldingBuilder()
        def node = mock(ASTNode)
        def constructor = mock(GrNewExpression)
        def argumentList = mock(PsiElement)

        when(node.psi).thenReturn(argumentList)
        when(argumentList.parent).thenReturn(constructor)

        // when:
        def result = subject.getLanguagePlaceholderText(node, EMPTY_RANGE)

        // then:
        assert result == "(...)"

        // and:
        verify(node).psi
        verify(argumentList).parent
    }

}
