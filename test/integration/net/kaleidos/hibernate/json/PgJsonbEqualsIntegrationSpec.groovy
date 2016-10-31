package net.kaleidos.hibernate.json

import spock.lang.Specification
import spock.lang.Unroll
import test.json.TestMapJsonb

class PgJsonbEqualsIntegrationSpec extends Specification {

    def pgJsonbTestSearchService

    @Unroll
    void 'Test equals finding value: #value (jsonb)'() {
        setup:
            new TestMapJsonb(data: [name: 'Iván', lastName: 'López']).save(flush: true)
            new TestMapJsonb(data: [name: 'Alonso', lastName: 'Torres']).save(flush: true)
            new TestMapJsonb(data: [name: 'Iván', lastName: 'Pérez']).save(flush: true)

        when:
            def result = pgJsonbTestSearchService.search('pgJsonHasFieldValue', 'data', 'name', value)

        then:
            result.size() == size
            result.every { it.data.name == value }

        where:
            value  || size
            'Iván' || 2
            'John' || 0
    }
}
