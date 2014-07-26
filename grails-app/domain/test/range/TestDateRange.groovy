package test.range

import net.kaleidos.hibernate.postgresql.range.DateRange
import net.kaleidos.hibernate.usertype.RangeType

class TestDateRange {
    DateRange dateRange

    static mapping = {
        dateRange type: RangeType, params: ["type": DateRange]
    }

    static constraints = {
        dateRange nullable: true
    }
}
