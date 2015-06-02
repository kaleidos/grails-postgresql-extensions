package test.json

import net.kaleidos.hibernate.usertype.JsonbMapType

class TestMapJsonb {

    Map data

    static constraints = {
    }

    static mapping = {
        data type: JsonbMapType
    }
}