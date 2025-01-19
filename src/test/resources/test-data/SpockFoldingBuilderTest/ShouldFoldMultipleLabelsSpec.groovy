package spock.lang

class Specification {}

class SampleSpec extends Specification <fold text='{...}'>{

    def 'should fold titled given, when, then, cleanup'() <fold text='{...}'>{
         <fold text='given some given block'><fold text='given some given block'>given: 'some given block'
             def foo = 'foo'</fold>

         <fold text='and another given block'>and: 'another given block'
             def bar = 'bar'</fold>

         and:
             def baz = 'baz'</fold>

         <fold text='when some when block'><fold text='when some when block'>when: 'some when block'
             def qux = foo + bar</fold>

         <fold text='and another when block'>and: 'another when block'
             def quux = bar + baz</fold>

         and:
             def quux = baz + qux</fold>

         <fold text='then some then block'><fold text='then some then block'>then: 'some then block'
             baz == 'foobar'</fold>

         <fold text='and another then block'>and: 'another then block'
             baz == 'foobar'</fold>

         and:
             true // non-string literal!!!</fold>

         <fold text='cleanup some cleanup block'><fold text='cleanup some cleanup block'>cleanup: 'some cleanup block'
             println('cleanup')</fold>

         <fold text='and another cleanup block'>and: 'another cleanup block'
             println('cleanup')</fold>

         and:
             println('cleanup')</fold>
     }</fold>


   def 'should fold untitled given, when, then, cleanup'() <fold text='{...}'>{
        <fold text='given...'>given:
            def foo = 'foo'

        and:
            def bar = 'bar'

        and:
            def baz = 'baz'</fold>

        <fold text='when...'>when:
            def qux = foo + bar

        and:
            def quux = bar + baz

        and:
            def quux = baz + qux</fold>

        <fold text='then...'>then:
            baz == 'foobar'

        and:
            baz == 'foobar'

        and:
            true // non-string literal!!!</fold>

        <fold text='cleanup...'>cleanup:
            println('cleanup')

        and:
            println('cleanup')

        and:
            println('cleanup')</fold>
    }</fold>

}</fold>
