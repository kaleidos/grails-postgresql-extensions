package test.json

import net.kaleidos.hibernate.usertype.JsonMapType

class TestMapJson {

    Map data

    static constraints = {
    }
    static mapping = {
        data type: JsonMapType
    }
}