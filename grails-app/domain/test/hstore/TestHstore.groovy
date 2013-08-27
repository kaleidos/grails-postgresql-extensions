package test.hstore

import net.kaleidos.hibernate.postgresql.hstore.Hstore
import net.kaleidos.hibernate.usertype.HstoreType

class TestHstore {

    @Hstore
    Map testAttributes

    String anotherProperty

    static constrains = {
    }

    static mapping = {
        testAttributes type:HstoreType
    }
}
