package net.kaleidos.hibernate.hstore

import grails.plugin.spock.*
import spock.lang.*

import spock.lang.Specification
import test.hstore.TestHstore

class PgHstoreContainsIntegrationSpec extends Specification {

    def pgHstoreTestSearchService

    void 'Test only one value result 2 different elements'() {
        setup:
            new TestHstore(name: "test1", testAttributes: ["a": "test", "b": "1"]).save(flush: true)
            new TestHstore(name: "test2", testAttributes: ["b": "2"]).save(flush: true)
            new TestHstore(name: "test3", testAttributes: ["a": "test"]).save(flush: true)
            new TestHstore(name: "test4", testAttributes: ["c": "test", "b": "1"]).save(flush: true)

        when:
            def result = pgHstoreTestSearchService.search('testAttributes', 'pgHstoreContains', map)

        then:
            result.size() == 2
            result.find { it.name == "test1" } != null
            result.find { it.name == "test2" } == null
            result.find { it.name == "test3" } == null
            result.find { it.name == "test4" } != null

        where:
            map = ["b": "1"]
    }

    void 'Test two values that matches partialy with one element'() {
        setup:
            new TestHstore(name: "test1", testAttributes: ["a": "test", "b": "1"]).save(flush: true)
            new TestHstore(name: "test2", testAttributes: ["b": "2"]).save(flush: true)
            new TestHstore(name: "test3", testAttributes: ["a": "test"]).save(flush: true)
            new TestHstore(name: "test4", testAttributes: ["c": "test", "b": "1"]).save(flush: true)

        when:
            def result = pgHstoreTestSearchService.search('testAttributes', 'pgHstoreContains', map)

        then:
            result.size() == 1
            result.find { it.name == "test1" } == null
            result.find { it.name == "test2" } == null
            result.find { it.name == "test3" } == null
            result.find { it.name == "test4" } != null

        where:
            map = ["b": "1", "c": "test"]
    }

    void 'No matches with the same combination key/value'() {
        setup:
            new TestHstore(name: "test1", testAttributes: ["a": "test", "b": "1"]).save(flush: true)
            new TestHstore(name: "test2", testAttributes: ["b": "2"]).save(flush: true)
            new TestHstore(name: "test3", testAttributes: ["a": "test"]).save(flush: true)
            new TestHstore(name: "test4", testAttributes: ["c": "test", "b": "1"]).save(flush: true)

        when:
            def result = pgHstoreTestSearchService.search('testAttributes', 'pgHstoreContains', map)

        then:
            result.size() == 0

        where:
            map = ["b": "3"]
    }

    void 'No matches with the same combination but one of the elements matches'() {
        setup:
            new TestHstore(name: "test1", testAttributes: ["a": "test", "b": "1"]).save(flush: true)
            new TestHstore(name: "test2", testAttributes: ["b": "2"]).save(flush: true)
            new TestHstore(name: "test3", testAttributes: ["a": "test"]).save(flush: true)
            new TestHstore(name: "test4", testAttributes: ["c": "test", "b": "1"]).save(flush: true)

        when:
            def result = pgHstoreTestSearchService.search('testAttributes', 'pgHstoreContains', map)

        then:
            result.size() == 0

        where:
            map = ["b": "1", "c": "test-otro"]
    }

    void 'When empty map returns all elements'() {
        setup:
            new TestHstore(name: "test1", testAttributes: ["a": "test", "b": "1"]).save(flush: true)
            new TestHstore(name: "test2", testAttributes: ["b": "2"]).save(flush: true)
            new TestHstore(name: "test3", testAttributes: ["a": "test"]).save(flush: true)
            new TestHstore(name: "test4", testAttributes: ["c": "test", "b": "1"]).save(flush: true)

        when:
            def result = pgHstoreTestSearchService.search('testAttributes', 'pgHstoreContains', map)

        then:
            result.size() == 4

        where:
            map = [:]
    }
}
