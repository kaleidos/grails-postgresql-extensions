package net.kaleidos.hibernate.hstore

import grails.test.mixin.integration.Integration
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.transaction.annotation.Transactional
import spock.lang.Specification
import test.criteria.hstore.PgHstoreTestSearchService
import test.hstore.TestHstoreMap

@Integration
@Transactional
class PgHstoreIsContainedIntegrationSpec extends Specification {
    @Autowired
    PgHstoreTestSearchService pgHstoreTestSearchService

    void 'No element matches with the empty set'() {
        setup:
            new TestHstoreMap(name: "test1", testAttributes: ["a": "test", "b": "1"]).save(flush: true)
            new TestHstoreMap(name: "test2", testAttributes: ["d": "10"]).save(flush: true)
            new TestHstoreMap(name: "test3", testAttributes: ["a": "test"]).save(flush: true)
            new TestHstoreMap(name: "test4", testAttributes: ["c": "test", "b": "1"]).save(flush: true)

        when:
            def result = pgHstoreTestSearchService.search('testAttributes', 'pgHstoreIsContained', map)

        then:
            result.size() == 0

        where:
            map = [:]
    }

    void 'All elements matches'() {
        setup:
            new TestHstoreMap(name: "test1", testAttributes: ["a": "test", "b": "1"]).save(flush: true)
            new TestHstoreMap(name: "test2", testAttributes: ["d": "10"]).save(flush: true)
            new TestHstoreMap(name: "test3", testAttributes: ["a": "test"]).save(flush: true)
            new TestHstoreMap(name: "test4", testAttributes: ["c": "test", "b": "1"]).save(flush: true)

        when:
            def result = pgHstoreTestSearchService.search('testAttributes', 'pgHstoreIsContained', map)

        then:
            result.size() == 4
            result.find { it.name == "test1" } != null
            result.find { it.name == "test2" } != null
            result.find { it.name == "test3" } != null
            result.find { it.name == "test4" } != null

        where:
            map = ["a": "test", "b": "1", "c": "test", "d": "10"]
    }

    void 'Some elements matches'() {
        setup:
            new TestHstoreMap(name: "test1", testAttributes: ["a": "test", "b": "1"]).save(flush: true)
            new TestHstoreMap(name: "test2", testAttributes: ["d": "10"]).save(flush: true)
            new TestHstoreMap(name: "test3", testAttributes: ["a": "test"]).save(flush: true)
            new TestHstoreMap(name: "test4", testAttributes: ["c": "test", "b": "1"]).save(flush: true)

        when:
            def result = pgHstoreTestSearchService.search('testAttributes', 'pgHstoreIsContained', map)

        then:
            result.size() == 3
            result.find { it.name == "test1" } != null
            result.find { it.name == "test2" } == null
            result.find { it.name == "test3" } != null
            result.find { it.name == "test4" } != null

        where:
            map = ["a": "test", "b": "1", "c": "test"]
    }
}
