package test.json

import groovy.transform.ToString
import net.kaleidos.hibernate.usertype.JsonMapType

@ToString
class TestMapJson {

    Map data

    static constraints = {
        data nullable: true
    }

    static mapping = {
        data type: JsonMapType
    }
}