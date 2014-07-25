package net.kaleidos.hibernate.json

import spock.lang.Specification
import spock.lang.Unroll
import test.json.TestMapJson

class PgJsonEqualsIntegrationSpec extends Specification {

    def pgJsonTestSearchService

    @Unroll
    void 'Test equals finding value: #value'() {
        setup:
            new TestMapJson(data: [name: 'Iván', lastName: 'López']).save(flush: true)
            new TestMapJson(data: [name: 'Alonso', lastName: 'Torres']).save(flush: true)
            new TestMapJson(data: [name: 'Iván', lastName: 'Pérez']).save(flush: true)

        when:
            def result = pgJsonTestSearchService.search('pgJsonEquals', 'data', 'name', value)

        then:
            result.size() == size
            result.every { it.data.name == value }

        where:
            value  || size
            'Iván' || 2
            'John' || 0
    }
}