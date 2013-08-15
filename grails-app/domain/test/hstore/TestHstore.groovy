package test.hstore

import net.kaleidos.hibernate.postgresql.Hstore
import net.kaleidos.hibernate.usertype.HstoreType

class TestHstore {

    //Map<String, String> contactMethods = new HashMap<String, String>();
    Hstore doc //= new Hstore()
    //Hstore doc2
    String kk
    Map<String, String> doc2
    String asd

    
    Map<String, String> doc3
    Map<String, String> doc4
    
    static constrains = {
        doc2 nullable:true
    }

    static mapping = {
        doc type:HstoreType
        doc2 type:HstoreType
        
        doc3 type:HstoreType
    }
}
