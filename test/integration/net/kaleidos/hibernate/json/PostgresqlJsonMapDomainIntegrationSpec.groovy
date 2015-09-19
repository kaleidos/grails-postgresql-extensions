package net.kaleidos.hibernate.json

import spock.lang.Specification
import spock.lang.Unroll
import test.json.TestMapJson

class PostgresqlJsonMapDomainIntegrationSpec extends Specification {

    @Unroll
    void 'save and read a domain class with a map #map to json'() {
        setup:
            def testMapJson = new TestMapJson(data: map)

        when:
            // Domain saving and retrieving should be in different sessions. Only in that case Hibernate will invoke
            // nullSafeGet on the corresponding user type and will not use current session's cache.
            TestMapJson.withNewSession {
                testMapJson.save(flush: true)
            }

        then:
            testMapJson.hasErrors() == false

        and:
            def obj = testMapJson.get(testMapJson.id)
            obj.data == map

        where:
            map << [null, [:], [name: 'Ivan', age: 34]]
    }

    void 'save and read a domain class with json'() {
        setup:
            def value = [name: 'Ivan', age: 34, hasChilds: true, childs: [[name: 'Judith', age: 7], [name: 'Adriana', age: 4]]]
            def testMapJson = new TestMapJson(data: value)

        when:
            testMapJson.save(flush: true)

        then:
            testMapJson.hasErrors() == false

        and:
            def obj = testMapJson.get(testMapJson.id)
            obj.data.keySet().collect { it.toString() }.equals(['name', 'age', 'hasChilds', 'childs'])
            obj.data.childs.size() == 2
    }
}