package test.array

import groovy.transform.ToString
import net.kaleidos.hibernate.usertype.ArrayType


@ToString
class TestFloat {

    Float[] floatNumbers

    static mapping = {
        floatNumbers type: ArrayType, params: [type: Float]
    }

    static constraints = {
        floatNumbers nullable: true
    }

}
