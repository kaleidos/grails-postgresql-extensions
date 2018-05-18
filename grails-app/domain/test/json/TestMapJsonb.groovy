package test.json

import groovy.transform.ToString
import net.kaleidos.hibernate.usertype.JsonbMapType

@ToString
class TestMapJsonb {

    Map data

    static constraints = {
        data nullable: true
    }

    static mapping = {
        data type: JsonbMapType
    }

}