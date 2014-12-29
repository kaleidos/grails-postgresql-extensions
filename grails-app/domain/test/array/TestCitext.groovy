package test.array

import net.kaleidos.hibernate.usertype.ArrayType

class TestCitext {

    String[] citextArray

    static mapping = {
        citextArray type: ArrayType, params: [type: String, caseInsensitive: true]
    }

    static constraints = {
        citextArray nullable:true
    }
}