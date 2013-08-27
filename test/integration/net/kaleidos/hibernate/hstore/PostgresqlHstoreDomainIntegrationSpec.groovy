package net.kaleidos.hibernate.hstore

import grails.plugin.spock.*
import spock.lang.*

import test.hstore.TestHstore
import net.kaleidos.hibernate.postgresql.hstore.HstoreDomainType

class PostgresqlHstoreDomainIntegrationSpec extends IntegrationSpec {

    void 'save a domain class with a map'() {
        setup:
            def testHstore = new TestHstore(testAttributes: data, anotherProperty: someValue)

        when:
            testHstore.save()

        then:
            testHstore.hasErrors() == false
            testHstore.testAttributes != null
            testHstore.testAttributes.size() == data.size()
            testHstore.testAttributes.foo == "bar"
            testHstore.anotherProperty == someValue

        where:
            data = [foo:"bar"]
            someValue = "some value"
    }
}
