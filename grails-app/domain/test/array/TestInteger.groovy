package test.array

import net.kaleidos.hibernate.usertype.ArrayType

class TestInteger {

    Integer[] integerNumbers

    static mapping = {
        integerNumbers type:ArrayType, params: ["type": Integer]
    }

    static constraints = {
        integerNumbers nullable: true
    }
}
