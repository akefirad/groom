package com.akefirad.groom.groovy

import com.intellij.lang.ASTNode
import com.intellij.openapi.editor.Document
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiJavaFile
import com.intellij.testFramework.fixtures.LightPlatformCodeInsightFixture4TestCase
import org.junit.Test

import static org.mockito.Mockito.mock
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
    void 'folding builder should do nothing if file is not Groovy'() {
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
    void 'getLanguagePlaceholderText should fail when node is not GrListOrMap'() {
        // given:
        def subject = new GroovyFoldingBuilder()
        def node = mock(ASTNode)
        when(node.psi).thenReturn(mock(PsiElement))

        // expect:
        assertThrows(IllegalStateException) { subject.getLanguagePlaceholderText(node, TextRange.EMPTY_RANGE) }

        // and:
        verify(node).psi
    }
}
