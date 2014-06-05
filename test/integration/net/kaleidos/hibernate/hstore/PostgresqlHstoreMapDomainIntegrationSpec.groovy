package net.kaleidos.hibernate.hstore

import grails.plugin.spock.*
import spock.lang.*

import spock.lang.Specification
import spock.lang.Unroll
import test.hstore.TestHstoreMap

class PostgresqlHstoreMapDomainIntegrationSpec extends Specification {

    @Unroll
    void 'save a domain class with a map. key: #data'() {
        setup:
            def testHstoreMap = new TestHstoreMap(testAttributes: data)

        when:
            testHstoreMap.save()

        then:
            testHstoreMap.hasErrors() == false
            testHstoreMap.testAttributes != null
            testHstoreMap.testAttributes.size() == data.size()
            testHstoreMap.testAttributes[attribute] == value

        where:
            data                   | attribute | value
            [foo: "bar"]           | "foo"     | "bar"
            ["foo,bar": "baz,qux"] | "foo,bar" | "baz,qux"
    }

    @Unroll
    void 'recover a domain class with a map. key: #data'() {
        setup:
            new TestHstoreMap(testAttributes: data).save()

        when:
            def testHstoreMap = TestHstoreMap.findAll().first()

        then:
            testHstoreMap.hasErrors() == false
            testHstoreMap.testAttributes != null
            testHstoreMap.testAttributes.size() == data.size()
            testHstoreMap.testAttributes[key] == value

        where:
            data                     | key       | value
            [foo: "bar", xxx: "abc"] | "foo"     | "bar"
            ["foo,bar": "baz,qux"]   | "foo,bar" | "baz,qux"
    }

    @Unroll
    void 'remove a key in a map. key: #data, valueToRemove: #valueToRemove'() {
        setup:
            def testHstoreMap = new TestHstoreMap(testAttributes: data)
            testHstoreMap.save()

        when:
            testHstoreMap.testAttributes.remove(valueToRemove)

        then:
            testHstoreMap.hasErrors() == false
            testHstoreMap.testAttributes != null
            testHstoreMap.testAttributes.size() == size

        where:
            data                     | valueToRemove | size
            [foo: "bar", xxx: "abc"] | 'xxx'         | 1
            ["foo,bar": "baz,qux"]   | 'foo,bar'     | 0
            [foo: "bar"]             | 'xxx'         | 1
    }

    @Unroll
    void 'save and delete a domain class with a map. key: #data'() {
        setup:
            def testHstoreMap = new TestHstoreMap(testAttributes: data)

        when: 'I save an instance'
            testHstoreMap.save()

        and: 'The instance is saved'
            assert testHstoreMap.hasErrors() == false

        and: 'I try to delete it'
            testHstoreMap.delete()

        then: 'It shouldn\'t be present in database anymore'
            TestHstoreMap.count() == 0

        where:
            data                   | attribute | value
            [foo: "bar"]           | "foo"     | "bar"
            ["foo,bar": "baz,qux"] | "foo,bar" | "baz,qux"
    }
}
