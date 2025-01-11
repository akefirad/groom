package foo.bar

import spock.lang.Specification

class SampleSpec: Specification <fold text='{...}'>{
    def 'test only given without title'() <fold text='{...}'>{
        <fold text='given: ...'>given:
            def foo = 'foo'
            def bar = 'bar'</fold>
    }</fold>

        def 'test only given'() <fold text='{...}'>{
            <fold text='given: "some given block"'>given: "some given block"
                def foo = 'foo'
                def bar = 'bar'</fold>
        }</fold>

    def 'test given, when, then'() <fold text='{...}'>{
        <fold text='given: "some given block"'>given: "some given block"
            def foo = 'foo'</fold>

        <fold text='and: "another given block"'>and: "another given block"
            def bar = 'bar'</fold>

        <fold text='when: "some when block"'>when: "some when block"
            def baz = foo + bar</fold>

        <fold text='then: "some then block"'>then: "some then block"
            baz == 'foobar'</fold>
    }</fold>

    // TODO: add more cases!
}</fold>
