package com.akefirad.groom.groovy

import com.intellij.testFramework.fixtures.BasePlatformTestCase

class GroovyFoldingBuilderTest : BasePlatformTestCase() {

    override fun getTestDataPath() = "src/test/resources/test-data"

    fun testFolding() {
        myFixture.testFolding("$testDataPath/${javaClass.simpleName}Data.groovy")
    }
}