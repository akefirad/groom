package com.akefirad.groom.spock

import com.akefirad.groom.spock.SpockSpecUtils.hasAnySpecification
import com.intellij.mock.MockPsiFile
import com.intellij.mock.MockPsiManager
import com.intellij.mock.MockVirtualFile
import com.intellij.testFramework.fixtures.LightPlatformCodeInsightFixture4TestCase
import org.junit.Test

class SpockSpecUtilsTest : LightPlatformCodeInsightFixture4TestCase() {

    @Test
    fun `hasAnySpecification should return false for non-Groovy file`() {
        // given:
        val manager = MockPsiManager(project)
        val file = MockPsiFile(MockVirtualFile("NonGroovy.txt"), manager)

        // when:
        val result = file.hasAnySpecification()

        // then:
        assertFalse(result)
    }

    // TODO: Add more tests for SpockSpecUtils
}
