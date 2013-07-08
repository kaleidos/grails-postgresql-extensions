package net.kaleidos.hibernate

import grails.plugin.spock.*
import spock.lang.*

import test.array.TestInteger
import test.array.TestLong
import test.array.TestString

class PostgresqlArraysDomainIntegrationSpec extends IntegrationSpec {

    @Unroll
    void 'save a domain class with an integer array value'() {
        setup:
            def testInt = new TestInteger(integerNumbers:numbers)

        when:
            testInt.save()

        then:
            testInt.hasErrors() == false
            testInt.integerNumbers.length == numbers.size()

        where:
            numbers << [ [], [5], [3, -1], [-9, 4, -123, 0] ]
    }

    @Unroll
    void 'save a domain class with a long array value'() {
        setup:
            def testLong = new TestLong(longNumbers:numbers)

        when:
            testLong.save()

        then:
            testLong.hasErrors() == false
            testLong.longNumbers.length == numbers.size()

        where:
            numbers << [ [], [5L], [3L, -1L], [-9L, 4L, -123L, 0L] ]
    }

    @Unroll
    void 'save a domain class with an string array value'() {
        setup:
            def testString = new TestString(stringArray:strings)

        when:
            testString.save()

        then:
            testString.hasErrors() == false
            testString.stringArray.length == strings.size()

        where:
            strings << [ [], ["string 1"], ["string 1", "string 2"], ["string 1", "string 2", "string 3"] ]
    }

}