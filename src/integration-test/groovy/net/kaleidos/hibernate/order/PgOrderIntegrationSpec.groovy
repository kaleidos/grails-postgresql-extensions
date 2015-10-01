package net.kaleidos.hibernate.order

import grails.test.mixin.integration.Integration
import grails.transaction.Rollback
import spock.lang.Specification
import test.json.TestMapJsonb

@Integration
@Rollback
class PgOrderIntegrationSpec extends Specification {

    def pgOrderService

    void 'Order by a json property'() {
        setup:
            new TestMapJsonb(data: [name: 'Iván', lastName: 'López']).save(flush: true)
            new TestMapJsonb(data: [name: 'Alonso', lastName: 'Torres']).save(flush: true)
            new TestMapJsonb(data: [name: 'Ernesto', lastName: 'Pérez']).save(flush: true)

        when:
            def result = pgOrderService.orderByJson()

        then:
            result != null
            result.data.name == ['Iván', 'Ernesto', 'Alonso']
    }

    void 'Order by random'() {
        setup:
            new TestMapJsonb(data: [name: 'Iván', lastName: 'López']).save(flush: true)
            new TestMapJsonb(data: [name: 'Alonso', lastName: 'Torres']).save(flush: true)
            new TestMapJsonb(data: [name: 'Ernesto', lastName: 'Pérez']).save(flush: true)

        when:
            def result = pgOrderService.orderByRandom()

        then:
            result != null
            result.size() == 3
    }
}