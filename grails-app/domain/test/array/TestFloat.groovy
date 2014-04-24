package test.array

import net.kaleidos.hibernate.usertype.ArrayType

class TestFloat {

    Float[] floatNumbers

    static mapping = {
        floatNumbers type:ArrayType, params: [type: Float]
    }

    static constraints = {
        floatNumbers nullable:true
    }
}
