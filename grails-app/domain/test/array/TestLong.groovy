package test.array

import groovy.transform.ToString
import net.kaleidos.hibernate.usertype.ArrayType

@ToString
class TestLong {

    Long[] longNumbers

    static mapping = {
        longNumbers type: ArrayType, params: [type: Long]
    }

    static constraints = {
        longNumbers nullable: true
    }

}
