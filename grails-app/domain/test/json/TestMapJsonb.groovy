package test.json

import net.kaleidos.hibernate.usertype.JsonbMapType

class TestMapJsonb {

    Map data

    static constraints = {
        data nullable: true
    }
    static mapping = {
        data type: JsonbMapType
    }
}