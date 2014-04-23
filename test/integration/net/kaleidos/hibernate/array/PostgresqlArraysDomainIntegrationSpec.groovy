package net.kaleidos.hibernate.array

import grails.plugin.spock.*
import spock.lang.*

import test.array.TestEnum
import test.array.TestInteger
import test.array.TestLong
import test.array.TestFloat
import test.array.TestDouble
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
    void 'save a domain class with a Float array value'() {
        setup:
            def testFloat = new TestFloat(floatNumbers:numbers)

        when:
            testFloat.save()

        then:
            testFloat.hasErrors() == false
            testFloat.floatNumbers.length == numbers.size()

        where:
            numbers << [ [], [5f], [3f, -1f], [-9f, 4f, -123f, 0f] ]
    }

    @Unroll
    void 'save a domain class with a Double array value'() {
        setup:
            def testDouble = new TestDouble(doubleNumbers:numbers)

        when:
            testDouble.save()

        then:
            testDouble.hasErrors() == false
            testDouble.doubleNumbers.length == numbers.size()

        where:
            numbers << [ [], [5d], [3d, -1d], [-9d, 4d, -123d, 0d] ]
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

    @Unroll
    void 'save a domain class with an enum array value'() {
        setup:
            def testEnum = new TestEnum(days: days)

        when:
            testEnum.save()

        then:
            testEnum.hasErrors() == false
            testEnum.days.length == days.size()

        where:
            days << [ [], [TestEnum.Day.MONDAY], [TestEnum.Day.SUNDAY, TestEnum.Day.SATURDAY], [TestEnum.Day.WEDNESDAY, TestEnum.Day.THURSDAY, TestEnum.Day.TUESDAY] ]
    }

}
