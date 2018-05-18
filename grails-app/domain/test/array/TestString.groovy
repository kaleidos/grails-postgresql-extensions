package test.array

import groovy.transform.ToString
import net.kaleidos.hibernate.usertype.ArrayType

@ToString
class TestString {

    String[] stringArray

    static mapping = {
        stringArray type: ArrayType, params: [type: String]
    }

    static constraints = {
        stringArray nullable: true
    }

}
