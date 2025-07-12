package com.akefirad.groom.spock

import com.intellij.testFramework.fixtures.LightPlatformCodeInsightFixture4TestCase
import org.jetbrains.plugins.groovy.lang.psi.GroovyPsiElementFactory
import org.junit.Test

class SpockLabelAnnotatorTest extends LightPlatformCodeInsightFixture4TestCase {

    private GroovyPsiElementFactory elementFactory

    void setUp() {
        super.setUp()
        elementFactory = GroovyPsiElementFactory.getInstance(project)
    }

    @Test
    void 'annotator should highlight Spock labels'() {
        myFixture.configureByText(
            "MySpec.groovy", '''
            class <info descr="null">Specification</info> {}

            class <info descr="null">MySpec</info> extends <info descr="null">Specification</info> {
                def 'should do something'() {
                    <info descr="null"><info descr="null">given</info>: 'a foo and bar'</info>
                    def <info descr="null">foo</info> = 1
                    def <info descr="null">bar</info> = 2

                    <info descr="null"><info descr="null">when</info>: 'foo + bar'</info>
                    def <info descr="null">baz</info> = <info descr="null">foo</info> + <info descr="null">bar</info>

                    <info descr="null"><info descr="null">then</info>: 'should be 3'</info>
                    <info descr="null">baz</info> == 3
                }
            }
        '''.stripIndent()
        )
        myFixture.testHighlighting(true, true, true)
    }

    @Test
    void 'annotator should highlight Spock labels without title'() {
        myFixture.configureByText(
            "MySpec.groovy", '''
            class <info descr="null">Specification</info> {}

            class <info descr="null">MySpec</info> extends <info descr="null">Specification</info> {
                def 'should do something'() {
                    <info descr="null"><info descr="null">given</info>:</info>
                    def <info descr="null">foo</info> = 1
                    def <info descr="null">bar</info> = 2

                    <info descr="null"><info descr="null">when</info>:</info>
                    def <info descr="null">baz</info> = <info descr="null">foo</info> + <info descr="null">bar</info>

                    <info descr="null"><info descr="null">then</info>:</info>
                    <info descr="null">baz</info> == 3
                }
            }
        '''.stripIndent()
        )
        myFixture.testHighlighting(true, true, true)
    }

    @Test
    void 'annotator should do nothing when element is not label-statement or spock-label'() {
        // given:
        myFixture.configureByText(
            "MySpec.groovy", '''
                class Foo extends Specification {
                    def bar() {
                        println 'not a label'

                        Given:
                        println 'not a Spock label'
                    }
                }
        '''.stripIndent()
        )

        // when:
        def highlights = myFixture.doHighlighting()

        // then:
        assertSize(4, highlights)
        assertEquals("Foo", highlights[0].text)
        assertEquals("Specification", highlights[1].text)
        assertEquals("bar", highlights[2].text)
        assertEquals("Given", highlights[3].text)
    }
}
