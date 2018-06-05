package test.array

import groovy.transform.ToString
import net.kaleidos.hibernate.usertype.ArrayType

@ToString
class TestUuid {

    UUID[] uuidArray

    static mapping = {
        uuidArray type: ArrayType, params: [type: UUID]
    }

    static constraints = {
        uuidArray nullable: true
    }

}
