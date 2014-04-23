package test.hstore

import net.kaleidos.hibernate.usertype.HstoreMapType

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


