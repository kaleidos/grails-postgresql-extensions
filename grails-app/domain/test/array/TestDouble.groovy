package test.array

import groovy.transform.ToString
import net.kaleidos.hibernate.usertype.ArrayType

@ToString
class TestDouble {

    Double[] doubleNumbers

    static mapping = {
        doubleNumbers type: ArrayType, params: [type: Double]
    }

    static constraints = {
        doubleNumbers nullable: true
    }

}
