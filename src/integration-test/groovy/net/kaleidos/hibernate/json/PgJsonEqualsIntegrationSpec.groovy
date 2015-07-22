package net.kaleidos.hibernate.json

import grails.test.mixin.integration.Integration
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.transaction.annotation.Transactional
import spock.lang.Specification
import spock.lang.Unroll
import test.criteria.json.PgJsonTestSearchService
import test.json.TestMapJson

@Integration
@Transactional
class PgJsonEqualsIntegrationSpec extends Specification {

    @Autowired
    PgJsonTestSearchService pgJsonTestSearchService

    @Unroll
    void 'Test equals finding value: #value'() {
        setup:
            new TestMapJson(data: [name: 'Iván', lastName: 'López']).save(flush: true)
            new TestMapJson(data: [name: 'Alonso', lastName: 'Torres']).save(flush: true)
            new TestMapJson(data: [name: 'Iván', lastName: 'Pérez']).save(flush: true)

        when:
            def result = pgJsonTestSearchService.search('pgJsonHasFieldValue', 'data', 'name', value)

        then:
            result.size() == size
            result.every { it.data.name == value }

        where:
            value  || size
            'Iván' || 2
            'John' || 0
    }
}