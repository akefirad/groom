// TODO: move the test to foo.bar package and remove the Specification class definition
package spock.lang

class Specification {}

class SampleSpec extends Specification <fold text='{...}'>{
    def 'test empty given without title'() <fold text='{...}'>{
        <fold text='given: ...'>given:
    </fold>}</fold> // TODO: this should not be folded!

    def 'test empty given and when, without title'() <fold text='{...}'>{
        given:

        <fold text='when: ...'>when:
    </fold>}</fold> // TODO: this should not be folded!

    def 'test empty given without title, when'() <fold text='{...}'>{
        given:

        <fold text='when: ...'>when:
        def foo = 'foo'</fold>
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

    def 'test given, when, then without title'() <fold text='{...}'>{
        <fold text='given: ...'>given:
            def foo = 'foo'</fold>

        <fold text='and: ...'>and:
            def bar = 'bar'</fold>

        <fold text='when: ...'>when:
            def baz = foo + bar</fold>

        <fold text='then: ...'>then:
            baz == 'foobar'</fold>
    }</fold>

    // TODO: add more cases!
}</fold>
