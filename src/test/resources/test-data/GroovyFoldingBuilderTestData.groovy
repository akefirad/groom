package foo.bar

import spock.lang.Specification

class SampleClass {
    def 'empty list/map should fold'() <fold text='{...}'>{
            def lst1 = []
            def lst2 = [ ]
            def lst3 = [
            ]

            def map1 = [:]
            def map2 = [ : ]
            def map3 = [
                :
            ]
    }</fold>

    def 'non-empty list/map should fold'() <fold text='{...}'>{
             def lst1 = [1, 2]
             def lst2 = <fold text='[...]'>[
                 1,
                 2,
             ]</fold>

             def map1 = [foo: 1, bar: 2]
             def map2 = <fold text='[...]'>[
                 foo: 1,
                 bar: 2,
             ]</fold>
     }</fold>

   def 'nexted list/map should not fold'() <fold text='{...}'>{
            def lst = <fold text='[...]'>[
                1,
                [2, 3],
            ]</fold>

            def map = <fold text='[...]'>[
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
}
