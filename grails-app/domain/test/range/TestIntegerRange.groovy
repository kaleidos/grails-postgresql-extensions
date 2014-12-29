package test.range

import net.kaleidos.hibernate.postgresql.range.IntegerRange
import net.kaleidos.hibernate.usertype.RangeType

class TestIntegerRange {
    IntegerRange integerRange

    static mapping = {
        integerRange type: RangeType, params: ["type": IntegerRange]
    }

    static constraints = {
        integerRange nullable: true
    }
}
