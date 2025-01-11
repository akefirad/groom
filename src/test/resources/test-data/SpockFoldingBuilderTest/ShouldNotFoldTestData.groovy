package foo.bar

class Specification {}

class SampleSpec extends Specification <fold text='{...}'>{
    def 'test given, when, then'() <fold text='{...}'>{
        given: "some given block"
            def foo = 'foo'

        and: "another given block"
            def bar = 'bar'

        when: "some when block"
            def baz = foo + bar

        then: "some then block"
            baz == 'foobar'
    }</fold>
}</fold>
