package test.array

import net.kaleidos.hibernate.usertype.ArrayType

class TestString {

    String[] stringArray

    static mapping = {
        stringArray type:ArrayType, params: [type: String]
    }

    static constraints = {
        stringArray nullable:true
    }
}
