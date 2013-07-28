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
            HstoreHelper.toString(m) == "foo=>bar"
    }

    void 'map with two values to string'() {
        setup:
            def m = [:]
            m.foo = "bar"
            m.xxx = "yyy"

        expect:
            HstoreHelper.toString(m) == "foo=>bar, xxx=>yyy"
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
            def m = HstoreHelper.toMap("\"foo\" => \"bar\"")

        then:
            m.size() == 1
            m.foo == "bar"
    }

    void 'test to map with two values'() {
        when:
            def m = HstoreHelper.toMap("\"foo\" => \"bar\", \"xxx\" => \"yyy\"")

        then:
            m.size() == 2
            m.foo == "bar"
            m.xxx == "yyy"
    }
}