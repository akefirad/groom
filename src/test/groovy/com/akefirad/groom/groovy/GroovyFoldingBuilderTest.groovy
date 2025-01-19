package com.akefirad.groom.groovy

import com.intellij.lang.ASTNode
import com.intellij.openapi.editor.Document
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiJavaFile
import com.intellij.testFramework.fixtures.LightPlatformCodeInsightFixture4TestCase
import org.junit.Test

import static com.akefirad.oss.easymock.EasyMock.mock
import static org.easymock.EasyMock.anyObject
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
    void 'folding builder should do nothing if file is not Groovy'() {
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
    void 'getLanguagePlaceholderText should fail when node has no list or map'() {
        // given:
        def subject = new GroovyFoldingBuilder()
        def node = mock(ASTNode)
        expect(node.getChildren(anyObject())).andReturn(new ASTNode[0])
        replay(node)

        // expect:
        assertThrows(AssertionError) { subject.getLanguagePlaceholderText(node, new TextRange(0, 0)) }

        // and:
        verify(node)
    }

    @Test
    void 'getLanguagePlaceholderText should fail when node has non-list-map child'() {
        // given:
        def subject = new GroovyFoldingBuilder()
        def node = mock(ASTNode)
        def child = mock(ASTNode)
        expect(child.psi).andReturn(mock(PsiElement))
        expect(node.getChildren(anyObject())).andReturn(new ASTNode[]{child})
        replay(node, child)

        // expect:
        assertThrows(IllegalStateException) { subject.getLanguagePlaceholderText(node, new TextRange(0, 0)) }

        // and:
        verify(node, child)
    }
}
