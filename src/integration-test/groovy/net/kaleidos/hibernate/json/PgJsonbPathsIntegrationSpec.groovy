package net.kaleidos.hibernate.json

import grails.gorm.transactions.Rollback
import grails.testing.mixin.integration.Integration
import org.springframework.beans.factory.annotation.Autowired
import spock.lang.Specification
import spock.lang.Unroll
import test.criteria.json.PgJsonbTestSearchService
import test.json.TestMapJsonb

@Integration
@Rollback
class PgJsonbPathsIntegrationSpec extends Specification {

    @Autowired PgJsonbTestSearchService pgJsonbTestSearchService

    @Unroll
    void 'Test equals finding nested values (jsonb). sqlOp: equals'() {
        setup:
        new TestMapJsonb(data: [name: 'Iván', lastName: 'López', nested: [a: 1, b: 2]]).save(flush: true)
        new TestMapJsonb(data: [name: 'Alonso', lastName: 'Torres', nested: [a: 2, b: 3]]).save(flush: true)
        new TestMapJsonb(data: [name: 'Iván', lastName: 'Pérez', nested: [a: 1, b: 5]]).save(flush: true)

        when:
        def result = pgJsonbTestSearchService.search('pgJson', 'data', '#>>', '{nested, a}', '=', value)

        then:
            result.size() == size
            result.every { it.data.nested.a == value }

        where:
            value || size
                1 || 2 // there are 2 items with nested.a equal to 1
                2 || 1
                3 || 0
    }

    @Unroll
    void 'Test equals finding nested values (jsonb). sqlOp: greater than'() {
        setup:
        new TestMapJsonb(data: [name: 'Iván', lastName: 'López', nested: [a: 1, b: 2]]).save(flush: true)
        new TestMapJsonb(data: [name: 'Alonso', lastName: 'Torres', nested: [a: 2, b: 3]]).save(flush: true)
        new TestMapJsonb(data: [name: 'Iván', lastName: 'Pérez', nested: [a: 1, b: 5]]).save(flush: true)

        when:
        def result = pgJsonbTestSearchService.search('pgJson', 'data', '#>>', '{nested, b}', '>', value)

        then:
            result.size() == size
            result.every { it.data.nested.b > value.toInteger() }

        where:
            value || size
                1 || 3 // There are 3 items with nested.b > 1
                3 || 1
                6 || 0
    }

}
