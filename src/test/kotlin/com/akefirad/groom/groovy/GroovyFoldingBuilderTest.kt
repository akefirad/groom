package com.akefirad.groom.groovy

import com.intellij.testFramework.fixtures.BasePlatformTestCase

class GroovyFoldingBuilderTest : BasePlatformTestCase() {

    override fun getTestDataPath() = "src/test/resources/test-data/${javaClass.simpleName}"

    fun `test folding builder`() {
        myFixture.testFolding("$testDataPath/ExpectedTestData.groovy")
    }
}
