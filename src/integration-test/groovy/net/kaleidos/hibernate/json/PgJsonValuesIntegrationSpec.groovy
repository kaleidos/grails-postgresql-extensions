package net.kaleidos.hibernate.json

import grails.gorm.transactions.Rollback
import grails.testing.mixin.integration.Integration
import spock.lang.Specification
import spock.lang.Unroll
import test.json.TestMapJson

@Integration
@Rollback
class PgJsonValuesIntegrationSpec extends Specification {

    def pgJsonTestSearchService

    @Unroll
    void 'Test equals finding value: #value with condition is ilike (json)'() {
        setup:
            new TestMapJson(data: [name: 'Iván', lastName: 'López']).save(flush: true)
            new TestMapJson(data: [name: 'Alonso', lastName: 'Torres']).save(flush: true)
            new TestMapJson(data: [name: 'Iván', lastName: 'Pérez']).save(flush: true)

        when:
        def result = pgJsonTestSearchService.search('pgJson', 'data', '->>', 'name', 'ilike', value)

        then:
            result.size() == size
            result.every { it.data.name.matches "^(?i)${value.replace('%', '.*')}\$" }

        where:
            value  || size
            '%iv%' || 2
            'John' || 0
    }

    @Unroll
    void 'Test equals finding value: #value with condition equals (json)'() {
        setup:
            new TestMapJson(data: [name: 'Iván', lastName: 'López']).save(flush: true)
            new TestMapJson(data: [name: 'Alonso', lastName: 'Torres']).save(flush: true)
            new TestMapJson(data: [name: 'Iván', lastName: 'Pérez']).save(flush: true)

        when:
        def result = pgJsonTestSearchService.search('pgJson', 'data', '->>', 'name', '=', value)

        then:
            result.size() == size
            result.every { it.data.name == value }

        where:
            value  || size
            'Iván' || 2
            'John' || 0
    }

    @Unroll
    void 'Test equals finding value: #value with condition does not equal (json)'() {
        setup:
            new TestMapJson(data: [name: 'Iván', lastName: 'López']).save(flush: true)
            new TestMapJson(data: [name: 'Alonso', lastName: 'Torres']).save(flush: true)
            new TestMapJson(data: [name: 'Iván', lastName: 'Pérez']).save(flush: true)

        when:
        def result = pgJsonTestSearchService.search('pgJson', 'data', '->>', 'name', '<>', value)

        then:
            result.size() == size
            result.every { it.data.name != value }

        where:
            value  || size
            'Iván' || 1
            'John' || 3
    }

}
