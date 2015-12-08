package net.kaleidos.hibernate.json

import grails.test.mixin.integration.Integration
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.transaction.annotation.Transactional
import spock.lang.Specification
import test.criteria.json.PgJsonbTestSearchService
import test.json.TestMapJsonb

@Integration
@Transactional
class PgJsonbIsContainedIntegrationSpec extends Specification {

    @Autowired
    PgJsonbTestSearchService pgJsonbTestSearchService

    void 'Test some values'() {
        given:
            def obj1 = new TestMapJsonb(data: [a: 'foo', b: '1']).save(flush: true)
            new TestMapJsonb(data: [b: 1, d: '2']).save(flush: true)
            new TestMapJsonb(data: [a: 'test']).save(flush: true)
            def obj4 = new TestMapJsonb(data: [b: '1', a: 'foo', c: 'test',]).save(flush: true)

        when:
            def result = pgJsonbTestSearchService.search('pgJsonbIsContained', 'data', map)

        then:
            result.size() == 2
            result.contains(obj1)
            result.contains(obj4)

        where:
            map = [a: 'foo', b: '1', c: 'test']
    }

    void 'No matches with the same combination key/value'() {
        given:
            new TestMapJsonb(data: [a: 'foo', b: '1']).save(flush: true)

        when:
            def result = pgJsonbTestSearchService.search('pgJsonbIsContained', 'data', map)

        then:
            result.size() == 0

        where:
            map = [a: 'foo']
    }

    void 'Only empty matched with empty map'() {
        given:
            new TestMapJsonb(data: [a: 'foo', b: '1']).save(flush: true)
            new TestMapJsonb(data: [b: '2']).save(flush: true)
            new TestMapJsonb(data: [a: 'test']).save(flush: true)
            def obj4 = new TestMapJsonb(data: [:]).save(flush: true)

        when:
            def result = pgJsonbTestSearchService.search('pgJsonbIsContained', 'data', map)

        then:
            result.size() == 1
            result.contains(obj4)

        where:
            map = [:]
    }

    void 'No data matches with null'() {
        given:
            new TestMapJsonb(data: [a: 'foo', b: '1']).save(flush: true)
            new TestMapJsonb(data: [b: '2']).save(flush: true)
            new TestMapJsonb(data: [a: 'test']).save(flush: true)
            new TestMapJsonb(data: [:]).save(flush: true)

        when:
            def result = pgJsonbTestSearchService.search('pgJsonbIsContained', 'data', map)

        then:
            result.size() == 0

        where:
            map = null
    }

}
