package spock.lang

class TestClassWithNestedTests {

    static class StaticNestedTest <fold text='{...}'>{
        def 'should fold static nested test'() <fold text='{...}'>{
            <fold text='given some given block'>given: 'some given block'
                def foo = 1</fold>

            <fold text='when some when block'>when: 'some when block'
                def bar = 2</fold>

            <fold text='then some then block'>then: 'some then block'
                bar == 2</fold>
        }</fold>
    }</fold>

    class NonStaticNestedTest <fold text='{...}'>{
        def 'should fold static nested test'() <fold text='{...}'>{
            <fold text='given some given block'>given: 'some given block'
                def foo = 1</fold>

            <fold text='when some when block'>when: 'some when block'
                def bar = 2</fold>

            <fold text='then some then block'>then: 'some then block'
                bar == 2</fold>
        }</fold>
    }</fold>
}
