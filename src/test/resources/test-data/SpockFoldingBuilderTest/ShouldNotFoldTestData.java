package spock.lang;

import spock.lang.Specification;

class SampleSpec extends Specification {
    void test_method() {
        given:
        var foo = 'foo'

        and:
        var bar = 'bar'

        when:
        var baz = foo + bar

        then:
        baz == 'foobar'
    }
}
