package net.kaleidos.hibernate.json

import spock.lang.Specification
import spock.lang.Unroll
import test.json.TestMapJsonb

class PostgresqlJsonbMapDomainIntegrationSpec extends Specification {

    @Unroll
    void 'save a domain class with a map #map to jsonb'() {
        setup:
            def testMapJsonb = new TestMapJsonb(data: map)

        when:
            testMapJsonb.save(flush: true)

        then:
            testMapJsonb.hasErrors() == false
            testMapJsonb.data == map

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