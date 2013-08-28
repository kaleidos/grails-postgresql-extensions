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
            HstoreHelper.toString(m) == 'foo=>"bar"'
    }

    void 'map with two values to string'() {
        setup:
            def m = [:]
            m.foo = "bar"
            m.xxx = "Groovy Rocks!"

        expect:
            HstoreHelper.toString(m) == 'foo=>"bar", xxx=>"Groovy Rocks!"'
    }

    @Unroll
    void 'map with different non-string types values to string'() {
        setup:
            def m = [:]
            m.prop = value

        expect:
            HstoreHelper.toString(m) == "prop=>\"${value}\""

        where:
            value << [123, true, null, 999L, new Date(), 87987.8976]
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
            def m = HstoreHelper.toMap('"foo" => "bar"')

        then:
            m.size() == 1
            m.foo == "bar"
    }

    void 'test to map with two values'() {
        when:
            def m = HstoreHelper.toMap('"foo" => "bar", "xxx" => "Groovy Rocks!"')

        then:
            m.size() == 2
            m.foo == "bar"
            m.xxx == "Groovy Rocks!"
    }
    
    @Unroll
    void 'test to map with different non-string values'() {
        when:
            def m = HstoreHelper.toMap("'prop' => \"${value}\"")

        then:
            m.size() == 1
            m.prop == "${value}"
            m.prop.class == java.lang.String

        where:
            value << [123, true, null, 999L, new Date(), 87987.8976]
    }
}