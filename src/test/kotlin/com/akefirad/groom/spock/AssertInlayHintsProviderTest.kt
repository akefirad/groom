package com.akefirad.groom.spock

import com.akefirad.groom.test.spock.util.Specification
import com.intellij.codeInsight.hints.InlayDumpUtil
import com.intellij.codeInsight.hints.declarative.InlayHintsProvider
import com.intellij.codeInsight.hints.declarative.InlayProviderPassInfo
import com.intellij.codeInsight.hints.declarative.impl.DeclarativeInlayHintsPass
import com.intellij.codeInsight.hints.declarative.impl.DeclarativeInlayRenderer
import com.intellij.codeInsight.hints.declarative.impl.TextInlayPresentationEntry
import com.intellij.openapi.progress.EmptyProgressIndicator
import com.intellij.testFramework.fixtures.LightPlatformCodeInsightFixture4TestCase
import com.intellij.testFramework.utils.inlays.InlayHintsProviderTestCase
import org.intellij.lang.annotations.Language
import org.jetbrains.plugins.groovy.lang.psi.GroovyPsiElementFactory
import org.junit.Test

class AssertInlayHintsProviderTest : LightPlatformCodeInsightFixture4TestCase() {

    private lateinit var elementFactory: GroovyPsiElementFactory

    override fun setUp() {
        super.setUp()
        elementFactory = GroovyPsiElementFactory.getInstance(project)
    }

    @Test // This is a complete test, don't remove it!
    fun `provider should provide implicit assert`() {
        val text = """
            ${Specification.CODE}
            
            abstract class MyBase {
                abstract void add(a, b)
            }
            
            class MySpec extends Specification {
                private final static Object obj = new Object()
                
                def 'something'() {
                    def assignment = 1 // should not be touched
                    
                    given: 'a foo'
                    def foo = 1
                    
                    and: 'a bar'
                    def bar = 2
                    bar == 2
            
                    when: 'foo + bar'
                    equals(foo, bar)
                    def baz = add(foo, bar)
                    baz == 3
                    
                    and: 'another when'
                    equals(foo, bar)
                    def quax = add(foo, bar)
                    quax == 3
            
                    then: 'should add assert to simple expressions'
                    /*<# assert #>*/baz == 3
                    /*<# assert #>*/baz == add(foo, bar)
                    
                    and: 'should add assert to method calls'
                    /*<# assert #>*/add(foo, bar) == 3
                    /*<# assert #>*/add(foo, bar)
                    /*<# assert #>*/equals(foo, bar)
                    
                    and: 'should not add assert to assignments'
                    assignment = 1
                    assignment = 2 
                    
                    and: 'should not add assert to interactions'
                    interaction {
                        add(foo, bar)
                    }
                    
                    and: // without title
                    /*<# assert #>*/obj != null
                    
                    when: 'should not touch following when blocks'
                    equals(foo, bar)
                    def baz2 = add(foo, bar)
                    baz2 == 3
                    
                    and: 'should not touch following when blocks'
                    equals(foo, bar)
                    def quax2 = add(foo, bar)
                    quax2 == 3
            
                    expect: 'should add assert to simple expressions'
                    /*<# assert #>*/3 == 3
                    /*<# assert #>*/3 == add(foo, bar)
                    /*<# assert #>*/!equals(1, 2)
                    
                    and: 'even for simple expressions'
                    /*<# assert #>*/true == true
                    /*<# assert #>*/true != false
                    
                    and: 'even for null'
                    /*<# assert #>*/null
                }
                
                def add(a, b) {
                    a + b
                }
                
                def equals(a, b) {
                    a == b
                }
            }
        """.trimIndent()
        testAnnotations(text)
    }

    @Test
    fun `provider should provide implicit assert when using verifyAll`() {
        val text = """
            ${Specification.CODE}
            class MySpec extends Specification {
                private final static int ONE = 1
            
                def 'something'() {
                    given: 'a foo and bar'
                    def foo = 1
            
                    when: 'foo + bar'
                    def baz = add(foo, bar)
                    
                    and: 'another when'
                    def quax = add(foo, bar)
            
                    then: 'should be 3'
                    verifyAll {
                        /*<# assert #>*/baz == 3
                        /*<# assert #>*/baz == add(foo, bar)
                    }
                    
                    and: 'should be 3'
                    verifyAll(baz) {
                        /*<# assert #>*/add(foo, bar) == 3
                        /*<# assert #>*/add(foo, bar)
                    }
                    
                    and: 'an error'
                    verifyAll()
                }
            }
        """.trimIndent()
        testAnnotations(text)
    }

    @Test
    fun `provider should provide implicit assert when using with`() {
        val text = """
            ${Specification.CODE}
            class MySpec extends Specification {
                private final static int ONE = 1
            
                def 'something'() {
                    given: 'a foo and bar'
                    def foo = 1
            
                    when: 'foo + bar'
                    def baz = add(foo, bar)
            
                    then: 'should be 3'
                    with(baz) {
                        /*<# assert #>*/baz == 3
                        /*<# assert #>*/baz == add(foo, bar)
                    }
                    
                    and: 'should be 3'
                    verifyAll {
                        with(baz) {
                            /*<# assert #>*/baz == 3
                            /*<# assert #>*/baz == add(foo, bar)
                        }
                    }
                    
                    and: 'should be 3'
                    with(baz) {
                        /*<# assert #>*/add(foo, bar) == 3
                        /*<# assert #>*/add(foo, bar)
                    }
                    
                    and: 'an error'
                    with(baz)
                }
            }
        """.trimIndent()
        testAnnotations(text)
    }

    @Test
    fun `provider should not provide implicit assert when assert is present`() {
        val text = """
            ${Specification.CODE}
            class MySpec extends Specification {
                private final static int ONE = 1
            
                def 'something'() {
                    given: 'a foo and bar'
                    def foo = 1
            
                    when: 'foo + bar'
                    def baz = add(foo, bar)
            
                    then: 'should be 3'
                    assert baz == 3
                    
                    and: 'should be 3'
                    with(baz) {
                        assert baz == add(foo, bar)
                        verifyAll {
                            assert baz == 3
                        }
                    }
                    
                    and: 'should be 3'
                    verifyAll {
                        assert baz == 3
                        with(baz) {
                            assert baz == 3
                        }
                    }
                }
            }
        """.trimIndent()
        testAnnotations(text)
    }

    private fun testAnnotations(@Language("Groovy") text: String) {
        doTestProvider("MySpec.groovy", text, AssertInlayHintsProvider())
    }

    @Suppress("UnstableApiUsage")
    private fun doTestProvider(
        fileName: String,
        expectedText: String,
        provider: InlayHintsProvider,
        enabledOptions: Map<String, Boolean> = emptyMap(),
        verifyHintsPresence: Boolean = false,
    ) {
        if (verifyHintsPresence) {
            InlayHintsProviderTestCase.verifyHintsPresence(expectedText)
        }
        val sourceText = InlayDumpUtil.removeHints(expectedText)
        myFixture.configureByText(fileName, sourceText)
        val file = myFixture.file!!
        val editor = myFixture.editor
        val providerInfo = InlayProviderPassInfo(provider, "provider.id", enabledOptions)
        val pass = DeclarativeInlayHintsPass(file, editor, listOf(providerInfo), isPreview = false)
        applyPassAndCheckResult(pass, sourceText, expectedText)
    }

    @Suppress("UnstableApiUsage")
    private fun applyPassAndCheckResult(
        pass: DeclarativeInlayHintsPass,
        previewText: String,
        expectedText: String,
    ) {
        pass.doCollectInformation(EmptyProgressIndicator())
        pass.applyInformationToEditor()

        val dump = InlayDumpUtil.dumpHintsInternal(previewText, renderer = { renderer, _ ->
            renderer as DeclarativeInlayRenderer
            renderer.presentationList.getEntries()
                .joinToString(separator = "|") { entry -> (entry as TextInlayPresentationEntry).text }
        }, file = myFixture.file!!, editor = myFixture.editor, document = myFixture.getDocument(myFixture.file!!))
        assertEquals(expectedText.trim(), dump.trim())
    }

}
