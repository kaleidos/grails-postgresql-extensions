package net.kaleidos.hibernate.range

import net.kaleidos.hibernate.postgresql.range.DateRange
import net.kaleidos.hibernate.postgresql.range.IntegerRange
import net.kaleidos.hibernate.postgresql.range.LongRange
import net.kaleidos.hibernate.postgresql.range.TimestampRange
import spock.lang.Specification
import test.range.TestDateRange
import test.range.TestIntegerRange
import test.range.TestLongRange
import test.range.TestTimestampRange

class RangeDomainIntegrationSpec extends Specification {
    def setupSpec() {
        Integer.mixin(groovy.time.TimeCategory)
    }

    void 'Integer - Save a domain object with a range inside, then retrieve it'() {
        setup:
            def testRange = new TestIntegerRange(integerRange: range)

        when:
            testRange.save(flush: true)
            def result = TestIntegerRange.findById(testRange?.id)

        then:
            testRange != null
            testRange.hasErrors() == false
            result.integerRange.from == range.from
            result.integerRange.to == range.to
            result.integerRange.range.containsWithinBounds(50)

        where:
            range << [new IntegerRange(1, 100), new IntegerRange(100, 1)]
    }

    void 'Long - Save a domain object with a range inside, then retrieve it'() {
        setup:
            def testRange = new TestLongRange(longRange: range)

        when:
            testRange.save(flush: true)
            def result = TestLongRange.findById(testRange?.id)

        then:
            testRange != null
            testRange.hasErrors() == false
            result.longRange != null
            result.longRange.from != null
            result.longRange.from == range.from
            result.longRange.to != null
            result.longRange.to == range.to
            result.longRange.range.containsWithinBounds(50)

        where:
            range << [new LongRange(1L, 100L), new LongRange(100L, 1L)]
    }

    void 'Date - Save a domain object with a range inside, then retrieve it'() {
        setup:
            def testRange = new TestDateRange(dateRange: range)

        when:
            testRange.save(flush: true)
            def result = TestDateRange.findById(testRange?.id)

        then:
            testRange != null
            testRange.hasErrors() == false
            result.dateRange.from == range.from
            result.dateRange.to == range.to
            result.dateRange.range.containsWithinBounds(new Date())

        where:
            range << [new DateRange(5.days.ago, 5.days.from.now), new DateRange(5.days.from.now, 5.days.ago)]
    }

    void 'Timestamp - Save a domain object with a range inside, then retrieve it'() {
        setup:
            def testRange = new TestTimestampRange(timestampRange: range)

        when:
            testRange.save(flush: true)
            def result = TestTimestampRange.findById(testRange?.id)

        then:
            testRange != null
            testRange.hasErrors() == false
            result.timestampRange.from == range.from
            result.timestampRange.to == range.to
            result.timestampRange.range.containsWithinBounds(new Date())

        where:
            range << [new TimestampRange(10.hours.ago, 10.hours.from.now), new TimestampRange(10.hours.from.now, 10.hours.ago)]
    }

    void 'Check null ranges'() {
        when:
            def result = testDomain.save(flush: true)

        then:
            result != null

        where:
            testDomain << [
                new TestIntegerRange(integerRange: null),
                new TestLongRange(longRange: null),
                new TestDateRange(dateRange: null),
                new TestTimestampRange(timestampRange: null)
            ]
    }

}