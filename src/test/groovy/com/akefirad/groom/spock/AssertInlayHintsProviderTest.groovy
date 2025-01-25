package com.akefirad.groom.spock

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

import static com.akefirad.groom.test.spock.util.SpockSpecification.CODE as SpecificationClass

class AssertInlayHintsProviderTest extends LightPlatformCodeInsightFixture4TestCase {

    private GroovyPsiElementFactory elementFactory

    void setUp() {
        super.setUp()
        elementFactory = GroovyPsiElementFactory.getInstance(project)
    }

    @Test
    void 'provider should provide implicit assert to simple spec'() {
        def code = """
            $SpecificationClass

            class MySpec extends Specification {
                def 'test with untitled labels'() {
                    given:
                    def foo = 1
                    def bar = 2 

                    when:
                    def baz = foo + bar
                    add(foo, bar)

                    then:
                    /*<# assert #>*/'literal string'
                    /*<# assert #>*/true // literal boolean
                    /*<# assert #>*/bar == foo
                    /*<# assert #>*/equals(baz, foo + bar)
                }

                def 'test with untitled labels'() {
                    given: 'a foo and bar'
                    def foo = 1
                    def bar = 2 

                    when: 'foo + bar'
                    def baz = foo + bar
                    add(foo, bar)

                    then: 'should be equal'
                    /*<# assert #>*/'literal string'
                    /*<# assert #>*/true // literal boolean
                    /*<# assert #>*/bar == foo
                    /*<# assert #>*/equals(baz, foo + bar)
                }
            }
        """.stripIndent()
        testAnnotations(code)
    }

    @Test
    void 'provider should provide implicit assert to simple spec with repeated labels'() {
        def code = """
            $SpecificationClass

            class MySpec extends Specification {
                def 'test with untitled labels'() {
                    given:
                    def foo = 1
                    def bar = 2 

                    when:
                    def baz = foo + bar
                    add(foo, bar)

                    then:
                    /*<# assert #>*/'literal string'
                    /*<# assert #>*/true // literal boolean
                    /*<# assert #>*/bar == foo
                    /*<# assert #>*/equals(baz, foo + bar)

                    when:
                    def baz2 = foo + bar
                    add(foo, bar)

                    then:
                    /*<# assert #>*/'literal string'
                    /*<# assert #>*/true // literal boolean
                    /*<# assert #>*/bar == foo
                    /*<# assert #>*/equals(baz, foo + bar)
                }

                def 'test with titled labels'() {
                    given: 'a foo and bar'
                    def foo = 1
                    def bar = 2 

                    when: 'foo + bar'
                    def baz = foo + bar
                    add(foo, bar)

                    then: 'should be equal'
                    /*<# assert #>*/'literal string'
                    /*<# assert #>*/true // literal boolean
                    /*<# assert #>*/bar == foo
                    /*<# assert #>*/equals(baz, foo + bar)

                    when: 'foo + bar'
                    def baz2 = foo + bar
                    add(foo, bar)

                    then: 'should be equal'
                    /*<# assert #>*/'literal string'
                    /*<# assert #>*/true // literal boolean
                    /*<# assert #>*/bar == foo
                    /*<# assert #>*/equals(baz, foo + bar)
                }
            }
        """.stripIndent()
        testAnnotations(code)
    }

    @Test
    void 'provider should provide implicit assert for complex code'() {
        def code = """
            $SpecificationClass

            abstract class MyBase {
                abstract void add(a, b)
            }

            class MySpec extends Specification {
                private final static int ONE = 1

                def 'something'() {
                    def assignment = 1 // should not be touched

                    given: 'a foo'
                    def foo = 1

                    and: 'a bar'
                    def bar = 2
                    bar == 2

                    when: 'foo + bar'
                    baz == 3
                    add(foo, bar)

                    and: 'another when'
                    quax == 3
                    equals(foo, bar)
                    add(foo, bar)

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

                    and:
                    /*<# assert #>*/'should add assert to untitled continuation'

                    and: 'should not add assert to interactions'
                    interaction {
                        add(foo, bar)
                    }

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
        """.stripIndent()
        testAnnotations(code)
    }

    @Test
    void 'provider should provide implicit assert when using verifyAll'() {
        def code = """
            $SpecificationClass

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
        """.stripIndent()
        testAnnotations(code)
    }

    @Test
    void 'provider should provide implicit assert when using with'() {
        def code = """
            $SpecificationClass

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
        """.stripIndent()
        testAnnotations(code)
    }

    @Test
    void 'provider should provide implicit assert for spock mocks'() {
        def code = """
            $SpecificationClass

            class MySpec extends Specification {
                def 'something'() {
                    given: 'a foo and bar'
                    def foo = 1
                    def bar = mock(Object)

                    when: 'foo + bar'
                    def baz = add(foo, bar)

                    then: 'should be 3'
                    /*<# assert #>*/_ * bar.foo()
                    /*<# assert #>*/1 * bar.foo()
                    /*<# assert #>*/1 * bar.bar(foo)
                    /*<# assert #>*/1 * bar.foo() >> 1
                    /*<# assert #>*/1 * bar.bar(foo) >> 2
                    
                    and: 'nested assertions'
                    /*<# assert #>*/_ * bar.foo {
                        /*<# assert #>*/it.foo == 'foo'
                    }                
                    /*<# assert #>*/1 * bar.foo {
                        /*<# assert #>*/it.foo == 'foo'
                    }                

                    and: 'nested verifyAll'
                    /*<# assert #>*/1 * bar.foo {
                        verifyAll(baz) {
                            /*<# assert #>*/it.baz == 3
                            /*<# assert #>*/it.baz == add(foo, bar)
                        }
                    }                

                    and: 'nested with'
                    /*<# assert #>*/1 * bar.foo {
                        with(baz) {
                            /*<# assert #>*/it.baz == 3
                            /*<# assert #>*/it.baz == add(foo, bar)
                        }
                    }
                    
                    and: 'non-mock'
                    /*<# assert #>*/1 + 2
                    /*<# assert #>*/1 * 2
                    /*<# assert #>*/1 + bar.foo()
                    /*<# assert #>*/1.2 + bar.foo()
                    /*<# assert #>*/foo.bar() + bar.foo()
                    /*<# assert #>*/1.2 * bar.foo()
                    /*<# assert #>*/1.2 * bar.foo {
                        it.baz == 3
                        with(baz) {
                            it.baz == 3
                        }
                        verifyAll(baz) {
                            it.baz == 3
                        }
                    }
                }
            }
        """.stripIndent()
        testAnnotations(code)
    }

    @Test
    void 'provider should not provide implicit assert when assert is present'() {
        def code = """
            $SpecificationClass

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
        """.stripIndent()
        testAnnotations(code)
    }

    @Test
    void 'provider should do nothing when file is not spock specification'() {
        given:
            def code = '''
            package foo.bar;
            class Specification {}
            class SampleSpec extends Specification {
                void test_method() <fold text='{...}'>{
                    given: "some given block"
                    var foo = 'foo';
            
                    when: "some when block"
                    var baz = foo + bar;
            
                    then: "some then block"
                    baz == 'foobar';
                }</fold>
            }
            '''.stripIndent()

        when:
            doTestProvider("MySpec.java", code, new AssertInlayHintsProvider())

        then:
            true // TODO: How to check it's not folded?
    }

    private void testAnnotations(@Language("Groovy") String code) {
        doTestProvider("MySpec.groovy", code, new AssertInlayHintsProvider())
    }

    private void doTestProvider(
        String fileName,
        String expectedText,
        InlayHintsProvider provider,
        Map<String, Boolean> enabledOptions = [:],
        boolean verifyHintsPresence = false
    ) {
        if (verifyHintsPresence) {
            InlayHintsProviderTestCase.Companion.verifyHintsPresence(expectedText)
        }
        def sourceText = InlayDumpUtil.INSTANCE.removeHints(expectedText)
        myFixture.configureByText(fileName, sourceText)
        def file = myFixture.file
        def editor = myFixture.editor
        def providerInfo = new InlayProviderPassInfo(provider, "provider.id", enabledOptions)
        def pass = new DeclarativeInlayHintsPass(file, editor, [providerInfo], false, false)
        applyPassAndCheckResult(pass, sourceText, expectedText)
    }

    private void applyPassAndCheckResult(
        DeclarativeInlayHintsPass pass,
        String previewText,
        String expectedText
    ) {
        pass.doCollectInformation(new EmptyProgressIndicator())
        pass.applyInformationToEditor()
        def file = myFixture.file
        def doc = myFixture.getDocument(file)
        def editor = myFixture.editor
        def dump = InlayDumpUtil.INSTANCE.dumpHintsInternal(
            previewText,
            null,
            { r, _ ->
                (r as DeclarativeInlayRenderer).presentationList
                    .entries
                    .collect { it as TextInlayPresentationEntry }
                    .collect { it.text }
                    .join("|")
            },
            file,
            editor,
            doc,
            0,
        )
        assertEquals(expectedText.trim(), dump.trim())
    }
}
