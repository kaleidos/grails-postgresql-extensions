package test.array

import net.kaleidos.hibernate.usertype.ArrayType

class TestLong {

    Long[] longNumbers

    static mapping = {
        longNumbers type:ArrayType, params: [type: Long]
    }

    static constraints = {
        longNumbers nullable: true
    }
}
