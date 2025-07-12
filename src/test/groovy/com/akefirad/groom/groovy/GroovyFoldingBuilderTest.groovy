package com.akefirad.groom.groovy

import com.intellij.lang.ASTNode
import com.intellij.openapi.editor.Document
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiJavaFile
import com.intellij.testFramework.fixtures.LightPlatformCodeInsightFixture4TestCase
import org.jetbrains.plugins.groovy.lang.psi.api.statements.expressions.GrNewExpression
import org.jetbrains.plugins.groovy.lang.psi.api.types.GrCodeReferenceElement
import org.junit.Test

import static com.akefirad.oss.easymock.EasyMock.mock
import static com.intellij.openapi.util.TextRange.EMPTY_RANGE
import static org.easymock.EasyMock.expect
import static org.easymock.EasyMock.replay
import static org.easymock.EasyMock.verify

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

        expect(root.node).andReturn(null)
        replay(root)

        // when:
        def result = subject.buildFoldRegions(root, mock(Document), false)

        // then:
        assert result.length == 0

        // and:
        verify(root)
    }

    @Test
    @SuppressWarnings("GroovyAccessibility")
    void 'addConstructorCallFoldRegions should not fold constructor that is not properly closed'() {
        // given:
        def descriptors = []
        def subject = new GroovyFoldingBuilder()
        def root = mock(PsiElement)
        def constructor = mock(GrNewExpression)

        expect(root.getChildren()).andReturn([constructor] as PsiElement[])
        expect(constructor.getText()).andReturn("new Foo(\n  param")  // Not properly closed - missing )
        expect(constructor.getChildren()).andReturn([] as PsiElement[])
        replay(root, constructor)

        // when:
        subject.addConstructorCallFoldRegions(descriptors, root)

        // then:
        assert descriptors.isEmpty()

        // and:
        verify(root, constructor)
    }

    @Test
    @SuppressWarnings("GroovyAccessibility")
    void 'addConstructorCallFoldRegions should not fold constructor has no arguments (null)'() {
        // given:
        def descriptors = []
        def subject = new GroovyFoldingBuilder()
        def root = mock(PsiElement)
        def constructor = mock(GrNewExpression)

        expect(root.getChildren()).andReturn([constructor] as PsiElement[])
        expect(constructor.getText()).andReturn("new Foo(\n    bar\n)").anyTimes()
        expect(constructor.getArgumentList()).andReturn(null)  // For the args check
        expect(constructor.getChildren()).andReturn([] as PsiElement[])
        replay(root, constructor)

        // when:
        subject.addConstructorCallFoldRegions(descriptors, root)

        // then:
        assert descriptors.isEmpty()

        // and:
        verify(root, constructor)
    }

    @Test
    void 'getLanguagePlaceholderText should fail when node is not supported type'() {
        // given:
        def subject = new GroovyFoldingBuilder()
        def node = mock(ASTNode)

        expect(node.getPsi()).andReturn(mock(PsiElement))
        replay(node)

        // expect:
        assertThrows(IllegalArgumentException) { subject.getLanguagePlaceholderText(node, EMPTY_RANGE) }

        // and:
        verify(node)
    }

    @Test
    void 'getLanguagePlaceholderText should handle GrNewExpression with null reference element'() {
        // given:
        def subject = new GroovyFoldingBuilder()
        def node = mock(ASTNode)
        def constructor = mock(GrNewExpression)

        expect(node.getPsi()).andReturn(constructor)
        expect(constructor.getReferenceElement()).andReturn(null)
        replay(node, constructor)

        // when:
        def result = subject.getLanguagePlaceholderText(node, EMPTY_RANGE)

        // then:
        assert result == "new (...)"

        // and:
        verify(node, constructor)
    }

    @Test
    void 'getLanguagePlaceholderText should handle GrNewExpression with null reference name'() {
        // given:
        def subject = new GroovyFoldingBuilder()
        def node = mock(ASTNode)
        def constructor = mock(GrNewExpression)
        def reference = mock(GrCodeReferenceElement)

        expect(node.getPsi()).andReturn(constructor)
        expect(constructor.getReferenceElement()).andReturn(reference)
        expect(reference.getReferenceName()).andReturn(null)
        replay(node, constructor, reference)

        // when:
        def result = subject.getLanguagePlaceholderText(node, EMPTY_RANGE)

        // then:
        assert result == "new (...)"

        // and:
        verify(node, constructor, reference)
    }

    @Test
    void 'getLanguagePlaceholderText should return correct text for argument list'() {
        // given:
        def subject = new GroovyFoldingBuilder()
        def node = mock(ASTNode)
        def constructor = mock(GrNewExpression)
        def argumentList = mock(PsiElement)

        expect(node.getPsi()).andReturn(argumentList)
        expect(argumentList.getParent()).andReturn(constructor)
        replay(node, argumentList, constructor)

        // when:
        def result = subject.getLanguagePlaceholderText(node, EMPTY_RANGE)

        // then:
        assert result == "(...)"

        // and:
        verify(node, argumentList, constructor)
    }

}
