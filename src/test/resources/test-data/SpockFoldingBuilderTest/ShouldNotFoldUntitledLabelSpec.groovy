package spock.lang

class Specification {}

class ShouldNotFoldUntitledLabelSpec extends Specification <fold text='{...}'>{

    def 'should not fold untitled given label'() <fold text='{...}'>{
        given:
        def foo = 'foo'
    }</fold>


    def 'should not fold untitled given label with new line'() <fold text='{...}'>{
        given:
        def foo = 'foo'

    }</fold>


    def 'should not fold untitled given label with continuation'() <fold text='{...}'>{
        <fold text='given...'>given:
        def foo = 'foo'

        and:
        def bar = 'bar'</fold>
    }</fold>


    def 'should not fold untitled given label with continuation and new line'() <fold text='{...}'>{
        <fold text='given...'>given:
        def foo = 'foo'

        and:
        def bar = 'bar'</fold>

    }</fold>


    def 'should not fold untitled given label with more continuation'() <fold text='{...}'>{
        <fold text='given...'>given:
        def foo = 'foo'

        and:
        def bar = 'bar'

        and:
        def baz = 'baz'</fold>
    }</fold>


    def 'should not fold untitled given label with titled continuation'() <fold text='{...}'>{
        <fold text='given...'>given:
        def foo = 'foo'

        <fold text='and bar'>and: 'bar'
        def bar = 'bar'</fold></fold>
    }</fold>


    def 'should not fold untitled given label with titled continuation'() <fold text='{...}'>{
        <fold text='given...'>given:
        def foo = 'foo'

        <fold text='and bar'>and: 'bar'
        def bar = 'bar'</fold>

        <fold text='and baz'>and: 'baz'
        def baz = 'baz'</fold></fold>
    }</fold>

}</fold>

