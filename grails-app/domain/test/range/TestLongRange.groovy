package test.range

import net.kaleidos.hibernate.postgresql.range.LongRange
import net.kaleidos.hibernate.usertype.RangeType

class TestLongRange {
    LongRange longRange

    static mapping = {
        longRange type:RangeType, params: ["type": LongRange]
    }

    static constraints = {
        longRange nullable: true
    }
}
