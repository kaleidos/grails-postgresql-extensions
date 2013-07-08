package test.array

import net.kaleidos.hibernate.usertype.IntegerArrayType

class TestInteger {

    Integer[] integerNumbers

    static mapping = {
        integerNumbers type:IntegerArrayType
    }
}
