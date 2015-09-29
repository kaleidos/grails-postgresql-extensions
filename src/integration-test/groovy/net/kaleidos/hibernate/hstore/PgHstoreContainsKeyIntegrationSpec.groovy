package net.kaleidos.hibernate.hstore

import grails.test.mixin.integration.Integration
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.transaction.annotation.Transactional
import spock.lang.Specification
import test.criteria.hstore.PgHstoreTestSearchService
import test.hstore.TestHstore

@Integration
@Transactional
class PgHstoreContainsKeyIntegrationSpec extends Specification {
    @Autowired
    PgHstoreTestSearchService pgHstoreTestSearchService

    void 'Test find hstore that contains key'() {
        setup:
            new TestHstore(name: "test1", testAttributes: ["a": "test", "b": "1"]).save(flush: true)
            new TestHstore(name: "test2", testAttributes: ["b": "2"]).save(flush: true)
            new TestHstore(name: "test3", testAttributes: ["a": "test"]).save(flush: true)
            new TestHstore(name: "test4", testAttributes: ["c": "test", "b": "3"]).save(flush: true)

        when:
            def result = pgHstoreTestSearchService.search('testAttributes', 'pgHstoreContainsKey', 'b')

        then:
            result.size() == 3
            result.find { it.name == "test1" } != null
            result.find { it.name == "test2" } != null
            result.find { it.name == "test3" } == null
            result.find { it.name == "test4" } != null
    }

    void 'Test find hstore that contains other key'() {
        setup:
            new TestHstore(name: "test1", testAttributes: ["a": "test", "b": "1"]).save(flush: true)
            new TestHstore(name: "test2", testAttributes: ["b": "2"]).save(flush: true)
            new TestHstore(name: "test3", testAttributes: ["a": "test"]).save(flush: true)
            new TestHstore(name: "test4", testAttributes: ["c": "test", "b": "3"]).save(flush: true)

        when:
            def result = pgHstoreTestSearchService.search('testAttributes', 'pgHstoreContainsKey', 'X')

        then:
            result.size() == 0
    }
}
