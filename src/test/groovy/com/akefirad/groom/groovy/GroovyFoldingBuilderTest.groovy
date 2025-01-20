package com.akefirad.groom.groovy

import com.intellij.lang.ASTNode
import com.intellij.openapi.editor.Document
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiJavaFile
import com.intellij.testFramework.fixtures.LightPlatformCodeInsightFixture4TestCase
import org.junit.Test

import static com.akefirad.oss.easymock.EasyMock.mock
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
    void 'getLanguagePlaceholderText should fail when node is not GrListOrMap'() {
        // given:
        def subject = new GroovyFoldingBuilder()
        def node = mock(ASTNode)
        expect(node.getPsi()).andReturn(mock(PsiElement))
        replay(node)

        // expect:
        assertThrows(IllegalStateException) { subject.getLanguagePlaceholderText(node, TextRange.EMPTY_RANGE) }

        // and:
        verify(node)
    }
}
