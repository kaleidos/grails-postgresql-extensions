package test.range

import net.kaleidos.hibernate.postgresql.range.TimestampRange
import net.kaleidos.hibernate.usertype.RangeType

class TestTimestampRange {
    TimestampRange timestampRange

    static mapping = {
        timestampRange type:RangeType, params: ["type": TimestampRange]
    }

    static constraints = {
        timestampRange nullable: true
    }
}
