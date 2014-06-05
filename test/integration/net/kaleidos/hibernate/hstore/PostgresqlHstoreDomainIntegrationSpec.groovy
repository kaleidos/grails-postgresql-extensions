package net.kaleidos.hibernate.hstore

import grails.plugin.spock.*
import spock.lang.*

import spock.lang.Specification
import spock.lang.Unroll
import test.hstore.TestHstore

class PostgresqlHstoreDomainIntegrationSpec extends Specification {

    @Unroll
    void 'save a domain class with a map. key: #data'() {
        setup:
            def testHstore = new TestHstore(testAttributes: data)

        when:
            testHstore.save()

        then:
            testHstore.hasErrors() == false
            testHstore.testAttributes != null
            testHstore.testAttributes.size() == data.size()
            testHstore.testAttributes[attribute] == value

        where:
            data                   | attribute | value
            [foo: "bar"]           | "foo"     | "bar"
            ["foo,bar": "baz,qux"] | "foo,bar" | "baz,qux"
    }

    @Unroll
    void 'recover a domain class with a map. key: #data'() {
        setup:
            new TestHstore(testAttributes: data).save()

        when:
            def testHstore = TestHstore.findAll().first()

        then:
            testHstore.hasErrors() == false
            testHstore.testAttributes != null
            testHstore.testAttributes.size() == data.size()
            testHstore.testAttributes[key] == value

        where:
            data                     | key       | value
            [foo: "bar", xxx: "abc"] | "foo"     | "bar"
            ["foo,bar": "baz,qux"]   | "foo,bar" | "baz,qux"
    }

    @Unroll
    void 'remove a key in a map. key: #data, valueToRemove: #valueToRemove'() {
        setup:
            def testHstore = new TestHstore(testAttributes: data)
            testHstore.save()

        when:
            testHstore.testAttributes.remove(valueToRemove)

        then:
            testHstore.hasErrors() == false
            testHstore.testAttributes != null
            testHstore.testAttributes.size() == size

        where:
            data                     | valueToRemove | size
            [foo: "bar", xxx: "abc"] | 'xxx'         | 1
            ["foo,bar": "baz,qux"]   | 'foo,bar'     | 0
            [foo: "bar"]             | 'xxx'         | 1
    }

    @Unroll
    void 'save and delete a domain class with a map. key: #data'() {
        setup:
            def testHstore = new TestHstore(testAttributes: data)

        when: 'I save an instance'
            testHstore.save()

        and: 'The instance is saved'
            assert testHstore.hasErrors() == false

        and: 'I try to delete it'
            testHstore.delete()

        then: 'It shouldn\'t be present in database anymore'
            TestHstore.count() == 0

        where:
            data                   | attribute | value
            [foo: "bar"]           | "foo"     | "bar"
            ["foo,bar": "baz,qux"] | "foo,bar" | "baz,qux"
    }
}
