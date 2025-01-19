package com.akefirad.groom.spock


import com.intellij.testFramework.fixtures.LightPlatformCodeInsightFixture4TestCase
import org.jetbrains.plugins.groovy.lang.psi.api.statements.GrLabeledStatement
import org.junit.Test

class SpecLabelElementTest extends LightPlatformCodeInsightFixture4TestCase {

    @Test
    void 'ofLabel should parse untitled labels'() {
        given:
            def code = '''
            class MySpec extends Specification {
                def 'should do something'() {
                    given:
                    def foo = 1
                    def bar = 2
                }
            }
            '''.stripIndent()
            myFixture.configureByText('MySpec.groovy', code)
            def element = myFixture.findElementByText('given:', GrLabeledStatement)

        when:
            def label = SpecLabelElement.ofLabel(element)

        then:
            assert label.title == null
            assert label.toString() == 'given'
    }

    @Test
    void 'ofLabel should parse titled labels'() {
        given:
            def code = '''
            class MySpec extends Specification {
                def 'should do something'() {
                    given: 'some given'
                    def foo = 1
                    def bar = 2
                }
            }
            '''.stripIndent()
            myFixture.configureByText('MySpec.groovy', code)
            def element = myFixture.findElementByText('given:', GrLabeledStatement)

        when:
            def label = SpecLabelElement.ofLabel(element)

        then:
            assert label.title == 'some given'
            assert label.toString() == "given: 'some given'"
    }

    @Test
    void 'ofLabel should parse titled labels but title is empty'() {
        given:
            def code = '''
            class MySpec extends Specification {
                def 'should do something'() {
                    given: ''
                    def foo = 1
                    def bar = 2
                }
            }
            '''.stripIndent()
            myFixture.configureByText('MySpec.groovy', code)
            def element = myFixture.findElementByText('given:', GrLabeledStatement)

        when:
            def label = SpecLabelElement.ofLabel(element)

        then:
            assert label.title == ''
    }

    @Test
    void 'ofLabel should parse untitled label preceding string literal'() {
        given:
            def code = '''
            class MySpec extends Specification {
                def 'should do something'() {
                    given:
                    'some string'
                    def foo = 1
                    def bar = 2
                }
            }
            '''.stripIndent()
            myFixture.configureByText('MySpec.groovy', code)
            def element = myFixture.findElementByText('given:', GrLabeledStatement)

        when:
            def label = SpecLabelElement.ofLabel(element)

        then:
            assert label.title == null
    }

    @Test
    void 'ofLabel should parse untitled labels proceeding non literal'() {
        given:
            def code = '''
            class MySpec extends Specification {
                def 'should do something'() {
                    given: foo()
                    'not a title'
                    def foo = 1
                    def bar = 2
                }
            }
            '''.stripIndent()
            myFixture.configureByText('MySpec.groovy', code)
            def element = myFixture.findElementByText('given:', GrLabeledStatement)

        when:
            def label = SpecLabelElement.ofLabel(element)

        then:
            assert label.title == null
            assert label.toString() == 'given'
    }

    @Test
    void 'ofLabel should parse untitled labels proceeding non-string literal'() {
        given:
            def code = '''
            class MySpec extends Specification {
                def 'should do something'() {
                    given: true
                    'not a title'
                    def foo = 1
                    def bar = 2
                }
            }
            '''.stripIndent()
            myFixture.configureByText('MySpec.groovy', code)
            def element = myFixture.findElementByText('given:', GrLabeledStatement)

        when:
            def label = SpecLabelElement.ofLabel(element)

        then:
            assert label.title == null
            assert label.toString() == 'given'
    }

}
