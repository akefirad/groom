package com.akefirad.groom.spock

import com.intellij.testFramework.fixtures.BasePlatformTestCase

class SpockFoldingBuilderTest : BasePlatformTestCase() {

    override fun getTestDataPath(): String = "src/test/testData"

    fun testFolding() {
        myFixture.testFolding("$testDataPath/SpockFoldingBuilderTestData.groovy")
    }
}
