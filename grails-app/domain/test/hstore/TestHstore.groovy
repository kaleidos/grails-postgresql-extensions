package test.hstore

import net.kaleidos.hibernate.postgresql.hstore.Hstore
import net.kaleidos.hibernate.usertype.HstoreType

class TestHstore {
    String name
    Integer luckyNumber

    @Hstore
    Map testAttributes

    static constraints = {
        name nullable: true
        luckyNumber nullable: true
    }
    static mapping = {
        testAttributes type:HstoreType
    }
}


