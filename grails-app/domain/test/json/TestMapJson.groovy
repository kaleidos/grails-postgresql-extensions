package test.json

import net.kaleidos.hibernate.usertype.JsonMapType

class TestMapJson {

    Map data

    static constraints = {
        data nullable: true
    }
    static mapping = {
        data type: JsonMapType
    }
}