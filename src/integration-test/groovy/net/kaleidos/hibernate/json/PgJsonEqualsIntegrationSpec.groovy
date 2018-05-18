package net.kaleidos.hibernate.json

import grails.gorm.transactions.Rollback
import grails.testing.mixin.integration.Integration
import org.springframework.beans.factory.annotation.Autowired
import spock.lang.Specification
import spock.lang.Unroll
import test.criteria.json.PgJsonTestSearchService
import test.json.TestMapJson

@Integration
@Rollback
class PgJsonEqualsIntegrationSpec extends Specification {

    @Autowired PgJsonTestSearchService pgJsonTestSearchService

    def setup() {
        TestMapJson.executeUpdate('delete from TestMapJson')
    }

    @Unroll
    void 'Test equals finding value: #value (json)'() {
        setup:
            new TestMapJson(data: [name: 'Iván', lastName: 'López']).save(flush: true, failOnError: true)
            new TestMapJson(data: [name: 'Alonso', lastName: 'Torres']).save(flush: true, failOnError: true)
            new TestMapJson(data: [name: 'Iván', lastName: 'Pérez']).save(flush: true, failOnError: true)

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
