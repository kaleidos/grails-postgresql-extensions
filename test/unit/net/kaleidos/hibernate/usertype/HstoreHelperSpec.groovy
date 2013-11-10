package net.kaleidos.hibernate.usertype

import grails.plugin.spock.*
import spock.lang.*

public class HstoreHelperSpec extends Specification {

    void 'empty map to string'() {
        setup:
            def m = [:]

        expect:
            HstoreHelper.toString(m) == ""
    }

    void 'null map to string'() {
        setup:
            def m = null

        expect:
            HstoreHelper.toString(m) == ""
    }

    void 'non empty map to string'() {
        setup:
            def m = [:]
            m.foo = "bar"

        expect:
            HstoreHelper.toString(m) == '"foo"=>"bar"'
    }

    void 'transform map with doulbe quotes'() {
        setup:
            def m = [:]
            m['Test "thing"'] = "bar"

        expect:
            HstoreHelper.toString(m) == '"Test \'thing\'"=>"bar"'
    }

    void 'map with two values to string'() {
        setup:
            def m = [:]
            m.foo = "bar"
            m.xxx = "Groovy Rocks!"

        expect:
            HstoreHelper.toString(m) == '"foo"=>"bar", "xxx"=>"Groovy Rocks!"'
    }

    @Unroll
    void 'map with different non-string types values to string. value: #value'() {
        setup:
            def m = [:]
            m.prop = value

        expect:
            HstoreHelper.toString(m) == "\"prop\"=>\"${value}\""

        where:
            value << [123, true, null, 999L, new Date(), 87987.8976]
    }

    void 'map with key and value that contains a comma'() {
        setup:
            def m = [:]
            m["foo,bar"] = "baz,qux"

        expect:
            HstoreHelper.toString(m) == '"foo,bar"=>"baz,qux"'
    }

    void 'map with key and value that contains a comma and space'() {
        setup:
        def m = [:]
        m["foo, bar"] = "baz, qux"

        expect:
        HstoreHelper.toString(m) == '"foo, bar"=>"baz, qux"'
    }

    void 'empty string to map'() {
        when:
            def m = HstoreHelper.toMap("")

        then:
            m.isEmpty()
    }

    void 'null string to map'() {
        when:
            def m = HstoreHelper.toMap(null)

        then:
            m.isEmpty()
    }

    void 'test to map'() {
        when:
            def m = HstoreHelper.toMap('"foo"=>"bar"')

        then:
            m.size() == 1
            m.foo == "bar"
    }

    void 'test to map with two values'() {
        when:
            def m = HstoreHelper.toMap('"foo"=>"bar", "xxx"=>"Groovy Rocks!"')

        then:
            m.size() == 2
            m.foo == "bar"
            m.xxx == "Groovy Rocks!"
    }

    @Unroll
    void 'test to map with different non-string values. value: #value'() {
        when:
            def m = HstoreHelper.toMap("'prop'=>\"${value}\"")

        then:
            m.size() == 1
            m.prop == "${value}"
            m.prop.class == java.lang.String

        where:
            value << [123, true, null, 999L, new Date(), 87987.8976]
    }

    void 'test to map with key and value that contains comma'() {
        when:
            def m = HstoreHelper.toMap('"foo,bar"=>"baz,qux"')

        then:
            m.size() == 1
            m['foo,bar'] == "baz,qux"
    }
    void 'test to map with key and value that contains comma and space'() {
        when:
        def m = HstoreHelper.toMap('"foo, bar"=>"baz, qux"')

        then:
        m.size() == 1
        m['foo, bar'] == "baz, qux"
    }

    @Unroll
    void 'Test asStatement. map: #map'() {
        when:
            def result = HstoreHelper.asStatement(map)

        then:
            result == expected

        where:
            map << [null, [:], ["a":"b"], ["a": "b", "c": "d"], ["a":"b","c":"d","e":"f"], ["foo,bar":"baz,qux"]]
            expected << ["", "", '"?"=>"?"', '"?"=>"?", "?"=>"?"', '"?"=>"?", "?"=>"?", "?"=>"?"', '"?"=>"?"']
    }

    @Unroll
    void 'Test asListKeyValue. value: #map'() {
        when:
            def result = HstoreHelper.asListKeyValue(map)

        then:
            result == expected

        where:
            map << [null, [:], ["a":"b"], ["a": "b", "c": "d"], ["a":"b","c":"d","e":"f"], ["foo,bar":"baz,qux"]]
            expected << [[], [], ["a","b"], ["a","b","c","d"], ["a","b","c","d","e","f"], ["foo,bar","baz,qux"]]
    }
}
