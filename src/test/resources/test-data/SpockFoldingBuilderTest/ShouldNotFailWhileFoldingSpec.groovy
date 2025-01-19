package spock.lang

class Specification {}

class ShouldNotFailWhileFoldingSpec extends Specification <fold text='{...}'>{
    private static final String CONSTANT = 'constant'

    private final String field = 'field'

    static class Foo {}
    class Bar {}
    interface Baz {}
    trait Qux {}

    def 'should not fail when empty method'() {}

    def 'should not fail when empty given'() <fold text='{...}'>{
        given:
    }</fold>

    def 'should not fail when empty untitled given and titled continuation'() <fold text='{...}'>{
        given:

        and: 'some continuation block'
        def foo = 'foo'
    }</fold> // TODO: fix this! it should fold the block!


    def 'should not fail when empty titled given and titled continuation'() <fold text='{...}'>{
        <fold text='given some given block'><fold text='given some given block'>given: 'some given block'</fold>

        <fold text='and some continuation block'>and: 'some continuation block'
        def foo = 'foo'</fold></fold>
    }</fold>


    def 'should not fail when empty untitled given and untitled continuation'() <fold text='{...}'>{
        given:

        and:
        def foo = 'foo'
    }</fold> // TODO: fix this! it should fold the block!


    def 'should not fail when empty titled given and untitled continuation'() <fold text='{...}'>{
        <fold text='given some given block'><fold text='given some given block'>given: 'some given block'</fold>

        and:
        def foo = 'foo'</fold>
    }</fold>


    def 'should not fail when empty untitled given and titled when block'() <fold text='{...}'>{
        given:

        when: 'some when block'
        def foo = 'foo'
    }</fold> // TODO: fix this! it should fold the block!


    def 'should not fail when empty titled given and titled when block'() <fold text='{...}'>{
        <fold text='given some given block'>given: 'some given block'</fold>

        <fold text='when some when block'>when: 'some when block'
        def foo = 'foo'</fold>
    }</fold>


    def 'should not fail when empty untitled given and untitled when block'() <fold text='{...}'>{
        given:

        when: 'some when block'
        def foo = 'foo'
    }</fold> // TODO: fix this! it should fold the block!


    def 'should not fail when empty titled given and untitled when block'() <fold text='{...}'>{
        <fold text='given some given block'>given: 'some given block'</fold>

        when:
        def foo = 'foo'
    }</fold>


    def 'should not fail when unexpected titled continuation'() <fold text='{...}'>{
        <fold text='and a valid bar'>and: 'a valid bar'
        def bar = 'bar'</fold>
    }</fold>


    def 'should not fail when unexpected titled continuation with new line'() <fold text='{...}'>{

        <fold text='and a valid bar'>and: 'a valid bar'
        def bar = 'bar'</fold>
        
    }</fold>


    def 'should not fail when unexpected titled continuation with titled continuation'() <fold text='{...}'>{
        <fold text='and a valid bar'><fold text='and a valid bar'>and: 'a valid bar'
        def bar = 'bar'</fold>

        <fold text='and a valid baz'>and: 'a valid baz'
        def baz = 'baz'</fold></fold>
    }</fold>


    def 'should not fail when unexpected titled continuation with titled continuation with new line'() <fold text='{...}'>{

        <fold text='and a valid bar'><fold text='and a valid bar'>and: 'a valid bar'
        def bar = 'bar'</fold>


        <fold text='and a valid baz'>and: 'a valid baz'
        def baz = 'baz'</fold></fold>

    }</fold>


    def 'should not fail when unexpected titled continuation with mixed continuation'() <fold text='{...}'>{
        <fold text='and a valid bar'><fold text='and a valid bar'>and: 'a valid bar'
        def bar = 'bar'</fold>

        and:
        def baz = 'baz'

        <fold text='and a valid qux'>and: 'a valid qux'
        def qux = 'qux'</fold>

        and:
        def quux = 'quux'</fold>
    }</fold>


    def 'should not fail when unexpected titled continuation with mixed continuation with new line'() <fold text='{...}'>{

        <fold text='and a valid bar'><fold text='and a valid bar'>and: 'a valid bar'
        def bar = 'bar'</fold>


        and:
        def baz = 'baz'


        <fold text='and a valid qux'>and: 'a valid qux'
        def qux = 'qux'</fold>


        and:
        def quux = 'quux'</fold>

    }</fold>


    def 'should not fail when unexpected untitled continuation with untitled continuation'() <fold text='{...}'>{
        <fold text='and...'>and:
        def bar = 'bar'

        and:
        def baz = 'baz'</fold>
    }</fold>


    def 'should not fail when unexpected untitled continuation with untitled continuation with new line'() <fold text='{...}'>{

        <fold text='and...'>and:
        def bar = 'bar'


        and:
        def baz = 'baz'</fold>

    }</fold>


    def 'should not fail when unexpected untitled continuation with titled continuation'() <fold text='{...}'>{
        <fold text='and...'>and:
        def bar = 'bar'

        <fold text='and a valid baz'>and: 'a valid baz'
        def baz = 'baz'</fold></fold>
    }</fold>


    def 'should not fail when unexpected untitled continuation with titled continuation with new line'() <fold text='{...}'>{

        <fold text='and...'>and:
        def bar = 'bar'


        <fold text='and a valid baz'>and: 'a valid baz'
        def baz = 'baz'</fold></fold>

    }</fold>


    def 'should not fail when unexpected untitled continuation with mixed continuation'() <fold text='{...}'>{
        <fold text='and...'>and:
        def bar = 'bar'

        <fold text='and a valid baz'>and: 'a valid baz'
        def baz = 'baz'</fold>

        and:
        def qux = 'qux'

        <fold text='and a valid quux'>and: 'a valid quux'
        def quux = 'quux'</fold></fold>
    }</fold>


    def 'should not fail when unexpected untitled continuation with mixed continuation with new line'() <fold text='{...}'>{

        <fold text='and...'>and:
        def bar = 'bar'

        <fold text='and a valid baz'>and: 'a valid baz'
        def baz = 'baz'</fold>


        and:
        def qux = 'qux'


        <fold text='and a valid quux'>and: 'a valid quux'
        def quux = 'quux'</fold></fold>

    }</fold>

}</fold>

