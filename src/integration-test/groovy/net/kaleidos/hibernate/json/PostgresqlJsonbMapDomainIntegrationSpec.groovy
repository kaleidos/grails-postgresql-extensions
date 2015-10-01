package net.kaleidos.hibernate.json

import grails.test.mixin.integration.Integration
import org.springframework.transaction.annotation.Transactional
import spock.lang.Specification
import spock.lang.Unroll
import test.json.TestMapJsonb

@Integration
@Transactional
class PostgresqlJsonbMapDomainIntegrationSpec extends Specification {

    @Unroll
    void 'save and read a domain class with a map #map to jsonb'() {
        setup:
            def testMapJsonb = new TestMapJsonb(data: map)

        when:
            // Domain saving and retrieving should be in different sessions. Only in that case Hibernate will invoke
            // nullSafeGet on the corresponding user type and will not use current session's cache.
            TestMapJsonb.withNewSession {
                testMapJsonb.save(flush: true)
            }

        then:
            testMapJsonb.hasErrors() == false

        and:
            def obj = testMapJsonb.get(testMapJsonb.id)
            obj.data == map

        where:
            map << [null, [:], [name: 'Ivan', age: 34]]
    }

    void 'save and read a domain class with jsonb'() {
        setup:
            def value = [name: 'Ivan', age: 34, hasChilds: true, childs: [[name: 'Judith', age: 7], [name: 'Adriana', age: 4]]]
            def testMapJsonb = new TestMapJsonb(data: value)

        when:
            testMapJsonb.save(flush: true)

        then:
            testMapJsonb.hasErrors() == false

        and:
            def obj = testMapJsonb.get(testMapJsonb.id)
            obj.data.keySet().collect { it.toString() }.equals(['name', 'age', 'hasChilds', 'childs'])
            obj.data.childs.size() == 2
    }
}