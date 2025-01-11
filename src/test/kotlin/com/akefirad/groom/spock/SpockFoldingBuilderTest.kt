package com.akefirad.groom.spock

import com.intellij.testFramework.fixtures.BasePlatformTestCase

class SpockFoldingBuilderTest : BasePlatformTestCase() {

    override fun getTestDataPath() = "src/test/resources/test-data//${javaClass.simpleName}"

    fun `test builder should fold Spock labels`() {
        myFixture.testFolding("$testDataPath/ShouldFoldTestData.groovy")
    }

    fun `TODO test builder should do nothing when the file is not Groovy file`() {
        myFixture.testFolding("$testDataPath/ShouldNotFoldTestData.java")
    }

    fun `test builder should do nothing when the file does not have any Spock specification`() {
        myFixture.testFolding("$testDataPath/ShouldNotFoldTestData.groovy")
    }
}
