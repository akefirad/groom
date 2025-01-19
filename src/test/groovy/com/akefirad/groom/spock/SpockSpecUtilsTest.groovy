package com.akefirad.groom.spock

import com.intellij.mock.MockPsiFile
import com.intellij.mock.MockPsiManager
import com.intellij.mock.MockVirtualFile
import com.intellij.testFramework.fixtures.LightPlatformCodeInsightFixture4TestCase
import org.junit.Test

import static com.akefirad.groom.spock.SpockSpecUtils.hasAnySpecification

class SpockSpecUtilsTest extends LightPlatformCodeInsightFixture4TestCase {

    @Test
    void 'hasAnySpecification should return false for non-Groovy file'() {
        // given:
        def manager = new MockPsiManager(project)
        def file = new MockPsiFile(new MockVirtualFile("NonGroovy.txt"), manager)

        // when:
        def result = hasAnySpecification(file)

        // then:
        assertFalse(result)
    }

    // TODO: Add more tests for SpockSpecUtils
}
