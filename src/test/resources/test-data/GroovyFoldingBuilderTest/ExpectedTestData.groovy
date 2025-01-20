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

    // TODO: add more cases!
}</fold>
