package test.array

import groovy.transform.ToString
import net.kaleidos.hibernate.usertype.ArrayType

@ToString
class TestInteger {

    Integer[] integerNumbers

    static mapping = {
        integerNumbers type: ArrayType, params: ["type": Integer]
    }

    static constraints = {
        integerNumbers nullable: true
    }

}
