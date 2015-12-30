package test.json

import net.kaleidos.hibernate.usertype.JsonListType
import test.Address

class TestListJson {

    String name
    List<Address> addresses

    static mapping = {
        addresses type: JsonListType
    }
}
