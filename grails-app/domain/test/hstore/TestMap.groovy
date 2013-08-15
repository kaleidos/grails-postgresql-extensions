package test.hstore

import net.kaleidos.hibernate.postgresql.Hstore
import net.kaleidos.hibernate.usertype.HstoreType

class TestMap {

    // v1
    //Hstore hstore
    
    // v2
    Hstore hstore = new Hstore()
    
    static constrains = {
    }

    static mapping = {
        hstore type:HstoreType
    }
}
