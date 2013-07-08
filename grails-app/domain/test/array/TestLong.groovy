package test.array

import net.kaleidos.hibernate.usertype.LongArrayType

class TestLong {

    Long[] longNumbers

    static mapping = {
        longNumbers type:LongArrayType
    }
}
