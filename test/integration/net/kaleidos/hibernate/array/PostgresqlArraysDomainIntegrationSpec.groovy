package net.kaleidos.hibernate.array

import spock.lang.Specification
import spock.lang.Unroll
import test.array.*

class PostgresqlArraysDomainIntegrationSpec extends Specification {

    @Unroll
    void 'save a domain class with an integer array value #numbers'() {
        setup:
            def testInt = new TestInteger(integerNumbers: numbers)

        when:
            testInt.save(flush: true)

        then:
            testInt.hasErrors() == false
            testInt.integerNumbers?.length == numbers?.size()

        where:
            numbers << [null, [], [5], [3, -1], [-9, 4, -123, 0]]
    }

    @Unroll
    void 'save a domain class with a long array value #numbers'() {
        setup:
            def testLong = new TestLong(longNumbers: numbers)

        when:
            testLong.save(flush: true)

        then:
            testLong.hasErrors() == false
            testLong.longNumbers?.length == numbers?.size()

        where:
            numbers << [null, [], [5L], [3L, -1L], [-9L, 4L, -123L, 0L]]
    }

    @Unroll
    void 'save a domain class with a Float array value #numbers'() {
        setup:
            def testFloat = new TestFloat(floatNumbers: numbers)

        when:
            testFloat.save(flush: true)

        then:
            testFloat.hasErrors() == false
            testFloat.floatNumbers?.length == numbers?.size()

        where:
            numbers << [null, [], [5f], [3f, -1f], [-9f, 4f, -123f, 0f]]
    }

    @Unroll
    void 'save a domain class with a Double array value #numbers'() {
        setup:
            def testDouble = new TestDouble(doubleNumbers: numbers)

        when:
            testDouble.save(flush: true)

        then:
            testDouble.hasErrors() == false
            testDouble.doubleNumbers?.length == numbers?.size()

        where:
            numbers << [null, [], [5d], [3d, -1d], [-9d, 4d, -123d, 0d]]
    }

    @Unroll
    void 'save a domain class with an string array value #strings'() {
        setup:
            def testString = new TestString(stringArray: strings)

        when:
            testString.save(flush: true)

        then:
            testString.hasErrors() == false
            testString.stringArray?.length == strings?.size()

        where:
            strings << [null, [], ["string 1"], ["string 1", "string 2"], ["string 1", "string 2", "string 3"]]
    }

    @Unroll
    void 'save a domain class with an enum array value #days'() {
        setup:
            def testEnum = new TestEnum(days: days)

        when:
            testEnum.save(flush: true)

        then:
            testEnum.hasErrors() == false
            testEnum.days?.length == days?.size()

        where:
            days << [null, [], [TestEnum.Day.MONDAY], [TestEnum.Day.SUNDAY, TestEnum.Day.SATURDAY], [TestEnum.Day.WEDNESDAY, TestEnum.Day.THURSDAY, TestEnum.Day.TUESDAY]]
    }
}
