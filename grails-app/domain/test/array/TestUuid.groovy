package test.array

import net.kaleidos.hibernate.usertype.ArrayType

class TestUuid {

    UUID[] uuidArray

    static mapping = {
        uuidArray type:ArrayType, params: [type: UUID]
    }

    static constraints = {
        uuidArray nullable:true
    }
}
