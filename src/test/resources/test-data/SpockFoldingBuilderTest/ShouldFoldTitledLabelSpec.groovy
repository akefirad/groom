package spock.lang

class Specification {}

class ShouldFoldTitledLabelSpec extends Specification <fold text='{...}'>{

    def 'should fold titled given label'() <fold text='{...}'>{
        <fold text='given a valid foo'>given: 'a valid foo'
        def foo = 'foo'</fold>
    }</fold>


    def 'should fold titled given label with new line'() <fold text='{...}'>{

        <fold text='given a valid foo'>given: 'a valid foo'
        def foo = 'foo'</fold>

    }</fold>


    def 'should fold titled given label with continuation'() <fold text='{...}'>{
        <fold text='given a valid foo'><fold text='given a valid foo'>given: 'a valid foo'
        def foo = 'foo'</fold>

        <fold text='and a valid bar'>and: 'a valid bar'
        def bar = 'bar'</fold></fold>
    }</fold>


    def 'should fold titled given label with continuation and new line'() <fold text='{...}'>{

        <fold text='given a valid foo'><fold text='given a valid foo'>given: 'a valid foo'
        def foo = 'foo'</fold>


        <fold text='and a valid bar'>and: 'a valid bar'
        def bar = 'bar'</fold></fold>

    }</fold>


   def 'should fold titled given label with more continuation'() <fold text='{...}'>{
        <fold text='given a valid foo'><fold text='given a valid foo'>given: 'a valid foo'
        def foo = 'foo'</fold>

        <fold text='and a valid bar'>and: 'a valid bar'
        def bar = 'bar'</fold>

        <fold text='and a valid baz'>and: 'a valid baz'
        def baz = 'baz'</fold></fold>
    }</fold>


    def 'should fold titled given label with untitled continuation'() <fold text='{...}'>{
        <fold text='given a valid foo'><fold text='given a valid foo'>given: 'a valid foo'
        def foo = 'foo'</fold>

        and:
        def bar = 'bar'</fold>
    }</fold>


    def 'should not fold untitled given label with more titled continuation'() <fold text='{...}'>{
        <fold text='given a valid foo'><fold text='given a valid foo'>given: 'a valid foo'
        def foo = 'foo'</fold>

        and:
        def bar = 'bar'

        and:
        def baz = 'baz'</fold>
    }</fold>


    def 'should not fold untitled given label with more titled continuation and new line'() <fold text='{...}'>{

        <fold text='given a valid foo'><fold text='given a valid foo'>given: 'a valid foo'
        def foo = 'foo'</fold>


        and:
        def bar = 'bar'


        and:
        def baz = 'baz'</fold>

    }</fold>

}</fold>
