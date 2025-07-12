// TODO: move the test to foo.bar package and remove the Specification class definition
package spock.lang

class Specification {}

class SampleClass extends Specification <fold text='{...}'>{
    def 'empty list should fold'() <fold text='{...}'>{
            def lst1 = []
            def lst2 = [ ]
            def lst3 = [
            ]

            println([])
            println([ ])

            println([], [])
            println([ ], [ ])
    }</fold>

    def 'empty map should fold'() <fold text='{...}'>{
            def map1 = [:]
            def map2 = [ : ]
            def map3 = [
                :
            ]

            println([:])
            println([ : ])

            println([:], [:])
            println([ : ], [ : ])
    }</fold>

    def 'non-empty list should fold'() <fold text='{...}'>{
             def lst1 = [1, 2]
             def lst2 = <fold text='[...]'>[
                 1,
                 2,
             ]</fold>

            println([1, 2])
            println(<fold text='[...]'>[
                1,
                2,
            ]</fold>)

            println([1, 2], [3, 4])
            println(<fold text='[...]'>[
                1,
                2,
            ]</fold>, <fold text='[...]'>[
                1,
                2,
            ]</fold>)
     }</fold>

    def 'non-empty map should fold'() <fold text='{...}'>{
             def map1 = [foo: 1, bar: 2]
             def map2 = <fold text='[...:...]'>[
                 foo: 1,
                 bar: 2,
             ]</fold>

            println([foo: 1, bar: 2])
            println(<fold text='[...:...]'>[
                foo: 1,
                bar: 2,
            ]</fold>)

            println([foo: 1, bar: 2], [baz: 3, qux: 4])
            println(<fold text='[...:...]'>[
                foo: 1,
                bar: 2,
            ]</fold>, <fold text='[...:...]'>[
                foo: 1,
                bar: 2,
            ]</fold>)
     }</fold>

   def 'nexted list/map should not fold'() <fold text='{...}'>{
            def lst = <fold text='[...]'>[
                1,
                [2, 3],
            ]</fold>

            def map = <fold text='[...:...]'>[
                foo: 1,
                bar: [baz: 2],
            ]</fold>
    }</fold>

    def 'open list/map should not fold'() <fold text='{...}'>{
            def lst = [
                1,

            def map = [
                foo: 1,
    }</fold>

    def 'constructor calls should fold'() <fold text='{...}'>{
        def person1 = new Person("John", 30)
        def person2 = new Person<fold text='(...)'>(
            "Very Long Name That Exceeds Line Limits",
            42,
            "Very long address string"
        )</fold>

        def config = new Configuration<fold text='(...)'>(
            [key1: "value1", key2: "value2"],
            ["item1", "item2", "item3"]
        )</fold>

        def complex = new OuterClass<fold text='(...)'>(
            new InnerClass(param1, param2),
            new AnotherClass(param3, param4)
        )</fold>
    }</fold>

    def 'constructor calls should not fold when single line'() <fold text='{...}'>{
        def simple = new SimpleClass()
        def withParams = new ClassWithParams(param1, param2, param3)
    }</fold>

    def 'constructor calls should not fold when incomplete'() <fold text='{...}'>{
        def incomplete = new IncompleteClass(
            param1,
            param2
    }</fold>

    def 'constructor calls should not fold when no parameters'() <fold text='{...}'>{
        def empty1 = new EmptyClass(
        )
        def empty2 = new AnotherEmptyClass(

        )
        def empty3 = new YetAnotherClass(
            // comments but no params
        )
    }</fold>

    def 'constructor calls with nested collections should fold both constructor params and collections'() <fold text='{...}'>{
        def withList = new ClassWithList<fold text='(...)'>(
            "param1",
            <fold text='[...]'>[
                "item1",
                "item2",
                "item3"
            ]</fold>,
            "param3"
        )</fold>

        def withMap = new ClassWithMap<fold text='(...)'>(
            "param1",
            <fold text='[...:...]'>[
                key1: "value1",
                key2: "value2",
                key3: "value3"
            ]</fold>,
            "param3"
        )</fold>

        def withBoth = new ClassWithBoth<fold text='(...)'>(
            <fold text='[...]'>[
                "item1",
                "item2"
            ]</fold>,
            <fold text='[...:...]'>[
                config: "value",
                settings: "another"
            ]</fold>
        )</fold>

        def complexNested = new ComplexClass<fold text='(...)'>(
            new NestedClass("param"),
            <fold text='[...]'>[
                new InnerObject("data1"),
                new InnerObject("data2")
            ]</fold>,
            <fold text='[...:...]'>[
                settings: <fold text='[...]'>[
                    "option1",
                    "option2"
                ]</fold>,
                config: <fold text='[...:...]'>[
                    nested: "value"
                ]</fold>
            ]</fold>
        )</fold>
    }</fold>

    // TODO: add more cases!
}</fold>
