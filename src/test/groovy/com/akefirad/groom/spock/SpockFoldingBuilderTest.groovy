package com.akefirad.groom.spock


import com.intellij.testFramework.fixtures.LightPlatformCodeInsightFixture4TestCase
import org.jetbrains.plugins.groovy.lang.psi.api.statements.GrLabeledStatement
import org.junit.Test

@SuppressWarnings('GroovyAccessibility')
class SpockFoldingBuilderTest extends LightPlatformCodeInsightFixture4TestCase {

    @Override
    protected String getTestDataPath() {
        return "src/test/resources/test-data/${this.class.simpleName}"
    }

    @Test
    void 'builder should fold multiple labels'() {
        myFixture.testFolding("$testDataPath/ShouldFoldMultipleLabelsSpec.groovy")
    }

    @Test
    void 'builder should not fold untitled labels'() {
        myFixture.testFolding("$testDataPath/ShouldNotFoldUntitledLabelSpec.groovy")
    }

    @Test
    void 'builder should fold titled labels'() {
        myFixture.testFolding("$testDataPath/ShouldFoldTitledLabelSpec.groovy")
    }

    @Test
    void 'build should fold nested classes'() {
        myFixture.testFolding("$testDataPath/ShouldFoldNestedClassesTestData.groovy")
    }

    @Test
    void 'build should not fail while folding'() {
        myFixture.testFolding("$testDataPath/ShouldNotFailWhileFoldingSpec.groovy")
    }

    @Test
    void 'empty FoldingContext should return no fold descriptors'() {
        given:
            def context = new SpockFoldingBuilder.FoldingContext(null, false, [])

        when:
            def descriptors = context.fold()

        then:
            descriptors.isEmpty()
    }

    @Test
    void 'empty FoldingContext should fail when calling nested'() {
        given:
            def code = '''
            class MySpec extends Specification {
                def 'should do something'() {
                    and:
                    def bar = 2
                }
            }
            '''.stripIndent()
            myFixture.configureByText('MySpec.groovy', code)
            def element = myFixture.findElementByText('and:', GrLabeledStatement)
            def context = new SpockFoldingBuilder.FoldingContext(null, false, [])

        expect:
            assertThrows(AssertionError) {
                context.nested(SpecLabelElement.ofLabel(element))
            }
    }

    @Test
    void 'FoldingContext should fail when calling nested with non-continuation label'() {
        given:
            def code = '''
            class MySpec extends Specification {
                def 'should do something'() {
                    given:
                    def foo = 1
                    
                    when:
                    def bar = 2                    
                }
            }
            '''.stripIndent()
            myFixture.configureByText('MySpec.groovy', code)
            def given = myFixture.findElementByText('given:', GrLabeledStatement)
            def context = new SpockFoldingBuilder.FoldingContext(SpecLabelElement.ofLabel(given), false, [])
            def when = myFixture.findElementByText('when:', GrLabeledStatement)

        expect:
            assertThrows(IllegalStateException) {
                context.nested(SpecLabelElement.ofLabel(when))
            }
    }

    @Test
    void 'SpockFoldingBuilder should do nothing when root is not GroovyFile'() {
        given:
            def code = '''
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
            myFixture.configureByText('MySpec.java', code)
            def file = myFixture.file
            def document = myFixture.getDocument(file)

        when:
            new SpockFoldingBuilder().buildFoldRegions(file, document, false)

        then:
            true // TODO: How to check it's not folded?
    }
}
