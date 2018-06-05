package test.hstore

import groovy.transform.ToString
import net.kaleidos.hibernate.usertype.HstoreMapType

@ToString
class TestHstoreMap {

    String name
    Integer luckyNumber

    Map testAttributes

    static constraints = {
        name nullable: true
        luckyNumber nullable: true
    }

    static mapping = {
        testAttributes type: HstoreMapType
    }

}


