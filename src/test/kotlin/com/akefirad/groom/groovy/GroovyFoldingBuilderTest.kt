package com.akefirad.groom.groovy

import com.intellij.lang.ASTNode
import com.intellij.openapi.editor.Document
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiJavaFile
import com.intellij.testFramework.fixtures.LightPlatformCodeInsightFixture4TestCase
import io.mockk.Called
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test

class GroovyFoldingBuilderTest : LightPlatformCodeInsightFixture4TestCase() {

    override fun getTestDataPath() = "src/test/resources/test-data/${javaClass.simpleName}"

    @Test
    fun `folding builder should fold`() {
        myFixture.testFolding("$testDataPath/ExpectedTestData.groovy")
    }

    @Test
    fun `folding builder should do nothing if file is not Groovy`() {
        // given:
        val subject = GroovyFoldingBuilder()
        val document = mockk<Document>()
        val element = mockk<PsiJavaFile>()
        every { element.node } returns null

        // when:
        val result = subject.buildFoldRegions(element, document, false)

        // then:
        assertEmpty(result)

        // and:
        verify { document wasNot Called }
    }

    @Test
    fun `getLanguagePlaceholderText should fail when node has no list or map`() {
        // given:
        val subject = GroovyFoldingBuilder()
        val node = mockk<ASTNode>()
        every { node.getChildren(any()) } returns emptyArray() // TODO: remove any() from getChildren

        // expect:
        assertThrows(AssertionError::class.java) { subject.getLanguagePlaceholderText(node, TextRange(0, 0)) }
    }


    @Test
    fun `getLanguagePlaceholderText should fail when node has non-list-map child`() {
        // given:
        val subject = GroovyFoldingBuilder()
        val node = mockk<ASTNode>()
        val child = mockk<ASTNode>()
        every { child.psi } returns mockk()
        every { node.getChildren(any()) } returns arrayOf(child) // TODO: remove any() from getChildren

        // expect:
        assertThrows(IllegalStateException::class.java) { subject.getLanguagePlaceholderText(node, TextRange(0, 0)) }
    }
}
