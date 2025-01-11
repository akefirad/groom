package com.akefirad.groom.spock

import com.intellij.testFramework.fixtures.BasePlatformTestCase

class SpockFoldingBuilderTest : BasePlatformTestCase() {

    override fun getTestDataPath() = "src/test/resources/test-data"

    fun testFolding() {
        myFixture.testFolding("$testDataPath/${javaClass.simpleName}Data.groovy")
    }
}
