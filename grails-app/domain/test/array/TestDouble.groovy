package test.array

import net.kaleidos.hibernate.usertype.ArrayType

class TestDouble {

    Double[] doubleNumbers

    static mapping = {
        doubleNumbers type:ArrayType, params: [type: Double]
    }

    static constraints = {
        doubleNumbers nullable:true
    }
}
