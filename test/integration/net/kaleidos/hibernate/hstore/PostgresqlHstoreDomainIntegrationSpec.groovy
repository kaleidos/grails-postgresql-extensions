package net.kaleidos.hibernate.hstore

import grails.plugin.spock.*
import spock.lang.*

import test.hstore.TestHstore
import net.kaleidos.hibernate.postgresql.hstore.HstoreDomainType

class PostgresqlHstoreDomainIntegrationSpec extends IntegrationSpec {

    void 'save a domain class with a map'() {
        setup:
            def testHstore = new TestHstore(testAttributes: data)

        when:
            testHstore.save()
            println testHstore.errors

        then:
            testHstore.hasErrors() == false
            testHstore.testAttributes != null
            testHstore.testAttributes.size() == data.size()
            testHstore.testAttributes.foo == "bar"

        where:
            data = [foo:"bar"]
    }

    void 'recover a domain class with a map'() {
        setup:
            new TestHstore(testAttributes: data).save()

        when:
            def testHstore = TestHstore.findAll().first()

        then:
            testHstore.hasErrors() == false
            testHstore.testAttributes != null
            testHstore.testAttributes.size() == data.size()
            testHstore.testAttributes.foo == "bar"
            testHstore.testAttributes.xxx == "abc"

        where:
            data = [foo:"bar", xxx:"abc"]
    }

    void 'remove a key in a map'() {
        setup:
            def testHstore = new TestHstore(testAttributes: data)
            testHstore.save()

        when:
            testHstore.testAttributes.remove('xxx')

        then:
            testHstore.hasErrors() == false
            testHstore.testAttributes != null
            testHstore.testAttributes.size() == 1

        where:
            data = [foo:"bar", xxx:"abc"]
    }
}
